<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} download</title>
  <parameter name="download" value="selected"></parameter>
  </head>
  <body>
  <div class="bread"><g:link action="">Home</g:link> > Download</div>
    <table class="table_basic">    
    <tr><td><h1>Domain</h1></td><td><h1>Species</h1></td><td><h1>Data</h1></td><td><h1>Version</h1></td><td><h1>Download</h1></td></tr>
    <sec:ifNotLoggedIn>
    	<g:each var="res" in="${files}">
    		<g:if test = "${res.blast == 'pub'}">
				<tr>
					<td>Public</td><td>${res.meta.genus} ${res.meta.species}</td><td>${res.file_type}</td><td>${res.file_version}</td>
					<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: res.file_name]}">${res.file_name}</g:link></td>
				</tr>
			</g:if>
    	</g:each>
    </sec:ifNotLoggedIn>
    <sec:ifLoggedIn>
    	<g:each var="res" in="${files}">
    		<tr>
    			<td>Public</td><td>${res.meta.genus} ${res.meta.species}</td><td>${res.file_type}</td><td>${res.file_version}</td>
    			<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: res.file_name]}">${res.file_name}</g:link></td>
    		</tr>
    	</g:each>
    </sec:ifLoggedIn>
    </table>
  </body>
</html>