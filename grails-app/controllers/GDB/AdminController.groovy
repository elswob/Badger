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
		def check = MetaData.findByGenusAndSpecies(params.genus, params.species)
		if (check){
			println "species already exists - "+check
			return [error: "duplicate"]	
		}else{  		
			dataMap.data_id = data_id
			dataMap.genus = params.genus
			dataMap.data_version = params.version
			dataMap.species = params.species
			dataMap.description = params.description
			println dataMap
			new MetaData(dataMap).save()
			
			def fileMap = [:]
			int getFileID = FileData.count()
			if (params.trans){
				getFileID++
				fileMap.file_id = getFileID
				fileMap.data_id = data_id
				fileMap.file_type = "transcriptome"
				fileMap.file_dir = params.dir
				fileMap.file_name = params.trans	
				fileMap.blast = params.blast_trans
				fileMap.file_version = params.trans_v
				fileMap.description = params.trans_d
				println fileMap
				new FileData(fileMap).save() 
				
			}
			if (params.genome){
				getFileID++
				fileMap.file_id = getFileID
				fileMap.data_id = data_id
				fileMap.file_type = "genome"
				fileMap.file_dir = params.dir
				fileMap.file_name = params.genome	
				fileMap.blast = params.blast_genome
				fileMap.file_version = params.genome_v
				fileMap.description = params.genome_d
				println fileMap
				new FileData(fileMap).save() 
			}
			if (params.genes){
				getFileID++
				fileMap.file_id = getFileID
				fileMap.data_id = data_id
				fileMap.file_type = "gff"
				fileMap.file_dir = params.dir
				fileMap.file_name = params.genes
				fileMap.blast = params.blast_genes
				fileMap.file_version = params.genes_v
				fileMap.description = params.genes_d
				println fileMap
				new FileData(fileMap).save() 
			}		
			return [dataMap:dataMap]
		}
	}
	
	@Secured(['ROLE_ADMIN'])
	def addAnno = {
		def sql = new Sql(dataSource)
		def dataSetsSql = "select meta_data.data_id,genus,species,file_name, file_version, file_type, file_id from meta_data, file_data where meta_data.data_id = file_data.data_id and (file_type = 'gff' or file_type = 'transcriptome') ;";
		def dataSets = sql.rows(dataSetsSql)
		//print dataSets
		return [dataSets:dataSets]
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedAnno = {
		//check if annotation is unique
		def check = AnnoData.findByData_idAndFile_idAndAnno_file(params.data_id, params.file_id, params.file_name)
		if (check){
			println "annotation already exists - "+check
			return [error: "duplicate"]	
		}else{
			def annoMap = [:]
			def dataSplit = params.dataSelect.split(":")	
			annoMap.data_id = dataSplit[0]
			annoMap.file_id = dataSplit[1]
			if (params.annoSelect == "1"){
				annoMap.type = "blast"				
				annoMap.link = params.b_link
				annoMap.source = params.b_source
				annoMap.regex = params.b_regex	
				annoMap.anno_file = params.b_anno_file	
			}else if (params.annoSelect == "2"){
				annoMap.type = "fun"
				annoMap.link = params.f_link
				annoMap.source = params.f_source
				annoMap.regex = params.f_regex	
				annoMap.anno_file = params.f_anno_file	
			}else if (params.annoSelect == "3"){
				annoMap.type = "ipr"
				annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
				annoMap.regex = "(IPR\\d+).*?"
				annoMap.source = "InteProScan"
				annoMap.anno_file = params.i_anno_file	
			}
			println annoMap
			new AnnoData(annoMap).save()
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
