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
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.0/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
    <style type="text/css">
            @import "${resource(dir: 'js', file: 'DataTables-1.9.0/media/css/demo_page.css')}";
            @import "${resource(dir: 'js', file: 'DataTables-1.9.0/media/css/demo_table.css')}";
            @import "${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}";
    </style>

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
    function zip(arrays) {
            	    return arrays[0].map(function(_,i){
            	    return arrays.map(function(array){return array[i]})
           });
    }
    function toggleDiv(divId) {
    	    $("#"+divId).slideToggle(20);
    }
    function changed(plot_type,params) {
	$("#chart").html('Loading...<img src="${resource(dir: 'images', file: 'spinner.gif')}" />');
	setTimeout(""+plot_type+"('"+params+"')", 1000);
    }

    <% 
    def jsonData = chartData.encodeAsJSON();  
    //println jsonData;
    %>
    var loadcheck = "no";
    function loadPlotData(){ 
    	     if (loadcheck == "no"){
		    //load the jqplot data
		    ContigData = ${jsonData};
		    //alert("loading the data")
		    for (var i = 0; i < ContigData.length; i++) {   		 	 
			    var hit = ContigData[i];
			    dlen.push(hit.length);
			    dcov.push(hit.coverage);
			    dgc.push(hit.gc);
			    dcon.push(hit.contig_id);
			    dsco.push(hit.score);
		    }
		    loadcheck = "yes";
		    setTimeout("makeArrays('len_cov')", 1000);
		    //add the click data here as adding it at the top causes multiple windows to open
		    $('#chart').bind('jqplotDataClick',
		    function (ev, seriesIndex, pointIndex, data) {
			//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
			window.open("/search/trans_info?contig_id=" + data[3]);
			}
		    );
	     }
    }
    //set the global variable for the plots
    var dlen = [], dcov = [], dgc = [], dcon = [], dcum = [], dcou = [], dsco = [];
    var joinArray = [];
    var xaxis_label="", yaxis_label="", title_label="", xaxis_type="", yaxis_type="";
    function makeArrays(arrayInfo){
    	    $("#chart").text('');
	    //alert(arrayInfo)	    
	    if (arrayInfo == 'len_gc'){
		    joinArray = zip([dgc,dlen,dsco,dcon,dlen,dgc,dcov]);
		    xaxis_label = "GC";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Length";
		    yaxis_type = $.jqplot.LogAxisRenderer;
		    title_label = "Length vs GC";
	    }else if (arrayInfo == 'cov_gc'){
		    joinArray = zip([dgc,dcov,dsco,dcon,dlen,dgc,dcov]);
		    xaxis_label = "GC";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Coverage";
		    yaxis_type = $.jqplot.LogAxisRenderer;
		    title_label = "Coverage vs GC";
	    }else if (arrayInfo == 'len_cov'){
		    joinArray = zip([dlen,dcov,dsco,dcon,dlen,dgc,dcov]);
		    xaxis_label = "Length";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Coverage";
		    yaxis_type = $.jqplot.LogAxisRenderer;
		    title_label = "Length vs Coverage";
	    }
	    graphDraw()
	    
    }
        function graphDraw(){
            //alert(joinArray)
    	    $('#chart').empty();
	    plot = $.jqplot('chart',[joinArray],{
		title: title_label, 
		seriesDefaults:{
		    renderer: $.jqplot.BubbleRenderer,
		       rendererOptions: {
			bubbleAlpha: 0.6,
			highlightAlpha: 0.8,
			showLabels: false,
			autoscalePointsFactor: -0.15,
			autoscaleMultiplier: 0.5,
			varyBubbleColors: false,
			color: 'green'
		    },	
		},
		highlighter: {
			 tooltipAxes: 'yx',
			 yvalues: 6,
			 show: true,
			 sizeAdjust: 7.5,
			 formatString: '<span style="display:none">%s</span>Score: %s<br>Contig ID: %s<br>Length: %s<br>GC: %.2f<br>Coverage: %.2f'
	
		 },
		 cursor:{
		 	 show: true,
		 	 zoom:true,
		 	 tooltipLocation:'nw'
		 },
		 axesDefaults: {
			 labelRenderer: $.jqplot.CanvasAxisLabelRenderer
		 },
		 axes: {
			xaxis: {
				label: xaxis_label,
				renderer: xaxis_type,
				pad: 0
			},
			yaxis: {
				label: yaxis_label,
				renderer: yaxis_type,
				pad: 0,
				tickOptions: {
					formatString: "%'i"
				}
			}
		 },
	    });
	    $('.button-reset').click(function() { plot.resetZoom() });
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
		
			<div id="showChart" style="display:none">
		    		<input type="button" class="mybuttons" value="Table" onclick="toggleDiv('showChart');toggleDiv('hideChart');">
		    		<input type="button" class="blankbutton" value="Chart">
			    	
		    		<table class="table_border" width='100%'>
			    	<tr><td>
		    			Searched ${printf("%,d\n",GDB.TransAnno.count())} records in ${search_time}.<br>
		    			Found ${uniques} contigs with ${results.size()} top hits from distinct databases
		    		</td></tr>
		    		</table>
				
		    		<!-- add the chart-->
				    <table><tr><td>
					<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','len_gc')" value="Length vs GC"/>
					<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','cov_gc')" value="Coverage vs GC"/>
					<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','len_cov')" value="Length vs Coverage"/>
					</td></tr>
					<tr><td><p>Zoom in by dragging around an area. Reset by double clicking or clicking <font STYLE="cursor: pointer" color="green" class="button-reset">here</font></td></tr>
				   </table>	    
			    <div id="chart" class="jqplot-target" style="height: 500px; width: 100%; position: center;"> Loading...<img src="${resource(dir: 'images', file: 'spinner.gif')}" </div>
			</div> 
		    	</div>
		    	<div id="hideChart">
		    	   
		    		<input type="button" class="blankbutton" value="Table">
		    		<input type="button" class="mybuttons" value="Chart" onclick="toggleDiv('showChart');toggleDiv('hideChart');loadPlotData()">
	
		    		<table class="table_border" width='100%'>
		    		<tr><td>
		    			Searched ${printf("%,d\n",GDB.TransAnno.count())} records in ${search_time}.<br>
		    			Found ${uniques} contigs with ${results.size()} top hits from distinct databases
		    		</td><td><center>
		    		<!-- download contigs form gets fileName value from get_table_data() -->
		    		<g:form name="fileDownload" url="[controller:'FileDownload', action:trans_contig_download']">
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
			   </div>
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


