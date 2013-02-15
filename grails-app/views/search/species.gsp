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
		$.get("${resource(dir: 'trees', file: 'badger_tree.xml')}", function(data) {
			var dataObject = {
				xml: data,
				fileSource: true
			};		
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
			init(); //unitip
		});
	});
	</script>
    
</head>
<body>
	
  	 <g:if test = "${meta}">
  	 <g:link action="">Search</g:link> > Species
  	 	  	 	
  	 <table class="table_100" align="center"><tr>
  	 <g:if test="${grailsApplication.config.t.file}">
  	 	<td><div id="svgCanvas1"> </div></td>
  	  	<td><div id="svgCanvas2"> </div></td>
  	  </g:if>
  	  </tr></table>
  	 	<br>
  	 	<g:each var="res" in="${meta}">
  	 		<div class="inline">
  	 			<a name="${res.genus} ${res.species}"><h2><b><i>${res.genus} ${res.species}</i></b></h2></a>	
  	 			<g:form name="selectSpecies" action="species_v" params="[Sid: res.id]">	
      				<input class="smallbuttons" type="button" value="Select" id="process" onclick="submit()" >
      			</g:form>
  	 		</div>
  	 		<table>
  	 			<tr>
  	 				<td width=150>
  	 					<g:if test="${grailsApplication.mainContext.getResource('images/'+res.image_file).exists()}"> 
	    					<a href = "species_v?Sid=${res.id}"><img src="${resource(dir: 'images', file: res.image_file)}" width="150" style="float:left;"/></a>
	    				</g:if>
	    				<g:else>
	    					<a href = "species_v?Sid=${res.id}"><img src="${resource(dir: 'images', file: grailsApplication.config.headerImage)}" width="150" style="float:left;"/></a>
	    				</g:else>
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
