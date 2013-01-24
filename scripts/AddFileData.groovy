package badger

import groovy.sql.Sql

def grailsApplication
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////Edit the section below ////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

// This just allows each data set to be turned on and off simply by commenting out the function call
H_duj()

/////// H. dujardini
def H_duj(){
	def metaMap = [:]
	def genomeMap = [:]
	def fileMap = [:]	
	def annoMap = [:]
	
// --- Species Data ---	
	metaMap.genus = "Hypsibius";
	metaMap.species = "dujardini";
	metaMap.description = "Hypsibius dujardinia are members of the phylum Tardigrada. These species can also be known as water bears or moss piglets. They are microscopic invertebrates, and their body lengths are only between 0.05-1.2mm. long. These organisms have four pairs of legs which have four to eight claws on each. Usually they live between four months to one year. However, they can survive in difficult conditions. They shut down their metabolisms which allows them to survive in extreme temperatures, pressure, and radiation for a long time. They feed on the fluids of plant cells, animal cells, and bacteria.."
	metaMap.image_file = "H_dujardini.jpg"
	metaMap.image_source = "http://mrrohanbio.wikispaces.com/Hypsibius+dujardini"
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nHd.2.3.1"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","17/01/2012")
	genomeMap.gversion = "2.3"
	addGenome(genomeMap)
	
// --- File Data --- 	
	// globals (blast,search,download and cov need to be added to each file type if the values differ)
	// blast, search and download can be either public (pub) or private (priv)
	// coverage can be either yes (y) or no (n)
	// file_link is only required for the mRNA and peptide files and needs to match the name of the GFF3 from which they originated
	fileMap.file_dir = "H_dujardini"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	fileMap.file_link = "n"
	
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "nHd.2.3.abv500.fna"
	fileMap.file_version = "2.3"
	fileMap.description = "The H. dujardini genome was sequenced by ...."
	addFile(fileMap)
	
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "maker1.gff3"
	fileMap.file_version = "1.0"
	fileMap.description = "MAKER gene prediction"
	addFile(fileMap)
	
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "all.maker.transcripts.edit.fasta"
	fileMap.file_version = "1.0"
	fileMap.description = "MAKER gene prediction"
	fileMap.file_link = "maker1.gff3"
	addFile(fileMap)
	
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "all.maker.proteins.edit.fasta"
	fileMap.file_version = "1.0"
	fileMap.description = "MAKER gene prediction"
	fileMap.file_link = "maker1.gff3"
	addFile(fileMap)

