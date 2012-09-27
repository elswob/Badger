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
  	<script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.barRenderer.min.js')}" type="text/javascript"></script>  	
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  
   <% 
  def jsonCountData = exonCountData.encodeAsJSON(); 
  def jsonDistData = exonDistData.encodeAsJSON(); 
  def jsonGeneData = geneDistData.encodeAsJSON(); 
  def jsonAnnoData = funAnnoData.encodeAsJSON(); 
  def jsonBlastData = blastAnnoData.encodeAsJSON();
  //println jsonAnnoData;
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
				renderer: $.jqplot.LogAxisRenderer,
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
		 title: 'Distribution of exon lengths (<1000bp)', 
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
				renderer: $.jqplot.LogAxisRenderer,
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
	
	FunData = ${jsonAnnoData};    
	var fCount = [], fDb = [], fPer =[];
    
    var FunArray = [];
    for (var i = 0; i < FunData.length; i++) {   		 	 
		var hit = FunData[i];
		if (hit.anno_db == null){
			hit.anno_db = "None"
		}	
		fPer.push((hit.count/"${geneCount}")*100);
		fDb.push(hit.anno_db);
		fCount.push(hit.count)
    }
    FunArray = zip([fCount,fDb]);
    
	var fun_plot = $.jqplot('fun_chart', [FunArray], {
		title: 'Functional annotations', 
  		animate: !$.jqplot.use_excanvas,
  		seriesColors: [ "green"],
        seriesDefaults: {
            renderer:$.jqplot.BarRenderer,
            // Show point labels to the right ('e'ast) of each bar.
            // edgeTolerance of -15 allows labels flow outside the grid
            // up to 15 pixels.  If they flow out more than that, they 
            // will be hidden.
            pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
            // Rotate the bar shadow as if bar is lit from top right.
            shadowAngle: 135,
            // Here's where we tell the chart it is oriented horizontally.
            rendererOptions: {
                barDirection: 'horizontal',
				//shadowDepth: 2,
        		//barMargin: 4,
		    }
        },
        axes: {
        	xaxis: {
				label: 'Number of genes with annotation',
			},
            yaxis: {
                renderer: $.jqplot.CategoryAxisRenderer
            }
        }
    });
    
    BlastData = ${jsonBlastData};    
	var bCount = [], bDb = [], bPer =[];
    
    var BlastArray = [];
    for (var i = 0; i < BlastData.length; i++) {   		 	 
		var hit = BlastData[i];
		if (hit.anno_db == null){
			hit.anno_db = "None"
		}	
		bPer.push((hit.count/"${geneCount}")*100);
		bDb.push(hit.anno_db);
		bCount.push(hit.count)
    }
    BlastArray = zip([bCount,bDb]);
    
	var blast_plot = $.jqplot('blast_chart', [BlastArray], {
		title: 'BLAST homology', 
  		animate: !$.jqplot.use_excanvas,
  		seriesColors: [ "blue"],
        seriesDefaults: {
            renderer:$.jqplot.BarRenderer,
            // Show point labels to the right ('e'ast) of each bar.
            // edgeTolerance of -15 allows labels flow outside the grid
            // up to 15 pixels.  If they flow out more than that, they 
            // will be hidden.
            pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
            // Rotate the bar shadow as if bar is lit from top right.
            shadowAngle: 135,
            // Here's where we tell the chart it is oriented horizontally.
            rendererOptions: {
                barDirection: 'horizontal',
				//shadowDepth: 2,
        		//barMargin: 4,
		    }
        },
        axes: {
        	xaxis: {
				label: 'Number of genes with annotation',
			},
            yaxis: {
                renderer: $.jqplot.CategoryAxisRenderer
            }
        }
    });
	    
	}); 
	
 </script>
  
  </head>

  <body>
  

 

 
 
 <table width=100%>
      <tr><td width=40%>
      	 <h1>Genome:</h1>
 		<table>
      	<tr><td><b>Span (bp):</b></td><td>${printf("%,d\n",genome_stats.span)}</td></tr>
		 <tr><td><b>Scaffolds:</b></td><td>${printf("%,d\n",GDB.GenomeInfo.count())}</td></tr>
		 <tr><td><b>N50:</b></td><td>${printf("%,d\n",genome_stats.n50)}</td></tr>
		 <tr><td><b>Smallest (bp)</b></td><td>${printf("%,d\n",genome_stats.min)}</td></tr>
		 <tr><td><b>Largest (bp)</b></td><td>${printf("%,d\n",genome_stats.max)}</td></tr>
		 <tr><td><b>GC (%)</b></td><td>${printf("%.4g",genome_stats.gc)}</td></tr>
		 <tr><td><b>Non ATGC (bp)</b></td><td>${printf("%,d\n",genome_stats.nonATGC)}</td></tr>
		  </table>
		 <h1>Genes:</h1>
		 <table>
		 <tr><td><b>Number</b></td><td>${printf("%,d\n",GDB.GeneInfo.count())}</td></tr>
		 <tr><td><b>Mean length (bp)</b></td><td>${printf("%,d\n",gene_stats.mean)}</td></tr>
		 <tr><td><b>Smallest (bp)</b></td><td>${printf("%,d\n",gene_stats.min)}</td></tr>
		 <tr><td><b>Largest (bp)</b></td><td>${printf("%,d\n",gene_stats.max)}</td></tr>
		 <tr><td><b>GC (%)</b></td><td>${printf("%.4g",gene_stats.gc)}</td></tr>
		 <tr><td><b>Non ATGC (bp)</b></td><td>${printf("%,d\n",gene_stats.nonATGC)}</td></tr>
		 </table>
	 </td><td>
	 	<br>
 		<div id="blast_chart" class="jqplot-target" style="height: 200px; width: 100%; position: center;"></div>
 		<br>
 		<div id="fun_chart" class="jqplot-target" style="height: 350px; width: 100%; position: center;"></div>
 	 </td></tr>
 </table>
 
 	<div id="chart3" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
 	<br>
    <div id="chart1" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
    <br>
    <div id="chart2" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
</body>
</html>
