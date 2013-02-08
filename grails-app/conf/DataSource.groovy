dataSource {
	pooled = true
	driverClassName = ""
	username = ""
	password = ""
	url = ""
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='org.hibernate.cache.EhCacheProvider'
}

environments {
	development {
		dataSource {
			dbCreate = "create" // one of 'create (drops data)', 'create-drop (drops tables)','update (will create if not done already)'
            loggingSql = true
		}
	}
	production {
		dataSource {
			dbCreate = "update"
		}
	}
}