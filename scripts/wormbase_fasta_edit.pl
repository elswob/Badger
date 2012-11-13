#!/usr/bin/perl -w

use strict;
use warnings;

#>BM22324        Bm1_00015A      Bm1_00015A
#ATGGAAGTTGGTAGTATATCATTGGAATGTGGTTCAGTACCACCTTTTCCGGTTACCGGT
#ATTAATTATTTACCAATTCCGTCATGTCGACAATCTTCATCATCCTCAGCACATGGTTCA

while(<>){
	chomp;
	if ($_ =~ /^>.*?\t(.*?)\t.*/){
		print ">$1\n";
	}elsif ($_ =~ /^>(.*?)\s.*/){
		print ">$1\n";
	}else{
		print "$_\n";
	}
}

