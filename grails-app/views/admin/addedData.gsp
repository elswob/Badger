<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} news</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>

	<h1>You have successfully added the following species:</h1>

	<br><i>${dataMap.genus} ${dataMap.species}</i><br>
	<br>${dataMap.data_version}<br>
	<br>${dataMap.description}<br>
	<br>
	<p>Go <g:link action="home">back</g:link> and add or edit data</p> 
	
</body>
</html>
