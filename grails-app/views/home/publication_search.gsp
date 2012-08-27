<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>Bicyclus anynana publications</title>
  <parameter name="publications" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.0/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
    <style type="text/css">
            @import "${resource(dir: 'js', file: 'DataTables-1.9.0/media/css/demo_page.css')}";
            @import "${resource(dir: 'js', file: 'DataTables-1.9.0/media/css/demo_table.css')}";
            @import "${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}";
    </style>
   <script>
    $(document).ready(function() {
            $('#table_data').dataTable({
    	    "sPaginationType": "full_numbers",
    	    "iDisplayLength": 10,
    	    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	    "oLanguage": {
    	     	     "sSearch": "Filter records:"
    	     },
    	    "aaSorting": [[ 3, "desc" ]],
    	    "sDom": 'T<"clear">lfrtip',
            "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
            }
         });
    });
    </script>
  </head>

  <body>
<g:if test="${pub_results}">
	<h1>A search of <i>Bicyclus anynana</i> publications for the term '<b>${searchId}</b>' returned ${pub_results.size()} results:</h1>
	<table id="table_data" class="display">
	<thead>
		<tr>
			<th><b>Title</b></th>
			<th><b>Authors</b></th>
			<th><b>Journal</b></th>
			<th><b>Date</b></th>
		</tr>
	</thead>
	<tbody>
	<g:each var="res" in="${pub_results}">
	<tr>
	<td><span class="dropt"><a href="http://www.ncbi.nlm.nih.gov/pubmed?term=${res.pubmed_id}" target='_blank'>${res.title}</a>
			<span style="width:90%;"><b>Abstract</b></br>${res.abstract_text}</span>
		</span>
	</td>
	<td>${res.authors}.</td>
	<td>${res.journal_short}</td>
	<td><g:formatDate format="yyyy MMM d" date="${res.date_string}"/></td>
	</tr>
	</g:each>
	</tbody>
	</table>
</g:if>
<g:else>
<h1>Your search for '<b>${searchId}</b>' returned no results in the publications. Please go back and try again.</h1>
</g:else>
</body>
</html>
