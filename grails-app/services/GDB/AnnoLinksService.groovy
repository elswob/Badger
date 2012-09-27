package GDB

class AnnoLinksService {
	def grailsApplication
	
    def getLinks() {
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
}
