<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} | Publication search</title>
  <parameter name="publications" value="selected"></parameter>
  <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
  <script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
  <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
  <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
  <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  <link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/data_table.css')}" type="text/css"></link>
  <link rel="stylesheet" href="${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}" type="text/css"></link>

  
  <script type="text/javascript"> 
    
   <% 
    def jsonData = pub_results.encodeAsJSON(); 
    //println jsonData;
    //return "${formatDate(format:'dd\/MM\/yyyy',date:'2011-05-05 00:00:00')}";
    %>

  $(document).ready(function() {
  
  var anOpen = [];
    var sImageUrl = "${resource(dir: 'js', file: 'DataTables-1.9.4/examples/examples_support/')}";
     
    var oTable = $('#example').dataTable( {
        "bProcessing": true,
        "aaData": ${jsonData},
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
    } );
         
  $('#example td.control').live( 'click', function () {
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
<div class="bread"><g:link action="">Home</g:link> > <g:link action="publications">Publications</g:link> > Search results </div>
<g:if test="${pub_results}">
<g:if test="${sp}">
	<h1>Your search for '<b>${sp} ${searchId}</b>' returned ${sprintf("%,d\n",pub_results.size())} publications</h1>
</g:if>
<g:else>
	<h1>Your search for '<b>${searchId}</b>' returned ${sprintf("%,d\n",pub_results.size())} publications</h1>
</g:else>
  <table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
    <thead>
        <tr>
            <th></th>
            <th width="30%">Title</th>
            <th width="40%">Authors</th>
            <th>Journal</th>
            <th>Date</th>
        </tr>
    </thead>
    <tbody></tbody>
</table>
</g:if>
<g:else>
<h1>Your search for '<b>${searchId}</b>' returned no results in the publications. Please go back and try again.</h1>
</g:else>
</body>
</html>
