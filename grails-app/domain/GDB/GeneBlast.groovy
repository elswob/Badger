package GDB

class GeneBlast {
    String anno_db
    String anno_id
    int hit_start
    int hit_stop
    int identity
    int positive
    int gaps
    int align
    int file_id
    String qseq
    String hseq
    String midline
    int anno_start
    int anno_stop
    float score 
    String descr
    static constraints = {
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
