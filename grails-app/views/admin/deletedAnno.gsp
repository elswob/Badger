<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
   <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editSpecies" params="${[Gid:del.gff.genome.meta.id]}"><i>${del.gff.genome.meta.genus} ${del.gff.genome.meta.species}</i></g:link> > <g:link action="editGenome" params="${[gid:del.gff.genome.id]}">Edit genome</g:link> > Delete annotation > Deleted annotation</div>

<h1>The following annotation file and all dependencies are being deleted</h1>

<h2>${del.source} ${del.file}</h2>

</body>
</html>
