package badger

class GenomeData {
    String gbrowse
    Date dateString
    String gversion
      
    static hasMany = [files: FileData]
    static belongsTo = [meta: MetaData]
}
