package GDB

import groovy.sql.Sql
def sql = new Sql(dataSource)

//add the annot8r info

def addAnnot8r(file){
    def annoMap = [:]
    file.eachLine { line ->
    	    splitter = line.split("\t")
    	    annoMap.contig_id = splitter[0]
    	    annoMap.anno_db = splitter[1]
    	    annoMap.anno_id = splitter[2]
    	    annoMap.anno_start = splitter[3]
    	    annoMap.anno_stop = splitter[4]
    	    def score = splitter[5] as double
    	    annoMap.score = score
    	    annoMap.descr = splitter[6]
    	    new UnigeneAnno(annoMap).save()
    }
}


// add the interposcan data
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
    file.eachLine { line ->
    	    splitter = line.split("\t")
    	    if (splitter[11] != 'NULL'){
				if ((matcher = splitter[0] =~ /(contig_\d+)_.*/)){
					annoMap.contig_id = matcher[0][1]
				}
				annoMap.anno_db = splitter[3]
				annoMap.anno_id = splitter[11]+":"+splitter[4]
				def start = splitter[6] as int
				def stop = splitter[7] as int
				annoMap.anno_start = start*3
				annoMap.anno_stop = stop*3
				//println "score = "+splitter[8]
				def score = splitter[8] as double
				//def score = splitter[8]
				//println "score2 = "+score
				annoMap.score = splitter[8]
				//annoMap.score = splitter[8] as float
				annoMap.descr = iprMap[splitter[11]]
				//println annoMap
				if (score < 1e-5){
					//println annoMap
					new UnigeneAnno(annoMap).save()
				}
		  }
    }
}

def inFile

inFile = new File('data/iprscan_raw.out').text
println "Adding ipr..."
addInterProScan(inFile)

inFile = new File('data/ec.txt').text
println "Adding ec..."
//addAnnot8r(inFile)

inFile = new File('data/go.txt').text
println "Adding go..."
//addAnnot8r(inFile)

inFile = new File('data/kegg.txt').text
println "Adding kegg..."
//addAnnot8r(inFile)





