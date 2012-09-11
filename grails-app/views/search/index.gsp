<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} search</title>
    <parameter name="search" value="selected"></parameter>
</head>
<body>
  <h1>Search the <i>${grailsApplication.config.projectID}</i> data:</h1>
  <table>
  	    <tr><td><g:link controller="home" action="publications">${GDB.Publication.count()} publications</g:link></td><td>Publications matching the term <i>${grailsApplication.config.species}</i>.</td></tr>

  		<g:if test="${grailsApplication.config.annoData.Transcripts == 'y'}">
  			<tr><td><g:link controller="search" action="trans_search">${GDB.TransInfo.count()} transcripts</g:link></td><td>The current transcriptome.</td></tr>
  		</g:if>
  		<g:if test="${grailsApplication.config.annoData.Genes == 'y'}">
  			<tr><td><g:link controller="search" action="gene_search">${GDB.GeneInfo.count()} genes</g:link></td><td>The latest set of genes.</td></tr>
  		</g:if>
  		<g:if test="${grailsApplication.config.seqData.Genome}">
  			<tr><td><g:link controller="search" action="genome_search">${GDB.GenomeInfo.count()} contigs/scaffolds</g:link></td><td>The latest genome.</td></tr>
  		</g:if>
  		
  		<tr><td><g:link controller="search" action="all_search">Search all by keyword</g:link></td><td>Search all data within the <i>${grailsApplication.config.species}</i> database by keywords.</td></tr>

  	</td></tr>
  </table>
</body>
</html>
