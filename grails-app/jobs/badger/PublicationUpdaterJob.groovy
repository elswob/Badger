package badger

class PublicationUpdaterJob {
	def pubService
    static triggers = {
    	//run once a week (0=seconds, 0=minutes, 0=hour[midnight], ?=any day of month, * = everyday month, 1 = on Sunday)
    	cron name: 'myTrigger', cronExpression: "0 0 0 ? * 1"  
    }
    def execute(){
    	def today = new Date()
		println today
    	println "Updating publications!"
    	pubService.runPub()
    }
}
