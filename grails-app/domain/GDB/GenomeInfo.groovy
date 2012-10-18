package GDB

class GenomeInfo {
	int data_id
	int file_id
    String contig_id
    String sequence
    Float gc
    Float coverage
    int length
    static constraints = {
    	data_id(blast:false)
    	file_id(blast:false)
        contig_id(blank:false)
        sequence(blank:false)
        gc(blank:false)
        coverage(blank:false)
        length(blank:false)
    }
    static mapping = {
        sequence type: "text"
    }
}
