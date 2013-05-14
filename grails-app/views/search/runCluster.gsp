<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} | Ortholog cluster</title>
    <parameter name="search" value="selected"></parameter>
    
</head>
<body>
<g:link action="">Search</g:link> > <g:link action="ortho">Search orthologs</g:link> > <g:link action="cluster" params="${[group_id:params.group_id]}">Cluster</g:link> > Alignment
  	 <g:if test = "${align}">
  	 	<h1>Alignment results</h1>
  	 	<div class="inline">
  	 	  Download: 
		  <g:form name="alnDownload" url="[controller:'FileDownload', action:'align_download']">
				<g:hiddenField name="fileId" value="${align.OutFile}"/>
				<g:hiddenField name="fileName" value="${align.name}.aln"/>
				<g:hiddenField name="type" value="aln"/>
				<a href="#" onclick="document.alnDownload.submit()">Clustal</a>
		  </g:form>
		  |
		  <g:form name="htmlDownload" url="[controller:'FileDownload', action:'align_download']">
				<g:hiddenField name="fileId" value="${align.OutFile}"/>
				<g:hiddenField name="fileName" value="${align.name}.html"/>
				<g:hiddenField name="type" value="html"/>
				<a href="#" onclick="document.htmlDownload.submit()"> HTML</a>
		  </g:form>
		</div>
		<br>
  	 	<!--div class="align_res"-->
	  	 	${align.htmlOut}<br>
	  	<!--/div-->
  	 </g:if>
</body>
</html>
