# Badger

A lightweight genome environment

## Installation requirements

#### Hardware

A linux server with at least 2 CPUs and 4 GB RAM

#### Software

* [Grails](http://grails.org/) version 2.1.0 or above and a Java Development Kit (JDK) installed version 1.6 or above.
* [BLAST+](http://blast.ncbi.nlm.nih.gov/Blast.cgi?CMD=Web&PAGE_TYPE=BlastDocs&DOC_TYPE=Download)
* [Clustal-omega](http://www.clustal.org/omega/) (Optional)

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

Set the JAVA_HOME path in .bashrc or .bash_profile

`export JAVA_HOME=/usr/lib/jvm/java-1.6.0-openjdk-amd64`

Install Git

`sudo apt-get install git`

Install PostGreSQL

`sudo apt-get install postgresql`

Download Badger

`git clone https://github.com/elswob/Badger.git`

Download BLAST+

`sudo apt-get install ncbi-blast+`

Download Clustal-omega (Optional)

```
wget http://www.clustal.org/omega/clustal-omega-1.1.0.tar.gz
tar zxvf clustal-omega-1.1.0.tar.gz
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

#### Badger config

Create a badger.config.properties file and edit where necessary, e.g. the paths to the executables...

```
///////////// global things //////////////

/*** project name (used in page titles) ***/
projectID = Nematode genomes

/*** database ***/
dataSource.driverClassName = org.postgresql.Driver
dataSource.username = yourname
dataSource.password = yourpassword
dataSource.url = jdbc:postgresql://127.0.0.1:5432/Badger

/*** executables ***/
blastPath = /usr/local/bin/
clustaloPath = /home/yourname/software/clustal_omega/clustalo-1.1.0-linux-64 

/*** web images for front page and favicon (need to be in grails web-app/images) ***/
headerImage = My_species_header.jpg
mainImage = My_species_main.jpg
mainImageSource = Where did the image come from?

/*** contact data for footer ***/
contact.email = your_email_address

/*** internal links ***/
i.links.download = public
i.links.all = public
i.links.species = public
i.links.members = private
i.links.blast = public
i.links.browse = public
i.links.stats = public

/*** external links ***/
e.links.pub.1 = Nematodes.org, http://www.nematodes.org
e.links.pub.2 = Nembase4, http://www.nematodes.org/nembase4/
e.links.pub.3 = WormBase, http://www.wormbase.org/
e.links.pub.4 = OpenWorm, http://www.openworm.org/

/*** project members ***/       
mem.person.1 = Your name, your_email_address, Location1, 
mem.person.2 = <a href ="http://www.nematodes.org">Mark Blaxter</a>, mark.blaxter.ed.ac.uk, Edinburgh, 
mem.location.Location1 = homecrest.gif, http://www.ed.ac.uk

/*** news ***/
news.status = public

/*** orthomcl ***/
o.file = orthomcl/groups_1.5.txt

/*** tree ***/
t.file = trees/your_phyloXML_tree.xml
```

## Upload data

#### Images

Add all images to the web-app/images directory

#### Data files

Create a data directory at the root level of where you installed Badger

`mkdir /home/user/Badger/data`

Create directory for each species/genome

`mkdir /home/user/Badger/data/M_meles`

Add all files for M. meles in here, e.g:

* Genome FASTA files
* GFF3 files 
* Transcript and protein FASTA files 
* BLAST XML files
* InterProScan RAW files
* Custom tabular annotatin files

## Start it up

#### Start grails

From within root directory of Badger:

`grails prod run-app`

If during start up there is a warning about the version of the app being older than the version of Grails installed, just upgrade the version of the app.

`grails upgrade`

#### Important security update !!!!

Change the default admin password! To do so go to this address `yourdomain.com/user`, find the admin user and change the password.

## Load data to database

#### Add file information

Two ways, the first option is ideal for one or two species/genomes, the second is more suitable for buld data.

1. Via the GUI

Log in as admin and fill in the forms as necessary.

2. Using the AddFileData.groovy script

Edit the script `scripts/AddFileData.groovy` in compliance with the demo data already there. 

#### Load data

When all the file information is successfully loaded into the database, open a terminal, go to the root directory of Badger, and type:

`./run_me.sh`

This will run a number of scripts which adds the rest of the data to the database. This step could take a number of hours depending on the size of the files and number of species/genomes involved.

#### Subsequent data

If you need to add more data just follow one of the two steps above and run the same command `./run_me.sh`. This will identify the new files and add the data accordingly.

## Additional features

#### Customise home page

By logging in as admin, an option to modify the home page is available.

#### Phylogenetic tree

For instances of Badger with multiple species a phylogenetic tree containing information on the relationshipds between them can be used. 
This tree needs to be in the [phyloXML](http://www.phyloxml.org/) format. A newick tree can be converted into this format using the [phyloxml converter](http://phylosoft.org/forester/applications/phyloxml_converter/) script:

`java -cp forester.jar org.forester.application.phyloxml_converter -f=nn -ru tree.newick tree.xml`

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


