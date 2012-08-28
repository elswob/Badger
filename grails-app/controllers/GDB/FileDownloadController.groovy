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
    
    def unigene_contig_download = {
     	 def object_array = params.fileId
     	 object_array = object_array.replaceAll(/\[/, '')
     	 object_array = object_array.replaceAll(/\]/, '')
     	 object_array = object_array.replaceAll(/ /, '')
     	 def object_list = [] 
     	 object_array.split(",").each{
     	 	 object_list.add(it)
     	 }
     	 //println "object_list = "+object_list
     	 def results = UnigeneInfo.findAllByContig_idInList(object_list)
		 def file_builder=""
     	 results.each {
     	 	//println "contig_id = "+it.contig_id
     	 	file_builder = file_builder + ">"+it.contig_id+"\n"+it.sequence+"\n"
		 }
		 println "created download file "+params.fileName+".fna"
     	 response.setHeader "Content-disposition", "attachment; filename="+params.fileName+".fna"
         response.contentType = 'text/csv'
         response.outputStream << file_builder
         response.outputStream.flush()
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
     	 def results = Contig.findAllByContig_idInList(object_list)
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
     	 println "object_list = "+object_list
     	 def results
     	 if (table == 'genome'){ results = Contig.findAllByContig_idInList(object_list)}
     	 if (table == 'unigenes'){ println "in unigenes"; results = UnigeneInfo.findAllByContig_idInList(object_list)}
     	 //def results = Contig.findAllByContig_idInList(object_list)
		 def file_builder=""
     	 results.each {
     	 	println "contig_id = "+it.contig_id
     	 	file_builder = file_builder + ">"+it.contig_id+"\n"+it.sequence+"\n"
		 }
		 //get blast sequence
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
