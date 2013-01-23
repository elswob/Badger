<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} transcript info</title>
    <parameter name="search" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.0/media/js/jquery.js')}" type="text/javascript"></script> 
    <script src="${resource(dir: 'js', file: 'DataTables-1.9.0/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
    <style type="text/css">
            @import "${resource(dir: 'js', file: 'DataTables-1.9.0/media/css/demo_table.css')}";
            @import "${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}";
    </style>
    
            	  <% 
		  def iprjsonData = ipr_results.encodeAsJSON();
		  def blastjsonData = blast_results.encodeAsJSON();
		  def funjsonData = fun_results.encodeAsJSON();
		  //println blastjsonData
		  %>  
    
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
       
     <script type="text/javascript">  
 		$(function() {
    		// get initial top offset of navigation 
    		var floating_navigation_offset_top = $('#nav_float').offset().top;   
    		// define the floating navigation function
    		var floating_navigation = function(){
            	// current vertical position from the top
    	    	var scroll_top = $(window).scrollTop(); 
	    	    // if scrolled more than the navigation, change its 
                // position to fixed to float to top, otherwise change 
                // it back to relative
        		if (scroll_top > floating_navigation_offset_top) { 
            		$('#nav_float').css({ 'position': 'fixed', 'top':0});
        		} else {
            		$('#nav_float').css({ 'position': 'relative' }); 
        		}   
    		};
     
    		// run function on load
    		floating_navigation();
     
    		// run function every time you scroll
    		$(window).scroll(function() {
         		floating_navigation();
    		});
	});
 	</script>
    <script type="text/javascript" charset="utf-8">
    var blasttableShow="";
    var iprtableShow="";
    var funtableShow="";
    blast_data = ${blastjsonData};	
	ipr_data = ${iprjsonData};
	fun_data = ${funjsonData};
    
    $(document).ready(function() {
		$(".scroll").click(function(event){		
			event.preventDefault();
			$('html,body').animate({scrollTop:$('[name="'+this.hash.substring(1)+'"]').offset().top}, 500);
		});
    	if (blast_data.length > 0){
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
		}
        
		if (fun_data.length > 0){
			$('#fun_table_data').dataTable({
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
			var funoTable = $('#fun_table_data').dataTable();
			funtableShow = funoTable._('td');
        }
		
        if (ipr_data.length > 0){
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
		}

     //draw the figure
     drawHits(); 
        
    });
    </script>
  </head>
  <body>
  <g:if test="${info_results}">
    <a name="info_anchor"><h1>Information for transcript <b>${info_results.contig_id[0]}</b>:</h1></a>
    <table>
      <tr>
        <td><b>Length:</b> ${sprintf("%,d\n",info_results.sequence[0].length())}</td>
        <g:if test = "${grailsApplication.config.coverage.Transcriptome == 'y'}">
        	<td><b>Coverage: </b> ${info_results.coverage[0]}</td>
        </g:if>
        <td><b>GC: </b> ${sprintf("%.2f",info_results.gc[0])}</td>
        <td><b>Sequence: </b> <g:link controller="FileDownload" action="contig_download" params="${[fileId : nuc_fasta, fileName: info_results.contig_id[0]+'.fa']}">Download</g:link></td>
      </tr>
    </table>
	
	<div id="nav_float">
		<div class="footer" role="contentinfo">
			<div class="nav_float">
				<ul>
				   <li><a href="#info_anchor" class="scroll">Info</a></li>
				   <li><a href="#anno_anchor" class="scroll">Annotations</a></li>
				   <li><a href="#files_anchor" class="scroll">Sequence data</a></li>
				   <g:if test="${blast_results}">
					   <li><a href="#blast_anchor" class="scroll">BLAST</a></li>
				   </g:if>
				   <g:if test="${fun_results}">
					   <li><a href="#fun_anchor" class="scroll">Functional</a></li>
				   </g:if>
				   <g:if test="${ipr_results}">
					   <li><a href="#ipr_anchor" class="scroll">InterPro</a></li>
				   </g:if>
				</ul>
			</div>
		</div>
	</div>
    
    <g:if test="${blast_results}" || test="${ipr_results}" || test="${fun_results}">
        <a name="anno_anchor"><hr size = 5 color="green" width="100%" style="margin-top:10px"></a> 
    
		<g:if test="${params.top != "10"}">
			<h1>Top annotations  / <g:link action="trans_info" params="${[contig_id : info_results.contig_id[0], top: 10]}"> Top 10 annotations</g:link> from each database</h1>  
		</g:if>
		<g:else>    	    
			<h1><g:link action="trans_info" params="${[contig_id : info_results.contig_id[0], top: 1]}">Top annotations </g:link> /  Top 10 annotations  from each database</h1>  
		</g:else>  
		  <div id = "blast_fig">
			 <script type="text/javascript" src="${resource(dir: 'js', file: 'raphael-min.js')}"></script>
			 <script type="text/javascript" src="${resource(dir: 'js', file: 'g.raphael-min.js')}"></script>
			 <script type="text/javascript" src="${resource(dir: 'js', file: 'g.line-min.js')}"></script>
			 <script type="text/javascript" src="${resource(dir: 'js', file: 'biodrawing.js')}"></script>
			 <script type="text/javascript">
			 var drawing = new BioDrawing();
			 //alert("in blast_fig")
			 function drawHits(){
				 //alert("in drawHits");
				 if (blast_data.length > 0 || ipr_data.length > 0 || fun_data.length > 0){
				 	 //alert("drawing figure")
					 var paperWidth = $('#blast_fig').width();
					 drawing.start(paperWidth, 'blast_fig');
					 drawing.drawSpacer(40);
					 //add scale bars
					 drawing.drawScoreScale(${info_results.sequence[0].length()});	
					 drawing.drawSpacer(20);
					 drawing.drawScale(${info_results.sequence[0].length()});			 
					 drawing.drawSpacer(10);
					 if (blast_data.length > 0){
						 drawing.drawSpacer(10);
						 drawing.drawColouredTitle('BLAST','black')
						 drawBars(blast_data,blasttableShow,'blast')
						 drawing.drawSpacer(10);
						 drawing.drawLine(${info_results.sequence[0].length()});
					 }
					 if (fun_data.length > 0){
						 drawing.drawSpacer(10);
						 drawing.drawColouredTitle('Functional','black')
						 drawBars(fun_data,funtableShow,'fun')
						 drawing.drawSpacer(10);
						 drawing.drawLine(${info_results.sequence[0].length()});
					 }
					 if (ipr_data.length > 0){
						 drawing.drawSpacer(10);
						 drawing.drawColouredTitle('InterPro','black')
						 drawBars(ipr_data,iprtableShow,'ipr')
					 }
					 drawing.drawSpacer(10);
					 drawing.end();         
				 }
			 }
			 function drawBars(funcAnno,name,type){
			  	 //alert("in drawBars")
				 var start=''
				 var stop=''
				 var score=''
				 var matched = new Array();
				 for (var i = 0; i < funcAnno.length; i++) {   		 	 
					 var hit = funcAnno[i];
					 //get rid of all strange characters
					 var stringy = String(name);
					 var idPattern = hit.anno_id.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
					 //ignore duplicate IDs
					 if (stringy.match(idPattern) && matched.indexOf(idPattern) < 0){
						 start = parseFloat(hit.anno_start)
						 stop = parseFloat(hit.anno_stop)
						 if (start > stop){
							 start = parseFloat(hit.anno_stop)
							 stop = parseFloat(hit.anno_start)
						 }
						 score = parseFloat(hit.score) 	
						 var hitColour = drawing.getBLASTColour(score,type);
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
					 matched.push(idPattern);	
					 }	    	    
				 }
		 	}
			 </script>
		
		  </div>
      </g:if>
      
      <a name="files_anchor"><hr size = 5 color="green" width="100%" style="margin-top:10px"></a>
      <h1>FASTA file</h1>
      <div style="overflow:auto; max-height:200px;">
	      <table style="table-layout: fixed; width:100%">
	      <tr><td style="word-wrap: break-word">
	      >${info_results.contig_id[0]}<br>${info_results.sequence[0]}
	      </td></tr>
	      </table>
      </div>      
      <g:if test="${blast_results}">
		  <a name="blast_anchor"><hr size = 5 color="green" width="100%" style="margin-top:10px"></a>
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
			 <tr id="${res.anno_id}">
			<td><a name="${res.anno_id}">${res.anno_db}</td>
			<%
			//set links for blast
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
	   </g:if>
	   
	   <g:if test="${fun_results}">
	   <br>
	   <a name="fun_anchor"><hr size = 5 color="green" width="100%" style="margin-top:10px"></a>
	   <h1>Functional annotation results</h1>
	   <table id="fun_table_data" class="display">
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
	     <% def fun_check = [:]%>
	     <g:each var="res" in="${fun_results}">
	     <g:unless test = "${fun_check[res.anno_db]}">
	     <tr id="${res.anno_id}">
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
		fun_check[check_id] = "yes"
	      }
	      %>
	     </g:each>
	      </tbody>
	    </table>
	    </g:if>
	   
	    
	   <br>
	   
	   <g:if test="${ipr_results}">
		   <a name="ipr_anchor"><hr size = 5 color="green" width="100%" style="margin-top:10px"></a>
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
			res.anno_id = res.anno_id.replaceAll(/(^GO:\d+)/, "<a href=\"http://www.ebi.ac.uk/QuickGO/GTerm?id=\$1\" target=\'_blank\'>\$1</a>")
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
     
	<g:if test="${blast_results}" || test="${ipr_results}" || test="${fun_results}">
	</g:if>
	<g:else>
	  	<hr size = 5 color="green" width="100%" style="margin-top:10px">
		<h1>There are no annotations for this contig</h1>
	</g:else>
  </g:if>
  <g:else>
    <h1>The transcript ID has no information</h1>
  </g:else>
  </body>
</html>
