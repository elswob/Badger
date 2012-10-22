package GDB

class TransAnno {
    String contig_id
    String anno_db
    String anno_id
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
    }
}
