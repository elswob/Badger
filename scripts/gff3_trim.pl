#!/usr/bin/perl -w

use strict;
use warnings;

#Bmal_supercontig12391   WormBase        mRNA    391     2197    .       -       .       ID=transcript:Bm1_00005A;Parent=gene:Bm1_00005A;Name=Bm1_00005A;info=position:89-173 method:InterPro accession:IPR001715 description:Calponin homology domain %0Aposition:91-173 2.3e-13 method:ipi_humanP accession:ENSP00000419674 %0Aposition:46-173 1.4e-15 method:ipi_humanP accession:ENSP00000400164 %0Aposition:92-173 7.9e-13 method:ipi_humanP accession:ENSP00000258643 %0Aposition:92-173 7.3e-13 method:ipi_humanP accession:ENSP00000308493 %0Aposition:36-70 2e-13 method:ipi_humanP accession:ENSP00000325091 %0Aposition:93-173 2e-13 method:ipi_humanP accession:ENSP00000325091 %0Aposition:91-173 4.2e-12 method:ipi_humanP accession:ENSP00000309689 %0Aposition:92-173 8.4e-13 method:ipi_humanP accession:ENSP00000374447 %0Aposition:3-173 1.1e-14 method:ipi_humanP accession:ENSP00000399751 %0Aposition:10-173 1.5e-14 method:ipi_humanP accession:ENSP00000394965 %0Aposition:3-173 7.1e-15 method:ipi_humanP accession:ENSP00000394609 %0Aposition:3-173 1e-14 method:ipi_humanP accession:ENSP00000393579 %0Aposition:39-173 4e-16 method:ipi_humanP accession:ENSP00000395309 %0Aposition:72-173 3.6e-15 method:ipi_humanP accession:ENSP00000394763 %0Aposition:91-173 2.3e-13 method:ipi_humanP accession:ENSP00000419870 %0Aposition:23-173 7.3e-10 method:brepepP accession:CBN02793 %0Aposition:92-173 2.2e-13 method:slimswissprotP accession:P41737 %0Aposition:92-173 6.3e-12 method:slimswissprotP accession:Q921G6 %0Aposition:92-173 7.2e-13 method:slimswissprotP accession:P62046 %0Aposition:36-70 1.1e-12 method:slimswissprotP accession:Q3UMG5 %0Aposition:93-173 1.1e-12 method:slimswissprotP accession:Q3UMG5 %0Aposition:8-173 1.5e-13 method:slimswissprotP accession:Q8BVU0 %0Aposition:29-173 7e-09 method:wormpepP accession:C14F11.2 %0Aposition:29-173 1.3e-09 method:remaneiP accession:CRE00187 %0Aposition:1-171 1.6e-11 method:ppapepP accession:PPA26371 %0Aposition:1-173 9.5e-86 method:slimtremblP accession:A8NCR5 %0Aposition:14-173 1.2e-09 method:brigpepP accession:CBG05005 %0A;public_name=Bm1_00005A

while(<>){
	my @a = split("\t");
	if (@a == 9){
		if ($a[2] eq 'mRNA'){
			if ($a[8] =~ /ID=.*?:(.*?;Parent=.*?;).*/){
			#if ($a[8] =~ /(ID=.*?;Parent=.*?;).*/){
				print "$a[0]\t$a[1]\t$a[2]\t$a[3]\t$a[4]\t$a[5]\t$a[6]\t$a[7]\tID=$1\n";
			}
		}elsif ($a[2] eq 'gene'){
			if ($a[8] =~ /ID=.*?:(.*)/){
			#if ($a[8] =~ /(ID=.*?;Parent=.*?;).*/){
				print "$a[0]\t$a[1]\t$a[2]\t$a[3]\t$a[4]\t$a[5]\t$a[6]\t$a[7]\tID=$1\n";
			}
		}elsif ($a[2] eq 'CDS'){
			#ID=cds:BM06760;Parent=transcript:Bm1_00020A
			if ($a[8] =~ /ID=.*?:(.*?);Parent=.*?:(.*)/){
				print "$a[0]\t$a[1]\t$a[2]\t$a[3]\t$a[4]\t$a[5]\t$a[6]\t$a[7]\tID=$1;Parent=$2\n";
			}
		}
	}
}