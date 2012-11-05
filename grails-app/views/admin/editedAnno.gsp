<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
 <g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editAnno" params="${[id:params.id]}">Edit annotation file</g:link> > Edited annotation file  
<h1>Edited annotation file data:</h1>

<p>Your file data has been edited successfully

</body>
</html>
