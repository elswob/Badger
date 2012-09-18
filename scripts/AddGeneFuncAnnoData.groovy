package GDB

import groovy.sql.Sql
def grailsApplication

getData()
def getData(){
	if (grailsApplication.config.g.fun.size()>0){
		for(item in grailsApplication.config.g.fun){
			item = item.toString()
     	 	def splitter = item.split("=")
     	 	println "Adding "+splitter[0]+" - "+splitter[1]
     	 	inFile = new File("data/"+splitter[1].trim()).text
     	 	addFunc(splitter[0].trim(),inFile)
     	 }
    }
    if (grailsApplication.config.g.IPR){
    	def  iprFile = new File("data/"+grailsApplication.config.g.IPR.trim()).text
    	println "Adding IPR - "+grailsApplication.config.g.IPR
    	addInterProScan(iprFile)
    }
}

//add the functional annotation info
//tab delimited
//gene_id	source	hit_id	start	stop	bit score	description

def addFunc(source,file){
    def annoMap = [:]
    def count=0
    file.eachLine { line ->
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
def addInterProScan(file){
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
                      	println annoMap
						new GeneAnno(annoMap).save(flush:true)
                    }else{
                      	new GeneAnno(annoMap).save()
                    }
				}
		  }
    }
}