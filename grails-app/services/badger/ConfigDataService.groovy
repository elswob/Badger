package badger

class ConfigDataService {
	def grailsApplication
	//gene annotation links
    def getGeneAnnoLinks() {
    	def annoLinks = [:]
    	if (grailsApplication.config.g.blast){
	    	for(item in grailsApplication.config.g.blast){
				item = item.toString()
     	 		def splitter = item.split("=",2)
     	 		def splitter2 = splitter[1].split(",")
     	 		annoLinks."${splitter[0]}" = [splitter2[0].trim(),splitter2[1].trim(),splitter2[2].trim()]
			}
		}
		if (grailsApplication.config.g.fun){	
			for(item in grailsApplication.config.g.fun){
					item = item.toString()
     	 			def splitter = item.split("=",2)
     	 			def splitter2 = splitter[1].split(",")
     	 			annoLinks."${splitter[0]}" = [splitter2[0].trim(),splitter2[1].trim(),splitter2[2].trim()]    	 			
     	 	}
     	 }
     	 if (grailsApplication.config.g.IPR){
     	 	def splitter2 = grailsApplication.config.g.IPR.split(",")
     	 	annoLinks.IPR = [splitter2[0].trim(),splitter2[1].trim(),splitter2[2].trim()]
     	 }
     	 return annoLinks
    }
    
    //transcript annotation links
    def getTransAnnoLinks() {
    	def annoLinks = [:]
    	if (grailsApplication.config.t.blast){
	    	for(item in grailsApplication.config.g.blast){
				item = item.toString()
     	 		def splitter = item.split("=",2)
     	 		def splitter2 = splitter[1].split(",")
     	 		annoLinks."${splitter[0]}" = [splitter2[0].trim(),splitter2[1].trim(),splitter2[2].trim()]
			}
		}
		if (grailsApplication.config.t.fun){	
			for(item in grailsApplication.config.g.fun){
					item = item.toString()
     	 			def splitter = item.split("=",2)
     	 			def splitter2 = splitter[1].split(",")
     	 			annoLinks."${splitter[0]}" = [splitter2[0].trim(),splitter2[1].trim(),splitter2[2].trim()]    	 			
     	 	}
     	 }
     	 if (grailsApplication.config.t.IPR){
     	 	def splitter2 = grailsApplication.config.g.IPR.split(",")
     	 	annoLinks.IPR = [splitter2[0].trim(),splitter2[1].trim(),splitter2[2].trim()]
     	 }
     	 return annoLinks
    }
    
    //blast annotation links
    def getBlastLinks = {
    	def blastFiles = [] 
    	def pubBlastFiles = [:]
    	def privBlastFiles = [:]
    	if (grailsApplication.config.blast.pub){
    		def pubLocations = grailsApplication.config.blast.pub
    		pubLocations.each {
    			if (it.value.size() >0){
    				pubBlastFiles."${it.key}" = it.value 
    			}
    		}
    	}
    	if (grailsApplication.config.blast.priv){
    		def privLocations = grailsApplication.config.blast.priv
    		privLocations.each {
    			if (it.value.size() >0){
    				privBlastFiles."${it.key}" = it.value 
    			}
    		}
    	}
    	println "public blastFiles = "+pubBlastFiles
    	println "private blastFiles = "+privBlastFiles
    	blastFiles.add(pubBlastFiles)
    	blastFiles.add(privBlastFiles)
    	return blastFiles
    }
}
