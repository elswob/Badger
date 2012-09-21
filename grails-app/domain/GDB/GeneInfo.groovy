package GDB

class GeneInfo {

    String gene_id
    String source
    String contig_id    
    int start
    int stop
    String nuc
    String pep
    static constraints = {
        gene_id(blank:false, unique: true)
        source(blank:false)
        contig_id(blank:false)
        start(blank:false)
        stop(blank:false)
    }
    static mapping = {
        nuc type: "text"
        pep type: "text"
    }
}
