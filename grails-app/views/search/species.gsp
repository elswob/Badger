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
    <script src="https://raw.github.com/mbostock/d3/master/d3.v2.js" type="text/javascript"></script>
    <script src="https://raw.github.com/jasondavies/newick.js/master/src/newick.js" type="text/javascript"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'd3.phylogram.js')}"></script>

    <style type="text/css" media="screen">
      body { font-family: "Helvetica Neue", Helvetica, sans-serif; }
      td { vertical-align: top; }
    </style>
    
    
    <script type="text/javascript">

	$(document).ready(function(){
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
				600, 600
				//'circular'
			);
			phylocanvas = new Smits.PhyloCanvas(
				dataObject,
				'svgCanvas2', 
				600, 600,
				'circular'
			);
			init(); //unitip
		});
		
		
		var newick = Newick.parse("(((OOC:0.0844943664275285,DIM:0.09129505569410035):0.025706590829786657,((LSI:0.12409961518541279,AVI:0.07446234568224375):0.030236339669637013,(LOA:0.0790916403236126,(BMA:0.034560373576475234,WBA:0.032161008868150875):0.06628725081120457):0.014654633492007463):0.01829852483759431):0.43704282407212874,ASC:0.43704282407212874);")
        var newickNodes = []
        function buildNewickNodes(node, callback) {
          newickNodes.push(node)
          if (node.branchset) {
            for (var i=0; i < node.branchset.length; i++) {
              buildNewickNodes(node.branchset[i])
            }
          }
        }
        buildNewickNodes(newick)
        
        d3.phylogram.buildRadial('#radialtree', newick, {
          width: 400,
          skipLabels: true
        })
        
        d3.phylogram.build('#phylogram', newick, {
          width: 300,
          height: 400
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
	    				<a href = "species_search?Gid=${res.id}"><img src="${resource(dir: 'images', file: res.image_file)}" width="150" style="float:left;"/></a>
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
