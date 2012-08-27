#!/usr/bin/perl -w

use strict;
use warnings;
use LWP::Simple;

my $utils = "http://www.ncbi.nlm.nih.gov/entrez/eutils";
my $db     = ask_user("Database", "Pubmed");
my $query  = ask_user("Query",    "Bicyclus anynana");

my $query_join="";
my @a = split(' ' ,$query);
foreach (@a){
	$query_join .= "$_+AND+";
}
$query_join = substr($query_join, 0, -5);

my $esearch = "$utils/esearch.fcgi?db=$db&retmax=100000&term=$query_join";
print "Getting IDs...\n";
print "$esearch\n";
my $esearch_result = get($esearch);

my @lines = split /\n/, $esearch_result;
my $efetch_ids;
foreach my $line (@lines) {
	if ($line =~ /.*?<Id>(\d+)<\/Id>/){
		$efetch_ids .= $1.", ";
	}
}
$efetch_ids = substr($efetch_ids,0,-2);

my $efetch = "$utils/efetch.fcgi?db=$db&id=$efetch_ids&retmode=xml";
print "Fetching data...\n";
print "$efetch\n";
my $efetch_result = get($efetch);


#get the date for the file name
my $date = localtime();
my @v = split(' ',$date);
$date = "$v[1]_$v[2]_$v[4]"; 

print "Printing to $query_join.$db.$date.xml\n";
open P,">$query_join.$db.$date.xml";
print P "$efetch_result\n";

# Subroutine to prompt user for variables in the first section
sub ask_user {
  print "$_[0] [$_[1]]: ";
  my $rc = <>;
  chomp $rc;
  if($rc eq "") { $rc = $_[1]; }
  return $rc;
}
