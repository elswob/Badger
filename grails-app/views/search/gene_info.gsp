<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} gene info</title>
    <parameter name="search" value="selected"></parameter>
  </head>
  <body>
  <g:if test="${info_results}">
    <h1>Stats for gene ${info_results.gene_id[0]}:</h1>
    <table>
      <tr>
        <td><b>Scaffold Id</b></td>
        <td><b>Length</b></td>
        <td><b>Introns</b></td>
        <td><b>Source</b></td>
        <td><b>Scaffold start</b></td>
        <td><b>Scaffold stop</b></td>
        <td><b>Download</b></td>
      </tr>
      <tr>
        <td><g:link action="genome_info" params="${[contig_id: info_results.contig_id[0]]}">${info_results.contig_id[0]}</g:link></td>
        <td>${info_results.pep[0].length()}</td>
        <td>${info_results.intron[0]}</td>
        <td>${info_results.source[0]}</td>
        <td>${info_results.start[0]}</td>
        <td>${info_results.stop[0]}</td>
        <td>
        	<div class="inline">
        	<g:form name="nucfileDownload" url="[controller:'FileDownload', action:'gene_download']">
		    	<g:hiddenField name="fileId" value="${info_results.gene_id[0]}"/>
		    	<g:hiddenField name="fileName" value="${info_results.gene_id[0]}"/>
		    	<g:hiddenField name="seq" value="Nucleotides"/>
		    	<a href="#" onclick="document.nucfileDownload.submit()">Nucleotides</a>
		    </g:form> 
		    |
		    <g:form name="pepfileDownload" url="[controller:'FileDownload', action:'gene_download']">
		    	<g:hiddenField name="fileId" value="${info_results.gene_id[0]}"/>
		    	<g:hiddenField name="fileName" value="${info_results.gene_id[0]}"/>
		    	<g:hiddenField name="seq" value="Peptides"/>
		    	<a href="#" onclick="document.pepfileDownload.submit()">Peptides</a>
		    </g:form>
		    </div>
		</td>
      </tr>
    </table> 
    <h1>Browse on the genome <a href="${grailsApplication.config.g.link}?name=${info_results.gene_id[0]}" target='_blank'>(go to genome browser)</a>:</h1>
     <iframe src="${grailsApplication.config.g.link}?name=${info_results.gene_id[0]}" width="100%" height="500">
   		<img src="${grailsApplication.config.g.link}?name=${info_results.gene_id[0]}"/>
	 </iframe>
    <h1>Top annotations for gene ${info_results.gene_id[0]}:</h1>
    <table>
      <tr>
        <td><b>Database</b></td>
        <td><b>Hit ID</b></td>
        <td><b>Description</b></td>
        <td><b>Start</b></td>
        <td><b>Stop</b></td>
        <td><b>Score</b></td>
      </tr>
     <% def anno_check = [:] %>
     <g:each var="res" in="${anno_results}">
     <g:unless test = "${anno_check[res.anno_db]}">
      <tr>  
        <td>${res.anno_db}</td>
        <%
        res.anno_id = res.anno_id.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>") 
        res.descr = res.descr.replaceAll(/\|([A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9]*[A-Z0-9])\|/, "<a href=\"http://www.ncbi.nlm.nih.gov/protein/\$1\" target=\'_blank\'>|\$1|</a>") 
        res.anno_id = res.anno_id.replaceAll(/lcl\|(.*)/, "<a href=\"http://www.uniprot.org/uniref/\$1\" target=\'_blank\'>\$1</a>") 
        %>
        <td>${res.anno_id}</td>
        <td>${res.descr}</td>
        <td>${res.anno_start}</td>
        <td>${res.anno_stop}</td>
        <td>${res.score}</td>
      </tr>  
      </g:unless>
      <%
      	//just get the first one for each annotation
      	def check_id = res.anno_db
      	anno_check[check_id] = "yes"
      %>
     </g:each>
    </table>
  </g:if>
  <g:else>
    <h1>The gene Id has no information</h1>
  </g:else>
  </body>
</html>
