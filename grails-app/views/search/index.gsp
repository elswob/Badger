<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} | Search</title>
    <parameter name="search" value="selected"></parameter>
</head>
<body>
<div class="introjs-search-index">
Search
  <h1>Search the <b>${grailsApplication.config.projectID}</b> data:</h1>
  <table>
  	    <tr data-intro='Search all the publications in the database' data-step='1'><td><g:link controller="home" action="publications">${sprintf("%,d\n",pub.count)} publications</g:link></td><td>Publications matching the names of the species in the database.</td></tr>
		<g:if test = "${metaData}" >
			<tr data-intro='Search all the genome and gene data' data-step='2'><td><g:link action="species">${metaData.size()} species</g:link></td><td>Search the data associated with each species.</tr>
		</g:if>
		<g:if test = "${grailsApplication.config.o.file}">
			<sec:ifNotLoggedIn>
				<g:if test = "${grailsApplication.config.i.links.ortho == 'public'}">
					<tr data-intro='Search all the ortholog data' data-step='3'><td><g:link controller="search" action="ortho">${sprintf("%,d\n",orth.max)} ortholog groups</g:link></td><td>Search data from the OrthoMCL analysis</td></tr>
				</g:if>
			</sec:ifNotLoggedIn>
			<sec:ifLoggedIn>
				<tr data-intro='Search all the ortholog data' data-step='3'><td><g:link controller="search" action="ortho">${sprintf("%,d\n",orth.max)} ortholog groups</g:link></td><td>Search data from the OrthoMCL analysis</td></tr>
			</sec:ifLoggedIn>
		</g:if>
  	  	<tr data-intro='Search all data by keyword' data-step='4'><td><g:link controller="search" action="all_search">Search all by keyword</g:link></td><td>Search all data within the <i>${grailsApplication.config.species}</i> database by keywords.</td></tr>

  	</td></tr>
  </table>
</div>
</body>
</html>
