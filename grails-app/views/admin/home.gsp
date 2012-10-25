<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  <script>
  function demoData(sp){
  	if (sp == "A_vit"){
		$("#genus").val("Acanthocheilonema");
		$("#species").val("viteae");
		$("#description").val("Acanthocheilonema viteae is a filarial nematode parasite of rodents. It is widely used as a model for human filariases. Importantly, A. viteae lacks the Wolbachia bacterial endosymbiont found in most human-infective filarial nematodes. Thus this species has become central in efforts to understand the role of the Wolbachia in the nematode-bacterial symbiosis, and in particular its possible role in immune evasion. The Wolbachia is also a drug target in nematodes that carry this symbiont, so work on A. viteae can also help to disentangle anti-nematode and anti-symbiont effects.");
		$("#gbrowse").val("http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/");
		$("#image_f").val("A_viteae_main.jpeg");
		$("#image_s").val("A. viteae lifecycle; from http://www.uni-giessen.de");
		
		$("#dir").val("A_viteae");
		//$("#trans").val("trans.fa"); $("#trans_v").val("1.1"); $("#trans_d").val("UniGenes"); $("select[name='trans_c']").val("n");
		$("#genome").val("Acanthocheilonema_viteae_v1.0.fna"); $("#genome_v").val("1.0"); $("#genome_d").val("The A. viteae genome was sequenced from material supplied by Kenneth Pfarr. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh. Assembly and annotation of the genome was performed by Georgios Koutsovoulos (assisted by Sujai Kumar and Alex Marshall)."); $("select[name='genome_c']").val("n");
		$("#genes").val("nAv.1.0.1.aug.blast2go.gff"); $("#genes_v").val("1.1"); $("#genes_d").val("Augustus");
		$("#mrna_trans").val("nAv.1.0.1.aug.transcripts.fasta"); $("#mrna_trans_v").val("1.1"); $("#mrna_trans_d").val("Augustus");
		$("#mrna_pep").val("nAv.1.0.1.aug.proteins.fasta"); $("#mrna_pep_v").val("1.1"); $("#mrna_pep_d").val("Augustus");
	}if (sp == "L_sig"){
		$("#genus").val("Litomosoides");
		$("#species").val("sigmodontis");
		$("#description").val("Litomosoides sigmodontis is a filarial nematode parasite of rodents. Found in cotton rats in the wild, it has been adapted to the laboratory mouse and is widely used as a model for human filariases. Beginning with work in Odile Bain's laboratory in Paris, this L. sigmodontis model has become central in efforts to develop vaccines against filarial infections, test new drugs before they are progressed to clinical trials, and to investigate the basic biology of the fascinating interactions between parasitic nematodes and their mammalian hosts.");
		$("#gbrowse").val("http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nLs.2.1.2/");
		$("#image_f").val("Litomosoides_sigmodontis.jpg");
		$("#image_s").val("tails of male L. sigmodontis; by L. LeGeoff");
		
		$("#dir").val("L_sigmodontis");
		$("#genome").val("Litomosoides_sigmodontis_2.1.fna"); $("#genome_v").val("2.1"); $("#genome_d").val("The L. sigmodontis genome was sequenced from material supplied by Simon Babayan. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh. Assembly and annotation of the genome was performed by Sujai Kumar (assisted by Graham Thomas, Georgios Koutsovoulos and Alex Marshall)."); $("select[name='genome_c']").val("n");
		$("#genes").val("nLs.2.1.2.aug.gff"); $("#genes_v").val("2.1.2"); $("#genes_d").val("Augustus");
		$("#mrna_trans").val("nLs.2.1.2.aug.transcripts.fasta"); $("#mrna_trans_v").val("2.1"); $("#mrna_trans_d").val("Augustus");
		$("#mrna_pep").val("nLs.2.1.2.aug.proteins.fasta"); $("#mrna_pep_v").val("2.1"); $("#mrna_pep_d").val("Augustus");
	}
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
	    	<g:form action="addAnno" controller="admin" params="[species: res.species]" >
	    		<a href="#" onclick="parentNode.submit()" title="Edit data"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
	    	</g:form>  	
	    	<g:form action="deleteSpecies" controller="admin" params="[species: res.species]" >
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
	<h1>Add a data set:</h1> Examples: <a href = "javascript:void(0)" onclick="demoData('A_vit')">A. viteae </a>| <a href = "javascript:void(0)" onclick="demoData('L_sig')">L. sigmodontis </a>
</div><br>
<h2><b>Project data</b></h2>
<g:form action="addedData" controller="admin">
	<p>Genus<font color="red">*</font></p>
	<g:textField name="genus" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p>Species<font color="red">*</font></p>
	<g:textField name="species" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p>Description<font color="red">*</font></p>
	<g:textArea name="description" style="width: 98%; height: 50px; border: 3px solid #cccccc; padding: 2px;"/><br>	
	<p>GBrowse link</p>
	<g:textField name="gbrowse" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p>Image file</p>
	<g:textField name="image_f" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p>Image source</p>
	<g:textField name="image_s" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	
	<h2><b>Data files</b></h2>	
	<p>Location (directory within data folder)<font color="red">*</font></p>
	<g:textField name="dir" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<table width=100%>
		<tr>
			<td width="40%">Transcriptome (FASTA file)<br>
				<g:textField name="trans" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%">Version<br>
				<g:textField name="trans_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td>Coverage<br>
				<select name="trans_c">
					<option selected="selected" value="n">No</option>
					<option value="y">Yes</option>
				</select>
			</td>
			<td width="45%">Description<br>
				<g:textField name="trans_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td width="40%">Genome (FASTA file)<font color="red">*</font><br>
				<g:textField name="genome" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%">Version<font color="red">*</font><br>
				<g:textField name="genome_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td>Coverage<br>
				<select name="genome_c">
					<option selected="selected" value="n">No</option>
					<option value="y">Yes</option>				
				</select>
			</td>
			<td>Description<font color="red">*</font><br>
				<g:textField name="genome_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td width="40%">Genes (GFF3 file)<font color="red">*</font><br>
				<g:textField name="genes" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%">Version<font color="red">*</font><br>
				<g:textField name="genes_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2>Description<font color="red">*</font><br>
				<g:textField name="genes_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td width="40%">mRNA transcripts (FASTA file)<font color="red">*</font><br>
				<g:textField name="mrna_trans" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%">Version<font color="red">*</font><br>
				<g:textField name="mrna_trans_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2>Description<font color="red">*</font><br>
				<g:textField name="mrna_trans_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
		<tr>
			<td width="40%">Peptide sequences (FASTA file)<font color="red">*</font><br>
				<g:textField name="mrna_pep" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td width="10%">Version<font color="red">*</font><br>
				<g:textField name="mrna_pep_v" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
			<td colspan=2>Description<font color="red">*</font><br>
				<g:textField name="mrna_pep_d" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
			</td>
		</tr>
	</table>
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	
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
	<td><select name="blast_genes">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td>	
	<td><select name="down_genes">
		<option value="pub">Public</option>
		<option value="priv">Private</option>
	</select></td></tr>	
	</table>
	
	
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	<br><input class="mybuttons" type="button" value="Add data" onclick="submit()" >
</g:form>
	

</body>
</html>