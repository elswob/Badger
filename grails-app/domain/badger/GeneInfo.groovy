package badger

class GeneInfo {
    String gene_id
    String mrna_id
    String source
    String contig_id    
    int start
    int stop
    String nuc
    String pep
    float gc
    String strand
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
    static hasMany = [exon:ExonInfo, gblast:GeneBlast, ganno: GeneAnno]
    static belongsTo = [ file: FileData ]
}
