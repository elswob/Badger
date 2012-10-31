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
  	<script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.enhancedLegendRenderer.min.js')}" type="text/javascript"></script> 	
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  
   <% 
  def jsonCountData = exonCountData.encodeAsJSON(); 
  def jsonDistData = exonDistData.encodeAsJSON(); 
  def jsonGeneData = geneDistData.encodeAsJSON(); 
  def jsonAnnoData = funAnnoData.encodeAsJSON(); 
  def jsonBlastData = blastAnnoData.encodeAsJSON();
  def jsonGeneCountData = geneCountData.encodeAsJSON();
  //println jsonGeneData;
  %>	

  <script>
  
  	function zip(arrays) {
            return arrays[0].map(function(_,i){
            return arrays.map(function(array){return array[i]})
         });
    }
        
    $(document).ready(function(){
   		
   		//get gene counts
		GCountData = ${jsonGeneCountData};		
		
		//exons per gene
		NumData = ${jsonCountData};
		var CArray = []
		var legendLabels = []
		for (var i = 0; i < GCountData.length; i++) {   		
			var count = GCountData[i];
			//alert('species = '+count.species+' count = '+count.count)
    		var exCountNumArray = [];
    		var exCount = [], exNum = [], exPer = [];
    		var meanCount = 0
    		
			for (var j = 0; j < NumData.length; j++) {   		 	 
				var hit = NumData[j];
				if (hit.species == count.species){
					meanCount += (hit.count*hit.num)
					exPer.push((hit.count/count.count)*100);
					exNum.push(hit.num);
					exCount.push(hit.count)
				}
			}
			if (exNum.length>0){
				meanCount = meanCount/count.count
				exCountNumArray = zip([exNum,exPer,exCount]);
				CArray.push(exCountNumArray)
				legendLabels.push(count.genus+" "+count.species+" (mean="+meanCount.toFixed(2)+")")
			}

		}
  		var plot1 = $.jqplot ('chart1', CArray	,{
  		 animate: true,
		 title: 'Distribution of exons per gene', 
		 legend: {
		 	renderer: $.jqplot.EnhancedLegendRenderer,
            rendererOptions: {
            	seriesToggle: 'slow',
                seriesToggleReplot: { resetAxes: true }
            },
            show: true,
            placement: 'ne',
            labels: legendLabels
         },
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
            	window.open("/search/gene_link?annoType=Exon_num&val=" + data[0]);
	    	}
	    );  
	    
	    //exon lengths    
    	DistData = ${jsonDistData};
    	legendLabels = []
    	var DArray = []
    	for (var i = 0; i < GCountData.length; i++) {   		
			var count = GCountData[i];
			var exDCount = [], exDNum = [], exDPer = [];
			var exDistNumArray = [];
			var meanCount = 0
			for (var j = 0; j < DistData.length; j++) {   		 	 
				var hit = DistData[j];
				if (hit.species == count.species){
					meanCount += (hit.count*hit.num)
					exDPer.push((hit.count/count.count)*100);
					exDNum.push(hit.num);
					exDCount.push(hit.count)
				}
			}
			if (exDNum.length>0){
				meanCount = meanCount/count.count
    			exDistNumArray = zip([exDNum,exDPer,exDCount]);
    			DArray.push(exDistNumArray)
    			legendLabels.push(count.genus+" "+count.species+" (mean="+meanCount.toFixed(2)+")")
    		}
    	}		
  		var plot2 = $.jqplot ('chart2', DArray,{
  		 animate: true,
		 title: 'Distribution of exon lengths', 
		 seriesDefaults:{
		 	showLine:false,
		 	markerOptions: { size: 2, style:"circle"},
		 },
		 legend: {
		 	renderer: $.jqplot.EnhancedLegendRenderer,
            rendererOptions: {
            	seriesToggle: 'slow',
                seriesToggleReplot: { resetAxes: true }
            },
            show: true,
            placement: 'ne',
            labels: legendLabels
         },
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
            	window.open("/search/gene_link?annoType=Exon_length&val=" + data[0]);
	    	}
	    );  
	    
	    //gene lengths
	    GDistData = ${jsonGeneData};
	    
	    var GArray = [];
	    var legendLabels = [];
		for (var i = 0; i < GCountData.length; i++) {   		
			var count = GCountData[i];
			//alert(count.species)
		
			var GCount = [], GNum = [], GPer = [];
			var meanCount = 0	
			var GDistNumArray = [];
			
			for (var j = 0; j < GDistData.length; j++) {   		 	 
				var hit = GDistData[j];
				//alert(hit.count)
				if (hit.species == count.species){
					meanCount += (hit.count*hit.num)
					GPer.push((hit.count/count.count)*100);
					GNum.push(hit.num);
					GCount.push(hit.count)
				}
			}
			if (GNum.length>0){
				meanCount = meanCount/count.count
				GDistNumArray = zip([GNum,GPer,GCount]);
				//alert(GDistNumArray)
				GArray.push(GDistNumArray)
				legendLabels.push(count.genus+" "+count.species+" (mean="+meanCount.toFixed(2)+")")
			}
		}
  		 		
  		var plot3 = $.jqplot ('chart3', GArray,{
  		 animate: true,
		 title: 'Distribution of gene lengths', 
		 seriesDefaults:{
		 	showLine:false,
		 	markerOptions: { size: 2, style:"circle"},
		 },
		 legend: {
		 	renderer: $.jqplot.EnhancedLegendRenderer,
            rendererOptions: {
            	seriesToggle: 'slow',
                seriesToggleReplot: { resetAxes: true }
            },
            show: true,
            placement: 'ne',
            labels: legendLabels
         },
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
            	window.open("/search/gene_link?annoType=Length&val=" + data[0]);
	    	}
	    );  
	

	//length and gc of exons by number
	exonLenNumData = ${exonLenNum}
	exonGCNumData = ${exonGCNum}
	var exonNumPlot = $.jqplot ('exon_num', [exonLenNumData, exonGCNumData],{
		legend: {
 		   show: true,
    		location: 'nw',
    		xoffset: 1,
    		fontSize: '11px',
    		rowSpacing: '10px',
    		textColor: '#222222',
    		fontFamily: 'Lucida Grande, Lucida Sans, Arial, sans-serif'
		 },
  		 animate: true,
		 title: 'Mean Length and GC content of exons by exon number', 
		 series:[
			 {
			 //showLine:false,
			 label:'Length',
			 markerOptions: { size: 2, style:"circle", color:"green"},
			 color: 'green'
			 },
			 {
			 //showLine:false,
			 label:'GC',
			 markerOptions: { size: 2, style:"circle", color:"orange"},
			 color: 'orange',
			 yaxis:'y2axis'
			 },
		 ],
		 axesDefaults: {
			 labelRenderer: $.jqplot.CanvasAxisLabelRenderer
		 },
		 axes: {
			xaxis: {
				//renderer: $.jqplot.LogAxisRenderer,
				label: 'Exon number',
				pad: 0
			},
			yaxis: {
				autoscale:true, 
				label: 'Length (bp)',
				pad: 0,
			},
			y2axis:{
				label: 'GC (%)',
          		autoscale:true, 
          		tickOptions:{showGridline:false}
      		}
		 },
		 //seriesColors: pointcolours,
		 highlighter: {
			 tooltipAxes: 'yx',
			 yvalues: 1,
			 show: true,
			 sizeAdjust: 7.5,
			 //formatString: "%d"
			 //formatString: ContigData[0].contig_id +" length: " + ContigData[1].length
			 formatString: 'Value: %d<br>Exon: %d'
	
		 },
		 cursor:{
		 	 show: true,
		 	 zoom:true,
		 	 showTooltip: false,
		 	 //tooltipLocation:'nw'
		 }
	    });      
	}); 
	
 </script>
  
  </head>

  <body>

 
 	<div id="chart3" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
 	<br>
    <div id="chart1" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
    <br>
    <div id="chart2" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
	<br>
    <div id="exon_num" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
</body>
</html>
