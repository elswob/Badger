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
    <link rel="shortcut icon" href="${resource(dir: 'images', file: grailsApplication.config.headerImage)}" type="image/x-icon">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>   

  <g:layoutHead/>
  <r:layoutResources />

</head>
<body>
 <div class="header" role="banner">
  	  <a href="/"><img src="${resource(dir: 'images', file: grailsApplication.config.headerImage)}" style="padding:10px;" align="left" height="100px"/></a>
   	  <font size="6"><br><font size="7"><i>${grailsApplication.config.projectID}</i></font></font>
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
		<li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li> 
		<li><g:link controller="search" class="${pageProperty(name:'page.search')}">Search</g:link>
			<ul>
				<li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li>
				<g:if test = "${grailsApplication.config.seqData.Transcriptome}">
					<g:if test = "${grailsApplication.config.i.links.trans == 'public'}">
			    		<li><g:link controller="search" action="trans_search" class="${pageProperty(name:'page.search')}">Transcripts</g:link></li>
					</g:if>
				</g:if>
				<g:if test = "${grailsApplication.config.seqData.GenePep}">
					<g:if test = "${grailsApplication.config.i.links.genes == 'public'}">
						<li><g:link controller="search" action="gene_search" class="${pageProperty(name:'page.search')}">Genes</g:link></li>
					</g:if>
				</g:if>
				<g:if test = "${grailsApplication.config.seqData.Genome}">
					<g:if test = "${grailsApplication.config.i.links.genome == 'public'}">
						<li><g:link controller="search" action="genome_search" class="${pageProperty(name:'page.search')}">Genome</g:link></li>
					</g:if>
				</g:if>
				<g:if test = "${grailsApplication.config.i.links.all == 'public'}">
			    	<li><g:link controller="search" action="all_search" class="${pageProperty(name:'page.search')}">All</g:link></li>
				</g:if>
			</ul>
		</li> 
		<g:if test = "${grailsApplication.config.i.links.blast == 'public'}">
			<li><g:link controller="blast" class="${pageProperty(name:'page.blast')}">BLAST</g:link></li> 
		</g:if>
		<g:if test = "${grailsApplication.config.i.links.members == 'public'}">
			<li><g:link controller="home" action="members" class="${pageProperty(name:'page.members')}">Members</g:link></li> 
		</g:if>
		<g:if test = "${grailsApplication.config.i.links.download == 'public'}">
			<li><g:link controller="home" action="download" class="${pageProperty(name:'page.download')}">Download</g:link></li> 
		</g:if>
		<g:if test = "${grailsApplication.config.i.links.browse == 'public'}">
			<g:if test ="${grailsApplication.config.g.link}">
				<li><g:link controller="home" action="browse" class="${pageProperty(name:'page.browse')}">Browse</g:link></li>
			</g:if>
		</g:if>
		<g:if test = "${grailsApplication.config.i.links.stats == 'public'}">
			<li><g:link controller="home" action="stats" class="${pageProperty(name:'page.stats')}">Statistics</g:link></li>
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
		<li style="float:right;border-left:1px solid #abbf78;;border-right:0px;"><g:link controller="login">Log in</g:link></li>
    	
	   </sec:ifNotLoggedIn>
	   
	   <sec:ifLoggedIn>		
		<li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link>
		<li><g:link controller="search" class="${pageProperty(name:'page.search')}">Search</g:link>
			<ul>
				<li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li>
			    <g:if test = "${grailsApplication.config.seqData.Transcriptome}">
			    	<li><g:link controller="search" action="trans_search" class="${pageProperty(name:'page.search')}">Transcripts</g:link></li>
			    </g:if>
			    <g:if test = "${grailsApplication.config.seqData.GenePep}">
				    <li><g:link controller="search" action="gene_search" class="${pageProperty(name:'page.search')}">Genes</g:link></li>
				</g:if>
				<g:if test = "${grailsApplication.config.seqData.Genome}">
					<li><g:link controller="search" action="genome_search" class="${pageProperty(name:'page.search')}">Genome</g:link></li>
				</g:if>
				<li><g:link controller="search" action="all_search" class="${pageProperty(name:'page.search')}">All</g:link></li>
			</ul>
		</li> 
		<li><g:link controller="blast" class="${pageProperty(name:'page.blast')}">BLAST</g:link>
		<li><g:link controller="home" action="members" class="${pageProperty(name:'page.members')}">Members</g:link>
		<li><g:link controller="home" action="download" class="${pageProperty(name:'page.download')}">Download</g:link>
		<g:if test = "${grailsApplication.config.g.link}">
			<li><g:link controller="home" action="browse" class="${pageProperty(name:'page.browse')}">Browse</g:link></li>
		</g:if>
		<li><g:link controller="home" action="stats" class="${pageProperty(name:'page.stats')}">Statistics</g:link></li>
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
    	
    	<!--li><g:link controller="logout">Log out</g:link></li-->
		<li style="float:right;border-left:1px solid #abbf78;;border-right:0px;"><g:link controller="logout">Log out</g:link></li>
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
</body>
</html>