<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name='layout' content='main'/>
    <title>Bicyclus anynana BLAST</title>
    <parameter name="blast" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
    <script type="text/javascript"> 
    	$(window).unload(function() {});
    </script>
</head>

<!--body onload="stopSpin();"-->
<body>
<!--body onunload=""-->

  <div id="content">
    <g:uploadForm action="runBlast" method="post">
      <br>
    <p align="center"><g:link controller="blast" action="info" fragment="program">Program</g:link>
    <select name = "PROGRAM">
    <option>blastn</option>
    <option>tblastx</option>
    <option>tblastn</option>
    <!--option>blastx</option>
    <option>blastp</option-->
    </select>
    <g:link controller="blast" action="info" fragment="db">Database</g:link>
    <select name = "datalib">
    <sec:ifLoggedIn>
    	<option value="genome">Genome contigs&nbsp;</option>
    	<option value="bacs">Public BACs&nbsp;</option>
    </sec:ifLoggedIn>
    <option value="unigenes">UniGenes&nbsp;</option>
    </select>
    <input TYPE="checkbox" NAME="UNGAPPED_ALIGNMENT" VALUE="is_set">
    Perform ungapped alignment
    <br>&nbsp;
    <p align="center">The query sequence is&nbsp;<input TYPE="checkbox" NAME="FILTER" VALUE="L" CHECKED>
    <g:link controller="blast" action="info" fragment="filt">filtered</g:link>
    for low complexity regions by default.
    <br>Enter here your input data as sequence in <g:link controller="blast" action="info" fragment="fasta">FASTA</g:link> format or upload a file <input type="file" name="myFile"/><br>
    <g:textArea name="blastId" style="width: 90%; height: 200px; border: 3px solid #cccccc; padding: 5px;"/><br>
    <p align="center">

    <br>Alignment view
    <select name = "ALIGNMENT_VIEW">
    <option value=0 selected> Full
    <option value=6> Tabular
    </select>
    <br><g:link controller="blast" action="info" fragment="exp">Expect</g:link>
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
    <br>
    <!--input TYPE="submit" VALUE="Search" id="button"-->
    <!--input TYPE="RESET" VALUE="Reset" id="button"-->
    <div id="buttons" align="center">
      <input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
      <!--input type="reset" value="Cancel" id="cancel"-->
    </div>
    
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
