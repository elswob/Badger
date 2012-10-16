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
    <% 
    def pubjsonData = pubRes.encodeAsJSON(); 
    %>
    $(document).ready(function() {
            $('#trans_table').dataTable({
    	    "sPaginationType": "full_numbers",
    	    "iDisplayLength": 10,
    	    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	    "oLanguage": {
    	     	     "sSearch": "Filter records:"
    	     },
    	    "aaSorting": [[ 7, "desc" ]],
    	    "sDom": 'T<"clear">lfrtip',
            "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
            }
         });
         
         $('#gene_table').dataTable({
    	    "sPaginationType": "full_numbers",
    	    "iDisplayLength": 10,
    	    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	    "oLanguage": {
    	     	     "sSearch": "Filter records:"
    	     },
    	    "aaSorting": [[ 7, "desc" ]],
    	    "sDom": 'T<"clear">lfrtip',
            "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
            }
         });
         
         var anOpen = [];
		var sImageUrl = "${resource(dir: 'js', file: 'DataTables-1.9.0/examples/examples_support/')}";
		 
		var oTable = $('#pub_table').dataTable( {
			"bProcessing": true,
			"aaData": ${pubjsonData},
			"aoColumns": [
				{
				   "mDataProp": null,
				   "sClass": "control center",
				   "sDefaultContent": '<img src="'+sImageUrl+'details_open.png'+'">'
				},
				{ "mDataProp": "title",
				"fnRender": function ( oObj, sVal ){
					return "<a href=\"http://www.ncbi.nlm.nih.gov/pubmed?term="+oObj.aData["pubmed_id"]+ "\"target='_blank'>"+sVal+"</a>";
				}},
				{ "mDataProp": "authors" },
				{ "mDataProp": "journal_short" },
				{ "mDataProp": "date_out"},
				{ "mDataProp": "rank",
				"fnRender": function ( oObj, sVal ){
					return sVal.toFixed(3);
				}},
			],
			"sPaginationType": "full_numbers",
				"iDisplayLength": 10,
				"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
				"oLanguage": {
						 "sSearch": "Filter records:"
				 },
				"aaSorting": [[ 5, "desc" ]],
				"sDom": 'T<"clear">lfrtip',
				"oTableTools": {
				"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
				}
		} );
		
		$('#pub_table td.control').live( 'click', function () {
		  var nTr = this.parentNode;
		  var i = $.inArray( nTr, anOpen );
		   
		  if ( i === -1 ) {
			$('img', this).attr( 'src', sImageUrl+"details_close.png" );
			var nDetailsRow = oTable.fnOpen( nTr, fnFormatDetails(oTable, nTr), 'details' );
			$('div.innerDetails', nDetailsRow).slideDown();
			anOpen.push( nTr );
		  }
		  else {
			$('img', this).attr( 'src', sImageUrl+"details_open.png" );
			$('div.innerDetails', $(nTr).next()[0]).slideUp( function () {
			  oTable.fnClose( nTr );
			  anOpen.splice( i, 1 );
			} );
		  }
		} );
		 
		function fnFormatDetails( oTable, nTr )
		{
		  var oData = oTable.fnGetData( nTr );
		  var sOut =
			'<div class="innerDetails">'+
			  '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:10px;">'+
				'<tr><td><b>Abstract:</b><br><p align="justify">'+oData.abstract_text+'</p></td></tr>'+
			  '</table>'+
			'</div>';
		   //alert(sOut)
		  return sOut;
		}
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
  
  <div class="inline">
  <br><h1>Results for search of '<em>${searchId}</em>' across all data</h1> 
  <p>(searched all records in ${search_time})</p>
  </div><br>
  
  <g:if test="${transRes}">
  	<h2>${transRes.size()} matches from the transcriptome data:</h2>   
        <table id="trans_table" class="display">
            <thead>
              <tr>
                <th><b>Transcript</b></th>
                <th><b>Database</b></th>
                <th><b>Hit</b></th>
                <th><b>Description</b></th>
                <th><b>Start</b></th>
                <th><b>Stop</b></th>
                <th><b>Score</b></th>
                <th><b>Rank</b></th>
              </tr>
             </thead>
             <tbody>
               <g:each var="res" in="${transRes}">
                <tr>  
                  <td><g:link action="trans_info" params="${[contig_id: res.contig_id]}"> ${res.contig_id}</g:link></td>
                  <td>${res.anno_db}</td>
                  <%res.anno_id = res.anno_id.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>")%>
                  <%res.anno_id = res.anno_id.replaceAll(/lcl\|(.*)/, "<a href=\"http://www.uniprot.org/uniref/\$1\" target=\'_blank\'>\$1</a>")%>
                  <td>${res.anno_id}</td>
                  <%res.descr = res.descr.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>")%>
                  <td>${res.descr}</td>
                  <td>${res.anno_start}</td>
                  <td>${res.anno_stop}</td>
                  <td>${res.score}</td>
                  <td>${sprintf("%.3f",res.rank)}</td>
                </tr>                  
               </g:each>
              </tbody>
         </table> 
         <br>
  </g:if>
  <g:else>
 	<g:if test = "${grailsApplication.config.seqData.Transcriptome}">
  		<h2>0 matches from the transcriptome data.</h2>
  	</g:if>
  </g:else>
  
  <g:if test="${geneRes}">
    <hr size = 5 color="green" width="100%" style="margin-top:10px"> 
    <h2>${geneRes.size()} matches from the gene data:</h2> 
        <table id="gene_table" class="display">
            <thead>
              <tr>
                <th><b>Gene ID</b></th>
                <th><b>Database</b></th>
                <th><b>Hit ID</b></th>
                <th><b>Description</b></th>
                <th><b>Start</b></th>
                <th><b>Stop</b></th>
                <th><b>Score</b></th>
                <th><b>Rank</b></th>
              </tr>
             </thead>
             <tbody>
               <g:each var="res" in="${geneRes}">
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
                  <td>${sprintf("%.3f",res.rank)}</td>
                </tr>                  
               </g:each>
              </tbody>
         </table> 
         <br>
  </g:if>
  <g:else>
  	<g:if test = "${grailsApplication.config.seqData.GenePep}">
    	<hr size = 5 color="green" width="100%" style="margin-top:10px">
  		<h2>0 matches from the gene data.</h2>
  	</g:if>
  </g:else>
  
    <g:if test="${pubRes}">
    <hr size = 5 color="green" width="100%" style="margin-top:10px">  
  	<h2>${pubRes.size()} matches from the publication data:</h2> 
    	<table cellpadding="0" cellspacing="0" border="0" class="display" id="pub_table">
			<thead>
				<tr>
					<th></th>
					<th width="40%">Title</th>
					<th>Authors</th>
					<th>Journal</th>
					<th>Date</th>
					<th><b>Rank</b></th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
    </g:if>
    <g:else>
     <hr size = 5 color="green" width="100%" style="margin-top:10px">
  	 <h2>0 matches from the publication data.</h2>
    </g:else>
  
</body>
</html>


