<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
	<g:if test = "${error == 'duplicate'}">
		<br><h2>This annotation file name already exists for this file.<br> Please go <a href="previous.html" onClick="history.back();return false;">back</a> and edit the form.</h2>
	</g:if>
	<g:if test = "${error == 'no file'}">
		<br><h2>The file <b>${file}</b> does not exist, please go <a href="previous.html" onClick="history.back();return false;">back</a> and edit the form.</h2>
	</g:if>
	<g:else>
		<h1>You have successfully added the following annotation data:</h1>
		<table>
			<tr><td><b>File:</b></td><td>${annoMap.anno_file}</td></tr>
			<tr><td><b>Source:</b></td><td>${annoMap.source}</td></tr>
		</table>
		<p>Now <g:link action="addAnno">add</g:link> some more annotations</p> 
	</g:else>	
</body>
</html>
