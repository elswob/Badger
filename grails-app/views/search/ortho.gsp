<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} | Orthologs</title>
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
  	<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
  	<script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/TableTools.js')}" type="text/javascript"></script>
  	<script src="${resource(dir: 'js', file: 'TableTools-2.0.2/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
  	<link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  	<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/data_table.css')}" type="text/css"></link>
  	<link rel="stylesheet" href="${resource(dir: 'js', file: 'TableTools-2.0.2/media/css/TableTools.css')}" type="text/css"></link>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
	<script>
	<% 
   		def jsonCountData = false
   		if (c) {jsonCountData = c.encodeAsJSON(); }
   		def jsonSpeciesCountData = false
   		if (p) {jsonSpeciesCountData = p.encodeAsJSON(); }
   		def jsonSpeciesData = false
   		if (o) {jsonSpeciesData = o.encodeAsJSON(); }
  	%>	
  	function zip(arrays) {
            return arrays[0].map(function(_,i){
            return arrays.map(function(array){return array[i]})
         });
    }
    
    function switchTab(tabShow,tabHide) {
		$("#tab_"+tabHide).hide();
		$("#tab_"+tabShow).show();
    }
        
    $(document).ready(function(){
    
  		//cluster size vs num
  		var CArray = []
		NumData = ${jsonCountData};
		var size = [], num = [];
		var sizeMap = {};
		for (var j = 0; j < NumData.length; j++) {   		 	 
			var hit = NumData[j];
			num.push(hit.count/hit.size)
			sizeMap[hit.size]=hit.count
		}
		var numArray = num
	    
	    //cluster size vs num per gene set
  		var SArray = []
  		var size = []
		SpeciesData = ${jsonSpeciesData};
		SpeciesCountData = ${jsonSpeciesCountData};
		
		//get legend and series info for gene sets
		var seriesInfo = []		
		for (var i = 0; i < SpeciesData.length; i++) {  
			series=new Object();
			count = SpeciesData[i]; 
			series.label = count.genus+" "+count.species+"<br>("+count.file_name+")";
			seriesInfo.push(series)
		}
		//set final series info for line
		series=new Object();		
		series.disableStack = true;
		series.yaxis = 'yaxis';
		series.label = 'Cluster frequency';
		series.renderer = $.jqplot.LineRenderer;
		series.color = 'blue';
		series.pointLabels = false;
		seriesInfo.push(series)
		//alert('s = '+seriesInfo)
		
		//get ticks for x axis
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
					for (var key in check) {
    					if (check[key] == false){
    						num = 0
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
			SArray.push(tempArray)
			
		}
		SArray.push(numArray)
		//var plot3 = $.jqplot ('chart3', [SArray[0],SArray[1],SArray[2],SArray[3],numArray]	,{
		var plot3 = $.jqplot ('chart1', SArray	,{
			animate: true,
			//title: 'Cluster size vs gene set percentage', 
			stackSeries: true,
            axesDefaults: {
			 	tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
			 	tickOptions: {
					fontSize: '10pt'
			 	}
		 	},
		 	seriesDefaults:{
            	renderer:$.jqplot.BarRenderer,
            	yaxis:'y2axis',            		
            	captureRightClick: true,
            	pointLabels:{show:true, stackedValue: true}
            },
            
            series:seriesInfo,

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
          			tickOptions: {
          				angle: -90,
          			},
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
    		/*
    		highlighter: {
				 tooltipAxes: 'yx',
				 useXTickMarks: true, 
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
			 */
        });
        $('#chart1').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
            	//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+ticks[pointIndex]);
            	window.open("ortho_search?type=bar&val=" + ticks[pointIndex],"_self");
	    	}
	    );  
	    

		var oTable = $('#stats').dataTable( {
			"bProcessing": true,
			"sPaginationType": "full_numbers",
			"iDisplayLength": 10,
			"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
			"oLanguage": {
				"sSearch": "Filter records:"
			},
			"aaSorting": [[ 0, "asc" ]],
			"sDom": 'T<"clear">lfrtip',
			"oTableTools": {
				"sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.0.2/media/swf/copy_cvs_xls_pdf.swf')}"
			}
		} );
		
		//auto fill the library search boxes
		$('.tball').keyup(function(){
			var content = $('.tball').val();
			$('.tbfill').val(content);
		});
		//auto change the selects
		$(".selectall").change(function() { //this occurs when select 1 changes
			$(".selectauto").val($(this).val());
			//alert(/.*?/+$(this).val())
		});
        
	}); 
	</script>
