<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} consortium</title>
  <parameter name="members" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="">Home</g:link> > Members</div>
    <h1>Project members:</h1>
    <table class="table_basic">
    <g:each var="res" in="${memberData}">  
    	<tr>
    		<td><a href = "${memberLoc."${res.value[2]}"[1]}" target='_blank'><img src="${resource(dir: 'images', file: memberLoc."${res.value[2]}"[0])}" height="70"/></a></td>
    		<td><img src="${resource(dir: 'images', file: res.value[3])}" height="70"/></td>
    		<td>${res.value[0]}</td>
    		<td><a href="mailto:${res.value[1]}">${res.value[1]}</a></td>
    	</tr>
    </g:each>
    </table>
  </body>
</html>