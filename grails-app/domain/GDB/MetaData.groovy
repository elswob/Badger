package GDB

class MetaData {
	int data_id
    String genus
    String species
    String description
    static constraints = {
        data_id(blank:false, unique: true)
    }
    static mapping = {
        description type: "text"
    }
}
