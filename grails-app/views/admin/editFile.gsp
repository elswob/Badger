<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > <g:link action="editSpecies" params="${[Gid:fileData.genome.meta.id]}"><i>${fileData.genome.meta.genus} ${fileData.genome.meta.species}</i></g:link> > <g:link action="editGenome" params="${[gid:fileData.genome.id]}">Edit genome</g:link> > Edit file</div>
  
<h1>Edit data file:</h1>

<g:form action="editedFile" controller="admin">

<p><b>Location (directory within data folder)</b><font color="red">*</font></p>
	<g:textField value="${fileData.file_dir}" name="dir" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<table width=100%>
	<g:if test = "${fileData.file_type == 'Transcriptome'}">
		<tr>
			<td width="40%"><b>Transcriptome (FASTA file)</b><br>
				<g:textField value="${fileData.file_name}" name="trans" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><br>
				<g:textField value="${fileData.file_version}" name="trans_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td><b>Coverage</b><br>
				<select name="trans_c">
					<option selected="selected" value="n">No</option>
					<option value="y">Yes</option>
				</select>
			</td>
			<td width="45%"><b>Description</b><br>
				<g:textField value="${fileData.description}" name="trans_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
	</g:if>
	<g:if test = "${fileData.file_type == 'Genome'}">
		<tr>
			<td width="40%"><b>Genome (FASTA file)</b><font color="red">*</font><br>
				<g:textField value="${fileData.file_name}" name="genome" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><font color="red">*</font><br>
				<g:textField value="${fileData.file_version}" name="genome_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td><b>Coverage</b><br>
				<select name="genome_c">
					<option selected="selected" value="n">No</option>
					<option value="y">Yes</option>				
				</select>
			</td>
			<td><b>Description</b><font color="red">*</font><br>
				<g:textField value="${fileData.description}" name="genome_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
	</g:if>
	<g:if test = "${fileData.file_type == 'Genes'}">
		<tr>
			<td width="40%"><b>Genes (GFF3 file)</b><font color="red">*</font><br>
				<g:textField value="${fileData.file_name}" name="genes" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><font color="red">*</font><br>
				<g:textField value="${fileData.file_version}" name="genes_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2><b>Description</b><font color="red">*</font><br>
				<g:textField value="${fileData.description}" name="genes_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
	</g:if>
	<g:if test = "${fileData.file_type == 'mRNA'}">
		<tr>
			<td width="40%"><b>mRNA transcripts (FASTA file)</b><font color="red">*</font><br>
				<g:textField value="${fileData.file_name}" name="mrna_trans" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><font color="red">*</font><br>
				<g:textField value="${fileData.file_version}" name="mrna_trans_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2><b>Description</b><font color="red">*</font><br>
				<g:textField value="${fileData.description}" name="mrna_trans_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
	</g:if>
	<g:if test = "${fileData.file_type == 'Peptide'}">
		<tr>
			<td width="40%"><b>Peptide sequences (FASTA file)</b><font color="red">*</font><br>
				<g:textField value="${fileData.file_name}" name="mrna_pep" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%"><b>Version</b><font color="red">*</font><br>
				<g:textField value="${fileData.file_version}" name="mrna_pep_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2><b>Description</b><font color="red">*</font><br>
				<g:textField value="${fileData.description}" name="mrna_pep_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
	</g:if>
	</table>
	
	<h2><b>Privacy</b></h2>	
	<table>
	<tr><td></td><td><b>Search</b></td><td><b>BLAST</b></td><td><b>Download</b></td></tr>
	<g:if test = "${fileData.file_type == 'Transcriptome'}">
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
	</g:if>
	<g:if test = "${fileData.file_type == 'Genome'}">
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
	</g:if>	
	<g:if test = "${fileData.file_type == 'Genes'}">
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
	</g:if>
	<g:if test = "${fileData.file_type == 'mRNA'}">
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
	</g:if>
	<g:if test = "${fileData.file_type == 'Peptide'}">
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
	</g:if>
	</table>
<input type = "hidden" name="id" value="${fileData.id}">	
<input class="mybuttons" type="button" value="Update data file" onclick="submit()" >

<g:if test="${fileData.file_type == 'Genes'}">
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	<h1>Edit / delete annotation files associated with this data set:</h1>
	
	<table class="compact">
	<g:each var="res" in="${fileData}">
		   <g:each var="f" in="${res.anno.sort({it.type})}">
		   <tr><td>
		   <div class="inline">
				<g:form action= "editAnno" controller="admin" params="[gid: f.id]" >
					<a href="#" onclick="parentNode.submit()" title="Edit annotation file"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
				</g:form>  	
				<g:form action="deleteAnno" controller="admin" params="[gid: f.id]" >
					<a href="#" onclick="parentNode.submit()" title="Delete annotation file"><img src="${resource(dir: 'images', file: 'delete-icon.png')}" width="15px"/></a>
				</g:form> 	
			</div>
				</td><td>${f.source}</td><td><b>${f.anno_file}</b></td></tr>
		   </g:each>
	</g:each>
	</table>
</g:if>
</g:form>
</body>
</html>
