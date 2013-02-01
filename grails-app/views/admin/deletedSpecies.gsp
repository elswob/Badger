<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
 <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > Delete species > Deleted species</div>  
<h1>The following species and all dependencies are being deleted</h1>

<h2>${params.genus} ${params.species}</h2>

</body>
</html>
