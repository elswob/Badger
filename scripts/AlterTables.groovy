package badger

import groovy.sql.Sql

alterTables()
def alterTables(){
	def dataSource = ctx.getBean("dataSource")
	def sql = new Sql(dataSource)
	println "Checking tables..."
	def intersql = "select column_name from information_schema.columns where table_name = 'gene_interpro' and column_name = 'textsearchable_index_col';";
	def inter = sql.rows(intersql)
	if (!inter){
		println "Adding gene interpro column..."
		//gene_inter
        sql.execute("ALTER TABLE gene_interpro ADD COLUMN textsearchable_index_col tsvector;")
    	sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON gene_interpro FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
        sql.execute("CREATE INDEX geneinterprosearch_idx ON gene_interpro USING gin(textsearchable_index_col);")
	}
	def annosql = "select column_name from information_schema.columns where table_name = 'gene_anno' and column_name = 'textsearchable_index_col';";
	def anno = sql.rows(annosql)
	if (!anno){
		println "Adding gene annotation column..."
		sql.execute("ALTER TABLE gene_anno ADD COLUMN textsearchable_index_col tsvector;")
		sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON gene_anno FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
		sql.execute("CREATE INDEX geneannosearch_idx ON gene_anno USING gin(textsearchable_index_col);")
	}
    def blastsql = "select column_name from information_schema.columns where table_name = 'gene_blast' and column_name = 'textsearchable_index_col';";
	def blast = sql.rows(blastsql)        
	if (!blast){
		println "Adding gene blast column..."
		sql.execute("ALTER TABLE gene_blast ADD COLUMN textsearchable_index_col tsvector;")
		sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON gene_blast FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', descr);")
		sql.execute("CREATE INDEX geneblastsearch_idx ON gene_blast USING gin(textsearchable_index_col);")
	}
	def pubsql = "select column_name from information_schema.columns where table_name = 'publication' and column_name = 'textsearchable_index_col';";
	def pub = sql.rows(pubsql)  
	if (!pub){
		println "Adding publication column..."
		sql.execute("ALTER TABLE publication ADD COLUMN textsearchable_index_col tsvector;")
		sql.execute("CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE ON publication FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(textsearchable_index_col, 'pg_catalog.english', title, abstract_text);")
		sql.execute("CREATE INDEX pubsearch_idx ON publication USING gin(textsearchable_index_col);")
	}
}
