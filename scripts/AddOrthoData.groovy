package badger

import groovy.sql.Sql

def grailsApplication

orthoCheck()
def orthoCheck(){
	if (grailsApplication.config.o.file){
		if (new File("data/"+grailsApplication.config.o.file.trim()).exists()){
    		def  orthoFile = new File("data/"+grailsApplication.config.o.file.trim()).text
    		println "Adding orthoMCL fole - "+grailsApplication.config.o.file
    		addOrtho(orthoFile)
    	}else{
    		println "Orthomcl file data/"+grailsApplication.config.o.file+" does not exist!"
    	}
    }else{
    	println "No orthoMCL file provided"
    }
}

def addOrtho(orthoFile){
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	println "Deleting old data..."
	def delsql = "delete from ortho;";
	println delsql
	sql.execute(delsql)
	println "Adding new...."
    def orthoMap = [:]
    def count=0
    orthoFile.eachLine { line ->
		count++
		splitter = line.split(":")
      	def group = splitter[1].trim()
		//println "group = "+group
        def gList = group.split(" ")
        //println "gList = "+gList 
     	gList.each {
     		def trans_name = it.split("\\|",0)
     		trans_name = trans_name[1].trim()
     		orthoMap.group_id = count as int
          	orthoMap.trans_name = trans_name
          	
     		GeneInfo geneFind = GeneInfo.findByMrna_id(trans_name)
     		def o = new Ortho(orthoMap)
          	o.gene = geneFind
          	
          	if ((count % 1000) ==  0){
          		//println "orthoMap = "+orthoMap
          		println count
            	o.save(flush:true)
            }else{
            	o.save()
            }
        }  
    }
}