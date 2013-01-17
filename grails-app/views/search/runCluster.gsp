<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} cluster</title>
    <parameter name="search" value="selected"></parameter>
    
</head>
<body>
  	 <g:if test = "${align}">
  	 	<h1>Alignment results</h1>
  	 	${align.type}
  	 </g:if>
</body>
</html>
