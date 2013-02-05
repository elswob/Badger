<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editSpecies" params="${[Gid:anno.filedata.genome.meta.id]}"><i>${anno.filedata.genome.meta.genus} ${anno.filedata.genome.meta.species}</i></g:link> > <g:link action="editGenome" params="${[gid:anno.filedata.genome.id]}">Edit genome</g:link> > <g:link action="editAnno" params="${[gid:anno.id]}">Edit annotation</g:link> > Edited annotation</div>
 <h1>Edited annotation file data:</h1>

<p>Your annotation file data has been edited successfully

</body>
</html>
