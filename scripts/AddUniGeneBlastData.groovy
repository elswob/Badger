package bicyclus_anynana

import groovy.sql.Sql
def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/b_anynana", 'ben', 'badger', 'org.postgresql.Driver')

//add Unigene annotations
def addUnigeneBlast(blastFile,db){
    def annoMap = [:]
    def count_check = 0
    def count_all = 0
    annoMap.anno_db = db
    blastFile.eachLine { line ->		
        if ((matcher = line =~ /<Iteration_query-def>(.*?)<\/Iteration_query-def>/)){
                annoMap.contig_id = matcher[0][1]
                count_check = 0
        }                	
        if ((matcher = line =~ /<Hit_num>(.*?)<\/Hit_num>/)){
                count_check++
        }
        if ((matcher = line =~ /<Hit_id>(.*?)<\/Hit_id>/)){
                annoMap.anno_id = matcher[0][1]
        }
        if ((matcher = line =~ /<Hit_def>(.*?)<\/Hit_def>/)){
                annoMap.descr = matcher[0][1]
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
            		if ((count_all % 10000) ==  0){
            			println count_all
            			new UnigeneAnno(annoMap).save(flush:true)
            		}else{
            			new UnigeneAnno(annoMap).save()
            		}
            	}
            }
        } 
    }
}
inFile = new File('data/cap3_public_e5_b10_v10_sprot_blastx2.out').text
//inFile = new File('data/est_others_m7_e5_b10_v10_blastn').text
addUnigeneBlast(inFile, 'SwissProt')
