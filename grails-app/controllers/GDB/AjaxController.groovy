package GDB
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql
import grails.plugin.cache.Cacheable

class AjaxController {
	def grailsApplication
	javax.sql.DataSource dataSource
	
	def getDataJSON = {
		def sql = new Sql(dataSource)
        def gene_id = params.gene_id
        def db = params.db
        def start = params.iDisplayStart.toLong()
        def limit = params.iDisplayLength.toLong()
        def sortIndex = params.iSortCol_0.toInteger()
        def sortDirection = params.sSortDir_0
        def searchString = params.sSearch
        def filteredCount
        def filtered
        
        println "geneId = "+gene_id+ " start = "+start+" limit = "+limit
		
		def funDBs = "(anno_db = "
		if (grailsApplication.config.g.fun.size()>0){
			for(item in grailsApplication.config.g.fun){
			item = item.toString()
				def splitter = item.split("=",2)
				funDBs += "'"+splitter[0]+"' or anno_db = "
			}
			funDBs = funDBs[0..-15]
			funDBs += ")"
		}
			
		def ajax_results
		def ajax_total
		def ajaxsql
		def total
		if (db == "blast"){		
			def sortColumnNames = ['alignment', 'anno_db', 'anno_id', 'descr', 'anno_start', 'anno_stop', 'score']			
			if (limit > 0 ){
				if (searchString){
					//def geneannosearch = "select distinct on (anno_db,gene_id) gene_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank FROM gene_anno, plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query;"
					ajaxsql = "select * from gene_blast, plainto_tsquery('"+searchString+"') AS query WHERE textsearchable_index_col @@ query and gene_id = '"+params.gene_id+"' order by "+sortColumnNames[sortIndex]+" "+sortDirection+" offset "+start+" limit "+limit+";";
					ajax_results = sql.rows(ajaxsql)
					filteredCount = ajax_results.size()		
					total = GeneBlast.findAllByGene_id(params.gene_id).size()							
				}else{
					ajaxsql = "select * from gene_blast where gene_id = '"+params.gene_id+"' order by "+sortColumnNames[sortIndex]+" "+sortDirection+" offset "+start+" limit "+limit+";";
					ajax_results = sql.rows(ajaxsql)
					total = GeneBlast.findAllByGene_id(params.gene_id).size()
					filteredCount = total
				}
			}else{	
				if (searchString){
					ajaxsql = "select * from (select distinct on (anno_db) * from gene_blast, plainto_tsquery('"+searchString+"') AS query WHERE textsearchable_index_col @@ query and gene_id = '"+params.gene_id+"' order by anno_db,score desc) as foo order by "+sortColumnNames[sortIndex]+" "+sortDirection+";"
					ajax_results = sql.rows(ajaxsql)
					total = GeneBlast.findAllByGene_id(params.gene_id).size()
					filteredCount = ajax_results.size()	
				}else{
					ajaxsql = "select * from (select distinct on (anno_db) * from gene_blast where gene_id = '"+params.gene_id+"' order by anno_db,score desc) as foo order by "+sortColumnNames[sortIndex]+" "+sortDirection+";"
					ajax_results = sql.rows(ajaxsql)
					total = GeneBlast.findAllByGene_id(params.gene_id).size()
					filteredCount = total
				}
			}
			println "ajax blast call - "+ajaxsql				
			def ajaxData = [
				iTotalRecords : total,
				iTotalDisplayRecords : filteredCount,
				sEcho : params.sEcho,
				aaData: ajax_results
			]
			render ajaxData.encodeAsJSON()
		}
		if (db == "fun"){
			def sortColumnNames = ['anno_db', 'anno_id', 'descr', 'anno_start', 'anno_stop', 'score']
			ajax_total = "select * from gene_anno where gene_id = '"+params.gene_id+"' and "+funDBs+";"
			total = sql.rows(ajax_total).size()			
			if (limit > 0 ){
				ajaxsql = "select * from gene_anno where gene_id = '"+params.gene_id+"' and "+funDBs+" order by "+sortColumnNames[sortIndex]+" "+sortDirection+" offset "+start+" limit "+limit+";";
				ajax_results = sql.rows(ajaxsql)
			}else{	
				ajaxsql = "select * from (select distinct on (anno_db) * from gene_anno where gene_id = '"+params.gene_id+"' and "+funDBs+" order by anno_db,score desc) as foo order by "+sortColumnNames[sortIndex]+" "+sortDirection+";"
				ajax_results = sql.rows(ajaxsql)
			}
			filtered = total
			println "fun ajax call - "+ajaxsql				
			def ajaxData = [
				iTotalRecords : total,
				iTotalDisplayRecords : filtered,
				sEcho : params.sEcho,
				aaData: ajax_results
			]
			render ajaxData.encodeAsJSON()
		}
		if (db == "ipr"){
			println "getting ipr data"	
			def sortColumnNames = ['anno_db', 'anno_id', 'descr', 'anno_start', 'anno_stop', 'score']
			ajax_total = "select * from gene_anno where gene_id = '"+params.gene_id+"' and anno_id ~ '^IPR';"
			total = sql.rows(ajax_total).size()				
			if (limit > 0 ){	
				ajaxsql = "select * from gene_anno where gene_id = '"+params.gene_id+"' and anno_id ~ '^IPR' order by "+sortColumnNames[sortIndex]+" "+sortDirection+" offset "+start+" limit "+limit+";";
				ajax_results = sql.rows(ajaxsql)
			}else{	
				ajaxsql = "select * from (select distinct on (anno_db) * from gene_anno where gene_id = '"+params.gene_id+"' and anno_id ~ '^IPR' order by anno_db,score asc) as foo order by "+sortColumnNames[sortIndex]+" "+sortDirection+";"
				ajax_results = sql.rows(ajaxsql)
			}
			filtered = total
			println "ipr ajax call - "+ajaxsql				
			def ajaxData = [
				iTotalRecords : total,
				iTotalDisplayRecords : filtered,
				sEcho : params.sEcho,
				aaData: ajax_results
			]
			render ajaxData.encodeAsJSON()
		}
    }
}
