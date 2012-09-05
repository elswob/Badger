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
    def trans_search = {   
    	 def sql = new Sql(dataSource)
     	 def sqlsearch = "select contig_id,gc,length,coverage from trans_info order by length desc;"
     	 def funsearch = "select trans_anno.contig_id,gc,length,coverage,anno_db,anno_id from trans_info,trans_anno where (anno_db = 'EC' or anno_db = 'KEGG' or anno_db = 'GO' or anno_id ~ '^IPR') and trans_anno.contig_id = trans_info.contig_id order by length desc;"
     	 def results = sql.rows(sqlsearch)
     	 def funresults = sql.rows(funsearch)
     	 def blastMap = [:]
     	 if (grailsApplication.config.t.blast.size()>0){
     	 	for(item in grailsApplication.config.t.blast){
     	 		item = item.toString()
     	 		def splitter = item.split("=")
     	 		blastMap[splitter[0]] = splitter[1]
     	 	}
     	 }
     	 println "blastMap = "+blastMap
     	 return [ transData: results, funData: funresults, blastMap: blastMap]
    }
    @Secured(['ROLE_USER'])
    def gene_search = {
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
    def genome_search = {   
    	 def sql = new Sql(dataSource)
     	 def sqlsearch = "select contig_id,gc,length,coverage from genome_info order by length desc;"
     	 def results = sql.rows(sqlsearch)
     	 //println results
     	 return [ genomeData: results]
    }
    //@Secured(['ROLE_USER','ROLE_ADMIN'])
    def search_results = {
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
            
        //check for gene annotation data
        }else if (table == 'Genes' || params.search =='gene'){
			def results_all = GeneAnno.findAllByDescrLike("%${searchId}%")	
			def results
			if (results_all.size() > 0){
            	results = results_all.sort({-it.score as double})
            }else{
            	results = results_all
            }
			def timeStop = new Date()
			def TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
			return [ anno: "yes", results: results, term : searchId , search_time: duration ]
			
        //check for transciptome annotation data
        }else if (table == 'Transcripts' || params.search =='trans'){
        	def sqlsearch = "select distinct on (anno_db,contig_id) contig_id,anno_id,anno_db,anno_start,anno_stop,descr,score from trans_anno where "+annoSearch+" and "+whatSearch+ "'${searchId}';"
        	def chartsearch = "select distinct on (trans_anno.contig_id,gc,length,coverage,score) trans_anno.contig_id,gc,length,coverage,score from trans_anno,trans_info where "+annoSearch+" and "+whatSearch+ "'${searchId}' and trans_info.contig_id = trans_anno.contig_id;"
        	println sqlsearch
        	println chartsearch
        	def results_all = sql.rows(sqlsearch)
        	def chart_results = sql.rows(chartsearch)
            //count the number of unique hits
            def hits = []
            results_all.each {
            	hits.add(it.contig_id)
            }
            //interpro scores are a problem for the chart so convert all to 1 (not great)
            chart_results.each {
            	if (it.score < 1){
            		it.score = 1;
            	}
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
            return [ uni: "yes", results: results, term : searchId , search_time: duration, uniques:uniques.size(),sql:sqlsearch, annoType: annoType, chartData: chart_results]
            
            
        //check for ncrna data
    	}else if (table == 'ncRNA'){            
        
        //go straight to a genomic contig page
        }else if (table == 'scaffoldID' || params.search == 'contig'){
            def contigs = Contig.findAllByContig_idLike(searchId)
            def genes = GeneInfo.findAllByContig_idLike(searchId)
            def fasta = ">"+params.contig_id+"\n"+contigs.sequence[0]+"\n"
            return [ contig: "yes", contigs: contigs, term : searchId, fasta : fasta, genes: genes ]
        
        //go straight to a gene page
        }else if (table == 'geneID'){
        	redirect(action: "gene_info", params: [gene_id: searchId])          
        }
    }
    @Secured(['ROLE_USER'])
    def gene_info = {
    	def offint = params.offset as Integer 
        def maxint = params.max as Integer
        def sum = maxint + offint
        def info_results = GeneInfo.findAllByGene_id(params.gene_id)
        def anno_results = GeneAnno.findAllByGene_id(params.gene_id,[sort:"score", order:"desc", max:params.max, offset:params.offset])
        def nuc_fasta = ">"+info_results.gene_id[0]+"\n"+info_results.nuc[0]+"\n"
        def pep_fasta = ">"+info_results.gene_id[0]+"\n"+info_results.pep[0]+"\n"
        return [ info_results: info_results, anno_results: anno_results, nuc_fasta: nuc_fasta, pep_fasta: pep_fasta]
    }
    def trans_info = {
    	def sql = new Sql(dataSource)
    	def blastDBs = "anno_db = "
     	if (grailsApplication.config.t.blast.size()>0){
     		for(item in grailsApplication.config.t.blast){
     		item = item.toString()
     			def splitter = item.split("=")
     			blastDBs += "'"+splitter[0]+"' or anno_db = "
     			println "adding "+splitter[0]
     		}
     	}
     	blastDBs = blastDBs[0..-15]
     	def blastsql = "select * from trans_anno where ("+blastDBs+") and contig_id = '"+params.contig_id+"' order by score desc;";
    	println blastsql
     	def blast_results = sql.rows(blastsql)
    	def iprsql = "select * from trans_anno where anno_id ~ '^IPR' and contig_id = '"+params.contig_id+"' order by score;";
    	def ipr_results = sql.rows(iprsql)
    	def a8rsql = "select * from trans_anno where (anno_db = 'EC' or anno_db = 'GO' or anno_db = 'KEGG') and contig_id = '"+params.contig_id+"' order by score desc;";
    	def a8r_results = sql.rows(a8rsql)
        def info_results = TransInfo.findAllByContig_id(params.contig_id)
        def nuc_fasta = ">"+info_results.contig_id[0]+"\n"+info_results.sequence[0]+"\n"
        return [ info_results: info_results, ipr_results: ipr_results, blast_results: blast_results, a8r_results: a8r_results, nuc_fasta: nuc_fasta]
    }
    def contig_info = {
        def info_results = GenomeInfo.findAllByContig_id(params.contig_id)
        def nuc_fasta = ">"+info_results.contig_id[0]+"\n"+info_results.sequence[0]+"\n"
        return [ info_results: info_results, nuc_fasta: nuc_fasta]
    }
}
