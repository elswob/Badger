package GDB
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql
import grails.plugin.cache.Cacheable

class AdminController {
	def grailsApplication
	javax.sql.DataSource dataSource
	
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
		 def newsData = News.findAllByTitleString(params.titleString)
		 return [newsData: newsData] 
	 }
	 @Secured(['ROLE_ADMIN'])
	 def editedNews = {
		 //delete old entry
		 def newsData = News.findAllByTitleString(params.newsTitle)
		 def delData = News.get(newsData.id[0])
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
		def a = MetaData.get(6)
		println "a = "+a.files
		for (t in a.files) {
    		println "file_ids for "+a+ " are "+t.file_id
		}	
		return [metaData: metaData]	 
	}
	
	@Secured(['ROLE_ADMIN'])
	def addSpecies = {
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedData = {
		def dataMap = [:]		
		int getDataID = MetaData.count()
		int data_id = getDataID + 1
		
		//check if name and version are unique
		def check = MetaData.findByGenusAndSpecies(params.genus.trim(), params.species.trim())
		if (check){
			println "species already exists - "+check
			return [error: "duplicate"]	
		}else{  		
			dataMap.data_id = data_id
			dataMap.genus = params.genus.trim()
			dataMap.species = params.species.trim()
			dataMap.description = params.description.trim()
			dataMap.image_file = params.image_f.trim()
			dataMap.image_source = params.image_s.trim()
			println dataMap
			new MetaData(dataMap).save()
			
			def fileMap = [:]
			int genomeID
			int geneID
			int getFileID = FileData.count()
			if (params.trans){
				getFileID++
				fileMap.file_id = getFileID
				fileMap.data_id = data_id
				fileMap.file_type = "Transcriptome"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.trans.trim()
				fileMap.blast = params.blast_trans
				fileMap.search = params.search_trans
				fileMap.download = params.down_trans
				fileMap.file_version = params.trans_v.trim()
				fileMap.description = params.trans_d.trim()
				fileMap.cov = params.trans_c.trim()
				println fileMap
				new FileData(fileMap).save() 
				
			}
			if (params.genome){
				getFileID++
				fileMap.file_id = getFileID
				fileMap.data_id = data_id
				fileMap.file_type = "Genome"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.genome.trim()	
				fileMap.blast = params.blast_genome
				fileMap.search = params.search_genome
				fileMap.download = params.down_genome
				fileMap.file_version = params.genome_v.trim()
				fileMap.description = params.genome_d.trim()
				fileMap.cov = params.genome_c.trim()
				genomeID = getFileID
				println fileMap
				new FileData(fileMap).save() 
			}
			if (params.genes){
				getFileID++
				fileMap.file_id = getFileID
				fileMap.data_id = data_id
				fileMap.file_type = "Genes"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.genes.trim()
				fileMap.blast = params.blast_genes
				fileMap.search = params.search_genes
				fileMap.download = params.down_genes
				fileMap.file_version = params.genes_v.trim()
				fileMap.description = params.genes_d.trim()
				fileMap.file_link = genomeID
				geneID = getFileID
				println fileMap
				new FileData(fileMap).save() 
			}
			if (params.mrna_trans){
				getFileID++
				fileMap.file_id = getFileID
				fileMap.data_id = data_id
				fileMap.file_type = "mRNA"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.mrna_trans.trim()
				fileMap.blast = params.blast_genes
				fileMap.search = params.search_genes
				fileMap.download = params.down_genes
				fileMap.file_version = params.mrna_trans_v.trim()
				fileMap.description = params.mrna_trans_d.trim()
				fileMap.file_link = geneID
				println fileMap
				new FileData(fileMap).save() 
			}
			if (params.mrna_pep){
				getFileID++
				fileMap.file_id = getFileID
				fileMap.data_id = data_id
				fileMap.file_type = "Peptide"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.mrna_pep.trim()
				fileMap.blast = params.blast_genes
				fileMap.search = params.search_genes
				fileMap.download = params.down_genes
				fileMap.file_version = params.mrna_pep_v.trim()
				fileMap.description = params.mrna_pep_d.trim()
				fileMap.file_link = geneID
				println fileMap
				new FileData(fileMap).save() 
			}			
			return [dataMap:dataMap]
		}
	}
	
	@Secured(['ROLE_ADMIN'])
	def addAnno = {
		def sql = new Sql(dataSource)
		def dataSetsSql = "select meta_data.data_id,genus,species,file_name, file_version, file_type, file_id from meta_data, file_data where meta_data.data_id = file_data.data_id and (file_type = 'Genes' or file_type = 'Transcriptome') ;";
		def dataSets = sql.rows(dataSetsSql)
		//print dataSets
		return [dataSets:dataSets]
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedAnno = {
		def dataSplit = params.dataSelect.split(":")	
	
		def annoMap = [:]			
		annoMap.data_id = dataSplit[0].trim()
		annoMap.file_id = dataSplit[1].trim()
		if (params.annoSelect == "1"){
			//check if annotation is unique
			def check = AnnoData.findByData_idAndFile_idAndAnno_file(dataSplit[0].trim(), dataSplit[1].trim(), params.b_anno_file.trim())
			if (check){
				return [error: "duplicate"]	
			}else{
				annoMap.type = "blast"				
				annoMap.link = params.b_link.trim()
				annoMap.source = params.b_source.trim()
				annoMap.regex = params.b_regex.trim()
				annoMap.anno_file = params.b_anno_file.trim()	
				println annoMap
				new AnnoData(annoMap).save()
				return [annoMap: annoMap]
			}
		}else if (params.annoSelect == "2"){
			//check if annotation is unique
			def check = AnnoData.findByData_idAndFile_idAndAnno_file(dataSplit[0].trim(), dataSplit[1].trim(), params.f_anno_file.trim())
			if (check){
				return [error: "duplicate"]	
			}else{
				annoMap.type = "fun"
				annoMap.link = params.f_link.trim()
				annoMap.source = params.f_source.trim()
				annoMap.regex = params.f_regex.trim()	
				annoMap.anno_file = params.f_anno_file.trim()	
				println annoMap
				new AnnoData(annoMap).save()
				return [annoMap: annoMap]
			}
		}else if (params.annoSelect == "3"){
			//check if annotation is unique
			def check = AnnoData.findByData_idAndFile_idAndAnno_file(dataSplit[0].trim(), dataSplit[1].trim(), params.i_anno_file.trim())
			if (check){
				return [error: "duplicate"]	
			}else{
				annoMap.type = "ipr"
				annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
				annoMap.regex = "(IPR\\d+).*?"
				annoMap.source = "InteProScan"
				annoMap.anno_file = params.i_anno_file.trim()	
				println annoMap
				new AnnoData(annoMap).save()
				return [annoMap: annoMap]
			}
		}
	}
	
	@Secured(['ROLE_ADMIN'])
	def deleteSpecies = {
		println "Deleting "+params.titleString
		def newsData = News.findAllByTitleString(params.titleString)
	 	return [newsData: newsData] 
	}
	@Secured(['ROLE_ADMIN'])
	def deletedSpecies = {
		def newsData = News.findAllByTitleString(params.newsTitle)
	 	def delData = News.get(newsData.id[0])
	 	delData.delete(flush: true)
	 	println "Deleted "+params.newsTitle
	}
}
