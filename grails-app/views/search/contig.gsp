<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search</title>
    <parameter name="search" value="selected"></parameter>
  </head>
  <body>
  <g:if test = "${contigs.size()}">
    <h1>Information for <em>${term}</em></h1>
      <table>
        <tr>
          <td><b>Stats</b></td>
          <td><b>Genes</b></td>
          <td><b>Links</b></td>
          <td><b>Data</b></td>
        </tr>
        <g:each var="contig" in="${contigs}">
          <tr>
            <td>
              Contig length = ${contig.sequence.length()}<br>
              GC = ${contig.gc}<br>
              Estimated coverage = ${contig.coverage}<br>
            </td>
            <td>Number of genes = 0</td>
            <td><a href ="">Browse</a></td>
            <td><g:link controller="FileDownload" action="contig_download" params="${[fileId : fasta, fileName: term+'.txt']}">Contigs</g:link></td>
          </tr>
      </g:each>
      </table>
  </g:if>
  <g:elseif test ="${term == ''}" >
    <h1>Please enter a search term!</h1>
  </g:elseif>
  <g:else>
    <h1>There is no contig with the ID <em>${term}</em>.</h1> 
    <g:link action=''>Search Again</g:link>
  </g:else>  
  </body>
</html>
