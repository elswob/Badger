<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} publications</title>
  <parameter name="publications" value="selected"></parameter>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.barRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.categoryAxisRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.pointLabels.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasTextRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js')}" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  

  <script>
 $(document).ready(function(){
   // create bar chart
   <% 
   def jsonData = yearData.encodeAsJSON();  
   //println "loaded data";
   %>    
   yearData = ${jsonData};  	  
   var counts = [];
   var year = [];
   for (var i = 0; i < yearData.length; i++) {
   		var hit = yearData[i];
   		counts.push(hit.count);
   		year.push(hit.date_part);
   }

    var plot1 = $.jqplot('chart', [counts], {
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            rendererOptions: {
		fillToZero: true,
		shadowDepth: 5,
            	barMargin: 4,
	    }
        },
        seriesColors: [ "green"],
        axesDefaults: {
            tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
            tickOptions: {
        	angle: -90,
        	fontSize: '10pt'
            }
        },
	axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: year,
                label: 'Year'
            },
            yaxis: {
            	labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                pad: 1.05,
		min: 0,
                label: 'Number of Publications',
                tickOptions: {
		    formatString: '%d',
		    angle: 0
		},
            }
        }
    });
    $('#chart').bind('jqplotDataClick',
		function (ev, seriesIndex, pointIndex, data) {
			//alert('series: '+seriesIndex+', point: '+pointIndex+', data: '+year[pointIndex]);
			window.open("/home/publication_search?year=" + year[pointIndex],'_self');
		}
	);
});	  
 </script>
  
  </head>

  <body>
 
 <table><tr><td>
 <h1>Publication search</h1>
 Search the ${distinct.count[0]} unique publications from PubMed which contain terms matching the species present in the <b>${grailsApplication.config.projectID}</b> project (updated weekly):<br>
 - To search by year click on a bar in the chart below<br>
 - To search by keyword use the search box.
 </td>

   <g:form action="publication_search">
 <td>
    
    <h1>Choose what to search:</h1>
    <label><input type="checkbox" checked="yes" name="pubVal" value="title" /> Title</label><br>
    <label><input type="checkbox" checked="yes" name="pubVal" value="abstract_text" /> Abstract</label><br>
    <label><input type="checkbox" checked="yes" name="pubVal" value="authors" /> Authors</label><br>
    <label><input type="checkbox" checked="yes" name="pubVal" value="journal" /> Journal</label><br>
    
   </td>
   <td>
    <h1>Enter a search term:</h1>(To see all publications leave the box blank)<br>
    <div id='selectedResult'></div>
    <g:textField name="searchId"  size="30"/>
    <input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
     </td>
   
   </tr>
   </table>
   </g:form>
    <div id="chart" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
</body>
</html>
