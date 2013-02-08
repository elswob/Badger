<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} gene detail</title>
    <parameter name="search" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.0/media/js/jquery.js')}" type="text/javascript"></script> 
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.0/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
    <style type="text/css">
            @import "${resource(dir: 'js', file: 'DataTables-1.9.0/media/css/demo_table.css')}";
            @import "${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}";
    </style>
    <script>
    function get_table_data(fileId){
    	var table_scrape = [];
    	var rowNum
    	var regex
	    var oTableData = document.getElementById('gene_table_data');
	    //gets table
	    var rowLength = oTableData.rows.length;
	    //gets rows of table
	    for (i = 0; i < rowLength; i++){
	    //loops through rows
	       var oCells = oTableData.rows.item(i).cells;
	       var cellVal = oCells.item(rowNum).innerHTML;
	       //alert(cellVal)
	       var matcher = cellVal.match(/.*?mid=(.*?)">.*/);
	       if (matcher){
	       	  	table_scrape.push(matcher[1])
	    	}
	    }
	    document.getElementById(fileId).value=table_scrape;
	    //alert(table_scrape)
    }
    </script>
  <script>
  $(document).ready(function() {
	$('#gene_table_data').dataTable({
		"sPaginationType": "full_numbers",
		"iDisplayLength": 10,
		"oLanguage": {
			"sSearch": "Filter records:"
		},
		"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
		"aaSorting": [[3, "asc" ]],
		"sDom": 'T<"clear">lfrtip',
		"oTableTools": {
			"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
		}
	});     
   });
   </script>
  
  </head>
 
  <body>
    <g:if test="${results}">
    <g:link action="">Search</g:link> > <g:link action="species">Species</g:link> > <g:link action="species_v" params="${[Sid:metaData.genome.meta.id]}"><i> ${metaData.genome.meta.genus} ${metaData.genome.meta.species}</i></g:link> > Genome: <g:link action="species_search" params="${[Gid:Gid,GFFid:GFFid]}">v${metaData.file_version}</g:link> > Scaffold:<g:link action="genome_info" params="${[Gid:Gid,GFFid:GFFid,contig_id:results.contig_id[0]]}"> ${results.contig_id[0]}</g:link> > Gene: ${results.gene_id[0]}
    	<g:if test="${results}">
    	<div class="inline">
    	<br>
			<!-- download genes form gets fileName value from get_table_data() -->		    		
			 <div style="right:0px;">
				 Download transcript sequences:
					<g:form name="nucfileDownload" url="[controller:'FileDownload', action:'gene_download']">
					<g:hiddenField name="nucFileId" value=""/>
					<g:hiddenField name="fileName" value="${results.contig_id[0]}.genes"/>
					<g:hiddenField name="seq" value="Nucleotides"/>
					<a href="#" onclick="get_table_data('nucFileId');document.nucfileDownload.submit()">Nucleotides</a>
				</g:form> 
				|
				<g:form name="pepfileDownload" url="[controller:'FileDownload', action:'gene_download']">
					<g:hiddenField name="pepFileId" value=""/>
					<g:hiddenField name="fileName" value="${results.contig_id[0]}.genes"/>
					<g:hiddenField name="seq" value="Peptides"/>
					<a href="#" onclick="get_table_data('pepFileId');document.pepfileDownload.submit()">Peptides</a>
				</g:form>
			</div>   	
		 </div>	
		    		
    		<table id="gene_table_data" class="display">
			  <thead>
			  	<tr>
					<th><b>Transcript ID</b></th>
					<th><b>Length (bp)</b></th>
					<th><b>Start</b></th>
					<th><b>Stop</b></th>
			   </tr>
			  </thead>
			  <tbody>
			 	<g:each var="res" in="${results}">
			 		<tr>
						<td><a href="m_info?Gid=${Gid}&GFFid=${GFFid}&mid=${res.mrna_id}">${res.mrna_id}</a></td>
						<td>${res.nuc.length()}</td>
						<td>${res.start}</td>
						<td>${res.stop}</td>
			  		</tr>  
			 	</g:each>
			  </tbody>
			</table>			
    	</g:if>    	
    	<br>
			<g:if test ="${metaData.genome.gbrowse}">
				<hr size = 5 color="green" width="100%" style="margin-top:10px">
		 		<h1>Browse on the genome <a href="${metaData.genome.gbrowse}?name=${results.contig_id[0].trim()}:${results.start[0]}..${results.stop[0]}" target='_blank'>(go to genome browser)</a>:</h1>
		 		<iframe src="${metaData.genome.gbrowse}?name=${results.contig_id[0].trim()}:${results.start[0]}..${results.stop[0]}" width="100%" height="700" frameborder="0">
					<img src="${metaData.genome.gbrowse}?name=${results.contig_id[0].trim()}:${results.start[0]}..${results.stop[0]}"/>
		 		</iframe>
			</g:if>
    </g:if>
    <g:else>
	    <h1>There is no information for <b>${results.contig_id[0]}</b></h1>
    </g:else>
  </body>
</html>
