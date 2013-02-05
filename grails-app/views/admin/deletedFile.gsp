<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editSpecies" params="${[Gid:genome.meta.id]}"><i>${genome.meta.genus} ${genome.meta.species}</i></g:link> > <g:link action="editGenome" params="${[gid:genome.id]}">Edit genome</g:link> > Deleted file </div>
  
<h1>The following data file and all dependencies are being deleted</h1>

<h2>${dir} / ${name}</h2>

</body>
</html>
