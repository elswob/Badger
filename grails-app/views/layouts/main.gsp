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
    <script type="text/javascript" src="http://code.jquery.com/jquery-latest.pack.js"></script>   

  <g:layoutHead/>
  <r:layoutResources />

</head>
<body>
  <div class="header" role="banner">
      <a href="/home/index"><img src="${resource(dir: 'images', file: grailsApplication.config.headerImage)}" style="padding:10px;" alt="b_anynana" align="left"/></a>
      <font size="6"><br>The <font size="7"><i>${grailsApplication.config.projectID}</i></font> genome project</font>
  </div>
  <div class="footer" role="contentinfo">
    <div class="navbar">
	<ul>
	   <sec:ifNotLoggedIn>
		<li><g:link controller="home" class="${pageProperty(name:'page.home')}">Home</g:link></li>
		<li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li>
		<li><g:link controller="search" action="trans_search" class="${pageProperty(name:'page.search')}">Search</g:link></li>
		<li><g:link controller="blast" class="${pageProperty(name:'page.blast')}">BLAST</g:link></li>
		<li><g:link controller="login" class="${pageProperty(name:'page.login')}">Log in</g:link></li>
	   </sec:ifNotLoggedIn>
	   <sec:ifLoggedIn>
		<li><g:link controller="home" class="${pageProperty(name:'page.home')}">Home</g:link></li>
		<li><g:link controller="home" action="consortium" class="${pageProperty(name:'page.consortium')}">The Consortium</g:link></li>
		<li><g:link controller="home" action="publications" class="${pageProperty(name:'page.publications')}">Publications</g:link></li>
		<li><g:link controller="search" class="${pageProperty(name:'page.search')}">Search</g:link>
			<ul>
				<%-- <li><g:link controller="search" action="gene_search" class="${pageProperty(name:'page.search')}">Genes</g:link></li> --%>
				<li><g:link controller="search" action="trans_search" class="${pageProperty(name:'page.search')}">Transcripts</g:link></li>
				<%-- li><g:link controller="search" action="ncrna_search" class="${pageProperty(name:'page.search')}">ncRNA</g:link></li --%>
				<%-- li><g:link controller="search" action="trans_search" class="${pageProperty(name:'page.search')}">Transposons</g:link></li --%>
			</ul>
		</li>       
		<li><g:link controller="home" class="${pageProperty(name:'page.browse')}">Browse</g:link>
			<ul>
				<li><a href="http://salmo.bio.ed.ac.uk/fgb2/gbrowse/B_anynana_BACs/" target='_blank'>BACs</a></li>
			</ul>
		</li>
		<li><g:link controller="blast" class="${pageProperty(name:'page.blast')}">BLAST</g:link></li>
		<li><g:link controller="home" action="download" class="${pageProperty(name:'page.download')}">Download</g:link></li>
		<%-- <li><g:link controller="home" class="${pageProperty(name:'page.download')}">Download</g:link></li>--%>
		<li><a href ="https://www.wiki.ed.ac.uk/display/BAGW/Home" target='_blank'>Wiki</a></li>
		<li><a href ="https://groups.google.com/group/bicyclus-anynana-genome?hl=en" target='_blank'>Google Group</a></li>
		<li><g:link controller="logout">Log out</g:link></li>
		<!--li style="float:right;border-left:1px solid #abbf78;;border-right:0px;"><g:link controller="logout">Log out</g:link></li-->
	   </sec:ifLoggedIn>
	</ul>
    </div>
  </div>
<g:layoutBody/>
<div class="footer" role="contentinfo"> 
<p align="right">Contact: <a href="mailto:ben.elsworth@ed.ac.uk">ben.elsworth@ed.ac.uk</a></p>
</div>
<g:javascript library="application"/>
<r:layoutResources />
</body>
</html>