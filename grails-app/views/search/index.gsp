<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} search</title>
    <parameter name="search" value="selected"></parameter>
</head>
<body>
<div class="bread">Search</div>
  <h1>Search the <b>${grailsApplication.config.projectID}</b> data:</h1>
  <table>
  	    <tr><td><g:link controller="home" action="publications">${printf("%,d\n",GDB.Publication.count())} publications</g:link></td><td>Publications matching the term <i>${grailsApplication.config.species}</i>.</td></tr>
		<g:if test = "${metaData}" >
			<tr><td><g:link action="species">${metaData.size()} species</g:link></td><td>Search the data associated with each species</tr>
		</g:if>
		
  	
  	  	<tr><td><g:link controller="search" action="all_search">Search all by keyword</g:link></td><td>Search all data within the <i>${grailsApplication.config.species}</i> database by keywords.</td></tr>

  	</td></tr>
  </table>
</body>
</html>
