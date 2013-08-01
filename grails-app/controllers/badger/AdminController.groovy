package badger
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql
import grails.plugin.cache.Cacheable

class AdminController {
	def grailsApplication
	def deleteService
	javax.sql.DataSource dataSource
	def pubService
	
	//news
	
	@Secured(['ROLE_ADMIN'])
	 def addNews = {
	 }
	 @Secured(['ROLE_ADMIN'])
	 def addedNews = {
		 def dataMap = [:]
		 def data
		 def title
		 Date date
		 if (params.newsTitle){
			title = params.newsTitle
		 }else{
			 title = null
		 }
		 if (params.newsData){
			data = params.newsData
		 }else{
			 data = null
		 }
		 if (params.newsDate){
			 def matcher
			 //check the date format is ok
			 if ((matcher = params.newsDate =~ /^\d{2}\/\d{2}\/\d{4}/)){
				 date = Date.parse("dd/MM/yyyy",params.newsDate)
			 }else{
				 date = null
			 }
		 }else{	 
			date = new Date()
		 }
		 dataMap.dataString = data
		 dataMap.titleString = title
		 dataMap.dateString = date
		 println dataMap
		 new News(dataMap).save()
		 return [newsTitle: title, newsData: data, newsDate: date]
	 }
	 @Secured(['ROLE_ADMIN'])
	 def editNews = {
		 println "Editing "+params.titleString
		 def newsData = News.findByTitleString(params.titleString)
		 return [newsData: newsData] 
	 }
	 @Secured(['ROLE_ADMIN'])
	 def editedNews = {
		 //delete old entry
		 def newsData = News.findById(params.newsId)
		 println "deleting "+params.newsId
		 def delData = News.get(newsData.id)
		 delData.delete(flush: true)
		 
		 def dataMap = [:]
		 def data
		 def title
		 Date date
		 if (params.newsTitle){
			title = params.newsTitle
		 }else{
			 title = null
		 }
		 if (params.newsData){
			data = params.newsData
		 }else{
			 data = null
		 }
		 if (params.newsDate){
			 def matcher
			 //check the date format is ok
			 if ((matcher = params.newsDate =~ /^\d{2}\/\d{2}\/\d{4}/)){
				 date = Date.parse("dd/MM/yyyy",params.newsDate)
			 }else{
				 date = null
			 }
		 }else{	 
			date = new Date()
		 }
		 dataMap.dataString = data
		 dataMap.titleString = title
		 dataMap.dateString = date
		 println dataMap
		 new News(dataMap).save()
		 return [newsTitle: title, newsData: data, newsDate: date]
	 }
	 @Secured(['ROLE_ADMIN'])
	 def deleteNews = {
		 println "Deleting "+params.titleString
		 def newsData = News.findAllByTitleString(params.titleString)
		 return [newsData: newsData] 
	 }
	 @Secured(['ROLE_ADMIN'])
	 def deletedNews = {
		 def newsData = News.findAllByTitleString(params.newsTitle)
		 def delData = News.get(newsData.id[0])
		 delData.delete(flush: true)
		 println "Deleted "+params.newsTitle
	 }
 
 	//species
	
