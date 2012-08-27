<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} blog</title>
  <parameter name="home" value="selected"></parameter>
  </head>

  <body>
   
<h1><i>Bicyclys anynana</i> blog:</h1>

<table>

<tr><td>
<h3 id ="15/08/12"><b>15/08/12</b> Publication data now auto updates and is searchable.</h3>
<p>All Bicyclus anynana PubMed data is auto updated into the database weekly and is searchable by title, abstract, author, journal or year.
</td></tr>

<tr><td>
<h3 id ="03/08/12"><b>03/08/12</b> Genome sequence update and download.</h3>
<p>There is a slightly improved genome assembly now available to download and search using BLAST.
</td></tr>

<tr><td>
<h3 id ="06/07/12"><b>06/07/12</b> BACs have been annotated.</h3>
<p>The 11 public BACs have been annotated using MAKER and can now be browsed using GBrowse2.
</td></tr>

<tr><td>
<h3 id ="29/06/12"><b>29/06/12</b> More BLAST updates.</h3>
<p>The 11 public BACs have been added to the BLAST databases as well as the ability to upload a file.
</td></tr>

<tr><td>
<h3 id ="23/06/12"><b>23/06/12</b> BLAST search now contains genomic contigs.</h3>
<p>An assembly of un-scaffolded genomic contigs are now available to search on the BLAST page. Please note that these are from a basic assembly and are likely to change at any time.
</td></tr>

<tr><td>
<h3 id="22/06/12"><b>22/06/12</b> Public data addded to home page.</h3>
<p>Search and BLAST access to the assembled public EST UniGenes is now possible from the home page without logging in.	
</td></tr>

<tr><td>
<h3 id ="24/05/12"><b>24/05/12</b> Contig attribute search and results added.</h3>
<p>There is now the option to search the UniGenes by contig attribute, (GC, length and coverage). This loads a lot of data into memory using javascript so please let me know if there are issues with this. The search results also now
features a similar view (under the chart tab) which displays each contig as a point with radius proportional to score.   
</td></tr>

<tr><td>
<h3 id="01/05/12"><b>01/05/12</b> Blog begins.</h3>
<p>
To keep you all up to date with the latest developments of the genome and it's associated data I have set up this blog. I will try to update it regularly and have added some of the major developments since the project started. 
</td></tr>

<tr><td>
<h3 id="27/04/12"><b>27/04/12</b> Functional annotations added to EST UniGenes</h3>
<p>
The 20,106 UniGenes have been annotated with <a href="http://www.nematodes.org/bioinformatics/annot8r/">annot8r</a> and <a href="http://www.ebi.ac.uk/Tools/pfa/iprscan/">InterProScan</a> which have added Gene Ontology, 
Enzyme Commission, KEGG and domain annotations. This now means that a search can be performed on either BLAST homology, Annot8r or InterProscan annotations.
</td></tr>

<tr><td>
<h3 id="20/04/12"><b>20/04/12</b> Second round of Illumina data received.</h3>
<p>
Due to the slightly poor quality of the first run, both the Illumina libraries were rerun and produced 371 million 101 bp reads (37 Gb). The <a href="http://www.bioinformatics.babraham.ac.uk/projects/fastqc/">fastQC</a> metrics for the libraries are available here:
</p>
<p>300 bp insert library: 
<a href="${g.resource(dir:'fastqc/120406_0171_AD0T2DACXX_1_SA-PE-041_1.sa_fastqc',file:'fastqc_report.html',base: '../')}">Read 1</a>
<a href="${g.resource(dir:'fastqc/120406_0171_AD0T2DACXX_1_SA-PE-041_2.sa_fastqc',file:'fastqc_report.html',base: '../')}">Read 2</a>
</p>
<p>
600 bp insert library: 
<a href="${g.resource(dir:'fastqc/120406_0171_AD0T2DACXX_1_SA-PE-042_1.sa_fastqc',file:'fastqc_report.html',base: '../')}">Read 1</a>
<a href="${g.resource(dir:'fastqc/120406_0171_AD0T2DACXX_1_SA-PE-042_2.sa_fastqc',file:'fastqc_report.html',base: '../')}">Read 2</a>
</td></tr>

<tr><td>
<h3 id="12/04/12"><b>02/04/12</b> BLAST server created.</h3>
<p> A new and improved BLAST server was generated allowing BLAST searches of the EST UniGenes.
</td></tr>

<tr><td>
<h3 id="02/04/12"><b>02/04/12</b> First round of Illumina data received.</h3>
<p>
265 million 101 bp reads (26 Gb). The <a href="http://www.bioinformatics.babraham.ac.uk/projects/fastqc/">fastQC</a> metrics for the libraries are available here:
</p>
<p>
300 bp insert library: 
<a href="${g.resource(dir:'fastqc/120315_0312_AD0T4UACXX_3_SA-PE-041_1.sa_fastqc',file:'fastqc_report.html',base: '../')}">Read 1</a>
<a href="${g.resource(dir:'fastqc/120315_0312_AD0T4UACXX_3_SA-PE-041_2.sa_fastqc',file:'fastqc_report.html',base: '../')}">Read 2</a>
</p>
<p>
600 bp insert library: 
<a href="${g.resource(dir:'fastqc/120315_0312_AD0T4UACXX_3_SA-PE-042_1.sa_fastqc',file:'fastqc_report.html',base: '../')}">Read 1</a>
<a href="${g.resource(dir:'fastqc/120315_0312_AD0T4UACXX_3_SA-PE-042_2.sa_fastqc',file:'fastqc_report.html',base: '../')}">Read 2</a>
</td></tr>

<tr><td>
<h3 id="02/03/12"><b>02/03/12</b> Public ESTs assembled, annotated and added to db.</h3>
<p>
The 100,595 public ESTs were assembled into 20,003 UniGenes using <a href="http://pbil.univ-lyon1.fr/cap3.php">CAP3</a>. These were then annotated with SwissProt, UniRef90 and ESTs using BLAST and added to the database. 
</td></tr>

<tr><td>
<h3 id="25/02/12"><b>25/02/12</b> Website created</h3>
The website for the genome project was launched.
</td></tr>
</table>

</body>
</html>
