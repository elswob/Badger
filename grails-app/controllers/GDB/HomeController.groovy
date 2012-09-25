package GDB
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql

class HomeController {
 def grailsApplication
 javax.sql.DataSource dataSource

 //@Secured(['ROLE_ADMIN','ROLE_USER'])
 def index = {
 	 def newsData = News.findAll(sort:"dateString",order:"desc")
 	 return [newsData: newsData] 	 
 }
 def browse = {
 }
 def news = {
 	 def newsData = News.findAll(sort:"dateString",order:"desc")
 	 return [newsData: newsData, highlight: params.newsTitle]
 }
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
 def publications = {
     //check the privacy setting
     if (grailsApplication.config.i.links.priv.publications && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
	 }else{ 
		 def sql = new Sql(dataSource)
		 def yearsql = "select count(*),date_part('year',date_string) from publication group by date_part('year',date_string) order by date_part('year',date_string);"
		 def yearData = sql.rows(yearsql)
		 return [yearData: yearData]
	 }
 }
 def publication_search = {
      //check the privacy setting
     if (grailsApplication.config.i.links.priv.publications && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
     }else{
		def sql = new Sql(dataSource)
		//get the year from the bar chart 
		if (params.year){
			def yearbefore = params.year-1
			def yearsql = "select *,to_char(date_string,'yyyy Mon dd') as date_out from publication where date_string between \'01/01/" +params.year+ "\' and \'31/12/"+params.year+"\';"
			println yearsql
			def pub_results = sql.rows(yearsql)
			return [ pub_results: pub_results, searchId: params.year]
		}else{	
			def pubSearch = "("
			def searchId = params.searchId
			def pubType = params.pubVal
			println "searchId = "+searchId
			println "pubType = "+pubType
			if (pubType){
				if (pubType instanceof String){
					pubSearch += pubType + "~* \'" + searchId + "\'"
				}else{
					def pubSelect = pubType		
					pubSelect.each {
						pubSearch += it + " ~* \'" + searchId + "\'" + " or "
					}
					pubSearch = pubSearch[0..-5]			
				}
				pubSearch += ")"
				def pub_search = "select *,to_char(date_string,'yyyy Mon dd') as date_out from publication where "+pubSearch+" order by date_string desc;"
				def pub_results = sql.rows(pub_search)
				println pub_search
				println "number of results = "+pub_results.size()
				return [ pub_results: pub_results, searchId: searchId]
				
			}
			if (!pubType){
				return [error: "no_type", searchId: searchId]
			}
		}
	}
 }

 def members = {
      //check the privacy setting
     if (grailsApplication.config.i.links.priv.members && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
     }else{
		 def memberData = [:]
		 def memberLoc = [:]
		 if (grailsApplication.config.mem.person){
			 grailsApplication.config.mem.person.each {
				 if (it.value.size() >0){
					 def dataSplit = it.value.split(",")
					 if (dataSplit[3].trim()){
						 memberData."${it.key}" = [dataSplit[0].trim(),dataSplit[1].trim(),dataSplit[2].trim(),dataSplit[3].trim()]
					 }else{
						 memberData."${it.key}" = [dataSplit[0].trim(),dataSplit[1].trim(),dataSplit[2].trim(),grailsApplication.config.headerImage]
					 }
				}
			}
		}
		if (grailsApplication.config.mem.location){
			 grailsApplication.config.mem.location.each {
				 if (it.value.size() >0){
					 def dataSplit = it.value.split(",")
					 memberLoc."${it.key}" = [dataSplit[0].trim(),dataSplit[1].trim()]
				}
			}
		}
		//println "Member data = "+memberData
		//println "Member locations = "+memberLoc
		return [ memberData: memberData, memberLoc: memberLoc]
	}
 }
 def download = {
     //check the privacy setting
     if (grailsApplication.config.i.links.priv.download && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
     }
     else{
		 def pubDownloadFiles = [:]
		 def privDownloadFiles = [:]
		 def dataSplit
		 if (grailsApplication.config.download.pub){
			 def pubLocations = grailsApplication.config.download.pub
			 pubLocations.each {
				 if (it.value.size() >0){
					 dataSplit = it.value.split(",")
					 pubDownloadFiles."${it.key}" = [dataSplit[0].trim(),dataSplit[1].trim(),dataSplit[2].trim()]
				}
			}
		}
		if (grailsApplication.config.download.priv){
			 def privLocations = grailsApplication.config.download.priv
			 privLocations.each {
				 if (it.value.size() >0){
					 dataSplit = it.value.split(",")
					 privDownloadFiles."${it.key}" = [dataSplit[0].trim(),dataSplit[1].trim(), dataSplit[2].trim()] 
				}
			}
		}
		println "Public download files = "+pubDownloadFiles
		println "Private download files = "+privDownloadFiles
		return [ pubDownloadFiles: pubDownloadFiles, privDownloadFiles: privDownloadFiles]
	 }
  }
  
  def stats = {
     //check the privacy setting
     if (grailsApplication.config.i.links.priv.stats && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
	 }else{ 
	 	 def sql = new Sql(dataSource)
	 	 
	 	 //get genome stats
	 	 def genomeInfoSql = "select sequence,gc,length from genome_info order by length;"
	 	 def genomeInfo = sql.rows(genomeInfoSql) 
		 int span=0, min=10000000000, max=0, n50=0, halfSpan=0, n50Span=0, nonATGC=0
		 float gc
		 def n50check = false
		 def genome_stats = [:]
	 	 //span
	 	 genomeInfo.each {
			span += it.length
			gc += it.gc
			nonATGC += it.sequence.toUpperCase().count("N")
			//nonATGC += it.sequence.toUpperCase().findAll(/G|C|A|T/).size()
			if (it.length < min){
				min = it.length
			}
			if (it.length > max){
				max = it.length
			}
		 }
		 //nonATGC = span-nonATGC

		 gc = gc/genomeInfo.size()
		 
		 //n50
		 halfSpan = span/2
		 genomeInfo.each {
			n50Span += it.length
			if (n50Span >= halfSpan && n50check !=true){
				n50 = it.length
				n50check = true
			}
		 }
	 	 genome_stats.span = span
	 	 genome_stats.n50 = n50
	 	 genome_stats.min = min
		 genome_stats.max = max
		 genome_stats.gc = gc
		 genome_stats.nonATGC = nonATGC
		 
		 //get data for plots
		 def geneCount = GeneInfo.count()
		 def exonCount = ExonInfo.count()
		 def exonCountSql = "select num,count(num) from (select gene_id, count(gene_id) as num from exon_info group by gene_id) as foo group by num order by num;"
		 def exonCountData = sql.rows(exonCountSql)
		 def exonDist = "select num,count(num) from (select exon_id, stop-start as num from exon_info group by exon_id,start,stop) as foo where num<1000 group by num order by num;"
		 def exonDistData = sql.rows(exonDist)
		 def geneDist = "select num,count(num) from (select gene_id, length(pep) as num from gene_info group by gene_id,pep) as foo group by num order by num;"
		 def geneDistData = sql.rows(geneDist)
		 return [exonCountData: exonCountData, geneCount:geneCount, exonDistData: exonDistData, exonCount: exonCount, geneDistData:geneDistData, genome_stats:genome_stats]
	 }
 }
}
