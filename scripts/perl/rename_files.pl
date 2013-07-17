#!/usr/bin/perl -w
use strict;
use warnings;
use Getopt::Long;

my ($genome,$genomeID,$gff,$gffID,$trans,$prot) = "";

GetOptions (
    "genome=s"      => \$genome,
    "genomeID=s"      => \$genomeID,
    "gff=s"      => \$gff,
    "gffID=s"      => \$gffID,
    "trans=s"      => \$trans,
    "prot=s"      => \$prot,
);

if ($genome eq "" || $genomeID eq ""){
        die("rename_files.pl\n --genome genome_fasta_file\n --genomeID unique ID for genome\n --gff gff3 file\n --trans transcript fasta file\n --prot protein fasta file\n --gffID unqiue ID for genes\n");
}

use Cwd qw/abs_path/;
my ($path) = abs_path($0) =~ m/(.*)rename_files.pl/i;

if (!-d "renamed"){
	`mkdir renamed`;
}

print "Renaming genome fasta file...\n";
my $count = 1;
my %gmap;
open G, $genome or die;
open OG, ">renamed/$genome.renamed";
while(<G>){
	chomp;
    if ($_ =~ /^>(.*)/){
        if ($count == 1){
            print OG ">$genomeID"."_$count\n";
        }else{
            print OG ">$genomeID"."_$count\n";
        }
        $gmap{$1} = "$genomeID"."_$count";
        $count++;
    }else{
        print OG "$_\n";
    }
}

#for my $k (keys %gmap){
#	print "$k -> $gmap{$k}\n";
#}

if ($gff ne ""){
	print "Renaming GFF3 file...\n";
	`$path/maker_map_ids --prefix=$gffID --abrv_gene -g --abrv_tran -t --justify 5 $gff > $gff.map 2> /dev/null`;
	`cp $gff $gff.original`;
	`$path/map_gff_ids $gff.map $gff 2> /dev/null`;
	`mv $gff $gff.renamed`;
	`mv $gff.original $gff`;
	
	open Og, ">renamed/$gff.renamed";
	open GFF, "$gff.renamed" or die;
	my @a;
	while(<GFF>){
		chomp;
		@a = split("\t",$_);
		if (scalar @a == 9){
			if (exists $gmap{$a[0]}){
				print Og "$gmap{$a[0]}\t$a[1]\t$a[2]\t$a[3]\t$a[4]\t$a[5]\t$a[6]\t$a[7]\t$a[8]\n";;
			}
		}else{
			print Og $_;
		}
	}
	`rm $gff.renamed`;
	
	print "Renaming transcript fasta file...\n";
	`cp $trans $trans.original`;
	`$path/map_fasta_ids $gff.map $trans`;
	`mv $trans renamed/$trans.renamed`;
	`mv $trans.original $trans`;	
	
	print "Renaming protein fasta file...\n";
	`cp $prot $prot.original`;
	`$path/map_fasta_ids $gff.map $prot`;
	`mv $prot renamed/$prot.renamed`;
	`mv $prot.original $prot`;
}