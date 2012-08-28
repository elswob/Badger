package GDB
import grails.plugins.springsecurity.Secured
import groovy.time.*
import groovy.sql.Sql

class HomeController {
 def grailsApplication
 javax.sql.DataSource dataSource

 //@Secured(['ROLE_'])
 def index() {
 }
 def blog() {}
 
 def publications = { 
 	 def sql = new Sql(dataSource)
 	 def yearsql = "select count(*),date_part('year',date_string) from publication group by date_part('year',date_string) order by date_part('year',date_string);"
 	 def yearData = sql.rows(yearsql)
 	 return [yearData: yearData]
 }
 def publication_search = {
 	def sql = new Sql(dataSource)
 	//get the year from the bar chart 
 	if (params.year){
 		def yearbefore = params.year-1
 		def yearsql = "select * from publication where date_string between \'01/01/" +params.year+ "\' and \'31/12/"+params.year+"\';"
 		println yearsql
 		def pub_results = sql.rows(yearsql)
 		return [ pub_results: pub_results, searchId: params.year]
 	}else{	
		def pubSearch = "("
		def searchId = params.searchId
		def pubType = params.pubVal
		println "searchId = "+searchId
		println "pubType = "+pubType
		if (pubType){
			if (pubType instanceof String){
				pubSearch += pubType + "~* \'" + searchId + "\'"
			}else{
				def pubSelect = pubType		
				pubSelect.each {
					pubSearch += it + " ~* \'" + searchId + "\'" + " or "
				}
				pubSearch = pubSearch[0..-5]			
			}
			pubSearch += ")"
			def pub_search = "select  * from publication where "+pubSearch+" order by date_string desc;"
			def pub_results = sql.rows(pub_search)
			println pub_search
			println "number of results = "+pub_results.size()
			return [ pub_results: pub_results, searchId: searchId]
			
		}
		if (!pubType){
			return [error: "no_type", searchId: searchId]
		}
	}
 }
 def consortium () {}
 def download () {}
}
