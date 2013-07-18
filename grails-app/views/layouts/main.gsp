<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <g:if test = "${grailsApplication.config.headerImage}">
  	  <link rel="shortcut icon" href="${resource(dir: 'images', file: grailsApplication.config.headerImage)}" type="image/x-icon">
   	</g:if>
   	<g:else>
   		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'Badger.png')}" type="image/x-icon">
   	</g:else>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>   
	<link rel="stylesheet" href="${resource(dir: 'js', file: 'intro.js/introjs.css')}" type="text/css"></link>	  
	<script src="${resource(dir: 'js', file: 'intro.js/intro.js')}" type="text/javascript"></script>
	<script>
		function runintroJs() {
			if ('${params.controller}-${params.action}' == 'search-species_search' || '${params.controller}-${params.action}' == 'search-ortho'){
				if ($(show_metrics).is(":visible")) {
					introJs('.introjs-${params.controller}-${params.action}-1').start();
				}else{
					introJs('.introjs-${params.controller}-${params.action}-2').start();
				}
			}else{
            	introJs('.introjs-${params.controller}-${params.action}').start();
            }
		}		
	</script>
	
  <g:layoutHead/>
  <r:layoutResources />

</head>
<body>
<div class="introjs-home-index">
 <div class="header" role="banner">
 	<div class="inline">
 	<g:if test = "${grailsApplication.config.headerImage}">
  	  <g:link controller="home" action="index"><img src="${resource(dir: 'images', file: grailsApplication.config.headerImage)}" style="padding:10px;" align="left" height="80px"/></g:link>
   	</g:if>
   	<g:else>
   		<g:link controller="home" action="index"><img src="${resource(dir: 'images', file: 'Badger.png')}" style="padding:10px;" align="left" height="80px"/></g:link>
   	</g:else>
   	  <br><font size="7">${grailsApplication.config.projectID}</font></font>
   	<g:form controller="search" action="all_searched" style="float:right; position:relative; top:10px" data-intro='Search all the data by keyword' data-step='8'>
    		Quick Search <g:textField name="searchId" size="20"/>
    		<input type="image" src="${resource(dir: 'images', file: 'search.jpg')}" height="25" onclick="submit()" style="border-style: none; right:5px; top:8px; position: relative">
   </g:form>
  </div>
 </div>    	  
  <div class="footer" role="contentinfo">
    <div class="navbar">
	<ul>
	   <li><g:link controller="home" class="${pageProperty(name:'page.home')}">Home</g:link></li>
	   <sec:ifNotLoggedIn>
		
		<!--li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li>
		<li><g:link controller="search" action="trans_search" class="${pageProperty(name:'page.search')}">Search</g:link></li>
		<li><g:link controller="blast" class="${pageProperty(name:'page.blast')}">BLAST</g:link></li>
		<li><g:link controller="home" action="download" class="${pageProperty(name:'page.download')}">Download</g:link></li-->
		
		<!--% //add public internal links to navigation bar -->
		<li data-intro='Search the publications with matches to all the species in the database' data-step='2'><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li> 
		<li data-intro='Search the genome and gene data' data-step='3'><g:link controller="search" class="${pageProperty(name:'page.search')}">Search</g:link>
			<ul>
				<g:if test = "${grailsApplication.config.i.links.species == 'public'}">
			    	<li><g:link controller="search" action="species" class="${pageProperty(name:'page.search')}">Species</g:link></li>
				</g:if>
				<li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li>
				<g:if test = "${grailsApplication.config.i.links.ortho == 'public'}">
					<g:if test = "${grailsApplication.config.o.file}">
			    		<li><g:link controller="search" action="ortho" class="${pageProperty(name:'page.search')}">Orthologs</g:link></li>
			    	</g:if>
				</g:if>
				<g:if test = "${grailsApplication.config.i.links.all == 'public'}">
			    	<li><g:link controller="search" action="all_search" class="${pageProperty(name:'page.search')}">All</g:link></li>
				</g:if>
			</ul>
		</li>
		<g:if test = "${grailsApplication.config.i.links.blast == 'public'}">
			<li data-intro='Run a BLAST analysis' data-step='4'><g:link controller="blast" class="${pageProperty(name:'page.blast')}">BLAST</g:link></li> 
		</g:if>
		<g:if test = "${grailsApplication.config.i.links.members == 'public'}">
			<li><g:link controller="home" action="members" class="${pageProperty(name:'page.members')}">Members</g:link></li> 
		</g:if>
		<g:if test = "${grailsApplication.config.i.links.download == 'public'}">
			<li data-intro='Download data' data-step='5'><g:link controller="home" action="download" class="${pageProperty(name:'page.download')}">Download</g:link></li> 
		</g:if>
		<!--li><g:link controller="home" action="browse" class="${pageProperty(name:'page.browse')}">Browse</g:link></li-->
		<g:if test = "${grailsApplication.config.i.links.stats == 'public'}">
			<!--li><g:link controller="home" action="stats" class="${pageProperty(name:'page.stats')}">Statistics</g:link></li-->
		</g:if>
		
		<% //add public external links to navigation bar 
		if (grailsApplication.config.e.links.pub){
			println "<li><a href=\"#\">Links</a><ul>"
    		 grailsApplication.config.e.links.pub.each {
    			if (it.value.size() >0){
    				def dataSplit = it.value.split(",")
    				println "<li><a href =\"${dataSplit[1].trim()}\" target='_blank'>${dataSplit[0].trim()}</a></li>"
    			}
    		}
    		println "</ul></li>"
    	}
    	%>
		<li data-position='left' data-intro='Log in to access private secions of the site' data-step='7' style="float:right;border-left:1px solid #abbf78;;border-right:0px;"><g:link controller="login">Log in</g:link></li>
    	<li data-position='left' data-intro='This <b>Help</b> button provides page specific information throughout the website.<br><br>It can be controlled with the <b>mouse</b> or via the keyboard using the <b>arrows and escape key</b>.<br><br>Select <b>Next</b> to see the information for this page.' data-step='1' style="float:right;border-left:1px solid #abbf78;;border-right:0px;"><a href="javascript:void(0);" onclick="runintroJs();">Help</a></li>
	   </sec:ifNotLoggedIn>
	   
	   <sec:ifLoggedIn>		
		<li data-intro='Search the publications with matches to all the species in the database' data-step='2'><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link>
		<li data-intro='Search the genome and gene data' data-step='3'><g:link controller="search" class="${pageProperty(name:'page.search')}">Search</g:link>
			<ul>
				<li><g:link controller="search" action="species" class="${pageProperty(name:'page.search')}">Species</g:link></li>
				<li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li>				
				<g:if test = "${grailsApplication.config.o.file}">
			    	<li><g:link controller="search" action="ortho" class="${pageProperty(name:'page.search')}">Orthologs</g:link></li>
			    </g:if>
				<li><g:link controller="search" action="all_search" class="${pageProperty(name:'page.search')}">All</g:link></li>
			</ul>
		</li> 
		<li data-intro='Run a BLAST analysis' data-step='4'><g:link controller="blast" class="${pageProperty(name:'page.blast')}">BLAST</g:link>
		<li data-intro='Download data' data-step='5'><g:link controller="home" action="members" class="${pageProperty(name:'page.members')}">Members</g:link>
		<li><g:link controller="home" action="download" class="${pageProperty(name:'page.download')}">Download</g:link>
		<!--li><g:link controller="home" action="browse" class="${pageProperty(name:'page.browse')}">Browse</g:link></li-->
		<!--li><g:link controller="home" action="stats" class="${pageProperty(name:'page.stats')}">Statistics</g:link></li-->
		<!--li><a href="${grailsApplication.config.g.link}" target='_blank'>Browse </a></li--> 
		
		<% //add public and private external links to navigation bar 
		if (grailsApplication.config.e.links.pub){
			println "<li><a href=\"#\">Links</a><ul>"
    		 grailsApplication.config.e.links.pub.each {
    			if (it.value.size() >0){
    				def dataSplit = it.value.split(",")
    				println "<li><a href =\"${dataSplit[1].trim()}\" target='_blank'>${dataSplit[0].trim()}</a></li>"
    			}
    		}
    		if (grailsApplication.config.e.links.priv){
    			grailsApplication.config.e.links.priv.each {
    				if (it.value.size() >0){
    					def dataSplit = it.value.split(",")
    					println "<li><a href =\"${dataSplit[1].trim()}\" target='_blank'>${dataSplit[0].trim()}</a></li>"
    				}
    			}
    			println "</ul></li>"	
			}else{
				println "</ul></li>"
			}
		}
    	%>
    	
    	<!-- admin only -->
    	<sec:ifAnyGranted roles="ROLE_ADMIN">
    		<li><g:link controller="admin" action="home" class="${pageProperty(name:'page.admin')}">Admin</g:link></li>
    	</sec:ifAnyGranted>
    	
		<li style="float:right;border-left:1px solid #abbf78;;border-right:0px;"><g:link controller="logout">Log out</g:link></li>
    	<li data-position='left' data-intro='This <b>Help</b> button provides page specific information throughout the website.<br><br>It can be controlled with the <b>mouse</b> or via the keyboard using the <b>arrows and escape key</b>.<br><br>Select <b>Next</b> to see the information for this page.' data-step='1' style="float:right;border-left:1px solid #abbf78;;border-right:0px;"><a href="javascript:void(0);" onclick="runintroJs();">Help</a> ${params.controller} ${params.action}</li>
	   </sec:ifLoggedIn>
	   
	</ul>
    </div>
  </div>
<g:layoutBody/>
<div class="footer" role="contentinfo"> 
<p align="right">Contact: <a href="mailto:${grailsApplication.config.contact.email}">${grailsApplication.config.contact.email}</a></p>
</div>
<g:javascript library="application"/>
<r:layoutResources />
</div>
</body>
</html>