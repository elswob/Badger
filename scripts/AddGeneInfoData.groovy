package GDB

import groovy.sql.Sql
def sql = new Sql(dataSource)
//
//println args[0]
//println "Adding contig data from " + args
def inFile = new File('/Users/Ben/Work/data/lumbribase.fasta').text
