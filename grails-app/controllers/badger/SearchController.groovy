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
	def alignService
	def springSecurityService
    //@Secured(['ROLE_USER'])
    
    def index = {
    	def sql = new Sql(dataSource)  
    	def psql = "select count(distinct(pubmed_id)) from publication;";
    	def p = sql.rows(psql) 
    	def osql = "select max(group_id) from ortho;";
    	def o = sql.rows(osql)
    	def metaData = MetaData.findAll()
    	return [metaData: metaData,pub:p,orth:o]
    }
    def species = {
    	def metaData = MetaData.findAll(sort:"genus")
    	def genomes = GenomeData.findAll()
    	//def fileData = FileData.findAll()
    	println "Number of species = "+metaData.size()
    	if (metaData.size() == 1 && !grailsApplication.config.t.file){
    		redirect(action: "species_v", params: [Sid: metaData.id])
    	}else{
    		return [meta: metaData, genomes: genomes]
    	}
    	//return [meta: metaData, genomes: genomes]
    }
    def species_v = {
    	def sql = new Sql(dataSource)   	
    	def roles = springSecurityService.getPrincipal()
    	def user
    	if (roles == 'anonymousUser'){
    		user = "anon"
    	}else{
    		user = "user"
    	}
    	//println "user = "+user
    	def meta = MetaData.findAllById(params.Sid)
    	def genomesSql = "select genome_data.*,file_data.description,file_data.file_version,file_data.id as Gid, file_data.search from genome_data,file_data where genome_data.meta_id = "+params.Sid+" and genome_data.id = file_data.genome_id and file_data.file_type = 'Genome' order by date_string desc;"
    	//println genomesSql
    	def genomes = sql.rows(genomesSql)
    	return [meta: meta, genomes:genomes, user:user] 	
    }
    
    def ajax_gff = {
    	def sql = new Sql(dataSource)
    	//println "link = "+params.link
    	//def genes = GenomeData.findAllByGenome_idAndFile_type(params.link,'Genes')
    	def genesSql = "select * from file_data where genome_id = "+params.link+" and file_type = 'Genes';"
    	print genesSql
    	def genes = sql.rows(genesSql)
    	//println "gff number = "+genes.size
    	def genomeSql = "select id from file_data where genome_id = "+params.link+" and file_type = 'Genome';"
    	def genome = sql.rows(genomeSql)
    	//println "Gid = "+genome.id[0]
    	render(template:"gffSelectResponse", model: [genes:genes,genome:genome.id[0]])
    }
    
    @Cacheable('species_cache')
    //@CacheEvict(value='species_cache', allEntries=true)
    def species_search() {
    	def sql = new Sql(dataSource)
    	def Gid = params.Gid
    	def genomeData = FileData.findById(Gid)	
    	def geneData = FileData.findById(params.GFFid)
    	//check for external data
    	def esql = "select ext_info.* from file_data,ext_info where file_data.source = ext_info.ext_id and file_data.id = "+params.GFFid+";";
    	def ext = sql.rows(esql)
    	def annoTypesSql = "select distinct(type) from anno_data where filedata_id = "+params.GFFid+";";
    	def annoTypes = sql.rows(annoTypesSql)
    	def interSql = "select distinct(anno_db) from gene_interpro;";
    	def inter = sql.rows(interSql)
    	 //get genome info and stats
    	 def genomeInfoSql = "select non_atgc,contig_id,gc,length,coverage from genome_info,file_data where file_id = file_data.id and file_data.id = '"+Gid+"' order by length desc;"
	 	 //println genomeInfoSql
	 	 def genomeInfo = sql.rows(genomeInfoSql)
	 	 def genome_stats = [:]
	 	 int span=0, min=10000000000, max=0, n50=0, halfSpan=0, checkSpan=0, nonATGC=0, num=0, ninetySpan=0, counter=0;
		 def n50_list = [], n90_list = [];
		 float gc
		 def n50check = false, n90check = false;
			 
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
		 
		 //clear genomeInfo array if too big as plotting it isn't a good idea
		 if (genomeInfo.size > 100000){
	 	 	genomeInfo = []
	 	 	//println "Genome is in over 100,000 pieces!"
	 	 }
		 def genomeDescSql = "select file_data.description from file_data,genome_data where file_data.id = '"+Gid+"' and file_type = 'Genome';";
		 //println genomeDescSql
		 def genomeDesc = sql.rows(genomeDescSql)
		 
		 genome_stats.version = genomeData.file_version
    	 genome_stats.description = genomeDesc.description[0]
		 genome_stats.num = num
	 	 genome_stats.span = span
	 	 genome_stats.n50 = n50
	 	 genome_stats.min = min
		 genome_stats.max = max
		 genome_stats.gc = gc
		 genome_stats.nonATGC = nonATGC
		 //println "genome_stats = "+genome_stats
		 
		 //get gene stats
		 def geneInfoSql = "select nuc,gc from gene_info,file_data where file_id = file_data.id and file_data.id = '"+params.GFFid+"';";
	 	 //println geneInfoSql
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
			 //println "gene_stats = "+gene_stats
			
			//get gene info for selected gff file
			def msql = "select count(distinct(gene_id)) as g, count(distinct(mrna_id)) as m, count(distinct(pep)) as p from gene_info,file_data where file_id = file_data.id and file_data.id = "+params.GFFid+";";
			//println msql
			def m = sql.rows(msql)
			stats.Genes = m.g[0]
			stats.mRNA = m.m[0]
			stats.Peptide = m.p[0]
			gene_stats.genenum = m.g[0]
			gene_stats.mrnanum = m.m[0]
			
			for (a in genomeData){
				if (a.file_type == "Transcriptome"){
					msql = "select count(contig_id) as t from trans_info,file_data where file_id = file_data.id and file_data.id = '"+a.id+"' ;";
					println msql
					m = sql.rows(msql)
					stats.Transcriptome = m.t[0]
				}else if (a.file_type == "Genome"){
					msql = "select count(contig_id) as c from genome_info,file_data where file_id = file_data.id and file_data.id = '"+a.id+"' ;";
					println msql
					m = sql.rows(msql)
					stats.Genome = m.c[0]
				}
			}
		 }
		 
		 //get data for plots
		 def funAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_info left outer join gene_anno on (gene_info.id = gene_anno.gene_id),file_data where gene_info.file_id = file_data.id and file_data.id = '"+params.GFFid+"' group by anno_db;";
		 //println funAnnoSql
		 def funAnnoData = sql.rows(funAnnoSql)
		 def interAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_info left outer join gene_interpro on (gene_info.id = gene_interpro.gene_id),file_data where gene_info.file_id = file_data.id and file_data.id = '"+params.GFFid+"'group by anno_db;";		
		 ///println interAnnoSql
		 def interAnnoData = sql.rows(interAnnoSql)
		 def blastAnnoSql = "select anno_db,count(distinct(gene_info.gene_id)) from gene_info left outer join gene_blast on (gene_info.id = gene_blast.gene_id),file_data where gene_info.file_id = file_data.id and file_data.id = '"+params.GFFid+"'group by anno_db;";		
		 //println blastAnnoSql
		 def blastAnnoData = sql.rows(blastAnnoSql)
    	 return [ext:ext, interAnnoData:interAnnoData, inter:inter, annoTypes:annoTypes, geneData: geneData, n50: n50_list, n90: n90_list, genomeFile:genomeData, stats: stats, funAnnoData: funAnnoData, blastAnnoData: blastAnnoData, gene_stats: gene_stats, genome_stats: genome_stats, genomeInfo: genomeInfo]
    }
    @Cacheable('all_cache')
    //@CacheEvict(value='all_cache', allEntries=true)
    def all_search() {
         if (grailsApplication.config.i.links.all == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
     		def sql = new Sql(dataSource)
     		def gsql = "select count(gene_id) as g_count,count(mrna_id) as m_count,genus,species,genome_data.id,meta_data.id as sid from gene_info,file_data,genome_data,meta_data where gene_info.file_id = file_data.id and file_data.description != 'fake' and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id group by genus,species,genome_data.id,meta_data.id;"
     		//println gsql
     		def genes = sql.rows(gsql)
     		return [genes:genes]
     	}
    }
     def all_searched = {
        if (grailsApplication.config.i.links.all == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def timeStart = new Date()
			def sql = new Sql(dataSource)
			println "Searching all databases for "+params.searchId
			
			def geneRes
			def pubRes
			def genomeData
			//if coming from species page
			if (params.gffId){
				genomeData = FileData.findById(params.gId)	
				println "genomeData = "+genomeData
				//search gene annotations
				def geneintersearch = "select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,genus,species,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank, meta_data.id as gid FROM gene_interpro,gene_info,file_data,genome_data,meta_data,plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and gene_interpro.gene_id = gene_info.id and gene_info.file_id = '"+params.gffId+"';"
				//println "Gene inter search = "+geneintersearch
				def geneinter = sql.rows(geneintersearch)
				def geneannosearch = "select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,genus,species,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank, meta_data.id as gid FROM gene_anno,gene_info,file_data,genome_data,meta_data,plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and gene_anno.gene_id = gene_info.id and gene_info.file_id = '"+params.gffId+"';"
				//println "Gene anno search = "+geneannosearch
				def geneanno = sql.rows(geneannosearch)
				def geneblastsearch = "select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,genus,species,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank, meta_data.id as gid FROM gene_blast,gene_info,file_data,genome_data,meta_data,plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and gene_blast.gene_id = gene_info.id and gene_info.file_id = '"+params.gffId+"';"
				//println "Gene blast search = "+geneblastsearch
				def geneblast = sql.rows(geneblastsearch)
				geneRes = geneinter + geneanno + geneblast
			
				//search publications
				def pubsearch = "select distinct on (pubmed_id,date_out) pubmed_id,abstract_text,title,authors,journal_short,to_char(date_string,'yyyy Mon dd') as date_out,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank from publication , plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and meta_id = "+params.sId+" order by date_out,pubmed_id desc, rank desc;"								
				pubRes = sql.rows(pubsearch)
				//println "Publication search = "+pubsearch
			}else{ //if coming from all_search
				//search gene annotations
				def geneintersearch = "select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,genus,species,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank, meta_data.id as gid FROM gene_interpro,gene_info,file_data,genome_data,meta_data,plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and gene_interpro.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id;"
				//println "Gene inter search = "+geneintersearch
				def geneinter = sql.rows(geneintersearch)
				def geneannosearch = "select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,genus,species,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank, meta_data.id as gid FROM gene_anno,gene_info,file_data,genome_data,meta_data,plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and gene_anno.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id;"
				//println "Gene anno search = "+geneannosearch
				def geneanno = sql.rows(geneannosearch)
				def geneblastsearch = "select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,genus,species,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank, meta_data.id as gid FROM gene_blast,gene_info,file_data,genome_data,meta_data,plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query and gene_blast.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id;"
				//println "Gene blast search = "+geneblastsearch
				//select distinct on (anno_db,mrna_id) mrna_id,anno_id,anno_db,anno_start,anno_stop,descr,score,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank FROM gene_anno,gene_info,plainto_tsquery('globin') AS query WHERE textsearchable_index_col @@ query and gene_anno.gene_id = gene_info.id;
				def geneblast = sql.rows(geneblastsearch)
				geneRes = geneinter + geneanno + geneblast
			
				//search publications
				def pubsearch = "select distinct on (pubmed_id,date_out) pubmed_id,abstract_text,title,authors,journal_short,to_char(date_string,'yyyy Mon dd') as date_out,ts_rank_cd(textsearchable_index_col, query,32 /* rank/(rank+1) */) AS rank from publication , plainto_tsquery('"+params.searchId+"') AS query WHERE textsearchable_index_col @@ query order by date_out,pubmed_id desc, rank desc;"								
				pubRes = sql.rows(pubsearch)
				//println "Publication search = "+pubsearch
			}
			def timeStop = new Date()
			def TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
			
			def annoLinksSql = "select source,regex,link from anno_data;";
			println annoLinksSql
			def annoLinksAll = sql.rows(annoLinksSql)
			def annoLinks = [:]
			annoLinksAll.each{
				annoLinks."${it.source}" = [it.regex,it.link]
			}
			//println "Anno links =  "+annoLinks
			
			return [searchId: params.searchId, search_time: duration, geneRes: geneRes, pubRes: pubRes, annoLinks:annoLinks, genomeData: genomeData]
		}
		sql.close()
    }
         
    def gene_search_results = {
        if (grailsApplication.config.i.links.genes == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
			def sql = new Sql(dataSource)
			//set up some global search things
			def timeStart = new Date()
			def metaData = FileData.findById(params.Gid)
			println "m = "+metaData.genome.id
			println "species = "+metaData.genome.meta.species
			def searchfile_id = params.GFFid
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
			def annoLinksSql = "select source,regex,link from anno_data where filedata_id = '"+searchfile_id+"';";
			println annoLinksSql
			def annoLinksAll = sql.rows(annoLinksSql)
			def annoLinks = [:]
			annoLinksAll.each{
				annoLinks."${it.source}" = [it.regex,it.link]
			}
			//println "Anno links =  "+annoLinks
			//println "annoDB = "+annoDB
			
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
				}else if (params.toggler == "2"){
					println "Searching functional anno data"
					sqlsearch = "select distinct on (anno_db,gene_info.mrna_id) anno_db,mrna_id,anno_id,anno_start,anno_stop,descr,score from gene_anno,gene_info,file_data where "+annoSearch+" and "+whatSearch+ "'${searchId}' and gene_anno.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.id = '"+searchfile_id+"';";
				}else if (params.toggler == "3"){
					println "Searching interpro data"
					sqlsearch = "select distinct on (anno_db,gene_info.mrna_id) anno_db,mrna_id,anno_id,anno_start,anno_stop,descr,score from gene_interpro,gene_info,file_data where "+annoSearch+" and "+whatSearch+ "'${searchId}' and gene_interpro.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.id = '"+searchfile_id+"';";
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
				//println "meta check = "+metaData.genome.meta.id
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
			def GFFid
			//check if it links to an external db
			def esql = "select ext_info.* from gene_info,file_data,ext_info where gene_info.mrna_id = '"+mrna_id+"' and gene_info.file_id = file_data.id and file_data.source = ext_info.ext_id;";
			def extInfo = sql.rows(esql)
			
			//coming in from blast results
			if (!params.Gid){
				def mrna_details = GeneInfo.findByMrna_id(mrna_id)
				GFFid = mrna_details.file.id
				def genomeSql = "select file_data.id from file_data where file_type = 'Genome' and genome_id = "+mrna_details.file.genome.id+";";
				//println genomeSql
				Gid = sql.rows(genomeSql).id[0]
			}else{
				Gid = params.Gid
				GFFid = params.GFFid
			}	
			println "Looking at transcript "+mrna_id
			def metaData = FileData.findById(Gid);
			def blast_results
			def fun_results
			def ipr_results
			//if (grailsApplication.config.g.blast.size()>0){			
				def blastsql = "select gene_info.mrna_id,gene_info.gene_id,gene_blast.* from gene_blast,gene_info where gene_blast.gene_id = gene_info.id and mrna_id = '"+mrna_id+"' order by score;";
				//println blastsql
				blast_results = sql.rows(blastsql)
			//}
			//if (grailsApplication.config.g.IPR){
				def iprsql = "select gene_interpro.* from gene_interpro,gene_info where gene_interpro.gene_id = gene_info.id and mrna_id = '"+mrna_id+"' order by score;";
				//println iprsql
				ipr_results = sql.rows(iprsql)
			//}
			//if (grailsApplication.config.g.fun.size()>0){
				def funsql = "select gene_anno.* from gene_anno,gene_info where gene_anno.gene_id = gene_info.id and mrna_id = '"+mrna_id+"' order by score;";
				//println funsql
				fun_results = sql.rows(funsql)
			//}
			//get exon info
			def exonsql = "select contig_id,exon_info.*,length(exon_info.sequence) as length from exon_info,gene_info where exon_info.gene_id = gene_info.id and mrna_id = '"+mrna_id+"' order by exon_number;"
			def exon_results = sql.rows(exonsql)
			//println exonsql
			
			def annoLinksSql = "select source,regex,link from anno_data";
			//println annoLinksSql
			def annoLinksAll = sql.rows(annoLinksSql)
			def annoLinks = [:]
			annoLinksAll.each{
				annoLinks."${it.source}" = [it.regex,it.link]
			}
			//println "Anno links =  "+annoLinks

			//get amino acid info`
			def info_results = GeneInfo.findByMrna_id(mrna_id)
			def aaData
			info_results.each {
				aaData = peptideService.getComp(it.pep)
				//println "service = "+service
			}	
			
			//get orthomcl info
			def orthoId = Ortho.findByTrans_name(mrna_id)
			/*
			def orthoGet
			if (orthoId != null){
				//println "orthoId = "+orthoId.group_id
				orthoGet = Ortho.findAllByGroup_id(orthoId.group_id)
				//println "ortho = "+orthoGet
			}else{
				println "no orthoog!"
			}
			*/
			return [extInfo:extInfo, orthologs:orthoId, Gid:Gid, GFFid:GFFid, mrna_id: mrna_id, info_results: info_results, ipr_results: ipr_results, blast_results: blast_results, fun_results: fun_results, annoLinks: annoLinks, exon_results: exon_results, aaData:aaData, metaData: metaData]
    	sql.close()
    	}
    }
    
    def genome_info = {
       	if (grailsApplication.config.i.links.genome == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
     		def sql = new Sql(dataSource)
     		def Gid
     		def GFFid
     		//coming out from the blast result there is no easy way to get the species id, so use the contig_id (very risky as possible duplicate ids!) 
     		if (params.Gid){
     			Gid = params.Gid;
     			GFFid = params.GFFid
     		}else{
     			def genome_details = GenomeInfo.findByContig_id(params.contig_id)
				Gid = genome_details.file.id
				//GFFid = genome_details.file.id[0]
				
				def geneSql = "select file_data.id from file_data where file_type = 'Genes' and genome_id = "+genome_details.file.genome.id+";";
				//println geneSql
				GFFid = sql.rows(geneSql).id[0]
				
				//println "g = "+genome_details.file.file_name[0]
				//GFFid = FileData.findAllByFile_link(genome_details.file_name[0]).id[0]
     		}
     		println "Gid = "+Gid
     		println "GFFid = "+GFFid
     		if (GFFid == null){
     			GFFid = 0;
     		}
     		def metaData = FileData.findById(Gid); 
     		//def gene_results = GeneInfo.findAllByContig_id(params.contig_id)
     		//def genesql = "select gene_info.* from gene_info,file_data,meta_data where gene_info.contig_id = '"+params.contig_id+"' and meta_data.id = '"+Gid+"' and gene_info.file_id = file_data.id and file_data.meta_id = meta_data.id;"
     		def genesql = "select gene_info.gene_id, count(mrna_id), avg(length(nuc)) as a_nuc, avg(start) as a_start, avg(stop) as a_stop from gene_info,file_data where gene_info.contig_id = '"+params.contig_id+"' and file_data.id = '"+GFFid+"' and gene_info.file_id = file_data.id group by gene_info.gene_id order by a_start;";
     		//println genesql
     		def gene_results = sql.rows(genesql)
     		//println genesql
     		//println "contig_id ="+params.contig_id
			//def info_results = GenomeInfo.findAllByContig_id(params.contig_id)
			def infosql = "select genome_info.* from genome_info,file_data where genome_info.contig_id = '"+params.contig_id+"' and file_data.id = '"+Gid+"' and genome_info.file_id = file_data.id;";
			def info_results = sql.rows(infosql)
			return [ Gid:Gid, GFFid:GFFid, info_results: info_results, gene_results: gene_results, metaData:metaData, Gid:Gid]
		}
    }
    def g_info = {
       	if (grailsApplication.config.i.links.genome == 'private' && !isLoggedIn()) {
     		redirect(controller: "home", action: "index")
     	}else{
     		def sql = new Sql(dataSource)
     		def Gid
     		def GFFid
			if (!params.Gid){
				def gene_details = GeneInfo.findAllByGene_id(params.gid)
				//println "md = "+gene_details.file.genome.id[0]
				Gid = gene_details.file.genome.id[0]
				GFFid = gene_details.file.id[0]
			}else{
				Gid = params.Gid
				GFFid = params.GFFid
			}	
     		def metaData = FileData.findById(Gid); 
     		//def results = GeneInfo.findAllByGene_id(params.gid)
     		def genesql = "select gene_info.* from gene_info,file_data where gene_info.gene_id = '"+params.gid+"' and file_data.id = "+GFFid+" and gene_info.file_id = file_data.id;"
     		//println genesql
     		def gene_results = sql.rows(genesql)
     		println "only one transcript, skipping the gene page!"
     		if (gene_results.size() == 1){
     			redirect(action: "m_info", params: [Gid: Gid, GFFid: GFFid, mid:gene_results.mrna_id])
     		}else{	
				return [ Gid:Gid, GFFid:GFFid, results: gene_results, metaData:metaData]
			}
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
    def runCluster = {
    	def align 
    	println "group = "+params.group_id
    	if (params.seq == 'nuc'){
    		println "clustering with "+params.seq+" "+params.fileName+" and "+params.orthoClusterNucFileId
    		align = alignService.runAlign(params.seq,params.fileName,params.orthoClusterNucFileId)
    	}else{
    		println "clustering with "+params.seq+" "+params.fileName+" and "+params.orthoClusterPepFileId
    		align = alignService.runAlign(params.seq,params.fileName,params.orthoClusterPepFileId)	
    	}
    	return [align:align, group_id:params.group_id]
	}
	
	@Cacheable('ortho_cache')
    //@CacheEvict(value='ortho_cache', allEntries=true)
	def ortho() {
		def sql = new Sql(dataSource)
		//number
		def nsql = "select max(group_id) from ortho;"
		def n = sql.rows(nsql)
		//overview
		def osql = "select distinct on (file_name) file_name,genus,species,count(distinct(group_id)) as count_ortho, count(group_id) as count_all from ortho,gene_info,file_data,genome_data,meta_data where ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id group by file_name,genus,species; ";
		def o = sql.rows(osql)
		def gsql = "select file_name,count(mrna_id) from gene_info,file_data where gene_info.file_id = file_data.id group by file_name;";
		//println "g = "+gsql
		def g = sql.rows(gsql)
		def gmap = [:]
		g.each{
			gmap."${it.file_name}" = it.count
		}
		//number of seqs per cluster per gene set
		def psql = "select size,count(size),file_name from ortho,gene_info,file_data where ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id group by size,file_name order by size;";
		//println psql
		def p = sql.rows(psql)
		//cluster size vs number of seqs
		def csql = "select size,count(size) from ortho,gene_info,file_data where ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id group by size order by size;";
		//println csql
		def c = sql.rows(csql)
		
		return [o:o, n:n, gmap:gmap, c:c, p:p]
		//species info 
		//select distinct on (file_name) file_name,genus,species from ortho,gene_info,file_data,genome_data,meta_data where ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id; 
		//counts for each ortho
		//select group_id,count(group_id) as num from ortho group by group_id
		//counts for each file
		//select file_data.file_name,group_id,count(group_id) from ortho,gene_info,file_data where ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id group by group_id,file_data.file_name;
		//get singletons for a particular file
		//select mrna_id from gene_info left outer join ortho on (gene_info.id = ortho.gene_id), file_data where ortho.gene_id is NULL and gene_info.file_id = file_data.id and file_data.file_name = 'nAv.1.0.1.aug.blast2go.gff'; 
	}
	def ortho_search = {
		def sql = new Sql(dataSource)
		def filesql = "select distinct(file_name),genus,species from ortho,gene_info,file_data,genome_data,meta_data where ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id order by file_name;";
		//print filesql
		def fileData = sql.rows(filesql)
		def fileCheck = [:]
		//set all to 0 initially
		fileData.each{
			fileCheck."${it.file_name}" = 0
		}
		def newFile = [];
		println "type = "+params.type
		if (params.type == 'bar'){
			def bsql = "select group_id,file_name,count(file_name),genus,species,size from ortho,gene_info,file_data,genome_data,meta_data where ortho.size = "+params.val+" and ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id group by group_id,genus,species,file_name,size order by group_id;";
			//println bsql;
			def b = sql.rows(bsql);
			def old_id = 0
			def old_size = 0;
			def newMap = [:]
			b.each{ line ->
				if (line.group_id != old_id && old_id != 0){					
					newMap.group_id = old_id
					newMap.size = old_size
					fileCheck.each{
						newMap."${it.key}" = it.value
					}
					newFile.add(newMap)
					//set all to 0
					fileData.each{
						fileCheck."${it.file_name}" = 0
					}
					newMap = [:]
				}
				fileCheck."${line.file_name}" = line.count
				old_id = line.group_id
				old_size = line.size
			}
			//catch the last one
			newMap.group_id = old_id
			newMap.size = old_size
			fileCheck.each{
				newMap."${it.key}" = it.value
			}
			newFile.add(newMap)
			//println "results = "+newFile
			return [searchRes:newFile, files:fileData, type:"bar"]
		}
		if (params.type == 'search'){
			//bsql = "select group_id,file_name,count(distinct(mrna_id)),size,genus,species from ortho,gene_info,gene_blast,file_data,genome_data,meta_data where gene_blast.descr ~ '"+params.searchId+"' and ortho.gene_id = gene_info.id and gene_blast.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id group by group_id,genus,species,file_name,size order by group_id;";
			//select group_id,file_name,count(distinct(mrna_id)),size,genus,species from ortho,gene_info,file_data,genome_data,meta_data where gene_info.id in (select gene_id from gene_blast where descr ~* 'fish') gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id group by group_id,genus,species,file_name,size order by group_id;
			//select distinct on (group_id,anno_db) group_id,size,anno_db,descr,score from ortho,gene_blast,gene_info where gene_blast.descr ~ '"+params.searchId+"' and gene_blast.gene_id = gene_info.id and ortho.gene_id = gene_info.id order by group_id,anno_db,score;
			//bsql = "select distinct on (group_id,genus,species) group_id,size,anno_db,descr,score,mrna_id,genus,species from ortho,gene_blast,gene_info,file_data,genome_data,meta_data where gene_blast.descr ~ '"+params.searchId+"' and gene_blast.gene_id = gene_info.id and ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id order by group_id,genus,species,score;";
			def blastsql = "select distinct on (group_id,anno_db) group_id,size,anno_db,descr,score,mrna_id,genus,species from ortho,gene_blast,gene_info,file_data,genome_data,meta_data where gene_blast.descr ~ '"+params.searchId+"' and gene_blast.gene_id = gene_info.id and ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id";
			def annosql = "select distinct on (group_id,anno_db) group_id,size,anno_db,descr,score,mrna_id,genus,species from ortho,gene_anno,gene_info,file_data,genome_data,meta_data where gene_anno.descr ~ '"+params.searchId+"' and gene_anno.gene_id = gene_info.id and ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id";
			def intersql = "select distinct on (group_id,anno_db) group_id,size,anno_db,descr,score,mrna_id,genus,species from ortho,gene_interpro,gene_info,file_data,genome_data,meta_data where gene_interpro.descr ~ '"+params.searchId+"' and gene_interpro.gene_id = gene_info.id and ortho.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.genome_id = genome_data.id and genome_data.meta_id = meta_data.id";
			
			//construct the union search string			
			//if just one db is passed then the list becomes a string
			def bsql = ""
			def oVal = params.oVal
			if (oVal){
				if (oVal instanceof String){
					if (oVal == 'blast'){
						bsql = blastsql
					}else if (oVal == 'anno'){
						bsql = annosql
					}else if (oVal == 'inter'){
						bsql = intersql
					}
				}else{
					def annoSelect = oVal		
					annoSelect.each {
						if (it == 'blast'){
							bsql += blastsql+" union ";
						}
						if (it == 'anno'){
							bsql += annosql+" union ";
						}
						if (it == 'inter'){
							bsql += intersql+" union ";
						}
					}
					bsql = bsql[0..-8]			
				}
				println bsql;
				bsql += " order by group_id,anno_db,score;"
				def b = sql.rows(bsql);
				println bsql;
				return [searchRes:b, files:fileData, type:"search"]
				
			}else{
				return [searchRes:b, files:fileData, type:"search"]
			}
		}
		
	}
	
	@Cacheable('cluster_cache')
    //@CacheEvict(value='cluster_cache', allEntries=true)
	def cluster() {
		def data = Ortho.findAllByGroup_id(params.group_id)
		//println "cluster data = "+data
		return [data:data]
	}
}
