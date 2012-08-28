package GDB

class PublicationUpdaterJob {
	def pubService
    static triggers = {
    	//run once a week (0=seconds, 0=minutes, 0=hour[midnight], ?=any day of month, * = everyday month, 1 = on Sunday)
    	cron name: 'myTrigger', cronExpression: "0 0 0 ? * 1"  
    }
    def execute(){
    	print "Updating publications!"
    	pubService.getPub()
    }
}
