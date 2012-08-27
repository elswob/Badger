package bicyclus_anynana

import groovy.sql.Sql
def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/b_anynana", 'ben', 'badger', 'org.postgresql.Driver')
//
//println args[0]
//println "Adding contig data from " + args
def inFile = new File('/Users/Ben/Work/data/lumbribase.fasta').text
