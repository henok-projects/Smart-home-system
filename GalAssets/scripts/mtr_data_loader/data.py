from typing import List, Optional


class CommonCD:
    def __init__(self, intId, hexId, definition, name):
        self.intId = intId
        self.hexId = hexId
        self.definition = definition
        self.name = name


class ElementDictionary:

    # if multi is set, stores for each id and definition, a list of objects
    def __init__(self, multi: bool = False):
        # Don't care about memory usage, do this for practicality and speed fetching for different attributes
        self.id_dict : dict[int, CommonCD | [CommonCD]] = {} # dictionary mapping an int to a some object
        self.multi = multi

    def contains_id(self, id: int):
        return id in self.id_dict

    def contains_definition(self, definition: str):
        return definition in self.definition

    def add_new(self, object: CommonCD):
        id = object.intId
        if not self.contains_id(object.intId) or not self.multi: # if its not self.multi, overrides
            self.id_dict[id] = [object] if self.multi else object
            return
        self.id_dict[id].append(object)

    def get_by_id(self, id: int) -> 'CommonCD | List[CommonCD] | None':
        return self.id_dict[id] if id in self.id_dict else None

    def get_by_name(self, name: str, ignoreCase=True) -> 'CommonCD | List[CommonCD] | None':
        data = []
        check = lambda nme: name == nme if not ignoreCase else name.lower() == nme.lower()
        if not self.multi:
            data.extend([item for item in self.id_dict.values() if check(item.name)])
        else:
            for list in self.id_dict.values():
                data.extend([item for item in list if check(item.name)] )
        return None if len(data) == 0 else (data[0] if not self.multi else data)

    def get_by_definition(self, definition: str) -> 'CommonCD | List[CommonCD] | None':
        if not self.multi:
            data = [item for item in self.id_dict.values() if item.definition == definition] 
        else:
            data = []
            for list in self.id_dict.values():
                data.extend([item for item in list if item.definition == definition] ) 
        return None if len(data) == 0 else (data[0] if not self.multi else data)

    def get_sorted_by_id(self) -> 'CommonCD | List[CommonCD] | None':
        return dict(sorted(self.id_dict.items(), key=lambda item: item[0]))
    
    def get_sorted_item_list(self) -> List[CommonCD]:
        sorted = self.get_sorted_by_id()
        if not self.multi:
            return list(sorted.values())
        data = []
        for list in sorted.values():
            data.extend(list)
        return data
class ClusterAttribute:
    SIDE_SERVER = "server"
    SIDE_CLIENT = "client"
    def __init__(self, definition: str, is_optional: bool, side):
        self.definition = definition
        self.optional = is_optional
        self.side = side

class Cluster(CommonCD):
    def __init__(self, intId, hexId, definition, name):
        self.attributes : List[ClusterAttribute] = []
        super().__init__(intId, hexId, definition, name)

    def add_attribute(self, attr: ClusterAttribute):
        self.attributes.append(attr)

    def get_required_attributes(self):
        return [attr for attr in self.attributes if not attr.optional]

    def get_optional_attributes(self):
        return [attr for attr in self.attributes if attr.optional]

    def get_attr_from_definition(self, attr_definition) -> Optional[ClusterAttribute]:
        found = [attr for attr in self.attributes if attr.definition == attr_definition]
        if len(found) == 0:
            return None
        return found[0]



class DeviceCluster:
    # cluster_int_id is that of a cluster above
    def __init__(self, cluster: Cluster):
        self.cluster = cluster
        self.required_attribs: List[ClusterAttribute] = [] # optional attributes in the cluster, but required for the device cluster

    def add_required_attrib(self, attrib: ClusterAttribute):
        self.required_attribs.append(attrib)

    def is_required(self) -> bool: # a DeviceCluster is required if it has at least one required_attribs in the DeviceCluster or at least one non-optional attribute in the Cluster
        return len(self.required_attribs) > 0 or len(self.cluster.get_required_attributes()) > 0

    def get_required_attributes(self) -> List[ClusterAttribute]:
        return self.required_attribs
class Device(CommonCD):
    def __init__(self, intId, hexId, definition, name):
        super().__init__(intId, hexId, definition, name)
        self.clusters : List[DeviceCluster] = []
    
    def addCluster(self, device_cluster: DeviceCluster):
        self.clusters.append(device_cluster)

    def getClusters(self):
        return self.clusters

    def has_required_clusters_equals_other_clusters_ingoring(self, clusters: List[Cluster], ignoring: List[str]):
        required_clusters = [dcluster.cluster for dcluster in self.get_required_clusters() if dcluster.cluster.definition not in ignoring]
        check = [required_cluster in clusters for required_cluster in required_clusters] ## iff all the required clusters are contained within the optional, theres am match
        return required_clusters if len(check) > 0 and all(check) else None

    def get_required_clusters(self) -> List[DeviceCluster]:
        return [cluster for cluster in self.clusters if cluster.is_required()]

    def get_optional_clusters(self) -> List[DeviceCluster]:
        return [cluster for cluster in self.clusters if not cluster.is_required()]