	@Secured(['ROLE_ADMIN'])
	def home = {
		def metaData = MetaData.findAll()
		return [metaData: metaData]	 
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedSpecies = {
		def dataMap = [:]		
		//check if name and version are unique
		def check = MetaData.findByGenusAndSpecies(params.genus.trim(), params.species.trim())
		if (check){
			println "species already exists - "+check
			return [error: "duplicate"]	
		}else{  		
			dataMap.genus = params.genus.trim()
			dataMap.species = params.species.trim()
			dataMap.description = params.description.trim()
			if (params.image_f){
				dataMap.image_file = params.image_f.trim()
				dataMap.image_source = params.image_s.trim()
			}else{
				dataMap.image_file = ""
				dataMap.image_source = ""
			}
			println dataMap
			MetaData meta = new MetaData(dataMap)
			meta.save()
			return [dataMap:dataMap, Gid: meta.id]
		}
	}
	
	@Secured(['ROLE_ADMIN'])
	def editSpecies = {
		def metaData = MetaData.findById(params.Gid)
		return [metaData: metaData]	 
	}
	
	@Secured(['ROLE_ADMIN'])
	def editedSpecies = {
		def sql = new Sql(dataSource)
		def upsql = "update meta_data set genus = '"+params.genus.trim()+"', species = '"+params.species.trim()+"', description = '"+params.description.trim()+"', image_file = '"+params.image_f.trim()+"', image_source = '"+params.image_s.trim()+"' where id = '"+params.id+"';";
		println "upsql = "+upsql
		def update = sql.execute(upsql)
	}
	
	@Secured(['ROLE_ADMIN'])
	def deleteSpecies = {
		def metaData = MetaData.findById(params.Gid)
		return [metaData: metaData]	
	}
	@Secured(['ROLE_ADMIN'])
	def deletedSpecies = {
		println "Run deleteService with species id "+params.id
		def del = deleteService.deleteSpecies(params.Gid)
	 	return [del:del]		 	
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedGenome = {
		def dataMap = [:]
		def meta = MetaData.findById(params.meta)		
		//check if version is unique
		def check = GenomeData.findByGversion(params.genome_version.trim())
		if (check){
			println "genome version already exists - "+check
			return [error: "duplicate",genome:check]	
		}else{  		
			dataMap.gversion = params.genome_version.trim()			
			if (params.gbrowse){
				dataMap.gbrowse = params.gbrowse.trim()
			}else{
				dataMap.gbrowse = ""
			}
			def date
			if (params.genome_date){
				def matcher
			 	//check the date format is ok
			 	if ((matcher = params.genome_date =~ /^\d{2}\/\d{2}\/\d{4}/)){
					date = Date.parse("dd/MM/yyyy",params.genome_date)
			 	}else{
				 	date = null
			 	}
		 	}else{	 
				date = new Date()
		 	}
		 	dataMap.dateString = date
			println dataMap
			def new_genome = new GenomeData(dataMap) 
			meta.addToGenome(new_genome)
			new_genome.save(flush:true)
			println "New genome for "+new_genome.meta.genus+" "+new_genome.meta.species+" date "+date+" was added"
			return [dataMap:dataMap, genome: new_genome]
		}
	}
	
	@Secured(['ROLE_ADMIN'])
	def editGenome = {
		def genomeData = GenomeData.findById(params.gid)
		return [genome: genomeData]	 
	}
	
	@Secured(['ROLE_ADMIN'])
	def editedGenome = {
		def sql = new Sql(dataSource)
		def date
		if (params.genome_date){
			def matcher
			//check the date format is ok
			if ((matcher = params.genome_date.trim() =~ /^\d{2}\/\d{2}\/\d{4}/)){
				date = Date.parse("dd/MM/yyyy",params.genome_date.trim())
			}else{
				date = null
			}
		}else{	 
			date = new Date()
		}
		def upsql = "update genome_data set date_string = '"+date+"', gversion = '"+params.genome_version.trim()+"', gbrowse = '"+params.gbrowse.trim()+"' where id = '"+params.id+"';";
		println "upsql = "+upsql
		def update = sql.execute(upsql)
	}
	
	@Secured(['ROLE_ADMIN'])
	def deleteGenome = {
		def genomeData = GenomeData.findById(params.gid)
		return [genome: genomeData]	
	}
	
	@Secured(['ROLE_ADMIN'])
	def deletedGenome = {
		println "Run deleteService with genome id "+params.id
		def del = deleteService.deleteGenome(params.id)
	 	return [del:del]	
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedFiles = {
		def fileMap = [:]		
		def genome = GenomeData.findById(params.gid)
		println "genome = "+genome		
		fileMap.loaded = false
		def extraGff
		def fileDir
		fileMap.search = "pub"
		fileMap.blast = "pub"
		fileMap.download = "pub"
		fileMap.cov = "n"
		if (params.extra){
			println "extra gff file for genome = "+params.extra
			extraGff = FileData.findById(params.extra)
			fileDir = extraGff.file_dir
		}else{
			fileDir = params.dir.trim()
		}
		fileMap.file_dir = fileDir
		if (params.genome){
			fileMap.file_type = "Genome"
			fileMap.file_name = params.genome.trim()	
			fileMap.blast = params.blast_genome
			fileMap.search = params.search_genome
			fileMap.download = params.down_genome
			fileMap.file_version = params.genome_v.trim()
			fileMap.description = params.genome_d.trim()
			fileMap.cov = params.genome_c.trim()		
			fileMap.file_link = "n"
			def check = FileData.findByFile_nameAndFile_type(fileMap.file_name, fileMap.file_type)
			if (check){
				println "file name "+fileMap.file_name+" type "+fileMap.file_type+" already exists - "+check
				return [error: "already", file: "data/"+fileDir+"/"+params.genome, genome:genome]
			}
			else if (new File("data/"+fileDir+"/"+params.genome.trim()).exists()){
				println "Adding genome "+fileMap
				FileData file = new FileData(fileMap) 
				genome.addToFiles(file)
				file.save()
			}else{
				println "file does not exist!"
				return [error: "no file", file: "data/"+fileDir+"/"+params.genome, genome:genome]
			}
		}
		if (params.genes){
			fileMap.file_type = "Genes"
			fileMap.file_name = params.genes.trim()
			fileMap.download = params.down_genes
			fileMap.file_version = params.genes_v.trim()
			fileMap.description = params.genes_d.trim()
			//check for extra gene sets and get the genome id
			if (params.extra){
				fileMap.file_link = extraGff.file_name.trim()
			}else{	
				fileMap.file_link = params.genome.trim()
			}
			def check = FileData.findByFile_nameAndFile_type(fileMap.file_name, fileMap.file_type)
			if (check){
				println "file name "+fileMap.file_name+" type "+fileMap.file_type+" already exists - "+check
				return [error: "already", file: "data/"+fileDir+"/"+params.genes, genome:genome]
			}else if (new File("data/"+fileDir+"/"+params.genes.trim()).exists()){
				println "Adding GFF3 file "+fileMap
				FileData file = new FileData(fileMap) 
				genome.addToFiles(file)
				file.save()
			}else{
				println "file does not exist!"
				return [error: "no file", file: "data/"+fileDir+"/"+params.genes, genome:genome]
			}
		}
		if (params.mrna_trans){
			fileMap.file_type = "mRNA"
			fileMap.file_name = params.mrna_trans.trim()
			fileMap.blast = params.blast_mrna
			fileMap.download = params.down_mrna
			fileMap.file_version = params.mrna_trans_v.trim()
			fileMap.description = params.mrna_trans_d.trim()
			fileMap.file_link = params.genes.trim()
			def check = FileData.findByFile_nameAndFile_type(fileMap.file_name, fileMap.file_type)
			if (check){
				println "file name "+fileMap.file_name+" type "+fileMap.file_type+" already exists - "+check
				return [error: "already", file: "data/"+fileDir+"/"+params.mrna_trans, genome:genome]
			}else if (new File("data/"+fileDir+"/"+params.mrna_trans.trim()).exists()){
				println "Adding transcript file "+fileMap
				FileData file = new FileData(fileMap) 
				genome.addToFiles(file)
				file.save()
			}else{
				println "file does not exist!"
				return [error: "no file", file: "data/"+fileDir+"/"+params.mrna_trans, genome:genome]
			}
		}
		if (params.mrna_pep){
			fileMap.file_type = "Peptide"
			fileMap.file_name = params.mrna_pep.trim()
			fileMap.blast = params.blast_pep
			fileMap.download = params.down_pep
			fileMap.file_version = params.mrna_pep_v.trim()
			fileMap.description = params.mrna_pep_d.trim()
			fileMap.file_link = params.genes.trim()
			def check = FileData.findByFile_nameAndFile_type(fileMap.file_name, fileMap.file_type)
			if (check){
				println "file name "+fileMap.file_name+" type "+fileMap.file_type+" already exists - "+check
				return [error: "already", file: "data/"+fileDir+"/"+params.mrna_pep, genome:genome]
			}else if (new File("data/"+fileDir+"/"+params.mrna_pep.trim()).exists()){
				println "Adding protein file "+fileMap
				FileData file = new FileData(fileMap) 
				genome.addToFiles(file)
				file.save()
			}else{
				println "file does not exist!"
				return [error: "no file", file: "data/"+fileDir+"/"+params.mrna_pep, genome:genome]
			}
		}
		return [genome: genome]			
	}
	
	@Secured(['ROLE_ADMIN'])
	def editFile = {
		def file = FileData.findById(params.fid)
		return [fileData:file]
	}
	
	@Secured(['ROLE_ADMIN'])
	def editedFile = {
		def sql = new Sql(dataSource)
		def upsql
		def file = FileData.findById(params.id)
		if (params.trans){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.trans.trim()+"', blast = '"+params.blast_trans+"', search = '"+params.search_trans+"', download = '"+params.down_trans+"', file_version = '"+params.trans_v.trim()+"', description = '"+params.trans_d.trim()+"', cov = '"+params.trans_c.trim()+"' where id = '"+params.id+"';";
		}else if (params.genome){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.genome.trim()+"', blast = '"+params.blast_genome+"', search = '"+params.search_genome+"', download = '"+params.down_genome+"', file_version = '"+params.genome_v.trim()+"', description = '"+params.genome_d.trim()+"', cov = '"+params.genome_c.trim()+"' where id = '"+params.id+"';";
		}else if (params.genes){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.genes.trim()+"', search = '"+params.search_genes+"', download = '"+params.down_genes+"', file_version = '"+params.genes_v.trim()+"', description = '"+params.genes_d.trim()+"' where id = '"+params.id+"';";
		}else if (params.mrna_trans){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.mrna_trans.trim()+"', blast = '"+params.blast_mrna+"', download = '"+params.down_mrna+"', file_version = '"+params.mrna_trans_v.trim()+"', description = '"+params.mrna_trans_d.trim()+"' where id = '"+params.id+"';";	
		}else if (params.mrna_pep){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.mrna_pep.trim()+"', blast = '"+params.blast_pep+"', download = '"+params.down_pep+"', file_version = '"+params.mrna_pep_v.trim()+"', description = '"+params.mrna_pep_d.trim()+"' where id = '"+params.id+"';";	
		}
		println "upsql = "+upsql
		def update = sql.execute(upsql)
		return [fileData:file]
	}
	
	@Secured(['ROLE_ADMIN'])
	def deleteFile = {
		def fileData = FileData.findById(params.fid)
		return [fileData: fileData]	
	}
	
	@Secured(['ROLE_ADMIN'])
	def deletedFile = {	
		println "Run deleteService with file id "+params.id
		def del = deleteService.deleteFile(params.id)
	 	return [del:del]
	}
	
	@Secured(['ROLE_ADMIN'])
	def addAnno = {
		def gff = FileData.findById(params.gid)
		return [gff:gff]
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedAnno = {
		def gff = FileData.findById(params.gff)
		println "gff = "+gff
		def annoMap = [:]			
		if (params.annoSelect == "1"){
			def check = FileData.findById(gff.id).anno.anno_file			
			println "check = "+check
			if (params.b_anno_file.trim() in check){
				println "check exists : "+check
				return [error: "duplicate", file:gff]	
			}else{
				annoMap.type = "blast"				
				annoMap.link = params.b_link.trim()
				annoMap.source = params.b_source.trim()
				annoMap.regex = params.b_regex.trim()
				annoMap.anno_file = params.b_anno_file.trim()
				annoMap.loaded = false	
				if (new File("data/"+gff.file_dir+"/"+params.b_anno_file.trim()).exists()){				
					println annoMap
					AnnoData anno = new AnnoData(annoMap)
					gff.addToAnno(anno)
					anno.save()
					return [annoMap: annoMap, file:gff]
					//redirect(action: "editGenome", params: [gid: gff.id])
				}else{
					return [error: "no file", fileLoc: "data/"+gff.file_dir+"/"+params.b_anno_file, file:gff]
				}
			}
		}else if (params.annoSelect == "2"){
			def check = FileData.findById(gff.id).anno.anno_file
			println "check = "+check
			if (params.f_anno_file.trim() in check){
				println "check exists : "+check
				return [error: "duplicate", file:gff]	
			}else{
				annoMap.type = "fun"
				annoMap.link = params.f_link.trim()
				annoMap.source = params.f_source.trim()
				annoMap.regex = params.f_regex.trim()	
				annoMap.anno_file = params.f_anno_file.trim()
				annoMap.loaded = false
				if (new File("data/"+gff.file_dir+"/"+params.f_anno_file.trim()).exists()){		
					println annoMap
					AnnoData anno = new AnnoData(annoMap)
					gff.addToAnno(anno)
					anno.save()
					return [annoMap: annoMap, file:gff]
				}else{
					return [error: "no file", fileLoc: "data/"+gff.file_dir+"/"+params.f_anno_file, file:gff]
				}
			}
		}else if (params.annoSelect == "3"){
			def check = FileData.findById(gff.id).anno.anno_file
			println "check = "+check
			if (params.i_anno_file.trim() in check){
				println "check exists : "+check
				return [error: "duplicate", file:gff]	
			}else{
				annoMap.type = "ipr"
				annoMap.link = "http://www.ebi.ac.uk/interpro/entry/"
				annoMap.regex = "(IPR\\d+).*?"
				annoMap.source = "InteProScan"
				annoMap.anno_file = params.i_anno_file.trim()
				annoMap.loaded = false	
				if (new File("data/"+gff.file_dir+"/"+params.i_anno_file.trim()).exists()){
					println annoMap
					AnnoData anno = new AnnoData(annoMap)
					gff.addToAnno(anno)
					anno.save()
					return [annoMap: annoMap, file:gff]
				}else{
					return [error: "no file", fileLoc: "data/"+gff.file_dir+"/"+params.i_anno_file, file:gff]
				}
			}
		}
	}
	
	@Secured(['ROLE_ADMIN'])
	def editAnno = {
		def annoData = AnnoData.findById(params.gid)
		return [annoData: annoData]	 
	}
	
	@Secured(['ROLE_ADMIN'])
	def editedAnno = {
		def anno = AnnoData.findById(params.id)
		def sql = new Sql(dataSource)
		def upsql 
		if (params.b_anno_file){
			upsql = "update anno_data set anno_file = '"+params.b_anno_file.trim()+"', source = '"+params.b_source.trim()+"', link = '"+params.b_link.trim()+"', regex = '"+params.b_regex.trim()+"' where id = '"+params.id+"';";		
		}else if (params.f_anno_file){
			upsql = "update anno_data set anno_file = '"+params.f_anno_file.trim()+"', source = '"+params.f_source.trim()+"', link = '"+params.f_link.trim()+"', regex = '"+params.f_regex.trim()+"' where id = '"+params.id+"';";
		}else if (params.i_anno_file){
			upsql = "update anno_data set anno_file = '"+params.i_anno_file.trim()+"' where id = '"+params.id+"';";
		}
		
		println "upsql = "+upsql
		def update = sql.execute(upsql)
		return [anno:anno]
	}
	
	@Secured(['ROLE_ADMIN'])
	def deleteAnno = {
		def annoData = AnnoData.findById(params.gid)
		return [annoData: annoData]	
	}
	@Secured(['ROLE_ADMIN'])
	def deletedAnno = {
		println "Run deleteService with annotaion id "+params.id
		def del = deleteService.deleteAnno(params.id)
	 	return [del:del]
	 	
	}
	
	def ajax_pub = {
    	def sql = new Sql(dataSource)
    	def pubnumsql1 = "select count(distinct(pubmed_id)) from publication;"
    	//print pubnumsql1
    	def pubnum1 = sql.rows(pubnumsql1)
    	//println "p1 = "+pubnum1
    	pubService.runPub()
    	def pubnumsql2 = "select count(distinct(pubmed_id)) from publication;"
    	//print pubnumsql2
    	def pubnum2 = sql.rows(pubnumsql2)
    	//println "p2 = "+pubnum2
    	render(template:"pubSelectResponse", model: [pubnum1:pubnum1,pubnum2:pubnum2])
    }
}
