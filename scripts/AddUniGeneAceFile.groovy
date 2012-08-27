package GDB

import groovy.sql.Sql
//
//add the ace file info

inFile = new File('data/sequence.fasta.cap.ace').text
addAceData(inFile, 'UniGeneInfo')

def addAceData(aceFile, table) {
	def sql = new Sql(dataSource)
	boolean inReadString = false
    boolean inContigString = false
    boolean inQualityString = false
    def contigSeq = ""
    def readSeq = ""
    int readCount
	aceFile.eachLine { line ->
      if (line.startsWith(/CO /)) {
        if (readCount > 0){
        	//get rid of * in sequence
            contigSeq = contigSeq.replaceAll(/\*/, '')
            float cov = readCount / contigSeq.length()
            println "Coverage = " + readCount + " / " + contigSeq.length() + "*100 = " + sprintf("%.2f",cov)          
            def sqlUpdate = "update unigene_info set coverage = '" +sprintf("%.2f",cov)+ "' where sequence = '" +contigSeq+"';"
            sql.executeUpdate(sqlUpdate)
            println sqlUpdate
            readCount = 0
        }           
          contigSeq = ""
          inContigString = true
          println line
      }

      else if (line.startsWith(/BQ/)) {
        inContigString = false;
      }
      else if (line.startsWith(/AF /)) {
         
      }
      else if (line.startsWith(/RD /)) {
         readSeq = ""
         inReadString = true
      }
      else if (line.startsWith(/QA /)) {
         inReadString = false
         //println readCount
      }
      else if (inContigString){
          contigSeq += line
      }
      else if (inReadString){
          readSeq += line
          readCount = readCount + line.length()
      }
      //else{
      //  println "something else"
      //}
    }
}

//get the singlets
def sqlUpdateEmpty = "update unigene_info set coverage = 1 where coverage = 0;"
sql.executeUpdate(sqlUpdate)
