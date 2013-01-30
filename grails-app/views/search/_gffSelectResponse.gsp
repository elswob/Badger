<g:if test = "${genes}">
	<g:form action="species_search" method="get">
		<input type="hidden" name="Gid" value="${genome}">
		<table>
		<g:if test = "${genes.size == 1}">
			<h1>GFF3 Version</h1>
			<tr>
				<td><input type="radio" name="GFFid" id="${genes.id}" value="${genes.id[0]}" checked/></td>
				<td><label for="${genes.id}"><b>${genes.file_version[0]}</b></label></td><td><label for="${genes.id}">${genes.description[0]}</label></td>
			</tr>
		</g:if>
		<g:else>
			<h1>Choose a GFF3 version</h1>
			<g:each var="g" in="${genes}">
				<tr>
					<td><input type="radio" name="GFFid" id="${g.id}" value="${g.id}" checked/></td>
					<td><label for="${g.id}"><b>${g.file_version}</b></label></td><td><label for="${g.id}">${g.description}</label></td>
				</tr>
			</g:each>	
		</g:else>
		</table>
		<div class="toHide" id="buttons">
			<input class="mybuttons" type="button" value="Select" id="process" onclick="submit()" >
		</div>
	</g:form>
</g:if>
<g:else>
	<h1> No GFF3 files are available for this genome</h1>
</g:else>