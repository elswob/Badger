<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} | Search all</title>
    <parameter name="search" value="selected"></parameter>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
    <script type="text/javascript"> 
    	$(window).unload(function() {});
    </script>
	
</head>
<body>
<g:link action="">Search</g:link> > Search all
  <div id="content">
     <h1>Search all the data by keyword:</h1>
 
   <g:form action="all_searched">
    <table>
    	<tr><td>
    		<g:textField name="searchId"  size="100"/>
    		<input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
    
     </td></tr>
    </table>
   </g:form>
   <br>
       
    <p>Keyword search will find <b>related terms</b>, e.g. searching for 'tolerate' will also match 'tolerance'.</p><br>
    <p><b>Multiple keywords</b> will search for cases where all words are present, e.g. searching for 'kinase domain' will match only entries that contain both words or their derivatives.</p>
    <!--p>Query text can contain the Boolean operators & (and), | (or) and ! (not), e.g. 'atpase & zinc', '!metal', 'atpase & zinc | !metal'    </p-->
   <br>
   <h2>Sources of data:</h2>
     
	<table>
  	    <tr><td><g:link controller="home" action="publications">${sprintf("%,d\n",badger.Publication.count())} publications</g:link></td><td>The title and abstract of publications matching the names of the species in the database</td></tr>
		<g:each var="res" in="${genes}">	
  			 <tr><td><g:link controller="search" action="species_v" params="${[Sid:res.sid]}">${sprintf("%,d\n",res.g_count)} ${res.genus} ${res.species} genes</g:link> </td><td> Annotation descriptions from the ${res.genus} ${res.species} gene predictions</td></tr>
			</g:each>
  	</td></tr>
  </table>
   </div>
   
  </div>
	<script>
          $(document).ready(function(){
            $('.toHide').hide();
            $("#blk_1").show('slow');
	    $("#sel_1").show('fast');
	    showSelected($("#sel_1").val())
	    
	    $("#contig_attribute").show('slow');
	    
            $("#process").bind("click", function () {
              $("#content").mask("Searching the database...");
            });
				
            $("#cancel").bind("click", function () {
		$("#content").unmask();
            });               
          });
        </script>
</body>
</html>
