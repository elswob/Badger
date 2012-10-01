<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} genome search</title>
    <parameter name="search" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasTextRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.highlighter.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.cursor.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.dateAxisRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.logAxisRenderer.js')}" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
    <script type="text/javascript">
    
    function zip(arrays) {
            	    return arrays[0].map(function(_,i){
            	    return arrays.map(function(array){return array[i]})
           });
    }

    function changed(plot_type,params) {
		$("#chart").html('Loading...<img src="${resource(dir: 'images', file: 'spinner.gif')}" />');
		setTimeout(""+plot_type+"('"+params+"')", 2000);
    }
 
    //set the global variable for the plots
    var dlen = [], dcov = [], dgc = [], dcon = [], dcum = [], dcou = [];
    var joinArray = []; 
    var N50 = ${n50}, N90 = ${n90};
    var counter=0;
    var cum = 0;
    var xaxis_label="", yaxis_label="", title_label="", xaxis_type="", yaxis_type="";
    var arraySet;
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
		    graphDraw()
	    }else if (arrayInfo == 'cov_gc'){
		    joinArray = zip([dgc,dcov,dcon,dlen,dgc,dcov]);
		    xaxis_label = "GC";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Coverage";
		    yaxis_type = $.jqplot.LogAxisRenderer;
		    title_label = "Coverage vs GC";
		    graphDraw()
	    }else if (arrayInfo == 'len_cov'){
		    joinArray = zip([dlen,dcov,dcon,dlen,dgc,dcov]);
		    xaxis_label = "Length";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Coverage";
		    yaxis_type = $.jqplot.LogAxisRenderer;
		    title_label = "Length vs Coverage";
		    graphDraw()
	    }else if (arrayInfo == 'cum'){          	    	    
		    joinArray = zip([dcou,dcum,dcon,dlen,dgc,dcov]);
		    xaxis_label = "Contigs ranked by size";
		    xaxis_type = $.jqplot.LinearAxisRenderer;
		    yaxis_label = "Cumulative contig length (bp)";
		    yaxis_type = $.jqplot.LinearAxisRenderer;
		    title_label = "Cumulative contig length";
		    cumDraw()
	    }
	    
    }

    function cumDraw(){
       	//alert(joinArray)
	    plot1 = $.jqplot ('chart', [joinArray,N50,N90],{
		 title: title_label,
		 legend: {
		 	show: true,
		 	location: 'se',
		 },  
		 series:[
			 {
				showLine:false,
			 	markerOptions: { size: 1, style:"circle", color:"green"},
			 	//label: 'No annotation',
			 	color: 'green',
			 	showLabel: false,
			 	highlighter: {
					 tooltipAxes: 'yx',
					 yvalues: 5,
					 show: true,
					 sizeAdjust: 7.5,
					 //formatString: "%d"
					 //formatString: ContigData[0].contig_id +" length: " + ContigData[1].length
					 formatString: '<span style="display:none">%s</span>Contig ID: %s<br>Length: %s<br>GC: %.2f<br>Coverage: %.2f'
			
				 },
			 },
			 {
            	showLine:false,
                markerOptions: { size: 20, style:'circle', color:"blue"},
                label: "N50",
                color: "blue",
                highlighter: {
					 tooltipAxes: 'yx',
					 yvalues: 2,
					 show: true,
					 sizeAdjust: 7.5,
					 formatString: '<span style="display:none">%s</span>N50:%s'
			
				 },
             },
             {
            	showLine:false,
                markerOptions: { size: 20, style:'circle', color:"red"},
                label: "N90",
                color: "red",
                highlighter: {
					 tooltipAxes: 'yx',
					 yvalues: 2,
					 show: true,
					 sizeAdjust: 7.5,
					 formatString: '<span style="display:none">%s</span>N90:%s'
			
				 },
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
    
    function graphDraw(){
       	//alert(joinArray)
	    plot1 = $.jqplot ('chart', [joinArray],{
		 title: title_label,
		 legend: {
		 	show: true,
		 	location: 'se',
		 },  
		 series:[
			 {
				showLine:false,
			 	markerOptions: { size: 1, style:"circle", color:"green"},
			 	//label: 'No annotation',
			 	color: 'green',
			 	showLabel: false,
			 	highlighter: {
					 tooltipAxes: 'yx',
					 yvalues: 5,
					 show: true,
					 sizeAdjust: 7.5,
					 //formatString: "%d"
					 //formatString: ContigData[0].contig_id +" length: " + ContigData[1].length
					 formatString: '<span style="display:none">%s</span>Contig ID: %s<br>Length: %s<br>GC: %.2f<br>Coverage: %.2f'
			
				 },
			 },
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
     <h1>Search the ${printf("%,d\n",GDB.GenomeInfo.count())} contigs by attribute:</h1>
     <div>
     <table>
     	<tr>
     		<td>
     			<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','cum')" value="Cumulative length"/>
     			<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','len_gc')" value="Length vs GC"/>
     			<g:if test = "${grailsApplication.config.coverage.Genome == 'y'}">
     				<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','cov_gc')" value="Coverage vs GC"/>
     				<input type="button" class="mybuttons" id="process_graph" onclick="changed('makeArrays','len_cov')" value="Length vs Coverage"/>
				</g:if>
     		</td>
     	</tr>
		<tr><td><p>Zoom in by dragging around an area. Reset by double clicking or clicking <font STYLE="cursor: pointer" color="green" class="button-reset">here</font></td></tr>
	 </table>   
	 
	  <div id="chart" class="jqplot-target" style="height: 500px; width: 100%; position: center;">Loading...<img src="${resource(dir: 'images', file: 'spinner.gif')}"</div>
	</div>
	 
  <% 
  def jsonData = genomeData.encodeAsJSON(); 
  //println jsonData;
  %>
	<script>
          $(document).ready(function(){          
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
            //draw the graph on load
            setTimeout("makeArrays('cum')", 1000);
            //add the click data here as adding it at the top causes multiple windows to open
            $('#chart').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
            //alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
            window.open("/search/genome_info?contig_id=" + data[2]);
	    	}
	    );      
                    
       });
        </script>
</body>
</html>
