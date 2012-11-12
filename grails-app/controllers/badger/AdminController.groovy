package badger
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql
import grails.plugin.cache.Cacheable

class AdminController {
	def grailsApplication
	javax.sql.DataSource dataSource
	
	//news
	
	@Secured(['ROLE_ADMIN'])
	 def addNews = {
	 }
	 @Secured(['ROLE_ADMIN'])
	 def addedNews = {
		 def dataMap = [:]
		 def data
		 def title
		 Date date
		 if (params.newsTitle){
			title = params.newsTitle
		 }else{
			 title = null
		 }
		 if (params.newsData){
			data = params.newsData
		 }else{
			 data = null
		 }
		 if (params.newsDate){
			 def matcher
			 //check the date format is ok
			 if ((matcher = params.newsDate =~ /^\d{2}\/\d{2}\/\d{4}/)){
				 date = Date.parse("dd/MM/yyyy",params.newsDate)
			 }else{
				 date = null
			 }
		 }else{	 
			date = new Date()
		 }
		 dataMap.dataString = data
		 dataMap.titleString = title
		 dataMap.dateString = date
		 println dataMap
		 new News(dataMap).save()
		 return [newsTitle: title, newsData: data, newsDate: date]
	 }
	 @Secured(['ROLE_ADMIN'])
	 def editNews = {
		 println "Editing "+params.titleString
		 def newsData = News.findAllByTitleString(params.titleString)
		 return [newsData: newsData] 
	 }
	 @Secured(['ROLE_ADMIN'])
	 def editedNews = {
		 //delete old entry
		 def newsData = News.findAllByTitleString(params.newsTitle)
		 def delData = News.get(newsData.id[0])
		 delData.delete(flush: true)
		 
		 def dataMap = [:]
		 def data
		 def title
		 Date date
		 if (params.newsTitle){
			title = params.newsTitle
		 }else{
			 title = null
		 }
		 if (params.newsData){
			data = params.newsData
		 }else{
			 data = null
		 }
		 if (params.newsDate){
			 def matcher
			 //check the date format is ok
			 if ((matcher = params.newsDate =~ /^\d{2}\/\d{2}\/\d{4}/)){
				 date = Date.parse("dd/MM/yyyy",params.newsDate)
			 }else{
				 date = null
			 }
		 }else{	 
			date = new Date()
		 }
		 dataMap.dataString = data
		 dataMap.titleString = title
		 dataMap.dateString = date
		 println dataMap
		 new News(dataMap).save()
		 return [newsTitle: title, newsData: data, newsDate: date]
	 }
	 @Secured(['ROLE_ADMIN'])
	 def deleteNews = {
		 println "Deleting "+params.titleString
		 def newsData = News.findAllByTitleString(params.titleString)
		 return [newsData: newsData] 
	 }
	 @Secured(['ROLE_ADMIN'])
	 def deletedNews = {
		 def newsData = News.findAllByTitleString(params.newsTitle)
		 def delData = News.get(newsData.id[0])
		 delData.delete(flush: true)
		 println "Deleted "+params.newsTitle
	 }
 
 //species
	
