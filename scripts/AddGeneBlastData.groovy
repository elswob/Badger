package GDB

import groovy.sql.Sql

def a = AnnoData.findAllByType('blast')
a.each{
	AnnoData anno = it
	//get FileData parent of AnnoData object
	def b = anno.filedata
	def fileLoc = b.file_dir+"/"+anno.anno_file
	def blastFile = new File("data/"+fileLoc).text
	println fileLoc
	addGeneBlast(anno.source,blastFile)
}

//add Unigene annotations
def addGeneBlast(db,blastFile){
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	//println "Deleting old data..."
	//def delsql = "delete from gene_blast where file_id = '"+file_id+"' and anno_db = '"+db+"';";
	//sql.execute(delsql)
	println "Adding new...."
    def annoMap = [:]
    def count_check = 0
    def count_all = 0
    def anno_id
    annoMap.anno_db = db
    def mrna_id
    blastFile.eachLine { line ->		
        if ((matcher = line =~ /<Iteration_query-def>(.*?)<\/Iteration_query-def>/)){
                annoMap.mrna_id = matcher[0][1]
                mrna_id = matcher[0][1] 
                count_check = 0
        }                	
        if ((matcher = line =~ /<Hit_num>(.*?)<\/Hit_num>/)){
                count_check++
        }
        if ((matcher = line =~ /<Hit_id>(.*?)<\/Hit_id>/)){
        		annoMap.anno_id = matcher[0][1]
        		//catch the stupid BL_ORD_ID hitIDs
        		if ((matcher = line =~ /<Hit_id>.*?BL_ORD_ID.*<\/Hit_id>/)){
        			anno_id = "wrong"
        		}else{            	
	               	anno_id = "right"
                }
        }
        if ((matcher = line =~ /<Hit_def>(.*?)<\/Hit_def>/)){
        		annoMap.descr = matcher[0][1]
        		if (anno_id == "wrong"){
        			if ((matcher = line =~ /<Hit_def>(.*?)\s(.*?)<\/Hit_def>/)){
        				annoMap.anno_id = matcher[0][1]
        			}else{
        				annoMap.anno_id = "n/a"
        			}
        		}                
        }
        //get HSP info
        if ((matcher = line =~ /<Hsp_score>(.*?)<\/Hsp_score>/)){
                annoMap.score = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_query-from>(.*?)<\/Hsp_query-from>/)){
                annoMap.anno_start = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_query-to>(.*?)<\/Hsp_query-to>/)){
                annoMap.anno_stop = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_hit-from>(.*?)<\/Hsp_hit-from>/)){
                annoMap.hit_start = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_hit-to>(.*?)<\/Hsp_hit-to>/)){
                annoMap.hit_stop = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_identity>(.*?)<\/Hsp_identity>/)){
                annoMap.identity = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_gaps>(.*?)<\/Hsp_gaps>/)){
                annoMap.gaps = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_align-len>(.*?)<\/Hsp_align-len>/)){
                annoMap.align = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_qseq>(.*?)<\/Hsp_qseq>/)){
                annoMap.qseq = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_hseq>(.*?)<\/Hsp_hseq>/)){
                annoMap.hseq = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_midline>(.*?)<\/Hsp_midline>/)){
                annoMap.midline = matcher[0][1]
        }
        //if ((matcher = line =~ /<Hsp_evalue>(.*?)<\/Hsp_evalue>/)){
        //        annoMap.eval = matcher[0][1]
        //}
        //find an end of an HSP and save data
        if ((matcher = line =~ /<\/Hsp>/)){
            //only add set number of elements from each blast
            if (count_check < 10){
            	//check for max evalue
            	def scoreInt = annoMap.score as Integer
            	if (scoreInt >= 100){
            		count_all++
            		
            		GeneInfo geneFind = GeneInfo.findByMrna_id(mrna_id)
            		GeneBlast gb = new GeneBlast(annoMap)
					geneFind.addToGeneBlast(gb)
					
            		if ((count_all % 1000) ==  0){
            			println count_all
            			//println annoMap
            			gb.save(flush:true)
            		}else{
            			gb.save()
            		}
            	}
            }
        } 
    }
}
