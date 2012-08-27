package GDB

import groovy.sql.Sql
def sql = new Sql(dataSource)
def annoMap = [:]

//set the db
annoMap.annoDb = 'SwissProt'

def inFile = new File('/Users/Ben/lumbricus/isilon/b_anynana/est_assembly/cap3_public/cap3_public_e5_b10_v10_sprot_blastx.out').text
inFile.eachLine { line ->
    if ((matcher = line =~ /<Iteration_query-def>(.*?)<\/Iteration_query-def>/)){
            annoMap.geneId = matcher[0][1]
    }
    if ((matcher = line =~ /<Hit_id>(.*?)<\/Hit_id>/)){
            annoMap.annoId = matcher[0][1]
    }
    if ((matcher = line =~ /<Hit_def>(.*?)<\/Hit_def>/)){
            annoMap.descr = matcher[0][1]
    }
    //get HSP info
    if ((matcher = line =~ /<Hsp_evalue>(.*?)<\/Hsp_evalue>/)){
            annoMap.score = matcher[0][1]
    }
    if ((matcher = line =~ /<Hsp_query-from>(.*?)<\/Hsp_query-from>/)){
            annoMap.annoStart = matcher[0][1]
    }
    if ((matcher = line =~ /<Hsp_query-to>(.*?)<\/Hsp_query-to>/)){
            annoMap.annoStop = matcher[0][1]
    }
    //find an end of an HSP and save data
    if ((matcher = line =~ /<\/Hsp>/)){
        println "$annoMap\n"
        new GeneAnno(annoMap).save()
    }
    
    
}

