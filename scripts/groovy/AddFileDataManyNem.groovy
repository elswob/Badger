package badger

import groovy.sql.Sql

def grailsApplication
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)


//add data
A_vit()
L_sig()
D_imm()
O_och()

/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// A. viteae
def A_vit(){
	def metaMap = [:]
	def fileMap = [:]
	def genomeMap = [:]
	
// --- Species Data ---	
	metaMap.genus = "Acanthocheilonema";
	metaMap.species = "viteae";
	metaMap.description = "Acanthocheilonema viteae is a filarial nematode parasite of rodents. It is widely used as a model for human filariases. Importantly, A. viteae lacks the Wolbachia bacterial endosymbiont found in most human-infective filarial nematodes. Thus this species has become central in efforts to understand the role of the Wolbachia in the nematode-bacterial symbiosis, and in particular its possible role in immune evasion. The Wolbachia is also a drug target in nematodes that carry this symbiont, so work on A. viteae can also help to disentangle anti-nematode and anti-symbiont effects."
	metaMap.image_file = "a_viteae_lifecycle.jpg"
	metaMap.image_source = "A. viteae lifecycle; from http://www.uni-giessen.de"
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","01/02/2012")
	genomeMap.gversion = "1.0"
	addGenome(genomeMap)
	
// --- File Data --- 	
	//globals (blast,search,download and cov need to be added to each file type if the values differ)
	fileMap.file_dir = "A_viteae"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "Acanthocheilonema_viteae_v1.0.fna"
	fileMap.file_version = "1.0"
	fileMap.description = "The A. viteae genome was sequenced from material supplied by Kenneth Pfarr. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh. Assembly and annotation of the genome was performed by Georgios Koutsovoulos (assisted by Sujai Kumar and Alex Marshall)"
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "nAv.1.0.1.aug.blast2go.gff"
	fileMap.file_version = "1.0.1"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "nAv.1.0.1.aug.transcripts.fasta"
	fileMap.file_version = "1.0.1"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "nAv.1.0.1.aug.blast2go.gff"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "nAv.1.0.1.aug.proteins.fasta"
	fileMap.file_version = "1.0.1"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "nAv.1.0.1.aug.blast2go.gff"
	addFile(fileMap)

// --- Annotation files and data ---	
	def annoMap = [:]
	//blast
	annoMap.type = "blast"				
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "sprot.xml"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "NCBI NR"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "nr_10.xml"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.nematodes.org/nembase4/cluster.php?cluster="
	annoMap.source = "Nembase4"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "nembase_tblastn.xml"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Avit_a8r.ec.txt"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Avit_a8r.go.txt"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Avit_a8r.kegg.txt"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
	//interproscan
	annoMap.type = "ipr"
	annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
	annoMap.source = "InterProScan"
	annoMap.regex = "(IPR\\d+).*?"
	annoMap.anno_file = "A_viteae.iprscan.raw"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
// --- another genome starts here ---		
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","02/02/2012")
	addGenome(genomeMap)
// --- another genome ... ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","02/01/2013")
	addGenome(genomeMap)
}
/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// L.sigmodontis
def L_sig(){
	def metaMap = [:]
	def fileMap = [:]
	def genomeMap = [:]
	
// --- Species Data ---	
	metaMap.genus = "Litomosoides";
	metaMap.species = "sigmodontis";
	metaMap.image_file = "Litomosoides_sigmodontis.jpg"
	metaMap.image_source = "tails of male L. sigmodontis; by L. LeGeoff"
	metaMap.description = "Litomosoides sigmodontis is a filarial nematode parasite of rodents. Found in cotton rats in the wild, it has been adapted to the laboratory mouse and is widely used as a model for human filariases. Beginning with work in Odile Bain's laboratory in Paris, this L. sigmodontis model has become central in efforts to develop vaccines against filarial infections, test new drugs before they are progressed to clinical trials, and to investigate the basic biology of the fascinating interactions between parasitic nematodes and their mammalian hosts."
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nLs.2.1.2/"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","11/01/2013")
	genomeMap.gversion = "2.1"
	addGenome(genomeMap)
	
// --- File Data --- 
	fileMap.file_dir = "L_sigmodontis"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "Litomosoides_sigmodontis_2.1.fna"
	fileMap.file_version = "2.1"
	fileMap.description = "The L. sigmodontis genome was sequenced from material supplied by Simon Babayan. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh. Assembly and annotation of the genome was performed by Sujai Kumar (assisted by Graham Thomas, Georgios Koutsovoulos and Alex Marshall)."
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "nLs.2.1.2.aug.gff"
	fileMap.file_version = "2.1.2"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "nLs.2.1.2.aug.transcripts.fasta"
	fileMap.file_version = "2.1.2"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "nLs.2.1.2.aug.gff"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "nLs.2.1.2.aug.proteins.fasta"
	fileMap.file_version = "2.1.2"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "nLs.2.1.2.aug.gff"
	addFile(fileMap)
	
