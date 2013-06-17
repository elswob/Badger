<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name='layout' content='main'/>
    <title>${grailsApplication.config.projectID} | BLAST</title>
    <parameter name="blast" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
    <script type="text/javascript"> 
    	$(window).unload(function() {});
    	
		function toggleDiv(divId) {
			//$('.toHide').hide();
            $("#"+divId).slideToggle(400);
    	}

    </script>
    <script>
     $(document).ready( function() {
 
        // Select all
        $("A[href='#select_all']").click( function() {
            $("#" + $(this).attr('rel') + " INPUT[type='checkbox']").attr('checked', true);
            return false;
        });
 
        // Select none
        $("A[href='#select_none']").click( function() {
            $("#" + $(this).attr('rel') + " INPUT[type='checkbox']").attr('checked', false);
            return false;
        });
 
        // Invert selection
        $("A[href='#invert_selection']").click( function() {
            $("#" + $(this).attr('rel') + " INPUT[type='checkbox']").each( function() {
                $(this).attr('checked', !$(this).attr('checked'));
            });
            return false;
        });
 
    
    function demoSeq(){
		$("#blastId").val(">example\nMALCMHPNVVMYHTSFVVGEELWVVMRLLNCGSMLDILKRRIKAMGKEAASSGVLDEVTIATVLKEVLRGLEYFHSSGQIHRDIKAGNILIADDGTVQIADFGVSGWLAASQGDLSRQKVRHTFVGTPCWMAPEVMEQVSGYDFKADIWSFGILAIELATGTAPYHKFPPMKVLMLTLQNDPPGLDTNAERKDQYKAYGKSFRHVIKDCLQKDPSKRPTASELLKYSFFKKAKDKKYLVHALIENLASMPLPSHHQTDAPKKVASGKLKKNSEGNWEFEPEETESEEEGEKMDLPTTSTAVATTSETINLVLRVRNAQKELNDIKFDYTPSVDTVEGIAHELVAAELIDGHDLVVVAANLKKLVDAALSKSDKKSVTFALSSVPPQEMPDERALIGFAQISLIDSSNAQVD");
    }
    
    //function programCheck(type){
    	//alert($('input[@name="genomeRadio"]:checked').val());
    	var type = $('input[@name="genomeRadio"]:checked').val()
		$("#program").empty();
		var select = $("#program")[0];	
		if (type < 3){
			select.add(new Option("BLASTN (DNA vs DNA)", "blastn"));
			select.add(new Option("TBLASTN (protein vs translated DNA)", "tblastn"));
			select.add(new Option("TBLASTX (translated DNA vs translated DNA)", "tblastx"));
		}else{
			select.add(new Option("BLASTP (protein vs protein)", "blastp"));
			select.add(new Option("BLASTX (translated DNA vs protein)", "blastx"));		
		}
    //}
    
    });
    
    function programCheck(){
    	//alert($('input[@name="genomeRadio"]:checked').val());
    	var type = $('input[@name="genomeRadio"]:checked').val()
		$("#program").empty();
		var select = $("#program")[0];	
		if (type < 3){
			select.add(new Option("BLASTN (DNA vs DNA)", "blastn"));
			select.add(new Option("TBLASTN (protein vs translated DNA)", "tblastn"));
			select.add(new Option("TBLASTX (translated DNA vs translated DNA)", "tblastx"));
		}else{
			select.add(new Option("BLASTP (protein vs protein)", "blastp"));
			select.add(new Option("BLASTX (translated DNA vs protein)", "blastx"));		
		}
    }
    </script>
    
</head>

<!--body onload="stopSpin();"-->
<body>
<div class="bread">BLAST</div>  

