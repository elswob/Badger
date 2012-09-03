<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} news</title>
  <parameter name="home" value="selected"></parameter>
  </head>

  <body>
   
<h1>Delete news item:</h1>
<g:form action="deletedNews" controller="home">
<p>Are you sure you want to delete the following news item?</p><br>
<h2>Title</h2>
<g:textArea value="${newsData.titleString[0]}" name="newsTitle" style="width: 98%; height: 20px; border: 3px solid #cccccc; padding: 5px;"/><br>
<h2>Details</h2>
<g:textArea value="${newsData.dataString[0]}" name="newsData" style="width: 98%; height: 50px; border: 3px solid #cccccc; padding: 5px;"/><br>
<h2>Date, e.g. dd/mm/yyyy (leave blank if using today's date)</h2>
<g:textField value="${formatDate(format:'dd/MM/yyyy',date:newsData.dateString[0])}" name="newsDate" style="border: 3px solid #cccccc; padding: 5px;"/><br><br>

<input class="mybuttons" type="button" value="Delete" onclick="submit()" >

</g:form>
</body>
</html>
