<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	  <meta name='layout' content='main'/>
	  <title>${grailsApplication.config.projectID} home</title>
	  <parameter name="home" value="selected"></parameter>
  </head>

  <body>
	  <sec:ifNotLoggedIn>
	  	<h1>Welcome to the home of the <i>${grailsApplication.config.projectID}</i> genome project.</h1>    
	  </sec:ifNotLoggedIn>	
	  <sec:ifLoggedIn>
	  <h1>Welcome <sec:username /></h1>
	    <p>
	    This site represents the main portal to the <i>${grailsApplication.config.projectID}</i> genome project. All assembly and annotation data 
	    will be accessible from here via the links above. 
	</sec:ifLoggedIn>
  </body>
</html>
