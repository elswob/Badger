package badger
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql
import grails.plugin.cache.Cacheable
import grails.plugin.cache.CacheEvict

class SearchController {
	def matcher
	def grailsApplication
	javax.sql.DataSource dataSource
	def peptideService
	def configDataService
    //@Secured(['ROLE_USER'])
    
    def index = {
    	def metaData = MetaData.findAll()
    	return [metaData: metaData]
    }
    def species = {
    	def metaData = MetaData.findAll(sort:"genus")
    	def fileData = FileData.findAll()
    	return [meta: metaData, file: fileData]
    	
    }
    @Cacheable('species_cache')
    //@CacheEvict(value='species_cache', allEntries=true)
    def species_search() {
    	def sql = new Sql(dataSource)
    	def Gid = params.Gid
    	def metaData = MetaData.findById(Gid)	
    	
    	 //get genome info and stats
    	 def genomeInfoSql = "select non_atgc,contig_id,gc,length,coverage from genome_info,file_data,meta_data where file_id = file_data.id and meta_id = meta_data.id and meta_data.id = '"+Gid+"' order by length desc;"
	 	 //def sqlsearch = "select contig_id,gc,length,coverage from genome_info order by length desc;"
	 	 println genomeInfoSql
	 	 def genomeInfo = sql.rows(genomeInfoSql)
	 	 //def genomeSeqSql = "select sequence,contig_id,gc,length,coverage from genome_info,file_data,meta_data where file_id = file_data.id and meta_id = meta_data.id and meta_data.id = '"+Gid+"' order by length desc;"
	 	 //def sqlsearch = "select contig_id,gc,length,coverage from genome_info order by length desc;"
	 	 //println genomeSeqSql
	 	 //def genomeSeq = sql.rows(genomeSeqSql) 
		 int span=0, min=10000000000, max=0, n50=0, halfSpan=0, checkSpan=0, nonATGC=0, num=0, ninetySpan=0, counter=0;
		 def n50_list = [], n90_list = [];
		 float gc
		 	 
		 def n50check = false, n90check = false;
		 def genome_stats = [:]
	 	 //span
	 	 genomeInfo.each {
	 	 	num ++
			span += it.length
			gc += it.gc
			nonATGC += it.non_atgc
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
		 ninetySpan = span/100*90
		 genomeInfo.each {
		 	counter++
			checkSpan += it.length
			if (checkSpan >= halfSpan && n50check !=true){
				def aa = [counter,checkSpan/1000000,it.length]
				n50 = it.length
				n50check = true
				n50_list.add(aa)
			}
			if (checkSpan >= ninetySpan && n90check !=true){
				def aa = [counter,checkSpan/1000000,it.length]
				n90_list.add(aa)
				n90check = true
			}
		 }
		 
		 println "n50 = "+n50_list
		 println "n90 = "+n90_list
		 
		 def genomeDescSql = "select file_version,file_data.description from file_data,meta_data where file_data.meta_id = meta_data.id and meta_data.id = '"+Gid+"' and file_type = 'Genome';";
		 def genomeDesc = sql.rows(genomeDescSql)
		 
		 genome_stats.version = genomeDesc.file_version[0]
    	 genome_stats.description = genomeDesc.description[0]
		 genome_stats.num = num
	 	 genome_stats.span = span
	 	 genome_stats.n50 = n50
	 	 genome_stats.min = min
		 genome_stats.max = max
		 genome_stats.gc = gc
		 genome_stats.nonATGC = nonATGC
		 
		 //get gene stats
		 def geneInfoSql = "select nuc,gc from gene_info,file_data,meta_data where file_id = file_data.id and meta_id = meta_data.id and  meta_data.id = '"+Gid+"';";
	 	 println geneInfoSql
	 	 def geneInfo = sql.rows(geneInfoSql) 
	 	 int mean=0
	 	 min=10000000000
	 	 max=0
	 	 nonATGC=0
	 	 gc=0
		 def stats = [:]
		 def gene_stats = [:]
		 
		 if (geneInfo){
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
			 
			for (a in metaData.files){
				if (a.file_type == "Genes"){
					def msql = "select count(distinct(gene_id)) as g, count(distinct(mrna_id)) as m, count(distinct(pep)) as p from gene_info,file_data where file_id = file_data.id and file_data.id = '"+a.id+"' ;";
					println msql
					def m = sql.rows(msql)
					stats.Genes = m.g[0]
					stats.mRNA = m.m[0]
					stats.Peptide = m.p[0]
					gene_stats.genenum = m.g[0]
					gene_stats.mrnanum = m.m[0]
				}else if (a.file_type == "Transcriptome"){
					def msql = "select count(contig_id) as t from trans_info,file_data where file_id = file_data.id and file_data.id = '"+a.id+"' ;";
					def m = sql.rows(msql)
					stats.Transcriptome = m.t[0]
				}else if (a.file_type == "Genome"){
					def msql = "select count(contig_id) as c from genome_info,file_data where file_id = file_data.id and file_data.id = '"+a.id+"' ;";
					def m = sql.rows(msql)
					stats.Genome = m.c[0]
				}
			}
		 }
		 
		 //get data for plots
		 //def funAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_anno,gene_info,file_data,meta_data where gene_anno.gene_id = gene_info.id and gene_info.file_id = file_data.id and meta_id = '"+params.id+"' group by anno_db;";
		 def funAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_info left outer join gene_anno on (gene_info.id = gene_anno.gene_id),file_data,meta_data where gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id and meta_data.id = '"+Gid+"' group by anno_db;";
		 println funAnnoSql
		 def funAnnoData = sql.rows(funAnnoSql)
		 //def blastAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_blast,gene_info,file_data,meta_data where gene_blast.gene_id = gene_info.id and gene_info.file_id = file_data.id and meta_id = '"+params.id+"' group by anno_db;";
		 def blastAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_info left outer join gene_blast on (gene_info.id = gene_blast.gene_id),file_data,meta_data where gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id and meta_data.id = '"+Gid+"' group by anno_db;";		
		 println blastAnnoSql
		 def blastAnnoData = sql.rows(blastAnnoSql)
    	//return [n50: n50_list, n90: n90_list, meta: metaData, stats: stats, funAnnoData: funAnnoData, blastAnnoData: blastAnnoData, gene_stats: gene_stats, genome_stats: genome_stats]
    	 return [n50: n50_list, n90: n90_list, meta: metaData, stats: stats, funAnnoData: funAnnoData, blastAnnoData: blastAnnoData, gene_stats: gene_stats, genome_stats: genome_stats, genomeInfo: genomeInfo]
    }
    def all_search = {
         if (grailsApplication.config.i.links.all == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
     		def sql = new Sql(dataSource)
     		def gsql = "select count(gene_id) as g_count,count(mrna_id) as m_count,genus,species,meta_data.id from gene_info,file_data,meta_data where gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id group by genus,species,meta_data.id;"
     		def genes = sql.rows(gsql)
     		def tsql = "select count(contig_id) as t_count,genus,species,meta_data.id from trans_info,file_data,meta_data where trans_info.file_id = file_data.id and file_data.meta_id = meta_data.id group by genus,species,meta_data.id;"
     		def trans = sql.rows(tsql)
     		return [genes:genes,trans:trans]
     	}
    }
     def all_searched = {
        if (grailsApplication.config.i.links.all == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def timeStart = new Date()
			def sql = new Sql(dataSource)
			println "Searching all databases for "+params.searchId
			
			//search transcriptome annotations
			def transannosearch = "select distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank FROM trans_anno, plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query;"      
			println "Trans anno search = "+transannosearch
			def transanno = sql.rows(transannosearch)
			def transblastsearch = "select distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank FROM trans_blast, plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query;"      
			def transblast = sql.rows(transblastsearch)
			def transRes = transanno + transblast
			
			//search gene annotations
			def geneannosearch = "select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank, genus, species, meta_data.id as gid FROM gene_anno,gene_info,file_data,meta_data,plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and gene_anno.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id;"
			println "Gene anno search = "+geneannosearch
			def geneanno = sql.rows(geneannosearch)
			def geneblastsearch = "select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank, genus, species, meta_data.id as gid FROM gene_blast,gene_info,file_data,meta_data,plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and gene_blast.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id;"
			println "Gene blast search = "+geneblastsearch
			//select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank FROM gene_anno,gene_info,plainto_tsquery('globin') AS query WHERE textsearchable_index_col @@ query and gene_anno.gene_id = gene_info.id;
			def geneblast = sql.rows(geneblastsearch)
			def geneRes = geneanno + geneblast
			
			//search publications
			def pubsearch = "select distinct on (pubmed_id,date_out) pubmed_id,abstract_text,title,authors,journal_short,to_char(date_string,'yyyy Mon dd') as date_out,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank from publication , plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query order by date_out,pubmed_id desc, rank desc;"								
			def pubRes = sql.rows(pubsearch)
			println "Publication search = "+pubsearch
			
			def timeStop = new Date()
			def TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
			
			def annoLinksSql = "select source,regex,link from anno_data;";
			println annoLinksSql
			def annoLinksAll = sql.rows(annoLinksSql)
			def annoLinks = [:]
			annoLinksAll.each{
				annoLinks."${it.source}" = [it.regex,it.link]
			}
			println "Anno links =  "+annoLinks
			
			return [searchId: params.searchId, search_time: duration, transRes: transRes, geneRes: geneRes, pubRes: pubRes, annoLinks:annoLinks]
		}
		sql.close()
    }

    def trans_search = { 
    	if (grailsApplication.config.i.links.trans == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	 }else{  
			 def blastMap = [:]
			 if (grailsApplication.config.t.blast.size()>0){
				for(item in grailsApplication.config.t.blast){
					item = item.toString()
					def splitter = item.split("=",2)
					blastMap[splitter[0]] = splitter[1]
				}
			 }
			 println "blastMap = "+blastMap
			 
			 def funMap = [:]
			 if (grailsApplication.config.t.fun.size()>0){
				for(item in grailsApplication.config.t.fun){
					item = item.toString()
					def splitter = item.split("=",2)
					funMap[splitter[0]] = splitter[1]
				}
			 }
			 println "funMap = "+funMap
			 
			 def iprMap = [:]
			 if (grailsApplication.config.t.IPR.size()>0){
				iprMap.IPR = grailsApplication.config.t.IPR
			 }
			 println "iprMap = "+iprMap
			 return [blastMap: blastMap, funMap: funMap, iprMap: iprMap]
		}
    }
    
    def gene_search = {
         if (grailsApplication.config.i.links.genes == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	 }else{  
			 def blastMap = [:]
			 if (grailsApplication.config.g.blast.size()>0){
				for(item in grailsApplication.config.g.blast){
					item = item.toString()
					def splitter = item.split("=",2)
					blastMap[splitter[0]] = splitter[1]
				}
			 }
			 println "blastMap = "+blastMap
			 
			 def funMap = [:]
			 if (grailsApplication.config.g.fun.size()>0){
				for(item in grailsApplication.config.g.fun){
					item = item.toString()
					def splitter = item.split("=",2)
					funMap[splitter[0]] = splitter[1]
				}
			 }
			 println "funMap = "+funMap
			 
			 def iprMap = [:]
			 if (grailsApplication.config.g.IPR.size()>0){
				iprMap.IPR = grailsApplication.config.g.IPR
			 }
			 println "iprMap = "+iprMap
			 return [blastMap: blastMap, funMap: funMap, iprMap: iprMap]
		}
    }

    def trans_search_results = {
    	if (grailsApplication.config.i.links.trans == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def sql = new Sql(dataSource)
			//set up some global search things
			def timeStart = new Date()
			def table = params.dataSet
			def searchId = params.searchId   
			def annoSearch = "(anno_db = "
			def whatSearch
			def annoType = params.toggler
			println "annoType = "+annoType
			def annoDB
			if (annoType == '1'){
				whatSearch = params.tableSelect_1
				annoDB = params.blastAnno
				//choose what to search			
				if (whatSearch == 'e.g. ATPase'){whatSearch = 'descr ~* '}
				if (whatSearch == 'e.g. 215283796 or P31409'){whatSearch = 'anno_id ~* '}
				if (whatSearch == 'e.g. contig_1'){whatSearch = 'contig_id = '}
			}
			if (annoType == '2'){
				whatSearch = params.tableSelect_2
				annoDB = params.funAnno
				//choose what to search			
				if (whatSearch == 'e.g. Calcium-transportingATPase'){whatSearch = 'descr ~* '}
				if (whatSearch == 'e.g. GO:0008094 or 3.6.3.8 or K02147'){whatSearch = 'anno_id ~* '}
				if (whatSearch == 'e.g. contig_1'){whatSearch = 'contig_id = '}
			}
			if (annoType == '3'){
				whatSearch = params.tableSelect_3
				annoDB = params.iprAnno
				//choose what to search			
				if (whatSearch == 'e.g. Vacuolar (H+)-ATPase G subunit'){whatSearch = 'descr ~* '}
				if (whatSearch == 'e.g. IPR023298 or PF01813'){whatSearch = 'anno_id ~* '}
				if (whatSearch == 'e.g. contig_1'){whatSearch = 'contig_id = '}
			}
			println "annoDB = "+annoDB
			//construct the anno_db search string			
			//if just one db is passed then the list becomes a string
			if (annoDB){
				if (annoDB instanceof String){
					annoSearch += "\'" + annoDB + "\'"
				}else{
					def annoSelect = annoDB		
					annoSelect.each {
						annoSearch += "\'" + it + "\'" + " or anno_db = "
					}
					annoSearch = annoSearch[0..-15]			
				}
				annoSearch += ")"
			}
			
			if (!annoDB){
				return [error: "no_anno"]
			}
			//check for single letter searches
			else if (searchId.size() < 2){
					return [error: "too_short"]
			//check for empty searches
			}else if (searchId ==""){
				return [error: "empty"]
			}else{   
				def sqlsearch
				println "search = "+params.toggler
				if (params.toggler == "1"){
					println "Searching blast data"
					sqlsearch = "select distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score,gaps,hit_start,hit_stop,hseq,identity,midline,positive,qseq,align from trans_blast where "+annoSearch+" and "+whatSearch+ "'${searchId}';"       		        		
				}else{
					println "Searching anno data"
					sqlsearch = "select distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score from trans_anno where "+annoSearch+" and "+whatSearch+ "'${searchId}';"       		
				}
				println sqlsearch
				def results_all = sql.rows(sqlsearch)
				//count the number of unique hits
				def hits = []
				results_all.each {
					hits.add(it.contig_id)
				}
				def uniques = hits.unique()
				def results
				if (results_all.size() > 0){
					results = results_all.sort({-it.score as double})
				}else{
					results = results_all
				}
				def timeStop = new Date()
				def TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
				return [results: results, term : searchId , search_time: duration, uniques:uniques.size(),sql:sqlsearch, annoType: annoType]         
			}
			sql.close()	
		  }		  
		}
         
    def gene_search_results = {
        if (grailsApplication.config.i.links.genes == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def sql = new Sql(dataSource)
			//set up some global search things
			def timeStart = new Date()
			def metaData = MetaData.findById(params.Gid);
			def searchfile_id = params.dataSelect
			def table = params.dataSet
			def searchId = params.searchId   
			def annoSearch = "(anno_db = "
			def whatSearch
			def annoType = params.toggler
			println "annoType = "+annoType
			def annoDB
			if (annoType == '1'){
				whatSearch = params.tableSelect_1
				annoDB = params.blastAnno
				//choose what to search			
				if (whatSearch == 'e.g. ATPase'){whatSearch = 'descr ~* '}
				if (whatSearch == 'e.g. 215283796 or P31409'){whatSearch = 'anno_id ~* '}
				if (whatSearch == 'e.g. contig_1'){whatSearch = 'contig_id = '}
			}
			if (annoType == '2'){
				whatSearch = params.tableSelect_2
				annoDB = params.funAnno
				//choose what to search			
				if (whatSearch == 'e.g. Calcium-transportingATPase'){whatSearch = 'descr ~* '}
				if (whatSearch == 'e.g. GO:0008094 or 3.6.3.8 or K02147'){whatSearch = 'anno_id ~* '}
				if (whatSearch == 'e.g. contig_1'){whatSearch = 'contig_id = '}
			}
			if (annoType == '3'){  	 			
				whatSearch = params.tableSelect_3
				annoDB = params.iprAnno
				//choose what to search			
				if (whatSearch == 'e.g. Vacuolar (H+)-ATPase G subunit'){whatSearch = 'descr ~* '}
				if (whatSearch == 'e.g. IPR023298 or PF01813'){whatSearch = 'anno_id ~* '}
				if (whatSearch == 'e.g. contig_1'){whatSearch = 'contig_id = '}
			}
			//def annoLinks = configDataService.getGeneAnnoLinks()
			//def annoLinks = AnnoDataFindByFiledata_id(file_id)
			def annoLinksSql = "select source,regex,link from anno_data where filedata_id = '"+searchfile_id+"';";
			def annoLinksAll = sql.rows(annoLinksSql)
			def annoLinks = [:]
			annoLinksAll.each{
				annoLinks."${it.source}" = [it.regex,it.link]
			}
			println "Anno links =  "+annoLinks
			println "annoDB = "+annoDB
			//construct the anno_db search string			
			//if just one db is passed then the list becomes a string
			if (annoDB){
				if (annoDB instanceof String){
					annoSearch += "\'" + annoDB + "\'"
				}else{
					def annoSelect = annoDB		
					annoSelect.each {
						annoSearch += "\'" + it + "\'" + " or anno_db = "
					}
					annoSearch = annoSearch[0..-15]			
				}
				annoSearch += ")"
			}
			
			if (!annoDB){
				return [error: "no_anno"]
			}
			//check for single letter searches
			else if (searchId.size() < 2){
					return [error: "too_short"]
			//check for empty searches
			}else if (searchId ==""){
				return [error: "empty"]
			}else{
				def sqlsearch
				println "search = "+params.toggler
				if (params.toggler == "1"){
					println "Searching blast data"
					sqlsearch = "select distinct on (anno_db,gene_info.mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,gaps,hit_start,hit_stop,hseq,identity,midline,positive,qseq,align from gene_blast,gene_info,file_data where "+annoSearch+" and "+whatSearch+ "'${searchId}' and gene_blast.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.id = '"+searchfile_id+"';"       		        		
				}else{
					println "Searching anno data"
					sqlsearch = "select distinct on (anno_db,gene_info.mrna_id) anno_db,mrna_id,anno_id,anno_start,anno_stop,descr,score from gene_anno,gene_info,file_data where "+annoSearch+" and "+whatSearch+ "'${searchId}' and gene_anno.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.id = '"+searchfile_id+"';";
				}
				println sqlsearch
				def results_all = sql.rows(sqlsearch)
				//count the number of unique hits
				def hits = []
				results_all.each {
					hits.add(it.mrna_id)
				}
				def uniques = hits.unique()
				def results
				if (results_all.size() > 0){
					results = results_all.sort({-it.score as double})
				}else{
					results = results_all
				}
				def timeStop = new Date()
				def TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
				return [metaData:metaData, results: results, term : searchId , search_time: duration, uniques:uniques.size(),sql:sqlsearch, annoType: annoType, annoLinks: annoLinks]         
			}
			sql.close()
		}
      }
    @Cacheable('m_cache')
    //@CacheEvict(value='m_cache', allEntries=true)
    def m_info() {
    	if (grailsApplication.config.i.links.genes == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def sql = new Sql(dataSource)
			def mrna_id = params.mid
			def Gid
			if (!params.Gid){
				def GidSql = "select meta_data.id from gene_info,file_data,meta_data where gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id and mrna_id = '"+mrna_id+"';";
				print "Getting species id "+GidSql
				Gid = sql.rows(GidSql).id[0]
			}else{
				Gid = params.Gid
			}	
			def metaData = MetaData.findById(Gid);
			println "metaData = "+metaData
			def blast_results
			def fun_results
			def ipr_results
			//if (grailsApplication.config.g.blast.size()>0){			
				def blastsql = "select gene_info.mrna_id,gene_info.gene_id,gene_blast.* from gene_blast,gene_info where gene_blast.gene_id = gene_info.id and mrna_id = '"+mrna_id+"' order by score desc;";
				println blastsql
				blast_results = sql.rows(blastsql)
			//}
			//if (grailsApplication.config.g.IPR){
				def iprsql = "select gene_anno.* from gene_anno,gene_info where anno_id ~ '^IPR' and gene_anno.gene_id = gene_info.id and mrna_id = '"+mrna_id+"' order by score;";
				println iprsql
				ipr_results = sql.rows(iprsql)
			//}
			//if (grailsApplication.config.g.fun.size()>0){
				def funsql = "select gene_anno.* from gene_anno,gene_info where anno_id !~ '^IPR' and gene_anno.gene_id = gene_info.id and mrna_id = '"+mrna_id+"' order by score desc;";
				println funsql
				fun_results = sql.rows(funsql)
			//}
			//get exon info
			def exonsql = "select contig_id,exon_info.*,length(exon_info.sequence) as length from exon_info,gene_info where exon_info.gene_id = gene_info.id and mrna_id = '"+mrna_id+"' order by exon_number;"
			def exon_results = sql.rows(exonsql)
			println exonsql
			
			//def fsql = "select file_id from gene_info where mrna_id = '"+mrna_id+"';";
			//println fsql
			//def fid = sql.rows(fsql)
			//println "fid = "+fid.file_id[0]
			//def annoLinksSql = "select source,regex,link from anno_data where filedata_id = '"+fid.file_id[0]+"';";
			def annoLinksSql = "select source,regex,link from anno_data";
			println annoLinksSql
			def annoLinksAll = sql.rows(annoLinksSql)
			def annoLinks = [:]
			annoLinksAll.each{
				annoLinks."${it.source}" = [it.regex,it.link]
			}
			println "Anno links =  "+annoLinks

			//get amino acid info`
			def info_results = GeneInfo.findAllByMrna_id(mrna_id)
			def aaData
			info_results.each {
				aaData = peptideService.getComp(it.pep)
				//println "service = "+service
			}	
			return [mrna_id: mrna_id, info_results: info_results, ipr_results: ipr_results, blast_results: blast_results, fun_results: fun_results, annoLinks: annoLinks, exon_results: exon_results, aaData:aaData, metaData: metaData]
    	sql.close()
    	}
    }
    def trans_info = {
    	if (grailsApplication.config.i.links.trans == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def sql = new Sql(dataSource)
			def blastDBs = "anno_db = "
			if (grailsApplication.config.t.blast.size()>0){
				for(item in grailsApplication.config.t.blast){
				item = item.toString()
					def splitter = item.split("=",2)
					blastDBs += "'"+splitter[0]+"' or anno_db = "
					//println "adding "+splitter[0]
				}
				blastDBs = blastDBs[0..-15]
			}
			
			def funDBs = "anno_db = "
			if (grailsApplication.config.t.fun.size()>0){
				for(item in grailsApplication.config.t.fun){
				item = item.toString()
					def splitter = item.split("=",2)
					funDBs += "'"+splitter[0]+"' or anno_db = "
					//println "adding "+splitter[0]
				}
				funDBs = funDBs[0..-15]
			}
			def blast_results
			def fun_results
			def ipr_results
			if (grailsApplication.config.t.blast.size()>0){			
				def blastsql = "select * from trans_blast where ("+blastDBs+") and contig_id = '"+params.contig_id+"' order by score desc;";
				println blastsql
				blast_results = sql.rows(blastsql)
			}
			if (grailsApplication.config.t.IPR){
				def iprsql = "select * from trans_anno where (anno_id ~ '^IPR' or anno_db = 'IPRGO') and contig_id = '"+params.contig_id+"' order by score;";
				ipr_results = sql.rows(iprsql)
				println iprsql
			}
			if (grailsApplication.config.t.fun.size()>0){
				def funsql = "select * from trans_anno where ("+funDBs+") and contig_id = '"+params.contig_id+"' order by score desc;";
				println funsql
				fun_results = sql.rows(funsql)
			}
			def info_results = TransInfo.findAllByContig_id(params.contig_id)
			return [ info_results: info_results, ipr_results: ipr_results, blast_results: blast_results, fun_results: fun_results]
    	sql.close()
    	}
	}
    def genome_info = {
       	if (grailsApplication.config.i.links.genome == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
     		def sql = new Sql(dataSource)
     		def Gid
     		//coming out from the blast result there is no easy way to get the species id, so use the contig_id (very risky as possible duplicate ids!) 
     		if (params.Gid){
     			Gid = params.Gid;
     		}else{
     			def getGidSql = "select file_data.meta_id from file_data,genome_info where genome_info.file_id = file_data.id and genome_info.contig_id = '"+params.contig_id+"';";
     			Gid = sql.rows(getGidSql).meta_id[0]
     		}
     		def metaData = MetaData.findById(Gid); 
     		//def gene_results = GeneInfo.findAllByContig_id(params.contig_id)
     		//def genesql = "select gene_info.* from gene_info,file_data,meta_data where gene_info.contig_id = '"+params.contig_id+"' and meta_data.id = '"+Gid+"' and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id;"
     		def genesql = "select gene_info.gene_id, count(mrna_id), avg(length(nuc)) as a_nuc, avg(start) as a_start, avg(stop) as a_stop from gene_info,file_data,meta_data where gene_info.contig_id = '"+params.contig_id+"' and meta_data.id = '"+Gid+"' and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id group by gene_info.gene_id order by a_start;";
     		def gene_results = sql.rows(genesql)
     		println genesql
     		//println "contig_id ="+params.contig_id
			//def info_results = GenomeInfo.findAllByContig_id(params.contig_id)
			def infosql = "select genome_info.* from genome_info,file_data,meta_data where genome_info.contig_id = '"+params.contig_id+"' and meta_data.id = '"+Gid+"' and genome_info.file_id = file_data.id and file_data.meta_id = meta_data.id;";
			def info_results = sql.rows(infosql)
			return [ info_results: info_results, gene_results: gene_results, metaData:metaData, Gid:Gid]
		}
    }
    def g_info = {
       	if (grailsApplication.config.i.links.genome == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
     		def sql = new Sql(dataSource)
     		def Gid
			if (!params.Gid){
				def GidSql = "select meta_data.id from gene_info,file_data,meta_data where gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id and gene_id = '"+params.gid+"';";
				print "Getting species id "+GidSql
				Gid = sql.rows(GidSql).id[0]
			}else{
				Gid = params.Gid
			}	
     		def metaData = MetaData.findById(Gid); 
     		//def results = GeneInfo.findAllByGene_id(params.gid)
     		def genesql = "select gene_info.* from gene_info,file_data,meta_data where gene_info.gene_id = '"+params.gid+"' and meta_data.id = '"+Gid+"' and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id;"
     		def gene_results = sql.rows(genesql)
     		//println "g = "+gene_results
			return [ results: gene_results, metaData:metaData]
		}
    }
	def gene_link = {
		def sql = new Sql(dataSource)
       	if (grailsApplication.config.i.links.genes == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
     		def timeStart = new Date()
     		def val = params.val
     		def type = params.annoType
     		def gene_sql
     		if (type == "Blast"){
     			if (val == "None"){
     				gene_sql = "select gene_info.gene_id,gc,length(pep) as lpep, length(nuc) as lnuc, strand from gene_info left outer join gene_blast on (gene_info.gene_id = gene_blast.gene_id) where anno_db is NULL;"
     			}else{
     				gene_sql = "select distinct on (gene_id,anno_db) gene_id,anno_db,anno_start,anno_stop,anno_id,score,descr,gaps,align,hit_start,hit_stop,hseq,identity,midline,positive,qseq from gene_blast where anno_db = '"+val+"';"
     			}
     		}
     		else if (type == "Functional" || type == "IPR"){
     			if (val == "None"){
     				gene_sql = "select gene_info.gene_id,gc,length(pep) as lpep, length(nuc) as lnuc, strand from gene_info left outer join gene_anno on (gene_info.gene_id = gene_anno.gene_id) where anno_db is NULL;"
     			}else{
     				gene_sql = "select distinct on (gene_id,anno_db) gene_id,anno_db,anno_id,score,descr from gene_anno where anno_db = '"+val+"';"
     			}
     		}
     		else if (type == "Length"){
     			gene_sql = "select gene_id,gc,length(pep) as lpep, length(nuc) as lnuc, strand from gene_info where length(pep) = '"+val+"';"
     		}
     		else if (type == "Exon_num"){
     			gene_sql = "select gene_id,gc,length(pep) as lpep, length(nuc) as lnuc, strand from gene_info where gene_id in (select gene_id from exon_info group by gene_id having count(exon_id) = '"+val+"');"
     		}else if (type == "Exon_length"){
     			gene_sql = "select gene_info.gene_id,gene_info.gc,length(pep) as lpep, length(nuc) as lnuc, gene_info.strand from gene_info,exon_info where gene_info.gene_id = exon_info.gene_id and length(exon_info.sequence) = '"+val+"';"
     		}

     		println gene_sql
     		def gene_results = sql.rows(gene_sql)
     		
     		//get anno links
     		def annoLinks = configDataService.getGeneAnnoLinks()
     		println "links = "+annoLinks
     		
     		def timeStop = new Date()
			def TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
			return [ val: val, gene_results: gene_results, search_time: duration, annoLinks: annoLinks, annoType: type]
			sql.close()
		}
    }
}
