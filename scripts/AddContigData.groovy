package bicyclus_anynana

import groovy.sql.Sql
def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/b_anynana", 'ben', '', 'org.postgresql.Driver')
//
//println args[0]
//println "Adding contig data from " + args
def contigFile = new File('data/velvet_khmer_k41.fa').text
//def contigFile = new File('data/test.fa').text
def sequence=""
def contig_id=""
def count_all=0
def count_gc

println "Starting..."
def contigMap = [:]
contigFile.split("\n").each{
    if ((matcher = it =~ /^>(.*)/)){
        if (sequence != ""){
            count_all++
            //get gc
            count_gc = sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
            def gc = (count_gc/sequence.length())*100
            gc = sprintf("%.2f",gc)
            //add data to map
            contigMap.contig_id = contig_id
            contigMap.gc = gc
            contigMap.length = sequence.length()
            contigMap.sequence = sequence
            contigMap.coverage = '0.0'
            if ((count_all % 1000) ==  0){
            	println "Adding $contig_id - $count_all"
            	new Contig(contigMap).save(flush:true)
            }else{
            	new Contig(contigMap).save()
            }
            //println contigMap
            sequence=""
        }
        contig_id = matcher[0][1]
        count_gc = 0
    }else{
        sequence += it.toUpperCase()
    }
} 
//catch the last one
count_gc = sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
def gc = (count_gc/sequence.length())*100
contigMap.contigId = contig_id
contigMap.gc = gc
contigMap.length = sequence.length()
contigMap.sequence = sequence
contigMap.coverage = '0'
new Contig(contigMap).save()
