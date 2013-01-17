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
				<g:if test="${grailsApplication.mainContext.getResource('images/meta.image_file[0]').exists()}"> 
	    			<a href = "species_search?Gid=${meta.id[0]}"><img src="${resource(dir: 'images', file: meta.image_file[0])}" width="150" style="float:left;"/></a>
	    		</g:if>
	    		<g:else>
	    			<a href = "species_search?Gid=${meta.id[0]}"><img src="${resource(dir: 'images', file: grailsApplication.config.headerImage)}" width="150" style="float:left;"/></a>
	    		</g:else>
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
		<g:if test="${genomes.size == 1}">
		<g:javascript>var genomeId = ${genomes.id[0]}; ${remoteFunction(action:'ajax_gff',update:'gffSelect',params:'\'link=\' + genomeId ')};</g:javascript>
			<tr>
				<td><input type="radio" name="genomeSelect" id="${genomes.id[0]}" value="${genomes.id[0]}" checked onclick="${remoteFunction(action:'ajax_gff',update:'gffSelect',params:'\'link=\' + this.value')};"/></td>
				<td><b>${genomes.file_version[0]}</b></td><td><g:formatDate format="yyyy MMM d" date="${genomes.date_string[0]}"/></td><td></td></td><td>${genomes.description[0]}</td>
			</tr>
			<!--g:remoteFunction action="ajax_gff" update="gffSelect" params="['link':genomes.id[0]]" /-->
		</g:if>
		<g:else>
			<g:each var="g" in="${genomes}">
				<tr>
					<td><input type="radio" name="genomeSelect" id="${g.id}" value="${g.id}" onclick="${remoteFunction(action:'ajax_gff',update:'gffSelect',params:'\'link=\' + this.value')};"/></td>
					<td><label for="${g.id}"><b>${g.gversion}</b></label></td><td><label for="${g.id}"><g:formatDate format="yyyy MMM d" date="${g.date_string}"/></label></td><td><label for="${g.id}">${g.description}</label></td></td>
				</tr>			
			</g:each>
		</g:else>
	</table>
	<div id="gffSelect">
	</div>
</body>
</html>
