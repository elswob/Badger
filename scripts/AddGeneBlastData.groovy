package GDB

import groovy.sql.Sql

def cleanUpGorm() { 
	def sessionFactory = ctx.getBean("sessionFactory")
	def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    def session = sessionFactory.currentSession 
    session.flush() 
    session.clear() 
    propertyInstanceMap.get().clear() 
}

def a = AnnoData.findAllByType('blast')
a.each{
	AnnoData anno = it
	//get FileData parent of AnnoData object
	def b = anno.filedata
	def fileLoc = b.file_dir+"/"+anno.anno_file
	def blastFile = new File("data/"+fileLoc).text
	println "anno.source = "+anno.source
	println "fileLoc = "+fileLoc
	if (b.file_dir != "A_viteae"){
		addGeneBlast(anno.source,blastFile,anno.anno_file)
	}
}

//add Unigene annotations
def addGeneBlast(db,blastFile,annoFile){
	cleanUpGorm()
	def dataSource = ctx.getBean("dataSource")
  	def sql = new Sql(dataSource)
  	//println "Deleting old data..."
	//def delsql = "delete from gene_blast,gene_info,file_data,anno_data where gene_blast.gene_id = gene_info.id and gene_info.file_id = file_data.id and file_data.id = anno_data.filedata_id and anno_data.anno_file = '"+annoFile+"';";
	//println delsql
	//sql.execute(delsql)
	println "Adding new...."
	println new Date()
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
					geneFind.addToGblast(gb)
					
            		if ((count_all % 5000) ==  0){
            			println count_all
            			println new Date()
            			gb.save(flush:true)
            			cleanUpGorm()
            			//println annoMap         			
            		}else{
            			gb.save()
            		}
            	}
            }
        } 
    }
    println count_all
}
