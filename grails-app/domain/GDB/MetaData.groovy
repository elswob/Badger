package GDB

class MetaData {
    String genus
    String species
    String description
    String image_file
    String image_source
    String gbrowse
    static constraints = {
        genus(blank:false)
        species(blank:false)
        description(blank:false)
    }
    static mapping = {
        description type: "text"
    }    
    static hasMany = [files: FileData, pubs: Publication]
}
