import os
import xml.etree.ElementTree as ET
from data import *
from xml_builder import *
"""
Script scans clusters and outputs them in a Swift static variable definition format 
- Each cluster is an xml file that ends with -cluster.xml
- The cluster information is found in the <cluster> block
  - The cluster String name is in <name>  (this isn't used)
  - The cluster define name is in <define> (this is the name that is used in the output format) 
  - The cluster Hex ID is in <code> (this is used)
"""

DATA_MODEL_PATH = "/Volumes/1tbssd/GalMatter/connectedhomeip/src/app/zap-templates/zcl/data-model"
ALL_INFO = "all.xml"
CLUSTERS = ElementDictionary()
DEVICES = ElementDictionary(multi=True) # stores for each id, a set of devices with that id
UNFOUND_CLUSTERS : dict[str:List[str]] = {} # for each not found cluster, which devices referenced it
UNFOUND_CLUSTER_ATTRIBS : dict[str, List[str]] = {} # cluster attributues which were referenced by a device but not found in the cluster

def outputSwiftData():
    """
    print("///////////////////////////////////////")
    print("////////  Matter Clusters  ///////////")
    print("///////////////////////////////////////")
    clusters = dict(sorted(CLUSTERS.items(), key=lambda item: item[0]))
    for key in clusters.keys():
        cluster = clusters[key]
        print("public static let %s = n(%s)" %(cluster.definition, cluster.uid))  
    print("public static let TYPES = [%s]" % (", ".join([cluster.definition for cluster in clusters.values()])))

    print("//////////////////////////////////////")
    print("////////  Matter Devices  //////////")
    print("//////////////////////////////////////")

    devices = dict(sorted(DEVICES.items(), key=lambda items: items[0]))
    for device in devices.values():
        print("public static let %s = n(%s)" %(device.definition, device.uid))  
    print("public static let TYPES = [%s]" % (", ".join([device.definition for device in devices.values()])))
    """
    pass


def extractData():
    path = os.path.join(DATA_MODEL_PATH, ALL_INFO)
    print(path)
    with open(path) as file:
        xmlTree = ET.parse(file)
        root = xmlTree.getroot()
        paths = [os.path.join(DATA_MODEL_PATH, el.attrib['href']) for el in root.findall("{http://www.w3.org/2001/XInclude}include")]
        for path in paths:
            # first extract clusters
            extract_clusters_from_file(path)
        for path in paths:
            extract_devices_from_file(path)

def extract_clusters_from_file(path):
    with open(path) as file:
        xmlTree = ET.parse(file)
        root = xmlTree.getroot()

        for cluster in root.findall('cluster'):
            definition = cluster.find('define')
            name = cluster.find('name').text
            definition = "MTR_%s" % definition.text.replace("_CLUSTER", "")
            uidHex = cluster.find('code').text
            uidint = int(uidHex, 16)
            if CLUSTERS.contains_id(uidint):
                print("WARNING DUPLICATE: %s, %s!! Skipping..." % (uidHex, definition))
                continue
            galCluster = Cluster(uidint, uidHex, definition, name)
            attributes = cluster.findall("attribute")
            for attr in attributes:
                is_optional = False
                try:
                    is_optional = bool(attr.attrib['optional'].lower())
                except:
                    pass
                attr_definition = attr.attrib['define']
                side = attr.attrib['side']
                galCluster.add_attribute(ClusterAttribute(attr_definition, is_optional, side.lower()))

            CLUSTERS.add_new(galCluster)


