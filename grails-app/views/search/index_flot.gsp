<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search</title>
    <parameter name="search" value="selected"></parameter>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
    <script type="text/javascript" src="http://code.jquery.com/jquery-latest.pack.js"></script>
    <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'flot/jquery.flot.js')}" type="text/javascript"></script>
    <script type="text/javascript">
	$(function() {
		$("[name=toggler]").click(function(){
				$('.toHide').hide();
				$("#blk_"+$(this).val()).show('slow');
				$("#sel_"+$(this).val()).show('fast');
				showSelected($("#sel_"+$(this).val()).val())
		});
	 });
    </script> 
    <script type="text/javascript">
    function showSelected(val){
		document.getElementById
		('selectedResult').innerHTML = val;
	}
    </script>
	
</head>
<body>
  <div id="content">
    <g:form action="search_results">  
    <table>
    <tr>
      <td>  
    <h1>Choose a data set:</h1>
    <select name = dataSet>
      <option value="UniGenes">EST UniGenes</option>
      <%--
      <option value="Genes">Gene annotations</option>    
      <option value="ncRNA">ncRNA</option>
      <option value="scaffoldID">Scaffold IDs</option>
      --%>
    </select>
      </td>
     
      <td>  
    <h1>Choose some annotations:</h1>   
      <label><input name="toggler" type="radio" id="blast" checked="checked" value="1"> BLAST homology</label><br>
      	<div class="toHide" id="blk_1" style="height:150;width:200px;overflow:auto;border:3px solid green;display:none">
		  <label><input type="checkbox" checked="yes" name="blastAnno" value="SwissProt" /> SwissProt <a href="http://web.expasy.org/groups/swissprot/" style="text-decoration:none" target="_blank">?</a></label><br>
		  <label><input type="checkbox" checked="yes" name="blastAnno" value="UniRef90" /> UniRef90 <a href="http://www.uniprot.org/help/uniref" style="text-decoration:none" target="_blank">?</a></label><br>
		  <label><input type="checkbox" checked="yes" name="blastAnno" value="EST others" /> EST Others <a href="http://www.ncbi.nlm.nih.gov.ezproxy.webfeat.lib.ed.ac.uk/dbEST/" style="text-decoration:none" target="_blank">?</a></label><br>
	</div> 
      
      <label><input name="toggler" type="radio" id="a8r" value="2"> Annot8r <a href="http://www.nematodes.org/bioinformatics/annot8r/" style="text-decoration:none" target="_blank">?</a></label><br>
      	<div class="toHide" id="blk_2" style="height:150;width:200px;overflow:auto;border:3px solid green;display:none">
		  <label><input type="checkbox" checked="yes" name="a8rAnno" value="GO" /> Gene Ontology <a href="http://www.geneontology.org/" style="text-decoration:none" target="_blank">?</a></label><br>
		  <label><input type="checkbox" checked="yes" name="a8rAnno" value="KEGG" /> KEGG <a href="http://www.genome.jp/kegg/" style="text-decoration:none" target="_blank">?</a></label><br>
		  <label><input type="checkbox" checked="yes" name="a8rAnno" value="EC" /> Enzyme Commission <a href="http://enzyme.expasy.org/" style="text-decoration:none" target="_blank">?</a></label><br>
	</div> 
		
      <label><input name="toggler" type="radio" id="ipr" value="3"> InterProScan <a href="http://www.ebi.ac.uk/interpro/index.html" style="text-decoration:none" target="_blank">?</a></label><br>
      	<div class="toHide" id="blk_3" style="height:150;width:200px;overflow:auto;border:3px solid green;display:none">
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="HMMPanther" /> PANTHER <a href="http://www.pantherdb.org/" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="BlastProDom" /> ProDom <a href="http://prodom.prabi.fr/prodom/current/html/home.php" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="Gene3D" /> Gene3D <a href="http://gene3d.biochem.ucl.ac.uk/Gene3D/" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="HMMSmart" /> SMART <a href="http://smart.embl-heidelberg.de/" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="HMMPfam" /> Pfam <a href="http://pfam.sanger.ac.uk/" style="text-decoration:none" target="_blank">?</a></label><br>
      		<label><input type="checkbox" checked="yes" name="iprAnno" value="HMMTigr" /> TIGRFAMs <a href="http://www.jcvi.org/cgi-bin/tigrfams/index.cgi" style="text-decoration:none" target="_blank">?</a></label><br>
	</div> 
      </td> 
      
    <td>  
    <h1>Choose what to search:</h1>
    <select class="toHide" name = "tableSelect_1" id ="sel_1" onChange='showSelected(this.value)'>
      <option value="e.g. ATPase">Description</option>
      <option value="e.g. 215283796 or P31409">Accession</option>    
      <!--option value="e.g. contig_1">ID</option-->
    </select>
    <select class="toHide" name = "tableSelect_2" id ="sel_2" onChange='showSelected(this.value)'>
      <option value="e.g. Calcium-transportingATPase">Description</option>
      <option value="e.g. GO:0008094 or 3.6.3.8 or K02147">Accession</option>    
      <!--option value="e.g. contig_1">ID</option-->
    </select>
    <select class="toHide" name = "tableSelect_3" id ="sel_3" onChange='showSelected(this.value)'>
      <option value="e.g. Vacuolar (H+)-ATPase G subunit">Description</option>
      <option value="e.g. IPR023298 or PF01813">Accession</option>    
      <!--option value="e.g. contig_1">ID</option-->
    </select>
      </td>
      
      <td>
    <h1>Enter a search term:</h1>
    <div id='selectedResult'></div>
    <g:textField name="searchId"  size="30"/>
    <input class="buttons" type="button" value="Search" id="process" onclick="submit()" >
    </g:form>
     </td>
  </tr>
     </table>
  </div>
  <br>

  <h1>Search by something else...</h1>
  <br>
  <div id="placeholder" style="width: 1000px; height: 300px; padding: 10px; position: center;"></div>
  <% 
  def jsonData = uniGeneData.encodeAsJSON(); 
  //println jsonData;
  %>


	    <script type="text/javascript">
		$(function () {
			data = ${jsonData};
			var d1 = [];
			var counter=0;
			var cum = 0;
			for (var i = 0; i < data.length; i++) {   		 	 
				var hit = data[i];
				counter ++;
				cum += hit.length
				d1.push([counter, cum])
			}
			var options = {
				//legend: {
				//	show: true,
				//	margin: 5,
				//	backgroundOpacity: 0.5
				//},
				points: {
					show: true,
					radius: 1
				},
				grid: { 
					hoverable: true, 
					clickable: true 
				}
			};
			function showTooltip(x, y, contents) {
				$('<div id="tooltip">' + contents + '</div>').css( {
				    position: 'absolute',
				    display: 'none',
				    top: y + 5,
				    left: x + 5,
				    border: '1px solid #fdd',
				    padding: '2px',
				    'background-color': '#fee',
				    opacity: 0.80
				}).appendTo("body").fadeIn(200);
			}
			
			//$.plot($("#placeholder"),[{data: d1, label: "UniGenes"}],options);
			$.plot($("#placeholder"),[d1],options);
			
			$("#placeholder").bind("plothover", function (event, pos, item) {
				$("#x").text(pos.x.toFixed(2));
				$("#y").text(pos.y.toFixed(2));			
				if (item) {
					if (previousPoint != item.dataIndex) {
					    previousPoint = item.dataIndex;
					    
					    $("#tooltip").remove();
					    var x = item.datapoint[0].toFixed(2),
						y = item.datapoint[1].toFixed(2);
					    
					    //showTooltip(item.pageX, item.pageY, item.series.label + " of " + x + " = " + y);
					    showTooltip(item.pageX, item.pageY, data[item.dataIndex].contig_id + ": length "+data[item.dataIndex].length);
					}
				}
				else {
					$("#tooltip").remove();
					previousPoint = null;            
				}
			});

			$("#placeholder").bind("plotclick", function (event, pos, item) {
				if (item) {
				    window.open("/search/unigene_info?contig_id=" + data[item.dataIndex].contig_id);
				}
			});

		});
	</script>

	<script>
          $(document).ready(function(){
            $('.toHide').hide();
            $("#blk_1").show('slow');
	    $("#sel_1").show('fast');
	    showSelected($("#sel_1").val())
            $("#process").bind("click", function () {
              $("#content").mask("Searching the database...");
            });
				
            $("#cancel").bind("click", function () {
		$("#content").unmask();
            });
             //$('#example_text').watermark($('#tableSelect-2').val())  
          });
        </script>
</body>
</html>
