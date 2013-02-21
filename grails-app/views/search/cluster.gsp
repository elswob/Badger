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
    
    function get_table_data(table){
    	var table_scrape = [];
    	var rowNum
    	var regex
	    var oTableData = document.getElementById('cluster');
	    rowNum = 1
	    
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
	    
	    if (table == 'orthoPep'){
	    	//alert('adding '+table_scrape+' to orthoPepFileId')
		    document.getElementById('orthoPepFileId').value=table_scrape;
		}else if (table == 'orthoNuc'){
			document.getElementById('orthoNucFileId').value=table_scrape;
		}else if (table == 'orthoClusterNuc'){
			document.getElementById('orthoClusterNucFileId').value=table_scrape;
		}else if (table == 'orthoClusterPep'){
			document.getElementById('orthoClusterPepFileId').value=table_scrape;
		}
	    
	    //alert(table_scrape)
	    return table_scrape;
	    
    }
    
   <% 
    def jsonData = data.encodeAsJSON(); 
    println jsonData;
    %>

  $(document).ready(function() {

    var oTable = $('#cluster').dataTable( {
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
<g:link action="">Search</g:link> > <g:link action="ortho">Search orthologs</g:link> > Cluster
	<h1>Cluster data:</h1>
	<div class="inline">
		Download (sequences in table only): 
		<g:form name="orthonucfileDownload" url="[controller:'FileDownload', action:'gene_download']">
			<g:hiddenField name="orthoNucFileId" value=""/>
			<g:hiddenField name="fileName" value="cluster.${params.group_id}.orthologs"/>
			<g:hiddenField name="seq" value="OrthoNucleotides"/>
			<a href="javascript:void(0);" onclick="get_table_data('orthoNuc');document.orthonucfileDownload.submit()">Nucleotides</a>
		</g:form> 
		|
		<g:form name="orthopepfileDownload" url="[controller:'FileDownload', action:'gene_download']">
			<g:hiddenField name="orthoPepFileId" value=""/>
			<g:hiddenField name="fileName" value="cluster.${params.group_id}.orthologs"/>
			<g:hiddenField name="seq" value="OrthoPeptides"/>
			<a href="javascript:void(0);" onclick="get_table_data('orthoPep');document.orthopepfileDownload.submit()">Peptides</a>
		</g:form>
	</div>	
	<g:if test = "${grailsApplication.config.musclePath}">
		<div class="inline">
			<g:if test="${data.size() < 21}">
				Cluster (sequences in table only): 
				<g:form name="orthoNucCluster" url="[action:'runCluster']">
					<g:hiddenField name="orthoClusterNucFileId" value=""/>
					<g:hiddenField name="fileName" value="cluster.${params.group_id}.orthologs"/>
					<g:hiddenField name="seq" value="nuc"/>
					<g:hiddenField name="group_id" value="${params.group_id}"/>
					<a href="javascript:void(0);" onclick="get_table_data('orthoClusterNuc');document.orthoNucCluster.submit()">Nucleotides</a>
				</g:form> 
				|
				<g:form name="orthoPepCluster" url="[action:'runCluster']">
					<g:hiddenField name="orthoClusterPepFileId" value=""/>
					<g:hiddenField name="fileName" value="cluster.${params.group_id}.orthologs"/>
					<g:hiddenField name="seq" value="pep"/>
					<g:hiddenField name="group_id" value="${params.group_id}"/>
					<a href="javascript:void(0);" onclick="get_table_data('orthoClusterPep');document.orthoPepCluster.submit()">Peptides</a>
				</g:form>					
			</g:if>
			<g:else>
				Clustering only available for groups of 20 or less
			</g:else>
		</div>
	</g:if>
	  <table cellpadding="0" cellspacing="0" border="0" class="display" id="cluster">
		<thead>
			<tr>
				<th>Species</th>
				<th>Transcript</th>
				<th>Length (bp)</th>
				<th>Exons</th>
				<th>Top BLAST</th>
				<th>Top domain</th>
			</tr>
		</thead>
		<tbody>
			<g:each var="res" in="${data}">
				<tr>
					<td><i>${res.gene.file.genome.meta.genus[0]}. ${res.gene.file.genome.meta.species}</i></td>
					<td><g:link action="m_info" params="${[mid: res.gene.mrna_id]}"> ${res.gene.mrna_id}</g:link></td>
					<td>${res.gene.nuc.size()}</td>
					<td>${res.gene.exon.size()}</td>
					<td><g:if test = "${res.gene.gblast}"><% def top = res.gene.gblast.findAll().sort({-it.score})[0]; if (top.descr.size()>200){ top.descr = top.descr[0..200]+" ... "};%>${top.descr} <br>Bit score: <span style="color:blue">${top.score}</g:if><g:else>No BLAST hits</g:else></td>
					<td><g:if test = "${res.gene.ginter}"><% def top = res.gene.ginter.findAll().sort({it.score})[0]; if (top.descr.size()>200){ top.descr = top.descr[0..200]+" ... "};%>${top.descr} <br>Evalue: <span style="color:blue">${top.score}</g:if><g:else>No domain hits</g:else></td>
				</tr>
			</g:each>
		</tbody>
	</table>

</body>
</html>

