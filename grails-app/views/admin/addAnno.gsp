<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} news</title>
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
			<option value=${res.data_id}:${res.file_id}>${res.genus} ${res.species}: ${res.file_type} (${res.file_version}) - ${res.file_name}
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
		<h2><b>Functional annotation file (tab delimited file)</b></h2>
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
		<h2><b>InterProScan (raw output)</b></h2>
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
