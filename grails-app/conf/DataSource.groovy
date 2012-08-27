dataSource {
	pooled = true
	driverClassName = "org.postgresql.Driver"
	username = "ben"
	password = "badger"
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='org.hibernate.cache.EhCacheProvider'
}

environments {
	development {
		dataSource {
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			url = "jdbc:postgresql://127.0.0.1:5432/b_anynana"
            loggingSql=true
		}
	}
	test {
		dataSource {
			dbCreate = "create"
			url = "jdbc:postgresql://127.0.0.1:5432/b_anynana"
			logSql = true 
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:postgresql://127.0.0.1:5432/b_anynana"
		}
	}
}