<!--body onunload=""-->

  <div id="content">
    <g:uploadForm action="runBlast" method="post">
	<h1>Choose a database:</h1>
	<g:if test="${blastFiles.size == 0}">
		<h2>There are currently no data to search! Please add some data to the database.</h2>
	</g:if>
	<g:else>
		<table class="compact"><tr><td>	
			<g:each var="d" in="${dataTypes}">
				<g:if test="${d.file_type == 'Genome'}">
					<label><input name="blastDB" type="radio" id="genomeRadio" checked="checked" value="1" STYLE="cursor: pointer" onclick="programCheck('nuc');toggleDiv('blk_1');$('#blk_2').hide();$('#blk_3').hide();"> Genomes</label>
				</g:if>
				<g:if test="${d.file_type == 'Genes'}">
					<label><input name="blastDB" type="radio" id="transRadio" checked="checked" value="2" STYLE="cursor: pointer" onclick="programCheck('nuc');toggleDiv('blk_2');$('#blk_1').hide();$('#blk_3').hide();"> Transcripts</label>
					<label><input name="blastDB" type="radio" id="proteinRadio" checked="checked" value="3" STYLE="cursor: pointer" onclick="programCheck('pep');toggleDiv('blk_3');$('#blk_1').hide();$('#blk_2').hide();"> Proteins</label>
				</g:if>
			</g:each>
			(click to show/hide available data sets)
		<fieldset id="blast_dbs">
			<div class="toHide" id="blk_1" style="display:none">
			<table><tr><td>
			Select <a rel="blast_dbs" href="#select_all">All</a> | 
			<a rel="blast_dbs" href="#select_none">None</a> | 
			<a rel="blast_dbs" href="#invert_selection">Invert</a>
				<table class="blast"><tr>
					<g:each var="res" in="${blastFiles}">
						<g:if test="${res.file_type == 'Genome' && res.loaded == true}">		
							<g:if test="${res.blast == 'priv' && user == 'user' || res.blast == 'pub'}">	
								<td><g:checkBox name="genomeCheck" value="${res.file_name}" /></td><td><i>${res.genome.meta.genus} ${res.genome.meta.species}</td><td>Version ${res.file_version}</td></tr>
							</g:if>
						</g:if>
					</g:each>
				</table>
			</td></tr></table>
			</div>
		
			<div class="toHide" id="blk_2" style="display:none">
			<table><tr><td>
			Select <a rel="blast_dbs" href="#select_all">All</a> | 
			<a rel="blast_dbs" href="#select_none">None</a> | 
			<a rel="blast_dbs" href="#invert_selection">Invert</a>
				<table class="blast"><tr>
					<g:each var="res" in="${blastFiles}">
						<g:if test="${res.file_type == 'mRNA' && res.loaded == true}">
							<g:if test="${res.blast == 'priv' && user == 'user' || res.blast == 'pub'}">
								<td><g:checkBox name="transCheck" value="${res.file_name}" /></td><td><i>${res.genome.meta.genus} ${res.genome.meta.species}</i></td><td>Version ${res.file_version}</td></tr>
							</g:if>
						</g:if>
					</g:each>
				</table>
			</td></tr></table>
			</div>

			<div class="toHide" id="blk_3" style="display:none">
			<table><tr><td>
			Select <a rel="blast_dbs" href="#select_all">All</a> | 
			<a rel="blast_dbs" href="#select_none">None</a> | 
			<a rel="blast_dbs" href="#invert_selection">Invert</a>
				<table class="blast"><tr>
					<g:each var="res" in="${blastFiles}">
						<g:if test="${res.file_type == 'Peptide' && res.loaded == true}">
							<g:if test="${res.blast == 'priv' && user == 'user' || res.blast == 'pub'}">
								<td><g:checkBox name="protCheck" value="${res.file_name}" /></td><td><i>${res.genome.meta.genus} ${res.genome.meta.species}</i></td><td>Version ${res.file_version}</td></tr>
							</g:if>
						</g:if>
					</g:each>
				</table>
			</td></tr></table>	
			</div>

		</fieldset>
		</td></tr></table>
	
		<h1>Set parameters:</h1>
		<table><tr><td>
		<g:link controller="blast" action="info" fragment="program">Program</g:link>
		<g:if test="${blastFiles.size == 1}">
			<select name = "PROGRAM" id = "program">
				<option value="blastn">BLASTN (protein vs protein)</option>
				<option value="tblastx">TBLASTX (translated DNA vs protein)</option>
			</select>	
		</g:if>
		<g:else>
			<select name = "PROGRAM" id = "program">
				<option value="blastp">BLASTP (protein vs protein)</option>
				<option value="blastx">BLASTX (translated DNA vs protein)</option>
			</select>	
		</g:else>
		<g:link controller="blast" action="info" fragment="out">Output</g:link>
		<select name = "ALIGNMENT_VIEW">
		<option value=0 selected> Full
		<option value=6> Tabular
		</select>
	
		<g:link controller="blast" action="info" fragment="exp">Expect</g:link>
		<select name = "EXPECT">
		<option> 1e-50 
		<option> 1e-20 
		<option> 1e-10 
		<option selected> 1e-5 
		<option> 0.001 
		<option> 1
		</select>
		<g:link controller="blast" action="info" fragment="desc">Descriptions</g:link>
		<select name = "DESCRIPTIONS">
		<option>0
		<option>10
		<option selected>20
		<option>50
		<option>100
		<option>250
		<option>500
		</select>
		<g:link controller="blast" action="info" fragment="ali">Alignments</g:link>
		<select name = "ALIGNMENTS">
		<option>0
		<option>10
		<option selected>20
		<option>50
		<option>100
		<option>250
		<option>500
		</select>
	
		<br><br><input TYPE="checkbox" NAME="UNGAPPED_ALIGNMENT" VALUE="is_set">
		Perform ungapped alignment
		</td></tr></table>
	
		<h1>Enter a sequence:</h1>
		<table><tr><td>
		<p>The query sequence is&nbsp;<input TYPE="checkbox" NAME="FILTER" VALUE="L" CHECKED>
		<g:link controller="blast" action="info" fragment="filt">filtered</g:link>
		for low complexity regions by default.
		<p>This is not a batch BLAST server, if a multi-FASTA file is used only the first sequence will be used.
		<p>Enter here your input data as sequence in <g:link controller="blast" action="info" fragment="fasta">FASTA</g:link> format or upload a file <input type="file" name="myFile"/>
		<p>Click <a href = "javascript:void(0)" onclick="demoSeq()">here</a> for an example FASTA file.
		<g:textArea name="blastId" style="width: 100%; height: 200px; border: 3px solid #cccccc; padding: 5px;"/>
		</td></tr></table>
	
		<h1>BLAST:</h1>
		<table><tr><td>
		<!--input TYPE="submit" VALUE="Search" id="button"-->
		<!--input TYPE="RESET" VALUE="Reset" id="button"-->
		<div id="buttons">
		  <input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
		  <!--input type="reset" value="Cancel" id="cancel"-->
		</div>
		</td></tr></table>
    </g:else>
  </g:uploadForm>
</div>
  <script>
          $(document).ready(function(){	  
            $("#process").bind("click", function () {
              $("#content").mask("Waiting for BLAST...");
            });
          });
   </script>
</body>
</html>
