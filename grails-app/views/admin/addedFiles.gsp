<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
   <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > Added data set </div>
	<g:if test = "${error == 'no file'}">
		<br><h2>The file <b>${file}</b> does not exist, please go <a href="previous.html" onClick="history.back();return false;">back</a> and edit the form.</h2>
	</g:if>
	<g:elseif test= "${error == 'file exists'}">
		<br><h2>The file <b>${file}</b> is already present in the database for this genome, please go <a href="previous.html" onClick="history.back();return false;">back</a> and edit the form.</h2>
	</g:elseif>
	<g:else>
		<h1>You have successfully added the file data:</h1>
		<p>Now <g:link action="addAnno" params="${[Gid:Gid]}">add</g:link> some annotations</p> 
	</g:else>	
</body>
</html>
