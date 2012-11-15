package badger
import grails.plugins.springsecurity.Secured
//@Secured(['ROLE_USER','ROLE_ADMIN'])

class BlastController { 
	def grailsApplication
	def configDataService
	
    def info = {
    }
    def index() { 
    	def blastFiles = FileData.findAllByFile_typeInList(["mRNA","Peptide","Genome"],[sort:"id"])
    	return [blastFiles:blastFiles]
    }
    def blastError = {
    }
    def runBlast = {
    	// set the database files
    	def select 
    	def type 
    	if (params.blastDB =="genomeRadio"){
    		select = params.genomeCheck
    		type = "Genome"
    	}else if (params.blastDB =="transRadio"){
    		select = params.transCheck
    		type = "Transcriptome"
    	}else{
    		select = params.protCheck
    		type = "Genes"
    	}
    	println "select = "+select
    	def dbString = ""
    	if (select instanceof String){
    		def fileInfo = FileData.findByFile_name(select)
			def dbfile = fileInfo.file_dir+"/"+fileInfo.file_name
			println "dbfile = "+dbfile
    		dbString = "data/"+dbfile
    	}else{
			select.each{
				def fileInfo = FileData.findByFile_name(it)
				def dbfile = fileInfo.file_dir+"/"+fileInfo.file_name
				println "dbfile = "+dbfile
				dbString += "data/"+dbfile+" "
			}
		}
		dbString = dbString.trim()
        println "dbString = "+dbString
        def program = grailsApplication.config.blastPath+params.PROGRAM
        def eval = params.EXPECT
        def blastSeq = params.blastId

        //check if a file has been uploaded
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
				def comm = """$program -db \"$dbString\" -outfmt $outFmt -num_threads 1 -query $blastJobId -evalue $eval -num_descriptions $numDesc -num_alignments $numAlign -out $BlastOutFile $unGap"""
				//["$program outfmt $outFmt"].execute().text 
				println "comm = "+comm
				//def comm = "/home/elswob/software/blast+/ncbi-blast-2.2.26+/bin/blastp -db \"data/A_viteae/nAv.1.0.1.aug.proteins.fasta data/L_sigmodontis/nLs.2.1.2.aug.proteins.fasta data/D_immitis/nDi.2.2.2.aug.proteins.fasta data/O_ochengi/nOo.2.0.1.aug.proteins.fasta data/B_malayi/b_malayi.WS234.protein_edit.fa data/C_angaria/c_angaria.WS234.protein_edit.fa data/B_xylophilus/b_xylophilus.WS234.protein_edit.fa data/H_contortus/h_contortus.WS234.protein_edit.fa data/S_ratti/s_ratti.WS234.protein_edit.fa data/T_spiralis/t_spiralis.WS234.protein_edit.fa data/C_elegans/c_elegans.WS233.protein_edit.fa\" -outfmt 0 -num_threads 1 -query /tmp/blast_job_e4889ba3-2b90-422e-a9fc-0619f50f6529 -evalue 1e-5 -num_descriptions 20 -num_alignments 20 -out /tmp/blast_job_e4889ba3-2b90-422e-a9fc-0619f50f6529.out"
				//println "blast command = "+comm
				
				def p = comm.execute()
				//wait until the blast has finished     
				p.waitFor()
				
				//def blastProcess = new ProcessBuilder("${grailsApplication.config.blastxPath} -db ${grailsApplication.config.sprotPath} -outfmt 5 -window_size 0 -num_threads 4 -max_target_seqs 10".split(" "))
				//def blastCom = ("$program", "-outfmt", "$outFmt", "-num_threads", "1", "-query", "$blastJobId", "-evalue", "$eval", "-num_descriptions", "$numDesc", "-num_alignments", "$numAlign", "-out", "$BlastOutFile", "$unGap", "-db", "\"$dbString\"")
				//blastCom.last("-db \"$dbString\"")
				//println "blastCom = "+blastCom
				//def blastProcess = new ProcessBuilder("$program", "-outfmt $outFmt", "-num_threads 1", "-query $blastJobId", "-evalue $eval", "-num_descriptions $numDesc", "-num_alignments $numAlign", "-out $BlastOutFile", "$unGap", "-db \"$dbString\"")
                //println "blast = "+blastProcess
                //blastProcess.redirectErrorStream(true)
                //Process p = blastProcess.start()
				//blastProcess.waitFor()
				//p.waitFor()
				
				println "finished BLAST"
				println "open BLAST output"
				//def blastOut = p.text
				List blastRes = new ArrayList();
				def queryInfo = []
				def hitInfo = []
				def singleHit = [:]
				def matcher
				def oldId = ""
				def newId =""
				//check output file has something in it
				println "outfile size = "+BlastOutFile.length()
				if (BlastOutFile.length() > 0){
					//check for tab format
					if (outFmt == '6'){
						def blastOut = new File("$BlastOutFile").text
						//split blast result by new lines
						blastOut.split("\n").each{ 
							if ((matcher = it =~ /(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*?)\t(.*)/)){
								
								//add anchor and internal links
								def linker = matcher[0][2].replaceAll(/-\./, '') 
								if (type == 'Peptide' || 'Genes' || 'mRNA'){
									it = "<a name=\"$linker\"></a>"+matcher[0][1]+"<a href=\"/search/m_info?mid="+matcher[0][2]+"\">"+matcher[0][2]+"</a>\t"+matcher[0][3]+"\t"+matcher[0][4]+"\t"+matcher[0][5]+"\t"+matcher[0][6]+"\t"+matcher[0][7]+"\t"+matcher[0][8]+"\t"+matcher[0][9]+"\t"+matcher[0][10]+"\t"+matcher[0][11]+"\t"+matcher[0][12]
								}else if (type == 'Genome'){
									it = "<a name=\"$linker\"></a>"+matcher[0][1]+"<a href=\"/search/genome_info?contig_id="+matcher[0][2]+"\">"+matcher[0][2]+"</a>\t"+matcher[0][3]+"\t"+matcher[0][4]+"\t"+matcher[0][5]+"\t"+matcher[0][6]+"\t"+matcher[0][7]+"\t"+matcher[0][8]+"\t"+matcher[0][9]+"\t"+matcher[0][10]+"\t"+matcher[0][11]+"\t"+matcher[0][12]
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
						return[blast_file: type, blast_result: blastRes, term: blastName, command: comm, blastId: blastJobId, hitData: sortedHitInfo, queryInfo: queryInfo, jsonData: jsonData]
					//check for full format		
					}else if (outFmt == '0'){              
						def blastOut = new File("$BlastOutFile").text
						 //split blast result by new lines
						blastOut.split("\n").each{  
							//create internal links markers
							if ((matcher = it =~ /^\s{2}(.*?)(\s+.*?\s{4}(\d{1}[e\.].*?)$)/)){
								def linker_rep = matcher[0][1].replaceAll(/\.|-|\||:/, "")
								def linker = matcher[0][1]
								it = "<a href=\"#\" onclick=\"\$.scrollTo('#$linker_rep', 800, {offset : -10});\">$linker</a>"+"  "+matcher[0][2]
							}
							//get the query length
							if ((matcher = it =~ /^Length=(.*)/)){
								if (queryInfo.size()==0){ 
									queryInfo.add(matcher[0][1])
								}
							 }
							if ((matcher = it =~ /^>\s(.*)/)){
								//add name attribute to alignment for anchor
								def linker = matcher[0][1].replaceAll(/\.|-/, "") 								                     
								//create internal links
								//println "file type = "+type
								if (type == 'Peptide' || type == 'Genes' || type == 'mRNA'){
									it = "><a href=\"/search/m_info?mid="+matcher[0][1]+"\">"+matcher[0][1]+"</a>"
								}else if (type == 'Genome'){
									it = "><a href=\"/search/genome_info?contig_id="+matcher[0][1]+"\">"+matcher[0][1]+"</a>"
								}
								//transform IDs to links but not before the first alignment
								it = it.replaceAll(/^>/,"<span id=\"$linker\">></span>")
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
							
						}else if (newId != ""){
							singleHit.id = newId
							//println "singleHit = "+singleHit
						}
						
						if (singleHit.size() > 0){
							hitInfo.add(singleHit)
						}
						//println "hitInfo = "+hitInfo
						def sortedHitInfo = hitInfo.sort{it.score as double}.reverse()
						//def sortedHitInfo = hitInfo.sort{it.id}.reverse()
						def jsonData = sortedHitInfo.encodeAsJSON();
						//println "jsonData = "+jsonData
						return[blast_file: type, blast_result: blastRes, term: blastName, command: comm, blastId: blastJobId, hitData: sortedHitInfo, queryInfo: queryInfo, jsonData: jsonData]
					}	
				}else{
					println "BLAST result is empty"
					blastRes = []
				}
            }
    }
}
