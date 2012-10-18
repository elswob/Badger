package GDB

import groovy.sql.Sql

def grailsApplication

def getFiles = FileData.findAll()
getFiles.each {
	def fileLoc = it.file_dir+"/"+it.file_name
	if (it.file_type == "genome"){
		addGenomeData(fileLoc, it.cov, it.data_id, it.file_id)
	}else if (it.file_type == "transcriptome"){
		//addTransData(fileLoc, it.cov)
	}else if (it.file_type == "genes"){
		//addGenomeData(fileLoc, it.cov)
	}
}

//add the Transcripts
def addTransData(fileLoc, cov, data_id, file_id){
	println "Adding transcript data - "+fileLoc
	def contigFile = new File("data/"+fileLoc).text
	def cov_check = false
	def header_regex
	if (cov == 'y'){
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
				contigMap.data_id = data_id
				contigMap.file_id = file_id
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
	contigMap.coverage = coverage
	contigMap.data_id = data_id
	contigMap.file_id = file_id
	new TransInfo(contigMap).save()
}

//add the genome data (for coverage info header needs to be in format of >contigID_coverage)
def addGenomeData(fileLoc, cov, data_id, file_id){
	println "Adding genome data - "+fileLoc
	def contigFile = new File("data/"+fileLoc).text
	def cov_check = false
	def header_regex
	if (cov == 'y'){
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
				contigMap.data_id = data_id
				contigMap.file_id = file_id
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
	contigMap.data_id = data_id
	contigMap.file_id = file_id
	new GenomeInfo(contigMap).save()
}

//add the GFF data
def addGeneData(){	
	if (grailsApplication.config.seqData.GeneNuc && grailsApplication.config.seqData.GenePep && grailsApplication.config.seqData.GeneData){
		def geneData = [:]
		def nucData = [:]
		def pepData = [:]
      	def geneMap = [:]
        def geneId
      	def gene_nuc
        def gene_count_gc
      	def gene_start
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
        int exon_count
      	int exon_start
      	int exon_marker
      	int exon_end
      	def exon_score
      	def exon_frame
      	def exon_count_gc
      	def exon_sequence
      	def exon_gc
        dataFile.split("\n").each{
          	
      		//ignore comment lines
        	if ((matcher = it =~ /^#.*/)){
              //println "ignoring "+it
            }else{
              def dataArray = it.split("\t")
              if (dataArray[2] == 'mRNA'){
              	exon_marker=0
                exon_count=0
                gene_count++
				if ((matcher = dataArray[8] =~ /ID=(.*?);.*/)){  
                  	gene_id = matcher[0][1]
                }
                gene_start = dataArray[3].toInteger()
                gene_nuc = nucData."${gene_id}"
				gene_count_gc = gene_nuc.toUpperCase().findAll({it=='G'|it=='C'}).size()
				gene_gc = (gene_count_gc/gene_nuc.length())*100
				gene_gc = sprintf("%.3f",gene_gc)
				geneMap.gc = gene_gc
                geneMap.gene_id = gene_id
                geneMap.start = dataArray[3]
                geneMap.stop = dataArray[4]
                geneMap.source = dataArray[1]
                geneMap.contig_id = dataArray[0]
                geneMap.strand = dataArray[6]
                geneMap.nuc = nucData."${gene_id}"
                geneMap.pep = pepData."${gene_id}"
				if ((gene_count % 1000) ==  0){
            			println gene_count
            			//println geneMap
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
                //println "gene_id = "+gene_id
                //println "exon count = "+exon_count
                //println "exon start = "+dataArray[3]
                //println "exon_stop = "+dataArray[4]
                //println "gene_start = "+gene_start
                exon_start = exon_marker
                exon_end = dataArray[4].toInteger()-dataArray[3].toInteger()+exon_marker.toInteger()+1
                //println "exon start real = "+exon_start
                //println "exon stop real = "+exon_end
                //println "exon length = "+gene_nuc.length()
                //println "exon_marker = "+exon_marker
                exon_sequence = gene_nuc[exon_marker..dataArray[4].toInteger()-dataArray[3].toInteger()+exon_marker.toInteger()]
                //println "exon seq = "+exon_sequence
                //get exon gc
				exon_count_gc = exon_sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
				exon_gc = (exon_count_gc/exon_sequence.length())*100
				exon_gc = sprintf("%.3f",exon_gc)
				exonMap.sequence = exon_sequence
				exon_marker = exon_start + dataArray[4].toInteger()-dataArray[3].toInteger()+1
				
                exonMap.contig_id = dataArray[0]
                exonMap.exon_number = exon_count
                exonMap.gene_id = gene_id
                exonMap.start = dataArray[3]
                exonMap.stop = dataArray[4]
                exonMap.score = dataArray[5]
                exonMap.strand = dataArray[6]
                exonMap.phase = dataArray[7].toInteger()
                exonMap.gc = exon_gc
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

//package GDB

//import groovy.sql.Sql
//def nucToPepService

//getPep()
//def getPep(){
// 	def seq = "ATGGTGCTGTCTGCCGCCGACAAGGGCAATGTCAAGGCCGCCTGGGGCAAGGTTGGCGGCCACGCTGCAGAGTATGGCGCAGAGGCCCTG";
//	def service = nucToPepService.getPep(seq)
//    println "service = +"service
//    println "seq = "+seq
//}



