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
    <h1>Project members:</h1>
    <table class="table_basic">
    <g:each var="loc" in="${memberLoc}">  
    	<tr><td style="text-align: center;"><a href = "${loc.value[1]}" target='_blank'><img src="${resource(dir: 'images', file: loc.value[0])}" height="60"/></a></td>
    	<td><table>
    		<g:each var="mem" in="${memberData}">
    			<g:if test="${mem.value[2] == loc.key}"> 
    				<tr>
    					<td>${mem.value[0]}</td>
    					<td><a href="mailto:${mem.value[1]}">${mem.value[1]}</a></td>
    				</tr>
    			</g:if>
    		</g:each>
    		</table></td>
    	</tr>
    </g:each>
    </table>
  </body>
</html>