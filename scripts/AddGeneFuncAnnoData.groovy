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
    file.eachLine { line ->
    	    splitter = line.split("\t")
    	    annoMap.gene_id = splitter[0]
    	    annoMap.anno_db = source
    	    annoMap.anno_id = splitter[2]
    	    annoMap.anno_start = splitter[3]
    	    annoMap.anno_stop = splitter[4]
    	    def score = splitter[5] as double
    	    annoMap.score = score
    	    annoMap.descr = splitter[6]
    	    new TransAnno(annoMap).save()
    }
}

// add the interposcan raw data 
def addInterProScan(file){
	// get the ipr descriptions
	def iprMap = [:]
	iprFile = new File('data/entry.list').text 
	iprFile.eachLine { line ->
		if ((matcher = line =~ /(IPR\d+)\s+(.*)/)){
			iprMap[matcher[0][1]] = matcher[0][2]
		}
	}
    def annoMap = [:]
    def GOMap = [:]
    file.eachLine { line ->
    	    splitter = line.split("\t")
    	    if (splitter[11] != 'NULL'){
				if ((matcher = splitter[0] =~ /(.*?)_\d+_ORF\d.*/)){
					annoMap.gene_id = matcher[0][1]
					GOMap.gene_id = matcher[0][1]
				}
				annoMap.anno_db = splitter[3]
				annoMap.anno_id = splitter[11]+" - "+splitter[4]
				def start = splitter[6] as int
				def stop = splitter[7] as int
				annoMap.anno_start = start
				GOMap.anno_start = start
				annoMap.anno_stop = stop
				GOMap.anno_stop = stop
				//println "score = "+splitter[8]
				def score = splitter[8] as double
				//def score = splitter[8]
				//println "score2 = "+score
				annoMap.score = splitter[8]
				GOMap.score = splitter[8]
				GOMap.anno_db = "IPRGO"
				//annoMap.score = splitter[8] as float
				annoMap.descr = iprMap[splitter[11]]
				//println annoMap
              	if ((matcher = line =~ /(Molecular Function:.*?)\s\((GO:\d+)\)/)){
						GOMap.descr = matcher[0][1]
						GOMap.anno_id = matcher[0][2]
						if (score < 1e-5){
							new GeneAnno(GOMap).save()
						}
				}
                if ((matcher = line =~ /(Cellular Component:.*?)\s\((GO:\d+)\)/)){
						GOMap.descr = matcher[0][1]
						GOMap.anno_id = matcher[0][2]
						if (score < 1e-5){
							new GeneAnno(GOMap).save()
						}
				}
                if ((matcher = line =~ /.*?(Biological Process:.*?)\s\((GO:\d+)\)/)){
						GOMap.descr = matcher[0][1]
						GOMap.anno_id = matcher[0][2]
						if (score < 1e-5){
							new GeneAnno(GOMap).save()
						}
				}
				if (score < 1e-5){
					new GeneAnno(annoMap).save()
				}
		  }
    }
}