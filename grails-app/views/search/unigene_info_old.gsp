<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>UniGene Info</title>
    <parameter name="search" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.0/media/js/jquery.js')}" type="text/javascript"></script> 
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.0/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
    <style type="text/css">
            @import "${resource(dir: 'js', file: 'DataTables-1.9.0/media/css/demo_page.css')}";
            @import "${resource(dir: 'js', file: 'DataTables-1.9.0/media/css/demo_table.css')}";
            @import "${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}";
    </style>
    <script type="text/javascript" charset="utf-8">
    var range = '';
    $(document).ready(function() {
    	$('#table_data').dataTable({
    	    "sPaginationType": "full_numbers",	
    	    "aaSorting": [[ 5, "desc" ]],
    	    "sDom": 'T<"clear">lfrtip',
            "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
            }
         });
         //$(document).data('mysite.option', $("#table_data_info").html(););
        //range = $("#table_data_info").html();
        range = $("#table_data").html();
        //$(document).data('mysite.option', range);
        alert(range);
        //window.range = $("#table_data_info").html();
        drawHits();
    });
    </script>
  </head>
  <body>
  <g:if test="${info_results}">
    <h1>Stats for UniGene <b>${info_results.contig_id[0]}</b>:</h1>
    <table>
      <tr>
        <td><b>Length: </b></td>
        <td>${info_results.sequence[0].length()}</td>
        <!--td><b>Coverage</b></td-->
        <td><b>Sequence: </b></td>
        <td><g:link controller="FileDownload" action="contig_download" params="${[fileId : nuc_fasta, fileName: info_results.contig_id[0]+'.fa']}">Download</g:link></td>
      </tr>
    </table>
      
    <g:if test="${anno_results}">
    	 <% 
      def jsonData = anno_show.encodeAsJSON();
      //def jsonData = anno_results.encodeAsJSON();
      println jsonData
      %>    
      <div id = "blast_fig">
         <script type="text/javascript" src="${resource(dir: 'js', file: 'raphael-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'g.raphael-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'g.line-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'biodrawing.js')}"></script>
         <script type="text/javascript">       
         function drawHits(){
		 data_full = ${jsonData};
		 var range_edit = range;
		 //var range = document.getElementById("table_data_info").value;
		 //alert(range);
		 //document.write(range);
		 //alert($("#table_data_info").html());
		 data = data_full.slice(0,3);
		 var paperWidth = $('#blast_fig').width() - 40;
		 var drawing = new BioDrawing();
		 drawing.start(paperWidth, 'blast_fig');
		 drawing.drawSpacer(20);
		 drawing.drawScale(${info_results.sequence[0].length()});
		 var start=''
		 var stop=''
		 var score=''
		 for (var i = 0; i < data.length; i++) {        	 
			 var hit = data[i];
			 start = parseFloat(hit.anno_start)
			 stop = parseFloat(hit.anno_stop)
			 if (start > stop){
				 start = parseFloat(hit.anno_stop)
				 stop = parseFloat(hit.anno_start)
			 }
			 score = parseFloat(hit.score) 
			 var hitColour = drawing.getBLASTColour(score);
			 var blastRect = drawing.drawBar(start, stop, 7, hitColour, hit.anno_db + ": " + hit.anno_id, '');
			 //document.write('hit.start = ' + hit.start + ' start = ' +start)
			 blastRect.click(function(id){
					 return function(){ location.href='#'+id;}
					 }(hit.anno_id));
			 blastRect.hover(
				 function(event) {
					this.attr({stroke: 'black', 'stroke-width' : '2'});
					$('#' + hit.anno_id).css("background-color", "bisque");
	
				    },
				    function(event) {
					this.attr({stroke: 'black', 'stroke-width' : '0'});
					$('#' + hit.anno_id).css("background-color", "white");
				    }
			    );
		 }
		 drawing.drawSpacer(20);
		 drawing.drawTitle(range);
		 drawing.end();         
         }
         </script>
    
      </div>    
    	    <h1><g:link action="unigene_info" params="${[contig_id : info_results.contig_id[0], top: 1]}">Top annotations </g:link> / <g:link action="unigene_info" params="${[contig_id : info_results.contig_id[0], top: 10]}"> Top 10 annotations</g:link> from each database</h1>    
	      <table id="table_data" class="display">
	      <thead>
	      <tr>
		<th><b>Database</b></th>
		<th><b>Hit ID</b></th>
		<th><b>Description</b></th>
		<th><b>Start</b></th>
		<th><b>Stop</b></th>
		<th><b>Score</b></th>
	      </tr>
	      </thead>
	      <tbody>
	     <% def anno_check = [:]
	     def anno_show = [] 
	     def anno_list = [:]
	     %>
	     <g:each var="res" in="${anno_results}">
	     <g:unless test = "${anno_check[res.anno_db]}">
	          <% if (res.anno_db == 'SwissProt'){ %> <tr class='gradeA'>  <% } %>
                  <% if (res.anno_db == 'UniRef90'){ %> <tr class='gradeB'>  <% } %>
                  <% if (res.anno_db == 'EST others'){ %> <tr class='gradeC'>  <% } %> 
                  <%
                  anno_list.anno_start = res.anno_start
                  anno_list.anno_stop = res.anno_stop
                  anno_list.anno_id = res.anno_id
                  anno_list.anno_db = res.anno_db
                  anno_show.add(anno_list)  
                  println anno_show
                  anno_list = [:] 
                  %>
		<td><a name="${res.anno_id}">${res.anno_db}</td>
		<%
		res.anno_id = res.anno_id.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>") 
		res.descr = res.descr.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>") 
		res.anno_id = res.anno_id.replaceAll(/lcl\|(.*)/, "<a href=\"http://www.uniprot.org/uniref/\$1\" target=\'_blank\'>\$1</a>") 
		%>
		<td>${res.anno_id}</td>
		<td>${res.descr}</td>
		<td>${res.anno_start}</td>
		<td>${res.anno_stop}</td>
		<td>${res.score}</td>
	      </tr>  
	      </g:unless>
	      <%
	      if (params.top != '10'){
		//just get the first one for each annotation
		def check_id = res.anno_db
		anno_check[check_id] = "yes"
	      }
	      %>
	     </g:each>
	     </tbody>
	    </table>
	    
     
	</g:if>
	<g:else>
		<h1>There are no annotations for this contig</h1>
        </g:else>
  </g:if>
  <g:else>
    <h1>The gene Id has no information</h1>
  </g:else>
  </body>
</html>
