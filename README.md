# Badger

A lightweight genome environment 

## Home page

http://badger.bio.ed.ac.uk/

## Installation requirements

#### Hardware

A linux server with at least 2 CPUs and 4 GB RAM

#### Software

* [Grails](http://grails.org/) version 2.1.0 or above and a Java Development Kit (JDK) installed version 1.6 or above.
* [BLAST+](http://blast.ncbi.nlm.nih.gov/Blast.cgi?CMD=Web&PAGE_TYPE=BlastDocs&DOC_TYPE=Download)
* [Muscle](http://www.drive5.com/muscle/) (Optional)

#### Data

Each genome requires one genome file:

* A genome in FASTA format

Each set of gene predictions requires three files:

* A set of gene predictions in GFF3 format
* The set of gene transcripts from the GFF3 file in FASTA format
* The set of gene proteins from the GFF3 file in FASTA format 

Each set of gene predictions can be decorated with annotation data

* XML BLAST output
* RAW InterProScan output
* Custom tabular annotation data files

## Installation (Ubuntu 12.04 and above)

Install Grails and the JDK

```
sudo add-apt-repository ppa:groovy-dev/grails
sudo apt-get update
sudo apt-get install grails-ppa
```

Download and install either the openjdk-6-jdk or sun-java6-jdk JDK package and install, then set the JAVA_HOME path in .bashrc or .bash_profile, e.g.

`export JAVA_HOME=/usr/lib/jvm/java-1.6.0-openjdk-amd64`

Install Git

`sudo apt-get install git`

Install PostGreSQL

`sudo apt-get install postgresql`

Download Badger

`git clone https://github.com/elswob/Badger.git`

Download BLAST+

`sudo apt-get install ncbi-blast+`

Download Muscle (Optional)

```
wget http://www.drive5.com/muscle/downloads3.8.31/muscle3.8.31_i86linux64.tar.gz
tar zxvf muscle3.8.31_i86linux64.tar.gz
```

## Configure

#### PostGreSQL

Download the postgres [driver](http://jdbc.postgresql.org/download/postgresql-9.2-1002.jdbc4.jar) to place in the grails lib directory later on

Create a postgres user with superuser rights and password

```
su
su postgres
createuser -P -s -e yourname
```

Create a database

```
su postgres
createdb Badger -O xxx -E UTF8 --locale=en_GB.utf8 -T template0;
```

If this fails with this error?

`createdb: database creation failed: ERROR:  invalid locale name en_GB.utf8`

Try this:

`createdb Badger -O xxx -E UTF8 -T template0;`

Edit your pg_hba.conf file to allow local and host md5 authentication.

#### Badger config

Create a badger.config.properties file and edit where necessary, e.g. the paths to the executables...

```
/*** project name (used in page titles) ***/
projectID = Nematode genomes

/*** database ***/
dataSource.driverClassName = org.postgresql.Driver
dataSource.username = yourname
dataSource.password = yourpassword
dataSource.url = jdbc:postgresql://127.0.0.1:5432/Badger

/*** executables ***/
blastPath = /usr/local/bin/
musclePath = /path/to/muscle/muscle3.8.31_i86linux64

/*** web images for front page and favicon (need to be in grails web-app/images) ***/
headerImage = My_species_header.jpg
mainImage = My_species_main.jpg
mainImageSource = Where did the image come from?

/*** contact data for footer ***/
contact.email = your_email_address

/*** internal links - set to public (everyone) or private (only those logged in)***/
i.links.download = public
i.links.all = public
i.links.species = public
i.links.members = private
i.links.blast = public
i.links.browse = public
i.links.stats = public

/*** external links (two comma separated values, 1:the name to show 2:where to go)***/
e.links.pub.1 = Nematodes.org, http://www.nematodes.org
e.links.pub.2 = Nembase4, http://www.nematodes.org/nembase4/
e.links.pub.3 = WormBase, http://www.wormbase.org/
e.links.pub.4 = OpenWorm, http://www.openworm.org/

/*** project members (a place to list details of the people associated with the project)***/
/*** takes three comma separated values 1: a name with optional html link 2: email address 3: location Id (matches the mem.location I) ***/         
mem.person.1 = Your name, your_email_address, Location1, 
mem.person.2 = <a href ="http://www.badger.bio.org">Ben Elsworth</a>, ben.elsworth.ed.ac.uk, Edinburgh, 
mem.location.Location1 = homecrest.gif, http://www.ed.ac.uk

/*** news feed (set to public or private)***/
news.status = public

/*** orthomcl (location of optional orthomcl file) ***/
o.file = orthomcl/groups_1.5.txt

/*** tree (location of optional phyloxml file) ***/
t.file = trees/your_phyloXML_tree.xml
```

Note, any changes made to this file will only be updated by restarting the `grails prod run-app` command (see below)

## Upload data

#### Images

Add all images to the web-app/images directory

#### Data files

Create a data directory at the root level of where you installed Badger

`mkdir /home/user/Badger/data`

Create directory for each species/genome

`mkdir /home/user/Badger/data/M_meles`

Note, all data must adhere to the [data rules](https://github.com/elswob/Badger/blob/master/README.md#data-rules)

Add all files for M. meles in here, e.g:

* Genome FASTA files
* GFF3 files 
* Transcript and protein FASTA files 
* BLAST XML files
* InterProScan RAW files
* Custom tabular annotation files

Custom tabular format is defined as 6 tab delimited columns:

1 = the transcript ID  
2 = the source of the annotation  
3 = the annotation ID  
4 = the start base of annotation on transcript  
5 = the end base of annotation on transctipt  
6 = the score  

e.g.

```
nAv.1.0.1.t00006-RA     EC      3.1.3.2 31      369      201    Acidphosphatase.
nAv.1.0.1.t00006-RA     EC      3.1.3.5 31      369      178    5{prime}-nucleotidase.
nAv.1.0.1.t00011-RA     EC      3.6.4.6 237     377     58.2    Vesicle-fusingATPase.
nAv.1.0.1.t00015-RA     EC      2.7.7.56        246     485     62.0    tRNAnucleotidyltransferase.
nAv.1.0.1.t00021-RA     EC      3.1.3.16        4       316      295    Phosphoproteinphosphatase.
```

## Start it up

#### Start grails 

For ease of control it may be wise to run this command using [screen](https://help.ubuntu.com/community/Screen).

To run on port 8080, run the following command as normal user from within root directory of Badger:

`grails prod run-app`

Or to run on port 80, run as root:

`grails prod run-app -Dserver.port=80`

If during start up there is a warning about the version of the app being older than the version of Grails installed, just upgrade the version of the app.

`grails upgrade`

#### Important security update !!!!

Change the default admin password! To do so go to this address `yourdomain.com/user`, find the admin user and change the password.

Whilst in this section, you can register users for the site giving them access to any areas you have labelled as private. This can be done by going to ``yourdomain.com/register` and registering a new user.

## Load data to database

#### Add file information

Two ways, the first option is ideal for one or two species/genomes, the second is more suitable for buld data.

1. Via the GUI

Log in as admin and fill in the forms as necessary. 

2. Using the AddFileData.groovy script

Edit the script `scripts/AddFileData.groovy` in compliance with the demo data already there, and make sure the data will be loaded by removing the '//' at the start of the section.  

#### Load data

As this can take a while it may be wise to run this command using [screen](https://help.ubuntu.com/community/Screen).

When all the file information is successfully loaded into the database, open a terminal, go to the root directory of Badger, and type:

`./runme.sh`

This will run a number of scripts which adds the actual content of the data files to the database. These scripts are:

```
AlterTables.groovy - Creates text indices columns for annotation descriptions.

AddFileData.groovy - If required, file data will be loaded from here.

AddPublicationData.groovy - Based on the species data added, all publications from PubMed will be downloaded and added to the database.

AddSequenceData.groovy - All sequence data for the genomes and genes is added.

AddOrthoData.groovy  - If provided the OrthoMCL groups file is loaded.

AddGeneBlastData.groovy - All BLAST XML data is loaded.

AddGeneFuncAnnoData.groovy - All custom annotation and InterProScan data is added.

CreateFiles.groovy - A tab delimited annotaion file for each set of genes is produced and added to the downloads page.
```

This step could take a number of hours depending on the size of the files and number of species/genomes involved.

#### Subsequent data

If you need to add more data just follow one of the two steps above and run the same command `./run_me.sh`. This will identify the new files and add the data accordingly.

## Additional features

#### Customise home page

By logging in as admin, an option to modify the home page is available.

#### Phylogenetic tree

For instances of Badger with multiple species a phylogenetic tree containing information on the relationshipds between them can be used. 
This tree needs to be in the [phyloXML](http://www.phyloxml.org/) format. A newick tree can be converted into this format using the [phyloxml converter](http://phylosoft.org/forester/applications/phyloxml_converter/) script:

`java -cp forester.jar org.forester.application.phyloxml_converter -f=nn -ru tree.newick tree.xml`

To add the file, simply copy the tree to a directory within your data folder and add its whereabouts to the appropriate secion of the badger.config.properties file

#### OrthoMCL analysis

Orthologs of gene trancripts can be displayed if an OrthoMCL groups file is provided in the badger.config.properties file. This is the file produced during step 13 of the OrthoMCL process:

```
========== Step 13: orthomclMclToGroups ==========
Input:
  - mclOutput file
Output:
  - groups.txt

Change to my_orthomcl_dir and run:
  orthomclMclToGroups my_prefix 1000 < mclOutput > groups.txt
```

The naming convention expected is that each transcript ID is preceded by an OrthoMCL ID, e.g. Mmeles|gene1234

If this file is added after the initial data, the ortholog data will need to be loaded into the database. This can be done simply by running `./run_me.sh` again.

#### External data

It is possible to add data from an external genome project and link the genes back to the external database. Furthermore this is possible without a GFF3 file and just the three FASTA files. If this file is missing then a fake GFF3 file is needed (a list of transcript IDs that matches those in the FASTA files). In addotion information about the external data source is also required.

Currently the only way to add this data is using the AddFileData.groovy script. For an example see the C. teleta section. 

## Data rules

1. All chromosme/scaffold/contig IDs must be unique, e.g. Mmeles_v1.0_scaffold_1
2. GFF3 files should follow this format:

```
http://rice.bio.indiana.edu:7082/annot/gff3.html

THE CANONICAL GENE
------------------

To illustrate how a canonical gene should be represented consider
Figure 1 (canonical_gene.png).  This indicates a gene named EDEN
extending from position 1000 to position 9000.  It encodes three
alternatively-spliced transcripts named EDEN.1, EDEN.2 and EDEN.3.  It
also has an identified transcriptional factor binding site located 50
bp upstream from the transcriptional start site of EDEN.1 and EDEN2.

Here is how this gene should be described using GFF3:

##gff-version   3
##sequence-region   ctg123 1 1497228       
ctg123 . gene            1000  9000  .  +  .  ID=gene00001;Name=EDEN
ctg123 . mRNA            1050  9000  .  +  .  ID=mRNA00001;Parent=gene00001;Name=EDEN.1
ctg123 . CDS             1201  1500  .  +  0  Parent=mRNA0001
ctg123 . CDS             3000  3902  .  +  0  Parent=mRNA0001
ctg123 . CDS             5000  5500  .  +  0  Parent=mRNA0001
ctg123 . CDS             7000  7600  .  +  0  Parent=mRNA0001
ctg123 . mRNA            1050  9000  .  +  .  ID=mRNA00002;Parent=gene00001;Name=EDEN.2
ctg123 . CDS             1201  1500  .  +  0  Parent=mRNA0002
ctg123 . CDS             5000  5500  .  +  0  Parent=mRNA0002
ctg123 . CDS       7000  7600	 .  +  0  Parent=mRNA0002
ctg123 . mRNA            1300  9000  .  +  .  ID=mRNA00003;Parent=gene00001;Name=EDEN.3
ctg123 . CDS             3301  3902  .  +  0  Parent=mRNA0003
ctg123 . CDS	     5000  5500	 .  +  2  Parent=mRNA0003
ctg123 . CDS	     7000  7600	 .  +  2  Parent=mRNA0003
```
3. All gene IDs must be unique, e.g. Mmeles_v_1.0_g_1 / Mmeles_v_1.0_t_1 and match the ID in the GFF3 file
4. Alternate transcripts are fine as long as names are unique and CDS follows mRNA
5. GFF3 files need to be sorted with respect to the proteins, e.g. gene -> mRNA -> CDS. This can be achieved with gffsort.pl (see wormbase section below).
6. GFF3 files are for gene predictions only, any term matching gene, mRNA or CDS in the third column will be picked up, so remove anything that isn't a gene, e.g. ncRNA in some of the wormbase files
7. All images go in web-app/images not in the user generated data/directores

## Troubleshooting

#### No data loaded

If you are using the AddFileData.groovy script make sure the `//` have been removed from line 15 or so where the method call is made

#### Strange page behaviour

If either the species_search or m_info pages appear to be wrong, it may be because they are cached. To remove this cache simply stop grails and restart it. By killing this command and then starting it again.

`grails prod run-app`

#### Errors on start up

Errors relating to the postgres indexing such as:

```
| Error 2013-01-24 15:35:53,254 [localhost-startStop-1] ERROR context.GrailsContextLoader  - Error executing bootstraps: org.h2.jdbc.JdbcSQLException: Unkno
wn data type: "TSVECTOR"; SQL statement:
ALTER TABLE trans_anno ADD COLUMN textsearchable_index_col tsvector; [50004-164]
```

Generally means grails can't link to the postgres database, so check your badger.config.properties file carefully

#### Loading file data

Error:

```
xxxxxxx.xml was added
| Error Error executing script RunScript: java.lang.NullPointerException: Cannot get property 'file_dir' on null object (Use --stacktrace to see the full trace)
```

Could mean that the GFF Id used in the addAnno() function does not match the GFF file from which this annotation is linked.

#### Loading sequence data

Error:

```
| Error Error executing script RunScript: java.lang.NullPointerException: Cannot get property 'file_dir' on null object (Use --stacktrace to see the full trace)
```

If you have loaded file information using the AddFileData.groovy script, check that the FASTA files are linked to the GFF file in the `fileMap.file_link` setting.