</head>
<body>
<g:link action="">Search</g:link> > Search orthologs
	<br><br>
	<g:if test="${o}">
	<div class="introjs-search-ortho-1">
		<div id="tab_1">
			<input type="button" class="tabbuttons" id="show_metrics" value="Metrics" style="color:#BFBFBF"/>
			<input type="button" class="tabbuttons" id="show_search" onclick="switchTab('2','1')" value="Search" data-position='top' data-intro='Search the ortholog data in more detail.' data-step='3'/>
			<div style="border:2px solid; border-color:#BFBFBF">
		
				<h3>Click on a bar or point to view the clusters of that size:</h3><br>
				<div id="chart1" class="jqplot-target" style="height: 300px; width: 80%; position: center;" data-position='top' data-intro='Plot of ortholog groups showing size and frequencey of each group and the percentage of each species per group.<br><br>Click on a bar or point on the line to get details for that group.' data-step='1'></div>
				
				<table cellpadding="0" cellspacing="0" border="0" class="display" id="stats">
					<thead data-position='top' data-intro='Ortholog statistics for each species.' data-step='2'>
						<tr><td><b>Species</b></td><td><b>File</b></td><td><b># clusters</b></td><td><b>Total seqs</b></td><td><b># seqs in clusters</b></td><td><b># singletons</b></td></tr>
					</thead>
					<tbody>	
						<g:each var="res" in="${o}">
							<tr><td><i>${res.genus[0]}. ${res.species}</i></td><td>${res.file_name}</td><td>${sprintf("%,d\n",res.count_ortho)}</td><td>${sprintf("%,d\n",gmap."${res.file_name}")}</td><td>${sprintf("%,d\n",res.count_all)}</td><td>${sprintf("%,d\n",gmap."${res.file_name}" - res.count_all)}</td></tr>
						</g:each>
					</tbody>
				</table>		
				<br><br><br>
			</div>
	    </div>
	 </div>
	 <div class="introjs-search-ortho-2">
	 <div id="tab_2" style="display:none">
	 <input type="button" class="tabbuttons" id="show_metrics" onclick="switchTab('1','2')" value="Metrics" />
	 <input type="button" class="tabbuttons" id="show_search" value="Search" style="color:#BFBFBF"/>
	 <div style="border:2px solid; border-color:#BFBFBF">
		<g:if test="${o.size()>1}">
			<h1>Search by group metric:</h1>
			<h3>Search the ortholog groups by the number of sequences per species. Leave blank for zero sequences.<h3> 
		
			<fieldset id="counts">	
				<g:form action="ortho_search" params="${[type:'count']}">
					<table class="blast" data-position='top' data-intro='Search the ortholog groups by member distribution.' data-step='1'>
					
						<tr><td></td><td>Select all</td><td>
						<SELECT NAME=allsign class="selectall">
							<OPTION selected="selected" VALUE="eq">equal to
							<OPTION VALUE="gt">greater than
							<OPTION VALUE="lt">less than     
						 </SELECT>
						<INPUT TYPE=text NAME=allcount VALUE="" SIZE=3 class="tball">
						<g:each var="res" in="${o}">
							<g:if test="${res.file_type == 'Genes' && res.loaded == true}">		
								<g:if test="${res.search == 'priv' && user == 'user' || res.search == 'pub'}">	
									<tr><td><input type="hidden" name="orthoCheck" id="orthoCheck" value="${res.file_name}" /></td><td><i>${res.genus} ${res.species}</i> (${res.file_name})</td><td>
										<SELECT id="orthoSign" NAME="orthoSign" class="selectauto">
											<OPTION VALUE="eq">equal to
											<OPTION VALUE="gt">greater than
											<OPTION VALUE="lt">less than     
										</SELECT>
										<INPUT id="orthoCount" TYPE="text" NAME="orthoCount" VALUE="" SIZE=3 class="tbfill">
									</tr>
								</g:if>
							</g:if>
						</g:each>				
					</table>
					<input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
			   </g:form>
			</fieldset>
			<hr size = 5 color="green" width="100%" style="margin-top:10px">
		</g:if>

		<h1>Search the annotation descriptions associated with transcripts marked as orthologs:</h3> 
		<fieldset id="blast_dbs">
			<g:form action="ortho_search" params="${[type:'search']}">		
			<table data-position='top' data-intro='Search the ortholog groups by annotation text.' data-step='2'><tr>
				<td>
			
				<h2>Choose what to search:</h1>
				<label><input type="checkbox" checked="yes" name="oVal" value="blast" /> BLAST similarity</label><br>
				<label><input type="checkbox" checked="yes" name="oVal" value="anno" /> Functional annotations</label><br>
				<label><input type="checkbox" checked="yes" name="oVal" value="inter" /> InterPro domains</label><br>
			
			   </td>
			   <td>
				<h2>Enter a search term:</h1><br>
				<div id='selectedResult'></div>
				<g:textField name="searchId"  size="60"/>
				<input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
				 </td>
		   </tr>
		   </table>
		   </g:form>
		</fieldset>
	</div>
	</div>
	</div>
</g:if>
<g:else>
	<h1>There is no ortholog information in the database</h1>
</g:else>
</body>
</html>
