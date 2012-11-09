package badger

import groovy.sql.Sql

def grailsApplication
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)



def metaMap = [:]
def fileMap = [:]

//MetaData 
def genus,species,gversion,description,gbrowse,image_file,image_source
//FileData
def blast,cov,download,file_dir,file_link,file_name,file_type,file_version,loaded,search

MetaData meta
def addMeta(metaMap){
	meta = new MetaData(metaMap)
	meta.save(flush:true)
}
def addFile(fileMap){
	FileData file = new FileData(fileMap) 
	meta.addToFiles(file)
}

///////// test data
metaMap.genus = "Test";
metaMap.species = "test";
metaMap.gversion = "1.0.1"
metaMap.description = "Test data set"
metaMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/"
metaMap.image_file = "a_viteae_lifecycle.jpg"
metaMap.image_source = "A. viteae lifecycle; from http://www.uni-giessen.de"
//addMeta(metaMap)

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
//addFile(fileMap)
//gff
fileMap.file_type = "Genes"
fileMap.file_name = "test.gff"
fileMap.file_version = "1.0"
fileMap.description = "Test gff"
fileMap.file_link = "n"
//addFile(fileMap)
//mRNA
fileMap.file_type = "mRNA"
fileMap.file_name = "nAv.1.0.1.aug.transcripts.fasta"
fileMap.file_version = "1.0"
fileMap.description = "Test mRNA"
fileMap.file_link = "test.gff"
//addFile(fileMap)
//Peptide
fileMap.file_type = "Peptide"
fileMap.file_name = "nAv.1.0.1.aug.proteins.fasta"
fileMap.file_version = "1.0"
fileMap.description = "Test peptide"
fileMap.file_link = "test.gff"
//addFile(fileMap)

/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// A. viteae
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

/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// L.sigmodontis
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

/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// D.immitis
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

/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// O. ochengi
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

/////////////////////////////////////////////////////////////////////////////////////////////////////

/////// B. malayi
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
fileMap.file_name = "b_malayi.WS234.annotations.gff3"
fileMap.file_version = "234"
fileMap.description = "WormBase"
fileMap.file_link = "n"
addFile(fileMap)
//mRNA
fileMap.file_type = "mRNA"
fileMap.file_name = "b_malayi.WS234.cds_transcripts.fa"
fileMap.file_version = "234"
fileMap.description = "WormBase"
fileMap.file_link = "b_malayi.WS234.annotations.gff3"
addFile(fileMap)
//Peptide
fileMap.file_type = "Peptide"
fileMap.file_name = "b_malayi.WS234.protein.fa"
fileMap.file_version = "234"
fileMap.description = "WormBase"
fileMap.file_link = "b_malayi.WS234.annotations.gff3"
addFile(fileMap)