<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} statistics</title>
  <parameter name="stats" value="selected"></parameter>
  <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
  <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
   <script type="text/javascript"> 
    	$(window).unload(function() {});
    	
		function toggleDiv(divId) {
			//$('.toHide').hide();
            $("#"+divId).slideToggle(400);
    	}

    </script>
    <script>
     $(document).ready( function() {
 
        // Select all
        $("A[href='#select_all']").click( function() {
            $("#" + $(this).attr('rel') + " INPUT[type='checkbox']").attr('checked', true);
            return false;
        });
 
        // Select none
        $("A[href='#select_none']").click( function() {
            $("#" + $(this).attr('rel') + " INPUT[type='checkbox']").attr('checked', false);
            return false;
        });
 
        // Invert selection
        $("A[href='#invert_selection']").click( function() {
            $("#" + $(this).attr('rel') + " INPUT[type='checkbox']").each( function() {
                $(this).attr('checked', !$(this).attr('checked'));
            });
            return false;
        });
 
    });
   </script> 
  </head>

  <body>
	<div class="bread"><g:link action="">Home</g:link> > Statistics</div>	
 	<div id="content">
		<g:form action="stats_results" method="post">
		<table><tr><td>
			<div class="inline">
				<label><a href = "" STYLE="cursor: pointer" onclick="toggleDiv('blk_1');"><h1>Choose species:</h1></a></label>
				(click to show/hide available species)
			</div>
			<fieldset id="speciesSelect">	
				<!--div id="blk_1" style="display:none"-->
				<div id="blk_1">
					<table class="compact">
						<tr>
							</td>Select <a rel="speciesSelect" href="#select_all">All</a> | 
							<a rel="speciesSelect" href="#select_none">None</a> | 
							<a rel="speciesSelect" href="#invert_selection">Invert</a>
							</td>
						</tr>
						<g:each var="res" in="${species}">
							<g:if test="${res.file_type == 'Genome'}">		
								<g:if test="${res.search == 'priv' && isLoggedIn()}">	
									<tr><td><g:checkBox name="speciesCheck" value="${res.meta.species}" /></td><td><i>${res.meta.genus} ${res.meta.species}</td></tr>
								</g:if>
								<g:elseif test="${res.search == 'pub'}">
									<tr><td><g:checkBox name="speciesCheck" value="${res.meta.species}" /></td><td><i>${res.meta.genus} ${res.meta.species}</td></tr>
								</g:elseif>
							</g:if>
						</g:each>
					</table>
				</div>
			</fieldset>
		</td></tr>
		<tr><td><h1>Choose figure type:</h1>
		<fieldset>
			<table class="compact">
				<tr><td> <label><input checked="checked" name="figure" type="radio" id="fig" value="1" STYLE="cursor: pointer"> Scaffold metrics</label></td></tr>
				<tr><td> <label><input name="figure" type="radio" id="fig" value="2" STYLE="cursor: pointer"> Distribution of gene lengths</label></td></tr>
				<tr><td> <label><input name="figure" type="radio" id="fig" value="3" STYLE="cursor: pointer"> Distribution of exons per gene</label></td></tr>
				<tr><td> <label><input name="figure" type="radio" id="fig" value="4" STYLE="cursor: pointer"> Distribution of exon lengths</label></td></tr>
				<tr><td> <label><input name="figure" type="radio" id="fig" value="5" STYLE="cursor: pointer"> Mean length and GC content by exon number</label></td></tr>
			</table>
		</fieldset>
		</td></tr>
		</table>
		<div id="buttons">
			<input class="mybuttons" type="button" value="Generate figure" id="process" onclick="submit()" >
		</div>
	  </g:form>
	</div>
 	<script>
          $(document).ready(function(){	  
            $("#process").bind("click", function () {
              $("#content").mask("Generating figure...");
            });
          });
   </script>
</body>
</html>
