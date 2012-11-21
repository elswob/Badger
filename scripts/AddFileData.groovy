package badger

import groovy.sql.Sql

def grailsApplication
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)

//MetaData 
def genus,species,gversion,description,gbrowse,image_file,image_source
//FileData
def blast,cov,download,file_dir,file_link,file_name,file_type,file_version,loaded,search

MetaData meta
def addMeta(metaMap){
	def check = MetaData.findByGenusAndSpecies(metaMap.genus, metaMap.species)
	if (check){
		println "species already exists - "+check
	}else{  		
		meta = new MetaData(metaMap)
		meta.save(flush:true)
	}
}
def addFile(fileMap){
	def check = FileData.findByFile_nameAndFile_type(fileMap.file_name, fileMap.file_type)
	if (check){
		println "file name and type already exists - "+check
	}
	else if (new File("data/"+fileMap.file_dir+"/"+fileMap.file_name).exists()){
		println fileMap
		FileData file = new FileData(fileMap) 
		meta.addToFiles(file)
		file.save(flush:true)
		println fileMap.file_name+" was added"
	}else{
		println "file data/"+fileMap.file_dir+"/"+params.file_name+" does not exist!"
	}
}
def addAnno(fileName,annoMap){
	FileData file = FileData.findByFile_name(fileName)
	println "file = "+file
	def filedir = FileData.findByFile_name(fileName).file_dir
	println "filedir = "+filedir
	def check = []
	if (FileData.findByFile_name(fileName).anno){
		println "anno = "+FileData.findByFile_name(fileName).anno
		check = FileData.findByFile_name(fileName).anno.anno_file			
	}
	println "check = "+check
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

//add data
//test()
A_vit()
L_sig()
D_imm()
O_och()
B_mal()
C_ang()
B_xyl()
H_con()
//M_inc()
S_rat()
T_spi()
//C_ele()

///////// test data
def test(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Test";
	metaMap.species = "test";
	metaMap.gversion = "1.0.1"
	metaMap.description = "Test data set"
	metaMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/"
	metaMap.image_file = "a_viteae_lifecycle.jpg"
	metaMap.image_source = "A. viteae lifecycle; from http://www.uni-giessen.de"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "test"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "test_genome.fa"
	fileMap.file_version = "1.0"
	fileMap.description = "Test genome"
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "test.gff"
	fileMap.file_version = "1.0"
	fileMap.description = "Test gff"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "nAv.1.0.1.aug.transcripts.fasta"
	fileMap.file_version = "1.0"
	fileMap.description = "Test mRNA"
	fileMap.file_link = "test.gff"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "nAv.1.0.1.aug.proteins.fasta"
	fileMap.file_version = "1.0"
	fileMap.description = "Test peptide"
	fileMap.file_link = "test.gff"
	addFile(fileMap)
}
/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// A. viteae
def A_vit(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Acanthocheilonema";
	metaMap.species = "viteae";
	metaMap.gversion = "1.0.1"
	metaMap.description = "Acanthocheilonema viteae is a filarial nematode parasite of rodents. It is widely used as a model for human filariases. Importantly, A. viteae lacks the Wolbachia bacterial endosymbiont found in most human-infective filarial nematodes. Thus this species has become central in efforts to understand the role of the Wolbachia in the nematode-bacterial symbiosis, and in particular its possible role in immune evasion. The Wolbachia is also a drug target in nematodes that carry this symbiont, so work on A. viteae can also help to disentangle anti-nematode and anti-symbiont effects."
	metaMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/"
	metaMap.image_file = "a_viteae_lifecycle.jpg"
	metaMap.image_source = "A. viteae lifecycle; from http://www.uni-giessen.de"
	addMeta(metaMap)
	
	//global
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
	
	//annotations
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
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("nAv.1.0.1.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
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
}
/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// L.sigmodontis
def L_sig(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Litomosoides";
	metaMap.species = "sigmodontis";
	metaMap.gversion = "2.1"
	metaMap.description = "Litomosoides sigmodontis is a filarial nematode parasite of rodents. Found in cotton rats in the wild, it has been adapted to the laboratory mouse and is widely used as a model for human filariases. Beginning with work in Odile Bain's laboratory in Paris, this L. sigmodontis model has become central in efforts to develop vaccines against filarial infections, test new drugs before they are progressed to clinical trials, and to investigate the basic biology of the fascinating interactions between parasitic nematodes and their mammalian hosts."
	metaMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nLs.2.1.2/"
	metaMap.image_file = "Litomosoides_sigmodontis.jpg"
	metaMap.image_source = "tails of male L. sigmodontis; by L. LeGeoff"
	addMeta(metaMap)
	
	//global
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
	
	//annotations
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
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("nLs.2.1.2.aug.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("nLs.2.1.2.aug.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("nLs.2.1.2.aug.gff",annoMap)
}
/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// D.immitis
def D_imm(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Dirofilaria";
	metaMap.species = "immitis";
	metaMap.gversion = "2.2.2"
	metaMap.description = "The heartworm Dirofilaria immitis is an important parasite of dogs. Transmitted by mosquitoes in warmer climatic zones, it is spreading across Southern Europe and the Americas at an alarming pace. There is no vaccine and chemotherapy is prone to complications. To learn more about this parasite, we have sequenced the genomes of D. immitis and its endosymbiont Wolbachia."
	metaMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nDi.2.2.2/"
	metaMap.image_file = "D_immitis.jpg"
	metaMap.image_source = "heartworm in situ; photo from S. Williams"
	addMeta(metaMap)
	
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
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("nDi.2.2.2.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("nDi.2.2.2.aug.blast2go.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("nDi.2.2.2.aug.blast2go.gff",annoMap)
}
/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// O. ochengi
def O_och(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Onchocerca";
	metaMap.species = "ochengi";
	metaMap.gversion = "2.2.2"
	metaMap.description = "Onchocerca ochengi is a filarial nematode parasite of cattle, and is native to West Africa, including Cameroon, where the specimens used for this genome project were isolated. As well as being a significant disease of native (Bos indicus) cattle, O. ochengi is very closely related to the human-parasitic Onchocerca volvulus. O. volvulus causes river blindness and skin disease throughout West Africa, and is the subject of intense efforts by several international agencies and teams aiming at disease eradication. The relationship between O. ochengi and O. volvulus, and concern over the possibility of cattle acting as a zoonotic reservoir make understanding of the parasite of some importance. Additionally, the genetic closeness to O. volvulus and the tractability of the bovine host makes the O. ochengi-cattle model a useful one in vaccine and drug development work."
	metaMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nOo.2.0.1/"
	metaMap.image_file = "O_ochengi.jpg"
	metaMap.image_source = "O. ochengi larva; from Sandy Trees and colleagues http://ars.sciencedirect.com"
	addMeta(metaMap)
	
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
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("nOo.2.0.1.aug.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("nOo.2.0.1.aug.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("nOo.2.0.1.aug.gff",annoMap)
}
/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// B. malayi
def B_mal(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Brugia";
	metaMap.species = "malayi";
	metaMap.gversion = "234"
	metaMap.description = "Brugia malayi is a gonochoristic (male-female) filarial parasite, of medical interest because it infects mosquito vectors (Aedes,Anopheles, and Culex) and humans, and is phylogenetically representative of other infectious nematodes. Infection of humans by B. malayi causes filariasis."
	metaMap.gbrowse = "http://www.wormbase.org/tools/genome/gbrowse/b_malayi/"
	metaMap.image_file = "BmalayiL3.gif"
	metaMap.image_source = "nematodes.org"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "B_malayi"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "b_malayi.WS234.genomic.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase."
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "b_malayi.WS234.annotations_trim.gff3"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "b_malayi.WS234.cds_transcripts_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "b_malayi.WS234.annotations_trim.gff3"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "b_malayi.WS234.protein_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "b_malayi.WS234.annotations_trim.gff3"
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
	addAnno("b_malayi.WS234.annotations_trim.gff3",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("b_malayi.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("b_malayi.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("b_malayi.WS234.annotations_trim.gff3",annoMap)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// C. angaria
def C_ang(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Caenorhabditis";
	metaMap.species = "angaria";
	metaMap.gversion = "234"
	metaMap.description = "Caenorhabditis angaria (ex-species 3) is a gonochoristic (male-female) species that is part of the Drosophilae super-group of Caenorhabditis species, with quite distinct morphology and behavior compared to C. elegans. Molecular divergence with C. elegans is similar to the fish-human distance. The closest known relative of C. angaria is C. sp. 12, another male-female species with which it can produce F1 hybrids. C. angaria is found in tropical regions, associated with palm and sugarcane weevils, Rhynchophorus palmarum and Metamasius hemipterus (Curculionidae) in Trinidad and Florida. Dauer larvae wave and are transported by adult weevils. The association is probably phoretic, although C. angaria can develop on dead weevils. A notable difference between C. angaria and C. elegans is the spiral male mating behavior of the former."
	metaMap.gbrowse = "http://www.wormbase.org/tools/genome/gbrowse/c_angaria/"
	metaMap.image_file = "acrobelesrainbow.gif"
	metaMap.image_source = "nematodes.org"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "C_angaria"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "c_angaria.WS234.genomic.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase."
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "c_angaria.WS234.annotations_trim.gff3"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "c_angaria.WS234.cds_transcripts_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "c_angaria.WS234.annotations_trim.gff3"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "c_angaria.WS234.protein_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "c_angaria.WS234.annotations_trim.gff3"
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
	addAnno("c_angaria.WS234.annotations_trim.gff3",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("c_angaria.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("c_angaria.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("c_angaria.WS234.annotations_trim.gff3",annoMap)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////// B. xylophilus
def B_xyl(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Bursaphelenchus";
	metaMap.species = "xylophilus";
	metaMap.gversion = "234"
	metaMap.description = "Bursaphelenchus xylophilus is a gonochoristic species of the Clade IV nematodes. It is a parasite of Pine trees thought to originate from North America, but has spread through East Asia and Portugal. It is also known as Pine Wilt Nematode (PWN)."
	metaMap.gbrowse = "http://www.wormbase.org/tools/genome/gbrowse/b_xylophilus/"
	metaMap.image_file = "B_xylophilus.jpg"
	metaMap.image_source = "http://plpnemweb.ucdavis.edu/nemaplex/Taxadata/G145S1.HTM"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "B_xylophilus"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "b_xylophilus.WS234.genomic.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase."
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "b_xylophilus.WS234.annotations_trim.gff3"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "b_xylophilus.WS234.cds_transcripts_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "b_xylophilus.WS234.annotations_trim.gff3"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "b_xylophilus.WS234.protein_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "b_xylophilus.WS234.annotations_trim.gff3"
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
	addAnno("b_xylophilus.WS234.annotations_trim.gff3",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("b_xylophilus.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("b_xylophilus.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("b_xylophilus.WS234.annotations_trim.gff3",annoMap)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////// H. contortus
def H_con(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Haemonchus";
	metaMap.species = "contortus";
	metaMap.gversion = "234"
	metaMap.description = "Haemonchus contortus is an animal endoparasite infecting ruminants worldwide also known as red stomach worm, wire worm or Barber's pole worm, is very common parasite and one the most pathogenic nematodes of ruminants. Adult worms are attached to abomasal mucosa and feed on the blood."
	metaMap.gbrowse = "http://www.wormbase.org/tools/genome/gbrowse/h_contortus/"
	metaMap.image_file = "H_contortus.jpg"
	metaMap.image_source = "Nembase4"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "H_contortus"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "h_contortus.WS234.genomic.fa"
	fileMap.file_version = "234"
	fileMap.description = "The current assembly shown at WormBase is from August 2009 and provided by a collaboration of the Wellcome Trust Sanger Institute and the University of Calgary. It is a combination of capillary and 454 sequence reads assembled into a draft 59707 supercontigs of a total size of 297 975 349 bp. The experimentally determined genome size is ~60 million bp. In addition RNAseq reads were used to determine the structures of 6201 genes using the [http://www.ensembl.org EnsEMBL] pipeline. A first analysis found that for ~50% of them ortholog ''C.elegans'' genes could be determined."
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "h_contortus.WS234.annotations_trim.gff3"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "h_contortus.WS234.cds_transcripts_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "h_contortus.WS234.annotations_trim.gff3"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "h_contortus.WS234.protein_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "h_contortus.WS234.annotations_trim.gff3"
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
	addAnno("h_contortus.WS234.annotations_trim.gff3",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("h_contortus.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("h_contortus.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("h_contortus.WS234.annotations_trim.gff3",annoMap)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////// M. incognita
def M_inc(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Meloidogyne";
	metaMap.species = "incognita";
	metaMap.gversion = "234"
	metaMap.description = "Meloidogyne incognita is an important plant parasite attacking the root of its host plant."
	metaMap.gbrowse = "http://www.wormbase.org/tools/genome/gbrowse/m_incognita/"
	metaMap.image_file = "M_incognita.jpg"
	metaMap.image_source = "http://en.wikipedia.org/wiki/Meloidogyne_incognita"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "M_incognita"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "m_incognita.WS234.genomic.fa"
	fileMap.file_version = "234"
	fileMap.description = "Sex Determination: gonochoristic. Haploid No. chromosomes: 21 (20 autosomes, XY)"
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "m_incognita.WS234.annotations_trim.gff3"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "m_incognita.WS228.cds_transcripts_edit.fa"
	fileMap.file_version = "228"
	fileMap.description = "WormBase"
	fileMap.file_link = "m_incognita.WS234.annotations_trim.gff3"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "m_incognita.WS228.protein_edit.fa"
	fileMap.file_version = "228"
	fileMap.description = "WormBase"
	fileMap.file_link = "m_incognita.WS234.annotations_trim.gff3"
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
	addAnno("m_incognita.WS234.annotations_trim.gff3",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("m_incognita.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("m_incognita.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("m_incognita.WS234.annotations_trim.gff3",annoMap)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////// S. ratti
def S_rat(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Strongyloides";
	metaMap.species = "ratti";
	metaMap.gversion = "234"
	metaMap.description = "Strongyloides ratti is a common gastro-intestinal parasite of the rat. The adult parasites are female only, about 2mm long and live in the mucosa of the small intestine. These parasites produce eggs that pass out of the host in its faeces. In the environment infective larval stages develop either directly or after a facultative sexual free-living adult generation. Infective larvae infect hosts by skin penetration. "
	metaMap.gbrowse = ""
	metaMap.image_file = "Strongyloides_ratti.jpg"
	metaMap.image_source = "http://lemur.amu.edu.pl/share/php/mirnest/browse.php?species=Strongyloides%20ratti"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "S_ratti"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "s_ratti.WS234.genomic.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "s_ratti.WS234.annotations_trim.gff3"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "s_ratti.WS234.cds_transcripts_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "s_ratti.WS234.annotations_trim.gff3"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "s_ratti.WS234.protein_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "s_ratti.WS234.annotations_trim.gff3"
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
	addAnno("s_ratti.WS234.annotations_trim.gff3",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("s_ratti.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("s_ratti.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("s_ratti.WS234.annotations_trim.gff3",annoMap)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////// T. spiralis
def T_spi(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Trichinella";
	metaMap.species = "spiralis";
	metaMap.gversion = "234"
	metaMap.description = "Trichinella spiralis is a gonochoristic species in Clade I."
	metaMap.gbrowse = "http://www.wormbase.org/tools/genome/gbrowse/t_spiralis/"
	metaMap.image_file = "T_spiralis.jpg"
	metaMap.image_source = "www.trichinella.org"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "T_spiralis"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "t_spiralis.WS234.genomic.fa"
	fileMap.file_version = "234"
	fileMap.description = "Sex Determination: gonochoristic. Haploid No. chromosomes: 3 (2 autosomes, XO)"
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "t_spiralis.WS234.annotations_trim.gff3"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "t_spiralis.WS234.cds_transcripts_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "t_spiralis.WS234.annotations_trim.gff3"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "t_spiralis.WS234.protein_edit.fa"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "t_spiralis.WS234.annotations_trim.gff3"
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
	addAnno("t_spiralis.WS234.annotations_trim.gff3",annoMap)
	
	//functional
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "ec.txt"
	annoMap.loaded = false	
	addAnno("t_spiralis.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "go.txt"
	annoMap.loaded = false	
	addAnno("t_spiralis.WS234.annotations_trim.gff3",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "kegg.txt"
	annoMap.loaded = false	
	addAnno("t_spiralis.WS234.annotations_trim.gff3",annoMap)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////// C. elegans
def C_ele(){
	def metaMap = [:]
	def fileMap = [:]
	metaMap.genus = "Caenorhabditis";
	metaMap.species = "elegans";
	metaMap.gversion = "233"
	metaMap.description = "Caenorhabditis elegans is the best-characterized species in the Caenorhabditis genus, or, for that matter, in the nematode phylum of animals. Its evolutionary relationship to other Caenorhabditis species and to all other nematodes is described in WormBook, as is what little is known of its ecology. Although C. elegans belongs to a set of highly similar species (the Elegans group), it has no known sister species (i.e., there exists no known Caenorhabditis species that is more closely related to C. elegans than to C. briggsae et al.)."
	metaMap.gbrowse = "http://www.wormbase.org/tools/genome/gbrowse/c_elegans/"
	metaMap.image_file = "c-elegans-worms.jpg"
	metaMap.image_source = "NIH"
	addMeta(metaMap)
	
	//global
	fileMap.file_dir = "C_elegans"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "c_elegans.WS233.genomic.fa"
	fileMap.file_version = "233"
	fileMap.description = "Sex Determination: hermaphrodite or male. Haploid No. chromosomes: 6 chromosomes (named I, II, III, IV, V and X) and a Mitochondrion."
	fileMap.file_link = "n"
	addFile(fileMap)
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "c_elegans.WS233.annotations_trim.sorted.gff3"
	fileMap.file_version = "234"
	fileMap.description = "WormBase"
	fileMap.file_link = "n"
	addFile(fileMap)
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "c_elegans.WS233.cds_transcripts_edit.fa"
	fileMap.file_version = "233"
	fileMap.description = "WormBase"
	fileMap.file_link = "c_elegans.WS233.annotations_trim.sorted.gff3"
	addFile(fileMap)
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "c_elegans.WS233.protein_edit.fa"
	fileMap.file_version = "233"
	fileMap.description = "WormBase"
	fileMap.file_link = "c_elegans.WS233.annotations_trim.sorted.gff3"
	addFile(fileMap)
}