package GDB

class TransInfo {
	int data_id
	int file_id
    String contig_id
    String sequence
    Float gc
    Float coverage
    int length
    static constraints = {
        contig_id(blank:false,unique: true)
        sequence(blank:false)
        gc(blank:false)
        coverage(blank:false)
        length(blank:false)
    }
    static mapping = {
        sequence type: "text"
        contig_id index:'trans_contig'
    }
}
