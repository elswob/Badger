package bicyclus_anynana

import groovy.sql.Sql

def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/genome_29_09_11", 'ben', 'badger', 'org.postgresql.Driver')

def sqlsearch = "select * from gene_info;"
def results = sql.rows(sqlsearch)
def infoMap = [:]
def count_check=0;
results.each {
  infoMap.gene_id = it.id
  infoMap.contig_id = it.contig
  infoMap.coverage = it.coverage
  infoMap.intron = it.intron
  infoMap.nuc = it.nuc
  infoMap.pep = it.pept
  infoMap.rep = it.rep
  infoMap.source = it.source
  infoMap.start = it.start
  infoMap.stop = it.stop
  count_check++
  if ((count_check % 10000) ==  0){
    println "gene_info "+count_check
    new GeneInfo(infoMap).save(flush:true)
  }else{
    new GeneInfo(infoMap).save()
  }
}


count_check=0;
def sqlsearch2 = "select * from gene_anno;"
def results2 = sql.rows(sqlsearch2)
def infoMap2 = [:]
results2.each{
  infoMap2.gene_id = it.id
  infoMap2.anno_db = it.anno_db
  infoMap2.anno_id = it.anno_id
  infoMap2.anno_start = it.anno_start
  infoMap2.anno_stop = it.anno_stop
  infoMap2.descr = it.descr
  infoMap2.score = it.score
  if (it.descr == null){
    infoMap2.descr = 'n/a'
  }
  count_check++
  if ((count_check % 10000) ==  0){
  	  println "gene_anno " +count_check
  	  new GeneAnno(infoMap2).save(flush:true)
  }else{
  	  new GeneAnno(infoMap2).save()
  }
}
