package badger
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql
import grails.plugin.cache.Cacheable
import grails.plugin.cache.CacheEvict

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
			def yearsql = "select distinct on (pubmed_id,date_out) pubmed_id,abstract_text,title,authors,journal_short,to_char(date_string,'yyyy Mon dd') as date_out from publication where date_string between \'01/01/" +params.year+ "\' and \'31/12/"+params.year+"\' order by date_out,pubmed_id;"
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
				//check for species specific searches
				if (params.speciesId){
					pubSearch += " and meta_id = '"+params.speciesId+"' "
				}
				//def pub_search = "select distinct(pubmed_id,abstract_text,title,authors,journal_short,to_char(date_string,'yyyy Mon dd') as date_out) from publication where "+pubSearch+" order by date_string desc;"
				def pub_search = "select distinct on (pubmed_id,date_out) pubmed_id,abstract_text,title,authors,journal_short,to_char(date_string,'yyyy Mon dd') as date_out from publication where "+pubSearch+" order by date_out,pubmed_id desc;"								
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
  
  def stats(){
  	def species = FileData.findAllByFile_typeInList(["Genome"],[sort:"meta.genus"])
    	return [species:species]
  	}
  
  //@Cacheable('stats_cache') 
  @CacheEvict(value='stats_cache', allEntries=true)
  def stats_results() {  	 
     //check the privacy setting
     if (grailsApplication.config.i.links.priv.stats && !isLoggedIn()) {
     	redirect(controller: "home", action: "index")
	 }else{ 
	 	 def sql = new Sql(dataSource)
		 
		 //get global counts data
		 //println "size 1 = "+params.speciesCheck.length()
		 def meta 		
		 def speciesString = ""
		 println "species selected = "+params.speciesCheck
		 if (params.speciesCheck instanceof String){
		 	meta = MetaData.findAllBySpecies(params.speciesCheck)
		 	speciesString += "species = '"+params.speciesCheck+"'"
		 }else{	 
		 	speciesString += "";
			def speciesList = []
			params.speciesCheck.each{
		 		speciesList.add(it)
		 		if (speciesString == ""){
		 			speciesString += "(species = '"+it+"'"
		 		}else{
		 			speciesString += " or species = '"+it+"'"
		 		}
		 	}
		 	speciesString += ")"
		 	meta = MetaData.findAllBySpeciesInList(speciesList)
		 }
		 println "speciesString = "+speciesString
		 def geneCountAll = []
		 def geneCountData
		 
		 meta.each{		 
			 def geneCount = "select count(mrna_id) from gene_info,file_data,meta_data where gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id and meta_data.id = '"+it.id+"';";
		 	 println geneCount
		 	 geneCountData = sql.rows(geneCount)
		 	 geneCountAll.add([genus:it.genus,species:it.species,count:geneCountData.count[0]])
		 }
		 
		 println "geneCountAll = "+geneCountAll
		 println "fig = "+params.figure
		 
		 //get data individual for plots
		 
		 //scaffold length/gc/coverage
		 def genomeInfo 
		 def n50_list
		 def n90_list
		 if (params.figure == "1"){
			 def genomeInfoSql = "select contig_id,gc,length,coverage,species from genome_info,file_data,meta_data where "+speciesString+" and file_id = file_data.id and meta_id = meta_data.id group by species,non_atgc,contig_id,gc,length,coverage order by species,length desc;"
			 println genomeInfoSql
			 genomeInfo = sql.rows(genomeInfoSql)
			 int span=0, min=10000000000, max=0, n50=0, halfSpan=0, checkSpan=0, nonATGC=0, num=0, ninetySpan=0, counter=0;
			 n50_list = []; n90_list = [];
			 float gc
				 
			 def n50check = false, n90check = false;
			 def spanList = [:]
			 def old_species = ""
			 //span
			 genomeInfo.each {
			 	if (it.species != old_species){
			 		if (old_species != ""){
			 			spanList[old_species] = span
			 		}
			 		old_species = it.species
			 		span=0
					
				}
				span += it.length
			 }		
			 spanList[old_species] = span
			 println "spans = "+spanList
			 
			 //N50/90s
			 old_species = ""
			 genomeInfo.each {
			 	if (it.species != old_species){
			 		def currentSpecies = it.species
			 		halfSpan = spanList."${currentSpecies}"/2
			 		ninetySpan = spanList."${currentSpecies}"/100*90
			 		old_species = it.species
			 		counter=0;checkSpan=0;n50check = false;n90check = false;	
				}
				counter++
				checkSpan += it.length
				if (checkSpan >= halfSpan && n50check !=true){
					def aa = [counter,checkSpan,it.length,it.species]
					n50 = it.length
					n50check = true
					n50_list.add(aa)
				}
				if (checkSpan >= ninetySpan && n90check !=true){
					def aa = [counter,checkSpan,it.length,it.species]
					n90_list.add(aa)
					n90check = true
				}
			}
			println "n50 = "+n50_list
		 	println "n90 = "+n90_list
		 }
		 
		 //gene lengths
		 def geneDistData 
		 if (params.figure == "2"){
			 def geneDist = "select num,count(num),species from (select gene_id, length(pep) as num,species from gene_info,file_data,meta_data where "+speciesString+" and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id group by gene_id,pep,meta_data.species) as foo group by species,num order by species,num;";
			 println geneDist
			 geneDistData = sql.rows(geneDist)
		 }
		 
		 //exons per gene
		 def exonCountData
		 if (params.figure == "3"){
			 //def exonCountSql = "select num,count(num) from (select gene_id, count(gene_id) as num from exon_info group by gene_id) as foo group by num order by num;"
			 def exonCountSql = "select num,count(num),species from (select gene_info.gene_id, count(gene_info.gene_id) as num,species from exon_info,gene_info,file_data,meta_data where "+speciesString+" and exon_info.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id and "+speciesString+" group by gene_info.gene_id,species) as foo group by species,num order by species,num;";
			 println exonCountSql
			 exonCountData = sql.rows(exonCountSql)
		 }
		 
		 //exon distribution
		 def exonDistData
		 if (params.figure == "4"){
			 def exonDist = "select num,count(num),species from (select exon_number, length(sequence) as num,species from exon_info,gene_info,file_data,meta_data where "+speciesString+" and exon_info.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id group by species,exon_number,sequence) as foo group by species,num order by species,num;"
			 println exonDist
			 exonDistData = sql.rows(exonDist)
		 }
		 
		 //exon lengths and gc by exon number
		 def exonLenNum = []
		 def exonGCNum = []
		 if (params.figure == "5"){
			 def exonNumLenGCsql = "select exon_number,avg(length(sequence)) as len ,avg(gc) as gc from exon_info group by exon_number order by exon_number;"
			 //def exonNumLenGCsql = "select exon_number,avg(length(sequence)) as len ,avg(exon_info.gc) as gc,species from exon_info,gene_info,file_data,meta_data where exon_info.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id group by species,exon_number order by species,exon_number;";
			 def exonNumLenGC = sql.rows(exonNumLenGCsql)
			
			 exonNumLenGC.each{
				def aa = [it.exon_number,it.len]
				exonLenNum.add(aa)
				def bb = [it.exon_number,it.gc]
				exonGCNum.add(bb)
			}
		}
		return [genomeInfo: genomeInfo, n50_list: n50_list, n90_list: n90_list, geneDistData:geneDistData, geneCountData:geneCountAll, exonCountData: exonCountData, exonDistData:exonDistData, exonLenNum: exonLenNum, exonGCNum: exonGCNum]
	 }
	 sql.close()
  }
}
