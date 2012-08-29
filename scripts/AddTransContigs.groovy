package GDB

import groovy.sql.Sql

def grailsApplication

//add the Transcripts
addData()
def addData(){
	if (grailsApplication.config.pub.Transcripts){
		println "Adding transcripts - "+grailsApplication.config.pub.Transcripts
		def contigFile = new File("data/"+grailsApplication.config.pub.Transcripts).text
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
}

