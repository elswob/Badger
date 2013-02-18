<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} orthologs</title>
    <parameter name="search" value="selected"></parameter>
    <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
    <script src="${resource(dir: 'js', file: 'jqplot/jquery.min.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>
	
</head>
<body>
<g:link action="">Search</g:link> > Search orthologs
     <h1>Search the clusters:</h1>
 	 <table>
 	 <tr><td><b>Species</b></td><td><b>File</b></td><td><b># clusters</b></td><td><b>Total seqs</b></td><td><b># seqs in clusters</b></td><td><b># singletons</b></td></tr>
 	  	<g:each var="res" in="${o}">
 	 		<tr><td><i>${res.genus} ${res.species}</i></td><td>${res.file_name}</td><td>${sprintf("%,d\n",res.count_ortho)}</td><td>${sprintf("%,d\n",gmap."${res.file_name}")}</td><td>${sprintf("%,d\n",res.count_all)}</td><td>${sprintf("%,d\n",gmap."${res.file_name}" - res.count_all)}</td></tr>
 	 	</g:each>
 	 <tr><td>Total</td><td>n/a</td><td>${sprintf("%,d\n",n.max)}</td><td>${sprintf("%,d\n",badger.Ortho.count())}</td></tr>
 	 </table>
</body>
</html>
