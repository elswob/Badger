<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
<g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > Edit species data
<h1>Edit species data:</h1>

<g:form action="editedSpecies" controller="admin">

<p><b>Genus</b><font color="red">*</font></p>
<g:textField value="${metaData.genus}" name="genus" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Species</b><font color="red">*</font></p>
<g:textField value="${metaData.species}" name="species" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Description</b><font color="red">*</font></p>
<g:textArea value="${metaData.description}" name="description" style="width: 80%; height: 50px; border: 3px solid #cccccc; padding: 2px;"/><br>	
<p><b>Image file</b></p>
<g:textField value="${metaData.image_file}" name="image_f" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Image source</b></p>
<g:textField value="${metaData.image_source}" name="image_s" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<input type = "hidden" name="id" value="${metaData.id}">
<br>
<input class="mybuttons" type="button" value="Update data set" onclick="submit()" >
</g:form>
<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>

<h1>Edit / delete a genome:</h1>

<table>
<g:if test="${metaData.genome}">
	<g:each var="f" in="${metaData.genome}">
	   <tr><td>
	   <div class="inline">
	   		<g:form action= "editGenome" controller="admin" params="[gid: f.id]" >
	    		<a href="#" onclick="parentNode.submit()" title="Edit file"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
	    	</g:form>  	
	    	<g:form action="deleteGenome" controller="admin" params="[gid: f.id]" >
	    		<a href="#" onclick="parentNode.submit()" title="Delete file"><img src="${resource(dir: 'images', file: 'delete-icon.png')}" width="15px"/></a>
	    	</g:form> 	
	    </div>
      		</td><td>Version ${f.gversion}:</td><td><g:formatDate format="d MMM yyyy" date="${f.dateString}"/></td></tr>
   	   </g:each>
</g:if>
<g:else>
	There are no genomes for this species
</g:else>
</table>

<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>

<g:form action="addedGenome" controller="admin">
<h1>Add a genome for <i>${metaData.genus} ${metaData.species}</i></h1>
<table>
	<tr>
		<td width="45%"><b>Date e.g. dd/mm/yyyy (leave blank for today's date)</b><font color="red">*</font><br>
			<g:textField name="genome_date" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
		</td>
		<td width="50%"><b>Version (a unique ID for the genome)</b><font color="red">*</font><br>
			<g:textField name="genome_version" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
		</td>
	</tr>
</table>	
<p><b>URL of Gbrowse2 instance (if available)</b></p>
<g:textField name="gbrowse" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br><br>
<input type="hidden" name="meta" value="${metaData.id}">	
	<input class="mybuttons" type="button" value="Add new genome" onclick="submit()" >
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
</g:form>


</body>
</html>
