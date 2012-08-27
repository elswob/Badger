<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} download</title>
  <parameter name="download" value="selected"></parameter>
  </head>

  <body>
    <table class="table_basic">
    <tr><td><h1>Data</h1></td><td><h1>Description</h1></td><td><h1>Download</h1></td></tr>
    <tr>
    	<td>Genome assembly</td><td>Khmer Velvet k41 draft assembly</td>
    	<td><g:link controller="FileDownload" action="zip_download" params="${[fileName: 'velvet_khmer_k41.fa.zip']}">velvet_khmer_k41.fa.zip</g:link></td></tr>
    </table>
  </body>
</html>