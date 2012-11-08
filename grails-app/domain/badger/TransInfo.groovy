package badger

class TransInfo {
    String contig_id
    String sequence
    Float gc
    Float coverage
    int length
    static constraints = {
        contig_id(blank:false,unique: true)
        sequence(blank:false)
        gc(blank:false)
        coverage(blank:false)
        length(blank:false)
    }
    static mapping = {
        sequence type: "text"
        contig_id index:'trans_contig'
    }
    static belongsTo = [ file: FileData ]
}
