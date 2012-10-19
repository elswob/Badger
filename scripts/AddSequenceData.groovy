package GDB

import groovy.sql.Sql

def grailsApplication

def getPep(seq, int frame){
	//def trans = [ ATT:'I',ATC:'I',ATA:'I',CTT:'L',CTC:'L',CTA:'L',CTG:'L',TTA:'L',TTG:'L',GTT:'V',GTC:'V',GTA:'V',GTG:'V',TTT:'F',TTC:'F',ATG:'M',TGT:'C',TGC:'C',GCT:'A',GCC:'A',GCA:'A',GCG:'A',GGT:'G',GGC:'G',GGA:'G',GGG:'G',CCT:'P',CCC:'P',CCA:'P',CCG:'P',ACT:'T',ACC:'T',ACA:'T',ACG:'T',TCT:'S',TCC:'S',TCA:'S',TCG:'S',AGT:'S',AGC:'S',TAT:'Y',TAC:'Y',TGG:'W',CAA:'Q',CAG:'Q',AAT:'N',AAC:'N',CAT:'H',CAC:'H',GAA:'E',GAG:'E',GAT:'D',GAC:'D',AAA:'K',AAG:'K',CGT:'R',CGC:'R',CGA:'R',CGG:'R',AGA:'R',AGG:'R',TAA:'.',TAG:'.',TGA:'.' ];
	def trans = [ ATT:'I',ATC:'I',ATA:'I',CTT:'L',CTC:'L',CTA:'L',CTG:'L',TTA:'L',TTG:'L',GTT:'V',GTC:'V',GTA:'V',GTG:'V',TTT:'F',TTC:'F',ATG:'M',TGT:'C',TGC:'C',GCT:'A',GCC:'A',GCA:'A',GCG:'A',GGT:'G',GGC:'G',GGA:'G',GGG:'G',CCT:'P',CCC:'P',CCA:'P',CCG:'P',ACT:'T',ACC:'T',ACA:'T',ACG:'T',TCT:'S',TCC:'S',TCA:'S',TCG:'S',AGT:'S',AGC:'S',TAT:'Y',TAC:'Y',TGG:'W',CAA:'Q',CAG:'Q',AAT:'N',AAC:'N',CAT:'H',CAC:'H',GAA:'E',GAG:'E',GAT:'D',GAC:'D',AAA:'K',AAG:'K',CGT:'R',CGC:'R',CGA:'R',CGG:'R',AGA:'R',AGG:'R'];
  	def pepSeq = ""
	int i = frame
	while (i + 2 < (seq.length())){
		if (trans[seq[i..i+2]]){
			pepSeq += trans[seq[i..i+2]]
		}
		i += 3
	}
	return pepSeq
}

//def seq = "ATGGTGCTGTCTGCCGCCGACAAGGGCAATGTCAAGGCCGCCTGGGGCAAGGTTGGCGGCCACGCTGCAGAGTATGGCGCAGAGGCCCTG";
//def pep = getPep(seq,0)
//println "pep = "+pep

//load all genome data first as it is needed to gff
def getFiles = FileData.findAll()
getFiles.each {  	
	def fileLoc = it.file_dir+"/"+it.file_name
	if (it.file_type == "genome"){
      	println "Type = "+it.file_type
		addGenomeData(fileLoc, it.cov, it.data_id, it.file_id)
	}else if (it.file_type == "transcriptome"){
		//addTransData(fileLoc, it.cov)
	}
}

