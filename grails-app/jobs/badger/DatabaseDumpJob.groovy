package badger

class DatabaseDumpJob {
	def dumpService
	def grailsApplication
    static triggers = {
      //simple repeatInterval: 10000l // execute job once in 5 seconds
      //run once a week (0=seconds, 0=minutes, 1=hour[01:00], ?=any day of month, * = everyday month, * = everyday)
      cron name: 'dumpTrigger', cronExpression: "0 0 1 ? * *" 
    }

    def execute(){
    	def today = new Date()
		
		def dbString = grailsApplication.config.dataSource.url.trim()
    	def matcher
    	def db
    	if ((matcher = dbString =~ /jdbc:postgresql:\/\/.*?\/(.*)$/)){
    		db = matcher[0][1]
		}
		
		def fileMap = [:]
		new File("data/db_dump").eachFileRecurse{file->
			//println "file = "+file
			if ((matcher = file =~ /data\/db_dump\/${db}_/)){
				fileMap."${file}" = new Date(file.lastModified()).format('EEE MMM dd hh:mm:ss a yyyy')
			}
		}
		println "There are "+fileMap.size()+" database backups"
		println "The limit is set to "+grailsApplication.config.d.number.trim()
		def fileMapSort = fileMap.sort {a, b -> b.key <=> a.key}
		def oldest = ""
		def newest = ""
		fileMapSort.each{
			println "sorted = "+it.key+" -> "+it.value
			if (newest == ""){
				newest = it.key
			}
			//println it
			oldest = it.key
		}
		println "newest = "+newest
		println "oldest = "+oldest
		def fileDate = String.format('%tY_%<tm_%<td:%<tH:%<tM:%<tS', today)
		
    	println "Dumping database..."
		String[] comm = ["pg_dump", "-a", "${db}","-f", "data/db_dump/${db}_${fileDate}.pgsql"]
		ProcessBuilder dumpProcess = new ProcessBuilder(comm)  
		dumpProcess.redirectErrorStream(true)
        Process p = dumpProcess.start()
		p.waitFor()
		println "Database dumped. - created data/db_dump/${db}_${fileDate}.pgsql"
		println "Checking to see if anything has changed..."
		String[] check = ["cmp", "${newest}", "data/db_dump/${db}_${fileDate}.pgsql"]
		ProcessBuilder checkProcess = new ProcessBuilder(check)  
		checkProcess.redirectErrorStream(true)
        Process c = checkProcess.start()
		c.waitFor()
		//println "Error = "+c.text
		if (c.text == ""){
			println "No change, not adding new dump"
			def del = "rm data/db_dump/${db}_${fileDate}.pgsql"
			del.execute()
		}else{
			println "Changes have been made, adding new dump"
			if (fileMap.size() >= grailsApplication.config.d.number.trim().toInteger()){
				//delete the oldest file
				println "The maximum number of backups is reached, deleting "+oldest+" ..."
				def del = "rm "+oldest
				del.execute()
			}
		}
    }
}
