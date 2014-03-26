# Badger

An accessible genome exploration environment 

# Contents

[Home page](#home-page)  
[Installation](#installation-requirements)  
[Configuration](#configure)  
[Uploading file data](#upload-data)  
[Starting the application](#start-it-up)  
[Loading data into the database](#load-data-to-database)  
[Site navigation](#site-navigation)  
[Additional features](#additional-features)  
[Data rules](#data-rules)  
[Troubleshooting](#troubleshooting)

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

## Installation 

The following guide is for **Ubuntu 12.04** and above. For other OS please follow the instructions [here](http://grails.org/doc/latest/guide/gettingStarted.html#requirements). There is a known issues with Mac OS and the latest version of Grails causing errors whilst upgrading the project. To avoid this please use an earlier version of Grails, e.g. 2.1.*

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

Make project directory and download badger

```
mkdir myProject
cd myProject
git clone https://github.com/elswob/Badger.git
```

Download BLAST+

`sudo apt-get install ncbi-blast+`

Download Muscle (Optional)

```
wget http://www.drive5.com/muscle/downloads3.8.31/muscle3.8.31_i86linux64.tar.gz
tar zxvf muscle3.8.31_i86linux64.tar.gz
```

## Configure

#### PostGreSQL

Download the postgres [driver](http://jdbc.postgresql.org/download/postgresql-9.2-1002.jdbc4.jar) and place it in the lib directory of the Badger application:


`cp postgresql-9.2-1002.jdbc4.jar Badger/lib/`


Create a postgres user with superuser rights and password (edit the red text and remove apostrophes).

```bash
su
su postgres
createuser -P -s -e 'your_user_name'
```

Create a database

```bash
su postgres
createdb Badger -O 'your_user_name' -E UTF8 --locale=en_GB.utf8 -T template0;
```

If this fails with this error?

`createdb: database creation failed: ERROR:  invalid locale name en_GB.utf8`

Try this:

```bash
createdb Badger -O 'your_user_name' -E UTF8 -T template0;`
```

Set the PostGreSQL permissions - edit your `pg_hba.conf` file to allow local and host md5 authentication and restart postgres.

#### Badger config

Edit the **badger.config.properties** file inside `myProject/Badger` where necessary (red text). 
**Note, your text should not contain apostrophes!**. If using the demo data just fill in the missing information.

```bash
/*** project name (used in page titles) ***/
projectID = 'name_of_your_project'

/*** database ***/
dataSource.driverClassName = org.postgresql.Driver
dataSource.username = 'your postgres user name'
dataSource.password = 'your postgres password'
dataSource.url = jdbc:postgresql://127.0.0.1:5432/Badger

/*** executables ***/
blastPath = '/path/to/blast+/bin/'
musclePath = '/path/to/muscle/muscle3.8.31_i86linux64'

/*** web images for front page and favicon (need to be in grails web-app/images) ***/
headerImage = 'My_species_header.jpg'
mainImage = 'My_species_main.jpg'
mainImageSource = 'Where did the image come from?'

/*** contact data for footer ***/
contact.email = 'your_email_address'

/*** internal links - set to public (everyone) or private (only those logged in)***/
i.links.download = 'public'
i.links.all = 'public'
i.links.species = 'public'
i.links.members = 'private'
i.links.blast = 'public'
i.links.browse = 'public'
i.links.stats = 'public'

/*** external links (two comma separated values, 1:the name to show 2:where to go)***/
e.links.pub.1 = 'Nematodes.org, http://www.nematodes.org'
e.links.pub.2 = 'Nembase4, http://www.nematodes.org/nembase4/'
e.links.pub.3 = 'WormBase, http://www.wormbase.org/'
e.links.pub.4 = 'OpenWorm, http://www.openworm.org/'

/*** project members (a place to list details of the people associated with the project)***/
/*** takes three comma separated values 1: a name with optional html link 2: email address 3: location Id (matches the mem.location I) ***/         
mem.person.1 = 'Your name, your_email_address, Location1'
mem.person.2 = '<a href ="http://www.badger.bio.org">Ben Elsworth</a>, ben.elsworth.ed.ac.uk, Edinburgh' 
mem.location.Location1 = 'university_edinburgh.jpg, http://www.ed.ac.uk'

/*** news feed (set to public or private)***/
news.status = 'public'

/*** orthomcl (location of optional OrthoMCL file) ***/
o.file = 'orthomcl/groups_1.5.txt'

/*** tree (location of optional phyloxml file) ***/
t.file = 'trees/your_phyloXML_tree.xml'

/*** database backup (the number of nightly backups of the database to keep) ***/
d.number = 10
```

Note, any changes made to this file will only be updated by restarting the `grails prod run-app` command (see below)

## Upload data

Skip this section if using the demo data.

#### Images

Add all images to the web-app/images directory

#### Data files

Create a data directory at the root level of where you installed Badger

`mkdir myProject/Badger/data`

Create directory for each species/genome, e.g for M. meles

`mkdir myProject/Badger/data/M_meles`

Note, all data must adhere to the [data rules](https://github.com/elswob/Badger/blob/master/README.md#data-rules)

Add all unzipped files for M. meles into `myProject/Badger/data/M_meles`, e.g:

* Genome FASTA files
* GFF3 files 
* Transcript and protein FASTA files 
* BLAST XML files
* InterProScan RAW files
* Custom tabular annotation files

Note, the only way to do this is by manually copying the files to the server.

Custom tabular format is defined as 7 tab delimited columns:

1 = the transcript ID  
2 = the source of the annotation  
3 = the annotation ID  
4 = the start base of annotation on transcript  
5 = the end base of annotation on transctipt  
6 = the score  
7 = notes

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

To run on port 8080, run the following command as normal user from within root directory of Badger, e.g. `myProject/Badger`:

`grails prod run-app`

Or to run on port 80, run as root:

`grails prod run-app -Dserver.port=80`

If during start up there is a warning about the version of the app being older than the version of Grails installed, just upgrade the version of the app.

`grails upgrade`

Note, running in production mode will not pick up any code changes to config files or controllers. To add these changes to the running instance stop grails and start it up again with `grails prod run-app`.

#### Important security update !!!!

Change the default admin password! To do so, login in as user `admin` using the password `badger` and go to this address `name_of_your_project/user`, find the admin user and change the password.

Whilst in this section, you can register users for the site giving them access to any areas you have labelled as private. This can be done by going to `name_of_your_project/register` and registering a new user.

## Load data to database

#### Add file information

Two ways, the first option is ideal for one or two species/genomes, the second is more suitable for buld data. For the demo data use option 2 with no changes to the file.

1. Via the GUI - log in as admin, click on the admin tab and fill in the forms as necessary. 
2. Using the AddFileData.groovy script - edit the script `scripts/AddFileData.groovy` in compliance with the demo data already there, and make sure the data will be loaded by removing the '//' at the start of the section.  

#### Load data

As this can take a while it may be wise to run this command using another [screen](https://help.ubuntu.com/community/Screen).

Again, there are two options depending on how the file data was added. If it was added using the GUI, open a terminal, go to the project root directory of Badger, e.g. `myProject/Badger` and type:

`./runme.sh`

If file data is being added using the `AddFileData.groovy`, or if you just want to load the demo data set, use this command:

`./runme_data.sh`

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

If you need to add more data just follow one of the two steps above and run the same command `runme`. This will identify the new files and add the data accordingly.

## Site navigation

On every page there is an **info** button in the navigation bar. This provides useful information on the key features of each page and an overview of the site on the home page. This information is described in more detail below.

#### Home page

The aim of the home page is to give an overview of the project, up-to-date news and links to the main sections of the site. The default layout of the page is an image and text describing the project. However, using the inbuilt text editor the admin user can customise the page to their own liking, uploading images, adding text, tables, etc.

The news box is again, by default, only editable by the admin user. It is a simple blog style news feed which links to a page where more details of each of the entries can be viewed.

The search box in the top right corner of the site is a keyword based search that searches all the text in the publication abstracts and gene annotations using related terms and multiple word options.

The navigation bar at the top of the page links to the main sections of the site, these include:

#### Publications

By default, all PubMed publications that match the species names in the database are downloaded to the site and automatically updated every week. The data can be searched by author, year, journal, abstract keyword and species. 

#### Search

Clicking on the search link itself will provide an overview of all the data in the database. The dropdown options include:

###### Species

The first option opens a page which displays meta data on all the species present in the database. If only one species is present and no phyloXML files, then this link skips straight to the genome and gene selection page. Once a species is selected, a page with available genome and annotation versions is displayed. This then links to a genome/gene overview page for the selected version, including interactive graphs and a tab controlled search page. The search page has numerous options to search the data from searching everything with a keyword to searching by gene ID.   

Search results then provide links to gene IDs, which when clicked will either open a gene page, or go straight to the more detailed transcript page if only one transcript exists for that particular gene. This page contains all information for the gene, including sequence downloads, annotations, exons, GBrowse (where available) and ortholog information.

###### Publications

Is a link to the publications page as mentioned above.

###### Orthologs

If an ortholog file is provided, this link will open up a page displaying an overview of that data. This includes an interactive plot showing the distribution of ortholog groups as well as a tab controlled search page for the ortholog data. Once an ortholog group has been selected there is an option to download the sequences from that group or align them to see immediately how the sequences in the cluster align.

###### All

This page provides the same service as the search box in the top right corner of the site. 

#### BLAST

Another method of searching the data is by alignment. This is achieved using the inbuilt BLAST server which can be used to search the genome and gene data. Results from a search can be downloaded or used to obtain more information on the transcripts that have successfully matched.

#### Download

The main data files and an automatically generated tab delimited annotation file can be downloaded from this page. Access to this data can be restricted to users that are logged in to the site.  

#### Login

The admin user is installed by default, and this user then has the ability to create other users for the site. This then allows certain aspects of the data to be available to logged in users only, e.g. search, BLAST and download.

When logged in as the admin user an admin link appears in the navigation bar. This opens up the admin section of the site wherein it is possible to add genome and gene data.

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

The naming convention expected is that each transcript ID is preceded by an OrthoMCL ID, e.g. Mmeles|gene1234, and the transcript ID matches that of the other files.

If this file is added after the initial data, the ortholog data will need to be loaded into the database. This can be done simply by running `./run_me.sh` again.

#### External data

It is possible to add data from an external genome project and link the genes back to the external database. Furthermore this is possible without a GFF3 file and just the three FASTA files. If this file is missing then a fake GFF3 file is needed (a list of transcript IDs that matches those in the FASTA files). In addotion information about the external data source is also required.

Currently the only way to add this data is using the AddFileData.groovy script. For an example see the C. teleta section. 

#### Running multiple instances

It is possible to run multiple versions of Badger on the same machine, however it does require a little bit of work. One solution is to install [lighttpd](http://www.lighttpd.net/) and configure this to point unique url links to separate port numbers. An example of how to do this on a Ubuntu 12.04 install is shown below.

###### Step 1 -  install lighttpd

First if it is installed, you may need to either stop or remove apache:

```
#stop
sudo service apache2 stop

#Remove apache2 packages and dependencies:
sudo apt-get purge apache2 apache2-utils apache2.2-bin apache2-common
sudo apt-get autoremove

#If you manually modified or installed stuff, apt might not remove it. Check what's left:
whereis apache2

#Have a look whats inside these directories, and if you're confident you want to trash it, manually remove the directories. 
sudo rm -Rf /etc/apache2 /usr/lib/apache2 /usr/include/apache2
```

Next, install lighttpd:

```
sudo apt-get install lighttpd
sudo /etc/init.d/lighttpd start
```

###### Step 2 - configure ligttpd

Each instance needs a unique URL and two ports assigning to it. To do this edit the lighttpd.conf file. The Badger version looks like this:

```
server.modules = (
	"mod_access",
	"mod_alias",
	"mod_compress",
 	"mod_redirect",
#       "mod_rewrite",
	"mod_proxy",
	"mod_accesslog"
)


$HTTP["url"] =~ "/demo*"{
           proxy.server = (
                "" => (
                        "grails" => (
                                "host" => "127.0.0.1",
                                "port" => 8082,
                                "fix-redirects" => 1
                        )
                )
        )

}

$HTTP["url"] =~ "/filarial*"{
           proxy.server = (
                "" => (
                        "grails" => (
                                "host" => "127.0.0.1",
                                "port" => 8084,
                                "fix-redirects" => 1
                        )
                )
        )

}

server.document-root        = "/var/www"
server.upload-dirs          = ( "/var/cache/lighttpd/uploads" )
server.errorlog             = "/var/log/lighttpd/error.log"
accesslog.filename	        = "/var/log/lighttpd/access.log"	
server.pid-file             = "/var/run/lighttpd.pid"
server.username             = "www-data"
server.groupname            = "www-data"
server.errorfile-prefix     = "/var/www/html/errors/status-" 

index-file.names            = ( "index.php", "index.html",
                                "index.htm", "default.htm",
                               " index.lighttpd.html" )

url.access-deny             = ( "~", ".inc" )

static-file.exclude-extensions = ( ".php", ".pl", ".fcgi" )

## Use ipv6 if available
#include_shell "/usr/share/lighttpd/use-ipv6.pl"

dir-listing.encoding        = "utf-8"
server.dir-listing          = "enable"

compress.cache-dir          = "/var/cache/lighttpd/compress/"
compress.filetype           = ( "application/x-javascript", "text/css", "text/html", "text/plain" )

include_shell "/usr/share/lighttpd/create-mime.assign.pl"
include_shell "/usr/share/lighttpd/include-conf-enabled.pl"
```

Once edited, lighttpd needs restarting:

`sudo /etc/init.d/lighttpd restart`

###### Step 3 - set the grails root 

Each Grails instance has a root application context, this is the root directory of the app. By default it is set to /, however we need to set each instance to match the URL we assigned it in the lighttpd.config file. To do this, go into each of your Grails application, and edit the Config.groovy file (found within `Badger/grails-app/conf/`). Edit the grails.app.context line to match the URL set in the lighttpd.config file, e.g.

`grails.app.context = "/demo"`

###### Step 4 - set Grails working directory

To avoid any confusion Grails may encounter when running multiple instances, it is important to set the working directory for each application. To do this, each time a Grails command is run, a parameter needs to be set to state where the working directory is. For example, when running the application, an extra statement is required:

```
#instead of just
grails prod -Dserver.port=8082 run-app
#you need
grails prod -Dgrails.work.dir='/path/to/grails/demo/' -Dserver.port=8082 run-app
```

This also needs to be done for the run_me.sh scripts, e.g.

```
#!/bin/bash

date
grails prod -Dgrails.work.dir='/path/to/grails/demo/' run-script \
scripts/AlterTables.groovy \
scripts/AddPublicationData.groovy \
scripts/AddSequenceData.groovy \
scripts/AddOrthoData.groovy \
scripts/AddGeneBlastData.groovy \
scripts/AddGeneFuncAnnoData.groovy \
scripts/CreateFiles.groovy
date
```

Now, each of your instances should run without affecting the other. There may be some issues with the auto publication updates which i'm looking into and you also need to be aware that each instance will require at least 1 CPU and 2-4 GB RAM.

## Data rules

1. All chromosome/scaffold/contig IDs must be unique across all data sets, e.g. Mmeles_v1.0_scaffold_1. A script is provided to generate unique headers for each sequence:

```bash
/path/to/Badger/scripts/perl/rename_files.pl --genome 'xxx.fa' --genomeID 'unique_prefix' 
``` 

This will create a new version of the fasta file called `xxx.fa.renamed` which should be used in place of the original file. Note, that these new IDs also need to match those in column 1 of the GFF3 file (see rule 3). 

This script uses three perl scripts (`map_fasta_ids`, `maker_map_ids` and `map_gff_ids`) which are originally from MAKER2 - [MAKER2: an annotation pipeline and genome-database management tool for second-generation genome projects](http://www.biomedcentral.com/1471-2105/12/491).  

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
3. All gene IDs must be unique across all data sets, e.g. Mmeles_v_1.0_g_1 / Mmeles_v_1.0_t_1 and match the ID in the GFF3 file. To rename the gene IDs use `rename_files.pl`:

```bash
/path/to/Badger/scripts/perl/rename_files.pl --genome 'genome.fa' --genomeID 'unique_prefix' --gff 'genes.gff' --gffID 'unique_prefix' --trans 'transcripts.fa' --prot 'proteins.fa'
```

Note, that all annotations must now be performed using the renamed proteins fasta files.

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

1) Errors relating to the postgres indexing such as:

```
| Error 2013-01-24 15:35:53,254 [localhost-startStop-1] ERROR context.GrailsContextLoader  - Error executing bootstraps: org.h2.jdbc.JdbcSQLException: Unkno
wn data type: "TSVECTOR"; SQL statement:
ALTER TABLE trans_anno ADD COLUMN textsearchable_index_col tsvector; [50004-164]
```

Generally means grails can't link to the postgres database, so check your badger.config.properties file carefully

2) Errors with this:

```
org.codehaus.groovy.grails.orm.hibernate.exceptions.CouldNotDetermineHibernateDialectException: Could not determine Hibernate dialect for database name [PostgreSQL]!
```

Could be related to the version of open JDK, if using v 1.6 try installing v 1.7, e.g.

```
sudo apt-get install openjdk-7-jre
```


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

Error:

```
| Error Error executing script RunScript: Java heap space (Use --stacktrace to see the full trace)
```

Java may require more memory, add this to your .bashrc/profile and edit according to how much memory you need/have.

```
export GRAILS_OPTS="-XX:MaxPermSize=8192M -Xmx8192M -server"
```

#### Missing sequence!

If there is an error saying something like this:

```
Missing sequence! - augustus_masked-nMf.1.1.scaf40720-processed-gene-0.0-mRNA-1 is not found in the FASTA files!
Missing sequence! - augustus_masked-nMf.1.1.scaf24728-processed-gene-0.0-mRNA-1 is not found in the FASTA files!
Missing sequence! - snap_masked-nMf.1.1.scaf09457-processed-gene-0.3-mRNA-1 is not found in the FASTA files!
Missing sequence! - snap_masked-nMf.1.1.scaf43196-processed-gene-0.2-mRNA-1 is not found in the FASTA files!
Missing sequence! - maker-nMf.1.1.scaf13556-augustus-gene-0.5-mRNA-1 is not found in the FASTA files!
Missing sequence! - genemark-nMf.1.1.scaf41595-processed-gene-0.0-mRNA-1 is not found in the FASTA files!
```

during the GFF3 loading, perhaps the ID of the gene/transcript does not match the header on the FASTA files. 

#### Unknown

Any changes to a config file or controller will only take effect once grails has been stopped and restarted, so maybe give that a go :) 
