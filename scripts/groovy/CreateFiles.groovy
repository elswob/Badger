package badger

import groovy.sql.Sql

def grailsApplication


def cleanUpGorm() { 
	def sessionFactory = ctx.getBean("sessionFactory")
	def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    def session = sessionFactory.currentSession 
    session.flush() 
    session.clear() 
    propertyInstanceMap.get().clear() 
}

def getFiles = FileData.findAllByFile_type("Genes")
if (getFiles){
	getFiles.each {
		createAnnoFile(it.id)
	}
}else{
	println "There are no genes to create an annotation file with"
}

def createAnnoFile(gff){
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	
  	//create output file
  	def gffInfo = FileData.findById(gff)
  	println "### Getting data for "+gffInfo.file_name+" ###"
  	println new Date()
  	def outfile = new File("data/"+gffInfo.file_dir+"/"+gffInfo.file_name+".anno.tsv")
	if (outfile.exists()){outfile.delete()}
	
  	def asql = "select * from anno_data where filedata_id = "+gff+" order by type;"
    def a = sql.rows(asql)

	//add headers for file
	outfile << "\t"
	a.each{anno->
  	  	outfile << "\t"+anno.source+"\t\t"
  	}
  	outfile << "\n"
  	outfile << "Transcript ID"
  	a.each{anno->
  		outfile << "\tAnnotation ID\tDescription\tScore"
  	}
  	outfile << "\n"
  	def count = 0
  	
  	//create gene list
  	def glist = [:]
  	def gsql = "select distinct(mrna_id) mrna_id from gene_info where file_id = "+gff+" order by mrna_id;"
  	def g = sql.rows(gsql)
  	g.each{
  		glist."${it.mrna_id}" = []
  	}
  	//get blasts
  	a.each{anno->
  		def bsql
  		if (anno.type == 'blast'){
			println "--- Getting blast data for '"+anno.source+"' ---"
			bsql = "select distinct on (mrna_id) mrna_id,anno_id,descr,score from gene_info left outer join gene_blast on (gene_info.id = gene_blast.gene_id) where gene_info.file_id = "+gff+" and gene_blast.anno_db = '"+anno.source+"' group by mrna_id,anno_id,descr,score order by mrna_id,score;";
		}else if (anno.type == 'fun'){
			println "--- Getting functional annotation data for '"+anno.source+"' ---"
			bsql = "select distinct on (mrna_id) mrna_id,anno_id,descr,score from gene_info left outer join gene_anno on (gene_info.id = gene_anno.gene_id) where gene_info.file_id = "+gff+" and gene_anno.anno_db = '"+anno.source+"' group by mrna_id,anno_id,descr,score order by mrna_id,score;";
		}else if (anno.type == 'ipr'){
			println "--- Getting interproscan data for '"+anno.source+"' ---"
			bsql = "select distinct on (mrna_id) mrna_id,anno_id,descr,score from gene_info left outer join gene_interpro on (gene_info.id = gene_interpro.gene_id) where gene_info.file_id = "+gff+" group by mrna_id,anno_id,descr,score order by mrna_id,score;";
		}
  		def b = sql.rows(bsql)
  		def hit = [:]
  		b.each{
  			//catch the interproscan data
  			if (it.anno_id != null){
				hit."${it.mrna_id}" = true  
				glist."${it.mrna_id}".add(it.anno_id)
				glist."${it.mrna_id}".add(it.descr)
				glist."${it.mrna_id}".add(it.score)
			}
  		}
  		//add spaces for no hits
  		glist.each{
  			if (hit."${it.key}" != true){
  				glist."${it.key}".add("")
  				glist."${it.key}".add("")
  				glist."${it.key}".add("")
  			}
  			
  		}
  	}
  	glist.each{
  		outfile << it.key
  		for (i in it.value){
  			outfile << "\t"+i
  		} 
  		outfile << "\n"
  	}
    
    println "Zipping up for download..."
	def ant = new AntBuilder()
	ant.zip(destfile: "data/"+gffInfo.file_dir+"/"+gffInfo.file_name+".anno.tsv.zip", basedir: "data/"+gffInfo.file_dir, includes: gffInfo.file_name+".anno.tsv")
}

DBDump()

def DBDump(){
	println "Create postgres database dump..."
	def today = new Date()
	
	def dbString = grailsApplication.config.dataSource.url.trim()
	def matcher
	def db
	if ((matcher = dbString =~ /jdbc:postgresql:\/\/.*?\/(.*)$/)){
		db = matcher[0][1]
	}
	
	def fileMap = [:]
	new File("data/db_dump").eachFileRecurse{file->
		//println "file = "+file
		if ((matcher = file =~ /data\/db_dump\/${db}_/)){
			fileMap."${file}" = new Date(file.lastModified()).format('EEE MMM dd hh:mm:ss a yyyy')
		}
	}
	println "There are "+fileMap.size()+" database backups"
	println "The limit is set to "+grailsApplication.config.d.number.trim()
	def fileMapSort = fileMap.sort {a, b -> b.key <=> a.key}
	def oldest = ""
	def newest = ""
	fileMapSort.each{
		println "sorted = "+it.key+" -> "+it.value
		if (newest == ""){
			newest = it.key
		}
		//println it
		oldest = it.key
	}
	println "newest = "+newest
	println "oldest = "+oldest
	def fileDate = String.format('%tY_%<tm_%<td:%<tH:%<tM:%<tS', today)
	
	println "Dumping database..."
	String[] comm = ["pg_dump", "-U", "${grailsApplication.config.dataSource.username.trim()}", "-a", "${db}","-f", "data/db_dump/${db}_${fileDate}.pgsql"]
	ProcessBuilder dumpProcess = new ProcessBuilder(comm)  
	dumpProcess.redirectErrorStream(true)
	Process p = dumpProcess.start()
	p.waitFor()
	println "Created data/db_dump/${db}_${fileDate}.pgsql"
	println "Checking to see if anything has changed between this and the last dump..."
	String[] check = ["cmp", "${newest}", "data/db_dump/${db}_${fileDate}.pgsql"]
	ProcessBuilder checkProcess = new ProcessBuilder(check)  
	checkProcess.redirectErrorStream(true)
	Process c = checkProcess.start()
	c.waitFor()
	//println "Error = "+c.text
	if (c.text == ""){
		println "No change, not adding new dump"
		def del = "rm data/db_dump/${db}_${fileDate}.pgsql"
		del.execute()
	}else{
		println "Changes have been made, adding new dump"
		if (fileMap.size() >= grailsApplication.config.d.number.trim().toInteger()){
			//delete the oldest file
			println "The maximum number of backups is reached, deleting "+oldest+" ..."
			def del = "rm "+oldest
			del.execute()
		}
	}
}