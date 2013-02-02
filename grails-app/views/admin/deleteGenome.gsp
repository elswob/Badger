<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} admin</title>
  <parameter name="admin" value="selected"></parameter>
  </head>

  <body>
  <div class="bread"><g:link action="home">Admin</g:link> > <g:link action="home">Home</g:link> > Delete genome</div>
   
<h1>Delete genome:</h1>

<g:form action="deletedGenome" controller="admin">

<h2>Are you sure you wish to delete the following genome? This will delete all associated files!</h2>

<table>
	<tr>
		<td width="45%"><b>Date (leave blank for today's date)</b><font color="red">*</font><br>
			<g:textField value="${formatDate(format:'dd/MM/yyyy',date:genome.dateString)}" name="genome_date" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
		</td>
		<td width="50%"><b>Version (a unique ID for the genome)</b><font color="red">*</font><br>
			<g:textField value="${genome.gversion}" name="genome_version" style="width:100%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/>
		</td>
	</tr>
</table>	
<p><b>URL of Gbrowse2 instance</b></p>
<g:textField value="${genome.gbrowse}" name="gbrowse" style="width: 98%; height: 18px; border: 3px solid #cccccc; padding: 2px;"/><br><br>
<br>
<input type = "hidden" name="gid" value="${genome.id}">
<input class="mybuttons" type="button" value="Delete genome" onclick="submit()" >
<hr size = 5 color="green" width="100%" style="margin-top:10px"><br>

</g:form>
</body>
</html>
