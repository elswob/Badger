package badger

import groovy.sql.Sql

def grailsApplication
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)

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

//create annotation spreadsheet
def createAnnoFile(gffId){
	println "### GFF id "+gffId+" ###"
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
	def blastData = [:]
	def funData = [:]
	def iprData = [:]
	
	println "Getting BLAST data ..."
	def blastSQL = "select distinct on (anno_db,mrna_id) mrna_id,anno_db,anno_id,score from gene_blast,gene_info where gene_blast.gene_id = gene_info.id and gene_info.file_id = "+gffId+" and anno_id !~ '^IPR' order by mrna_id,anno_db,score desc limit 100;";
	def blastOut = sql.rows(blastSQL)
	blastOut.each{
		blastData."${it.mrna_id}" = it            
	}
	println blastData
	
	println "Getting non InterPro functional annotation data ..."
	def funSQL = "select distinct on (anno_db,mrna_id) mrna_id,anno_db,anno_id,score from gene_anno,gene_info where gene_anno.gene_id = gene_info.id and gene_info.file_id = "+gffId+" and anno_id !~ '^IPR' order by mrna_id,anno_db,score desc limit 100;";
	def funOut = sql.rows(funSQL)
	funOut.each{
		funData."${it.mrna_id}" = it            
	}
	
	println "Getting InterPro functional annotation data ..."
	def iprSQL = "select distinct on (mrna_id) mrna_id,anno_db,anno_id,score from gene_anno,gene_info where gene_anno.gene_id = gene_info.id and gene_info.file_id = "+gffId+" and anno_id ~ '^IPR' order by mrna_id,anno_db,score asc limit 100;";
	def iprOut = sql.rows(iprSQL)
	iprOut.each{
		iprData."${it.mrna_id}" = it            
	}
	
	//create file
	println "Creating file ..."
	def trans = GeneInfo.findAll()
	trans.each{
		if (blastData."${it.mrna_id}"){
			println "BLAST!!! - "+blastData."${it.mrna_id}"
		}else{
			//println "no blast for "+mid
		}
	}
}