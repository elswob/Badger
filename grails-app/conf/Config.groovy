// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

grails.config.locations = ["file:badger.config.properties"]
println "Using external configuration file "+grails.config.locations	

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']


// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
    //display SQL logging
    //trace 'org.hibernate.SQL'

}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'Security.SecUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'Security.SecUserSecRole'
grails.plugins.springsecurity.authority.className = 'Security.SecRole'
grails.plugins.springsecurity.auth.loginFormUrl = '/'
//grails.plugins.springsecurity.failureHandler.defaultFailureUrl = '/'

grails.app.context = "/"


// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'Security.SecUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'Security.SecUserSecRole'
grails.plugins.springsecurity.authority.className = 'Security.SecRole'

grails.cache.config = {
   cache {
      name 'stats_cache'
   }
   cache {
      name 'species_cache'
   }
   cache {
      name 'm_cache'
   }
   cache {
      name 'cluster_cache'
   }
   cache {
      name 'ortho_cache'
   }
   cache {
      name 'all_cache'
   }
}
// Added by the JQuery Validation UI plugin:
jqueryValidationUi {
	errorClass = 'error'
	validClass = 'valid'
	onsubmit = true
	renderErrorsOnTop = false
	
	qTip {
		packed = true
	  classes = 'ui-tooltip-red ui-tooltip-shadow ui-tooltip-rounded'  
	}
	
	/*
	  Grails constraints to JQuery Validation rules mapping for client side validation.
	  Constraint not found in the ConstraintsMap will trigger remote AJAX validation.
	*/
	StringConstraintsMap = [
		blank:'required', // inverse: blank=false, required=true
		creditCard:'creditcard',
		email:'email',
		inList:'inList',
		minSize:'minlength',
		maxSize:'maxlength',
		size:'rangelength',
		matches:'matches',
		notEqual:'notEqual',
		url:'url',
		nullable:'required',
		unique:'unique',
		validator:'validator'
	]
	
	// Long, Integer, Short, Float, Double, BigInteger, BigDecimal
	NumberConstraintsMap = [
		min:'min',
		max:'max',
		range:'range',
		notEqual:'notEqual',
		nullable:'required',
		inList:'inList',
		unique:'unique',
		validator:'validator'
	]
	
	CollectionConstraintsMap = [
		minSize:'minlength',
		maxSize:'maxlength',
		size:'rangelength',
		nullable:'required',
		validator:'validator'
	]
	
	DateConstraintsMap = [
		min:'minDate',
		max:'maxDate',
		range:'rangeDate',
		notEqual:'notEqual',
		nullable:'required',
		inList:'inList',
		unique:'unique',
		validator:'validator'
	]
	
	ObjectConstraintsMap = [
		nullable:'required',
		validator:'validator'
	]
	
	CustomConstraintsMap = [
		phone:'true', // International phone number validation
		phoneUS:'true',
		alphanumeric:'true',
		letterswithbasicpunc:'true',
    lettersonly:'true'
	]	
}

ckeditor {
	config = "/js/myckconfig.js"
        skipAllowedItemsCheck = false
	defaultFileBrowser = "ofm"
	upload {
		basedir = "/uploads/"
	        overwrite = false
	        link {
	            browser = true
	            upload = false
	            allowed = []
	            denied = ['html', 'htm', 'php', 'php2', 'php3', 'php4', 'php5',
	                      'phtml', 'pwml', 'inc', 'asp', 'aspx', 'ascx', 'jsp',
	                      'cfm', 'cfc', 'pl', 'bat', 'exe', 'com', 'dll', 'vbs', 'js', 'reg',
	                      'cgi', 'htaccess', 'asis', 'sh', 'shtml', 'shtm', 'phtm']
	        }
	        image {
	            browser = true
	            upload = true
	            allowed = ['jpg', 'gif', 'jpeg', 'png']
	            denied = []
	        }
	        flash {
	            browser = false
	            upload = false
	            allowed = ['swf']
	            denied = []
	        }
	}
}
