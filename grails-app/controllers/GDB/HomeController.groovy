package GDB
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql
import grails.plugin.cache.Cacheable

class HomeController {
 def grailsApplication
 javax.sql.DataSource dataSource

 //@Secured(['ROLE_ADMIN','ROLE_USER'])
 def index = {
 	 def newsData = News.findAll(sort:"dateString",order:"desc")
 	 return [newsData: newsData] 	 
 }
 def browse = {
 	  //check the privacy setting
     if (grailsApplication.config.i.links.priv.browse && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
     }
     else{
		 def files = FileData.findAll(sort:"id")
		 return [ files: files]
	 }
 }
 def news = {
 	 def newsData = News.findAll(sort:"dateString",order:"desc")
 	 return [newsData: newsData, highlight: params.newsTitle]
 }
 
 def publications = {
     //check the privacy setting
     if (grailsApplication.config.i.links.priv.publications && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
	 }else{ 
		 def sql = new Sql(dataSource)
		 def yearsql = "select count(distinct(pubmed_id)),date_part('year',date_string) from publication group by date_part('year',date_string) order by date_part('year',date_string);"
		 def yearData = sql.rows(yearsql)
		 def dissql = "select count(distinct(pubmed_id)) from publication;"
		 def dis = sql.rows(dissql)
		 return [yearData: yearData, distinct: dis]
		 sql.close()
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
		sql.close()
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
		 def files = FileData.findAll(sort:"id")
		 return [ files: files]
	 }
  }
  
  @Cacheable('stats_cache') 
  def stats() {  	 
     //check the privacy setting
     if (grailsApplication.config.i.links.priv.stats && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
	 }else{ 
	 	 def sql = new Sql(dataSource)
	 	 
	 	 //get genome stats
	 	 def genomeInfoSql = "select sequence,gc,length from genome_info order by length desc;"
	 	 //def genomeInfoSql = "select gc,length,meta_data.species from genome_info,file_data,meta_data where file_id = file_data.id and meta_id = meta_data.id order by species,length desc;"
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
		 
		 //get gene stats
		 def geneInfoSql = "select * from gene_info;"
	 	 def geneInfo = sql.rows(geneInfoSql) 
	 	 int mean=0
	 	 min=10000000000
	 	 max=0
	 	 nonATGC=0
	 	 gc=0
		 def gene_stats = [:]
		 
	 	 geneInfo.each {
			mean += it.nuc.length()
			gc += it.gc
			nonATGC += it.nuc.toUpperCase().count("N")
			//nonATGC += it.sequence.toUpperCase().findAll(/G|C|A|T/).size()
			if (it.nuc.length() < min){
				min = it.nuc.length()
			}
			if (it.nuc.length() > max){
				max = it.nuc.length()
			}
		 }
		 //nonATGC = span-nonATGC
		 mean = mean/geneInfo.size()
		 gc = gc/geneInfo.size()
	 	 gene_stats.mean = mean
	 	 gene_stats.gc = gc
	 	 gene_stats.min = min
	 	 gene_stats.max = max
	 	 gene_stats.nonATGC = nonATGC
	 	 println gene_stats
		 
		 //get data for plots
		 def geneCount = GeneInfo.count()
		 def exonCount = ExonInfo.count()
		 def funAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_info left outer join gene_anno on (gene_info.gene_id = gene_anno.gene_id) group by anno_db;"
		 def funAnnoData = sql.rows(funAnnoSql)
		 def blastAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_info left outer join gene_blast on (gene_info.gene_id = gene_blast.gene_id) group by anno_db;"
		 def blastAnnoData = sql.rows(blastAnnoSql)
		 def exonCountSql = "select num,count(num) from (select gene_id, count(gene_id) as num from exon_info group by gene_id) as foo group by num order by num;"
		 def exonCountData = sql.rows(exonCountSql)
		 def exonDist = "select num,count(num) from (select exon_id, length(sequence) as num from exon_info group by exon_id,sequence) as foo group by num order by num;"
		 //def exonDist = "select num,count(num) from (select exon_id, length(sequence) as num from exon_info group by exon_id,sequence) as foo where num<10000 group by num order by num;"
		 def exonDistData = sql.rows(exonDist)
		 def geneDist = "select num,count(num) from (select gene_id, length(pep) as num from gene_info group by gene_id,pep) as foo group by num order by num;"
		 def geneDistData = sql.rows(geneDist)
		 
		 //exon lengths and gc by exon number
		 def exonNumLenGCsql = "select exon_number,avg(length(sequence)) as len ,avg(gc) as gc from exon_info group by exon_number order by exon_number;"
		 def exonNumLenGC = sql.rows(exonNumLenGCsql)
		 def exonLenNum = []
		 def exonGCNum = []
		 exonNumLenGC.each{
		 	def aa = [it.exon_number,it.len]
		 	exonLenNum.add(aa)
		 	def bb = [it.exon_number,it.gc]
		 	exonGCNum.add(bb)
		 }
		 return [exonCountData: exonCountData, geneCount:geneCount, exonDistData: exonDistData, exonCount: exonCount, geneDistData:geneDistData, genome_stats:genome_stats, gene_stats:gene_stats, blastAnnoData: blastAnnoData, funAnnoData: funAnnoData, exonLenNum: exonLenNum, exonGCNum: exonGCNum ]
	 }
	 sql.close()
 }
}
