<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} transcript search</title>
    <parameter name="search" value="selected"></parameter>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
    <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
    <script type="text/javascript"> 
    	$(window).unload(function() {});
    </script>
    <script type="text/javascript">

    function showSelected(val){
		document.getElementById
		('selectedResult').innerHTML = val;
    }
    $(function() {
		$("[name=toggler]").click(function(){
				$('.toHide').hide();
				$("#blk_"+$(this).val()).show('slow');
				$("#sel_"+$(this).val()).show('fast');
				showSelected($("#sel_"+$(this).val()).val())
		});
    });
    </script>
</head>
<body>
  <div id="content">
  <br>
     <p>There are three methods to search the ${badger.GeneInfo.count()} genes:</p><br>
     1. By BLAST homology<br>
     2. By functional annoation<br>
     2. By domain annotation<br>
    <g:form action="gene_search_results">
    <g:hiddenField name="dataSet" value="Transcripts"/>

    <div id = "showAnno">
    <table>
    <tr><td>
    <h1>Choose an annotation:</h1>
    <g:if test="${blastMap}">
      <label><input name="toggler" type="radio" id="blast" checked="checked" value="1"> 1. BLAST homology</label><br>
      	<div class="toHide" id="blk_1" style="height:150;width:200px;overflow:auto;border:3px solid green;display:none">
      	  <g:each var="res" in="${blastMap}">      	 
		  	<label><input type="checkbox" checked="yes" name="blastAnno" value="${res.key}" /> ${res.key}</label><br>
		  </g:each>
        </div>  
    </g:if>
    <g:if test="${funMap}">
      <label><input name="toggler" type="radio" id="anno" value="2"> 2. Functional annotation </label><br>
      	<div class="toHide" id="blk_2" style="height:150;width:200px;overflow:auto;border:3px solid green;display:none">
		 <g:each var="res" in="${funMap}">   
      		<label><input type="checkbox" checked="yes" name="funAnno" value="${res.key}" /> ${res.key}</label><br>
      	 </g:each>
      	</div> 
	</g:if>
	<g:if test="${iprMap}">
      <label><input name="toggler" type="radio" id="ipr" value="3"> 3. InterPro domains <a href="http://www.ebi.ac.uk/interpro/index.html" style="text-decoration:none" target="_blank">?</a></label><br>
      	<div class="toHide" id="blk_3" style="height:150;width:200px;overflow:auto;border:3px solid green;display:none">
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="HMMPanther" /> PANTHER <a href="http://www.pantherdb.org/" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="BlastProDom" /> ProDom <a href="http://prodom.prabi.fr/prodom/current/html/home.php" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="Gene3D" /> Gene3D <a href="http://gene3d.biochem.ucl.ac.uk/Gene3D/" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="HMMSmart" /> SMART <a href="http://smart.embl-heidelberg.de/" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="HMMPfam" /> Pfam <a href="http://pfam.sanger.ac.uk/" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="HMMTigr" /> TIGRFAMs <a href="http://www.jcvi.org/cgi-bin/tigrfams/index.cgi" style="text-decoration:none" target="_blank">?</a></label><br>
	   </div> 
      </td> 
    </g:if>
    <td>  
    <h1>Choose what to search:</h1>
    <select class="toHide" name = "tableSelect_1" id ="sel_1" onChange='showSelected(this.value)'>
      <option value="e.g. ATPase">Description</option>
      <option value="e.g. 215283796 or P31409">ID</option>    
      <!--option value="e.g. contig_1">ID</option-->
    </select>
    <select class="toHide" name = "tableSelect_2" id ="sel_2" onChange='showSelected(this.value)'>
      <option value="e.g. Calcium-transportingATPase">Description</option>
      <option value="e.g. GO:0008094 or 3.6.3.8 or K02147">ID</option>    
      <!--option value="e.g. contig_1">ID</option-->
    </select>
    <select class="toHide" name = "tableSelect_3" id ="sel_3" onChange='showSelected(this.value)'>
      <option value="e.g. Vacuolar (H+)-ATPase G subunit">Description</option>
      <option value="e.g. IPR023298 or PF01813">ID</option>    
      <!--option value="e.g. contig_1">ID</option-->
    </select>
      </td>
      
      <td>
    <h1>Enter a search term:</h1>
    <div id='selectedResult'></div>
    <g:textField name="searchId"  size="30"/>
    <input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
    </g:form>
     </td>
  </tr>
   </table>
   <br>
   </div>
   
   <script>
          $(document).ready(function(){
            $('.toHide').hide();
            $("#blk_1").show('slow');
	    $("#sel_1").show('fast');
	    showSelected($("#sel_1").val())
	    
	    $("#contig_attribute").show('slow');
	    
            $("#process").bind("click", function () {
              $("#content").mask("Searching the database...");
            });
				
            $("#cancel").bind("click", function () {
		$("#content").unmask();
            });               
          });
        </script>
   
</body>
</html>
