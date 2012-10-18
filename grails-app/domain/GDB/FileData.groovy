package GDB

class FileData {
	int data_id
	int file_id
    String file_type
    String file_dir
    String file_name
    String blast
    String file_version
    String description
    String cov
    String search
    String download
    static constraints = {
        file_id(blank:false, unique: true)
    }
}
