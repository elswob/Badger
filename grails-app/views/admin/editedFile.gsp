<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editSpecies" params="${[Gid:fileData.genome.meta.id]}"><i>${fileData.genome.meta.genus} ${fileData.genome.meta.species}</i></g:link> > <g:link action="editGenome" params="${[gid:fileData.genome.id]}">Edit genome</g:link> > <g:link action="editFile" params="${[fid:fileData.id]}">Edit file</g:link> > Edited file</div>
  
<h1>Edited file data:</h1>

<p>Your file data has been edited successfully

</body>
</html>
