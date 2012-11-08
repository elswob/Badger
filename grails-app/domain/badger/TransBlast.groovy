package badger

class TransBlast {
    String contig_id
    String anno_db
    String anno_id
    int hit_start
    int hit_stop
    int identity
    int positive
    int gaps
    int align
    String qseq
    String hseq
    String midline
    int anno_start
    int anno_stop
    float score 
    String descr
    int file_id
    static constraints = {
        contig_id(blank:false)
        anno_db(blank:false)
        anno_id(blank:false)
        anno_start(blank:false)
        anno_stop(blank:false)
        score(blank:false)
        descr(blank:false)
    }
    static mapping = {
        descr type: "text"
        qseq type: "text"
        hseq type: "text"
        midline type: "text"
    }
}
