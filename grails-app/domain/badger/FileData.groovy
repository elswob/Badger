package badger

class FileData {
    String file_type
    String file_link
    String file_dir
    String file_name
    String blast
    String file_version
    String description
    String cov
    String search
    String download
	String url
    Boolean loaded
    String source = "local"

    static mapping = {
		url(blank:true, nullable: true)
        description type: "text"
    }
    static hasMany = [anno:AnnoData, gene:GeneInfo, scaffold:GenomeInfo]
    static belongsTo = [genome: GenomeData]
    
}
