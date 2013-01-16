<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} species</title>
    <parameter name="search" value="selected"></parameter>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'raphael-min.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jsphylosvg-1.55/jsphylosvg-min.js')}"></script>   
    <style type="text/css">
            @import "${resource(dir: 'css', file: 'unitip.css')}";
    </style>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'unitip.js')}"></script>

    <style type="text/css" media="screen">
      body { font-family: "Helvetica Neue", Helvetica, sans-serif; }
      td { vertical-align: top; }
    </style>
    
    
    <script type="text/javascript">

	$(document).ready(function(){
		var paperWidth1 = $('#svgCanvas1').width();
		var paperWidth2 = $('#svgCanvas2').width();
		//alert(paperWidth1)
		//$.get("/trees/10-coffee.xml", function(data) {
		//$.get("/trees/filarial_rooted.xml", function(data) {
		//$.get("/trees/all_nem.xml", function(data) {
		//$.get("/trees/GK_nem_rooted.xml", function(data) {
		$.get("/trees/badger_tree.xml", function(data) {
			var dataObject = {
				xml: data,
				fileSource: true
			};		
			//Smits.PhyloCanvas.Render.Style.text["font-size"] = 12;
			phylocanvas = new Smits.PhyloCanvas(
				dataObject,
				'svgCanvas1', 
				paperWidth1/1.1, paperWidth1/1.1
				//'circular'
			);
			phylocanvas = new Smits.PhyloCanvas(
				dataObject,
				'svgCanvas2', 
				paperWidth2/1.1, paperWidth2/1.1,
				'circular'
			);
			//Smits.ZoomCanvas.init($('#svgCanvas1 svg'), 2000, 2000);
			//Smits.ZoomCanvas.scale(0.5);			
			init(); //unitip
		});
	});
	</script>
    
</head>
<body>
	
  	 <g:if test = "${meta}">
  	 <g:link action="">Search</g:link> > Species
  	 <!--
  	 <table>
      <tr>
        <td>
          <h2>Circular Dendrogram</h2>
          <div id='radialtree'></div>
        </td>
        <td>
          <h2>Phylogram</h2>
          <div id='phylogram'></div>
        </td>
      </tr>
    </table>
  	 -->
  	 <table class="table_100" align="center"><tr>
  	 <td><div id="svgCanvas1"> </div></td>
  	  <td><div id="svgCanvas2"> </div></td>
  	  </tr></table>
  	 	<br>
  	 	<g:each var="res" in="${meta}">
  	 		<a name="${res.genus} ${res.species}"><h2><b><i>${res.genus} ${res.species}</i></b></h2></a>		
  	 		<table>
  	 			<tr>
  	 				<td width=150> 
	    				<a href = "species_v?Sid=${res.id}"><img src="${resource(dir: 'images', file: res.image_file)}" width="150" style="float:left;"/></a>
	    			</td>
	    			<td>
	    				<div style="overflow:auto; padding-right:2px; height:150px">
	    					<p>${res.description}</p>
	    					<br><font size="1">Picture supplied by ${res.image_source}</font>
	    				</div>
	    			</td>
	    		</tr>
	    	</table>
		</g:each>
		</table>	
  	 </g:if>
  	 <g:else>
  	 	<h2>There are no species in the database at present, please add some</h2>
  	 </g:else>
  </table>
</body>
</html>
