package badger

class GenomeInfo {
    String contig_id
    Float gc
    Float coverage
    int length
    static constraints = {
        contig_id(blank:false)
        gc(blank:false)
        coverage(blank:false)
        length(blank:false)
    }
    static belongsTo = [ file: FileData ]
}
