<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} | News</title>
  <parameter name="home" value="selected"></parameter>
  </head>

  <body>
   
<h1><i>${grailsApplication.config.projectID}</i> news:</h1>
<table>
<g:each var="res" in="${newsData}">
	<g:if test = "${res.titleString == highlight}">
		<tr style="background: #E1F2B6">
	</g:if>
	<g:else>
		<tr>
	</g:else>
		<td>	
			<h3 id ="${res.dateString}"><b><g:formatDate format="yyyy MMM d" date="${res.dateString}"/></b>: ${res.titleString}</h3>
			<p>${res.dataString}
		</td>
	</tr>
	
</g:each>
</table>

</body>
</html>
