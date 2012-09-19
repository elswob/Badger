<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} download</title>
  <parameter name="download" value="selected"></parameter>
  </head>
  <body>
    <table class="table_basic">    
    <tr><td><h1>Domain</h1></td><td><h1>Data</h1></td><td><h1>Description</h1></td><td><h1>Download</h1></td></tr>
    <sec:ifLoggedIn>
    	<g:each var="res" in="${privDownloadFiles}">
    		<tr>
    			<td>Private</td><td>${res.value[0]}</td><td>${res.value[2]}</td>
    			<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: res.value[1]]}">${res.value[1]}</g:link></td>
    		</tr>
    	</g:each>
    </sec:ifLoggedIn>
    <g:each var="res" in="${pubDownloadFiles}">
    	<tr>
    		<td>Public</td><td>${res.value[0]}</td><td>${res.value[2]}</td>
    		<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: res.value[1]]}">${res.value[1]}</g:link></td>
    	</tr>
    </g:each>
    </table>
  </body>
</html>