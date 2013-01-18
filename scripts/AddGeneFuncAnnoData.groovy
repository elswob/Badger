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

def a = AnnoData.findAllByType('fun')
a.each{
	AnnoData anno = it
	if (anno.loaded == false){
		if (!anno.isAttached()) {
		 anno.attach()
		}
		//get FileData parent of AnnoData object
		def b = anno.filedata
		def fileLoc = b.file_dir+"/"+anno.anno_file
		def annoFile = new File("data/"+fileLoc)
		println "Source = "+anno.source
		println "File = "+fileLoc
		//println "type = "+a.type
		addFunc(anno,annoFile)
	}
}

a = AnnoData.findAllByType('ipr')
a.each{
	AnnoData anno = it
	if (anno.loaded == false){
		if (!anno.isAttached()) {
		 anno.attach()
		}
		//get FileData parent of AnnoData object
		def b = anno.filedata
		def fileLoc = b.file_dir+"/"+anno.anno_file
		def annoFile = new File("data/"+fileLoc)
		println "Source = "+anno.source
		println "File = "+fileLoc
		//println "type = "+a.type
		addInterProScan(anno,annoFile)
	}
}

def addFunc(anno,annoFile){
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	//println "Deleting old data..."
	//def delsql = "delete from gene_anno,gene_info,file_data,anno_data where gene_anno.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.id = anno_data.filedata_id and anno_data.anno_file = '"+annoFile+"';";
	//println delsql
	//sql.execute(delsql)
	println "Adding new...."
	println new Date()
    def annoMap = [:]
    def count=0
    annoFile.eachLine { line ->
		count++
		splitter = line.split("\t")
		def mrna_id = splitter[0]
		annoMap.anno_db = anno.source
		annoMap.anno_id = splitter[2]
		annoMap.anno_start = splitter[3]
		annoMap.anno_stop = splitter[4]
		def score = splitter[5] as double
		annoMap.score = score
		annoMap.descr = splitter[6]
		//println annoMap
		GeneInfo geneFindFun = GeneInfo.findByMrna_id(mrna_id)
		GeneAnno ga = new GeneAnno(annoMap)
		if (geneFindFun){
			geneFindFun.addToGanno(ga)
			if ((count % 5000) ==  0){
				println count
				println new Date()
				ga.save(flush:true)
				cleanUpGorm()            	
			}else{
				ga.save()
			}	
		}else{
			println mrna_id+" does not exist"
		}
    }
    println count
    //mark as loaded
    def aSql = "update anno_data set loaded = true where id = '"+anno.id+"'";
    println aSql
    sql.execute(aSql)
	println anno.anno_file+" is loaded"
}

// add the interposcan raw data 
def addInterProScan(anno,annoFile){
	cleanUpGorm()
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	//println "Deleting old data..."
	//def delsql = "delete from gene_anno,gene_info,file_data,anno_data where gene_anno.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.id = anno_data.filedata_id and anno_data.anno_file = '"+annoFile+"';";
	//println delsql
	//sql.execute(delsql)
	println "Adding new...."
	println new Date()
  	def count=0
	// get the ipr descriptions
	//def iprMap = [:]
	//iprFile = new File('data/entry.list')
 	//iprFile.eachLine { line ->
    //   if ((matcher = line =~ /(IPR\d+)\s+(.*)/)){
    //		iprMap[matcher[0][1]] = matcher[0][2]
    //    }
    //}
    def annoMap = [:]
    annoFile.eachLine { line ->
		count++
		def mrna_id
		splitter = line.split("\t")
		if (splitter[11] != 'NULL'){
			if ((matcher = splitter[0] =~ /(.*)/)){
				mrna_id = matcher[0][1]
			}
			annoMap.anno_db = splitter[3]
			annoMap.anno_id = splitter[11]+" - "+splitter[4]
			def start = splitter[6] as int
			def stop = splitter[7] as int
			annoMap.anno_start = start
			annoMap.anno_stop = stop
			def score = splitter[8] as float
			annoMap.score = score
			//annoMap.descr = iprMap[splitter[11]]
			annoMap.descr = splitter[12]
			if (score < 1e-5){
				GeneInfo geneFind = GeneInfo.findByMrna_id(mrna_id)
				GeneAnno ga = new GeneAnno(annoMap)
				if (geneFind){
				geneFind.addToGanno(ga)
					if ((count % 5000) ==  0){
						println count
						//println annoMap
						println new Date()          			
						ga.save(flush:true)
						cleanUpGorm()
					}else{
						ga.save()
					}
				}else{
					println mrna_id+" does not exist!"
				}
			}
		}
    }
    println count
	//mark as loaded
    def aSql = "update anno_data set loaded = true where id = '"+anno.id+"'";
    println aSql
    sql.execute(aSql)
	println anno.anno_file+" is loaded"
}