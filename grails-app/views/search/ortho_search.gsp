<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} ortholog search</title>
  <parameter name="search" value="selected"></parameter>
  <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
  <script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
  <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
  <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
  <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  <link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/data_table.css')}" type="text/css"></link>
  <link rel="stylesheet" href="${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}" type="text/css"></link>

  
  <script type="text/javascript"> 
    
   <% 
    def jsonData = searchRes.encodeAsJSON(); 
    println jsonData;
    %>
	
  $(document).ready(function() {

    var oTable = $('#res').dataTable( {
        "bProcessing": true,
        "sPaginationType": "full_numbers",
    	"iDisplayLength": 10,
    	"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	"oLanguage": {
    		"sSearch": "Filter records:"
    	},
    	"aaSorting": [[ 0, "asc" ]],
    	"sDom": 'T<"clear">lfrtip',
        "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
        }
    } );
 });
    </script>
  </head>

  <body>
<g:link action="">Search</g:link> > <g:link action="ortho">Search orthologs</g:link> > Search results
<g:if test="${params.type == 'bar'}">
	<h1>Clusters with size ${params.val}:</h1>
	<table cellpadding="0" cellspacing="0" border="0" class="display" id="res">
		<thead>
			<tr>
				<th>Cluster</th>
				<g:each var="res" in="${files}">
					<th>${res.genus[0]}. ${res.species}<br> (${res.file_name})</td>					
				</g:each>
			</tr>
		</thead>
		<tbody>
			<g:each var="res" in="${searchRes}">
				<tr><td><g:link action="cluster" params="${[group_id:res.group_id]}">${res.group_id}</g:link></td>
					<g:each var="s" in="${files}">
						<td>${res."${s.file_name}"}</td>
					</g:each>
				</td>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:if test="${params.type == 'search'}">
	<g:if test="${searchRes}">
		<h1>Clusters with a description matching '${params.searchId}'</h1>
		
		  <table cellpadding="0" cellspacing="0" border="0" class="display" id="res">
			<thead>
				<tr>
					<th>Cluster</th>
					<th>Size</th>
					<th>Species</th>
					<th>Transcript</th>
					<th>Description</th>
				</tr>
			</thead>
			<tbody>
				<g:each var="res" in="${searchRes}">
					<tr><td><g:link action="cluster" params="${[group_id:res.group_id]}">${res.group_id}</g:link></td>
					<td>${res.size}</td>
					<td><i>${res.genus[0]}. ${res.species}</i></td>
					<td><g:link action="m_info" params="${[mid: res.mrna_id]}"> ${res.mrna_id}</g:link></td>
					<td><span style="color:red">${res.anno_db}: </span><% if (res.descr.size()>200){ res.descr = res.descr[0..200]+" ... "};%>${res.descr} <span style="color:blue">Evalue: ${res.score}</span></td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<h1>There were no results for your search of '${params.searchId}'</h1>
	</g:else>
</g:if>
</body>
</html>

