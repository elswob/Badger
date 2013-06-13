<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} | Members</title>
  <parameter name="members" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="">Home</g:link> > Members</div>
    <!--h1>Project members:</h1-->
    <br>
    <table class="members">
    <g:each var="loc" in="${memberLoc}">  
    	<tr><td colspan=2><hr size=1 color="green" width="100%"></td></tr>
    	<tr><td>
    	<table>
    		<g:each var="mem" in="${memberData}">
    			<g:if test="${mem.value[2] == loc.key}"> 
    				<tr>
    					<td width="150px">${mem.value[0]}</td>
    					<td width="200px"><a href="mailto:${mem.value[1]}">${mem.value[1]}</a></td>
    				</tr>
    			</g:if>
    		</g:each>
    		</table></td>
    		<td><a href = "${loc.value[1]}" target='_blank'><img src="${resource(dir: 'images', file: loc.value[0])}" height="50"/></a></td>
    	</tr>
    </g:each>
    </table>
  </body>
</html>