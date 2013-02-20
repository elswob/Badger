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
		var sizeMap = {};
		var tempArray1 = [];
		var tempArray2 = [];
		for (var j = 0; j < NumData.length; j++) {   		 	 
			var hit = NumData[j];
			size.push(hit.size);
			num.push(hit.count/hit.size)
			sizeMap[hit.size]=hit.count
		}
		tempArray1 = zip([size,num]);
		tempArray2 = num
		CArray.push(tempArray1)
		
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
  		var test = {}
  		var size = []
  		var legendLabels = []
		SpeciesData = ${jsonSpeciesData};
		SpeciesCountData = ${jsonSpeciesCountData};
		
		//get legend
		for (var i = 0; i < SpeciesData.length; i++) {    
			count = SpeciesData[i]; 
			legendLabels.push(count.file_name)	
		}
		//get ticks
		var ticks = [];
		for (var j = 0; j < SpeciesCountData.length; j++) { 
			var hit = SpeciesCountData[j];
			if($.inArray(hit.size, ticks) < 0) {
				ticks.push(hit.size)
			}
		}
		var old_size = 0
		var check = {}
		for (var i = 0; i < SpeciesData.length; i++) {   
			var count = SpeciesData[i]; 
			var num
			var tempArray = [];					
			for (var j = 0; j < SpeciesCountData.length; j++) { 
				var hit = SpeciesCountData[j];
				if (old_size > 0 && old_size != hit.size){
					//alert('checking check!')
					for (var key in check) {
    					if (check[key] == false){
    						num = 0
    						//num.push(old_size)
    						tempArray.push(num)
    					}else{
    						tempArray.push(check[key])
    					}
					}
					check = {}
				}
				if (hit.file_name == count.file_name){	
					num = hit.count	
					check[count.file_name] = num/sizeMap[hit.size]*100
				}else{
					if (check[count.file_name]){
						if (check[count.file_name].length < 1){
							check[count.file_name] = false
						}
					}else{
						check[count.file_name] = false
					}
				}
				old_size = hit.size
			}
			//catch the last one
			for (var key in check) {
				if (check[key] == false){
					num = 0
					tempArray.push(num)
				}else{
					tempArray.push(check[key])
				}
			}
			check = {}
			test[count.file_name] = tempArray
			//SArray = []
			SArray.push(tempArray)
		}
		
		var plot2 = $.jqplot ('chart2', SArray	,{
			animate: true,
			title: 'Cluster size vs gene set percentage', 
			stackSeries: true,
            captureRightClick: true,
            seriesDefaults:{
                renderer:$.jqplot.BarRenderer,
                rendererOptions: {
                    highlightMouseDown: true   
                },
                //pointLabels:{show:true, stackedValue: true}
            },
            axes: {
      			xaxis: {
          			renderer: $.jqplot.CategoryAxisRenderer,
          			ticks: ticks,
          			label: 'Cluster size',
      			},
      			yaxis: {
					// Don't pad out the bottom of the data range.  By default,
					// axes scaled as if data extended 10% above and below the
					// actual range to prevent data points right on grid boundaries.
					// Don't want to do that here.
					padMin: 0,
					max:100,
					labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
					label: 'Percentage',
					//renderer: $.jqplot.LogAxisRenderer,
				  }
    		},
            legend: {
      			renderer: $.jqplot.EnhancedLegendRenderer,
				rendererOptions: {
					seriesToggle: 'slow',
					seriesToggleReplot: { resetAxes: true }
				},
				show: true,
				location: 'e',
                placement: 'outside',
				labels: legendLabels
    		}        
        });
		
		var plot3 = $.jqplot ('chart3', [SArray[0],SArray[1],SArray[2],SArray[3],tempArray2]	,{
			animate: true,
			title: 'Cluster size vs gene set percentage', 
			stackSeries: true,
            axesDefaults: {
			 	tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
			 	tickOptions: {
					fontSize: '10pt'
			 	}
		 	},
		 	seriesDefaults:{
            	renderer:$.jqplot.BarRenderer,
            	label:legendLabels,
            	yaxis:'y2axis',            		
            	captureRightClick: true,
            },
            series:[
            	//{
            	//	renderer:$.jqplot.BarRenderer,
            	//	label:legendLabels,
            	//	yaxis:'y2axis',            		
            	//	captureRightClick: true,
            	{label:legendLabels[0],},{label:legendLabels[1],},{label:legendLabels[2],},{label:legendLabels[3],}, 
            	{          		
            		disableStack: true, 
            		yaxis:'yaxis',
            		label:'Cluster frequency',	
            		renderer:$.jqplot.LineRenderer,
            	}
            ],
            //seriesDefaults:{
            //    renderer:$.jqplot.BarRenderer,
            //    rendererOptions: {
            //        highlightMouseDown: true   
            //    },
                //pointLabels:{show:true, stackedValue: true}
            //},
            legend: {
						renderer: $.jqplot.EnhancedLegendRenderer,
						rendererOptions: {
							seriesToggle: 'slow',
							seriesToggleReplot: { resetAxes: true }
						},
						show: true,
						location: 'e',
						placement: 'outside',
						marginLeft: '80px',
					},        
            axes: {
      			xaxis: {
          			renderer: $.jqplot.CategoryAxisRenderer,
          			ticks: ticks,
          			label: 'Size',
      			},
      			yaxis: {
					renderer: $.jqplot.LogAxisRenderer,
					labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
					label: 'Frequency',
					//autoscale:true
					//renderer: $.jqplot.LogAxisRenderer,
				},
				y2axis: {
        			padMin: 0,
					max:100,
					min:0,
					labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
					label: 'Percentage',
					pad:0,
					//autoscale:true
      			}	
    		},
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
        
	}); 
	</script>
</head>
<body>
<g:link action="">Search</g:link> > Search orthologs
     <h1>Search the clusters:</h1>
     	<div id="chart3" class="jqplot-target" style="height: 300px; width: 80%; position: center;"></div>
   		<div id="chart1" class="jqplot-target" style="height: 300px; width: 100%; position: center;"></div>
		<div id="chart2" class="jqplot-target" style="height: 300px; width: 80%; position: center;"></div>
 	 <table>
 	 <tr><td><b>Species</b></td><td><b>File</b></td><td><b># clusters</b></td><td><b>Total seqs</b></td><td><b># seqs in clusters</b></td><td><b># singletons</b></td></tr>
 	  	<g:each var="res" in="${o}">
 	 		<tr><td><i>${res.genus} ${res.species}</i></td><td>${res.file_name}</td><td>${sprintf("%,d\n",res.count_ortho)}</td><td>${sprintf("%,d\n",gmap."${res.file_name}")}</td><td>${sprintf("%,d\n",res.count_all)}</td><td>${sprintf("%,d\n",gmap."${res.file_name}" - res.count_all)}</td></tr>
 	 	</g:each>
 	 <tr><td>Total</td><td>n/a</td><td>${sprintf("%,d\n",n.max)}</td><td>${sprintf("%,d\n",badger.Ortho.count())}</td></tr>
 	 </table>
</body>
</html>
