<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} genome detail</title>
    <parameter name="search" value="selected"></parameter>
  </head>
  <body>
    <g:if test="${info_results}">
    <h1>Information for <b>${info_results.contig_id[0]}</b>:</h1>
    <table>
      <tr>
        <td><b>Length:</b> ${printf("%,d\n",info_results.sequence[0].length())}</td>
        <td><b>Coverage: </b> ${info_results.coverage[0]}</td>
        <td><b>GC: </b> ${sprintf("%.2f",info_results.gc[0])}</td>
        <td><b>Sequence: </b> 
        <g:form name="fileDownload" url="[controller:'FileDownload', action:'genome_contig_download']" style="display: inline" >
        	<g:hiddenField name="fileId" value="${info_results.contig_id[0]}"/>
			<g:hiddenField name="fileName" value="${info_results.contig_id[0]}"/>
			<a href="#" onclick="document.fileDownload.submit()">Download</a>
			<!--input type="submit" value="Download" class="mybuttons"/-->
		</g:form>
	</td>        
      </tr>
    </table>
    <h1>Browse on the genome <a href="${grailsApplication.config.g.link}?name=${info_results.contig_id[0]}" target='_blank'>(go to genome browser)</a>:</h1>
     <iframe src="${grailsApplication.config.g.link}?name=${info_results.contig_id[0]}" width="100%" height="500">
   		<img src="${grailsApplication.config.g.link}?name=${info_results.contig_id[0]}"/>
	 </iframe>
    </g:if>
    <g:else>
	    <h1>There is no information for <b>${info_results.contig_id[0]}</b></h1>
    </g:else>
  </body>
</html>
