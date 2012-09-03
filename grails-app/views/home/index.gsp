<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	  <meta name='layout' content='main'/>
	  <title>${grailsApplication.config.projectID} home</title>
	  <parameter name="home" value="selected"></parameter>
  </head>

  <body>
	  <sec:ifNotLoggedIn>
	  	<h1>Welcome to the home of the <i>${grailsApplication.config.projectID}</i> genome project.</h1>    
	  </sec:ifNotLoggedIn>	
	  <sec:ifLoggedIn>
	  <h1>Welcome <sec:username /></h1>
	    <p>
	    This site represents the main portal to the <i>${grailsApplication.config.projectID}</i> genome project. All assembly and annotation data 
	    will be accessible from here via the links above. </p><br>
	  <div class="inline">  
	  	<h1>Latest News</h1>
	  	<sec:ifAnyGranted roles="ROLE_ADMIN">
	  		<g:form action="addNews" controller="home">
	  			<a href="#" onclick="parentNode.submit()">(add news)</a>
	  		</g:form>
	  		<br>
	  	</sec:ifAnyGranted>
	  </div>
	  <table>
	  
	  <g:each var="res" in="${newsData}">
	  <tr><td>
	  <div class="inline">
	    <b><g:formatDate format="yyyy MMM d" date="${res.dateString}"/></b>
	    <sec:ifAnyGranted roles="ROLE_ADMIN">	    
	    	<g:form action="editNews" controller="home" params="[titleString: res.titleString]" >
	  			(<a href="#" onclick="parentNode.submit()">edit</a> /
	  		</g:form>  	
	  		<g:form action="deleteNews" controller="home" params="[titleString: res.titleString]" >
	  			<a href="#" onclick="parentNode.submit()">delete</a>)
	  		</g:form> 
	  		<br>
	    </sec:ifAnyGranted>
	    </div>
	    <br>
	    <g:link action="news" params="${[newsTitle : res.titleString]}">${res.titleString} </g:link>
	  </td></tr>
	  </g:each>
	  </table>
	  
	
	</sec:ifLoggedIn>
  </body>
</html>
