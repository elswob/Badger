<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <meta name='layout' content='main'/>
   <title>${grailsApplication.config.projectID} | BLAST parameters</title>
   <parameter name="blast" value="selected"></parameter>
</head>
<body text="#000000" bgcolor="#FFFFFF" link="#0000FF" vlink="#FF0000">
<!-- Changed by: Sergei Shavirin,  2-Apr-1996 -->
<hr>
<A name=program><h1>Programs available for the BLAST search</h1></A>
<hr>
<p>The NCBI <b>BLAST</b> programs available are :
<p>
<p><b>blastn</b></p>
<p>compares a nucleotide query sequence against a nucleotide sequence database</p>
<p><b>tblastn</b></p>
<p>compares a protein query sequence against a nucleotide sequence database
dynamically translated in all reading frames</p>
<p><b>tblastx</b></p>
<p>compares the six-frame translations of a nucleotide query sequence against
the six-frame translations of a nucleotide sequence database.</p>
<p><b>blastp</b></p>
<p>compares an amino acid query sequence against a protein sequence database.</p>
<p><b>blastx</b></p>
<p>compares a nucleotide query sequence translated in all reading frames against a protein sequence database.
</p>

<hr>
<h1><A name=db>Databases available for BLAST search</h1></center></A>
<hr>
<p>
The databases available to search are split into genomes, transcripts and proteins. Availability can be controlled by setting each data set to public or private.
<p>
<hr>
<h1><A name=filt>Low complexity filtering</h1></center></A>
<hr>
<p>
The server filters your query sequence for low compositional complexity regions by default. Low complexity regions
commonly give spuriously high scores that reflect compositional bias rather than significant position-by- position alignment.
Filtering can elminate these potentially confounding matches (e.g., hits against proline-rich regions or poly-A tails) from the blast
reports, leaving regions whose blast statistics reflect the specificity of their pairwise alignment. Queries searched with the blastn
program are filtered with DUST. Other programs use SEG. 
<p>
Low complexity sequence found by a filter program is substituted using the letter "N" in nucleotide sequence (e.g.,
"NNNNNNNNNNNNN") and the letter "X" in protein sequences (e.g., "XXXXXXXXX"). Users may turn off filtering by using the
"Filter" option on the "Advanced options for the BLAST server" page. 
<p>
Reference for the DUST program:
<br>
<br>     Tatusov, R. L. and D. J. Lipman, in preparation. 
<br>     Hancock, J. M. and J. S. Armstrong (1994). SIMPLE34: an improved and enhanced implementation for VAX and Sun
computers of the SIMPLE algorithm for analysis of clustered repetitive motifs in nucleotide sequences. Comput Appl Biosci
10:67-70. 
<p>
Reference for the SEG program:
<br>
<br>      Wootton, J. C. and S. Federhen (1993). Statistics of local complexity in amino acid sequences and sequence databases.
Computers in Chemistry 17:149-163. 
<br>      Wootton, J. C. and S. Federhen (1996). Analysis of compositionally biased regions in sequence databases. Methods in
Enzymology 266: 554-571. 
<p>
Reference for the role of filtering in search strategies:
<br>
<br>      Altschul, S. F., M. S. Boguski, W. Gish, J. C. Wootton (1994). Issues in searching molecular sequence databases. Nat
Genet 6: 119-129.
<p>
<hr>
<h1><A name=desc>Descriptions</h1></A>
<hr>
<p> 
Restricts the number of short descriptions of matching sequences reported to the number specified; default limit is 100
descriptions. See also EXPECT. 
<p>
<hr>
<h1><A name=ali>Alignments</h1></A>
<hr>
<p> 
Restricts database sequences to the number specified for which high-scoring segment pairs (HSPs) are reported; the
default limit is 100. If more database sequences than this happen to satisfy the statistical significance threshold for
reporting (see EXPECT below), only the matches ascribed the greatest statistical significance are reported. 
<p>
<hr>
<h1><A name=out>Output</h1></A>
<hr>
<p> 
Two options, full format is the standard BLAST alignment output and tabular is the tabular based output.
<p>
<hr>
<h1><A name=exp>Expect</h1></A>
<hr>
<p> 
The statistical significance threshold for reporting matches against database sequences; the default value is 10, such that 10
matches are expected to be found merely by chance, according to the stochastic model of Karlin and Altschul (1990). If
the statistical significance ascribed to a match is greater than the EXPECT threshold, the match will not be reported. Lower
EXPECT thresholds are more stringent, leading to fewer chance matches being reported. Fractional values are acceptable. 
<p>
<hr>
<h1><A name=fasta>Fasta Format Description</h1></A>
<hr>
<p> 
A sequence in FASTA format begins with a single-line description, followed by lines of sequence data. The description
line is distinguished from the sequence data by a greater-than (">") symbol in the first column. An example sequence in 
FASTA format is: </p><br>
<code>
>gi|1256569|gb|AAA96502.1| lumbrokinase-1T4 precursor [Lumbricus rubellus]<br>
MLLLALASLVAVGFAQPPVWYPGGQCSVSQYSDAGDMELPPGTKIVGGIEARPYEFPWQVSVRRKSSDSH<br>
FCGGSIINDRWVVCAAHCMQGESPALVSLVVGEHDSSAASTVRQTHDVDSIFVHEDYNGNTFENDVSVIK<br>
TVNAIAIDINDGPICAPDPANDYVYRKSQCSGWGTINSGGVCCPNVLRYVTLNVTTNAFCDDIYSPLYTI<br>
TSDMICATDNTGQNERDSCQGDSGGPLSVKDGSGIFSLIGIVSWGIGCASGYPGVYARVGSQTGWITDII<br>
TNN<br>

<br>
</code>
<br><p>
Sequences are expected to be represented in the standard IUB/IUPAC amino acid and nucleic acid codes, with these
exceptions: lower-case letters are accepted and are mapped into upper-case; a single hyphen or dash can be used to represent a
gap of indeterminate length; and in amino acid sequences, U and * are acceptable letters (see below). Before submitting a
request, any numerical digits in the query sequence should either be removed or replaced by appropriate letter codes (e.g., N for
unknown nucleic acid residue or X for unknown amino acid residue). 
The nucleic acid codes supported are: 
<PRE>
        A --> adenosine           M --> A C (amino)
        C --> cytidine            S --> G C (strong)
        G --> guanine             W --> A T (weak)
        T --> thymidine           B --> G T C
        U --> uridine             D --> G A T
        R --> G A (purine)        H --> A C T
        Y --> T C (pyrimidine)    V --> G C A
        K --> G T (keto)          N --> A G C T (any)
                                  -  gap of indeterminate length
</PRE>

For those programs that use amino acid query sequences (BLASTP 
and TBLASTN), the accepted amino acid codes are:
<PRE>

    A  alanine                         P  proline
    B  aspartate or asparagine         Q  glutamine
    C  cystine                         R  arginine
    D  aspartate                       S  serine
    E  glutamate                       T  threonine
    F  phenylalanine                   U  selenocysteine
    G  glycine                         V  valine
    H  histidine                       W  tryptophan
    I  isoleucine                      Y  tyrosine
    K  lysine                          Z  glutamate or glutamine
    L  leucine                         X  any
    M  methionine                      *  translation stop
    N  asparagine                      -  gap of indeterminate length
</PRE>
<HR>

</body>
</html>
