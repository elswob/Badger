<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  <script>
  function demoData(db){
  	if (db == 'nr'){
    	$("#b_source").val("NCBI NR");
    	$("#b_link").val("http://www.ncbi.nlm.nih.gov/protein/");
   	 	$("#b_regex").val("gi\\|(\\d+)\\|.*");
   	}if (db == 'swiss'){
    	$("#b_source").val("SwissProt");
    	$("#b_link").val("http://www.ncbi.nlm.nih.gov/protein/");
   	 	$("#b_regex").val("gi\\|(\\d+)\\|.*");
    }if (db == 'nembase4'){
    	$("#b_source").val("Nembase4");
    	$("#b_link").val("http://www.nematodes.org/nembase4/cluster.php?cluster=");
   	 	$("#b_regex").val("(.*)");
    }if (db == 'a8r_ec'){
    	$("#f_source").val("Annot8r EC");
    	$("#f_link").val("http://enzyme.expasy.org/EC/");
   	 	$("#f_regex").val("(.*)");
   	 }if (db == 'a8r_go'){
    	$("#f_source").val("Annot8r GO");
    	$("#f_link").val("http://www.ebi.ac.uk/QuickGO/GTerm?id=");
   	 	$("#f_regex").val("(.*)");
   	 }if (db == 'a8r_kegg'){
    	$("#f_source").val("Annot8r KEGG");
    	$("#f_link").val("http://www.genome.jp/dbget-bin/www_bget?ko:");
   	 	$("#f_regex").val("(.*)");
   	 }if (db == 'ipr_raw'){
    	$("#i_anno_file").val("A_viteae.iprscan.raw");
    	$("select[name='iprSelect']").val("ipr_raw");
   	 }if (db == 'ipr_xml'){
    	$("#i_anno_file").val("A_viteae.iprscan.xml");
    	$("select[name='iprSelect']").val("ipr_xml");
   	 }
  }
  $(function() {
    $('#annoSelect').change(function(){
        if ($(this).val() == "1") {
            $('#blast').show();
            $('#fun').hide();
            $('#ipr').hide();
        }if ($(this).val() == "2") {
            $('#blast').hide();
            $('#fun').show();
            $('#ipr').hide();
        }if ($(this).val() == "3") {
            $('#blast').hide();
            $('#fun').hide();
            $('#ipr').show();
        }
    });
  });
  </script>
  </head>
  
  <body>

<g:if test="${dataSets}">
	   
	<h1>Add an annotation file:</h1>   
	
	<g:form action="addedAnno" controller="admin">
	
	<h2><b>Select your data set</b></h2>
	<select name="dataSelect">
		<g:each var="res" in="${dataSets}">
			<g:each var="f" in="${res.files}">
				<g:if test="${f.file_type == 'Genes' || f.file_type == 'Transcriptome'}">
					<option value=${f.file_name}>${res.genus} ${res.species}: ${f.file_type} (${f.file_version}) - ${f.file_name}
				</g:if>
			</g:each>
		</g:each>
	</select>
	<br><br>
	
	<h2><b>Choose annotation type</b></h2>
	<select id="annoSelect" name="annoSelect">
		<option value="1">BLAST</option>
		<option value="2">Functional</option>
		<option value="3">InterProScan</option>
	</select>
	
	<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	<div id="blast">  
		<div class="inline">   
			<h2><b>BLAST file (xml blast output)</b></h2>
			<p>Examples: <a href = "javascript:void(0)" onclick="demoData('nr')">NR</a> | <a href = "javascript:void(0)" onclick="demoData('swiss')">SwissProt</a> | <a href = "javascript:void(0)" onclick="demoData('nembase4')">Nembase4</a><br>
		</div>
			<p>Database<font color="red">*</font></p>
			<g:textArea name="b_source" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
			<p>File name<font color="red">*</font></p>
			<g:textArea name="b_anno_file" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
			<p>Link<font color="red">*</font></p>
			<g:textArea name="b_link" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
			<p>Regex<font color="red">*</font></p>
			<g:textArea name="b_regex" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>	
			<br><input class="mybuttons" type="button" value="Add data" onclick="submit()" >
			<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>	
	</div>
	
	<div id="fun" style="display:none"> 
		<div class="inline">
			<h2><b>Functional annotation file (tab delimited file)</b></h2>
			<p>Examples: <a href = "javascript:void(0)" onclick="demoData('a8r_ec')">EC</a> | <a href = "javascript:void(0)" onclick="demoData('a8r_go')">GO</a> | <a href = "javascript:void(0)" onclick="demoData('a8r_kegg')">KEGG</a><br>
		</div>
			<p>Database<font color="red">*</font></p>
			<g:textArea name="f_source" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
			<p>File name<font color="red">*</font></p>
			<g:textArea name="f_anno_file" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
			<p>Link<font color="red">*</font></p>
			<g:textArea name="f_link" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
			<p>Regex<font color="red">*</font></p>
			<g:textArea name="f_regex" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>	
			<br><input class="mybuttons" type="button" value="Add data" onclick="submit()" >
			<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	</div>
	
	<div id="ipr" style="display:none"> 
		<div class="inline">	
			<h2><b>InterProScan</b></h2>
			<p>Examples: <a href = "javascript:void(0)" onclick="demoData('ipr_raw')">Raw output</a> | <a href = "javascript:void(0)" onclick="demoData('ipr_xml')">XML</a><br>
		</div>
			<select id="iprSelect" name="iprSelect">
				<option value="ipr_raw">Raw</option>
				<option value="ipr_xml">XML</option>
			</select>
			<p>File name<font color="red">*</font></p>
			<g:textArea name="i_anno_file" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br>
			<br><input class="mybuttons" type="button" value="Add data" onclick="submit()" >
			<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>
	</div>
	
	</g:form>
</g:if>
<g:else>
	<h2>There are no data sets to annotate, please create one <g:link action="home">here</g:link>.</h2>
</g:else>
</body>
</html>
