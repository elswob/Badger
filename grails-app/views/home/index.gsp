<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	  <meta name='layout' content='main'/>
	  <title>${grailsApplication.config.projectID} home</title>
	  <parameter name="home" value="selected"></parameter>
  </head>

  <body>
	  <sec:ifNotLoggedIn>
	  <h1>Welcome to the home of the <i>Bicyclus anynana</i> genome project.</h1> 
            <p>We are sequencing the genome of the Squinting Bush Brown butterfly <i>Bicyclus anynana</i> as a joint project between the following institutions. </p>
            <br>
            <p>
            <a href = "http://www.ed.ac.uk" target='_blank'><img src="${resource(dir: 'images', file: 'homecrest.gif')}" height="70" width="70" style="margin: 0px 10px" /></a> 
            <a href = "http://genepool.bio.ed.ac.uk/" target='_blank'><img src="${resource(dir: 'images', file: 'gp.jpg')}" height="70" width="135" style="margin: 0px 10px"/></a> 
            <a href = "http://www.cam.ac.uk/" target='_blank'><img src="${resource(dir: 'images', file: 'University-of-Cambridg.png')}" height="70" width="270" style="margin: 0px 10px"/></a>   
            <a href = "http://www.yale.edu/" target='_blank'><img src="${resource(dir: 'images', file: 'yale.jpg')}" height="70" width="250" style="margin: 0px 10px"/></a>
            <a href = "http://www.igc.gulbenkian.pt/" target='_blank'><img src="${resource(dir: 'images', file: 'logoigc50.jpg')}" height="70" width="350" style="margin: 0px 10px"/></a>
            <a href="http://www.wur.nl/UK/" target='_blank'><img src="${resource(dir: 'images', file: 'UNIVERSITY_web-rgb.gif')}" height="70" width="350" style="margin: 0px 10px"/></a>
            <a href="http://www.uni-greifswald.de/?L=1" target='_blank'><img src="${resource(dir: 'images', file: 'siegel_testheader.gif')}" height="70" width="350" style="margin: 0px 10px"/></a>
            <a href="http://www.liv.ac.uk/" target='_blank'><img src="${resource(dir: 'images', file: 'UniLiv_logo.jpg')}" height="70" width="350" style="margin: 0px 10px"/></a>
            <a href="http://www.uci.edu/" target='_blank'><img src="${resource(dir: 'images', file: 'uci_textbanner.png')}" height="70" width="350" style="margin: 0px 10px"/></a>
            </p>
            <br>
            <h1>Data access</h1>
            <p>Currently, only the UniGene contigs assembled from the public ESTs are available to search using the Search and BLAST buttons above.</p>
	    <p>If you are a collaborator on the project please <g:link controller="login">login</g:link> to access the full site. 
	    If you have forgotten or not yet recieved your login details please get in contact.</p>

	</sec:ifNotLoggedIn>


	
	  <sec:ifLoggedIn>
	  <h1>Welcome <sec:username /></h1>
	    <p>
	    This site represents the main portal to the <i>Bicyclus anynana</i> genome project. All assembly and annotation data 
	    will be accessible from here via the links above. From here you can also access the 
	    <a href ="https://www.wiki.ed.ac.uk/display/BAGW/Home" target='_blank'>genome project wiki</a> where collaborators can add information about the project, 
	    and a <a href ="https://groups.google.com/group/bicyclus-anynana-genome?hl=en" target='_blank'>google group</a> 
	    which you should all have had an invite to (let me know if you haven't) which has the following 
	    email address: 
	    <a href="mailto:bicyclus-anynana-genome@googlegroups.com">bicyclus-anynana-genome@googlegroups.com</a>
	    </p>
	    <br>
	    <div style="overflow:auto; padding-right:2px; height:380px">
	    <table>
	    	<tr>
	    	<td>
		    <img src="${resource(dir: 'images', file: 'wet-dry.jpg')}" alt="b_anynana" height="304" width="600" style="padding:10px; float:left;margin:0 5px 0 0;"/>
		    <br>
		    <font size="1">Picture supplied by Antónia Monteiro</font>
		</td>
		<td>
		   <h1>Latest blog posts:</h1>
		   <p><b><g:link controller="home" action="blog" fragment="15/08/12">15/08/12</g:link></b> Publication data now auto updates and is searchable...</p>
		   <p><b><g:link controller="home" action="blog" fragment="03/08/12">03/08/12</g:link></b> Genome sequence update and download...</p>
		   <p><b><g:link controller="home" action="blog" fragment="06/07/12">06/07/12</g:link></b> BACs have been annotated...</p>
		   <p><b><g:link controller="home" action="blog" fragment="29/06/12">29/06/12</g:link></b> More BLAST updates...</p>
		   <p><b><g:link controller="home" action="blog" fragment="23/06/12">23/06/12</g:link></b> BLAST search now contains genomic contigs...</p>		   
		   <p><b><g:link controller="home" action="blog" fragment="22/06/12">22/06/12</g:link></b> Public data addded to home page...</p>
		   <p><b><g:link controller="home" action="blog" fragment="24/05/12">24/05/12</g:link></b> Contig attribute search and results added...</p>
		   <p><b><g:link controller="home" action="blog" fragment="01/05/12">01/05/12</g:link></b> Blog begins...</p>
		   <p><b><g:link controller="home" action="blog" fragment="27/04/12">27/04/12</g:link></b> Functional annotations added to EST UniGenes...</p>
		   <p><b><g:link controller="home" action="blog" fragment="20/04/12">20/04/12</g:link></b> Second round of Illumina data received...</p>
		   <p><b><g:link controller="home" action="blog" fragment="12/04/12">12/04/12</g:link></b> BLAST server created...</p>
		   <p><b><g:link controller="home" action="blog" fragment="02/04/12">02/04/12</g:link></b> First round of Illumina data received...</p>
		   <p><b><g:link controller="home" action="blog" fragment="02/03/12">02/03/12</g:link></b> Public ESTs assembled, annotated and added to db...</p>
		   <p><b><g:link controller="home" action="blog" fragment="25/02/12">25/02/12</g:link></b> Website created...<p>
		</td>
		</tr>
	    </table>
	    </div>
	    
	    <h1>The plan for the genome:</h1>
	    <p>Two libraries of 100bp Illumina reads are being generated at the <a href ="http://genepool.bio.ed.ac.uk/" target='_blank'>GenePool</a>. 
	    Combined, they will provide an estimated 100x coverage of the genome which after quality filtering will be used to produce the initial assembly.
	    Scaffolding of this assembly will then be achieved using BAC ends, RNA-seq and RAD sequencing, with the possibility of some mate pairs or PacBio 
	    long reads if needed. Once a first draft has been generated, a set of annotations will be produced and added to this site.
	    </p>
	    
	</sec:ifLoggedIn>
  </body>
</html>
