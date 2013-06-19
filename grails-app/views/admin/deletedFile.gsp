<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editSpecies" params="${[Gid:del.genome.meta.id]}"><i>${del.genome.meta.genus} ${del.genome.meta.species}</i></g:link> > <g:link action="editGenome" params="${[gid:del.genome.id]}">Edit genome</g:link> > Deleted file </div>
  
<h1>The following data file and all dependencies are being deleted</h1>

<h2>${del.dir} / ${del.name}</h2>

</body>
</html>