	@Secured(['ROLE_ADMIN'])
	def home = {
		def metaData = MetaData.findAll()
		return [metaData: metaData]	 
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedData = {
		def dataMap = [:]		

		//check if name and version are unique
		def check = MetaData.findByGenusAndSpecies(params.genus.trim(), params.species.trim())
		if (check){
			println "species already exists - "+check
			return [error: "duplicate"]	
		}else{  		
			dataMap.genus = params.genus.trim()
			dataMap.species = params.species.trim()
			dataMap.description = params.description.trim()
			if (params.image_f){
				dataMap.image_file = params.image_f.trim()
				dataMap.image_source = params.image_s.trim()
			}else{
				dataMap.image_file = "acrobelesrainbow.gif"
				dataMap.image_source = "nematodes.org"
			}
			dataMap.gbrowse = params.gbrowse.trim()
			dataMap.Gversion = params.version.trim()
			println dataMap
			MetaData meta = new MetaData(dataMap)
			meta.save()
			
			def fileMap = [:]
			fileMap.loaded = false
			int genomeID
			int geneID
			if (params.trans){
				fileMap.file_type = "Transcriptome"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.trans.trim()
				fileMap.blast = params.blast_trans
				fileMap.search = params.search_trans
				fileMap.download = params.down_trans
				fileMap.file_version = params.trans_v.trim()
				fileMap.description = params.trans_d.trim()
				fileMap.cov = params.trans_c.trim()
				fileMap.file_link = "n"
				if (new File("data/"+params.dir.trim()+"/"+params.trans.trim()).exists()){
					println fileMap
					FileData file = new FileData(fileMap) 
					meta.addToFiles(file)
					file.save()
				}else{
					println "file does not exist!"
					return [error: "no file", file: "data/"+params.dir+"/"+params.trans]
				}
				
			}
			if (params.genome){
				fileMap.file_type = "Genome"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.genome.trim()	
				fileMap.blast = params.blast_genome
				fileMap.search = params.search_genome
				fileMap.download = params.down_genome
				fileMap.file_version = params.genome_v.trim()
				fileMap.description = params.genome_d.trim()
				fileMap.cov = params.genome_c.trim()
				fileMap.file_link = "n"
				if (new File("data/"+params.dir.trim()+"/"+params.genome.trim()).exists()){
					println fileMap
					FileData file = new FileData(fileMap) 
					meta.addToFiles(file)
					file.save()
				}else{
					println "file does not exist!"
					return [error: "no file", file: "data/"+params.dir+"/"+params.genome]
				}
			}
			if (params.genes){
				fileMap.file_type = "Genes"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.genes.trim()
				fileMap.search = params.search_genes
				fileMap.download = params.down_genes
				fileMap.file_version = params.genes_v.trim()
				fileMap.description = params.genes_d.trim()
				fileMap.file_link = params.genome.trim()
				if (new File("data/"+params.dir.trim()+"/"+params.genes.trim()).exists()){
					println fileMap
					FileData file = new FileData(fileMap) 
					meta.addToFiles(file)
					file.save()
				}else{
					println "file does not exist!"
					return [error: "no file", file: "data/"+params.dir+"/"+params.genes]
				}
			}
			if (params.mrna_trans){
				fileMap.file_type = "mRNA"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.mrna_trans.trim()
				fileMap.blast = params.blast_mrna
				fileMap.download = params.down_mrna
				fileMap.file_version = params.mrna_trans_v.trim()
				fileMap.description = params.mrna_trans_d.trim()
				fileMap.file_link = params.genes.trim()
				if (new File("data/"+params.dir.trim()+"/"+params.mrna_trans.trim()).exists()){
					println fileMap
					FileData file = new FileData(fileMap) 
					meta.addToFiles(file)
					file.save()
				}else{
					println "file does not exist!"
					return [error: "no file", file: "data/"+params.dir+"/"+params.mrna_trans]
				}
			}
			if (params.mrna_pep){
				fileMap.file_type = "Peptide"
				fileMap.file_dir = params.dir.trim()
				fileMap.file_name = params.mrna_pep.trim()
				fileMap.blast = params.blast_pep
				fileMap.download = params.down_pep
				fileMap.file_version = params.mrna_pep_v.trim()
				fileMap.description = params.mrna_pep_d.trim()
				fileMap.file_link = params.genes.trim()
				if (new File("data/"+params.dir.trim()+"/"+params.mrna_pep.trim()).exists()){
					println fileMap
					FileData file = new FileData(fileMap) 
					meta.addToFiles(file)
					file.save()
				}else{
					println "file does not exist!"
					return [error: "no file", file: "data/"+params.dir+"/"+params.mrna_pep]
				}
			}			
			return [dataMap:dataMap, Gid: meta.id]
		}
	}
	
	@Secured(['ROLE_ADMIN'])
	def addAnno = {
		def sql = new Sql(dataSource)
		//def dataSetsSql = "select meta_data.data_id,genus,species,file_name, file_version, file_type, file_id from meta_data, file_data where meta_data.data_id = file_data.data_id and (file_type = 'Genes' or file_type = 'Transcriptome') ;";
		//def dataSets = sql.rows(dataSetsSql)
		def dataSets = MetaData.findAllById(params.Gid)
		//print dataSets
		return [dataSets:dataSets]
	}
	
	@Secured(['ROLE_ADMIN'])
	def addedAnno = {
		FileData file = FileData.findByFile_name(params.dataSelect)
		def filedir = FileData.findByFile_name(params.dataSelect).file_dir
		def annoMap = [:]			
		if (params.annoSelect == "1"){
			def check = FileData.findByFile_name(params.dataSelect).anno.anno_file			
			println "check = "+check
			if (params.b_anno_file.trim() in check){
				println "check exists : "+check
				return [error: "duplicate"]	
			}else{
				annoMap.type = "blast"				
				annoMap.link = params.b_link.trim()
				annoMap.source = params.b_source.trim()
				annoMap.regex = params.b_regex.trim()
				annoMap.anno_file = params.b_anno_file.trim()
				annoMap.loaded = false	
				if (new File("data/"+filedir+"/"+params.b_anno_file.trim()).exists()){				
					println annoMap
					AnnoData anno = new AnnoData(annoMap)
					file.addToAnno(anno)
					anno.save()
					return [annoMap: annoMap, file:file]
				}else{
					return [error: "no file", fileLoc: "data/"+filedir+"/"+params.b_anno_file, file:file]
				}
			}
		}else if (params.annoSelect == "2"){
			def check = FileData.findByFile_name(params.dataSelect).anno.anno_file
			println "check = "+check
			if (params.f_anno_file.trim() in check){
				println "check exists : "+check
				return [error: "duplicate"]	
			}else{
				annoMap.type = "fun"
				annoMap.link = params.f_link.trim()
				annoMap.source = params.f_source.trim()
				annoMap.regex = params.f_regex.trim()	
				annoMap.anno_file = params.f_anno_file.trim()
				annoMap.loaded = false
				if (new File("data/"+filedir+"/"+params.f_anno_file.trim()).exists()){		
					println annoMap
					AnnoData anno = new AnnoData(annoMap)
					file.addToAnno(anno)
					anno.save()
					return [annoMap: annoMap, file:file]
				}else{
					return [error: "no file", fileLoc: "data/"+filedir+"/"+params.f_anno_file, file:file]
				}
			}
		}else if (params.annoSelect == "3"){
			def check = FileData.findByFile_name(params.dataSelect).anno.anno_file
			println "check = "+check
			if (params.i_anno_file.trim() in check){
				println "check exists : "+check
				return [error: "duplicate"]	
			}else{
				annoMap.type = "ipr"
				annoMap.link = "http://www.ebi.ac.uk/interpro/IEntry?ac="
				annoMap.regex = "(IPR\\d+).*?"
				annoMap.source = "InteProScan"
				annoMap.anno_file = params.i_anno_file.trim()
				annoMap.loaded = false	
				if (new File("data/"+filedir+"/"+params.i_anno_file.trim()).exists()){
					println annoMap
					AnnoData anno = new AnnoData(annoMap)
					file.addToAnno(anno)
					anno.save()
					return [annoMap: annoMap, file:file]
				}else{
					return [error: "no file", fileLoc: "data/"+filedir+"/"+params.i_anno_file, file:file]
				}
			}
		}
	}
	
	@Secured(['ROLE_ADMIN'])
	def editAnno = {
		def annoData = AnnoData.findById(params.id)
		return [annoData: annoData]	 
	}
	
	@Secured(['ROLE_ADMIN'])
	def editedAnno = {
		def sql = new Sql(dataSource)
		def upsql 
		if (params.b_anno_file){
			upsql = "update anno_data set anno_file = '"+params.b_anno_file.trim()+"', source = '"+params.b_source.trim()+"', link = '"+params.b_link.trim()+"', regex = '"+params.b_regex.trim()+"' where id = '"+params.id+"';";		
		}else if (params.f_anno_file){
			upsql = "update anno_data set anno_file = '"+params.f_anno_file.trim()+"', source = '"+params.f_source.trim()+"', link = '"+params.f_link.trim()+"', regex = '"+params.f_regex.trim()+"' where id = '"+params.id+"';";
		}else if (params.i_anno_file){
			upsql = "update anno_data set anno_file = '"+params.i_anno_file.trim()+"' where id = '"+params.id+"';";
		}
		
		println "upsql = "+upsql
		def update = sql.execute(upsql)
	}
	
	@Secured(['ROLE_ADMIN'])
	def deleteAnno = {
		def annoData = AnnoData.findById(params.id)
		return [annoData: annoData]	
	}
	@Secured(['ROLE_ADMIN'])
	def deletedAnno = {
		def annoData = AnnoData.findById(params.id)
		def source = annoData.source
		def file = annoData.anno_file
		println "Deleting "+source+" "+file		
		//runAsync {
	 		annoData.delete()
	 	//}
	 	return [source:source, file:file]
	 	
	}
	
	@Secured(['ROLE_ADMIN'])
	def editData = {
		def metaData = MetaData.findById(params.id)
		return [metaData: metaData]	 
	}
	
	@Secured(['ROLE_ADMIN'])
	def editedData = {
		def sql = new Sql(dataSource)
		def upsql = "update meta_data set genus = '"+params.genus.trim()+"', species = '"+params.species.trim()+"', description = '"+params.description.trim()+"', image_file = '"+params.image_f.trim()+"', image_source = '"+params.image_s.trim()+"', gbrowse = '"+params.gbrowse.trim()+"' where id = '"+params.id+"';";
		println "upsql = "+upsql
		def update = sql.execute(upsql)
	}
	
	@Secured(['ROLE_ADMIN'])
	def deleteData = {
		def metaData = MetaData.findById(params.id)
		return [metaData: metaData]	
	}
	@Secured(['ROLE_ADMIN'])
	def deletedData = {
		def metaData = MetaData.findById(params.id)
		def genus = metaData.genus
		def species = metaData.species
		println "Deleting "+genus+" "+species		
		//runAsync {
	 		metaData.delete()
	 	//}
	 	return [genus:genus, species:species]
	 	
	}
	@Secured(['ROLE_ADMIN'])
	def editFile = {
		def fileData = FileData.findById(params.id)
		return [fileData: fileData]	 
	}
	@Secured(['ROLE_ADMIN'])
	def editedFile = {
		def sql = new Sql(dataSource)
		def upsql
		if (params.trans){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.trans.trim()+"', blast = '"+params.blast_trans+"', search = '"+params.search_trans+"', download = '"+params.down_trans+"', file_version = '"+params.trans_v.trim()+"', description = '"+params.trans_d.trim()+"', cov = '"+params.trans_c.trim()+"' where id = '"+params.id+"';";
		}else if (params.genome){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.genome.trim()+"', blast = '"+params.blast_genome+"', search = '"+params.search_genome+"', download = '"+params.down_genome+"', file_version = '"+params.genome_v.trim()+"', description = '"+params.genome_d.trim()+"', cov = '"+params.genome_c.trim()+"' where id = '"+params.id+"';";
		}else if (params.genes){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.genes.trim()+"', search = '"+params.search_genes+"', download = '"+params.down_genes+"', file_version = '"+params.genes_v.trim()+"', description = '"+params.genes_d.trim()+"' where id = '"+params.id+"';";
		}else if (params.mrna_trans){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.mrna_trans.trim()+"', blast = '"+params.blast_mrna+"', download = '"+params.down_mrna+"', file_version = '"+params.mrna_trans_v.trim()+"', description = '"+params.mrna_trans_d.trim()+"' where id = '"+params.id+"';";		
		}else if (params.mrna_pep){
			upsql = "update file_data set file_dir = '"+params.dir.trim()+"', file_name = '"+params.mrna_pep.trim()+"', blast = '"+params.blast_pep+"', download = '"+params.down_pep+"', file_version = '"+params.mrna_pep_v.trim()+"', description = '"+params.mrna_pep_d.trim()+"' where id = '"+params.id+"';";				
		}
		println "upsql = "+upsql
		def update = sql.execute(upsql)
	}
	@Secured(['ROLE_ADMIN'])
	def deleteFile = {
		def fileData = FileData.findById(params.id)
		return [fileData: fileData]	 	
	}
	@Secured(['ROLE_ADMIN'])
	def deletedFile = {
		def fileData = FileData.findById(params.id)
		def dir = fileData.file_dir
		def name = fileData.file_name
		println "Deleting "+dir+"/"+name
		//runAsync {
	 		fileData.delete()
	 	//}
	 	return [dir:dir,name:name]
	}
}
