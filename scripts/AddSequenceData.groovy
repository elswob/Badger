package GDB

import groovy.sql.Sql

def grailsApplication
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)

def getPep(seq, int frame){
	def trans = [ ATT:'I',ATC:'I',ATA:'I',CTT:'L',CTC:'L',CTA:'L',CTG:'L',TTA:'L',TTG:'L',GTT:'V',GTC:'V',GTA:'V',GTG:'V',TTT:'F',TTC:'F',ATG:'M',TGT:'C',TGC:'C',GCT:'A',GCC:'A',GCA:'A',GCG:'A',GGT:'G',GGC:'G',GGA:'G',GGG:'G',CCT:'P',CCC:'P',CCA:'P',CCG:'P',ACT:'T',ACC:'T',ACA:'T',ACG:'T',TCT:'S',TCC:'S',TCA:'S',TCG:'S',AGT:'S',AGC:'S',TAT:'Y',TAC:'Y',TGG:'W',CAA:'Q',CAG:'Q',AAT:'N',AAC:'N',CAT:'H',CAC:'H',GAA:'E',GAG:'E',GAT:'D',GAC:'D',AAA:'K',AAG:'K',CGT:'R',CGC:'R',CGA:'R',CGG:'R',AGA:'R',AGG:'R',TAA:'.',TAG:'.',TGA:'.' ];
	//def trans = [ ATT:'I',ATC:'I',ATA:'I',CTT:'L',CTC:'L',CTA:'L',CTG:'L',TTA:'L',TTG:'L',GTT:'V',GTC:'V',GTA:'V',GTG:'V',TTT:'F',TTC:'F',ATG:'M',TGT:'C',TGC:'C',GCT:'A',GCC:'A',GCA:'A',GCG:'A',GGT:'G',GGC:'G',GGA:'G',GGG:'G',CCT:'P',CCC:'P',CCA:'P',CCG:'P',ACT:'T',ACC:'T',ACA:'T',ACG:'T',TCT:'S',TCC:'S',TCA:'S',TCG:'S',AGT:'S',AGC:'S',TAT:'Y',TAC:'Y',TGG:'W',CAA:'Q',CAG:'Q',AAT:'N',AAC:'N',CAT:'H',CAC:'H',GAA:'E',GAG:'E',GAT:'D',GAC:'D',AAA:'K',AAG:'K',CGT:'R',CGC:'R',CGA:'R',CGG:'R',AGA:'R',AGG:'R'];
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
		addTransData(fileLoc, it.cov)
	}
}

getFiles.each {  	
	def fileLoc = it.file_dir+"/"+it.file_name
	if (it.file_type == "gff"){
      	println "Type = "+it.file_type
      	def getTransSql = "select file_name from file_data where file_type = 'mrna_trans' and file_link = '"+it.file_id+"';";
      	def getTrans = sql.rows(getTransSql)
      	def getPepSql = "select file_name from file_data where file_type = 'mrna_pep' and file_link = '"+it.file_id+"';";
      	def getPep = sql.rows(getPepSql)
      	def trans = it.file_dir+"/"+getTrans.file_name[0]
      	def pep = it.file_dir+"/"+getPep.file_name[0]
      	//def getTrans = FileData.findByFile_link(it.file_id)
      	println "trans = "+trans
      	println "pep = "+pep
		addGeneData(fileLoc, it.data_id, it.file_id, trans, pep)
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

//add the Genes
def addGeneData(fileLoc, data_id, file_id, trans, pep){	
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	println "Deleting any old gene and exon data..."
	def gsql = "delete from gene_info where data_id = '"+data_id+"' and file_id = '"+file_id+"';";
	def esql = "delete from exon_info where file_id = '"+file_id+"';";
	sql.execute(gsql)
	sql.execute(esql)
	def geneData = [:]
	def nucData = [:]
	def pepData = [:]
	def geneMap = [:]
	def geneId
	def gene_nuc
	def gene_count_gc
	def gene_start
	println "Adding new gene data... "
	println "Reading nucleotide data - "+trans
	def nucFile = new File("data/"+trans).text
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
	
	println "Reading peptide data - "+pep
	def pepFile = new File("data/"+pep).text
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
  
	println "Reading gff file - "+fileLoc
	def dataFile = new File("data/"+fileLoc).text
	//def dataFile = new File("data/A_viteae/test.gff".trim()).text
	def gene_count=0
	def gene_id
	def mrna_id
	
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
		  if (dataArray[2] == 'gene'){
		  		if ((matcher = dataArray[8] =~ /ID=(.*?);.*/)){  
					gene_id = matcher[0][1]
				}else if ((matcher = dataArray[8] =~ /ID=(.*)/)){  
					gene_id = matcher[0][1]
				}	
		  }
		  if (dataArray[2] == 'mRNA' || dataArray[2] == 'transcript'){
			exon_marker=0
			exon_count=0
			gene_count++
			if ((matcher = dataArray[8] =~ /ID=(.*?);.*/)){  
				mrna_id = matcher[0][1]
			}
			gene_start = dataArray[3].toInteger()
			gene_nuc = nucData."${mrna_id}"
			gene_count_gc = gene_nuc.toUpperCase().findAll({it=='G'|it=='C'}).size()
			gene_gc = (gene_count_gc/gene_nuc.length())*100
			gene_gc = sprintf("%.3f",gene_gc)
			geneMap.data_id = data_id
			geneMap.file_id = file_id
			geneMap.gc = gene_gc
			geneMap.gene_id = gene_id
			geneMap.start = dataArray[3]
			geneMap.stop = dataArray[4]
			geneMap.source = dataArray[1]
			geneMap.contig_id = dataArray[0]
			geneMap.strand = dataArray[6]
			geneMap.nuc = nucData."${mrna_id}"
			geneMap.pep = pepData."${mrna_id}"
			geneMap.gene_id = gene_id
            geneMap.mrna_id = mrna_id
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
			exonMap.mrna_id = mrna_id
			exonMap.start = dataArray[3]
			exonMap.stop = dataArray[4]
			exonMap.score = dataArray[5]
			exonMap.strand = dataArray[6]
			exonMap.phase = dataArray[7].toInteger()
			exonMap.gc = exon_gc
			exonMap.file_id = file_id
			if ((gene_count % 1000) ==  0){
				//println exonMap
				new ExonInfo(exonMap).save(flush:true)
			}else{
				new ExonInfo(exonMap).save()
			}
		  }
		}     
	}
}






