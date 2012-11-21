package badger

class ExonInfo {    
    int start
    int stop
    int phase
    float score
    int exon_number
    float gc
    String sequence
    String strand
    static constraints = {
        start(blank:false)
        stop(blank:false)
    }
    static mapping = {
        sequence  type: "text"
    }
    static belongsTo = [ gene: GeneInfo ]
}
