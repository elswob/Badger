package badger

import groovy.sql.Sql

def grailsApplication
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////Edit the section below ////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

// This just allows each data set to be turned on and off simply by commenting out the function call
//M_meles()
L_rub()
C_tel()
H_rob()

/////// L. rubellus
def L_rub(){
	def metaMap = [:]
	def genomeMap = [:]
	def fileMap = [:]	
	def annoMap = [:]
	
// --- Species Data ---	
	metaMap.genus = "Lumbricus";
	metaMap.species = "rubellus";
	metaMap.description = "Lumbricus rubellus is a common earthworm, found in many temperate ecosystems, used as a model species by researchers investigating the biology and ecology of the soil, and the effects of pollutants and other chemicals on soil organisms."
	metaMap.image_file = "lumbricus_rubellus.jpg"
	metaMap.image_source = "http://www.naturewatch.ca/english/wormwatch/about/key/about_key_lrub.html"
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/fgb2/gbrowse/lumbricus/"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","10/11/2011")
	genomeMap.gversion = "0.4"
	addGenome(genomeMap)
	
// --- File Data --- 	
	// globals (blast,search,download and cov need to be added to each file type if the values differ)
	// blast, search and download can be either public (pub) or private (priv)
	// coverage can be either yes (y) or no (n)
	// file_link is only required for the mRNA and peptide files and needs to match the name of the GFF3 from which they originated
	fileMap.file_dir = "L_rubellus"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	fileMap.file_link = "n"
	
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "L_rubellus_genome_0.4.fa"
	fileMap.file_version = "0.4"
	fileMap.description = "Mixed and merged assembly of Illumina and 454 data."
	addFile(fileMap)
	
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "L_rubellus_0.4.gff"
	fileMap.file_version = "0.4"
	fileMap.description = "Merged gene set using MAKER, Exonerate protein2genome, Augustus and manual gene predictions"
	addFile(fileMap)
	
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "L_rubellus_transcripts_0.4.fa"
	fileMap.file_version = "0.4"
	fileMap.description = "Merged gene set using MAKER, Exonerate protein2genome, Augustus and manual gene predictions"
	fileMap.file_link = "L_rubellus_0.4.gff"
	addFile(fileMap)
	
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "L_rubellus_proteins_0.4.fa"
	fileMap.file_version = "0.4"
	fileMap.description = "Merged gene set using MAKER, Exonerate protein2genome, Augustus and manual gene predictions"
	fileMap.file_link = "L_rubellus_0.4.gff"
	addFile(fileMap)
	
	// --- Annotation data ---	
	// the addAnno function requires two values, the first is the name of the GFF3 file to which the annotations should be assigned
	// the second should remain unchanged
	
	//blast data
	annoMap.type = "blast"				
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "sprot_new_manual.xml"
	annoMap.loaded = false	
	addAnno("L_rubellus_0.4.gff",annoMap)
	
	annoMap.link = "http://www.ncbi.nlm.nih.gov/nucest/"
    annoMap.source = "L. rubellus ESTs"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "lum_est_new_manual.xml"
    annoMap.loaded = false
    addAnno("L_rubellus_0.4.gff",annoMap)
    
    annoMap.link = "http://genome.jgi-psf.org/cgi-bin/dispGeneModel?db=Capca1&id="
    annoMap.source = "C. teleta genes"
	annoMap.regex = "Cap_(\\d+)"
	annoMap.anno_file = "cap_blast.xml"
    annoMap.loaded = false
    addAnno("L_rubellus_0.4.gff",annoMap)
    
    annoMap.link = "http://genome.jgi-psf.org/cgi-bin/dispGeneModel?db=Helro1&id="
    annoMap.source = "H. robusta genes"
	annoMap.regex = "Hel_(\\d+)"
	annoMap.anno_file = "hel_blast.xml"
    annoMap.loaded = false
    addAnno("L_rubellus_0.4.gff",annoMap)
	
	//functional data
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Lrub.ec.2.txt"
	annoMap.loaded = false	
	addAnno("L_rubellus_0.4.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Lrub.go.2.txt"
	annoMap.loaded = false	
	addAnno("L_rubellus_0.4.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "Lrub.kegg.2.txt"
	annoMap.loaded = false	
	addAnno("L_rubellus_0.4.gff",annoMap)
	
	//interproscan data
	annoMap.type = "ipr"
	annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
	annoMap.source = "InterProScan"
	annoMap.regex = "(IPR\\d+).*?"
	annoMap.anno_file = "L_rubellus_proteins_0.4.2.ipr"
	annoMap.loaded = false	
	addAnno("L_rubellus_0.4.gff",annoMap)
}

/////// C. teleta
def C_tel(){
	def metaMap = [:]
	def genomeMap = [:]
	def fileMap = [:]	
	def annoMap = [:]
	
// --- Species Data ---	
	metaMap.genus = "Capitella";
	metaMap.species = "teleta";
	metaMap.description = "Capitella teleta is a polychaete worm and representative of the phylum Annelida. C. teleta was formally described by Blake, Grassle and Eckelbarger (2009) and previously known as Capitella sp. I . Polychaete annelids, also known as the segmented worms, are members of the superphylum Lophotrochozoa, and C. teleta is among the first lophotrophozoans to have its genome sequenced. Capitella is a small benthic marine worm with a cosmopolitan distribution. It has many features characteristic of annelids including a segmented body plan, centralized nervous system, continued adult growth by addition of segments from a posterior growth zone, regenerative abilities, a holoblastic spiral cleavage program, and an indirect life cycle. Capitella is currently being developed as a model for evolutionary developmental studies and is one of the major protostome bio-indicators of disturbed marine habitats. As a representative lophotrochozoan, embryological and molecular genetic studies of Capitella will be pivotal for understanding evolution of a speciose and ecologically important, but understudied group of bilaterian animals. The Capitella genome sequence complements current developmental and environmental research programs and will provide opportunities to understand genome evolution and its role in body plan and life history evolution in the Metazoa."
	metaMap.image_file = "capitella.jpg"
	metaMap.image_source = "http://genome.jgi-psf.org/Capca1/Capca1.home.html"
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = ""
	genomeMap.dateString = Date.parse("dd/MM/yyyy","23/08/2007")
	genomeMap.gversion = "cap1"
	addGenome(genomeMap)
	
// --- File Data --- 	
	// globals (blast,search,download and cov need to be added to each file type if the values differ)
	// blast, search and download can be either public (pub) or private (priv)
	// coverage can be either yes (y) or no (n)
	// file_link is only required for the mRNA and peptide files and needs to match the name of the GFF3 from which they originated
	fileMap.file_dir = "C_teleta"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	fileMap.file_link = "n"
	
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "Capitella_spI.edit.fasta"
	fileMap.file_version = "1.0"
	fileMap.description = "The assembly release version 1.0 of whole genome shotgun reads was constructed with the JGI assembler, Jazz, using paired end sequencing reads at a coverage of ~7.9X. After trimming for vector and quality, 2,335,056 reads assembled into 21,042 main genome scaffolds totaling 333.7 Mb. Roughly half of the genome is contained in 454 scaffolds all at least 188 Kb in length."
	addFile(fileMap)	
	
	//fake gff (very important that the description is fake and the file is just a list of IDs that match the fasta file)
	fileMap.file_type = "Genes"
	fileMap.file_name = "Cap.gff"
	fileMap.file_version = "whatever"
	fileMap.description = "fake"
	fileMap.source = "cap"
	addFile(fileMap)
	
	//external peptide
	fileMap.file_type = "mRNA"
	fileMap.file_name = "C_teleta_edit.fa"
	fileMap.file_version = "1.0"
	fileMap.description = "The current draft release, version 1.0, includes a total of 32,415 gene models predicted and functionally annotated using the JGI annotation pipeline."
	fileMap.file_link = "Cap.gff"
	fileMap.source = "cap"
	addFile(fileMap)
	
	//external peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "C_teleta_edit.aa"
	fileMap.file_version = "1.0"
	fileMap.description = "The current draft release, version 1.0, includes a total of 32,415 gene models predicted and functionally annotated using the JGI annotation pipeline."
	fileMap.file_link = "Cap.gff"
	fileMap.source = "cap"
	addFile(fileMap)
	
	def extMap = [:]
	extMap.ext_id = "cap"
	extMap.link = "http://genome.jgi-psf.org/cgi-bin/dispGeneModel?db=Capca1&id="
	extMap.regex = "Cap_(\\d+)"
	extMap.ext_source = "<a href=\"http://genome.jgi-psf.org/Capca1/Capca1.home.html\" target=\"_blank\">JGI</a>"
	addExt(extMap)
}

/////// H. robusta
def H_rob(){
	def metaMap = [:]
	def genomeMap = [:]
	def fileMap = [:]	
	def annoMap = [:]
	
// --- Species Data ---	
	metaMap.genus = "Helobdella";
	metaMap.species = "robusta";
	metaMap.description = "Leeches are distinguished from other annelids by anterior and posterior suckers used for locomotion and feeding – on blood or soft body parts of other animals.  Helobdella robusta was chosen for whole genome sequencing because of its relatively small genome (~300 Mb) and its use as a model for annelid and lophotrochozoan development.  Hermaphroditic like all clitellate annelids, Helobdella is capable of both cross- and self-fertilization. It is small (1-3 cm in length), and breeds year round in laboratory culture, feeding on small freshwater snails. Roughly synchronous batches up to about ~100 embryos can be easily pucked from the adult and cultured to juvenile stages in simple salt solutions, allowing access to all stages of development."
	metaMap.image_file = "Helro_leech.jpg"
	metaMap.image_source = "http://genome.jgi-psf.org/Helro1/Helro1.home.html"
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = ""
	genomeMap.dateString = Date.parse("dd/MM/yyyy","20/09/2007")
	genomeMap.gversion = "hel1"
	addGenome(genomeMap)
	
// --- File Data --- 	
	// globals (blast,search,download and cov need to be added to each file type if the values differ)
	// blast, search and download can be either public (pub) or private (priv)
	// coverage can be either yes (y) or no (n)
	// file_link is only required for the mRNA and peptide files and needs to match the name of the GFF3 from which they originated
	fileMap.file_dir = "H_robusta"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	fileMap.file_link = "n"
	
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "Helobdella_robusta.edit.fasta"
	fileMap.file_version = "1.0"
	fileMap.description = "The assembly release version 1.0 of whole genome shotgun reads was constructed with the JGI assembler, Jazz, using paired end sequencing reads at a coverage of 7.92x. After trimming for vector and quality, 2,354,463 reads assembled into 1993 scaffolds totaling 235.4 Mbp. Roughly half of the genome is contained in 21 scaffolds all at least 3.1 Mbp in length."
	addFile(fileMap)	
	
	//fake gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "Hel.gff"
	fileMap.file_version = "0.1"
	fileMap.description = "fake"
	fileMap.source = "hel"
	addFile(fileMap)
	
	//External nucs
	fileMap.file_type = "mRNA"
	fileMap.file_name = "H_robusta_edit.fa"
	fileMap.file_version = "1.0"
	fileMap.description = "The current draft release includes a total of 23,432 gene models predicted using the JGI annotation pipeline. This data set is composed of gene models built by homology to known proteins from other model organisms and ab initio gene predictions as well as from available Helobdella robusta EST and cDNA data. Approximately 94.8% of the ESTs/cDNAs mapped to the v.1.0 assembly. Average gene length is 3.9 kb and average transcript length is 1.2 kb, with the average protein containing 376 amino acids. There are approximately 6.12 exons per gene averaging 206 bp each with intron spacing of 526 bp. Gene functions have been automatically assigned based on homology to known genes."
	fileMap.file_link = "Hel.gff"
	fileMap.source = "hel"
	addFile(fileMap)
	
	//External peptides
	fileMap.file_type = "Peptide"
	fileMap.file_name = "H_robusta_edit.aa"
	fileMap.file_version = "1.0"
	fileMap.description = "The current draft release includes a total of 23,432 gene models predicted using the JGI annotation pipeline. This data set is composed of gene models built by homology to known proteins from other model organisms and ab initio gene predictions as well as from available Helobdella robusta EST and cDNA data. Approximately 94.8% of the ESTs/cDNAs mapped to the v.1.0 assembly. Average gene length is 3.9 kb and average transcript length is 1.2 kb, with the average protein containing 376 amino acids. There are approximately 6.12 exons per gene averaging 206 bp each with intron spacing of 526 bp. Gene functions have been automatically assigned based on homology to known genes."
	fileMap.file_link = "Hel.gff"
	fileMap.source = "hel"
	addFile(fileMap)
	
	def extMap = [:]
	extMap.ext_id = "hel"
	extMap.link = "http://genome.jgi-psf.org/cgi-bin/dispGeneModel?db=Helro1&id="
	extMap.regex = "Hel_(\\d+)"	
	extMap.ext_source = "<a href=\"http://genome.jgi-psf.org/Helro1/Helro1.home.html\" target=\"_blank\">JGI</a>"
	addExt(extMap)
}

/////// M. meles
def M_meles(){
	def metaMap = [:]
	def genomeMap = [:]
	def fileMap = [:]	
	def annoMap = [:]
	
// --- Species Data ---	
	metaMap.genus = "Meles";
	metaMap.species = "meles";
	metaMap.description = "The European badger (Meles meles) is a species of badger of the genus Meles, native to almost all of Europe. It is classed as Least Concern for extinction by the IUCN, due to its wide distribution and large population. The European badger is a social, burrowing animal which lives on a wide variety of plant and animal foods. It is very fussy over the cleanliness of its burrow, and defecates in latrines. Cases are known of European badgers burying their dead family members. Although ferocious when provoked, a trait which was once exploited for the blood sport of badger-baiting, the European badger is generally a peaceful animal, having been known to share its burrows with other species such as rabbits, red foxes and raccoon dogs. Although it does not usually prey on domestic stock, the species is nonetheless alleged to damage livestock through spreading bovine tuberculosis."
	metaMap.image_file = "BadgerSilhouette.jpg"
	metaMap.image_source = "FreeVectors.net"
	addMeta(metaMap)
	
// --- Genome Data ---	
	genomeMap.gbrowse = "http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nHd.2.3.1"
	genomeMap.dateString = Date.parse("dd/MM/yyyy","17/01/2012")
	genomeMap.gversion = "M meles 1.0"
	addGenome(genomeMap)
	
// --- File Data --- 	
	// globals (blast,search,download and cov need to be added to each file type if the values differ)
	// blast, search and download can be either public (pub) or private (priv)
	// coverage can be either yes (y) or no (n)
	// file_link is only required for the mRNA and peptide files and needs to match the name of the GFF3 from which they originated
	fileMap.file_dir = "M_meles"
	fileMap.loaded = false
	fileMap.blast = "pub"
	fileMap.search = "pub"
	fileMap.download = "pub"
	fileMap.cov = "n"
	fileMap.file_link = "n"
	
	//genome
	fileMap.file_type = "Genome"
	fileMap.file_name = "M_meles.genome.100.fa"
	fileMap.file_version = "1.0"
	fileMap.description = "The genome of M. meles has not yet been sequenced, this is just an example data set."
	addFile(fileMap)
	
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "M_meles_1.100.gff"
	fileMap.file_version = "1.1"
	fileMap.description = "A GFF3 file generated by some gene prediction software"
	addFile(fileMap)
	
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "M_meles_1.transcripts.100.fa"
	fileMap.file_version = "1.1"
	fileMap.description = "A set of transcripts based on the predicted genes"
	fileMap.file_link = "M_meles_1.100.gff"
	addFile(fileMap)
	
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "M_meles_1.proteins.100.fa"
	fileMap.file_version = "1.1"
	fileMap.description = "A set of proteins based on the predicted genes"
	fileMap.file_link = "M_meles_1.100.gff"
	addFile(fileMap)

// --- Annotation data ---	
	// the addAnno function requires two values, the first is the name of the GFF3 file to which the annotations should be assigned
	// the second should remain unchanged
	
	//blast data
	annoMap.type = "blast"				
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "M_meles_1.blast1.100.xml"
	annoMap.loaded = false	
	addAnno("M_meles_1.100.gff",annoMap)
	
	annoMap.link = "http://www.ncbi.nlm.nih.gov/nucest/"
    annoMap.source = "Tardigrade ESTs"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "M_meles_1.blast2.100.xml"
    annoMap.loaded = false
    addAnno("M_meles_1.100.gff",annoMap)
	
	//functional data
	annoMap.type = "fun"				
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "M_meles_1.ec.100.txt"
	annoMap.loaded = false	
	addAnno("M_meles_1.100.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "M_meles_1.go.100.txt"
	annoMap.loaded = false	
	addAnno("M_meles_1.100.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "M_meles_1.kegg.100.txt"
	annoMap.loaded = false	
	addAnno("M_meles_1.100.gff",annoMap)
	
	//interproscan data
	annoMap.type = "ipr"
	annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
	annoMap.source = "InterProScan"
	annoMap.regex = "(IPR\\d+).*?"
	annoMap.anno_file = "M_meles_1.ipr.100.txt"
	annoMap.loaded = false	
	addAnno("M_meles_1.100.gff",annoMap)
	
	//Second set of gene predictions
	
	// --- Species Data ---	
	// only need genus and species to match previous record
	metaMap.genus = "Meles";
	metaMap.species = "meles";
	addMeta(metaMap)
	
	// --- Genome Data ---	
	//only need genome id to match previous record 
	genomeMap.gversion = "M meles 1.0"
	addGenome(genomeMap)
	
	//gff
	fileMap.file_type = "Genes"
	fileMap.file_name = "M_meles_2.100.gff"
	fileMap.file_version = "1.2"
	fileMap.description = "Another GFF3 file generated by some gene prediction software"
	addFile(fileMap)
	
	//mRNA
	fileMap.file_type = "mRNA"
	fileMap.file_name = "M_meles_2.transcripts.100.fa"
	fileMap.file_version = "1.2"
	fileMap.description = "A set of transcripts based on the predicted genes"
	fileMap.file_link = "M_meles_2.100.gff"
	addFile(fileMap)
	
	//Peptide
	fileMap.file_type = "Peptide"
	fileMap.file_name = "M_meles_2.proteins.100.fa"
	fileMap.file_version = "1.2"
	fileMap.description = "A set of transcripts based on the predicted genes"
	fileMap.file_link = "M_meles_2.100.gff"
	addFile(fileMap)

// // --- Annotation files and data ---
	annoMap = [:]
	//blast
	annoMap.type = "blast"
	annoMap.link = "http://www.ncbi.nlm.nih.gov/protein/"
	annoMap.source = "SwissProt"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "M_meles_2.blast1.100.xml"
	annoMap.loaded = false
	addAnno("M_meles_2.100.gff",annoMap)
	
	annoMap.link = "http://www.ncbi.nlm.nih.gov/nucest/"
	annoMap.source = "Tardigrade ESTs"
	annoMap.regex = "gi\\|(\\d+)\\|.*"
	annoMap.anno_file = "M_meles_2.blast2.100.xml"
	annoMap.loaded = false
	addAnno("M_meles_2.100.gff",annoMap)

	//functional
	annoMap.type = "fun"
	annoMap.link = "http://enzyme.expasy.org/EC/"
	annoMap.source = "Annot8r EC"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "M_meles_2.ec.100.txt"
	annoMap.loaded = false
	addAnno("M_meles_2.100.gff",annoMap)
	
	annoMap.link = "http://www.ebi.ac.uk/QuickGO/GTerm?id="
	annoMap.source = "Annot8r GO"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "M_meles_2.go.100.txt"
	annoMap.loaded = false
	addAnno("M_meles_2.100.gff",annoMap)
	
	annoMap.link = "http://www.genome.jp/dbget-bin/www_bget?ko:"
	annoMap.source = "Annot8r KEGG"
	annoMap.regex = "(.*)"
	annoMap.anno_file = "M_meles_2.kegg.100.txt"
	annoMap.loaded = false
	addAnno("M_meles_2.100.gff",annoMap)
	
	//interproscan
	annoMap.type = "ipr"
	annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
	annoMap.source = "InterProScan"                              
	annoMap.regex = "(IPR\\d+).*?"
	annoMap.anno_file = "M_meles_2.ipr.100.txt"
	annoMap.loaded = false
	addAnno("M_meles_2.100.gff",annoMap)
	
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
def addExt(extMap){
	println "Checking external data"
	//def check = ExtInfo.findByExt_id(extMap.ext_id)
	def check = FileData.findByFile_name(extMap.ext_id)
	if (check){
		println "External file ID "+extMap.ext_id+" already exists - "+check
	}else{
		println "Adding new external info "+extMap
		ExtInfo ext = new ExtInfo(extMap)
		ext.save(flush:true)
	}
}
	
