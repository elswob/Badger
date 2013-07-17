#!/usr/bin/perl -w
use strict;
use warnings;
use Getopt::Long;

my $file = "";
my $prefix = "";

GetOptions (
    "file=s"      => \$file,
    "prefix=s"      => \$prefix,
);

if ($file eq "" || $prefix eq ""){
        die("rename_fasta.pl -f fasta file -p prefix\n");
}

my $count = 1;
open FILE, $file or die;
open OUT, ">$file.renamed";
while(<FILE>){
	chomp;
    if ($_ =~ /^>(.*)/){
        if ($count == 1){
            print OUT ">$prefix"."_$count\n";
        }else{
            print OUT ">$prefix"."_$count\n";
        }$count++;
    }else{
        print OUT "$_\n";
    }
}
