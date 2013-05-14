<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name='layout' content='main'/>
    <parameter name="blast" value="selected"></parameter>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${grailsApplication.config.projectID} | BLAST error</title>
  </head>
  <body>
  <br>
    <g:if test="${params.error == 'no_header'}">
    	<h2>Your sequence appeared to have no header. Please use a correctly formatted FASTA file, with a header and sequence, as shown below and <g:link action=''>try again</g:link> </h2>
    	<br>
    	>fasta_header<br>
    	ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT
    	ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT
    	ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT
    	ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT
    	</div>
    </g:if>
    <g:if test="${params.error == 'no_seq'}">
    	<h2>Please enter a FASTA file with at least 10 bases in the format shown below and <g:link action=''>try again</g:link> </h2>
    	<br>
    	>fasta_header<br>
    	ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT
    	ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT
    	ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT
    	ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT
    </g:if>
  </body>
</html>
