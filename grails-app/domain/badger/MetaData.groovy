package badger

class MetaData {
    String genus
    String species
    String description
    String image_file
    String image_source
    static constraints = {
        genus(blank:false)
        species(blank:false)
        description(blank:false)
    }
    static mapping = {
        description type: "text"
    }    
    static hasMany = [genome: GenomeData, pubs: Publication]
}
