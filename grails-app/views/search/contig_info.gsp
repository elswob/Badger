<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} contig detail</title>
    <parameter name="search" value="selected"></parameter>
  </head>
  <body>
    <g:if test="${info_results}">
    <h1>Information for Contig <b>${info_results.contig_id[0]}</b>:</h1>
    <table>
      <tr>
        <td><b>Length:</b> ${printf("%,d\n",info_results.sequence[0].length())}</td>
        <!--td><b>Coverage: </b> ${info_results.coverage[0]}</td-->
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
    </g:if>
    <g:else>
	    <h1>There is no information for the contig <b>${info_results.contig_id[0]}</b></h1>
    </g:else>
  </body>
</html>
