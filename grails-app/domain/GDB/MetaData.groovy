package GDB

class MetaData {
    String genus
    String species
    String description
    String image_file
    String image_source

    static mapping = {
        description type: "text"
    }    
    static hasMany = [files: FileData]
}
