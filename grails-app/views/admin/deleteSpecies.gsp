<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > Delete species</div>
   
<h1>Delete species:</h1>

<g:form action="deletedSpecies" controller="admin">

<h2>Are you sure you wish to delete the following data set? This will delete all associated files and may take a long time!</h2>

<p><b>Genus</b><font color="red">*</font></p>
<g:textField value="${metaData.genus}" name="genus" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Species</b><font color="red">*</font></p>
<g:textField value="${metaData.species}" name="species" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Description</b><font color="red">*</font></p>
<g:textArea value="${metaData.description}" name="description" style="width: 80%; height: 50px; border: 3px solid #cccccc; padding: 2px;"/><br>	
<p><b>Image file</b></p>
<g:textField value="${metaData.image_file}" name="image_f" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Image source</b></p>
<g:textField value="${metaData.image_source}" name="image_s" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<input type = "hidden" name="Gid" value="${metaData.id}">
<br>
<input class="mybuttons" type="button" value="Delete data set" onclick="submit()" >
<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>

</g:form>
</body>
</html>
