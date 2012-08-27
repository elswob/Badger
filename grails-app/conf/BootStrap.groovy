class BootStrap {
    def springSecurityService
    javax.sql.DataSource dataSource
    
    def init = { servletContext ->
        
        environments {
            production {
                servletContext.setAttribute("env", "prod")
            }
            test {
                servletContext.setAttribute("env", "test")
            }
            development {
                servletContext.setAttribute("env", "dev")
                def userRole = Security.SecRole.findByAuthority('ROLE_USER') ?: new Security.SecRole(authority: 'ROLE_USER').save(failOnError: true)
                def adminRole = Security.SecRole.findByAuthority('ROLE_ADMIN') ?: new Security.SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)     
            }
        }       
    }
    def destroy = {
    }
}
