package GDB

class ExonInfo {

    String mrna_id
    String contig_id    
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
        contig_id(blank:false)
        start(blank:false)
        stop(blank:false)
    }
    static mapping = {
        sequence  type: "text"
    }
}
