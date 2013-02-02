<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
 <g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editGenome" params="${[gid:params.id]}">Edit genome data</g:link> > Edited genome data
<h1>Edited genome data:</h1>

<p>Your species data has been edited successfully

</body>
</html>
