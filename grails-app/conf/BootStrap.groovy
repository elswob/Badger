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
                //sql.execute("CREATE INDEX trans_annotation_idx ON trans_anno USING gin(to_tsvector('english', descr || ' ' || anno_id));")
                //sql.execute("CREATE INDEX gene_annotation_idx ON gene_anno USING gin(to_tsvector('english', descr || ' ' || anno_id));")
                //sql.execute("CREATE INDEX publication_idx ON publication USING gin(to_tsvector('english', abstract_text || ' ' || authors || ' ' || journal || '' || title));")                
                
                //trans_anno
                sql.execute("ALTER TABLE trans_anno ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON trans_anno FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
                sql.execute("CREATE INDEX transsearch_idx ON trans_anno USING gin(textsearchable_index_col);")
                
                //gene_anno
                sql.execute("ALTER TABLE gene_anno ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON gene_anno FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
                sql.execute("CREATE INDEX genesearch_idx ON gene_anno USING gin(textsearchable_index_col);")
               
                //publication
                sql.execute("ALTER TABLE publication ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON publication FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', title, abstract_text);")
                sql.execute("CREATE INDEX pubsearch_idx ON publication USING gin(textsearchable_index_col);")
                
                servletContext.setAttribute("env", "dev")
                def userRole = Security.SecRole.findByAuthority('ROLE_USER') ?: new Security.SecRole(authority: 'ROLE_USER').save(failOnError: true)
                def adminRole = Security.SecRole.findByAuthority('ROLE_ADMIN') ?: new Security.SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)     
                
                def adminUser = Security.SecUser.findByUsername('admin') ?: new Security.SecUser( username: 'admin', password: 'admin',enabled: true).save(failOnError: true)
                def testUser = new Security.SecUser(username: 'elswob',password: 'badger',enabled: true).save(failOnError: true)
                if (!adminUser.authorities.contains(adminRole)) {
                	Security.SecUserSecRole.create adminUser, adminRole
                }
                Security.SecUserSecRole.create testUser, userRole
                //add some first data to news
                def firstNews = new GDB.News(titleString: 'GDB created', dataString: 'The GDB database and web site were created', dateString: new Date(),enabled: true).save(failOnError: true)
            
            }
            test {
                servletContext.setAttribute("env", "test")
            }
            development {
            	//create some indexes
            	def sql = new Sql(dataSource)
                //sql.execute("CREATE INDEX trans_annotation_idx ON trans_anno USING gin(to_tsvector('english', descr || ' ' || anno_id));")
                //sql.execute("CREATE INDEX gene_annotation_idx ON gene_anno USING gin(to_tsvector('english', descr || ' ' || anno_id));")
                //sql.execute("CREATE INDEX publication_idx ON publication USING gin(to_tsvector('english', abstract_text || ' ' || authors || ' ' || journal || '' || title));")                
                
                //trans_anno
                sql.execute("ALTER TABLE trans_anno ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON trans_anno FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
                sql.execute("CREATE INDEX transsearch_idx ON trans_anno USING gin(textsearchable_index_col);")
                
                //gene_anno
                sql.execute("ALTER TABLE gene_anno ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON gene_anno FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
                sql.execute("CREATE INDEX genesearch_idx ON gene_anno USING gin(textsearchable_index_col);")
               
                //publication
                sql.execute("ALTER TABLE publication ADD COLUMN textsearchable_index_col tsvector;")
                sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON publication FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', title, abstract_text);")
                sql.execute("CREATE INDEX pubsearch_idx ON publication USING gin(textsearchable_index_col);")
                
                servletContext.setAttribute("env", "dev")
                def userRole = Security.SecRole.findByAuthority('ROLE_USER') ?: new Security.SecRole(authority: 'ROLE_USER').save(failOnError: true)
                def adminRole = Security.SecRole.findByAuthority('ROLE_ADMIN') ?: new Security.SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)     
                
                def adminUser = Security.SecUser.findByUsername('admin') ?: new Security.SecUser( username: 'admin', password: 'admin',enabled: true).save(failOnError: true)
                def testUser = new Security.SecUser(username: 'elswob',password: 'badger',enabled: true).save(failOnError: true)
                if (!adminUser.authorities.contains(adminRole)) {
                	Security.SecUserSecRole.create adminUser, adminRole
                }
                Security.SecUserSecRole.create testUser, userRole
                //add some first data to news
                def firstNews = new GDB.News(titleString: 'GDB created', dataString: 'The GDB database and web site were created', dateString: new Date(),enabled: true).save(failOnError: true)
            }
        }       
    }
    def destroy = {
    }
}
