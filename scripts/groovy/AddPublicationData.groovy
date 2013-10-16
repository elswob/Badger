package badger

import groovy.sql.Sql

def matcher
def dataSource = ctx.getBean("dataSource")
def sql = new Sql(dataSource)

def getFiles = MetaData.findAll()
getFiles.each {  	
	def checkSql = "select genus,species from meta_data,publication where genus='"+it.genus+"' and species = '"+it.species+"' and meta_data.id = publication.meta_id; "
	def check = sql.rows(checkSql)
	if (check){
		println "Skipping "+it.genus+" "+it.species
	}else{ 
		println new Date()
		def query = it.genus+"+AND+"+it.species
		println "Getting publication information for "+it.genus+" "+it.species
		getPub(it.id,query)
	}
}

def getPub(data_id,query){
	def idlist = new File("/tmp/"+data_id+"_idlist.txt")
	def pubdata = new File("/tmp/"+data_id+"_pubdata.txt")
	if (idlist.exists()){idlist.delete()}
	if (pubdata.exists()){pubdata.delete()}
	//get the pubmed data
	def utils = "http://www.ncbi.nlm.nih.gov/entrez/eutils";
	def db = 'PubMed';
	def esearch = "$utils/esearch.fcgi?db=$db&retmax=100000&term=$query";
	println "Getting IDs...";
	println esearch
	idlist << new URL(esearch).getText()
	//def esearch_result=new File("/tmp/idlist.txt")
	def counter=0
	def efetch_ids=''
	//esearch_result.eachLine {
	//esearch_result.split("\n").each{
	idlist.eachLine {
		if ((matcher = it =~ /.*?<Id>(\d+)<\/Id>/)){
			efetch_ids += matcher[0][1] + ","
			counter++
			//get the data in batches as fails if url is too long
			if ((counter % 100) ==  0){
				efetch_ids = efetch_ids[0..-2]
				println "Fetching "+counter			
				def efetch = "$utils/efetch.fcgi?db=$db&id=$efetch_ids&retmode=xml";
				pubdata << new URL(efetch).getText()
				efetch_ids=''
			}
		}
	}
	//get the last ones
	if (efetch_ids != ''){
		efetch_ids = efetch_ids[0..-2]
		println "Fetching "+counter			
		def efetch = "$utils/efetch.fcgi?db=$db&id=$efetch_ids&retmode=xml";
		println efetch
		pubdata << new URL(efetch).getText()
		addPub(pubdata,data_id) 
	}
}
//add info
def addPub(pubFile,data_id){
	MetaData meta = MetaData.findById(data_id)
	def dataSource = ctx.getBean("dataSource")
	def sql = new Sql(dataSource)
	println "Deleting old data..."
	def delsql = "delete from Publication where meta_id = '"+data_id+"';";
	sql.execute(delsql)
	println "Adding new data to db..."
	def pubMap = [:]
	pubMap.abstractText = ""
	int count_all = 0
	def dateString = ''
	def nameString = ''
	def year = ''
	def month = ''
	def day = ''
	def firstname = '' 
	def lastname = ''
	boolean indate = false
	pubFile.eachLine { line ->		
		if ((matcher = line =~ /<ArticleId IdType="pubmed">(.*?)<\/ArticleId>/)){
				pubMap.pubmedId = matcher[0][1]
				count_all++
		}        
		else if ((matcher = line =~ /<ArticleTitle>(.*?)<\/ArticleTitle>/)){
				pubMap.title =  matcher[0][1]    
		}
		else if ((matcher = line =~ /<AbstractText>(.*?)<\/AbstractText>/)){
			pubMap.abstractText = matcher[0][1]
		}else if ((matcher = line =~ /<AbstractText Label=(.*?)>(.*?)<\/AbstractText>/)){
			if ((matcher = line =~ /<AbstractText Label="(.*?)".*?>(.*?)<\/AbstractText>/)){
				pubMap.abstractText += matcher[0][1]+": "+matcher[0][2]+"<br><br>"
				//println pubMap.abstractText
			}
		}
		else if ((matcher = line =~ /<Title>(.*?)<\/Title>/)){
				pubMap.journal = matcher[0][1]
		}
		else if ((matcher = line =~ /<ISOAbbreviation>(.*?)<\/ISOAbbreviation>/)){
				pubMap.journal_short = matcher[0][1]
		}
		else if ((matcher = line =~ /<Volume>(.*?)<\/Volume>/)){
				pubMap.volume = matcher[0][1]
		}
		else if ((matcher = line =~ /<Issue>(.*?)<\/Issue>/)){
				pubMap.issue = matcher[0][1]
		}
		//use initials for first name as some entries have no first names!
		else if ((matcher = line =~ /<Initials>(.*?)<\/Initials>/)){
				firstname = matcher[0][1]
				nameString += firstname + " " + lastname + ", "
		}
		else if ((matcher = line =~ /<LastName>(.*?)<\/LastName>/)){
				lastname = matcher[0][1]
		}
		else if ((matcher = line =~ /<PubMedPubDate PubStatus="pubmed">/)){
				indate = true       		
		}       	
		else if ((matcher = line =~ /<\/PubMedPubDate>/)){
				indate = false
		}
		else if ((matcher = line =~ /<ArticleId IdType="doi">(.*?)<\/ArticleId>/)){
				pubMap.doi = matcher[0][1]
		}

		//get date data       
		else if (indate){
			if ((matcher = line =~ /<Year>(.*?)<\/Year>/)){
				year = matcher[0][1]
			}
			if ((matcher = line =~ /<Month>(.*?)<\/Month>/)){
				month = matcher[0][1]
			}
			if ((matcher = line =~ /<Day>(.*?)<\/Day>/)){
				day = matcher[0][1]
				dateString = year + "/" + month + "/" + day
				pubMap.dateString = dateString
			}       	
		}
		//end of an entry
		else if ((matcher = line =~ /<\/PubmedArticle>/)){
			if (nameString.size() > 0){
				nameString = nameString[0..-3]
			}
			pubMap.authors = nameString
			nameString = ''
			dateString = ''  

          	//check for missing abstracts, e.g. http://www.ncbi.nlm.nih.gov/pubmed?term=5594788           
         	if (!pubMap.abstractText){println "No abstract available for "+pubMap.pubmedId ; pubMap.abstractText = "Not available"} 
          	//other checks
          	if (!pubMap.issue){println "No issue available for "+pubMap.pubmedId ; pubMap.issue = "Not available"} 
          	if (!pubMap.journal){ println "No journal available for "+pubMap.pubmedId; pubMap.journal = "Not available"}
          	if (!pubMap.journal_short){ println "No journal_short available for "+pubMap.pubmedId; pubMap.journal_short = "Not available"}          	
          	if (!pubMap.volume){ println "No volume available for "+pubMap.pubmedId; pubMap.volume = "Not available"}
          	if (!pubMap.title){ println "No title available for "+pubMap.pubmedId; pubMap.title = "Not available"}
          	if (!pubMap.authors){ println "No authors available for "+pubMap.pubmedId; pubMap.authors = "Not available"}
          	if (!pubMap.dateString){ println "No dateString available for "+pubMap.pubmedId; pubMap.dateString = "Not available"}
          	if (!pubMap.doi){ println "No doi available for "+pubMap.pubmedId; pubMap.doi = "Not available"}
          	if (!pubMap.pubmedId){ println "No pubmedId available!"}

          	//println pubMap
            Publication pub = new Publication(pubMap)
			meta.addToPubs(pub)
          	//println "meta = "+meta
			if ((count_all % 100) ==  0){
				println "Adding "+count_all
				pub.save(flush:true)
			}else{
				pub.save(flush:true)
              	//println "pub = "+pub
			}
			//clear abstract text
			pubMap.abstractText = ""
		}
	}
	println "Added "+count_all 
}
