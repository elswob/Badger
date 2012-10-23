package GDB

class MetaData {
	int data_id
    String genus
    String species
    String description
    String image_file
    String image_source
    static constraints = {
        data_id(blank:false, unique: true)
    }
    static mapping = {
        description type: "text"
    }    
    static hasMany = [files: FileData]
}
