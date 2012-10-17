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
<b>Are you sure you want to delete the following news item?</b><br><br>
<h2>Title:</h2>
${newsData.titleString[0]}<br><br>
<h2>Details:</h2>
${newsData.dataString[0]}<br><br>
<h2>Date:</h2>
<g:formatDate format="yyyy MMM d" date="${newsData.dateString[0]}"/><br><br>
<input type="hidden" value="${newsData.titleString[0]}" name="newsTitle">
<input class="mybuttons" type="button" value="Delete" onclick="submit()" >

</g:form>
</body>
</html>
