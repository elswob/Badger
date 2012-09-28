<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} browse</title>
  <parameter name="browse" value="selected"></parameter>
  </head>
  <body>
	  <g:if test = "${grailsApplication.config.g.link}"> 
	  	<g:if test="${params.start && params.stop && params.contig_id}">
	  		<h1>Browse the genome <a href="${grailsApplication.config.g.link}?name=${params.contig_id}:${params.start}..${params.stop}" target='_blank'>(go to genome browser)</a>:</h1>
		 	<iframe src="${grailsApplication.config.g.link}?name=${params.contig_id}:${params.start}..${params.stop}" width="100%" height="1000" frameborder="0">
				<img src="${grailsApplication.config.g.link}?name=${params.contig_id}:${params.start}..${params.stop}"/>
		 	</iframe>
		 </g:if>
		 <g:else>
		 	<h1>Browse the genome <a href="${grailsApplication.config.g.link}" target='_blank'>(go to genome browser)</a>:</h1>
		 	<iframe src="${grailsApplication.config.g.link}" width="100%" height="1000" frameborder="0">
				<img src="${grailsApplication.config.g.link}"/>
		 	</iframe>
		 </g:else>
      </g:if>
  </body>
</html>