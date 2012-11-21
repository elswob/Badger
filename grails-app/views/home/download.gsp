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
    <table class="compact">    
    <!--tr><td><h1>Domain</h1></td><td><h1>Species</h1></td><td><h1>Data</h1></td><td><h1>Version</h1></td><td><h1>Download</h1></td></tr-->
    <% def sp = ""%>
    <sec:ifNotLoggedIn>
    	<g:each var="res" in="${files}">
    		<g:if test = "${res.blast == 'pub'}">
    		<% if (sp == ""){ 
    			println "<tr><td colspan=2><h1>${res.meta.genus} ${res.meta.species}</h1></td></tr><tr><td width=120><br><a href = \"/search/species_search?Gid=${res.meta.id}\"><img src=\"${resource(dir: 'images', file: res.meta.image_file)}\" width=\"120\" style=\"float:left;\"/></a></td><td><table>"
    		}else if (sp != res.meta.species){ 
    			println "</table></td><tr><td colspan=2><h1>${res.meta.genus} ${res.meta.species}</h1></td></tr>"
    			println "<tr><td width=120><a href = \"/search/species_search?Gid=${res.meta.id}\"><img src=\"${resource(dir: 'images', file: res.meta.image_file)}\" width=\"120\" style=\"float:left;\"/></a></td><td><table>" 
    		}%>
				<tr>
					<td>${res.file_type}</td><td>Version ${res.file_version}</td>
					<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: res.file_name]}">${res.file_name}</g:link></td>
				</tr>
			</g:if>
			<% sp = res.meta.species %>
    	</g:each>
    	</table>
    </sec:ifNotLoggedIn>
    <sec:ifLoggedIn>
    	<g:each var="res" in="${files}">
    		<% if (sp == ""){ 
    			println "<tr><td width=120><a href = \"/search/species_search?Gid=${res.meta.id}\"><img src=\"${resource(dir: 'images', file: res.meta.image_file)}\" width=\"120\" style=\"float:left;\"/></a></td><td><table>"
    		}else if (sp != res.meta.species){ 
    			println "</table></td><tr><td width=120><a href = \"/search/species_search?Gid=${res.meta.id}\"><img src=\"${resource(dir: 'images', file: res.meta.image_file)}\" width=\"120\" style=\"float:left;\"/></a></td><td><table>" 
    		}%>
    		<tr>
    			<td>${res.file_type}</td><td>Version ${res.file_version}</td>
    			<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: res.file_name]}">${res.file_name}</g:link></td>
    		</tr>
    		<% sp = res.meta.species %>
    	</g:each>
    	</table>
    </sec:ifLoggedIn>
    </table>
  </body>
</html>