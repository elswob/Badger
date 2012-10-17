<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} news</title>
  <parameter name="home" value="selected"></parameter>
  </head>

  <body>

<g:if test = "${newsDate == null}">
	<h1>News data failed to be added</h1>
	<p>The date '${params.newsDate}' is in the wrong format, it needs to be dd/mm/yyyy, e.g. 09/03/12</p>
</g:if>
<g:elseif test = "${newsTitle == null}">
	<h1>News item failed to be added</h1>
	<p>There was no title</p>
</g:elseif>
<g:elseif test = "${newsData == null}">
	<h1>News item failed to be added</h1>
	<p>There was no data</p>
</g:elseif>
<g:else>

	<h1>You have successfully added the following news item:</h1>

	<br><g:formatDate format="dd/MM/yyyy" date="${newsDate}"/><br>
	<br>${newsTitle}<br>
	<br>${newsData}<br>
</g:else>
	
</body>
</html>
