package badger

class ExtInfo {
    String ext_id
    String link
    String regex
    String ext_source
    
    static constraints = {
        ext_id(blank:false, unique: true)
        link(blank:false)
        regex(blank:false)
        ext_source(blank:false)
    }
}
