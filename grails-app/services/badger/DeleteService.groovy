package badger

import groovy.sql.Sql

class DeleteService {
	javax.sql.DataSource dataSource
	def grailsApplication
	
	def deleteAnno(id){
		def sql = new Sql(dataSource)
		def anno = AnnoData.findById(id)
		def source = anno.source
		def file = anno.anno_file
		def gff = anno.filedata
		def asql
		if (anno.type == 'blast'){
			asql = "delete from gene_blast where id in (select gene_blast.id from gene_blast,gene_info,anno_data where gene_blast.gene_id = gene_info.id and gene_info.file_id = anno_data.filedata_id and anno_data.id = "+id+" and gene_blast.anno_db = anno_data.source);";		
		}else if (anno.type == 'fun'){
			asql = "delete from gene_anno where id in (select gene_anno.id from gene_anno,gene_info,anno_data where gene_anno.gene_id = gene_info.id and gene_info.file_id = anno_data.filedata_id and anno_data.id = "+id+" and gene_anno.anno_db = anno_data.source);";		
		}else if (anno.type == 'ipr'){
			asql = "delete from gene_interpro where id in (select gene_interpro.id from gene_interpro,gene_info,anno_data where gene_interpro.gene_id = gene_info.id and gene_info.file_id = anno_data.filedata_id and anno_data.id = "+id+" and gene_interpro.anno_db = anno_data.source);";		
		}
		println asql
		sql.execute(asql)
		def fsql = "delete from anno_data where id = "+id+";";
		println fsql
		sql.execute(fsql)
		return [source:source, file:file, gff:gff]
	}
	def deleteFile(id){
		def sql = new Sql(dataSource)
		def fileData = FileData.findById(id)
		println "fileData for "+id+" = "+fileData
 		def genome = fileData.genome
 		def dir = fileData.file_dir
 		def name = fileData.file_name
 		println "Deleting "+dir+"/"+name
		//if gene file delete annotations, exons and genes	
		if (fileData.file_type == 'Genes'){	
			def bsql = "delete from gene_blast where gene_id in (select gene_info.id from gene_info,file_data where file_id = file_data.id and file_data.id = "+id+");";
			println "Deleting BLASTs..."
			println bsql
			sql.execute(bsql)
			
			def asql = "delete from gene_anno where gene_id in (select gene_info.id from gene_info,file_data where file_id = file_data.id and file_data.id = "+id+");";
			println "Deleting functional annotations..."
			println asql
			sql.execute(asql)
			
			def isql = "delete from gene_interpro where gene_id in (select gene_info.id from gene_info,file_data where file_id = file_data.id and file_data.id = "+id+");";
			println "Deleting interpro data..."
			println isql
			sql.execute(isql)
			
			def esql = "delete from exon_info where gene_id in (select gene_info.id from gene_info,file_data where gene_info.file_id = "+id+");";
			println "Deleting exon data..."
			println esql
			sql.execute(esql)
			
			def osql = "delete from ortho";
			println "Deleting ortholog data..."
			println osql
			sql.execute(osql)
			
			def gsql = "delete from gene_info where file_id = "+id+";";
			println "Deleting gene data..."
			println gsql
			sql.execute(gsql)
			
			def annosql = "delete from anno_data where filedata_id = "+id+";";
			println "Deleting annotation file data..."
			println annosql
			sql.execute(annosql)
			
			def lsql = "delete from file_data where file_link = '"+fileData.file_name+"';";
			println "Deleting fasta files..."
			println lsql
			sql.execute(lsql)
			
			def fsql = "delete from file_data where id = "+id+";";
			println "Deleting gene file data..."
			println fsql
			sql.execute(fsql)
			
		}
		//if genome file 
		if (fileData.file_type == 'Genome'){
			def gsql = "delete from genome_info where file_id = "+id+";";
			println "Deleting genome data..."
			println gsql
			sql.execute(gsql)
			
			def fsql = "delete from file_data where id = "+id+";";
			println "Deleting gene file data..."
			println fsql
			sql.execute(fsql)
		}
		return [dir:dir, name:name, genome:genome]
	}
	def deleteGenome(id){
		def sql = new Sql(dataSource)
		def genomeData = GenomeData.findById(id)
		def fileData = genomeData.files
		def genomeV = genomeData.gversion
		println "Deleting genome file data for "+genomeV+"..."
		fileData.each{
			//only run with these two as Genes will delete the fasta files
			if (it.file_type == 'Genome' || it.file_type == 'Genes'){
				println "running deleteFile with "+it.id
				deleteFile(it.id)
			}
		}
		def gsql = "delete from genome_data where id = "+id+";";
		println gsql
		sql.execute(gsql)
		return [genomeV: genomeV]
	}
	def deleteSpecies(id){	
		def sql = new Sql(dataSource)		 	
		def metaData = MetaData.findById(id)
		def genomeData = metaData.genome
		def genus = metaData.genus
		def species = metaData.species
		println "Deleting "+genus+" "+species
		//runAsync {
			genomeData.each{
				deleteGenome(it.id)		
			}
	 	//}
	 	def psql = "delete from publication where meta_id = "+id+";";
	 	println "deleting publications for "+genus+" "+species+"...";
	 	println psql
	 	sql.execute(psql)
	 	def ssql = "delete from meta_data where id = "+id+";";
	 	println ssql
	 	sql.execute(ssql)
	 	return [genus:genus, species:species]
	 }		
}
