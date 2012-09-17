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
		 <h1>Browse the genome <a href="${grailsApplication.config.g.link}" target='_blank'>(go to genome browser)</a>:</h1>
		 <iframe src="${grailsApplication.config.g.link}" width="100%" height="1000" frameborder="0">
			<img src="${grailsApplication.config.g.link}"/>
		 </iframe>
      </g:if>
  </body>
</html>