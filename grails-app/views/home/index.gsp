<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	  <meta name='layout' content='main'/>
	  <title>${grailsApplication.config.projectID} | Home</title>
	  <parameter name="home" value="selected"></parameter>
	  <ckeditor:resources/>
  </head>

  <body>
  	  <sec:ifAnyGranted roles="ROLE_ADMIN">
  	  <div class="inline">
  	  Edit page:
  	  <form name="editCheck" value="yes" method="post">
      		<input type="hidden" name="edit" value="y">
      		<a href="#" onclick="parentNode.submit()">modify </a>|
      </form>
      <g:form name="editPage" controller="Home" action="editPage" params="${[reset: 'y',page: '/home/index']}">
      		<input type="hidden" name="edit" value="y">
      		<a href="#" onclick="parentNode.submit()">reset </a>
      </g:form>
      </div>
      
	  <g:if test="${params.edit == 'y'}">
		<div class="sidebar">  
			<div class="inline">  
				<h1>Latest News</h1>
				<sec:ifAnyGranted roles="ROLE_ADMIN">
					<g:form action="addNews" controller="admin">
						(<a href="#" onclick="parentNode.submit()">add news item</a>)
					</g:form><br>
				</sec:ifAnyGranted>
			</div>			  
			<table>	  
			<g:each var="res" in="${newsData}">
				<tr>
					<td>
						<div class="inline">
							<sec:ifAnyGranted roles="ROLE_ADMIN">	    
								<g:form action="editNews" controller="admin" params="[titleString: res.titleString]" >
									<a href="#" onclick="parentNode.submit()" title="Edit news item"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
								</g:form>  	
								<g:form action="deleteNews" controller="admin" params="[titleString: res.titleString]" >
									<a href="#" onclick="parentNode.submit()" title="Delete news item"><img src="${resource(dir: 'images', file: 'delete-icon.png')}" width="15px"/></a>
								</g:form> 
							</sec:ifAnyGranted>
						<b><g:formatDate format="d MMM yyyy" date="${res.dateString}"/>:</b>
					</td>
					<td>
						<g:link action="news" params="${[newsTitle : res.titleString]}">${res.titleString}</g:link>
					</div>
					</td>
				</tr>
			</g:each>
			</table>
		</div>
		<div>
		<g:form name="editPage" url="[controller:'Home', action:'editPage']">
			<ckeditor:editor name="edits" height="500px" width="100%">
				<g:if test="${edits}">
					${edits.edit[0]}
				</g:if>
				<g:else>
					<h1>Welcome to the home of <b>${grailsApplication.config.projectID}</b></h1>	
					<table><tr><td>
						<g:if test = "${grailsApplication.config.headerImage}">
							<img src="${resource(dir: 'images', file: grailsApplication.config.mainImage)}" height="300px"/>
   						</g:if>
   						<g:else>
   							<img src="${resource(dir: 'images', file: 'badgeryellereye_white.png')}" height="300px"/>
					   	</g:else>						
					   	<g:if test = "${grailsApplication.config.mainImageSource}">
							<br><font size="1">Picture: ${grailsApplication.config.mainImageSource}</font>
						</g:if>
						</td><td>
						This is the home page for your project. If you are the admin user you can login and edit this page using the built in editor, and add items to the news section on the right.
					</td></tr></table>
				</g:else>
			</ckeditor:editor>
			<input type = "hidden" name="pageName" value="/home/index">
		</g:form>
		</div>
	  </g:if>
	  </sec:ifAnyGranted>
	  
      <g:if test="${params.edit != 'y'}">
		<div class="sidebar">  
			<div class="inline">  
				<h1>Latest News</h1>
				<sec:ifAnyGranted roles="ROLE_ADMIN">
					<g:form action="addNews" controller="admin">
						(<a href="#" onclick="parentNode.submit()">add news item</a>)
					</g:form><br>
				</sec:ifAnyGranted>
			</div>			  
			<table>	  
			<g:each var="res" in="${newsData}">
			
				<tr>
					<td>
						<div class="inline">
							<sec:ifAnyGranted roles="ROLE_ADMIN">	    
								<g:form action="editNews" controller="admin" params="[titleString: res.titleString]" >
									<a href="#" onclick="parentNode.submit()" title="Edit news item"><img src="${resource(dir: 'images', file: 'edit-icon.png')}" width="15px"/></a>
								</g:form>  	
								<g:form action="deleteNews" controller="admin" params="[titleString: res.titleString]" >
									<a href="#" onclick="parentNode.submit()" title="Delete news item"><img src="${resource(dir: 'images', file: 'delete-icon.png')}" width="15px"/></a>
								</g:form> 
							</sec:ifAnyGranted>
						<b><g:formatDate format="d MMM yyyy" date="${res.dateString}"/>:</b>

					</td>
					<td>
						<g:link action="news" params="${[newsTitle : res.titleString]}">${res.titleString}</g:link>
					</div>
					</td>
				</tr>
			</g:each>
			</table>
		</div>
		<g:if test="${edits}">
			${edits.edit[0]}
		</g:if>
		<g:else>
			<h1>Welcome to the home of <b>${grailsApplication.config.projectID}</b></h1>	
			<table><tr><td>
				<g:if test = "${grailsApplication.config.headerImage}">
					<img src="${resource(dir: 'images', file: grailsApplication.config.mainImage)}" height="300px"/>
				</g:if>
				<g:else>
					<img src="${resource(dir: 'images', file: 'badgeryellereye_white.png')}" height="300px"/>
				</g:else>	
				<g:if test = "${grailsApplication.config.mainImageSource}">
					<br><font size="1">Picture: ${grailsApplication.config.mainImageSource}</font>
				</g:if>
				</td><td><br><br>
				This is the home page for your project. If you are the admin user you can login and edit this page using the built in editor, and add items to the news section on the right.
			</td></tr></table>
		</g:else>
	 </g:if>
  </body>
</html>
