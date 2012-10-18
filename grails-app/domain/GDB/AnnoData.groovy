package GDB

class AnnoData {
	int data_id
	int file_id
	String anno_file
    String type
    String source
    String link
    String regex
    static constraints = {
        anno_file(blank:false, unique: true)
    }
}
