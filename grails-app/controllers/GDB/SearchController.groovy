package GDB
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql

class SearchController {
	def matcher
	def grailsApplication
	javax.sql.DataSource dataSource
    //@Secured(['ROLE_USER'])
    def index() {
    }
    def all_search = {
         if (grailsApplication.config.i.links.all == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}
    }
     def all_searched = {
        if (grailsApplication.config.i.links.all == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def timeStart = new Date()
			def sql = new Sql(dataSource)
			println "Searching all databases for "+params.searchId
			
			//def transsearch = "SELECT distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score FROM trans_anno WHERE to_tsvector(descr || ' ' || anno_id) @@ to_tsquery('"+params.searchId+"')";
			def transsearch = "select distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank FROM trans_anno, plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query;"      
			def transRes = sql.rows(transsearch)
			println "Trans search = "+transsearch
			
			//def genesearch = "SELECT distinct on (anno_db,gene_id) gene_id,anno_id,anno_db,anno_start,anno_stop,descr,score FROM gene_anno WHERE to_tsvector(descr || ' ' || anno_id) @@ to_tsquery('"+params.searchId+"')";
			def genesearch = "select distinct on (anno_db,gene_id) gene_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank FROM gene_anno, plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query;"
			def geneRes = sql.rows(genesearch)
			println "Gene search = "+genesearch
			
			//def pubsearch = "SELECT * FROM publication WHERE textsearchable_index_col @@ to_tsquery('"+params.searchId+"')";
			def pubsearch = "SELECT *, to_char(date_string,'yyyy Mon dd') as date_out, ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank FROM publication, plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query ORDER BY rank DESC;"
			def pubRes = sql.rows(pubsearch)
			println "Publication search = "+pubsearch
			
			def timeStop = new Date()
			def TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
			return [searchId: params.searchId, search_time: duration, transRes: transRes, geneRes: geneRes, pubRes: pubRes]
		}
    }

    def trans_search = { 
    	if (grailsApplication.config.i.links.trans == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	 }else{  
			 def blastMap = [:]
			 if (grailsApplication.config.t.blast.size()>0){
				for(item in grailsApplication.config.t.blast){
					item = item.toString()
					def splitter = item.split("=")
					blastMap[splitter[0]] = splitter[1]
				}
			 }
			 println "blastMap = "+blastMap
			 
			 def funMap = [:]
			 if (grailsApplication.config.t.fun.size()>0){
				for(item in grailsApplication.config.t.fun){
					item = item.toString()
					def splitter = item.split("=")
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
					println "size = "+grailsApplication.config.g.blast.size()
					item = item.toString()
					def splitter = item.split("=")
					blastMap[splitter[0]] = splitter[1]
				}
			 }
			 println "blastMap = "+blastMap
			 return [blastMap: blastMap]
		}
    }
    def genome_search = {
    	if (grailsApplication.config.i.links.genome == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{   
			 def sql = new Sql(dataSource)
			 def sqlsearch = "select contig_id,gc,length,coverage from genome_info order by length desc;"
			 def results = sql.rows(sqlsearch)
			 //println results
			 return [ genomeData: results]
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
		  }
		}
    
     
    def gene_search_results = {
        if (grailsApplication.config.i.links.genes == 'private' && !isLoggedIn()) {
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
				annoDB = params.a8rAnno
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
				
				def sqlsearch
				println "search = "+params.toggler
				if (params.toggler == "1"){
					println "Searching blast data"
					sqlsearch = "select distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score,gaps,hit_start,hit_stop,hseq,identity,midline,positive,qseq,align from gene_blast where "+annoSearch+" and "+whatSearch+ "'${searchId}';"       		        		
				}else{
					println "Searching anno data"
					sqlsearch = "select distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score from gene_anno where "+annoSearch+" and "+whatSearch+ "'${searchId}';"       		
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
				return [ uni: "yes", results: results, term : searchId , search_time: duration, uniques:uniques.size(),sql:sqlsearch, annoType: annoType]         
			}
		}
      }
    
    def gene_info = {
    	if (grailsApplication.config.i.links.genes == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def offint = params.offset as Integer 
			def maxint = params.max as Integer
			def sum = maxint + offint
			def info_results = GeneInfo.findAllByGene_id(params.gene_id)
			def anno_results = GeneAnno.findAllByGene_id(params.gene_id,[sort:"score", order:"desc", max:params.max, offset:params.offset])
			def nuc_fasta = ">"+info_results.gene_id[0]+"\n"+info_results.nuc[0]+"\n"
			def pep_fasta = ">"+info_results.gene_id[0]+"\n"+info_results.pep[0]+"\n"
			return [ info_results: info_results, anno_results: anno_results, nuc_fasta: nuc_fasta, pep_fasta: pep_fasta]
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
					def splitter = item.split("=")
					blastDBs += "'"+splitter[0]+"' or anno_db = "
					//println "adding "+splitter[0]
				}
			}
			blastDBs = blastDBs[0..-15]
			
			def funDBs = "anno_db = "
			if (grailsApplication.config.t.fun.size()>0){
				for(item in grailsApplication.config.t.fun){
				item = item.toString()
					def splitter = item.split("=")
					funDBs += "'"+splitter[0]+"' or anno_db = "
					//println "adding "+splitter[0]
				}
			}
			funDBs = funDBs[0..-15]
			
			def blastsql = "select * from trans_blast where ("+blastDBs+") and contig_id = '"+params.contig_id+"' order by score desc;";
			println blastsql
			def blast_results = sql.rows(blastsql)
			def iprsql = "select * from trans_anno where (anno_id ~ '^IPR' or anno_db = 'IPRGO') and contig_id = '"+params.contig_id+"' order by score;";
			def ipr_results = sql.rows(iprsql)
			def funsql = "select * from trans_anno where ("+funDBs+") and contig_id = '"+params.contig_id+"' order by score desc;";
			println funsql
			def fun_results = sql.rows(funsql)
			def info_results = TransInfo.findAllByContig_id(params.contig_id)
			def nuc_fasta = ">"+info_results.contig_id[0]+"\n"+info_results.sequence[0]+"\n"
			return [ info_results: info_results, ipr_results: ipr_results, blast_results: blast_results, fun_results: fun_results, nuc_fasta: nuc_fasta]
    	}
	}
    def contig_info = {
       	if (grailsApplication.config.i.links.genome == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def info_results = GenomeInfo.findAllByContig_id(params.contig_id)
			def nuc_fasta = ">"+info_results.contig_id[0]+"\n"+info_results.sequence[0]+"\n"
			return [ info_results: info_results, nuc_fasta: nuc_fasta]
		}
    }
}