//annotation files and data
	def annoMap = [:]
	
	//blast
	annoMap.type = "blast"				
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "sprot.xml"
	annoMap.loaded = false	
	addAnno("nLs.2.1.2.aug.gff",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Lsig_a8r.ec.txt"
	annoMap.loaded = false	
	addAnno("nLs.2.1.2.aug.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Lsig_a8r.go.txt"
	annoMap.loaded = false	
	addAnno("nLs.2.1.2.aug.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Lsig_a8r.kegg.txt"
	annoMap.loaded = false	
	addAnno("nLs.2.1.2.aug.gff",annoMap)
}
/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// D.immitis
def D_imm(){

	def metaMap = [:]
	def fileMap = [:]
	def genomeMap = [:]

// --- Species Data ---	
	metaMap.genus = "Dirofilaria";
	metaMap.species = "immitis";
	metaMap.description = "The heartworm Dirofilaria immitis is an important parasite of dogs. Transmitted by mosquitoes in warmer climatic zones, it is spreading across Southern Europe and the Americas at an alarming pace. There is no vaccine and chemotherapy is prone to complications. To learn more about this parasite, we have sequenced the genomes of D. immitis and its endosymbiont Wolbachia."
	metaMap.image_file = "D_immitis.jpg"
	metaMap.image_source = "heartworm in situ; photo from S. Williams"
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nDi.2.2.2/"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","016/01/2012")
	genomeMap.gversion = "2.2"
	addGenome(genomeMap)

// --- File Data --- 	
	//global
	fileMap.file_dir = "D_immitis"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "Dirofilaria_immitis_2.2.fna"
	fileMap.file_version = "2.2"
	fileMap.description = "The D. immitis genome was sequenced by an international collaboration headed by Mark Blaxter and Pascal Mäser. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh, and FASTERIS SA, Switzerland. The version 2.2 assembly and annotation was performed by Sujai Kumar (assisted by Georgios Koutsovoulos and Alex Marshall)."
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "nDi.2.2.2.aug.blast2go.gff"
	fileMap.file_version = "2.2.2"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "nDi.2.2.2.aug.transcripts.fasta"
	fileMap.file_version = "2.1.2"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "nDi.2.2.2.aug.blast2go.gff"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "nDi.2.2.2.aug.proteins.fasta"
	fileMap.file_version = "2.1.2"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "nDi.2.2.2.aug.blast2go.gff"
	addFile(fileMap)
	
	//annotations
	def annoMap = [:]
	
	//blast
	annoMap.type = "blast"				
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "sprot.xml"
	annoMap.loaded = false	
	addAnno("nDi.2.2.2.aug.blast2go.gff",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Dimm_a8r.ec.txt"
	annoMap.loaded = false	
	addAnno("nDi.2.2.2.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Dimm_a8r.go.txt"
	annoMap.loaded = false	
	addAnno("nDi.2.2.2.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Dimm_a8r.kegg.txt"
	annoMap.loaded = false	
	addAnno("nDi.2.2.2.aug.blast2go.gff",annoMap)
}
/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// O. ochengi

def O_och(){
	def metaMap = [:]
	def fileMap = [:]
	def genomeMap = [:]
	
// --- Species Data ---	
	metaMap.genus = "Onchocerca";
	metaMap.species = "ochengi";
	metaMap.description = "Onchocerca ochengi is a filarial nematode parasite of cattle, and is native to West Africa, including Cameroon, where the specimens used for this genome project were isolated. As well as being a significant disease of native (Bos indicus) cattle, O. ochengi is very closely related to the human-parasitic Onchocerca volvulus. O. volvulus causes river blindness and skin disease throughout West Africa, and is the subject of intense efforts by several international agencies and teams aiming at disease eradication. The relationship between O. ochengi and O. volvulus, and concern over the possibility of cattle acting as a zoonotic reservoir make understanding of the parasite of some importance. Additionally, the genetic closeness to O. volvulus and the tractability of the bovine host makes the O. ochengi-cattle model a useful one in vaccine and drug development work."
	metaMap.image_file = "O_ochengi.jpg"
	metaMap.image_source = "O. ochengi larva; from Sandy Trees and colleagues http://ars.sciencedirect.com"
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nOo.2.0.1/"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","01/02/2012")
	genomeMap.gversion = "2.0"
	addGenome(genomeMap)	
	
// --- File Data --- 	
	//global
	fileMap.file_dir = "O_ochengi"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "Onchocerca_ochengi_nuclear_assembly_nOo.2.0.fna"
	fileMap.file_version = "2.0"
	fileMap.description = "The O. ochengi genome was sequenced from material supplied by Benjamin Makepeace. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh. Assembly and annotation of the genome was performed by Gaganjot Kaur and Alex Marshall (assisted by Sujai Kumar and Georgios Koutsovoulos)."
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "nOo.2.0.1.aug.gff"
	fileMap.file_version = "2.0.1"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "nOo.2.0.1.aug.transcripts.fasta"
	fileMap.file_version = "2.0.1"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "nOo.2.0.1.aug.gff"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "nOo.2.0.1.aug.proteins.fasta"
	fileMap.file_version = "2.0.1"
	fileMap.description = "Augustus gene prediction"
	fileMap.file_link = "nOo.2.0.1.aug.gff"
	addFile(fileMap)
	
	//annotations
	def annoMap = [:]
	
	//blast
	annoMap.type = "blast"				
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "sprot.xml"
	annoMap.loaded = false	
	addAnno("nOo.2.0.1.aug.gff",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Ooch_a8r.ec.txt"
	annoMap.loaded = false	
	addAnno("nOo.2.0.1.aug.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Ooch_a8r.go.txt"
	annoMap.loaded = false	
	addAnno("nOo.2.0.1.aug.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Ooch_a8r.kegg.txt"
	annoMap.loaded = false	
	addAnno("nOo.2.0.1.aug.gff",annoMap)
}
/////////////////////////////////////////////////////////////////////////////////////////////////////
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
