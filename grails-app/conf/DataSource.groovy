dataSource {
	pooled = false
    driverClassName = "org.hsqldb.jdbcDriver"
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='com.opensymphony.oscache.hibernate.OSCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			url = "jdbc:hsqldb:mem:devDB"
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:hsqldb:mem:testDb"
		}
	}
	production {
		dataSource {
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "itemsdbuser"
            password = "itemsdbpass2009"
            url = "jdbc:mysql://localhost:3306/items?autoReconnect=true"
            dialect = org.hibernate.dialect.MySQL5InnoDBDialect
		}
	}
}