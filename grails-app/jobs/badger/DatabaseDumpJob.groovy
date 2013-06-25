package badger

class DatabaseDumpJob {
	def dumpService
	def grailsApplication
    static triggers = {
      //simple repeatInterval: 5000l // execute job once in 5 seconds
      //run once a week (0=seconds, 0=minutes, 1=hour[01:00], ?=any day of month, * = everyday month, * = everyday)
      cron name: 'dumpTrigger', cronExpression: "0 0 * ? * *" 
    }

    def execute(){
    	def today = new Date()
		
		def fileMap = [:]
		new File("data/db_dump").eachFileRecurse{file->
			fileMap."${file}" = new Date(file.lastModified()).format('EEE MMM dd hh:mm:ss a yyyy')
		}
		println "There are "+fileMap.size()+" database backups"
		println "The limit is set to "+grailsApplication.config.d.number.trim()
		def fileMapSort = fileMap.sort {a, b -> b.value <=> a.value}
		def oldest = ""
		fileMapSort.each{
			//println it
			oldest = it.key
		}
		if (fileMap.size() >= grailsApplication.config.d.number.trim().toInteger()){
			//delete the oldest file
			println "The maximum number of backups is reached, deleting "+oldest+" ..."
			def del = "rm "+oldest
			del.execute()
		}
		def fileDate = String.format('%tY_%<tm_%<td:%<tH:%<tM:%<tS', today)
		
    	println "Dumping database..."
    	def dbString = grailsApplication.config.dataSource.url.trim()
    	def matcher
    	def db
    	if ((matcher = dbString =~ /jdbc:postgresql:\/\/.*?\/(.*)$/)){
    		db = matcher[0][1]
		}
		String[] comm = ["pg_dump", "-a", "${db}","-f", "data/db_dump/${db}_${fileDate}.pgsql"]
		ProcessBuilder dumpProcess = new ProcessBuilder(comm)  
		dumpProcess.redirectErrorStream(true)
        Process p = dumpProcess.start()
		p.waitFor()
		//println "Error = "+p.text
    }
}
