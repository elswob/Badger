<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} news</title>
  <parameter name="admin" value="selected"></parameter>
  <script>
  function demoData(){
    $("#genus").val("Acanthocheilonema");
    $("#species").val("viteae");
    $("#description").val("A. viteae genome project");
    $("#dir").val("A_viteae");
    $("#trans").val("trans.fa"); $("#trans_v").val("1.1"); $("#trans_d").val("UniGenes");
    $("#genome").val("genome.fa"); $("#genome_v").val("1.0"); $("#genome_d").val("Data freeze 02.03.12");
    $("#genes").val("genome.gff"); $("#genes_v").val("1.1"); $("#genes_d").val("Augustus");
    $("select[name='blast_trans']").val("pub");
    $("select[name='blast_genome']").val("pub");
    $("select[name='blast_genes']").val("priv");
  }
  </script>
  </head>
  
  <body>
   
<h1>Admin for the <i>${grailsApplication.config.projectID}</i> project:</h1>
<p>This is where the administrator adds, edits and deletes the information for each of the data sets in the database.
<br>
<h1>Edit or delete a data set:</h1>
<g:if test = "${metaData}">	
	<g:each var="res" in="${metaData}">
		<div class="inline">	    
	    	<g:form action="editSpecies" controller="admin" params="[data_id: res.data_id]" >
	    		<a href="#" onclick="parentNode.submit()" title="Edit data"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
	    	</g:form>  	
	    	<g:form action="deleteSpecies" controller="admin" params="[data_id: res.data_id]" >
	    		<a href="#" onclick="parentNode.submit()" title="Delete data"><img src="${resource(dir: 'images', file: 'delete-icon.png')}" width="15px"/></a>
	    	</g:form> 	
	    	${res.genus} ${res.species}
		</div>
	</g:each>
</g:if>
<g:else>
	<p>There are currently no species in the database.</p>
</g:else>

<br>
<div class="inline">
	<h1>Add a data set:</h1> <a href = "javascript:void(0)" onclick="demoData()">(click for example)</a>
</div><br>
<h2><b>Project data</b></h2>
<g:form action="addedData" controller="admin">
	<p>Genus<font color="red">*</font></p>
	<g:textArea name="genus" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p>Species<font color="red">*</font></p>
	<g:textArea name="species" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p>Description<font color="red">*</font></p>
	<g:textArea name="description" style="width: 98%; height: 50px; border: 3px solid #cccccc; padding: 2px;"/><br>	
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	
	<h2><b>Data files</b></h2>	
	<p>Location (directory within data folder)<font color="red">*</font></p>
	<g:textArea name="dir" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<table width=100%>
		<tr>
			<td>Transcriptome (FASTA file)<br>
				<g:textArea name="trans" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td>Version<br>
				<g:textArea name="trans_v" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td>Description<br>
				<g:textArea name="trans_d" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td>Genome (FASTA file)<font color="red">*</font><br>
				<g:textArea name="genome" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td>Version<font color="red">*</font><br>
				<g:textArea name="genome_v" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td>Description<font color="red">*</font><br>
				<g:textArea name="genome_d" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td>Genes (GFF3 file)<font color="red">*</font><br>
				<g:textArea name="genes" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td>Version<font color="red">*</font><br>
				<g:textArea name="genes_v" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td>Description<font color="red">*</font><br>
				<g:textArea name="genes_d" style="height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
	</table>
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	
	<h2><b>BLAST server</b></h2>	
	Transcriptome: 
	<select name="blast_trans">
		<option value="n">None</option>
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select>	
	Genome:
	<select name="blast_genome">
		<option value="n">None</option>
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select>	
	Genes:
	<select name="blast_genes">
		<option value="n">None</option>
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select>	
	
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	<br><input class="mybuttons" type="button" value="Add data" onclick="submit()" >
</g:form>
	

</body>
</html>
