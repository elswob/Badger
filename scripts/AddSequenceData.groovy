package GDB

import groovy.sql.Sql

def grailsApplication

//add the Transcripts
addTransData()
def addTransData(){
	if (grailsApplication.config.seqData.Transcriptome){
		println "Adding transcript data - "+grailsApplication.config.seqData.Transcriptome
		def contigFile = new File("data/"+grailsApplication.config.seqData.Transcriptome.trim()).text
		def cov_check = false
      	def header_regex
		if (grailsApplication.config.coverage.Transcriptome == 'y'){
			cov_check = true
          	println "Data has coverage info."
          	header_regex = "^>(.*?)_(.*)"
		}else{
          	header_regex = "^>(.*)"
          	println "Data has no coverage info."
        }
		def sequence=""
		def contig_id=""
		def count=0
		def count_gc		
		def contigMap = [:]
		contigFile.split("\n").each{
			if ((matcher = it =~ header_regex)){
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
					if (cov_check == true){
						coverage = sprintf("%.2f",coverage)
					}
					contigMap.coverage = coverage
					contigMap.length = sequence.length()
					contigMap.sequence = sequence
					//println contigMap
					if ((count % 1000) ==  0){
            			println count
            			new TransInfo(contigMap).save(flush:true)
            		}else{
            			new TransInfo(contigMap).save()
            		}					
					sequence=""
				}
				contig_id = matcher[0][1]
				if (cov_check == true){
					coverage = matcher[0][2].toFloat()
				}else{
					coverage = 0
				}
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

//add the genome data (header needs to be in format of >contigID_coverage)
addGenomeData()
def addGenomeData(){
		if (grailsApplication.config.seqData.Genome){
		println "Adding genome data - "+grailsApplication.config.seqData.Genome
		def contigFile = new File("data/"+grailsApplication.config.seqData.Genome.trim()).text
		def cov_check = false
      	def header_regex
		if (grailsApplication.config.coverage.Genome == 'y'){
			cov_check = true
          	println "Data has coverage info."
          	header_regex = "^>(.*?)_(.*)"
		}else{
          	header_regex = "^>(.*)"
          	println "Data has no coverage info."
        }
		def sequence=""
		def contig_id=""
		def count=0
		def count_gc		
		def coverage
		def contigMap = [:]
		contigFile.split("\n").each{
            if ((matcher = it =~ header_regex)){
				if (sequence != ""){
					//println "Adding $contig_id - $count"
					count++
					//get gc
					count_gc = sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
					def gc = (count_gc/sequence.length())*100
					gc = sprintf("%.2f",gc)
					//add data to map
					contigMap.contig_id = contig_id.trim()
					contigMap.gc = gc
					if (cov_check == true){
						coverage = sprintf("%.2f",coverage)
					}
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
					sequence=""
				}
				contig_id = matcher[0][1]
				if (cov_check == true){
					coverage = matcher[0][2].toFloat()
				}else{
					coverage = 0
				}
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
		//def nucFile = new File("data/A_viteae/test.fa".trim()).text
        def sequence=""
		def count=0
		nucFile.split("\n").each{
			if ((matcher = it =~ /^>(.*)/)){
				if (sequence != ""){
					nucData."${geneId}" = sequence
					sequence=""
				}
				geneId = matcher[0][1].trim()
			}else{
				sequence += it
			}               
        }
		//catch the last one
		nucData."${geneId}" = sequence
		
		println "Reading peptide data - "+grailsApplication.config.seqData.GenePep
		def pepFile = new File("data/"+grailsApplication.config.seqData.GenePep.trim()).text
		//def pepFile = new File("data/A_viteae/test.aa".trim()).text
        sequence=""
		pepFile.split("\n").each{
			if ((matcher = it =~ /^>(.*)/)){
				if (sequence != ""){
					pepData."${geneId}" = sequence
					sequence=""
				}
				geneId = matcher[0][1].trim() 
			}else{
				sequence += it
			}
             
        }
		//catch the last one
		pepData."${geneId}" = sequence
		
      	//println nucData
        //println pepData
      
      	println "Reading gff file - "+grailsApplication.config.seqData.GeneData
      	def dataFile = new File("data/"+grailsApplication.config.seqData.GeneData.trim()).text
      	//def dataFile = new File("data/A_viteae/test.gff".trim()).text
        def gene_count=0
      	def gene_id
        
        def exonMap = [:]
        def exon_id
        def exon_count
      	def exon_start
      	def exon_end
      	def exon_score
      	def exon_frame
        dataFile.split("\n").each{
          	
      		//ignore comment lines
        	if ((matcher = it =~ /^#.*/)){
              //println "ignoring "+it
            }else{
              def dataArray = it.split("\t")
              if (dataArray[2] == 'mRNA'){
                exon_count=0
                gene_count++
				if ((matcher = dataArray[8] =~ /ID=(.*?);.*/)){  
                  	gene_id = matcher[0][1]
                }
                geneMap.gene_id = gene_id
                geneMap.start = dataArray[3]
                geneMap.stop = dataArray[4]
                geneMap.source = dataArray[1]
                geneMap.contig_id = dataArray[0]
                geneMap.nuc = nucData."${gene_id}"
                geneMap.pep = pepData."${gene_id}"
				if ((gene_count % 1000) ==  0){
            			println gene_count
            			new GeneInfo(geneMap).save(flush:true)
            		}else{
            			new GeneInfo(geneMap).save()
            		}
              }
              if (dataArray[2] == 'CDS'){
                exon_count++
                if ((matcher = dataArray[8] =~ /ID=(.*?);.*/)){                
      		      	exonMap.exon_id = matcher[0][1]
                  	//println "exon = "+exon_id+" - "+exon_count
                }
                exonMap.contig_id = dataArray[0]
                exonMap.gene_id = gene_id
                exonMap.start = dataArray[3]
                exonMap.stop = dataArray[4]
                exonMap.score = dataArray[5]
                exonMap.phase = dataArray[6]
                if ((gene_count % 1000) ==  0){
                	//println exonMap
            		new ExonInfo(exonMap).save(flush:true)
            	}else{
            		new ExonInfo(exonMap).save()
            	}
              }
            }     
        }
	}else{
		println "One of the three required files is missing"
	}
}


