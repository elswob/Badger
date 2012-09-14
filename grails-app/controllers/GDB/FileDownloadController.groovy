package GDB

class FileDownloadController {

    def index() { }
    def blast_download = {
        def fileOut = new File(params.blastfileId + '.out').text
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
    
	def gene_download = {
		def object_array
		if (params.seq == 'Peptides'){
     		object_array = params.pepFileId
     	}else{
     		object_array = params.nucFileId
     	}
     	 object_array = object_array.replaceAll(/\[/, '')
     	 object_array = object_array.replaceAll(/\]/, '')
     	 object_array = object_array.replaceAll(/ /, '')
     	 def object_list = [] 
     	 object_array.split(",").each{
     	 	 object_list.add(it)
     	 }
     	 println "object_list = "+object_list
     	 def results = GeneInfo.findAllByGene_idInList(object_list)
		 def pep_file_builder=""
		 def nuc_file_builder=""
     	 results.each {
     	 	println "gene_id = "+it.gene_id
     	 	pep_file_builder = pep_file_builder + ">"+it.gene_id+"\n"+it.pep+"\n"
     	 	nuc_file_builder = nuc_file_builder + ">"+it.gene_id+"\n"+it.nuc+"\n"
		 }
		 println "seq = "+params.seq
		 if (params.seq == 'Peptides'){
		 	println "created download file "+params.fileName+".aa"
     	 	response.setHeader "Content-disposition", "attachment; filename="+params.fileName+".aa"
         	response.contentType = 'text/csv'
         	response.outputStream << pep_file_builder
         	response.outputStream.flush()
         }else{
         	 println "created download files "+params.fileName+".fna"
	    	 response.setHeader "Content-disposition", "attachment; filename="+params.fileName+".fna"
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
     	 if (table == 'Genome'){ println "Getting genome seqs"; results = GenomeInfo.findAllByContig_idInList(object_list)}
     	 if (table == 'Transcriptome'){ println "Getting transcriptome seqs"; results = TransInfo.findAllByContig_idInList(object_list)}
     	 //def results = Contig.findAllByContig_idInList(object_list)
     	 println "results = "+results
		 def file_builder=""
     	 results.each {
     	 	println "contig_id = "+it.contig_id
     	 	file_builder = file_builder + ">"+it.contig_id+"\n"+it.sequence+"\n"
		 }
		 //get the blast sequence
		 def blastIn = new File(params.blastfileId).text
		 file_builder = file_builder + blastIn
		 def downloadName = params.fileName.trim()
		 println "created download file "+downloadName+".fna"
     	 response.setHeader "Content-disposition", "attachment; filename="+downloadName+".fna"
         response.contentType = 'text/csv'
         response.outputStream << file_builder
         response.outputStream.flush()
    }
    
    def zip_download = {
    	def fileOut = new File('data/'+params.fileName)
        response.setHeader "Content-disposition", "attachment; filename="+params.fileName
        response.contentType = 'application/zip'
        response.outputStream << fileOut.newInputStream()
        response.outputStream.flush()
    }
}
