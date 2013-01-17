package badger

import groovy.sql.Sql

class AlignService {
	javax.sql.DataSource dataSource
	def grailsApplication
	
	def runMuscle(type,name,fileList){
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
		println "running alignment"
		return [type:type, name:name]
				
		//String[] comm = ["${program}", "-outfmt", "${outFmt}", "-num_threads", "1", "-query", "${blastJobId}", "-evalue", "${eval}", "-num_descriptions", "${numDesc}", "-num_alignments", "${numAlign}", "-out", "${BlastOutFile}", "-db", "$dbString"]
		//ProcessBuilder blastProcess = new ProcessBuilder(comm)  
		//blastProcess.redirectErrorStream(true)
        //Process p = blastProcess.start()
		//p.waitFor()
		//println "Error = "+p.text

	}
}
