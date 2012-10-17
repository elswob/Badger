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
		dataMap.data_id = getDataID + 1
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
			println fileMap
			new FileData(fileMap).save() 
		}
		if (params.gff){
			getFileID++
			fileMap.file_id = getFileID
			fileMap.data_id = data_id
			fileMap.file_type = "gff"
			fileMap.file_dir = params.dir
			fileMap.file_name = params.gff	
			fileMap.blast = params.blast_genes
			println fileMap
			new FileData(fileMap).save() 
		}
		
		return [dataMap:dataMap]
	}
	
	@Secured(['ROLE_ADMIN'])
	def editSpecies = {
		println "Editing "+params.titleString
		def newsData = News.findAllByTitleString(params.titleString)
		return [newsData: newsData] 
	}
	
	@Secured(['ROLE_ADMIN'])
	def editedSpecies = {
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
