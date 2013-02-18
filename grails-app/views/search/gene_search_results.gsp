<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} search results</title>
    <parameter name="search" value="selected"></parameter>   
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
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
    function get_table_data(type){
    	var table_scrape = [];
    	var rowNum
    	var regex
    	if (${annoType} === 1){ 
	    	var oTableData = document.getElementById('table_data');
	    	rowNum = 1
	    }else if (${annoType} === 2){ 
	    	var oTableData = document.getElementById('func_table_data');
	    	rowNum = 0
	    }else if (${annoType} === 3){ 
	    	var oTableData = document.getElementById('ipr_table_data');
	    	rowNum = 0
	    }
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
	    if (type == 'pep'){
		    document.getElementById('pepFileId').value=table_scrape;
		}else{
			document.getElementById('nucFileId').value=table_scrape;
		}
    }
    </script>
    <script>
    
    <% 
    def jsonData = results.encodeAsJSON(); 
    def jsonAnno = annoLinks.encodeAsJSON(); 
    %>

    </script>
    <script>
 
  $(document).ready(function() {
 
  var anOpen = [];
  var sImageUrl = "${resource(dir: 'js', file: 'DataTables-1.9.4/examples/examples_support/')}";
     
	  if (${annoType} === 1){ 
		var oTable = $('#table_data').dataTable( {
			"bProcessing": true,
			"aaData": ${jsonData},
			"aoColumns": [
				{
				   "mDataProp": null,
				   "sClass": "control center",
				   "sDefaultContent": '<img src="'+sImageUrl+'details_open.png'+'">'
				},
				{ "mDataProp": "mrna_id",
				"fnRender": function ( oObj, sVal ){
					return "<a href=\"m_info?Gid=${params.Gid}&GFFid=${params.GFFid}&mid="+oObj.aData["mrna_id"]+"\">"+sVal+"</a>";				}},
				{ "mDataProp": "anno_db" },
				{ "mDataProp": "anno_id",
				"fnRender": function ( oObj, sVal ){
					AnnoData = ${jsonAnno};
					var db = oObj.aData["anno_db"]
					if (AnnoData[db]){
						var regex = new RegExp(AnnoData[db][0]);
						var link = sVal.replace(regex,"<a href=\""+AnnoData[db][1]+"$1 \" target='_blank'>$1</a>")
					}
					return link
				}},
				{ "mDataProp": "descr",
				"fnRender": function ( oObj, sVal ){
					if (oObj.aData["descr"].length>200){
						return oObj.aData["descr"].substring(0,200)+" ..."
					}else{
						return oObj.aData["descr"]
					}
				}},
				{ "mDataProp": "score"},
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
		
	   
	  $('#table_data td.control').live( 'click', function () {
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
		'<div class="blast_res">'+
		  '<table width="100%" cellpadding="5" cellspacing="0" border="0" style="table-layout:fixed; padding-left:10px; overflow:auto;">'+
			'<tr><td><b>Alignment info:</b> Length='+oData.align+' Gaps='+oData.gaps+' Identity='+oData.identity+'</td></tr>'+
			'<tr><td><b>'+oData.mrna_id+'</b> '+oData.anno_start+' '+oData.anno_stop+'</td></tr>'+
			'<tr><td>'+oData.qseq+'</td></tr>'+
			'<tr><td>'+oData.midline+'</td></tr>'+
			'<tr><td>'+oData.hseq+'</td></tr>'+
			'<tr><td><b>'+oData.anno_id+'</b> '+oData.hit_start+' '+oData.hit_stop+'</td></tr>'+
		  '</table>'+
		'</div>'+  
		'</div>';
	   //alert(sOut)
	  return sOut;
	}
  }else if (${annoType} === 2){
  //for func annotations  
	var iprTable = $('#func_table_data').dataTable( {
        "bProcessing": true,
        "aaData": ${jsonData},
        "aoColumns": [
          	{ "mDataProp": "mrna_id",
				"fnRender": function ( oObj, sVal ){
					return "<a href=\"m_info?Gid=${params.Gid}&GFFid=${params.GFFid}&mid="+oObj.aData["mrna_id"]+"\">"+sVal+"</a>";
			}},
            { "mDataProp": "anno_db" },
            { "mDataProp": "anno_id",
				"fnRender": function ( oObj, sVal ){
					AnnoData = ${jsonAnno};
					var db = oObj.aData["anno_db"]
					if (AnnoData[db]){
						var regex = new RegExp(AnnoData[db][0]);
						var link = sVal.replace(regex,"<a href=\""+AnnoData[db][1]+"$1 \" target='_blank'>$1</a>")
					}
					return link
				}},
            { "mDataProp": "descr",
				"fnRender": function ( oObj, sVal ){
					if (oObj.aData["descr"].length>200){
						return oObj.aData["descr"].substring(0,200)+" ..."
					}else{
						return oObj.aData["descr"]
					}
				}},
            { "mDataProp": "score"},
        ],
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
    	} 	);
	}else if (${annoType} === 3){
	  //for interproscan and it's evalue sorting
		var iprTable = $('#ipr_table_data').dataTable( {
			"bProcessing": true,
			"aaData": ${jsonData},
			"aoColumns": [
				{ "mDataProp": "mrna_id",
				"fnRender": function ( oObj, sVal ){
					return "<a href=\"m_info?Gid=${params.Gid}&GFFid=${params.GFFid}&mid="+oObj.aData["mrna_id"]+"\">"+sVal+"</a>";
				}},
				{ "mDataProp": "anno_db" },
				{ "mDataProp": "anno_id" ,
				"fnRender": function ( oObj, sVal ){
					var splitter = sVal.split(" ");
					if (splitter[0].match(/IPR/g)){ 
						return "<a href=\"http://www.ebi.ac.uk/interpro/IEntry?ac="+splitter[0]+ "\"target='_blank'>"+splitter[0]+"</a>: "+splitter[2]+"";
					}else{
						return "<a href=\"http://www.ebi.ac.uk/QuickGO/GTerm?id="+sVal+ "\"target='_blank'>"+sVal+"</a>";
					}
				}},
				{ "mDataProp": "descr",
				"fnRender": function ( oObj, sVal ){
					if (oObj.aData["descr"].length>200){
						return oObj.aData["descr"].substring(0,200)+" ..."
					}else{
						return oObj.aData["descr"]
					}
				}},
				{ "mDataProp": "score", "sType": "scientific"},
			],
			"sPaginationType": "full_numbers",
			"iDisplayLength": 10,
			"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
			"oLanguage": {
					 "sSearch": "Filter records:"
			 },
			"aaSorting": [[ 4, "asc" ]],
			"sDom": 'T<"clear">lfrtip',
			"oTableTools": {
			"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
				}
			} 	);
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
  <g:link action="">Search</g:link> > <g:link action="species">Species</g:link> > <g:link action="species_v" params="${[Sid:metaData.genome.meta.id]}"><i> ${metaData.genome.meta.genus} ${metaData.genome.meta.species}</i></g:link> > Genome: <g:link action="species_search" params="${[Gid:params.Gid,GFFid:params.GFFid]}">v${metaData.file_version}</g:link> > Search results
    <h1>Results for gene annotation descriptions matching '<em>${term}</em>'.</h1>
		<g:if test="${results}">
		    <table class="table_border" width='100%'>
		    	<tr><td>
		    		Searched ${sprintf("%,d\n",badger.GeneBlast.count() + badger.GeneAnno.count())} records in ${search_time}.<br>
		    		Found ${uniques} genes with ${results.size()} top hits from distinct databases
		    		</td><td><center>
		    		Download genes:
		    		<!-- download genes form gets fileName value from get_table_data() -->
		    		<div class="inline">
        				<g:form name="nucfileDownload" url="[controller:'FileDownload', action:'gene_download']">
		    			<g:hiddenField name="nucFileId" value=""/>
		    			<g:hiddenField name="fileName" value="${term}"/>
		    			<g:hiddenField name="seq" value="Nucleotides"/>
		    			<a href="#" onclick="get_table_data('nuc');document.nucfileDownload.submit()">Nucleotides</a>
		    		</g:form> 
		    		|
		    		<g:form name="pepfileDownload" url="[controller:'FileDownload', action:'gene_download']">
		    			<g:hiddenField name="pepFileId" value=""/>
		    			<g:hiddenField name="fileName" value="${term}"/>
		    			<g:hiddenField name="seq" value="Peptides"/>
		    			<a href="#" onclick="get_table_data('pep');document.pepfileDownload.submit()">Peptides</a>
		    		</g:form>
		    		</div>
		    		</center>
		    		</td></tr>
		    		</table>
		    	<% if (annoType == "1"){ %>
				<table id='table_data' class="display">  	
				<% }else if (annoType == "2"){ %>
				<table id='func_table_data' class="display">  
			    <% }else if (annoType == "3"){ %>
			    <table id='ipr_table_data' class="display">  
			    <% } %>   
			    <thead>
				<tr>
				<% if (annoType == "1"){ %>
				  <th></th>
				<% } %>
				  <th><b>Transcript ID</b></th>
				  <th><b>Database</b></th>
				  <th><b>Hit ID</b></th>
				  <th width=50%><b>Description</b></th>
				  <th><b>Score</b></th>
				</tr>
				</thead>
				<tbody>
				</tbody>
			    </table>

          </g:if>
          <g:else>
          	<p>Found <strong>0</strong> hits.</p>
          	<g:link action=''>Search Again</g:link>
          </g:else>
</body>
</html>


