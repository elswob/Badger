package badger

import groovy.sql.Sql

class AlignService {
	javax.sql.DataSource dataSource
	def grailsApplication
	
	def runAlign(type,name,fileList){
		//params.seq,params.fileName,params.orthoFileId
		def object_array = fileList
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
		def file_builder=""
     	results.each {
     		println "mrna_id = "+it.mrna_id
     		if (type == 'nuc'){
     			file_builder = file_builder + ">"+it.mrna_id+"\n"+it.nuc+"\n"
     		}else{
     			file_builder = file_builder + ">"+it.mrna_id+"\n"+it.pep+"\n"
			}
		}
		
		def uuid = UUID.randomUUID()
		def JobId = "/tmp/align_job_"+uuid
		println "align name = " + name
		println "align job Id = " + JobId
		println "writing fasta to file"
		File f = new File(JobId)
		f.write(file_builder)
		def OutFile = new File(JobId+".out")   
		//def aligner = grailsApplication.config.clustaloPath.trim()
		def aligner = grailsApplication.config.musclePath.trim()
		    
		println "running alignment - ${aligner} -infile ${f} -outfile ${OutFile}"
		
		//clustal omega		
		//String[] comm = ["${aligner}", "-i", "${f}", "-o", "${OutFile}", "--outfmt", "clu"]		
		//muscle
		String[] comm = ["${aligner}","-in", "${f}", "-out", "${OutFile}", "-html"]
		
		ProcessBuilder alignProcess = new ProcessBuilder(comm)  
		alignProcess.redirectErrorStream(true)
        Process p = alignProcess.start()
		p.waitFor()
		println "Error = "+p.text
		def alignOut = new File("$OutFile").text
		
		return [type:type, name:name, OutFile:OutFile, alignOut:alignOut]

	}
}
