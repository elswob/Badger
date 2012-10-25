<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} news</title>
  <parameter name="home" value="selected"></parameter>
  </head>

  <body>
   
<h1>Add news to the <i>${grailsApplication.config.projectID}</i> project:</h1>

<g:form action="addedNews" controller="admin">
<h2>Title</h2>
<g:textArea name="newsTitle" style="width: 98%; height: 20px; border: 3px solid #cccccc; padding: 5px;"/><br>
<h2>Details</h2>
<g:textArea name="newsData" style="width: 98%; height: 50px; border: 3px solid #cccccc; padding: 5px;"/><br>
<h2>Date, e.g. dd/mm/yyyy (leave blank if using today's date)</h2>
<g:textField name="newsDate" style="border: 3px solid #cccccc; padding: 5px;"/><br><br>

<input class="mybuttons" type="button" value="Add news" onclick="submit()" >

</g:form>
</body>
</html>
