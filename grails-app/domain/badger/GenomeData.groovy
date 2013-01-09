package badger

class GenomeData {
    String description
    String Gversion
    String gbrowse
    Date dateString
    static constraints = {
        description(blank:false)
    }
    static mapping = {
        description type: "text"
    }    
    static hasMany = [files: FileData]
    static belongsTo = [meta: MetaData]
}
