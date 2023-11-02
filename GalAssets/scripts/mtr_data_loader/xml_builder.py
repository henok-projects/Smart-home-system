
from data import *

class XMLBuilder:
    XML_DATA_FORMAT = "<!-- GENERATED FROM SCRIPT --> \n\n <%s>\n%s\n</%s>" # (root open tag, data, root close tag)
    DATA_POINT_FORMAT = """
        <%s>%s
        </%s>
    """
    def __init__(self, root_tag_name, element_tag_name, data_point_data_format):
        self.root_tag_name = root_tag_name
        self.element_tag_name = element_tag_name 

        self.xml_data_format = XMLBuilder.XML_DATA_FORMAT % (root_tag_name, "%s", root_tag_name)
        self.data_point_format = XMLBuilder.DATA_POINT_FORMAT % (element_tag_name, data_point_data_format, element_tag_name)

    def build(self) -> str:
        pass

    def buildToFile(self, path: str):
        with open(path, "w+") as f:
            f.write(self.build())

class ClusterXMLBuilder(XMLBuilder):
    CLUSTER_ATTR_FORMAT = "\n\t\t\t\t\t<attribute definition=\"%s\" side=\"%s\"/>"

    def __init__(self, clusters: ElementDictionary):
        super().__init__("mtrClusters", "type", """
            <id>%s</id>
            <hexId>%s</hexId>
            <definition>%s</definition>
            <name>%s</name>
            <mtrAttributes>
                <required>%s
                </required>

                <optional>%s
                </optional>
            </mtrAttributes>""")
        self.clusters = clusters
    
    def build(self) -> str:
        clusters = self.clusters.get_sorted_by_id()
        return self.xml_data_format %  "".join([self.data_point_format % (data.intId, data.hexId, data.definition, data.name, self.build_attributes(data, optional=False), self.build_attributes(data, optional=True)) for data in clusters.values()])

    
    def build_attributes(self, cluster: Cluster, optional=False):
        return  "".join([ClusterXMLBuilder.CLUSTER_ATTR_FORMAT % (attr.definition, attr.side) for attr in (cluster.get_required_attributes() if not optional else cluster.get_optional_attributes())])



class DeviceXMLBuilder(XMLBuilder):
    DEVICE_CLUSTER_FORMAT = "\n\t\t\t\t\t<cluster definition=\"%s\">%s</cluster>"
    DEVICE_REQ_ATTR_FORMAT = "\n\t\t\t\t\t\t<requiredAttribute definition=\"%s\"/>"
    
    def __init__(self, devices: ElementDictionary):
        super().__init__("mtrDevices", "type", """
            <id>%d</id> <!-- The unique id given by galsie -->
            <defaultId>%s</defaultId> <!-- The default id given by the matter model-->
            <defaultIdHex>%s</defaultIdHex>
            <definition>%s</definition>
            <name>%s</name>

            <clusters>
                <required>%s
                </required>

                <optional>%s
                </optional>
            </clusters>""")
        self.devices = devices

    def build(self) -> str:
        device_xml_sub_data = ""
        counter = 0
        devices = self.devices.get_sorted_by_id()
        for uid, device_set in devices.items():
            device_count = len(device_set)
            device_xml_sub_data += "\n<!-- IDS: %d -> %d  |  defaultID %d |  %d device%s -->\n" % (counter, counter+device_count-1,uid, device_count, "s" if device_count > 1 else "") + "".join([self.data_point_format % 
                            (device_set.index(data)+counter,data.intId, data.hexId, data.definition, data.name, 
                            self.build_clusters(data, False), self.build_clusters(data, True))
                            for data in device_set])
            counter += device_count
        return self.xml_data_format % device_xml_sub_data

    def build_clusters(self, device: Device, optional=False):
        clusters = device.get_optional_clusters() if optional else device.get_required_clusters()
        return "".join([DeviceXMLBuilder.DEVICE_CLUSTER_FORMAT % (device_cluster.cluster.definition, self.build_required_attributes(device_cluster)) for device_cluster in clusters])

    def build_required_attributes(self, device_cluster: DeviceCluster):
        attribs = device_cluster.get_required_attributes()
        if len(attribs) == 0:
            return "<!-- No Device specific required Attributes -->"
        return "".join([DeviceXMLBuilder.DEVICE_REQ_ATTR_FORMAT % req_attrib.definition for req_attrib in attribs])
