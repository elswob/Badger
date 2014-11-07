<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
   <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > Added species </div>
	<g:if test = "${error == 'duplicate'}">
		<br><h2>This species already exists.<br> Please go <a href="previous.html" onClick="history.back();return false;">back</a> and edit the data set.</h2>
	</g:if>
	<g:else>`
		<h1>You have successfully added the following species:</h1>
		<table>
		<tr><td><b>Species:</b></td><td><i>${dataMap.genus} ${dataMap.species}</i></td></tr>
		<tr><td><b>Description:</b></td><td>${dataMap.description}</td></tr>
		</table>
		<!--p>Go <g:link action="home">back</g:link> and add or edit data</p-->
		<h2>Now <g:link action="editSpecies" params="${[Gid:Gid]}"><b>add</b></g:link> a genome for this species</h2> 
	</g:else>	
</body>
</html>
