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
  	if (sp == "badger"){
		$("#genus").val("Meles");
		$("#species").val("meles");
		$("#version").val("1.0");
		$("#description").val("The badger is the king of all animals.");
		$("#gbrowse").val("http://salmo.bio.ed.ac.uk/cgi-bin/gbrowse/gbrowse/nAv.1.0.1/");
		$("#image_f").val("badger.jpg");
		$("#image_s").val("Taken from google");
		
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
<p>This is where the administrator adds, edits and deletes the information for each of the data sets in the database.
<br>
<h1>Edit the home page</h1>
Click <g:link controller="home" action="index" params="${[edit: 'y']}">here</g:link>.
<br>
<h1>Edit or delete a data set:</h1>
<g:if test = "${metaData}">	
	<g:each var="res" in="${metaData}">
		<div class="inline">	    
	    	<g:form action="editSpecies" controller="admin" params="[Gid: res.id]" >
	    		<a href="#" onclick="parentNode.submit()" title="Edit data"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
	    	</g:form>  	
	    	<g:form action="deleteSpecies" controller="admin" params="[Gid: res.id]" >
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
	<h1>Add a species:</h1> Examples: 
	 <a href = "javascript:void(0)" onclick="demoData('A_vit')">A. viteae </a>
	 | <a href = "javascript:void(0)" onclick="demoData('L_sig')">L. sigmodontis </a>
	 | <a href = "javascript:void(0)" onclick="demoData('D_imm')">D. immitis </a>
	 | <a href = "javascript:void(0)" onclick="demoData('O_och')">O. ochengi </a>
	 | <a href = "javascript:void(0)" onclick="demoData('badger')">M. meles </a>
</div><br>

<g:form action="addedSpecies" controller="admin">
	<p><b>Genus</b><font color="red">*</font></p>
	<g:textField name="genus" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p><b>Species</b><font color="red">*</font></p>
	<g:textField name="species" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p><b>Description</b><font color="red">*</font></p>
	<g:textArea name="description" style="width: 80%; height: 50px; border: 3px solid #cccccc; padding: 2px;"/><br>	
	<p><b>Image file</b></p>
	<g:textField name="image_f" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<p><b>Image source</b></p>
	<g:textField name="image_s" style="width: 80%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
	<br>	
	<div class="form-actions">
    	<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
    </div>
    
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
</g:form>	
	

</body>
</html>
