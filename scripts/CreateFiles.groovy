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
  	def outfile = new File("data/"+gffInfo.file_dir+"/"+gffInfo.file_name+".anno.csv")
	if (outfile.exists()){outfile.delete()}
	
  	def asql = "select * from anno_data where filedata_id = "+gff+" order by type;"
    def a = sql.rows(asql)
	
	//get list of interpro databases
	//def iSql = "select distinct(anno_db) from gene_anno where anno_id ~ '^IPR';";
	//def i = sql.rows(iSql).anno_db

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
  	gffInfo.gene.sort({it.mrna_id}).each{g->
  		count++
  		def gMap = [:]
  	  	a.each{anno->
  	  		if (anno.type == 'blast'){
				if (g.gblast.findAll({it.anno_db == anno.source})){
					def top = g.gblast.findAll({it.anno_db == anno.source}).sort({-it.score})[0]
					gMap."${anno.source}" = [top.anno_id,top.descr,top.score]
				}else{
					gMap."${anno.source}" = ["","",""]
				}
			}else if (anno.type == 'fun'){
				if (g.ganno.findAll({it.anno_db == anno.source})){
					def top = g.ganno.findAll({it.anno_db == anno.source}).sort({-it.score})[0]
					gMap."${anno.source}" = [top.anno_id,top.descr,top.score]
				}else{
					gMap."${anno.source}" = ["","",""]
				}
			}else if (anno.type == 'ipr'){
				if (g.ginter.findAll()){
					def top = g.ginter.findAll().sort({it.score})[0]
					gMap."${anno.source}" = [top.anno_id,top.descr,top.score]
				}else{
					gMap."${anno.source}" = ["","",""]
				}
			}
      	}
      	if ((count % 1000) ==  0){
			println count
			//cleanUpGorm()
		}
      	//println "m = "+g.mrna_id
      	outfile << g.mrna_id
      	gMap.each{
      		//println it.key+" -> "+it.value[0]+"\t"+it.value[1]+"\t"+it.value[2]+"\n"
      		outfile << "\t"+it.value[0]+"\t"+it.value[1]+"\t"+it.value[2]
      	}
      	outfile << "\n"
    }
    println "Zipping up for download..."
	def ant = new AntBuilder()
	ant.zip(destfile: "data/"+gffInfo.file_dir+"/"+gffInfo.file_name+".anno.csv.zip", basedir: "data/"+gffInfo.file_dir, includes: gffInfo.file_name+".anno.csv)
}
