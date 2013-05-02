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
	}else{
		println anno.source+": "+anno.anno_file+" already loaded";
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
	}else{
		println anno.source+": "+anno.anno_file+" already loaded";
	}
}

def addFunc(anno,annoFile){
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
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
		def score = splitter[5] as Float
        score = sprintf("%.3g",score)
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
    //println aSql
    sql.execute(aSql)
	println anno.anno_file+" is loaded"
}

// add the interposcan raw data 
def addInterProScan(anno,annoFile){
	cleanUpGorm()
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
	println "Adding new...."
	println new Date()
  	def count=0
    def annoMap = [:]
    annoFile.eachLine { line ->
		count++
		splitter = line.split("\t")
		def mrna_id, score, anno_db, anno_id, start, stop, descr
		mrna_id = splitter[0]
		anno_db = splitter[3]
		start = splitter[6] as int
		stop = splitter[7] as int
		if (splitter.size() == 11){
			//Assuming this is version 5 output as there are 11 columns
			anno_id = splitter[4]
			if (splitter[8] == '-'){
				score = 0 as float
			}else if (splitter[8].isNumber()){
				score = splitter[8] as float;
			}else{
				score = 1
			}
			descr = splitter[5]
			if (descr.size() < 1){
				descr = "n/a"
			}
		}else if (splitter.size() > 11){
			//Assuming this is pre version 5 output as there are 12 columns
			anno_id = splitter[11]+" - "+splitter[4]
			if (splitter[8].isNumber()){
				score = splitter[8] as float;
			}else{
				score = 1
			}
			descr = splitter[12]
		}
		if (splitter.size() == 12 && splitter[11] == 'NULL'){
			return;
		}else{
			if (descr != "NULL"){
				annoMap.anno_db = anno_db
				annoMap.anno_id = anno_id
				annoMap.anno_start = start
				annoMap.anno_stop = stop
				annoMap.score = score
				annoMap.descr = descr
				//println annoMap
				if (score < 1e-5){
					GeneInfo geneFind = GeneInfo.findByMrna_id(mrna_id)
					GeneInterpro ga = new GeneInterpro(annoMap)
					if (geneFind){
						geneFind.addToGinter(ga)
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
    }
    println count
	//mark as loaded
    def aSql = "update anno_data set loaded = true where id = '"+anno.id+"'";
    //println aSql
    sql.execute(aSql)
	println anno.anno_file+" is loaded"
}