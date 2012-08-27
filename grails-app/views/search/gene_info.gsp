<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gene Info</title>
    <parameter name="search" value="selected"></parameter>
  </head>
  <body>
  <g:if test="${info_results}">
    <h1>Stats for gene ${info_results.gene_id[0]}:</h1>
    <table>
      <tr>
        <td><b>Scaffold Id</b></td>
        <td><b>Length</b></td>
        <td><b>Coverage</b></td>
        <td><b>Introns</b></td>
        <td><b>Source</b></td>
        <td><b>Scaffold start</b></td>
        <td><b>Scaffold stop</b></td>
        <td><b>Nucleotide sequence</b></td>
        <td><b>Peptide sequence</b></td>
      </tr>
      <tr>
        <td><g:link action="search_results" params="${[search: 'contig', searchId: info_results.contig_id[0]]}">${info_results.contig_id[0]}</g:link></td>
        <td>${info_results.pep[0].length()}</td>
        <td>${info_results.coverage[0]}</td>
        <td>${info_results.intron[0]}</td>
        <td>${info_results.source[0]}</td>
        <td>${info_results.start[0]}</td>
        <td>${info_results.stop[0]}</td>
        <td><g:link controller="FileDownload" action="contig_download" params="${[fileId : nuc_fasta, fileName: info_results.gene_id[0]+'.fa']}">Download</g:link></td>
        <td><g:link controller="FileDownload" action="contig_download" params="${[fileId : pep_fasta, fileName: info_results.gene_id[0]+'.aa']}">Download</g:link></td>
      </tr>
    </table> 
    <div class="midtable">
    <h1>Browse on the genome: <a href ="">GBrowse2</a></h1>
    </div>
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
