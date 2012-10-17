package GDB

class MetaData {
	int data_id
    String genus
    String species
    String data_version
    String description
    static constraints = {
        data_id(blank:false, unique: true)
    }
}
