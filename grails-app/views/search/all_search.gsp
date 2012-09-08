<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} transcript search</title>
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
	setTimeout(""+plot_type+"('"+params+"')", 2000);
    }
    <% 
    def jsonData = transData.encodeAsJSON(); 
    def funjsonData = funData.encodeAsJSON(); 
    //println jsonData;
    //println ecjsonData;
    %>
    var loadcheck = "no";
    function loadPlotData(){  	    
    	    if (loadcheck == "no"){
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
		    loadcheck = "yes";
		    //draw the graph on load
		    setTimeout("makeArrays('len_cov')", 1000);
		    //add the click data here as adding it at the top causes multiple windows to open
		    $('#chart').bind('jqplotDataClick',
		    function (ev, seriesIndex, pointIndex, data) {
			//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
			window.open("/search/trans_info?contig_id=" + data[2]);
			}
		    );      
	    }
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
    	    joinArray = [];
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
    	    joinArray2 = [];
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
		 //legend: {
		 //	show: true,
		 //	location: 'ne',
		 //},  
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
  <br>
     <p>There are two methods to search all the data:</p><br>
     1. By key word (this will search all databases)<br>
     2. By genomic contig attribute (the length, coverage and GC of the contigs)<br>
    <g:form action="all_searched">
    <h1 STYLE="cursor: pointer" onclick="toggleDiv('showAnno');$('#showPub').hide();$('#showChart').hide();">1. Key word</h1>
    <div id = "showAnno">
    <p>Keyword search will find related terms, for example searching for 'tolerate' will also match 'tolerance'.</p>
    <p>Query text can contain the Boolean operators & (and), | (or) and ! (not), e.g. 'atpase & zinc', '!metal', 'atpase & zinc | !metal'    </p>
    <table>
    <tr><td>
    <g:textField name="searchId"  size="100"/>
    <input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
    </g:form>
     </td>
  </tr>
   </table>
   <br>
   </div>
   
   <h1 STYLE="cursor: pointer" onclick="toggleDiv('showChart');$('#showAnno').hide();$('#showPub').hide();loadPlotData();" value="Show plot">2. Contig attribute search:</h1>   
   <div id="showChart" style="display:none">
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
          });
        </script>
</body>
</html>
