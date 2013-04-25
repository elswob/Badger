<g:if test = "${genes}" >
	<input type="hidden" name="Gid" value="${genome}">
	<g:if test="${genes.description[0] != 'fake'}">	
		<table>
		<g:if test = "${genes.size == 1}">
			<h1>Annotation version</h1>
			<tr>
				<td><input type="radio" name="GFFid" id="${genes.id}" value="${genes.id[0]}" checked/></td>
				<td><label for="${genes.id}"><b>${genes.file_version[0]}</b></label></td><td><label for="${genes.id}">${genes.description[0]}</label></td>
			</tr>
		</g:if>
		<g:else>
			<h1>Choose an annotation version:</h1>
			<g:each var="g" in="${genes}">
				<tr>
					<td><input type="radio" name="GFFid" id="${g.id}" value="${g.id}" checked/></td>
					<td><label for="${g.id}"><b>${g.file_version}</b></label></td><td><label for="${g.id}">${g.description}</label></td>
				</tr>
			</g:each>	
		</g:else>
		</table>
	</g:if>
	<g:else>
		<h1>Annotation version</h1>
		<h3>There is no annotation file for this genome, just transcript sequences linking to an external database</h3>
		<input type="hidden" name="GFFid" id="${genes.id}" value="${genes.id[0]}">
	</g:else>
</g:if>
<g:else>
	<h1> No GFF3 files are available for this genome</h1>
	<input type="hidden" name="GFFid" value="0" checked/>
	<input type="hidden" name="Gid" value="${genome}">
</g:else>