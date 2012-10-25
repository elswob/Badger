package GDB

class ExonInfo {    
    int start
    int stop
    int phase
    String exon_id
    float score
    int exon_number
    float gc
    String sequence
    String strand
    static constraints = {
        exon_id(blank:false, unique: true)
        start(blank:false)
        stop(blank:false)
    }
    static mapping = {
        sequence  type: "text"
    }
    static belongsTo = [ gene: GeneInfo ]
}
