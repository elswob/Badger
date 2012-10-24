package GDB

class AnnoData {
	String anno_file
    String type
    String source
    String link
    String regex
    
    static belongsTo = [ filedata: FileData ]
}
