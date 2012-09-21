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
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  
   <% 
  def jsonCountData = exonCountData.encodeAsJSON(); 
  //println jsonCountData;
  //println ${geneCount}
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
			exNum.push(hit.exon);
			exCount.push(hit.count)
        }
    	exCountNumArray = zip([exNum,exPer,exCount]);
    	//alert(exCountNumArray)
  		//var plot1 = $.jqplot ('chart1', [exCountNumArray]);
  		 		
  		var plot1 = $.jqplot ('chart1', [exCountNumArray],{
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
			 formatString: '<span style="display:none">Percentage: %.3f</span>#genes: %d <br> #exons: %d'
	
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
	}); 
	
 </script>
  
  </head>

  <body>
 
 <h1>Project Data Statistics</h1>
 
 <h2>Metrics for the ${printf("%,d\n",GDB.GeneInfo.count())} genes:</h2>
    <div id="chart1" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
</body>
</html>
