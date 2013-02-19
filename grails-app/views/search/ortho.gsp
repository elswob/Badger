<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} orthologs</title>
    <parameter name="search" value="selected"></parameter>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
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
	<script>
	<% 
   		def jsonCountData = false
   		if (c) {jsonCountData = c.encodeAsJSON(); }
   		def jsonSpeciesCountData = false
   		if (p) {jsonSpeciesCountData = p.encodeAsJSON(); }
   		def jsonSpeciesData = false
   		if (p) {jsonSpeciesData = o.encodeAsJSON(); }
  	%>	
  	function zip(arrays) {
            return arrays[0].map(function(_,i){
            return arrays.map(function(array){return array[i]})
         });
    }
        
    $(document).ready(function(){
    
  		//cluster size vs num
  		var CArray = []
		NumData = ${jsonCountData};
		var size = [], num = [];
		var tempArray = [];
		for (var j = 0; j < NumData.length; j++) {   		 	 
			var hit = NumData[j];
			size.push(hit.size);
			num.push(hit.count/hit.size)
		}
		tempArray = zip([size,num]);
		CArray.push(tempArray)
		
		var plot1 = $.jqplot ('chart1', CArray	,{
		animate: true,
		title: 'Cluster size vs frequency', 
		//seriesDefaults:{
		//	markerOptions: { size: 5, style:"circle"},
		//},
		axesDefaults: {
			 tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
			 tickOptions: {
				fontSize: '10pt'
			 }
		 },
		 axes: {
			xaxis: {
				renderer: $.jqplot.LogAxisRenderer,
				label: 'Cluster size',
				pad: 0
			},
			yaxis: {
				renderer: $.jqplot.LogAxisRenderer,
				labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
				label: 'Frequency',
				//pad: 0
			}
		 },
		 //seriesColors: pointcolours,
		 highlighter: {
			 tooltipAxes: 'yx',
			 yvalues: 1,
			 show: true,
			 sizeAdjust: 7.5,
			 formatString: 'Frequency: %.2f<br>Cluster size: %.2f'
	
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
            	//window.open("/search/gene_link?annoType=Exon_num&val=" + data[0]);
	    	}
	    );  
	    
	    //cluster size vs num per gene set
  		var SArray = []
  		var size = []
  		var legendLabels = []
		SpeciesData = ${jsonSpeciesData};
		SpeciesCountData = ${jsonSpeciesCountData};
		
		//get legend
		for (var i = 0; i < SpeciesData.length; i++) {    
			count = SpeciesData[i]; 
			legendLabels.push(count.file_name)	
		}
		var old_size = 0
		var check = {}
		for (var j = 0; j < SpeciesCountData.length; j++) {  
			var count
			var num = [];
			var tempArray = [];			
			var hit = SpeciesCountData[j];
			alert('looking at '+hit.size+": "+hit.file_name) 
			for (var i = 0; i < SpeciesData.length; i++) {    
				count = SpeciesData[i]; 
				//alert('species = '+count.file_name)
				//alert('old_size = '+old_size+' hit.size = '+hit.size)
				if (old_size > 0 && old_size != hit.size){
					alert('checking check!')
					for (var key in check) {
    					//alert([key, check[key]].join("\n\n"));
    					if (check[key] == false){
    						num = []
    						num.push(0)
    						num.push(old_size)
    						tempArray.push(num)
    					}else{
    						tempArray.push(check[key])
    					}
					}
					check = {}
					alert('tArray = '+tempArray)
					SArray.push(tempArray)
					tempArray = [];	
				}
				if (hit.file_name == count.file_name){	
					//alert(count.file_name+' present = '+hit.size)	
					num = [] 	 					
					num.push(hit.count);
					num.push(hit.size)
					tempArray.push(num)
					//check = true;		
					check[count.file_name] = num
					//alert(SArray)
				}else{
					if (check[count.file_name]){
						//alert('l = '+check[count.file_name].length)
						//alert('p = '+check[count.file_name])
						if (check[count.file_name].length < 1){
							check[count.file_name] = false
						}
					}else{
						check[count.file_name] = false
					}
				}
				//alert('check = '+check[count.file_name])

				old_size = hit.size
			}
		}
		//catch the last one
		for (var key in check) {
			//alert([key, check[key]].join("\n\n"));
			if (check[key] == false){
				num = []
				num.push(0)
				num.push(old_size)
				tempArray.push(num)
			}else{
				tempArray.push(check[key])
			}
		}
		alert('tArray = '+tempArray)
		SArray.push(tempArray)
		alert('final = '+SArray)
		
		//var plot2 = $.jqplot('chart2', [[[2,1], [6,2], [7,3], [10,4]], [[7,1], [5,2],[3,3],[2,4]], [[14,1], [9,2], [9,3], [8,4]]], {
		var plot2 = $.jqplot ('chart2', SArray	,{
			animate: true,
			title: 'Cluster size vs frequency per gene set', 
			stackSeries: true,
    		captureRightClick: true,
    		seriesDefaults:{
      			renderer:$.jqplot.BarRenderer,
      			pointLabels: {show: true}
    		},
			axes: {
      			xaxis: {
          			renderer: $.jqplot.CategoryAxisRenderer,
          			//ticks: size
      			},
      			yaxis: {
        			// Don't pad out the bottom of the data range.  By default,
        			// axes scaled as if data extended 10% above and below the
        			// actual range to prevent data points right on grid boundaries.
        			// Don't want to do that here.
        			renderer: $.jqplot.LogAxisRenderer,
        			padMin: 0
      			}
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
    		}      
  		});
  		
  		$('#chart2').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
            	//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
            	//window.open("/search/gene_link?annoType=Exon_num&val=" + data[0]);
	    	}
	    ); 
	    
	}); 
	</script>
</head>
<body>
${p}
<g:link action="">Search</g:link> > Search orthologs
     <h1>Search the clusters:</h1>
     
   		<div id="chart1" class="jqplot-target" style="height: 300px; width: 100%; position: center;"></div>
		<div id="chart2" class="jqplot-target" style="height: 300px; width: 100%; position: center;"></div>
		<div id="chart4" class="jqplot-target" style="height: 300px; width: 100%; position: center;"></div>
 	 <table>
 	 <tr><td><b>Species</b></td><td><b>File</b></td><td><b># clusters</b></td><td><b>Total seqs</b></td><td><b># seqs in clusters</b></td><td><b># singletons</b></td></tr>
 	  	<g:each var="res" in="${o}">
 	 		<tr><td><i>${res.genus} ${res.species}</i></td><td>${res.file_name}</td><td>${sprintf("%,d\n",res.count_ortho)}</td><td>${sprintf("%,d\n",gmap."${res.file_name}")}</td><td>${sprintf("%,d\n",res.count_all)}</td><td>${sprintf("%,d\n",gmap."${res.file_name}" - res.count_all)}</td></tr>
 	 	</g:each>
 	 <tr><td>Total</td><td>n/a</td><td>${sprintf("%,d\n",n.max)}</td><td>${sprintf("%,d\n",badger.Ortho.count())}</td></tr>
 	 </table>
</body>
</html>
