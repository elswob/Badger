package badger

import groovy.sql.Sql

def grailsApplication
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)

def cleanUpGorm() { 
	def sessionFactory = ctx.getBean("sessionFactory")
	def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    def session = sessionFactory.currentSession 
    session.flush() 
    session.clear() 
    propertyInstanceMap.get().clear() 
}

def getBlast(){
	def program = grailsApplication.config.blastPath
	return program
}

//edit phyloXMl file if present
def editTree(){
	def tree = new File("web-app/trees/badger_tree.xml")
	if (tree.exists()){tree.delete()}
	
	def treeFile = new File("web-app/trees/GK_nem_rooted.xml").text
	
	def species = MetaData.findAll()
	def sList = []
	species.each{
	  sList.push(it.genus+" "+it.species)
	}
	//println "list = "+sList
	
	treeFile.split("\n").each{
	  if ((matcher = it =~ /^<phylogeny rooted=.*/)){
		tree << it+"\n"
		tree << " <render>\n"
		tree << " <parameters>\n"
		tree << "  <circular>\n"
		tree << "   <bufferRadius>0.6</bufferRadius>\n"
		tree << "  </circular>\n"
		tree << "  <rectangular>\n"
		tree << "   <bufferX>200</bufferX>\n"
		tree << "  </rectangular>\n"
		tree << " </parameters>\n"
		tree << "  <charts>\n"
		tree << "   <component type=\"binary\" thickness=\"0\" />\n"
		tree << "  </charts>\n"
		tree << "  <styles>\n"
		tree << "   <bold fill=\"#e5bd94\" stroke=\"#e5bd94\" type=\"radialGradient\" font-size=\"12\">\n"        
		tree << "    <stop offset=\"0%\" style=\"stop-color:#e5bd94; stop-opacity:0\"/>\n"
		tree << "    <stop offset=\"93%\" style=\"stop-color:#e5bd94; stop-opacity:1\"/>\n"
		tree << "    <stop offset=\"100%\" style=\"stop-color:#D1A373; stop-opacity:1\"/>\n"
		tree << "   </bold>\n" 		
		tree << "   <none fill=\"#FFFFFF\" stroke=\"#FFFFFF\">\n"
    	tree << "   </none>\n"
		tree << "  </styles>\n"
		tree << " </render>\n"
	  }else if ((matcher = it =~ /(.*?)<name>(.*?)<\/name>/)){
		def l = it
		//println "match = "+matcher[0][2]
		if (matcher[0][2] in sList){
		  tree << matcher[0][1]+"<name bgStyle=\"bold\">"+matcher[0][2]+"</name>\n"
		  tree << matcher[0][1]+"<chart>\n"
		  tree << " "+matcher[0][1]+"<component>bold</component>\n"
		  tree << matcher[0][1]+"</chart>\n"
		  tree << matcher[0][1]+"<annotation>\n"
		  tree << " "+matcher[0][1]+"<desc>"+matcher[0][2]+"</desc>\n"
		  tree << " "+matcher[0][1]+"<uri>#"+matcher[0][2]+"</uri>\n"
		  tree << matcher[0][1]+"</annotation>\n" 
		  //println "in list"
		}else{
		  //tree << it+"\n"
		  tree << matcher[0][1]+"<name bgStyle=\"none\">"+matcher[0][2]+"</name>\n"
		}
	  }else{      
		tree << it+"\n"
	  }
   }
}
editTree()

