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
			dbCreate = "create" // one of 'create', 'create-drop','update'
            loggingSql = true
		}
	}
	production {
		dataSource {
			dbCreate = "update"
		}
	}
	data_load {
		dataSource {
			dbCreate = "validate"
		}
	}
}