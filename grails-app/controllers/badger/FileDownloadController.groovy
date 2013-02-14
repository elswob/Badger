package badger
import groovy.sql.Sql

class FileDownloadController {
	javax.sql.DataSource dataSource

    def index() { }
    def blast_download = {
        def fileOut = new File(params.blastfileId + '.out').text
        response.setHeader "Content-disposition", "attachment; filename="+params.fileName
        response.contentType = 'text/csv'
        response.outputStream << fileOut
        response.outputStream.flush()
    }
    def align_download = {
    	def fileOut
    	if (params.type == 'aln'){
        	fileOut = new File(params.fileId + '.aln').text
        }else if (params.type == 'html'){
        	fileOut = new File(params.fileId + '.html').text
        }
        response.setHeader "Content-disposition", "attachment; filename="+params.fileName
        response.contentType = 'text/csv'
        response.outputStream << fileOut
        response.outputStream.flush()
    }
    
    def contig_download = {
        def fileOut = new File(params.fileId)
        response.setHeader "Content-disposition", "attachment; filename="+params.fileName
        response.contentType = 'text/csv'
        response.outputStream << fileOut
        response.outputStream.flush()
    }
    
    def trans_contig_download = {
     	 def object_array = params.fileId
     	 object_array = object_array.replaceAll(/\[/, '')
     	 object_array = object_array.replaceAll(/\]/, '')
     	 object_array = object_array.replaceAll(/ /, '')
     	 def object_list = [] 
     	 object_array.split(",").each{
     	 	 object_list.add(it)
     	 }
     	 println "object_list = "+object_list
     	 def results = TransInfo.findAllByContig_idInList(object_list)
		 def file_builder=""
     	 results.each {
     	 	println "contig_id = "+it.contig_id
     	 	file_builder = file_builder + ">"+it.contig_id+"\n"+it.sequence+"\n"
		 }
		 println "created download file "+params.fileName+".fna"
     	 response.setHeader "Content-disposition", "attachment; filename="+params.fileName+".fna"
         response.contentType = 'text/csv'
         response.outputStream << file_builder
         response.outputStream.flush()
    }
    
    def exon_download = {
    	def sql = new Sql(dataSource)
    	def m_id = params.fileName
    	def getExonSql = "select exon_info.* from exon_info,gene_info where exon_info.gene_id = gene_info.id and gene_info.mrna_id = '"+m_id+"' order by exon_number;";
    	println getExonSql
    	def getExon = sql.rows(getExonSql)
    	def file_builder=""
     	getExon.each {
     	 	println "number = "+it.exon_number
     	 	file_builder = file_builder + ">"+it.exon_number+"\n"+it.sequence+"\n"
		}
		def name = m_id.replaceAll(' ','_')
		println "created download file "+name+".exons.fna"
     	response.setHeader "Content-disposition", "attachment; filename="+name+".exons.fna"
        response.contentType = 'text/csv'
        response.outputStream << file_builder
        response.outputStream.flush()
    }
    
	def gene_download = {
		def object_array
		if (params.seq == 'Peptides'){
     		object_array = params.pepFileId
     	}else if (params.seq == 'Nucleotides'){
     		object_array = params.nucFileId
     	}else if (params.seq == 'OrthoPeptides'){
     		object_array = params.orthoPepFileId
     	}else if (params.seq == 'OrthoNucleotides'){
     		object_array = params.orthoNucFileId
     	}
     	 object_array = object_array.replaceAll(/\[/, '')
     	 object_array = object_array.replaceAll(/\]/, '')
     	 object_array = object_array.replaceAll(/ /, '')
     	 def object_list = [] 
     	 object_array.split(",").each{
     	 	 object_list.add(it)
     	 }
     	 println "object_array = "+object_array
     	 println "object_list = "+object_list
     	 println "size = "+object_list.size()
     	 def results
     	 if (object_list.size() == 1){
     	 	results = GeneInfo.findByMrna_id(object_array)
     	 }else{
     	 	results = GeneInfo.findAllByMrna_idInList(object_list)
		 }
		 def pep_file_builder=""
		 def nuc_file_builder=""
     	 results.each {
     	 	println "gene_id = "+it.mrna_id
     	 	pep_file_builder = pep_file_builder + ">"+it.mrna_id+"\n"+it.pep+"\n"
     	 	nuc_file_builder = nuc_file_builder + ">"+it.mrna_id+"\n"+it.nuc+"\n"
		 }
		 println "seq = "+params.seq
		 def name = params.fileName.replaceAll(' ','_')
		 if (params.seq == 'Peptides' || params.seq == 'OrthoPeptides'){
		 	println "created download file "+name+".aa"
     	 	response.setHeader "Content-disposition", "attachment; filename="+name+".aa"
         	response.contentType = 'text/csv'
         	response.outputStream << pep_file_builder
         	response.outputStream.flush()
         }else{
         	 println "created download files "+name+".fna"
	    	 response.setHeader "Content-disposition", "attachment; filename="+name+".fna"
    	     response.contentType = 'text/csv'
        	 response.outputStream << nuc_file_builder
         	 response.outputStream.flush()
         }
    }
    
