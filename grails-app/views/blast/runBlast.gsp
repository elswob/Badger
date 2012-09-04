<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <parameter name="blast" value="selected"></parameter>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} BLAST</title>
    
  </head>
  <body>
  
  <script type="text/javascript">
  var contig_list=[];
  function get_contig_data(){
	    document.getElementById('fileId').value=contig_list;
	    //alert(table_scrape)
    }
    </script>
    
    <g:if test="${blast_result}">
      <!--p>${command}</p><br-->
      <h1>BLAST result for <em>${term}</em></h1>
      
      <div id = "blast_fig">
         <script type="text/javascript" src="${resource(dir: 'js', file: 'raphael-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'g.raphael-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'g.line-min.js')}"></script>
      	 <script type="text/javascript" src="${resource(dir: 'js', file: 'biodrawing.js')}"></script>
         <script type="text/javascript">
         data = ${jsonData};
         //check for no hits
         //alert(data)
         if (data.length >1){
			 var paperWidth = $('#blast_fig').width() - 10;
			 var drawing = new BioDrawing();
			 drawing.start(paperWidth, 'blast_fig');
			 drawing.drawSpacer(20);
			 drawing.drawScale(${queryInfo});
			 var start=''
			 var stop=''
			 var score=''
			 //go through data in order of score
			 for (var c = 0; c < data.length; c++) {  
				 var hit_check = data[c];
				 //find all entries with that id
				 for (var i = 0; i < data.length; i++) {
					 var hit = data[i];
					 if (hit.id == hit_check.id){
					 //add contig IDs to list
					 contig_list.push(hit.id)
					 start = parseFloat(hit.start)
					 stop = parseFloat(hit.stop)
					 if (start > stop){
						 start = parseFloat(hit.stop)
						 stop = parseFloat(hit.start)
					 }
					 score = parseFloat(hit.score) 
					 var hitColour = drawing.getBLASTColour(score);
					 var hit_desc = hit.id+": "+start+" - "+stop;
					 var blastRect = drawing.drawBar(start, stop, 8, hitColour, hit_desc, '');
					 blastRect.click(function(id){
							 return function(){ location.href='#'+id;}
							 }(hit.id));
					 blastRect.hover(
						 function(event) {
							this.attr({stroke: 'black', 'stroke-width' : '2'});
							$('#' + hit.id).css("background-color", "bisque");
			
							},
							function(event) {
							this.attr({stroke: 'black', 'stroke-width' : '0'});
							$('#' + hit.id).css("background-color", "white");
							}
					 )
					 //remove entry
					 data.splice(i,1);
					 //go back one element of the json array
					 i--;
				 }
			 }
			 var blastRect = drawing.drawId(start, stop, 12, hitColour, hit_desc, hit_check.id);
			 drawing.drawSpacer(10);
			 //go back one element of the json array
			 c--
			 }
			 drawing.drawSpacer(20);
			 drawing.end();
		}
         </script>
        
      </div>
      <!--p>${jsonData}</p-->
      <!--p>${queryInfo}</p-->
      <table><tr><td>
      <g:form name="resultsDownload" url="[controller:'FileDownload', action:'blast_download']">
			    <g:hiddenField name="fileName" value='blast_result.txt'/>
			    <g:hiddenField name="blastfileId" value="${blastId}"/>
			    <input align="right" type="submit" value="Download BLAST result" class="mybuttons"/>
	  </g:form>
	  </td>
      <% if (blast_file == 'Genome' || blast_file == 'Transcriptome'){ %>
      	<td>
      		<g:form name="blastDownload" url="[controller:'FileDownload', action:'blast_contig_download']">
			    <g:hiddenField name="fileId" value=""/>
			    <g:hiddenField name="dataSource" value="${blast_file}"/>
			    <!--g:hiddenField name="fileName" value="${term}"/-->
			     <g:hiddenField name="fileName" value='blast_result'/>
			    <g:hiddenField name="blastfileId" value="${blastId}"/>
			    <input align="right" type="submit" value="Download sequences" class="mybuttons" onclick="get_contig_data()"/>
		    </g:form>
		</td></tr>
	 <% } %>
	 </table>
	 
      <div class="blast_res">
          <g:each var="line" in="${blast_result}">
${line}<br>
          </g:each>
      </div>
    </g:if>
  <g:else test="${term}">
    <h1>Your BLAST of <em>${term}</em> produced no results</h1>
    <p>Perhaps you used the wrong BLAST program, please go <g:link controller="blast">back</g:link> and try again.</p>  
  </g:else>
  </body>
  


</html>
