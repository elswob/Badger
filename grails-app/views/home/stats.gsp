<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} statistics</title>
  <parameter name="stats" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.categoryAxisRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.pointLabels.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasTextRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.highlighter.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.cursor.min.js')}" type="text/javascript"></script>
  	<script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.logAxisRenderer.js')}" type="text/javascript"></script>
  	<script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.trendline.min.js')}" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  
   <% 
  def jsonCountData = exonCountData.encodeAsJSON(); 
  def jsonDistData = exonDistData.encodeAsJSON(); 
  def jsonGeneData = geneDistData.encodeAsJSON(); 
  //println jsonCountData;
  %>	

  <script>
  
  	function zip(arrays) {
            return arrays[0].map(function(_,i){
            return arrays.map(function(array){return array[i]})
         });
    }
    
    var exCount = [], exNum = [], exPer = [];
    
    $(document).ready(function(){
    	var exCountNumArray = [];
    	NumData = ${jsonCountData};
        for (var i = 0; i < NumData.length; i++) {   		 	 
			var hit = NumData[i];
			//alert(hit.count)
			exPer.push((hit.count/"${geneCount}")*100);
			exNum.push(hit.num);
			exCount.push(hit.count)
        }
    	exCountNumArray = zip([exNum,exPer,exCount]);
    	//alert(exCountNumArray)
  		//var plot1 = $.jqplot ('chart1', [exCountNumArray]);
  		 		
  		var plot1 = $.jqplot ('chart1', [exCountNumArray],{
  		 animate: true,
		 title: 'Distribution of exons per gene', 
		 series:[
			 {
			 //showLine:false,
			 markerOptions: { size: 5, style:"circle", color:"green"},
			 color: 'green'
			 },
		 ],
		 axesDefaults: {
			 labelRenderer: $.jqplot.CanvasAxisLabelRenderer
		 },
		 axes: {
			xaxis: {
				label: 'Number of exons per gene',
				pad: 0
			},
			yaxis: {
				label: 'Percentage of genes',
				pad: 0,
			}
		 },
		 //seriesColors: pointcolours,
		 highlighter: {
			 tooltipAxes: 'yx',
			 yvalues: 2,
			 show: true,
			 sizeAdjust: 7.5,
			 //formatString: "%d"
			 //formatString: ContigData[0].contig_id +" length: " + ContigData[1].length
			 formatString: 'Percentage: %.2f<br>Frequency: %d <br> #exons: %d'
	
		 },
		 cursor:{
		 	 show: true,
		 	 zoom:true,
		 	 showTooltip: false,
		 	 //tooltipLocation:'nw'
		 }

	    });
  		
  		$('#chart1').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
            	//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
            	window.open("/search/genome_info?contig_id=" + data[2]);
	    	}
	    );  
	    
	    
	var exDCount = [], exDNum = [], exDPer = [];
    
    	var exDistNumArray = [];
    	DistData = ${jsonDistData};
        for (var i = 0; i < DistData.length; i++) {   		 	 
			var hit = DistData[i];
			//alert(hit.count)
			exDPer.push((hit.count/"${exonCount}")*100);
			exDNum.push(hit.num);
			exDCount.push(hit.count)
        }
    	exDistNumArray = zip([exDNum,exDPer,exDCount]);
    	//alert(exCountNumArray)
  		//var plot1 = $.jqplot ('chart1', [exCountNumArray]);
  		 		
  		var plot2 = $.jqplot ('chart2', [exDistNumArray],{
  		 animate: true,
		 title: 'Distribution of exon lengths (<500bp)', 
		 series:[
			 {
			 showLine:false,
			 markerOptions: { size: 2, style:"circle", color:"green"},
			 color: 'green'
			 },
		 ],
		 axesDefaults: {
			 labelRenderer: $.jqplot.CanvasAxisLabelRenderer
		 },
		 axes: {
			xaxis: {
				label: 'Exon length (bp)',
				//renderer: $.jqplot.LinearAxisRenderer,
				pad: 0
			},
			yaxis: {
				label: 'Percentage of exons',
				pad: 0,
			}
		 },
		 //seriesColors: pointcolours,
		 highlighter: {
			 tooltipAxes: 'yx',
			 yvalues: 2,
			 show: true,
			 sizeAdjust: 7.5,
			 //formatString: "%d"
			 //formatString: ContigData[0].contig_id +" length: " + ContigData[1].length
			 formatString: 'Percentage: %.2f<br>Frequency: %d <br> Exon length: %d'
	
		 },
		 cursor:{
		 	 show: true,
		 	 zoom:true,
		 	 showTooltip: false,
		 	 //tooltipLocation:'nw'
		 }

	    });
  		
  		$('#chart2').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
            	//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
            	window.open("/search/genome_info?contig_id=" + data[2]);
	    	}
	    );  
	    
	    var GCount = [], GNum = [], GPer = [];
    
    	var GDistNumArray = [];
    	GDistData = ${jsonGeneData};
        for (var i = 0; i < GDistData.length; i++) {   		 	 
			var hit = GDistData[i];
			//alert(hit.count)
			GPer.push((hit.count/"${geneCount}")*100);
			GNum.push(hit.num);
			GCount.push(hit.count)
        }
    	GDistNumArray = zip([GNum,GPer,GCount]);

  		 		
  		var plot3 = $.jqplot ('chart3', [GDistNumArray],{
  		 animate: true,
		 title: 'Distribution of gene lengths', 
		 series:[
			 {
			 showLine:false,
			 markerOptions: { size: 2, style:"circle", color:"green"},
			 color: 'green'
			 },
		 ],
		 axesDefaults: {
			 labelRenderer: $.jqplot.CanvasAxisLabelRenderer
		 },
		 axes: {
			xaxis: {
				label: 'Gene length (aa)',
				renderer: $.jqplot.LogAxisRenderer,
				pad: 0
			},
			yaxis: {
				label: 'Percentage of genes',
				pad: 0,
			}
		 },
		 //seriesColors: pointcolours,
		 highlighter: {
			 tooltipAxes: 'yx',
			 yvalues: 2,
			 show: true,
			 sizeAdjust: 7.5,
			 //formatString: "%d"
			 //formatString: ContigData[0].contig_id +" length: " + ContigData[1].length
			 formatString: 'Percentage: %.2f<br>Frequency: %d <br> Gene length: %d'
	
		 },
		 cursor:{
		 	 show: true,
		 	 zoom:true,
		 	 showTooltip: false,
		 	 //tooltipLocation:'nw'
		 }

	    });
  		
  		$('#chart3').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
            	//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
            	window.open("/search/genome_info?contig_id=" + data[2]);
	    	}
	    );  
	}); 
	
 </script>
  
  </head>

  <body>
  
 <h1>Genome:</h1>
 <table>
 <tr><td><b>Scaffolds</b></td><td><b>N50</b></td><td><b>Largest</b></td><td><b>Smallest</b></td><td><b>GC</b></td><td><b>Non ATGC</b></td></tr>
 <tr><td>${printf("%,d\n",GDB.GenomeInfo.count())}</td><td> </td><td> </td><td> </td><td> </td><td> </td></tr>
 </table>
 
 <h1>Genes:</h1>
 <table>
 <tr><td><b>Number</b></td><td><b>Mean length</b></td><td><b>Mean number of exons</b></td><td><b>Smallest</b></td><td><b>Largest</b></td><td><b>GC</b></td><td><b>Non ATGC</b></td></tr>
 <tr><td>${printf("%,d\n",GDB.GeneInfo.count())}</td><td> </td><td> </td><td> </td><td> </td><td> </td>><td> </td></tr>
 </table>
 	<div id="chart3" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
 	<br>
    <div id="chart1" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
    <br>
    <div id="chart2" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
</body>
</html>
