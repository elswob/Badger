package badger

class GenomeData {
    String gbrowse
    Date dateString
      
    static hasMany = [files: FileData]
    static belongsTo = [meta: MetaData]
}
