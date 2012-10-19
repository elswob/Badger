package GDB

class PeptideService {
	def getComp(def seq){
		def aaInfo = [:]
		//non-polar
		aaInfo.P = "Proline (Pro) [P]"		
		aaInfo.A = "Alanine (Ala) [A]"
		aaInfo.V = "Valine (Val) [V]"
		aaInfo.L = "Leucine (Leu) [L]"
		aaInfo.I = "Isoleucine (Ile) [I]"
		aaInfo.M = "Methionine (Met) [M]"
		aaInfo.F = "Phenylalanine (Phe) [F]"
		aaInfo.W = "Tryptophan (Trp) [W]"
		//polar
		aaInfo.G = "Glycine (Gly) [G]"
		aaInfo.S = "Serine (Ser) [S]"
		aaInfo.T = "Threonine (Thr) [T]"
		aaInfo.C = "Cysteine (Cys) [C]"
		aaInfo.Y = "Tyrosine (Tyr) [Y]"
		aaInfo.N = "Asparagine (Asn) [N]"
		aaInfo.Q = "Glutamine (Gln) [Q]"
		//acidic
		aaInfo.H = "Histidine (His) [H]"
		aaInfo.K = "Lysine (Lys) [K]"
		aaInfo.R = "Arginine (Arg) [R]"
		//basic
		aaInfo.E = "Glutamic Acid (Glu) [E]"
		aaInfo.D = "Aspartic Acid (Asp) [D]"
		
		def aaCount = [:]
		aaInfo.each {
			aaCount."${it.key}" = 0
		}
		//println "aaCount = "+aaCount
		def splitter = seq.split('')
		splitter.each { letter->
			if (aaCount."${letter}"){
				aaCount."${letter}" += 1
			}else{
				 aaCount."${letter}" = 1
			}
		}
		def aaData = [], pData = [], npData = [], aData = [], bData = []; 
		aaCount.each{
			if (aaInfo."${it.key}"){
				//def aa = [(it.value/seq.length())*100,"'"+aaInfo."${it.key}"+"'"]
				def aa = [it.value,"'"+aaInfo."${it.key}"+"'"]
				if (it.key =~ /P|A|V|L|I|M|F|W/){
					npData.add(aa)
				}
				else if (it.key =~ /G|S|T|C|Y|N|Q/){
					pData.add(aa)
				}
				else if (it.key =~ /H|K|R/){
					aData.add(aa)
				}
				else if (it.key =~ /E|D/){
					bData.add(aa)
				}
				//aaData.add(aa)
			}
		}
		aaData.add(npData)
		aaData.add(pData)
		aaData.add(aData)
		aaData.add(bData)
		//aaData = aaData.sort { it[1] }
		return aaData
	}
	
	def getPep(seq, int frame){
		def trans = [ ATT:'I',ATC:'I',ATA:'I',CTT:'L',CTC:'L',CTA:'L',CTG:'L',TTA:'L',TTG:'L',GTT:'V',GTC:'V',GTA:'V',GTG:'V',TTT:'F',TTC:'F',ATG:'M',TGT:'C',TGC:'C',GCT:'A',GCC:'A',GCA:'A',GCG:'A',GGT:'G',GGC:'G',GGA:'G',GGG:'G',CCT:'P',CCC:'P',CCA:'P',CCG:'P',ACT:'T',ACC:'T',ACA:'T',ACG:'T',TCT:'S',TCC:'S',TCA:'S',TCG:'S',AGT:'S',AGC:'S',TAT:'Y',TAC:'Y',TGG:'W',CAA:'Q',CAG:'Q',AAT:'N',AAC:'N',CAT:'H',CAC:'H',GAA:'E',GAG:'E',GAT:'D',GAC:'D',AAA:'K',AAG:'K',CGT:'R',CGC:'R',CGA:'R',CGG:'R',AGA:'R',AGG:'R',TAA:'.',TAG:'.',TGA:'.' ];
		def pepSeq = ""
  		int i = frame
		while (i + 2 < (seq.length())){
      		if (trans[seq[i..i+2]]){
          		pepSeq += trans[seq[i..i+2]]
        	}
      		i += 3
    	}
  		return pepSeq
	}
}