// --- Annotation data ---	
	// the addAnno function requires two values, the first is the name of the GFF3 file to which the annotations should be assigned
	// the second should remain unchanged
	
	//blast data
	annoMap.type = "blast"				
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "sprot.xml"
	annoMap.loaded = false	
	addAnno("maker1.gff3",annoMap)
	
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "NCBI NR"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "nr_10.xml"
	annoMap.loaded = false	
	addAnno("maker1.gff3",annoMap)
	
	annoMap.link = "http://www.nematodes.org/nembase4/cluster.php?cluster="
	annoMap.source = "Nembase4"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "nembase_tblastn.xml"
	annoMap.loaded = false	
	addAnno("maker1.gff3",annoMap)
	
	//functional data
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("maker1.gff3",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("maker1.gff3",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("maker1.gff3",annoMap)
	
	//interproscan data
	annoMap.type = "ipr"
	annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
	annoMap.source = "InterProScan"
	annoMap.regex = "(IPR\\d+).*?"
	annoMap.anno_file = "A_viteae.iprscan.raw"
	annoMap.loaded = false	
	addAnno("maker1.gff3",annoMap)
	
	//augustus genes
	
	// --- Species Data ---	
	metaMap.genus = "Hypsibius";
	metaMap.species = "dujardini";
	metaMap.description = "Hypsibius dujardinia are members of the phylum Tardigrada. These species can also be known as water bears or moss piglets. They are microscopic invertebrates, and their body lengths are only between 0.05-1.2mm. long. These organisms have four pairs of legs which have four to eight claws on each. Usually they live between four months to one year. However, they can survive in difficult conditions. They shut down their metabolisms which allows them to survive in extreme temperatures, pressure, and radiation for a long time. They feed on the fluids of plant cells, animal cells, and bacteria.."
	metaMap.image_file = "H_dujardini.jpg"
	metaMap.image_source = "http://mrrohanbio.wikispaces.com/Hypsibius+dujardini"
	addMeta(metaMap)
	
	// --- Genome Data ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nHd.2.3.1"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","17/01/2012")
	genomeMap.gversion = "2.3"
	addGenome(genomeMap)
	
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "nHd.2.3.1.aug.gff"
	fileMap.file_version = "2.3.1"
	fileMap.description = "AUGUSTUS gene prediction"
	addFile(fileMap)
	
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "nHd.2.3.1.aug.transcripts.fasta"
	fileMap.file_version = "2.3.1"
	fileMap.description = "AUGUSTUS gene prediction"
	fileMap.file_link = "nHd.2.3.1.aug.gff"
	addFile(fileMap)
	
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "nHd.2.3.1.aug.proteins.fasta"
	fileMap.file_version = "2.3.1"
	fileMap.description = "AUGUSTUS gene prediction"
	fileMap.file_link = "nHd.2.3.1.aug.gff"
	addFile(fileMap)

// // --- Annotation files and data ---
	annoMap = [:]
	//blast
	annoMap.type = "blast"
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "nHd.2.3.1.aug.proteins.fasta.swiss.xml"
	annoMap.loaded = false
	addAnno("nHd.2.3.1.aug.gff",annoMap)
	
	annoMap.link = "http://www.ncbi.nlm.nih.gov/nucest/"
	annoMap.source = "Tardigrade ESTs"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "nHd.2.3.1.aug.proteins.fasta.tardEST.xml"
	annoMap.loaded = false
	addAnno("nHd.2.3.1.aug.gff",annoMap)

	//functional
	annoMap.type = "fun"
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "nHd.2.3.1.aug.proteins.fasta.ec.out.ec.txt"
	annoMap.loaded = false
	addAnno("nHd.2.3.1.aug.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "nHd.2.3.1.aug.proteins.fasta.go.out.go.txt"
	annoMap.loaded = false
	addAnno("nHd.2.3.1.aug.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "nHd.2.3.1.aug.proteins.fasta.kegg.out.kegg.txt"
	annoMap.loaded = false
	addAnno("nHd.2.3.1.aug.gff",annoMap)
	
	//interproscan
	annoMap.type = "ipr"
	annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
	annoMap.source = "InterProScan"                              
	annoMap.regex = "(IPR\\d+).*?"
	annoMap.anno_file = "H_dujardini_aug_ipr.out"
	annoMap.loaded = false
	addAnno("nHd.2.3.1.aug.gff",annoMap)
	
}

// To add another species start a new function, e.g.
//def new_species(){
//	...
//}

// To add another genome add another 'Genome Data' section inside the same species function

// To add another set of genes add another set of gff, mRNA and peptides options from the 'File Data' section inside the same species function

// To add more annotation data add another 'Annotation Data' section inside the same species function


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Do not edit below this line /////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

//MetaData 
def genus,species,gversion,description,gbrowse,image_file,image_source
//FileData
def blast,cov,download,file_dir,file_link,file_name,file_type,file_version,loaded,search

MetaData meta
def addMeta(metaMap){
	def check = MetaData.findByGenusAndSpecies(metaMap.genus, metaMap.species)
	if (check){
		println metaMap.genus+" "+metaMap.species+" already exists - "+check
		meta = check
	}else{  		
		meta = new MetaData(metaMap)
		meta.save(flush:true)
	}
}

//set the new genome to global to be picked up by addFile
GenomeData new_genome
def addGenome(genomeMap){
	def check = GenomeData.findByGversion(genomeMap.gversion)
	if (check){
		println "genome version "+genomeMap.gversion+" already exists - "+check
		new_genome = check
	}else{
		println genomeMap
		new_genome = new GenomeData(genomeMap) 
		meta.addToGenome(new_genome)
		new_genome.save(flush:true)
		println "New genome for "+new_genome.meta.genus+" "+new_genome.meta.species+" date "+genomeMap.dateString+" was added"
	}
}

def addFile(fileMap){
	def check = FileData.findByFile_nameAndFile_type(fileMap.file_name, fileMap.file_type)
	if (check){
		println "file name "+fileMap.file_name+" type "+fileMap.file_type+" already exists - "+check
	}
	else if (new File("data/"+fileMap.file_dir+"/"+fileMap.file_name).exists()){
		println "Adding "+fileMap
		FileData file = new FileData(fileMap) 
		new_genome.addToFiles(file)
		file.save(flush:true)
		println fileMap.file_name+" was added"
	}else{
		println "file data/"+fileMap.file_dir+"/"+fileMap.file_name+" does not exist!"
	}
}
def addAnno(fileName,annoMap){
	FileData file = FileData.findByFile_name(fileName)
	//println "file = "+file
	def filedir = FileData.findByFile_name(fileName).file_dir
	//println "filedir = "+filedir
	def check = []
	if (FileData.findByFile_name(fileName).anno){
		//println "anno = "+FileData.findByFile_name(fileName).anno
		check = FileData.findByFile_name(fileName).anno.anno_file			
	}
	//println "check = "+check
	if (annoMap.anno_file in check){
		println annoMap.anno_file+" already exists for "+fileName+" so not adding"
	}else{
		if (new File("data/"+filedir+"/"+annoMap.anno_file).exists()){				
			println annoMap
			AnnoData anno = new AnnoData(annoMap)
			file.addToAnno(anno)
			anno.save(flush:true)
			println annoMap.anno_file+" was added"
		}else{
			println "data/"+filedir+"/"+annoMap.anno_file+" doesn't exist"
		}
	}
}