def extract_devices_from_file(path):
    with open(path) as file:
        xmlTree = ET.parse(file)
        root = xmlTree.getroot()
        for deviceTree in root.findall('deviceType'):
            uidHex = deviceTree.find('deviceId').text
            uidint = int(uidHex, 16)
            name = deviceTree.find("typeName").text
            definition = name.replace(" ", "_").replace("-","_").replace("/","_").replace("Matter", "MTR").upper()

            galDevice = Device(uidint, uidHex, definition, name)

            for included in deviceTree.find("clusters").findall("include"):
                if not "cluster" in included.attrib:
                    if included.text == None or len(included.text) == 0: ## FOR HA Devices, the cluster name is in the textual content
                        print("Device", name, "includes non cluster", included.attrib)
                        continue
                    cluster_name = included.text
                else:
                    cluster_name = included.attrib["cluster"]

                if cluster_name in UNFOUND_CLUSTERS: # if already unfound, skip
                    UNFOUND_CLUSTERS[cluster_name].append(name) # a[[emd tje devoce cluster
                    continue

                actual_cluster : Cluster = CLUSTERS.get_by_name(cluster_name)
                if actual_cluster == None:
                   # print("WARNING: CLUSTER %s NOT FOUND. Skipping..." % (cluster_name))
                    UNFOUND_CLUSTERS[cluster_name] = [name] # show cluster as not found
                    continue
                device_cluster = DeviceCluster(actual_cluster)
                for attr in included.findall("requireAttribute"):
                    attr_definition = attr.text
                    cluster_attribute = actual_cluster.get_attr_from_definition(attr_definition)
                    if cluster_attribute == None:
                        if actual_cluster.definition in UNFOUND_CLUSTER_ATTRIBS:
                            if not attr_definition in UNFOUND_CLUSTER_ATTRIBS[actual_cluster.definition]:
                                UNFOUND_CLUSTER_ATTRIBS[actual_cluster.definition].append(attr_definition)
                                #print("\tWARNING: Attribute %s NOT FOUND. Skipping..." % (attr_definition))
                        else:
                            UNFOUND_CLUSTER_ATTRIBS[actual_cluster.definition] = [attr_definition]    
                        continue
                    device_cluster.add_required_attrib(cluster_attribute)
                galDevice.addCluster(device_cluster)
            DEVICES.add_new(galDevice)



class ReplaceFromClustersToDevice:
    def __init__(self, for_device: Device, from_clusters: List[Cluster], to_device: Device):
        self.for_device = for_device
        self.from_clusters = from_clusters
        self.to_device = to_device

def find_device_repalceable_clusters() -> List[ReplaceFromClustersToDevice]:
    ### Some device types have optional clusters which are equivelant to other clusters.
    ### For example, the "On/Off Light" device type has an optional "Occupancy Sensing" cluster
    ### which is equivelant to the "Occupancy Sensor device type."
    ### This script was written to determine if that recurs.
    IGNORE_CLUSTERS  = ["MTR_ELECTRICAL_MEASUREMENT", "MTR_BASIC", "MTR_POWER_CONFIGURATION", "MTR_DESCRIPTOR", "MTR_IDENTIFY", "MTR_GROUPS", "MTR_SCENES", "MTR_OTA_PROVIDER"]
    sorted_devices : List[Device] = DEVICES.get_sorted_item_list()
    repalceable_devices = []
    for index in range(len(sorted_devices)-1): # last device would have been checked by all devices before it 
        device = sorted_devices[index]
        #print(device.definition)
        optional_clusters = [dcluster.cluster for dcluster in device.get_optional_clusters() if dcluster.cluster.definition not in IGNORE_CLUSTERS]
        for other_device in sorted_devices[index+1:]:
            dt = other_device.has_required_clusters_equals_other_clusters_ingoring([cluster for cluster in optional_clusters], IGNORE_CLUSTERS)
            if dt is not None:
                #print("\t - Device %s has optional clusters which can be can be replaced by %s" % (device.definition, other_device.definition))
                repalceable_devices.append(ReplaceFromClustersToDevice(device, dt, other_device ))
    return repalceable_devices

def run():
    extractData()
   # outputXmlData("mtr_clusters.xml", "mtr_devices.xml", "not_found_clusters.txt", "not_found_attrs.txt")
    ClusterXMLBuilder(CLUSTERS).buildToFile("out/mtr_clusters.xml")
    DeviceXMLBuilder(DEVICES).buildToFile("out/mtr_devices.xml")

    with open("out/not_found_clusters.txt", "w+") as file:
        for cluster in UNFOUND_CLUSTERS:
            file.write(cluster + ":\n - Not found for the following devices \n - " + "\n - ".join(UNFOUND_CLUSTERS[cluster]) + "\n\n")

    with open("out/not_found_attrs.txt", "w+") as file:
        for cluster in UNFOUND_CLUSTER_ATTRIBS:
            file.write(cluster + ":\n - " + "\n - ".join(UNFOUND_CLUSTER_ATTRIBS[cluster]) + "\n\n")

    with open("out/clusters_to_devicetypes.txt", "w+") as file:
        for replaceable in find_device_repalceable_clusters():
            file.write("FOR: %s\n" % replaceable.for_device.definition)
            file.write("\tReplaceable Optional clusters: \n\t\t - %s\n" % "\n\t\t - ".join([cluster.definition for cluster in replaceable.from_clusters]))
            file.write("To Device: %s\n\n" % replaceable.to_device.definition)
                 
    
    #print(len(CLUSTERS.id_dict))

if __name__ == '__main__':
    run()


