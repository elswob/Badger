<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} genome detail</title>
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
	       var matcher = cellVal.match(/.*?gene_id=(.*?)">.*/);
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
    <g:if test="${info_results}">
    <g:link action="">Search</g:link> > <g:link action="species">Species</g:link> > <g:link action="species_search" params="${[Gid:params.Gid]}"><i> ${metaData.genus} ${metaData.species}</i></g:link> > Scaffold: ${info_results.contig_id[0]}
    
    <h1>Information for <b>${info_results.contig_id[0]}</b>:</h1>
    <table>
      <tr>
        <td><b>Length:</b> </td>
        <g:if test = "${grailsApplication.config.coverage.Genome == 'y'}">
        	<td><b>Coverage: </b> </td>
        </g:if>
        <td><b>GC: </b> </td>
        <td><b>Sequence: </b> </td> 
      </tr>
      <tr>
      <td>${printf("%,d\n",info_results.sequence[0].length())}</td>
      <g:if test = "${grailsApplication.config.coverage.Genome == 'y'}">
      	<td>${info_results.coverage[0]}</td>
      </g:if>
      <td>${sprintf("%.2f",info_results.gc[0])}</td>
      <td>        
      	<g:form name="fileDownload" url="[controller:'FileDownload', action:'genome_contig_download']" style="display: inline" >
        	<g:hiddenField name="fileId" value="${info_results.contig_id[0]}"/>
			<g:hiddenField name="fileName" value="${info_results.contig_id[0]}"/>
			<a href="#" onclick="document.fileDownload.submit()">Download</a>
		</g:form>
	  </td>
	  </tr>
    </table>
    	
    	<g:if test="${gene_results}">
    	<hr size = 5 color="green" width="100%" style="margin-top:10px">
    	<div class="inline">
    	<br>
    	 <h1>${printf("%,d\n",gene_results.size())} transcripts</b>:</h1>
			<!-- download genes form gets fileName value from get_table_data() -->		    		
			 <!--div style="right:0px;">
				 &nbsp;&nbsp;(Download sequences:
					<g:form name="nucfileDownload" url="[controller:'FileDownload', action:'gene_download']">
					<g:hiddenField name="nucFileId" value=""/>
					<g:hiddenField name="fileName" value="${info_results.contig_id[0]}.genes"/>
					<g:hiddenField name="seq" value="Nucleotides"/>
					<a href="#" onclick="get_table_data('nucFileId');document.nucfileDownload.submit()">Nucleotides</a>
				</g:form> 
				|
				<g:form name="pepfileDownload" url="[controller:'FileDownload', action:'gene_download']">
					<g:hiddenField name="pepFileId" value=""/>
					<g:hiddenField name="fileName" value="${info_results.contig_id[0]}.genes"/>
					<g:hiddenField name="seq" value="Peptides"/>
					<a href="#" onclick="get_table_data('pepFileId');document.pepfileDownload.submit()">Peptides</a>
				</g:form>
				)	 
			</div-->   	
		 </div>	
		    		
    		<table id="gene_table_data" class="display">
			  <thead>
			  	<tr>
					<th><b>Gene ID</b></th>
					<th><b># transcripts</b></th>
					<th><b>Mean length</b></th>
					<th><b>Mean start</b></th>
					<th><b>Mean end</b></th>
			   </tr>
			  </thead>
			  <tbody>
			 	<g:each var="res" in="${gene_results}">
			 		<tr>
						<td><a href="g_info?Gid=${params.Gid}&gid=${res.gene_id}">${res.gene_id}</a></td>
						<td>${res.count}</td>
						<td>${sprintf("%.0f",res.a_nuc)}</td>
						<td>${sprintf("%.0f",res.a_start)}</td>
						<td>${sprintf("%.0f",res.a_stop)}</td>
			  		</tr>  
			 	</g:each>
			  </tbody>
			</table>			
    	</g:if>    	
    	<br>
		<g:if test ="${metaData.gbrowse}"> 
			<hr size = 5 color="green" width="100%" style="margin-top:10px">
			<h1>Browse on the genome <a href="${metaData.gbrowse}?name=${info_results.contig_id[0].trim()}" target='_blank'>(go to genome browser)</a>:</h1>
			 <iframe src="${metaData.gbrowse}?name=${info_results.contig_id[0].trim()}" width="100%" height="700" frameborder="0">
				<img src="${metaData.gbrowse}?name=${info_results.contig_id[0].trim()}"/>
			 </iframe>
		</g:if>
    </g:if>
    <g:else>
	    <h1>There is no information for <b>${info_results.contig_id[0]}</b></h1>
    </g:else>
  </body>
</html>
