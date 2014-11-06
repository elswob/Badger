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
				
				//url column
				sql.execute("alter table file_data alter column url drop not null;")
				
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
				
				//url column
				sql.execute("alter table file_data alter column url drop not null;")
				
             	//gene_inter
                sql.execute("ALTER TABLE gene_interpro ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON gene_interpro FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
                sql.execute("CREATE INDEX geneinterprosearch_idx ON gene_interpro USING gin(textsearchable_index_col);")
                
                //gene_anno
                sql.execute("ALTER TABLE gene_anno ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON gene_anno FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
                sql.execute("CREATE INDEX geneannosearch_idx ON gene_anno USING gin(textsearchable_index_col);")
               	
               	//gene_blast
                sql.execute("ALTER TABLE gene_blast ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON gene_blast FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
                sql.execute("CREATE INDEX geneblastsearch_idx ON gene_blast USING gin(textsearchable_index_col);")
               
                //publication
                sql.execute("ALTER TABLE publication ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON publication FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', title, abstract_text);")
                sql.execute("CREATE INDEX pubsearch_idx ON publication USING gin(textsearchable_index_col);")
             
                servletContext.setAttribute("env", "dev")
                def userRole = Security.SecRole.findByAuthority('ROLE_USER') ?: new Security.SecRole(authority: 'ROLE_USER').save(failOnError: true)
                def adminRole = Security.SecRole.findByAuthority('ROLE_ADMIN') ?: new Security.SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)     
                
                def adminUser = Security.SecUser.findByUsername('admin') ?: new Security.SecUser( username: 'admin', password: 'badger',enabled: true).save(failOnError: true)
                if (!adminUser.authorities.contains(adminRole)) {
                	Security.SecUserSecRole.create adminUser, adminRole
                }
           }
        }       
    }
    def destroy = {
    }
}
