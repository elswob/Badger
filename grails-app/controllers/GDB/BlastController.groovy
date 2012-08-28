package GDB
import grails.plugins.springsecurity.Secured
//@Secured(['ROLE_USER','ROLE_ADMIN'])

class BlastController { 
	def grailsApplication
    def info = {}
    def index() { }
    def blastError = {
    }
    def runBlast = {
    	// set the database files
    	def dbfile
    	println "datalib = "+params.datalib
    	if (params.datalib == "genome"){ dbfile = "velvet_khmer_k41.fa"}
    	if (params.datalib == "bacs"){ dbfile = "bacs_renamed.fa"}
    	if (params.datalib == "unigenes"){ dbfile = "sequence.fasta.cap.contigs_and_singlets_renamed.fa"}
        def db = "data/"+dbfile
        def blast_file = dbfile
        def program = grailsApplication.config.blastPath+params.PROGRAM
        def eval = params.EXPECT
        def blastSeq = params.blastId

        //check if file has been uploaded
        def upload = request.getFile('myFile')
		if (!upload.empty) {
				println "Uploaded file for BLAST"
				println "Class: ${upload.class}"
				println "Name: ${upload.name}"
				println "OriginalFileName: ${upload.originalFilename}"
				println "Size: ${upload.size}"
				println "ContentType: ${upload.contentType}"
				blastSeq = upload.inputStream.text
		}
        //blastSeq = params.blastId
        def numDesc = params.DESCRIPTIONS
        def numAlign = params.ALIGNMENTS
        def unGap = params.UNGAPPED_ALIGNMENT
        def outFmt = params.ALIGNMENT_VIEW
        if (unGap == "is_set" && params.program == "blastn"){
            unGap = "-ungapped"
        }else{
            unGap = ""
        }
        println "seq size = "+blastSeq.size()

        	
        //remove any whitespace in user input fasta file
        def blastSeqTrim = blastSeq.trim()
        List li=new ArrayList();
        int seq_length
        def count_seq=0
        //split file by new lines
        blastSeqTrim.split("\n").each{
            li.add(it)
            it = it.replaceAll(/\s*$/, '')
            if (count_seq > 0){
            	seq_length = seq_length + it.size()
            }
            count_seq ++
            //println it
        }
        //assume first line is fasta header
        def blastName = li[0]
        //do some checks
        if (blastSeq.size() < 10){
        	println "no sequence, redirecting...";
        	redirect(action:"blastError", params:[error:"no_seq"])
        //check to see if first line doesn't start with > (not the best way to do this i'm sure)
        }else if (blastName =~ /^\w+/){
        	println "incorrect header, redirecting... "+blastName
        	redirect(action:"blastError", params:[error:"no_header"])
        }else{
			//get rid of the >	
			blastName = blastName.replaceAll(/>/, "") 
			//generate unique id
			def uuid = UUID.randomUUID()
			def blastJobId = "/tmp/blast_job_"+uuid
			println "blast Name = " + blastName
			println "blast job Id = " + blastJobId
			//runAsync {
				println "writing fasta to file"
				File f = new File(blastJobId)
				f.write(blastSeq)
				def BlastOutFile = new File(blastJobId+".out")       
				println "running BLAST"
				//outFmt="\"7 sseqid ssac qstart qend sstart send qseq evalue bitscore\""
				def comm = "$program -db $db -outfmt $outFmt -num_threads 1 -query $blastJobId -evalue $eval -num_descriptions $numDesc -num_alignments $numAlign -out $BlastOutFile $unGap"
				println "blast command = "+comm
				def p = comm.execute()   
				
				//wait until the blast has finished     
				p.waitFor()
				println "finished BLAST"
				println "open BLAST output"
				//def blastOut = p.text
				List blastRes = new ArrayList();
				def queryInfo = []
				def hitInfo = []
				def singleHit = [:]
				def matcher
				def oldId
				def newId =""
				//check output file has something in it
				println "outfile size = "+BlastOutFile.length()
				if (BlastOutFile.length() > 0){
					//check which format the BLAST is
					if (outFmt == '6'){
						def blastOut = new File("$BlastOutFile").text
						//split blast result by new lines
						blastOut.split("\n").each{ 
							if ((matcher = it =~ /(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*)/)){
								//define link
								def linker = matcher[0][2]
								def marker = matcher[0][3]
								//it = it.replaceAll(/$linker/,"<a name=\"$linker\">$linker</a>")
								it = "<a name=\"$linker\"></a>" + it
								//add links
								if (params.datalib == "unigenes"){
									it = it.replaceAll(/\s(contig_\d+)/, "<a href=\"/search/unigene_info?contig_id=\$1\">\$1</a>") 
								}
								if (params.datalib == "genome"){
									it = it.replaceAll(/\s(contig_.*?)/, "<a href=\"/search/contig_info?contig_id=\$1\">\$1</a>") 
								}
								if (params.datalib == "bacs"){
									it = it.replaceAll(/\sgi_(\d+)_.*/, "<a href=\"http://www.ncbi.nlm.nih.gov/nuccore/\$1\">\$1</a>") 
								}	
								//get id, score start and stop
								singleHit.id=matcher[0][2]
								singleHit.start=matcher[0][7]
								singleHit.stop=matcher[0][8]  
								singleHit.score=matcher[0][12]
								hitInfo.add(singleHit)
								singleHit = [:]
								blastRes.add(it) 
							}
						}
						//get query length
						queryInfo.add(seq_length)
						def sortedHitInfo = hitInfo.sort{it.score as double}.reverse()
						def jsonData = sortedHitInfo.encodeAsJSON();
						return[blast_file: params.datalib, blast_result: blastRes, term: blastName, command: comm, blastId: blastJobId, hitData: sortedHitInfo, queryInfo: queryInfo, jsonData: jsonData]
							
					}else if (outFmt == '0'){              
						def blastOut = new File("$BlastOutFile").text
						 //split blast result by new lines
						blastOut.split("\n").each{  
							//create internal links markers
							if ((matcher = it =~ /^\s{2}(\w+).*?\s+(.*?)\s{4}(\d{1}[e\.].*?)$/)){
								def linker = matcher[0][1]
								it = it.replaceAll(/$linker/,"<a href=\"#$linker\">$linker</a>")
							}
							//get the query length
							if ((matcher = it =~ /^Length=(.*)/)){
								if (queryInfo.size()==0){ 
									queryInfo.add(matcher[0][1])
								}
							 }
							if ((matcher = it =~ /^>\s(\w+)/)){
								//add name attribute to alignment for anchor
								def linker = matcher[0][1]
								it = it.replaceAll(/>/,"<a name=\"$linker\">></a>")                       
								//transform IDs to links but not before the first alignment
									//add links
								if (params.datalib == "unigenes"){
									it = it.replaceAll(/\s(contig_\d+)/, "<a href=\"/search/unigene_info?contig_id=\$1\">\$1</a>") 
								}
								if (params.datalib == "genome"){
									it = it.replaceAll(/\s(contig_\d+)/, "<a href=\"/search/contig_info?contig_id=\$1\">\$1</a>") 
								}
								if (params.datalib == "bacs"){
									it = it.replaceAll(/\sgi_(\d+)_.*/, "<a href=\"http://www.ncbi.nlm.nih.gov/nuccore/\$1\">\$1</a>") 
								}	
								oldId = newId
								newId = matcher[0][1]                    
							}
							if ((matcher = it =~ /\s+Score\s=\s+(.*?)\s+bits.*/)){
								//check the alignment has been parsed
								if (singleHit['start']!=null){
									//check if it is a multiple HSP
									if (oldId != ""){
										singleHit.id = oldId
									}else{
										singleHit.id = newId
									}
									hitInfo.add(singleHit)  
									singleHit = [:]
									oldId = ""
								}
								singleHit.id = oldId
								singleHit.score=matcher[0][1]    
								singleHit.score
							}                   
							//get start
							if ((matcher = it =~ /^Query\s+(\d+).*/)){
								//only want the first one
								if (singleHit['start']==null){
									singleHit.start=matcher[0][1]    
									singleHit.start
								}
							}
							//get stop
							if ((matcher = it =~ /^Query.*?\s+(\d+)$/)){              
								singleHit.stop=matcher[0][1]    
								singleHit.stop
							}
							blastRes.add(it)                   
						}
						//catch the last one
						if (oldId != ""){
							singleHit.id = oldId
						}else{
							singleHit.id = newId
						}
						hitInfo.add(singleHit)
						def sortedHitInfo = hitInfo.sort{it.score as double}.reverse()
						//def sortedHitInfo = hitInfo.sort{it.id}.reverse()
						def jsonData = sortedHitInfo.encodeAsJSON();
						return[blast_file: params.datalib, blast_result: blastRes, term: blastName, command: comm, blastId: blastJobId, hitData: sortedHitInfo, queryInfo: queryInfo, jsonData: jsonData]
					}	
				}else{
					println "BLAST result is empty"
					blastRes = []
				}
            }
    }
    def test = {}
}
