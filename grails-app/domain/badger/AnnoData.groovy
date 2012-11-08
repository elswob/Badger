package badger

class AnnoData {
	String anno_file
    String type
    String source
    String link
    String regex
    Boolean loaded
    
    static constraints = {
        loaded(blank:false)
    }
    
    static belongsTo = [ filedata: FileData ]
}
