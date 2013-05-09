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
    <script src="${resource(dir: 'js', file: 'Highcharts-3.0.1/js/highcharts.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'Highcharts-3.0.1/js/modules/exporting.js')}" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jqplot/jquery.jqplot.css')}" type="text/css"></link>
  

  <script>
 $(document).ready(function(){
   // create bar chart
   <% 
   def jsonData = yearData.encodeAsJSON();  
   def highData = newyearData.encodeAsJSON(); 
   def unYears = unYear.encodeAsJSON();
   println jsonData;
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
			window.open("publication_search?year=" + year[pointIndex],'_self');
		}
	);
	$(".jqplot-xaxis-label")
    .css({
        cursor: "pointer",
        zIndex: "1"
    })
    .click(function(){ location.href = "http://google.com"; });
    
    
    //new highcharts plot
    newyearData = ${highData}; 
    years = ${unYears};
    var options = {
		chart: {
			renderTo: 'container2',
			defaultSeriesType: 'column'
		},
		title: {
			text: 'Fruit Consumption'
		},
		xAxis: {
			categories: years
		},
		yAxis: {
			min: 0,
			title: {
				text: 'Total fruit consumption'
			},
			stackLabels: {
				enabled: true,
				style: {
					fontWeight: 'bold',
					color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
				}
			}
		},
	   plotOptions: {
			column: {
				stacking: 'normal',
				dataLabels: {
					enabled: true,
					color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
				}
			}
		}, 
		series: []
	};
    
    var series = {
    	data: [],
    	name: []
    };
   	//alert(newyearData)
   	var old_sp = "";
   	for (var i = 0; i < newyearData.length; i++) {
   		//alert('i = '+i)
   		var item = newyearData[i];
   		series.name = item.sid
   		series.data = item.data
   		//alert(series.data)
   		options.series.push(series)
   		var series = {
    		data: [],
    		name: []
    	};
   	}
   
   	//alert(series.name)
   		
   	// Create the chart
    var chart = new Highcharts.Chart(options);
   	
    $(function () {
        $('#container').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'Stacked column chart'
            },
            xAxis: {
                categories: ['Apples', 'Oranges', 'Pears', 'Grapes', 'Bananas']
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Total fruit consumption'
                },
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                }
            },
            legend: {
                align: 'right',
                x: -100,
                verticalAlign: 'top',
                y: 20,
                floating: true,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
                borderColor: '#CCC',
                borderWidth: 1,
                shadow: false
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.x +'</b><br/>'+
                        this.series.name +': '+ this.y +'<br/>'+
                        'Total: '+ this.point.stackTotal;
                }
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true,
                        color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                    }
                }
            },
            
            series: [{
         		data: newyearData
     		}]
            
            // series: [{
//                 name: 'John',
//                 data: [5, 3, 4, 7, 2]
//             }, {
//                 name: 'Jane',
//                 data: [2, 2, 3, 2, 1]
//             }, {
//                 name: 'Joe',
//                 data: [3, 4, 4, 2, 5]
//             }]
        });
    });
});	  
 </script>
  
  </head>

  <body>
 <div class="bread"><g:link action="">Home</g:link> > Publications</div>
 <table><tr><td>
 <h1>Publication search</h1>
 Search the ${sprintf("%,d\n",distinct.count[0])} unique publications from PubMed which contain terms matching the species present in the <b>${grailsApplication.config.projectID}</b> project (updated weekly):<br>
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
   	<div id="container2" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
   	<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
    <div id="chart" class="jqplot-target" style="height: 400px; width: 100%; position: center;"></div>
</body>
</html>