def getFiles = FileData.findAllByLoaded(false,[sort:"id"])
if (getFiles){
	getFiles.each {  
		def blastPath = getBlast()	
		def fileLoc = it.file_dir+"/"+it.file_name
		println "Processing "+fileLoc
		println "Zipping up for download..."
		def ant = new AntBuilder()
		ant.zip(destfile: "data/"+it.file_dir+"/"+it.file_name+".zip", basedir: "data/"+it.file_dir, includes: it.file_name)
		if (it.file_type == "Genome"){
			println "Creating BLAST database..."
			def comm = "$blastPath/makeblastdb -in data/"+fileLoc+" -dbtype nucl -out data/"+fileLoc
			def p = comm.execute()   
			addGenomeData(fileLoc, it.cov, it.file_name)
		}else if (it.file_type == "Transcriptome"){
			println "Creating BLAST database..."
			def comm = "$blastPath/makeblastdb -in data/"+fileLoc+" -dbtype nucl -out data/"+fileLoc
			def p = comm.execute()  
			addTransData(fileLoc, it.cov, it.id)
		}else if (it.file_type == "Genes"){
			def getSeqs = FileData.findAllByFile_link(it.file_name)
			def nuc
			def pep
			getSeqs.each{
				def loc = it.file_dir+"/"+it.file_name
				if (it.file_type == "mRNA"){
					nuc = it
					println "Creating BLAST database..."
					def comm = "$blastPath/makeblastdb -in data/"+loc+" -dbtype nucl -out data/"+loc
					def p = comm.execute()               
				}else if (it.file_type == "Peptide"){
					pep = it
					println "Creating BLAST database..."
					def comm = "$blastPath/makeblastdb -in data/"+loc+" -dbtype prot -out data/"+loc
					def p = comm.execute() 
				}
			}
			addGeneData(fileLoc, it.file_name, nuc, pep)
		}
	}
}else{
	println "There are no new data files to load into the database"
}
//add the Transcripts
def addTransData(fileLoc, cov, data_id, file_id){
	println "Adding transcript data - "+fileLoc
	println new Date()
	def contigFile = new File("data/"+fileLoc).text
	def cov_check = false
	def header_regex
	if (cov == 'y'){
		cov_check = true
		println "Data has coverage info."
		header_regex = /^>(\w+)_(.*)/
	}else{
		header_regex = /^>(\w+)/
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
					cleanUpGorm()
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
def addGenomeData(fileLoc, cov, file_name){
	FileData Gfile = FileData.findByFile_name(file_name)
  	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
	println "Adding genome data for - "+fileLoc
	println new Date()
  	//println "Deleting old data..."
	//def delsql = "delete from genome_info where data_id = '"+data_id+"' and file_id = '"+file_id+"';";
	//sql.execute(delsql)
	def contigFile = new File("data/"+fileLoc).text
	def cov_check = false
	def header_regex
	if (cov == 'y'){
		cov_check = true
		println "Data has coverage info."
		header_regex = /^>(\w+)_(.*)/
	}else{
		header_regex = /^>(\w+)/
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
				GenomeInfo genome = new GenomeInfo(contigMap)
				Gfile.addToScaffold(genome)				
				if ((count % 2000) ==  0){
					println count
					genome.save(flush:true)
					println new Date()
					cleanUpGorm()
				}else{
					genome.save()
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
	GenomeInfo genome = new GenomeInfo(contigMap)
	Gfile.addToScaffold(genome)
	genome.save(flush:true)
	println count
	//mark file as loaded
	def gSql = "update file_data set loaded = true where file_name = '"+Gfile.file_name+"'";
	println gSql
    sql.execute(gSql)
	println Gfile.file_name+" is loaded"
}

//add the Genes
def addGeneData(fileLoc, file_name, nuc, pep){
	FileData gfile = FileData.findByFile_name(file_name)	
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	//println "Deleting any old gene and exon data..."
	//def gsql = "delete from gene_info where data_id = '"+data_id+"' and file_id = '"+file_id+"';";
	//def esql = "delete from exon_info where file_id = '"+file_id+"';";
	//sql.execute(gsql)
	//sql.execute(esql)
	def geneData = [:]
	def nucData = [:]
	def pepData = [:]
	def geneMap = [:]
	def geneId
	def gene_nuc
	def gene_count_gc
	def gene_start
	println "Adding new gene data... "
	println "Reading nucleotide data - "+nuc.file_dir+"/"+nuc.file_name
	def nucFile = new File("data/"+nuc.file_dir+"/"+nuc.file_name).text
	def sequence=""
	def count=0
	nucFile.split("\n").each{
		if ((matcher = it =~ /^>(.*)/)){
			if (sequence != ""){
				nucData."${geneId}" = sequence.toUpperCase()
				sequence=""
			}
			geneId = matcher[0][1].trim()
			//println geneId
		}else{
			sequence += it
		}               
	}
	//catch the last one
	nucData."${geneId}" = sequence.toUpperCase()
	
	println "Reading peptide data - "+pep.file_dir+"/"+pep.file_name
	def pepFile = new File("data/"+pep.file_dir+"/"+pep.file_name).text
	sequence=""
	pepFile.split("\n").each{
		if ((matcher = it =~ /^>(.*)/)){
			if (sequence != ""){
				pepData."${geneId}" = sequence.toUpperCase()
				sequence=""
			}
			geneId = matcher[0][1].trim() 
		}else{
			sequence += it
		}
		 
	}
	//catch the last one
	pepData."${geneId}" = sequence.toUpperCase()
  
	println "Reading gff file - "+fileLoc
	println "Adding gene data"
	println new Date()
	def dataFile = new File("data/"+fileLoc).text
	//def dataFile = new File("data/A_viteae/test.gff".trim()).text
	def gene_count=0
	def gene_id
	def mrna_id
	
	def exonMap = [:]
	def exon_id
	int exon_count
	int exon_count_all = 0
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
			//println "mrna_id = "+mrna_id
			gene_nuc = nucData."${mrna_id}"
			//println gene_nuc
			gene_count_gc = gene_nuc.toUpperCase().findAll({it=='G'|it=='C'}).size()
			gene_gc = (gene_count_gc/gene_nuc.length())*100
			gene_gc = sprintf("%.3f",gene_gc)
			geneMap.gc = gene_gc
			geneMap.gene_id = gene_id
			geneMap.mrna_id = mrna_id
			geneMap.start = dataArray[3]
			geneMap.stop = dataArray[4]
			geneMap.source = dataArray[1]
			geneMap.contig_id = dataArray[0]
			geneMap.strand = dataArray[6]
			geneMap.nuc = nucData."${mrna_id}"
			geneMap.pep = pepData."${mrna_id}"
			GeneInfo gene = new GeneInfo(geneMap)
			gfile.addToGene(gene)
			if ((gene_count % 5000) ==  0){
					println gene_count
					//println geneMap
					gene.save(flush:true)
					cleanUpGorm()
				}else{
					gene.save()
				}
		  	}
		}
	}
	println gene_count
	
	//mark files as loaded 
	   
    //FileData nucUp = FileData.findByFile_name(nuc.file_name)
	//nucUp.loaded = true
	//nucUp.save(flush:true)
	
    def nSql = "update file_data set loaded = true where file_name = '"+nuc.file_name+"'";
    println nSql
    sql.execute(nSql)
	println nuc.file_name+" is loaded"
	
	//FileData pepUp = FileData.findByFile_name(pep.file_name)
	//pepUp.loaded = true
	//pepUp.save(flush:true)
	
	def pSql = "update file_data set loaded = true where file_name = '"+pep.file_name+"'";
	println pSql
    sql.execute(pSql)
	println pep.file_name+" is loaded"
	
	//gfile.loaded = true
	//gfile.save(flush:true)
	//println gfile.file_name+" is loaded"
	
	gene_count = 0
	//read the file again as the gene tables need to be complete to use ids in exon tables
	println "Adding exon data"
	println new Date()
	dataFile = new File("data/"+fileLoc).text
	GeneInfo geneFind
	def parent = ""
	dataFile.split("\n").each{
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
				if ((gene_count % 1000) ==  0){
					println gene_count
					//println new Date()
				}
				if ((matcher = dataArray[8] =~ /ID=(.*?);.*/)){  
					mrna_id = matcher[0][1]
					geneFind = GeneInfo.findByMrna_id(mrna_id)
				}
			}
			if (dataArray[2] == 'CDS'){				
				exon_count++
				exon_count_all++
				if ((matcher = dataArray[8] =~ /ID=(.*?);Parent=(.*)/)){                
					exonMap.exon_id = matcher[0][1]
					//println "id = "+matcher[0][2]
					//check for alternative splicing
					if (parent != matcher[0][2] && exon_marker != 0){
						//println "alt trans"+parent+" - "+matcher[0][2]
						exon_marker = 0
					}
					parent = matcher[0][2]
					gene_nuc = nucData."${parent}"
					//println "exon = "+exon_id+" - "+exon_count
				}
				exon_start = exon_marker
				exon_end = dataArray[4].toInteger()-dataArray[3].toInteger()+exon_marker.toInteger()+1
				//println mrna_id
				//println gene_nuc
				//println dataArray[4].toInteger()+"-"+dataArray[3].toInteger()+"+"+exon_marker.toInteger()
				exon_sequence = gene_nuc[exon_marker..dataArray[4].toInteger()-dataArray[3].toInteger()+exon_marker.toInteger()]
				exon_count_gc = exon_sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
				exon_gc = (exon_count_gc/exon_sequence.length())*100
				exon_gc = sprintf("%.3f",exon_gc)
				exonMap.sequence = exon_sequence
				exon_marker = exon_start + dataArray[4].toInteger()-dataArray[3].toInteger()+1
				
				exonMap.exon_number = exon_count
				exonMap.start = dataArray[3].toInteger()
				exonMap.stop = dataArray[4].toInteger()
				exonMap.score = dataArray[5]
				exonMap.strand = dataArray[6]
				exonMap.phase = dataArray[7].toInteger()
				exonMap.gene = geneFind
				//exonMap.gene_id = geneFind.id.toBigInteger()
				exonMap.gc = exon_gc
				
				ExonInfo exon = new ExonInfo(exonMap)
				geneFind.addToExon(exon)
				
				if ((exon_count_all % 10000) ==  0){
					exon.save(flush:true)
					cleanUpGorm()
					//println exonMap
					//new ExonInfo(exonMap).save(flush:true)
				}else{
					//new ExonInfo(exonMap).save()
					exon.save()
				}
			}
	  	}    
	}
	println gene_count
	def fSql = "update file_data set loaded = true where file_name = '"+gfile.file_name+"'";
	println fSql
    sql.execute(fSql)
	println gfile.file_name+" is loaded"
    cleanUpGorm()
}