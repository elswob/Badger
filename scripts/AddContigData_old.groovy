package bicyclus_anynana

import groovy.sql.Sql
def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/b_anynana", 'ben', 'badger', 'org.postgresql.Driver')
//
//println args[0]
//println "Adding contig data from " + args
def contigFile = new File('/Users/Ben/Work/data/lumbribase.fasta').text
def sequence=""
def contig_id=""
def count=0
def count_gc=0
def match_gc = ~/(?i)[GC]/

def contigMap = [:]
contigFile.split("\n").each{
    if ((matcher = it =~ /^>(.*)/)){
        if (sequence != ""){
            println "Adding $contig_id - $count"
            count++
            //get gc
            def count_gc2 = sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
            println count_gc2
            def matcher = match_gc.matcher(sequence)
            while (matcher.find()){
                count_gc++
            }
            def gc = (count_gc/sequence.length())*100
            //add data to map
            contigMap.contigId = contig_id
            contigMap.gc = gc
            contigMap.length = sequence.length()
            contigMap.sequence = sequence
            contigMap.coverage = '99'
            new Contig(contigMap).save()
            sequence=""
        }
        contig_id = matcher[0][1]
        count_gc = 0
    }else{
        sequence += it
    }
} 
//catch the last one
def matcher = match_gc.matcher(sequence)
while (matcher.find()){
    count_gc++
}
def gc = (count_gc/sequence.length())*100
contigMap.contigId = contig_id
contigMap.gc = gc
contigMap.length = sequence.length()
contigMap.sequence = sequence
contigMap.coverage = '99'
new Contig(contigMap).save()
