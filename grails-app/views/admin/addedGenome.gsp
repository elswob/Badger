<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
   <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editSpecies" params="${[Gid:genome.meta.id]}"><i>${genome.meta.genus} ${genome.meta.species}</i></g:link> > Added genome </div>
	<g:if test = "${error == 'duplicate'}">
		<br><h2>This genome already exists.<br> Please go <a href="previous.html" onClick="history.back();return false;">back</a> and edit the data set.</h2>
	</g:if>
	<g:else>
		<h1>You have successfully added the following genome for <i>${genome.meta.genus} ${genome.meta.species}</i>:</h1>
		<table>
		<tr><td><b>Genome version:</b></td><td>${dataMap.gversion}</td></tr>
		<tr><td><b>Date:</b></td><td>${dataMap.dateString}</td></tr>
		</table>
		<!--p>Go <g:link action="home">back</g:link> and add or edit data</p-->
		<h2>Now <g:link action="editGenome" params="${[gid:genome.id]}"><b>add</b></g:link> some data files to this genome</h2> 
	</g:else>	
</body>
</html>
