<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  <r:require modules="jquery-validation-ui" />
  
  <script>
  function demoData(sp){
  	if (sp == "test"){
		$("#genus").val("Test");
		$("#species").val("species");
		$("#version").val("1.0.1");
		$("#description").val("Test data set.");
		$("#gbrowse").val("http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/");
		$("#image_f").val("a_viteae_lifecycle.jpg");
		$("#image_s").val("A. viteae lifecycle; from http://www.uni-giessen.de");
		
		$("#dir").val("test");
		//$("#trans").val("trans.fa"); $("#trans_v").val("1.1"); $("#trans_d").val("UniGenes"); $("select[name='trans_c']").val("n");
		$("#genome").val("test_genome.fa"); $("#genome_v").val("1.0"); $("#genome_d").val("The A. viteae genome was sequenced from material supplied by Kenneth Pfarr. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh. Assembly and annotation of the genome was performed by Georgios Koutsovoulos (assisted by Sujai Kumar and Alex Marshall)."); $("select[name='genome_c']").val("n");
		$("#genes").val("test.gff"); $("#genes_v").val("1.1"); $("#genes_d").val("Augustus");
		$("#mrna_trans").val("nAv.1.0.1.aug.transcripts.fasta"); $("#mrna_trans_v").val("1.1"); $("#mrna_trans_d").val("Augustus");
		$("#mrna_pep").val("nAv.1.0.1.aug.proteins.fasta"); $("#mrna_pep_v").val("1.1"); $("#mrna_pep_d").val("Augustus");
	}
  	if (sp == "A_vit"){
		$("#genus").val("Acanthocheilonema");
		$("#species").val("viteae");
		$("#version").val("1.0.1");
		$("#description").val("Acanthocheilonema viteae is a filarial nematode parasite of rodents. It is widely used as a model for human filariases. Importantly, A. viteae lacks the Wolbachia bacterial endosymbiont found in most human-infective filarial nematodes. Thus this species has become central in efforts to understand the role of the Wolbachia in the nematode-bacterial symbiosis, and in particular its possible role in immune evasion. The Wolbachia is also a drug target in nematodes that carry this symbiont, so work on A. viteae can also help to disentangle anti-nematode and anti-symbiont effects.");
		$("#gbrowse").val("http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/");
		$("#image_f").val("a_viteae_lifecycle.jpg");
		$("#image_s").val("A. viteae lifecycle; from http://www.uni-giessen.de");
		
		$("#dir").val("A_viteae");
		//$("#trans").val("trans.fa"); $("#trans_v").val("1.1"); $("#trans_d").val("UniGenes"); $("select[name='trans_c']").val("n");
		$("#genome").val("Acanthocheilonema_viteae_v1.0.fna"); $("#genome_v").val("1.0"); $("#genome_d").val("The A. viteae genome was sequenced from material supplied by Kenneth Pfarr. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh. Assembly and annotation of the genome was performed by Georgios Koutsovoulos (assisted by Sujai Kumar and Alex Marshall)."); $("select[name='genome_c']").val("n");
		$("#genes").val("nAv.1.0.1.aug.blast2go.gff"); $("#genes_v").val("1.1"); $("#genes_d").val("Augustus");
		$("#mrna_trans").val("nAv.1.0.1.aug.transcripts.fasta"); $("#mrna_trans_v").val("1.1"); $("#mrna_trans_d").val("Augustus");
		$("#mrna_pep").val("nAv.1.0.1.aug.proteins.fasta"); $("#mrna_pep_v").val("1.1"); $("#mrna_pep_d").val("Augustus");
	}
	if (sp == "L_sig"){
		$("#genus").val("Litomosoides");
		$("#species").val("sigmodontis");
		$("#version").val("2.1.2");
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
	if (sp == "D_imm"){
		$("#genus").val("Dirofilaria");
		$("#species").val("immitis");
		$("#version").val("2.2.2");
		$("#description").val("The heartworm Dirofilaria immitis is an important parasite of dogs. Transmitted by mosquitoes in warmer climatic zones, it is spreading across Southern Europe and the Americas at an alarming pace. There is no vaccine and chemotherapy is prone to complications. To learn more about this parasite, we have sequenced the genomes of D. immitis and its endosymbiont Wolbachia.");
		$("#gbrowse").val("http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nDi.2.2.2/");
		$("#image_f").val("D_immitis.jpg");
		$("#image_s").val("heartworm in situ; photo from S. Williams");
		
		$("#dir").val("D_immitis");
		$("#genome").val("Dirofilaria_immitis_2.2.fna"); $("#genome_v").val("2.2"); $("#genome_d").val("The D. immitis genome was sequenced by an international collaboration headed by Mark Blaxter and Pascal Mäser. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh, and FASTERIS SA, Switzerland. The version 2.2 assembly and annotation was performed by Sujai Kumar (assisted by Georgios Koutsovoulos and Alex Marshall)."); $("select[name='genome_c']").val("n");
		$("#genes").val("nDi.2.2.2.aug.blast2go.gff"); $("#genes_v").val("2.2.2"); $("#genes_d").val("Augustus");
		$("#mrna_trans").val("nDi.2.2.2.aug.transcripts.fasta"); $("#mrna_trans_v").val("2.2.2"); $("#mrna_trans_d").val("Augustus");
		$("#mrna_pep").val("nDi.2.2.2.aug.proteins.fasta"); $("#mrna_pep_v").val("2.2.2"); $("#mrna_pep_d").val("Augustus");
	}
	if (sp == "O_och"){
		$("#genus").val("Onchocerca");
		$("#species").val("ochengi");
		$("#version").val("2.0.1");
		$("#description").val("Onchocerca ochengi is a filarial nematode parasite of cattle, and is native to West Africa, including Cameroon, where the specimens used for this genome project were isolated. As well as being a significant disease of native (Bos indicus) cattle, O. ochengi is very closely related to the human-parasitic Onchocerca volvulus. O. volvulus causes river blindness and skin disease throughout West Africa, and is the subject of intense efforts by several international agencies and teams aiming at disease eradication. The relationship between O. ochengi and O. volvulus, and concern over the possibility of cattle acting as a zoonotic reservoir make understanding of the parasite of some importance. Additionally, the genetic closeness to O. volvulus and the tractability of the bovine host makes the O. ochengi-cattle model a useful one in vaccine and drug development work.");
		$("#gbrowse").val("http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nOo.2.0.1/");
		$("#image_f").val("O_ochengi.jpg");
		$("#image_s").val("O. ochengi larva; from Sandy Trees and colleagues http://ars.sciencedirect.com");
		
		$("#dir").val("O_ochengi");
		$("#genome").val("Onchocerca_ochengi_nuclear_assembly_nOo.2.0.fna"); $("#genome_v").val("2.0"); $("#genome_d").val("The O. ochengi genome was sequenced from material supplied by Benjamin Makepeace. Sequencing was performed by the GenePool Genomics Facility, University of Edinburgh. Assembly and annotation of the genome was performed by Gaganjot Kaur and Alex Marshall (assisted by Sujai Kumar and Georgios Koutsovoulos)."); $("select[name='genome_c']").val("n");
		$("#genes").val("nOo.2.0.1.aug.gff"); $("#genes_v").val("2.0.1"); $("#genes_d").val("Augustus");
		$("#mrna_trans").val("nOo.2.0.1.aug.transcripts.fasta"); $("#mrna_trans_v").val("2.0.1"); $("#mrna_trans_d").val("Augustus");
		$("#mrna_pep").val("nOo.2.0.1.aug.proteins.fasta"); $("#mrna_pep_v").val("2.0.1"); $("#mrna_pep_d").val("Augustus");
	}
  }
  </script>
  </head>
  
  <body>
  <g:link action="home">Admin</g:link> > Home 
<h1>Admin for the <i>${grailsApplication.config.projectID}</i> project:</h1>
<p>This is where the administrator adds, edits and deletes the information for each of the data sets in the database.
<br>
<h1>Edit or delete a data set:</h1>
<g:if test = "${metaData}">	
	<g:each var="res" in="${metaData}">
		<div class="inline">	    
	    	<g:form action="editData" controller="admin" params="[id: res.id]" >
	    		<a href="#" onclick="parentNode.submit()" title="Edit data"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
	    	</g:form>  	
	    	<g:form action="deleteData" controller="admin" params="[id: res.id]" >
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
	<h1>Add a data set:</h1> Examples: 
	 <a href = "javascript:void(0)" onclick="demoData('A_vit')">A. viteae </a>
	 | <a href = "javascript:void(0)" onclick="demoData('L_sig')">L. sigmodontis </a>
	 | <a href = "javascript:void(0)" onclick="demoData('D_imm')">D. immitis </a>
	 | <a href = "javascript:void(0)" onclick="demoData('O_och')">O. ochengi </a>
	 | <a href = "javascript:void(0)" onclick="demoData('test')">Test </a>
</div><br>

<h2><b>Project data</b></h2>
<g:form action="addedData" controller="admin">
	<label for="genus" class="control-label"><g:message code="person.name.label" default="Genus" /><span class="required-indicator">*</span></label>
	<div class="control-group fieldcontain ${hasErrors(bean: meta_data, field: 'genus', 'error')} required">
	
	<div class="controls">	
		
		<g:textField style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;" name="genus" required="" value="${meta_data?.genus}"/>
		<span class="help-inline">${hasErrors(bean: mete_data, field: 'genus', 'error')}</span>
	</div>
	</div>
	<p><b>Species</b><font color="red">*</font></p>
	<g:textField name="species" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p><b>Version</b><font color="red">*</font></p>
	<g:textField name="version" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p><b>Description</b><font color="red">*</font></p>
	<g:textArea name="description" style="width: 80%; height: 50px; border: 3px solid #cccccc; padding: 2px;"/><br>	
	<p><b>GBrowse link</b></p>
	<g:textField name="gbrowse" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p><b>Image file</b></p>
	<g:textField name="image_f" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p><b>Image source</b></p>
	<g:textField name="image_s" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	
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
	
	
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	<div class="form-actions">
    	<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
    </div>
</g:form>
	

</body>
</html>
