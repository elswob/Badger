<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} search results</title>
    <parameter name="search" value="selected"></parameter>   
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasTextRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.highlighter.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.cursor.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.dateAxisRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.logAxisRenderer.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.bubbleRenderer.min.js')}" type="text/javascript"></script>  
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/data_table.css')}" type="text/css"></link>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}" type="text/css"></link>

    <script type="text/javascript">
        /* new sorting functions */
        jQuery.fn.dataTableExt.oSort['scientific-asc']  = function(a,b) {
        	var x = parseFloat(a);
        	var y = parseFloat(b);
        	return ((x < y) ? -1 : ((x > y) ?  1 : 0));
        };
 
        jQuery.fn.dataTableExt.oSort['scientific-desc']  = function(a,b) {
        	var x = parseFloat(a);
        	var y = parseFloat(b);
        	return ((x < y) ? 1 : ((x > y) ?  -1 : 0));
        };
    </script> 
    <script>
    function get_table_data(){
    	    var table_scrape = [];
	    var oTable = document.getElementById('table_data');
	    //gets table
	    var rowLength = oTable.rows.length;
	    //gets rows of table
	    for (i = 0; i < rowLength; i++){
	    //loops through rows
	       var oCells = oTable.rows.item(i).cells;
	       var cellVal = oCells.item(0).innerHTML;
	       var regex = /.*?contig.*/g;
	       var matcher = cellVal.match(/.*?contig_id=(contig_\d+).*/);
	       if (matcher){
	       	       table_scrape.push(matcher[1])
	       }
	    }
	    document.getElementById('fileId').value=table_scrape;
	    //alert(table_scrape)
    }
    </script>
    <script>

    function toggleDiv(divId) {
    	    $("#"+divId).slideToggle(20);
    }



    </script>
    <script>
    $(document).ready(function() {
            $('#table_data').dataTable({
    	    "sPaginationType": "full_numbers",
    	    "iDisplayLength": 10,
    	    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	    "oLanguage": {
    	     	     "sSearch": "Filter records:"
    	     },
    	    "aaSorting": [[ 4, "desc" ]],
    	    "sDom": 'T<"clear">lfrtip',
            "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
            }
         });
    	$('#ipr_table_data').dataTable({
    	    "sPaginationType": "full_numbers",
    	    "iDisplayLength": 10,
    	    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	    "oLanguage": {
    	     	     "sSearch": "Filter records:"
    	     },
    	    "aaSorting": [[ 4, "asc" ]],
    	    "aoColumns": [
                 null,
                 null,
                 null,
                 null,
                 { "sType": "scientific" }
            ],
    	    "sDom": 'T<"clear">lfrtip',
            "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
            }
         });
    });
    </script>
  </head>
  <body>
    
  <!-- check for errors -->
  <g:if test="${error == "empty"}">
    <h1>Please enter a search term</h1>
    <g:link action=''>Search Again</g:link>
  </g:if>
  <g:if test="${error == "too_short"}">
    <h1>Your search term is too short, it needs to be at least two characters, please <g:link action=''>search Again</g:link></h1>
  </g:if>
  <g:if test="${error == "no_anno"}">
    <h1>Please select some annotations</h1>
    <g:link action=''>Search Again</g:link>
  </g:if>
  
  <!-- check for gene annotation searches -->
  <g:if test="${anno}">
            <h1>Results for gene annotation description matching '<em>${term}</em>'.</h1> 
            <p>Searched ${bicyclus_anynana.GeneAnno.count()} records in ${search_time}</p>
            <g:if test="${results}">
            <p>Found ${uniques} genes with ${results.size()} hits</p>
            <g:link action=''>Search Again</g:link>           
              <table id="table_data" class="display">
                <thead>
                <tr>
                  <th><b>Gene ID</b></th>
                  <th><b>Database</b></th>
                  <th><b>Hit ID</b></th>
                  <th><b>Description</b></th>
                  <th><b>Start</b></th>
                  <th><b>Stop</b></th>
                  <th><b>Score</b></th>
                </tr>
                </thead>
                <tbody>
                  <g:each var="res" in="${results}">
                  <tr>  
                    <td><g:link action="gene_info" params="${[gene_id: res.gene_id]}"> ${res.gene_id}</g:link></td>
                    <td>${res.anno_db}</td>
                    <%res.anno_id = res.anno_id.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>")%>
                    <%res.anno_id = res.anno_id.replaceAll(/lcl\|(.*)/, "<a href=\"http://www.uniprot.org/uniref/\$1\" target=\'_blank\'>\$1</a>")%>
                    <td>${res.anno_id}</td>
                    <%res.descr = res.descr.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>")%>
                    <td>${res.descr}</td>
                    <td>${res.anno_start}</td>
                    <td>${res.anno_stop}</td>
                    <td>${res.score}</td>
                  </tr>                  
                </g:each>
                </tbody>
              </table>
          </g:if>
          <g:else>
          	<p>Found <strong>${resCount}</strong> hits.</p>
          	<g:link action=''>Search Again</g:link>
          </g:else>
  </g:if>
  <!-- check for trans annotation searches -->
  <g:if test="${uni}">
            <h1>Results for transcriptome annotation descriptions matching '<em>${term}</em>'.</h1>
		<g:if test="${results}">
		    		<table class="table_border" width='100%'>
		    		<tr><td>
		    		<% if (annoType == '1'){ %>
		    			Searched ${sprintf("%,d\n",badger.TransBlast.count())} records in ${search_time}.<br>
		    		<% }else{ %>
		    			Searched ${sprintf("%,d\n",badger.TransAnno.count())} records in ${search_time}.<br>
		    		<% } %>
		    			Found ${uniques} contigs with ${results.size()} top hits from distinct databases
		    		</td><td><center>
		    		<!-- download contigs form gets fileName value from get_table_data() -->
		    		<g:form name="fileDownload" url="[controller:'FileDownload', action:'trans_contig_download']">
		    			<g:hiddenField name="fileId" value=""/>
		    			<g:hiddenField name="fileName" value="${term}"/>
		    			<input align="right" type="submit" value="Download contigs in table" class="mybuttons" onclick="get_table_data()"/>
		    		</g:form>
		    		</center>
		    		</td></tr>
		    		</table>

			    <% if (annoType == '3'){ %><table id='ipr_table_data' class="display">  <% }else{ %><table id="table_data" class="display"> <% } %>    
			      <thead>
				<tr>
				  <th><b>Transcript ID</b></th>
				  <th><b>Database</b></th>
				  <th><b>Hit ID</b></th>
				  <th><b>Description</b></th>
				  <th><b>Score</b></th>
				</tr>
				</thead>
				<tbody>
				  <g:each var="res" in="${results}">
				  <% if (res.anno_db == 'SwissProt'){ %> <tr class='gradeA'>  <% } %>
				  <% if (res.anno_db == 'UniRef90'){ %> <tr class='gradeB'>  <% } %>
				  <% if (res.anno_db == 'EST others'){ %> <tr class='gradeC'>  <% } %>
				  <% if (res.anno_db == 'GO' || res.anno_db == 'EC' || res.anno_db == 'KEGG'){ %> <tr class='gradeD'>  <% } %>
				    <td><g:link action="trans_info" params="${[contig_id: res.contig_id]}"> ${res.contig_id}</g:link></td>
				    <td>${res.anno_db}</td>
				    <%
				    res.anno_id = res.anno_id.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>")
				    res.anno_id = res.anno_id.replaceAll(/lcl\|(.*)/, "<a href=\"http://www.uniprot.org/uniref/\$1\" target=\'_blank\'>\$1</a>")
				    res.anno_id = res.anno_id.replaceAll(/(^\d+\.\d+\.\d+\.\d+)/, "<a href=\"http://enzyme.expasy.org/EC/\$1\" target=\'_blank\'>\$1</a>")
				    res.anno_id = res.anno_id.replaceAll(/(GO:\d+)/, "<a href=\"http://www.ebi.ac.uk/QuickGO/GTerm?id=\$1\" target=\'_blank\'>\$1</a>")
				    res.anno_id = res.anno_id.replaceAll(/(^K\d+)/, "<a href=\"http://www.genome.jp/dbget-bin/www_bget?ko:\$1\" target=\'_blank\'>\$1</a>")
				    res.anno_id = res.anno_id.replaceAll(/(^IPR\d+)/, "<a href=\"http://www.ebi.ac.uk/interpro/IEntry?ac=\$1\" target=\'_blank\'>\$1</a>")
				    %>
				    <td>${res.anno_id}</td>
				    <%res.descr = res.descr.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>")%>
				    <td>${res.descr}</td>
				    <td>${res.score}</td>
				  </tr>  
				</g:each>
				</tbody>
			      </table>

          </g:if>
          <g:else>
          	<p>Found <strong>0</strong> hits.</p>
          	<g:link action=''>Search Again</g:link>
          </g:else>
  </g:if>
   <!-- check for scaffold searches -->
  <g:if test="${contig}">
      <g:if test = "${contigs.size()}">
    <h1>Information for <em>${term}</em></h1>
      <table>
        <tr>
          <td><b>Stats</b></td>
          <td><b>Genes</b></td>
          <td><b>Links</b></td>
          <td><b>Data</b></td>
        </tr>
        <g:each var="contig" in="${contigs}">
          <tr>
            <td>
              Contig length = ${contig.sequence.length()}<br>
              GC = ${contig.gc}<br>
              Estimated coverage = ${contig.coverage}<br>
            </td>
            <td>Number of genes = ${genes.size()}</td>
            <td><a href ="">Browse</a></td>
            <td><g:link controller="FileDownload" action="contig_download" params="${[fileId : fasta, fileName: term+'.txt']}">Contigs</g:link></td>
          </tr>
      </g:each>
      </table>
  </g:if>
  <g:elseif test ="${term == ''}" >
    <h1>Please enter a search term!</h1>
  </g:elseif>
  <g:else>
    <h1>There is no contig with the ID <em>${term}</em>.</h1> 
    <g:link action=''>Search Again</g:link>
  </g:else> 
  </g:if>
  

  
  
  
  
  
  
</body>
</html>


