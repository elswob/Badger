<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
<g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > Edit data set   
<h1>Edit data set:</h1>

<g:form action="editedData" controller="admin">

<p><b>Genus</b><font color="red">*</font></p>
<g:textField value="${metaData.genus}" name="genus" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Species</b><font color="red">*</font></p>
<g:textField value="${metaData.species}" name="species" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Description</b><font color="red">*</font></p>
<g:textArea value="${metaData.description}" name="description" style="width: 80%; height: 50px; border: 3px solid #cccccc; padding: 2px;"/><br>	
<p><b>GBrowse link</b></p>
<g:textField value="${metaData.gbrowse}" name="gbrowse" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Image file</b></p>
<g:textField value="${metaData.image_file}" name="image_f" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<p><b>Image source</b></p>
<g:textField value="${metaData.image_source}" name="image_s" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
<input type = "hidden" name="id" value="${metaData.id}">
<br>
<input class="mybuttons" type="button" value="Update data set" onclick="submit()" >
</g:form>
<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>

<h1>Add an annotation</h1>
<div class="bread"><g:link action="addAnno" params="${[Gid:metaData.id]}">Add me!</g:link></div>

<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>

<g:form action="addedFile" controller="admin">
<h1>Add a new data set</h1>
<h2><b>Data files</b></h2>	
	<p><b>Location (directory within data folder)</b><font color="red">*</font></p>
	<g:textField name="dir" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<table width=100%>
		<tr>
			<td width="40%"><b>Transcriptome (FASTA file)</b><br>
				<g:textField name="trans" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><br>
				<g:textField name="trans_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td><b>Coverage</b><br>
				<select name="trans_c">
					<option selected="selected" value="n">No</option>
					<option value="y">Yes</option>
				</select>
			</td>
			<td width="45%"><b>Description</b><br>
				<g:textField name="trans_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td width="40%"><b>Genome (FASTA file)</b><font color="red">*</font><br>
				<g:textField name="genome" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><font color="red">*</font><br>
				<g:textField name="genome_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td><b>Coverage</b><br>
				<select name="genome_c">
					<option selected="selected" value="n">No</option>
					<option value="y">Yes</option>				
				</select>
			</td>
			<td><b>Description</b><font color="red">*</font><br>
				<g:textField name="genome_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td width="40%"><b>Genes (GFF3 file)</b><font color="red">*</font><br>
				<g:textField name="genes" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><font color="red">*</font><br>
				<g:textField name="genes_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2><b>Description</b><font color="red">*</font><br>
				<g:textField name="genes_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td width="40%"><b>mRNA transcripts (FASTA file)</b><font color="red">*</font><br>
				<g:textField name="mrna_trans" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><font color="red">*</font><br>
				<g:textField name="mrna_trans_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2><b>Description</b><font color="red">*</font><br>
				<g:textField name="mrna_trans_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td width="40%"><b>Peptide sequences (FASTA file)</b><font color="red">*</font><br>
				<g:textField name="mrna_pep" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><font color="red">*</font><br>
				<g:textField name="mrna_pep_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2><b>Description</b><font color="red">*</font><br>
				<g:textField name="mrna_pep_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
	</table>
	
	<h2><b>Privacy</b></h2>	
	<table>
	<tr><td></td><td><b>Search</b></td><td><b>BLAST</b></td><td><b>Download</b></td></tr>
	<tr><td><b>Transcriptome</b></td>
	<td><select name="search_trans">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td>	
	<td><select name="blast_trans">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td>	
	<td><select name="down_trans">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td></tr>	
	
	<tr><td><b>Genome</b></td>
	<td><select name="search_genome">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td>	
	<td><select name="blast_genome">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td>	
	<td><select name="down_genome">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td></tr>	
	
	<tr><td><b>Genes</b></td>
	<td><select name="search_genes">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td>	
	<td></td>	
	<td><select name="down_genes">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td></tr>	
	
	<tr><td><b>mRNA</b></td>
	<td></td>
	<td><select name="blast_mrna">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td>	
	<td><select name="down_mrna">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td></tr>	
	
	<tr><td><b>Peptide</b></td>
		<td></td>
	<td><select name="blast_pep">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td>
	<td><select name="down_pep">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td></tr>	
	</table>
		
	<input class="mybuttons" type="button" value="Add new data set" onclick="submit()" >
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
</g:form>

<h1>Edit / delete files associated with this data set:</h1>

<table>
<g:each var="res" in="${metaData}">
	   <g:each var="f" in="${res.files}">
	   <tr><td>
	   <div class="inline">
	   		<g:form action= "editFile" controller="admin" params="[id: f.id]" >
	    		<a href="#" onclick="parentNode.submit()" title="Edit file"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
	    	</g:form>  	
	    	<g:form action="deleteFile" controller="admin" params="[id: f.id]" >
	    		<a href="#" onclick="parentNode.submit()" title="Delete file"><img src="${resource(dir: 'images', file: 'delete-icon.png')}" width="15px"/></a>
	    	</g:form> 	
	    </div>
      		</td><td>${f.file_type}:</td><td>${f.file_name}</td></tr>
   	   </g:each>
</g:each>
</table>

</body>
</html>