getFiles.each {  	
	def fileLoc = it.file_dir+"/"+it.file_name
	if (it.file_type == "gff"){
      	println "Type = "+it.file_type
		addGeneData(fileLoc, it.data_id, it.file_id)
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
	new TransInfo(contigMap).save(flush:true)
}

//add the genome data (for coverage info header needs to be in format of >contigID_coverage)
def addGenomeData(fileLoc, cov, data_id, file_id){
  	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
	println "Adding genome data for - "+fileLoc
  	println "Deleting old data..."
	def delsql = "delete from genome_info where data_id = '"+data_id+"' and file_id = '"+file_id+"';";
	sql.execute(delsql)
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
	new GenomeInfo(contigMap).save(flush:true)
}

//add the GFF data
def addGeneData(fileLoc, data_id, file_id){
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)	
	println "Reading gff file - "+fileLoc+" - data_id = "+data_id+" file_id = "+file_id
	println "Deleting old data..."
	def delsql = "delete from gene_info where data_id = '"+data_id+"' and file_id = '"+file_id+"';";
	sql.execute(delsql)
	def dataFile = new File("data/"+fileLoc).text
  	println dataFile
	def geneMap = [:]
	def gene_id
	def mrna_id
	def gene_nuc = ""
	def gene_pep
	def gene_count_gc
	def gene_start
  	def gene_stop
  	def gene_source
  	def gene_contig_id
  	def gene_strand
	def gene_count=0
	
	def exonMap = [:]
	def exon_id = ""
	int exon_count
	int exon_start
	int exon_end
	def exon_score
	def exon_frame
	def exon_count_gc
	def exon_sequence
	def exon_gc
    def scaffSeq
  	def comp = [ A:'T', T:'A', G:'C', C:'G', N:'N']
  	def exonList = [:]
	dataFile.split("\n").each{	
		//ignore comment lines
		if ((matcher = it =~ /^#.*/)){
		  //println "ignoring "+it
		}else{
          def dataArray = it.split("\t")
          if (dataArray[2] == 'gene'){
            gene_count++
            //add the gene info on second pass
          	if (exon_id != ""){ 
                   if (gene_strand == '-'){
                  //println exonList.sort{it.key}.collect{it}.reverse().value
                  exonList.sort{it.key}.collect{it}.reverse().value.each{
                    gene_nuc += it
                	}
                }else{
                  exonList.each{
                    gene_nuc += it.value
                  }
                }
              	//gene_nuc = "AAA"
				gene_count_gc = gene_nuc.toUpperCase().findAll({it=='G'|it=='C'}).size()
				gene_gc = (gene_count_gc/gene_nuc.length())*100
				gene_gc = sprintf("%.3f",gene_gc)
				geneMap.data_id = data_id
				geneMap.file_id = file_id
				geneMap.gc = gene_gc
				geneMap.gene_id = gene_id
            	geneMap.mrna_id = mrna_id
				geneMap.start = gene_start
				geneMap.stop = gene_stop
				geneMap.source = gene_source
				geneMap.contig_id = gene_contig_id
				geneMap.strand = gene_strand
				geneMap.nuc = gene_nuc
				geneMap.pep = getPep(gene_nuc,0)
				//println geneMap
				if ((gene_count % 1000) ==  0){
					println gene_count					
					new GeneInfo(geneMap).save(flush:true)
				}else{
					new GeneInfo(geneMap).save()
				}
		  
        	}
            def scaff = GenomeInfo.findByData_idAndContig_id(data_id,dataArray[0])
            scaffSeq = scaff.sequence.toUpperCase()
            gene_id = dataArray[8]
            gene_nuc = ""
            exon_sequence = ""
            exonList = [:]
            gene_start = dataArray[3].toInteger()
            gene_stop = dataArray[4].toInteger()
            gene_source = dataArray[1]
            gene_contig_id = dataArray[0]
            gene_strand = dataArray[6] 
            //check for reverse strand
            def compSeq = ""
            if (gene_strand == '-'){
            	scaffSeq.reverse().each{
                	compSeq += comp[it]
   				}
              	scaffSeq = compSeq
            }
		  }
		  if (dataArray[2] == 'mRNA'){
			exon_count=0
			if ((matcher = dataArray[8] =~ /ID=(.*?);.*/)){  
				mrna_id = matcher[0][1]
			}
		  }
		  if (dataArray[2] == 'CDS'){
			exon_count++
			if ((matcher = dataArray[8] =~ /ID=(.*?);.*/)){                
				exon_id = matcher[0][1]
                exonMap.exon_id = exon_id
				//println "exon = "+exon_id+" - "+exon_count
			}
			if (gene_strand == '-'){
              exon_start = scaffSeq.length() - (dataArray[4].toInteger())
              exon_end = scaffSeq.length() - (dataArray[3].toInteger())
            }else{
              exon_start = dataArray[3].toInteger() -1
              exon_end = dataArray[4].toInteger() -1
			}              
            exon_sequence = scaffSeq[exon_start..exon_end]
            //gene_nuc += exon_sequence
            exonList."${exon_count}" = exon_sequence
            println exon_start+" : "+exon_end+ " - "+data_id
			//get exon gc
			exon_count_gc = exon_sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
			exon_gc = (exon_count_gc/exon_sequence.length())*100
			exon_gc = sprintf("%.3f",exon_gc)
			exonMap.sequence = exon_sequence			
			exonMap.contig_id = dataArray[0]
			exonMap.exon_number = exon_count
			exonMap.mrna_id = mrna_id
			exonMap.start = exon_start
			exonMap.stop = exon_end
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
	if (gene_strand == '-'){
	  //println exonList.sort{it.key}.collect{it}.reverse().value
	  exonList.sort{it.key}.collect{it}.reverse().value.each{
		gene_nuc += it
		}
	}else{
	  exonList.each{
		gene_nuc += it.value
	  }
	}
	gene_count_gc = gene_nuc.toUpperCase().findAll({it=='G'|it=='C'}).size()
	gene_gc = (gene_count_gc/gene_nuc.length())*100
	gene_gc = sprintf("%.3f",gene_gc)
	geneMap.data_id = data_id
	geneMap.file_id = file_id
	geneMap.gc = gene_gc
	geneMap.gene_id = gene_id
	geneMap.mrna_id = mrna_id
	geneMap.start = gene_start
	geneMap.stop = gene_stop
	geneMap.source = gene_source
	geneMap.contig_id = gene_contig_id
	geneMap.strand = gene_strand
	geneMap.nuc = gene_nuc
	geneMap.pep = getPep(gene_nuc,0)				
	new GeneInfo(geneMap).save(flush:true)
}





