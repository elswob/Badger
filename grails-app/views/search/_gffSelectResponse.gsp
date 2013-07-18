<g:if test = "${genes}" >
	<input type="hidden" name="Gid" value="${genome}">
	<g:if test="${genes.description[0] != 'fake'}">	
		<table data-intro='Select an annotation version' data-step='2'>
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
		<div class="inline"><h1>View genome</h1> <input data-intro='Then hit Select' data-step='3' class="smallbuttons" type="button" value="Select" id="process" onclick="submit()"></div>
	</g:if>
	<g:else>
		<h1>Annotation version</h1>
		<h3>There is no annotation file for this genome, just transcript sequences linking to an external database</h3>
		<input type="hidden" name="GFFid" id="${genes.id}" value="${genes.id[0]}">
		<p><input data-intro='Then hit Select' data-step='2' class="smallbuttons" type="button" value="Select" id="process" onclick="submit()">
	</g:else>
</g:if>
<g:else>
	<h1> No annotation files are available for this genome</h1>
	<p><input data-intro='Then hit Select' data-step='2' class="smallbuttons" type="button" value="Select" id="process" onclick="submit()">
	<input type="hidden" name="GFFid" value="0" checked/>
	<input type="hidden" name="Gid" value="${genome}">
</g:else>