    def genome_contig_download = {
     	 def object_array = params.fileId
     	 object_array = object_array.replaceAll(/\[/, '')
     	 object_array = object_array.replaceAll(/\]/, '')
     	 object_array = object_array.replaceAll(/ /, '')
     	 def object_list = [] 
     	 object_array.split(",").each{
     	 	 object_list.add(it)
     	 }
     	 println "object_list = "+object_list
     	 def results = GenomeInfo.findAllByContig_idInList(object_list)
     	 //def results = Contig.findAllByContig_idInList(object_list)
		 def file_builder=""
     	 results.each {
     	 	println "contig_id = "+it.contig_id
     	 	file_builder = file_builder + ">"+it.contig_id+"\n"+it.sequence+"\n"
		 }
		 println "created download file "+params.fileName+".fna"
     	 response.setHeader "Content-disposition", "attachment; filename="+params.fileName+".fna"
         response.contentType = 'text/csv'
         response.outputStream << file_builder
         response.outputStream.flush()
    }
    
    def blast_contig_download = {
    	 def table = params.dataSource
    	 println "table = "+table
    	 println "blastfile = "+params.blastfileId
     	 def object_array = params.fileId
     	 object_array = object_array.replaceAll(/\[/, '')
     	 object_array = object_array.replaceAll(/\]/, '')
     	 object_array = object_array.replaceAll(/ /, '')
     	 def object_list = [] 
     	 object_array.split(",").each{
     	 	 object_list.add(it)
     	 }
     	 //remove redundancy caused by tblastx or whatever
     	 object_list.unique()
     	 println "object_list = "+object_list
     	 def results
     	 if (table == 'Genes'){ println "Getting gene seqs"; results = GeneInfo.findAllByMrna_idInList(object_list)}
     	 if (table == 'Genome'){ println "Getting genome seqs"; results = GenomeInfo.findAllByContig_idInList(object_list)}
     	 if (table == 'Transcriptome'){ println "Getting transcriptome seqs"; results = TransInfo.findAllByContig_idInList(object_list)}
     	 //def results = Contig.findAllByContig_idInList(object_list)
     	 println "results = "+results
		 def file_builder=""
     	 results.each {
     	 	println "id = "+it.id
     	 	if (table == 'Genes'){
     	 		file_builder = file_builder + ">"+it.mrna_id+"\n"+it.pep+"\n"
     	 	}else{
 	    	 	file_builder = file_builder + ">"+it.contig_id+"\n"+it.sequence+"\n"
 	    	 }
		 }
		 //get the blast sequence
		 def blastIn = new File(params.blastfileId).text
		 file_builder = file_builder + blastIn
		 def downloadName = params.fileName.trim()
		 println "created download file "+downloadName+".fna"
     	 response.setHeader "Content-disposition", "attachment; filename="+downloadName+".sequences.fna"
         response.contentType = 'text/csv'
         response.outputStream << file_builder
         response.outputStream.flush()
    }
    
    def zip_download = {
    	def f = FileData.findByFile_name(params.fileName)
    	if (new File("data/"+f.file_dir+"/"+f.file_name).exists()){
    		def fileOut = new File("data/"+f.file_dir+"/"+f.file_name+".zip")
			response.setHeader "Content-disposition", "attachment; filename="+f.file_name+".zip"
			response.contentType = 'application/zip'
			response.outputStream << fileOut.newInputStream()
			response.outputStream.flush()
		}else{
			println "data/"+params.fileName+" does not exists"
		}
    }
    def zip_anno_download = {
    	def f = FileData.findByFile_name(params.fileName)
    	if (new File("data/"+f.file_dir+"/"+f.file_name+".anno.tsv.zip").exists()){
    		def fileOut = new File("data/"+f.file_dir+"/"+f.file_name+".anno.tsv.zip")
    		println "fileOut = "+fileOut
			response.setHeader "Content-disposition", "attachment; filename="+f.file_name+".anno.tsv.zip"
			response.contentType = 'application/zip'
			response.outputStream << fileOut.newInputStream()
			response.outputStream.flush()
		}else{
			println params.fileName+".anno.tsv.zip does not exist"
		}
    }
}
