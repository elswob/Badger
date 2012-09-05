package GDB

import groovy.sql.Sql

def grailsApplication

//add the Transcripts
addTransData()
def addTransData(){
	if (grailsApplication.config.seqData.Transcriptome){
		println "Adding transcriptome data - "+grailsApplication.config.seqData.Transcriptome
		def contigFile = new File("data/"+grailsApplication.config.seqData.Transcriptome.trim()).text
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
					if ((count % 1000) ==  0){
            			println count
            			new TransInfo(contigMap).save(flush:true)
            		}else{
            			new TransInfo(contigMap).save()
            		}					
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

//add the genome data
addGenomeData()
def addGenomeData(){
	if (grailsApplication.config.seqData.Genome){
		println "Adding genome data - "+grailsApplication.config.seqData.Genome
		def contigFile = new File("data/"+grailsApplication.config.seqData.Genome.trim()).text
		def sequence=""
		def contig_id=""
		def count=0
		def count_gc		
		def coverage
		def contigMap = [:]
		contigFile.split("\n").each{
		if ((matcher = it =~ /^>(.*?)_(.*)/)){
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
					contigMap.coverage = coverage
					contigMap.length = sequence.length()
					contigMap.sequence = sequence
					//println contigMap
					if ((count % 1000) ==  0){
            			println count
            			new GenomeInfo(contigMap).save(flush:true)
            		}else{
            			new GenomeInfo(contigMap).save()
            		}			
					//println contigMap
					sequence=""
				}
				contig_id = matcher[0][1]
				coverage = matcher[0][2]
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
		contigMap.coverage = coverage
		new GenomeInfo(contigMap).save()
	}
}

//add the Genes
addGeneData()
def addGeneData(){	
	if (grailsApplication.config.seqData.GeneNuc && grailsApplication.config.seqData.GenePep && grailsApplication.config.seqData.GeneData){
		def geneData = [:]
		def nucData = [:]
		def pepData = [:]
      	def geneMap = [:]
        def geneId
		println "Adding gene data... "
		println "Reading nucleotide data - "+grailsApplication.config.seqData.GeneNuc
		def nucFile = new File("data/"+grailsApplication.config.seqData.GeneNuc.trim()).text
		def sequence=""
		def count=0
		nucFile.split("\n").each{
			if ((matcher = it =~ /^>(.*)/)){
				if (sequence != ""){
					nucData."${geneId}" = sequence
					sequence=""
				}
				geneId = matcher[0][1]
			}else{
				sequence += it
			}               
        }
		//catch the last one
		nucData."${geneId}" = sequence
		
		println "Reading peptide data - "+grailsApplication.config.seqData.GenePep
		def pepFile = new File("data/"+grailsApplication.config.seqData.GenePep.trim()).text
		sequence=""
		pepFile.split("\n").each{
			if ((matcher = it =~ /^>(.*)/)){
				if (sequence != ""){
					pepData."${geneId}" = sequence
					sequence=""
				}
				geneId = matcher[0][1] 
			}else{
				sequence += it
			}
             
        }
		//catch the last one
		pepData."${geneId}" = sequence
		
      	//println nucData
        //println pepData
      
      	println "Reading gene data file - "+grailsApplication.config.seqData.GeneData
      	def dataFile = new File("data/"+grailsApplication.config.seqData.GeneData.trim()).text
      	dataFile.split("\n").each{
      		//ignore comment lines
        	if ((matcher = it =~ /^#.*/)){
              //println "ignoring "+it
            }else{
              def dataArray = it.split("\t")
              geneData."${dataArray[0]}" = [dataArray[1],dataArray[2],dataArray[3],dataArray[4],dataArray[5]]
            }
        }
      	//println geneData
      	geneData.each{gene->
          count++
          geneMap.gene_id = gene.key
          geneMap.contig_id = gene.value[0]
          geneMap.intron = gene.value[1]
          geneMap.source = gene.value[2]
          geneMap.start = gene.value[3]
          geneMap.stop = gene.value[4]
          geneMap.nuc = nucData."${gene.key}"
          geneMap.pep = pepData."${gene.key}"
          if ((count % 1000) ==  0){
            println "Added "+count
            new GeneInfo(geneMap).save(flush:true)
          }else{
            new GeneInfo(geneMap).save()
          }			         
        }
        println "Added "+count		
	}else{
		println "One of the three required files is missing"
	}
}


