<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} UniGene info</title>
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
    <script type="text/javascript" charset="utf-8">
    var blasttableShow="";
    var iprtableShow="";
    var a8rtableShow="";
    $(document).ready(function() {
    	$('#blast_table_data').dataTable({
    	    "sPaginationType": "full_numbers",
    	    "iDisplayLength": 10,
    	    "oLanguage": {
    	     	     "sSearch": "Filter records:"
    	     },
    	    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	    "aaSorting": [[ 5, "desc" ]],
    	    "sDom": 'T<"clear">lfrtip',
            "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
            }
         });     
        //capture the rows in the tables to use for the image
        var blastoTable = $('#blast_table_data').dataTable();
        blasttableShow = blastoTable._('td');
        
        $('#a8r_table_data').dataTable({
    	    "sPaginationType": "full_numbers",
    	    "iDisplayLength": 10,
    	    "oLanguage": {
    	     	     "sSearch": "Filter records:"
    	     },
    	    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	    "aaSorting": [[ 5, "desc" ]],
    	    "sDom": 'T<"clear">lfrtip',
            "oTableTools": {
        	"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
            }
         });     
        //capture the rows in the tables to use for the image
        var a8roTable = $('#a8r_table_data').dataTable();
        a8rtableShow = a8roTable._('td');
        
        $('#ipr_table_data').dataTable({
    	    "sPaginationType": "full_numbers",
    	    "iDisplayLength": 10,
    	    "oLanguage": {
    	     	     "sSearch": "Filter records:"
    	     },
    	    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    	    "aaSorting": [[ 5, "asc" ]],
    	    "aoColumns": [
                 null,
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
        //capture the rows in the tables to use for the image
        var iproTable = $('#ipr_table_data').dataTable();
        iprtableShow = iproTable._('td');

        //draw the figure
        drawHits();
    });
    </script>
  </head>
  <body>
  <g:if test="${info_results}">
    <h1>Stats for UniGene <b>${info_results.contig_id[0]}</b>:</h1>
    <table>
      <tr>
        <td><b>Length:</b> ${printf("%,d\n",info_results.sequence[0].length())}</td>
        <td><b>Coverage: </b> ${info_results.coverage[0]}</td>
        <td><b>GC: </b> ${sprintf("%.2f",info_results.gc[0])}</td>
        <td><b>Sequence: </b> <g:link controller="FileDownload" action="contig_download" params="${[fileId : nuc_fasta, fileName: info_results.contig_id[0]+'.fa']}">Download</g:link></td>
      </tr>
    </table>

    
    <g:if test="${blast_results}">
        <hr size = 5 color="green" width="100%" style="margin-top:10px">  
    <g:if test="${params.top != "10"}">
    	<h1>Top annotations  / <g:link action="unigene_info" params="${[contig_id : info_results.contig_id[0], top: 10]}"> Top 10 annotations</g:link> from each database</h1>  
    </g:if>
    <g:else>    	    
    	<h1><g:link action="unigene_info" params="${[contig_id : info_results.contig_id[0], top: 1]}">Top annotations </g:link> /  Top 10 annotations  from each database</h1>  
    </g:else>
      <% 
      def iprjsonData = ipr_results.encodeAsJSON();
      def blastjsonData = blast_results.encodeAsJSON();
      def a8rjsonData = a8r_results.encodeAsJSON();
      //println a8rjsonData
      %>    
      <div id = "blast_fig">
         <script type="text/javascript" src="${resource(dir: 'js', file: 'raphael-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'g.raphael-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'g.line-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'biodrawing.js')}"></script>
         <script type="text/javascript">
         var drawing = new BioDrawing();
         function drawHits(){
         	 //alert(tableShow);
		 blast_data = ${blastjsonData};	
		 ipr_data = ${iprjsonData};
		 a8r_data = ${a8rjsonData};
		 if (blast_data.length > 0 || ipr_data.length > 0 || a8r_data.length > 0){
			 var paperWidth = $('#blast_fig').width() - 40;
			 //var drawing = new BioDrawing();
			 drawing.start(paperWidth, 'blast_fig');
			 drawing.drawSpacer(20);
			 drawing.drawScale(${info_results.sequence[0].length()});			 
			 drawing.drawSpacer(10);
			 if (blast_data.length > 0){
			 	 drawing.drawSpacer(20);
				 drawing.drawColouredTitle('BLAST','green')
				 drawBars(blast_data,blasttableShow)
			 }
			 if (a8r_data.length > 0){
				 drawing.drawSpacer(20);
				 drawing.drawColouredTitle('annot8r','green')
				 drawBars(a8r_data,a8rtableShow)
			 }
			 if (ipr_data.length > 0){
				 drawing.drawSpacer(20);
				 drawing.drawColouredTitle('InterPro','green')
				 drawBars(ipr_data,iprtableShow)
			 }
			 drawing.end();         
		 }
         }
         function drawBars(funcAnno,name){
         	 var start=''
		 var stop=''
		 var score=''
         	 for (var i = 0; i < funcAnno.length; i++) {   		 	 
			 var hit = funcAnno[i];
			 //get rid of all strange characters
			 var stringy = String(name);
			 var idPattern = hit.anno_id.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
			 if (stringy.match(idPattern)){
				 start = parseFloat(hit.anno_start)
				 stop = parseFloat(hit.anno_stop)
				 if (start > stop){
					 start = parseFloat(hit.anno_stop)
					 stop = parseFloat(hit.anno_start)
				 }
				 score = parseFloat(hit.score) 	
				 var hitColour = drawing.getBLASTColour(score);
				 var blastRect = drawing.drawBar(start, stop, 7, hitColour, hit.anno_db + ": " + hit.anno_id + "\n" + hit.descr, '');
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
				)
			 drawing.drawSpacer(10);	 
			 }	    	    
		 }
	 }
         </script>
    
      </div>
      <hr size = 5 color="green" width="100%" style="margin-top:10px">
      <h1>FASTA file</h1>
      <div style="overflow:auto; height:200px;">
	      <table style="table-layout: fixed; width:100%">
	      <tr><td style="word-wrap: break-word">
	      >${info_results.contig_id[0]}<br>${info_results.sequence[0]}
	      </td></tr>
	      </table>
      </div>
      <br>
      <hr size = 5 color="green" width="100%" style="margin-top:10px">
      	   <h1>BLAST results</h1>
	   <table id="blast_table_data" class="display">
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
	     <% def blast_check = [:]%>
	     <g:each var="res" in="${blast_results}">
	     <g:unless test = "${blast_check[res.anno_db]}">
	          <% if (res.anno_db == 'SwissProt'){ %> <tr class='gradeA' id="${res.anno_id}">  <% } %>
                  <% if (res.anno_db == 'UniRef90'){ %> <tr class='gradeB' id="${res.anno_id}">  <% } %>
                  <% if (res.anno_db == 'EST others'){ %> <tr class='gradeC' id="${res.anno_id}">  <% } %> 
		<td><a name="${res.anno_id}">${res.anno_db}</td>
		<%
		res.anno_id = res.anno_id.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>") 
		res.descr = res.descr.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>") 
		res.anno_id = res.anno_id.replaceAll(/lcl\|(.*)/, "<a href=\"http://www.uniprot.org/uniref/\$1\" target=\'_blank\'>\$1</a>") 
		res.anno_id = res.anno_id.replaceAll(/(^\d+\.\d+\.\d+\.\d+)/, "<a href=\"http://enzyme.expasy.org/EC/\$1\" target=\'_blank\'>\$1</a>")
                res.anno_id = res.anno_id.replaceAll(/(GO:\d+)/, "<a href=\"http://www.ebi.ac.uk/QuickGO/GTerm?id=\$1\" target=\'_blank\'>\$1</a>")
                res.anno_id = res.anno_id.replaceAll(/(^K\d+)/, "<a href=\"http://www.genome.jp/dbget-bin/www_bget?ko:\$1\" target=\'_blank\'>\$1</a>")
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
		blast_check[check_id] = "yes"
	      }
	      %>
	     </g:each>
	      </tbody>
	    </table>
	   
	   <!--g:if test="${a8r_results}"-->
	   <br>
	   <hr size = 5 color="green" width="100%" style="margin-top:10px">
	   <h1>annot8r results</h1>
	   <table id="a8r_table_data" class="display">
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
	     <% def a8r_check = [:]%>
	     <g:each var="res" in="${a8r_results}">
	     <g:unless test = "${a8r_check[res.anno_db]}">
                <% if (res.anno_db == 'GO' || res.anno_db == 'EC' || res.anno_db == 'KEGG'){ %> <tr class='gradeD' id="${res.anno_id}">  <% } %>
		<td><a name="${res.anno_id}">${res.anno_db}</td>
		<%
		res.anno_id = res.anno_id.replaceAll(/(^\d+\.\d+\.\d+\.\d+)/, "<a href=\"http://enzyme.expasy.org/EC/\$1\" target=\'_blank\'>\$1</a>")
                res.anno_id = res.anno_id.replaceAll(/(GO:\d+)/, "<a href=\"http://www.ebi.ac.uk/QuickGO/GTerm?id=\$1\" target=\'_blank\'>\$1</a>")
                res.anno_id = res.anno_id.replaceAll(/(^K\d+)/, "<a href=\"http://www.genome.jp/dbget-bin/www_bget?ko:\$1\" target=\'_blank\'>\$1</a>")
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
		a8r_check[check_id] = "yes"
	      }
	      %>
	     </g:each>
	      </tbody>
	    </table>
	    <!--/g:if-->
	   <br>
	   <hr size = 5 color="green" width="100%" style="margin-top:10px">
	   <h1>InterProScan results</h1>
	   <table id="ipr_table_data" class="display">
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
	     <% def ipr_check = [:]%>
	     <g:each var="res" in="${ipr_results}">
	     <g:unless test = "${ipr_check[res.anno_db]}">
	     	<tr id="${res.anno_id}">
		<td><a name="${res.anno_id}">${res.anno_db}</td>
		<%
                res.anno_id = res.anno_id.replaceAll(/(^IPR\d+)/, "<a href=\"http://www.ebi.ac.uk/interpro/IEntry?ac=\$1\" target=\'_blank\'>\$1</a>")
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
		ipr_check[check_id] = "yes"
	      }
	      %>
	     </g:each>
	     </tbody>
	    </table>
	    
     
	</g:if>
	<g:else>
		<h1>There are no annotations for this contig</h1>
		<h1>FASTA file</h1>
		<div style="overflow:auto; height:200px;">
			<table style="table-layout: fixed; width:100%">
			<tr><td style="word-wrap: break-word">
			>${info_results.contig_id[0]}<br>${info_results.sequence[0]}
			</td></tr>
			</table>
		</div>
		
        </g:else>
  </g:if>
  <g:else>
    <h1>The gene Id has no information</h1>
  </g:else>
  </body>
</html>
