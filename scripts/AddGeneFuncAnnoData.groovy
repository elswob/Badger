package GDB

import groovy.sql.Sql
def grailsApplication

def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)

def getFilesSql = "select file_dir,anno_file,anno_data.file_id,source,type from anno_data,file_data where anno_data.file_id = file_data.file_id and type != 'blast';";
println getFilesSql
def getFiles = sql.rows(getFilesSql)
getFiles.each {  	
	def fileLoc = it.file_dir+"/"+it.anno_file
	def annoFile = new File("data/"+fileLoc).text
	if (it.type == "fun"){
		println "Adding functional annotation info for "+fileLoc		
		addFunc(it.source,annoFile,it.file_id)
	}else if (it.type == "ipr"){
		println "Adding InterProScan annotation info for "+fileLoc		
		addInterProScan(annoFile,it.file_id)
	}
}

def addFunc(source,file,file_id){
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	println "Deleting old data..."
	def delsql = "delete from gene_anno where file_id = '"+file_id+"' and anno_db = '"+source+"';";
	sql.execute(delsql)
	println "Adding new...."
    def annoMap = [:]
    def count=0
    file.eachLine { line ->
    		annoMap.file_id = file_id
      		count++
    	    splitter = line.split("\t")
    	    annoMap.gene_id = splitter[0]
    	    annoMap.anno_db = source
    	    annoMap.anno_id = splitter[2]
    	    annoMap.anno_start = splitter[3]
    	    annoMap.anno_stop = splitter[4]
    	    def score = splitter[5] as double
    	    annoMap.score = score
    	    annoMap.descr = splitter[6]
    	    if ((count % 1000) ==  0){
            	println count
            	new GeneAnno(annoMap).save(flush:true)
            }else{
            	new GeneAnno(annoMap).save()
            }	
    	    //new GeneAnno(annoMap).save()
    }
}

// add the interposcan raw data 
def addInterProScan(file,file_id){
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	println "Deleting old data..."
	def delsql = "delete from gene_anno where file_id = '"+file_id+"' and anno_id ~ '^IPR';";
	sql.execute(delsql)
	println "Adding new...."
  	def count=0
	// get the ipr descriptions
	def iprMap = [:]
	iprFile = new File('data/entry.list').text
 	iprFile.eachLine { line ->
        if ((matcher = line =~ /(IPR\d+)\s+(.*)/)){
    		iprMap[matcher[0][1]] = matcher[0][2]
        }
    }
    def annoMap = [:]
    file.eachLine { line ->
    		annoMap.file_id = file_id
      		count++
    	    splitter = line.split("\t")
    	    if (splitter[11] != 'NULL'){
				if ((matcher = splitter[0] =~ /(.*)/)){
					annoMap.gene_id = matcher[0][1]
				}
				annoMap.anno_db = splitter[3]
				annoMap.anno_id = splitter[11]+" - "+splitter[4]
				def start = splitter[6] as int
				def stop = splitter[7] as int
				annoMap.anno_start = start
				annoMap.anno_stop = stop
				//println "score = "+splitter[8]
				//def score = splitter[8] as double
				def score = splitter[8] as float
				//println "score2 = "+score
				//annoMap.score = splitter[8]
				annoMap.score = score
				annoMap.descr = iprMap[splitter[11]]
				if (score < 1e-5){
                  	if ((count % 1000) ==  0){
            			println count
                      	//println annoMap
						new GeneAnno(annoMap).save(flush:true)
                    }else{
                      	new GeneAnno(annoMap).save()
                    }
				}
		  }
    }
}