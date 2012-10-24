package GDB

class GeneAnno {
    String anno_db
    String anno_id
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
    }
}
