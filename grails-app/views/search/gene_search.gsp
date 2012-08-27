<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} gene search</title>
    <parameter name="search" value="selected"></parameter>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasTextRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.highlighter.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.cursor.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.dateAxisRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.logAxisRenderer.js')}" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
    <script type="text/javascript"> 
    	$(window).unload(function() {});
    </script>
    <script type="text/javascript">

    function showSelected(val){
		document.getElementById
		('selectedResult').innerHTML = val;
    }
    $(function() {
		$("[name=toggler]").click(function(){
				$('.toHide').hide();
				$("#blk_"+$(this).val()).show('slow');
				$("#sel_"+$(this).val()).show('fast');
				showSelected($("#sel_"+$(this).val()).val())
		});
    });
    function zip(arrays) {
            	    return arrays[0].map(function(_,i){
            	    return arrays.map(function(array){return array[i]})
           });
    }
    function toggleDiv(divId) {
    	    $("#"+divId).slideToggle(2000);
    }
    function changed(plot_type,params) {
	$("#chart").html('Loading...<img src="${resource(dir: 'images', file: 'spinner.gif')}" />');
	setTimeout(""+plot_type+"('"+params+"')", 1000);
    }
 
    //set the global variable for the plots
    var dlen = [], dcov = [], dgc = [], dcon = [], dcum = [], dcou = [];
    var elen = [], ecov = [], egc = [], econ = [];
    var glen = [], gcov = [], ggc = [], gcon = [];
    var klen = [], kcov = [], kgc = [], kcon = [];
    var ilen = [], icov = [], igc = [], icon = [];
    var joinArray = [], joinArray2 = [];
    var counter=0;
    var cum = 0;
    var xaxis_label="", yaxis_label="", title_label="", xaxis_type="", yaxis_type="";
    var arraySet, funSet, funColour;
    function makeArrays(arrayInfo){
    	    $("#chart").text('');
	    //alert(arrayInfo)
	    arraySet = arrayInfo;	    	    
	    if (arrayInfo == 'len_gc'){
		    joinArray = zip([dgc,dlen,dcon,dlen,dgc,dcov]);
		    xaxis_label = "GC";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Length";
		    yaxis_type = $.jqplot.LogAxisRenderer;
		    title_label = "Length vs GC";
		    addArrays()
	    }else if (arrayInfo == 'cov_gc'){
		    joinArray = zip([dgc,dcov,dcon,dlen,dgc,dcov]);
		    xaxis_label = "GC";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Coverage";
		    yaxis_type = $.jqplot.LogAxisRenderer;
		    title_label = "Coverage vs GC";
		    addArrays()
	    }else if (arrayInfo == 'len_cov'){
		    joinArray = zip([dlen,dcov,dcon,dlen,dgc,dcov]);
		    xaxis_label = "Length";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Coverage";
		    yaxis_type = $.jqplot.LogAxisRenderer;
		    title_label = "Length vs Coverage";
		    addArrays()
	    }else if (arrayInfo == 'cum'){          	    	    
		    joinArray = zip([dcou,dcum,dcon,dlen,dgc,dcov]);
		    xaxis_label = "Contigs ranked by size";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Cumulative contig length (bp)";
		    yaxis_type = $.jqplot.LinearAxisRenderer;
		    title_label = "Cumulative contig length";
		    graphDraw()
	    }
	    
    }
    function addArrays(funInfo){
    	    $("#chart").text('');
    	    //alert("funInfo = "+funInfo)
    	    if (funInfo){
    	    	    funSet = funInfo;
    	    }
	    if (funSet == 'EC'){
	    	    funColour = "blue";
	    	    if (arraySet == 'len_gc'){
	    	    	    joinArray2 = zip([egc,elen,econ,elen,egc,ecov]);
	    	    }if (arraySet == 'cov_gc'){
	    	    	    joinArray2 = zip([egc,ecov,econ,elen,egc,ecov]);
	    	    }if (arraySet == 'len_cov'){
	    	    	    joinArray2 = zip([elen,ecov,econ,elen,egc,ecov]);
	    	    }	    	    
	    }else if (funSet == 'GO'){
	    	    funColour = "orange";
	    	    if (arraySet == 'len_gc'){
	    	    	    joinArray2 = zip([ggc,glen,gcon,glen,ggc,gcov]);
	    	    }if (arraySet == 'cov_gc'){
	    	    	    joinArray2 = zip([ggc,gcov,gcon,glen,ggc,gcov]);
	    	    }if (arraySet == 'len_cov'){
	    	    	    joinArray2 = zip([glen,gcov,gcon,glen,ggc,gcov]);
	    	    }	    	    
	    }else if (funSet == 'KEGG'){
	    	    funColour = "red";
	    	    if (arraySet == 'len_gc'){
	    	    	    joinArray2 = zip([kgc,klen,kcon,klen,kgc,kcov]);
	    	    }if (arraySet == 'cov_gc'){
	    	    	    joinArray2 = zip([kgc,kcov,kcon,klen,kgc,kcov]);
	    	    }if (arraySet == 'len_cov'){
	    	    	    joinArray2 = zip([klen,kcov,kcon,klen,kgc,kcov]);
	    	    }	    	    
	    }else if (funSet == 'InterPro Domains'){
	    	    funColour = "purple";
	    	    if (arraySet == 'len_gc'){
	    	    	    joinArray2 = zip([igc,ilen,icon,ilen,igc,icov]);
	    	    }if (arraySet == 'cov_gc'){
	    	    	    joinArray2 = zip([igc,icov,icon,ilen,igc,icov]);
	    	    }if (arraySet == 'len_cov'){
	    	    	    joinArray2 = zip([ilen,icov,icon,ilen,igc,icov]);
	    	    }			    	    
	    }else{
	    	    joinArray2 = "";
	    }
	    //alert(joinArray2)
	    graphDraw()
    }
    function graphDraw(){
    	    $('#chart').empty();
	    //alert("j = "+joinArray)
	    //alert("j2 = "+joinArray2)
	    plot1 = $.jqplot ('chart', [joinArray,joinArray2],{
		 title: title_label,
		 legend: {
		 	show: true,
		 	location: 'ne',
		 },  
		 series:[
			 {
			 showLine:false,
			 markerOptions: { size: 1, style:"circle", color:"green"},
			 label: 'No annotation',
			 color: 'green'
			 },
			 {
			 showLine:false,
			 markerOptions: { size: 1, style:'dimaond', color:funColour},
			 label: funSet,
			 color: funColour
			 }
		 ],
		 axesDefaults: {
			 labelRenderer: $.jqplot.CanvasAxisLabelRenderer
		 },
		 noDataIndicator: {
		    show: true
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
		 //seriesColors: pointcolours,
		 highlighter: {
			 tooltipAxes: 'yx',
			 yvalues: 5,
			 show: true,
			 sizeAdjust: 7.5,
			 //formatString: "%d"
			 //formatString: ContigData[0].contig_id +" length: " + ContigData[1].length
			 formatString: '<span style="display:none">%s</span>Contig ID: %s<br>Length: %s<br>GC: %.2f<br>Coverage: %.2f'
	
		 },
		 cursor:{
		 	 show: true,
		 	 zoom:true,
		 	 tooltipLocation:'nw'
		 }
	    });
	    $('.button-reset').click(function() { plot1.resetZoom() });
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
    <input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
    </g:form>
     </td>
  </tr>
   </table>
   <br>
   
   
   <div id="hideChart">
   	<table><tr><td><h1 STYLE="cursor: pointer" onclick="toggleDiv('showChart');toggleDiv('hideChart');" value="Show plot">Search by contig attribute (show):</h1></td></tr></table>
   </div>
   
   <div id="showChart" style="display:none">
	<table><tr><td><h1 STYLE="cursor: pointer" onclick="toggleDiv('showChart');toggleDiv('hideChart');" value="Show plot">Search by contig attribute (hide):</h1></td></tr></table>
	<div id="contig_attribute">
	<table><tr><td>
			<!--tr><td>X axis</td><td>Y axis</td></tr>
			<tr><td><input type="radio" name="plotx" value="dlen"/>Length</td><td><input type="radio" name="ploty" value="dgc"/>GC</td></tr>
			<tr><td><input type="radio" name="plotx" value="dgc"/>GC</td><td><input type="radio" name="ploty" value="dcov"/>Coverage</td></tr>
			<tr><td><button type="button" onclick="graphDraw($('input:radio[name=plotx]:checked').val(),$('input:radio[name=ploty]:checked').val())">Redraw</button></td></tr-->
			<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','cum')" value="Cumulative length"/>
			<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','len_gc')" value="Length vs GC"/>
			<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','cov_gc')" value="Coverage vs GC"/>
			<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','len_cov')" value="Length vs Coverage"/>
			</td></tr>
			<tr><td><h4>Colour by functional annotation: 
			<font color="orange" STYLE="cursor: pointer" onclick="changed('addArrays','GO')">GO </font> 
			<font color="blue" STYLE="cursor: pointer" onclick="changed('addArrays','EC')">EC </font> 
			<font color="red" STYLE="cursor: pointer" onclick="changed('addArrays','KEGG')">KEGG </font> 
			<font color="purple" STYLE="cursor: pointer" onclick="changed('addArrays','InterPro Domains')">InterProScan </font>
			<font color="green" STYLE="cursor: pointer" onclick="changed('addArrays','Reset')">(Reset) </font>
			</td></tr>
			<tr><td><p>Zoom in by dragging around an area. Reset by double clicking or clicking <font STYLE="cursor: pointer" color="green" class="button-reset">here</font></td></tr>
		   </table>
		   <br>   
	  <div id="chart" class="jqplot-target" style="height: 500px; width: 100%; position: center;">Loading...<img src="${resource(dir: 'images', file: 'spinner.gif')}"</div>
	  </div>
  </div>
  </div>

  <% 
  def jsonData = uniGeneData.encodeAsJSON(); 
  def funjsonData = funData.encodeAsJSON(); 
  //println jsonData;
  //println ecjsonData;
  %>
	<script>
          $(document).ready(function(){
            $('.toHide').hide();
            $("#blk_1").show('slow');
	    $("#sel_1").show('fast');
	    showSelected($("#sel_1").val())
	    
	    $("#contig_attribute").show('slow');
	    
            $("#process").bind("click", function () {
              $("#content").mask("Searching the database...");
            });
				
            $("#cancel").bind("click", function () {
		$("#content").unmask();
            });
            
            //load the jqplot data
            ContigData = ${jsonData};
            for (var i = 0; i < ContigData.length; i++) {   		 	 
            	    var hit = ContigData[i];
            	    counter++;
            	    cum += hit.length;
            	    dlen.push(hit.length);
            	    dcov.push(hit.coverage);
            	    dgc.push(hit.gc);
            	    dcon.push(hit.contig_id);
            	    dcum.push(cum);
            	    dcou.push(counter);
            }
            FunData = ${funjsonData};
            var ipr_match = /^IPR/;
            for (var i = 0; i < FunData.length; i++) {   		 	 
            	    var hit = FunData[i];
            	    if (hit.anno_db == 'EC'){
			    elen.push(hit.length);
			    ecov.push(hit.coverage);
			    egc.push(hit.gc);
			    econ.push(hit.contig_id);
		    }else if (hit.anno_db == 'GO'){
			    glen.push(hit.length);
			    gcov.push(hit.coverage);
			    ggc.push(hit.gc);
			    gcon.push(hit.contig_id);
		    }else if (hit.anno_db == 'KEGG'){
			    klen.push(hit.length);
			    kcov.push(hit.coverage);
			    kgc.push(hit.gc);
			    kcon.push(hit.contig_id);
		    }else if (hit.anno_id.match(/^IPR/)){
			    ilen.push(hit.length);
			    icov.push(hit.coverage);
			    igc.push(hit.gc);
			    icon.push(hit.contig_id);
		    }
            }
            //draw the graph on load
            setTimeout("makeArrays('cum')", 1000);
            //add the click data here as adding it at the top causes multiple windows to open
            $('#chart').bind('jqplotDataClick',
	    function (ev, seriesIndex, pointIndex, data) {
		//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
		window.open("/search/unigene_info?contig_id=" + data[2]);
	    	}
	    );      
                    
          });
        </script>
</body>
</html>
