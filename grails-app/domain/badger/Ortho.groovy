package badger

class Ortho {
	String trans_name
    int group_id
    int size
    GeneInfo gene
    
    static mapping = {
        gene(index: "gene_info_ortho")
    }
    //static belongsTo = [gene:GeneInfo]
}
