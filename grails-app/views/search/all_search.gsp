<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} transcript search</title>
    <parameter name="search" value="selected"></parameter>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
    <script type="text/javascript"> 
    	$(window).unload(function() {});
    </script>
    <script type="text/javascript">

    function showSelected(val){
		document.getElementById
		('selectedResult').innerHTML = val;
    }
    $(function() {
		$("[name=toggler]").click(function(){
				$('.toHide').hide();
				$("#blk_"+$(this).val()).show('slow');
				$("#sel_"+$(this).val()).show('fast');
				showSelected($("#sel_"+$(this).val()).val())
		});
    });

    </script>
	
</head>
<body>
  <div id="content">
     <h1>Search all the data by keyword:</h1>
	<table>
  	    <tr><td><g:link controller="home" action="publications">${printf("%,d\n",GDB.Publication.count())} publications</g:link></td><td>The title and abstract of publications matching the term <i>${grailsApplication.config.species}</i>.</td></tr>
		<g:if test="${grailsApplication.config.annoData.Transcripts == 'y'}">
			<g:if test = "${grailsApplication.config.i.links.trans == 'public'}">			
				<tr><td><g:link controller="search" action="trans_search">${printf("%,d\n",GDB.TransInfo.count())} transcripts</g:link></td><td>The description of the current transcriptome annotations.</td></tr>
			</g:if>
			<g:else>
				<sec:ifLoggedIn>
					<tr><td><g:link controller="search" action="trans_search">${printf("%,d\n",GDB.TransInfo.count())} transcripts</g:link></td><td>The description of the current transcriptome annotations.</td></tr>
				</sec:ifLoggedIn>
			</g:else>	
		</g:if>
		
  		<g:if test="${grailsApplication.config.annoData.Genes == 'y'}">
  			<g:if test = "${grailsApplication.config.i.links.genes == 'public'}">
  				<tr><td><g:link controller="search" action="gene_search">${printf("%,d\n",GDB.GeneInfo.count())} genes</g:link></td><td>The descriptions from the latest set of genes annotations.</td></tr>
  			</g:if>
  			<g:else>
  				<sec:ifLoggedIn>
  	  				<tr><td><g:link controller="search" action="gene_search">${printf("%,d\n",GDB.GeneInfo.count())} genes</g:link></td><td>The descriptions from the latest set of genes annotations.</td></tr>
				</sec:ifLoggedIn>
			</g:else>
		</g:if>
  	</td></tr>
  </table>
	<br>
    <p>Keyword search will find related terms, for example searching for 'tolerate' will also match 'tolerance'.</p><br>
    <p>Query text can contain the Boolean operators & (and), | (or) and ! (not), e.g. 'atpase & zinc', '!metal', 'atpase & zinc | !metal'    </p>
    <g:form action="all_searched">
    <table>
    	<tr><td>
    		<g:textField name="searchId"  size="100"/>
    		<input class="mybuttons" type="button" value="Search" id="process" onclick="submit()" >
    </g:form>
     </td>
  </tr>
   </table>
   <br>
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
