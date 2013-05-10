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
   def highData = newyearData.encodeAsJSON(); 
   def unYears = unYear.encodeAsJSON();
   %>    
    
    //new highcharts plot
    newyearData = ${highData}; 
    years = ${unYears};
    var options = {
		chart: {
			renderTo: 'container',
			defaultSeriesType: 'column'
		},
		title: {
			text: 'PubMed Publications'
		},
		xAxis: {
			categories: years,
			labels: {
                rotation: -90
            }
		},
		yAxis: {
			min: 0,
			title: {
				text: 'Number of publications'
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
					color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
					formatter:function(){
                    	if(this.y > 0)
                        return this.y;
                	}
				}
			},
			series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function() {
                        	//console.log(this); alert('Category: '+ this.category +', value: '+ this.y + 'Series: ' +  this.series.name + ' ID: ')
                            location.href = "publication_search?year=" + this.category+"&sp="+this.series.name;
                        }
                    }
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
   		var dataStore = item.data
   		series.data = item.data
   		//alert(series.data)
   		options.series.push(series)
   		var series = {
    		data: [],
    		name: []
    	};
   	}

   	// Create the chart
    var chart = new Highcharts.Chart(options);
   	
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
   	<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
</body>
</html>
