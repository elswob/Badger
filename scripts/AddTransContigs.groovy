package GDB

import groovy.sql.Sql

def grailsApplication

println "Adding the sequence data..."

getFiles()

def getFiles(){
	if (grailsApplication.config.data){
		def Locations = grailsApplication.config.data
    	Locations.each {
    		if (it.value.size() >0){ 
    			println "adding "+it.key+" and "+it.value
    			//addData(it.key,dataFile)
    		}
    	}
    }
}

def addData(dataFile,db){
	def seqFile = new File("data/"+dataFile).text
	def sequence=""
	def contig_id=""
	def count=0
	def count_gc		
	def contigMap = [:]
	contigFile.split("\n").each{
		if ((matcher = it =~ /^>(.*)/)){
			if (sequence != ""){
				//println "Adding $contig_id - $count"
				count++
				//get gc
				count_gc = sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
				def gc = (count_gc/sequence.length())*100
				gc = sprintf("%.2f",gc)
				//add data to map
				contigMap.contig_id = contig_id
				contigMap.gc = gc
				contigMap.coverage = 0
				contigMap.length = sequence.length()
				contigMap.sequence = sequence
				//println contigMap
				new TransInfo(contigMap).save()
				//println contigMap
				sequence=""
			}
			contig_id = matcher[0][1]
			count_gc = 0
		}else{
			sequence += it
		}
	} 
	//catch the last one
	count_gc = sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
	def gc = (count_gc/sequence.length())*100
	contigMap.contig_id = contig_id
	contigMap.gc = gc
	contigMap.length = sequence.length()
	contigMap.sequence = sequence
	contigMap.coverage = 0
	new TransInfo(contigMap).save()
}

