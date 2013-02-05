<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
	<div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editSpecies" params="${[Gid:file.genome.meta.id]}"><i>${file.genome.meta.genus} ${file.genome.meta.species}</i></g:link> > <g:link action="editGenome" params="${[gid:file.genome.id]}">Edit genome</g:link> > <g:link action="addAnno" params="${[gid:file.id]}">Add annotation</g:link> > Added annotation file </div>
	
	<g:if test = "${error == 'duplicate'}">
		<br><h2>This annotation file name already exists for this GFF file.<br> Please go <a href="previous.html" onClick="history.back();return false;">back</a> and edit the form or rename the annotation file.</h2>
	</g:if>
	<g:elseif test = "${error == 'no file'}">
		<br><h2>The file <b>${fileLoc}</b> does not exist, please go <a href="previous.html" onClick="history.back();return false;">back</a> and edit the form or add the file to the correct directory.</h2>
	</g:elseif>
	<g:else>
		<h1>You have successfully added the following annotation data:</h1>
		<table>
			<tr><td><b>File:</b></td><td>${annoMap.anno_file}</td></tr>
			<tr><td><b>Source:</b></td><td>${annoMap.source}</td></tr>
		</table>
		<p>Now <g:link action="addAnno" params="${[gid:file.id]}">add</g:link> some more annotations</p> 
	</g:else>	
</body>
</html>
