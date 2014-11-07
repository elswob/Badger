<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} | Download</title>
  <parameter name="download" value="selected"></parameter>
  </head>
  <body>
  <div class="introjs-home-download">
  <div class="bread"><g:link action="">Home</g:link> > Download</div>
  <div data-intro='Download the raw data files. Downloads may be restricted depending on the settings of the project.' data-step='1'></div>
    <table class="compact">    
    <% def sp = ""%>
    <sec:ifNotLoggedIn>
    	<g:each var="res" in="${files}">
    		<g:if test = "${res.download == 'pub' && res.loaded == true}">
    		<% if (sp == ""){ 
    			println "<tr><td colspan=2><h1>${res.genus} ${res.species}</h1></td></tr><tr><td width=120><a href = \"/search/species_v?Sid=${res.sid}\"><img src=\"${resource(dir: 'images', file: res.image_file)}\" width=\"120\" style=\"float:left;\"/></a></td><td><table><tr><td><b>Data type</b></td><td><b>Version</b></td><td><b>Download</b></td><td><b>Link</b></td></tr>"
    		}else if (sp != res.species){ 
    			println "</table></td><tr><td colspan=2><h1>${res.genus} ${res.species}</h1></td></tr>"
    			println "<tr><td width=120><a href = \"/search/species_v?Sid=${res.sid}\"><img src=\"${resource(dir: 'images', file: res.image_file)}\" width=\"120\" style=\"float:left;\"/></a></td><td><table><tr><td><b>Data type</b></td><td><b>Version</b></td><td><b>Download</b></td><td><b>Link</b></td></tr>" 
    		}%>
    			<g:if test = "${res.description != 'fake'}">
					<tr>
						<td>${res.file_type}</td><td>${res.file_version}</td>
						<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: res.file_name]}">${res.file_name}</g:link></td>
						<g:if test="${res.url}"><td><a href=${res.url}>${res.url}</a></td></g:if><g:else><td>N/A</td></g:else>
					</tr>
					<g:if test="${res.file_type == 'Genes'}">
						<% if (gffAnno."${res.file_name}" == true){ %>
							<tr><td>Genes (annotated file)</td><td>${res.file_version}</td>
							<td><g:link controller="FileDownload" action="zip_anno_download" params="${[fileName: res.file_name]}">${res.file_name}.anno.tsv</g:link></td>
						<%}%>
						<g:if test="${res.url}"><td><a href=${res.url}>${res.url}</a></td></g:if><g:else><td>N/A</td></g:else>
					</tr>
					</g:if>
				</g:if>
			</g:if>
			<% sp = res.species %>
    	</g:each>
    	</table>
    </sec:ifNotLoggedIn>
    <sec:ifLoggedIn>
    	<g:each var="res" in="${files}">
    		<g:if test = "${res.loaded == true}">
				<% if (sp == ""){ 
    			println "<tr><td colspan=2><h1>${res.genus} ${res.species}</h1></td></tr><tr><td width=120><br><a href = \"/search/species_search?Gid=${res.id}\"><img src=\"${resource(dir: 'images', file: res.image_file)}\" width=\"120\" style=\"float:left;\"/></a></td><td><table><tr><td><b>Data type</b></td><td><b>Version</b></td><td><b>Download</b></td><td><b>Link</b></td></tr>"
    		}else if (sp != res.species){ 
    			println "</table></td><tr><td colspan=2><h1>${res.genus} ${res.species}</h1></td></tr>"
    			println "<tr><td width=120><a href = \"/search/species_search?Gid=${res.id}\"><img src=\"${resource(dir: 'images', file: res.image_file)}\" width=\"120\" style=\"float:left;\"/></a></td><td><table><tr><td><b>Data type</b></td><td><b>Version</b></td><td><b>Download</b></td><td><b>Link</b></td></tr>" 
    		}%>
    			<g:if test = "${res.description != 'fake'}">
					<tr>
						<td>${res.file_type}</td><td>${res.file_version}</td>
						<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: res.file_name]}">${res.file_name}</g:link></td>
						<g:if test="${res.url}"><td><a href=${res.url}>${res.url}</a></td></g:if><g:else><td>N/A</td></g:else>
					</tr>
					<g:if test="${res.file_type == 'Genes'}">
						<% if (gffAnno."${res.file_name}" == true){ %>
							<tr><td>Genes (annotated file)</td><td>${res.file_version}</td>
							<td><g:link controller="FileDownload" action="zip_anno_download" params="${[fileName: res.file_name]}">${res.file_name}.anno.tsv</g:link></td>
						<%}%>
						<g:if test="${res.url}"><td><a href=${res.url}>${res.url}</a></td></g:if><g:else><td>N/A</td></g:else>
					</tr>
					</g:if>
				</g:if>
			</g:if>
			<% sp = res.species %>
    	</g:each>
    	</table>
    </sec:ifLoggedIn>
    </table>
  </body>
</html>