import groovy.sql.Sql

class BootStrap {
    def springSecurityService
    javax.sql.DataSource dataSource
    
    def init = { servletContext ->
        
        environments {
            production {
                servletContext.setAttribute("env", "prod")
                //create some indexes
            	def sql = new Sql(dataSource)

                servletContext.setAttribute("env", "dev")
                def userRole = Security.SecRole.findByAuthority('ROLE_USER') ?: new Security.SecRole(authority: 'ROLE_USER').save(failOnError: true)
                def adminRole = Security.SecRole.findByAuthority('ROLE_ADMIN') ?: new Security.SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)     
                
                def adminUser = Security.SecUser.findByUsername('admin') ?: new Security.SecUser( username: 'admin', password: 'badger',enabled: true).save(failOnError: true)
                if (!adminUser.authorities.contains(adminRole)) {
                	Security.SecUserSecRole.create adminUser, adminRole
                }
            }
            development {
            	def sql = new Sql(dataSource)
             
                servletContext.setAttribute("env", "dev")
                def userRole = Security.SecRole.findByAuthority('ROLE_USER') ?: new Security.SecRole(authority: 'ROLE_USER').save(failOnError: true)
                def adminRole = Security.SecRole.findByAuthority('ROLE_ADMIN') ?: new Security.SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)     
                
                def adminUser = Security.SecUser.findByUsername('admin') ?: new Security.SecUser( username: 'admin', password: 'badger',enabled: true).save(failOnError: true)
                if (!adminUser.authorities.contains(adminRole)) {
                	Security.SecUserSecRole.create adminUser, adminRole
                }
                //add some first data to news
                //def firstNews = new badger.News(titleString: 'Database and site created', dataString: 'The database and web site were created', dateString: new Date(),enabled: true).save(failOnError: true)
            }
        }       
    }
    def destroy = {
    }
}
