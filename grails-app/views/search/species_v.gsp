<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} versions</title>
    <parameter name="search" value="selected"></parameter>   
    <g:javascript library="prototype" />
</head>
<body>
	<g:link action="">Search</g:link> > <g:link action="species">Species</g:link> > ${meta.genus[0]} ${meta.species[0]}
	 	
	<table>
		<tr>
			<td width=150> 
				<a href = "species_search?Gid=${meta.id[0]}"><img src="${resource(dir: 'images', file: meta.image_file[0])}" width="150" style="float:left;"/></a>
			</td>
			<td>
				<div style="overflow:auto; padding-right:2px; height:150px">
					<p>${meta.description[0]}</p>
					<br><font size="1">Picture supplied by ${meta.image_source[0]}</font>
				</div>
			</td>
		</tr>
	</table>
	<h1>Genome versions</h1>
	<table>
		<g:each var="g" in="${genomes}">
			<tr>
				<td><input type="radio" name="genomeSelect" id="${g.gversion}" value="${g.id}" onclick="${remoteFunction(action:'get_gff',update:'gffDiv',params:'\'link=\' + this.value')};"/></td>
				<td><label for="${g.gversion}"><b>${g.gversion}</b></label></td><td><label for="${g.gversion}">${g.description}</label></td></td><td><label for="${g.gversion}"><g:formatDate format="yyyy MMM d" date="${g.date_string}"/></label></td>
			</tr>			
		</g:each>
	</table>
	<g:select name="gffDiv" id ="gffDiv" noSelection="['':'Select one...']" from=""></g:select>
</body>
</html>
