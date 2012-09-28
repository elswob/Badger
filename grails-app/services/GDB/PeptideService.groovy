package GDB

class PeptideService {
	def getComp(def seq){
		def aaInfo = [:]
		aaInfo.G = "Glycine (Gly)"
		aaInfo.P = "Proline (Pro)"
		aaInfo.A = "Alanine (Ala)"
		aaInfo.V = "Valine (Val)"
		aaInfo.L = "Leucine (Leu)"
		aaInfo.I = "Isoleucine (Ile)"
		aaInfo.M = "Methionine (Met)"
		aaInfo.C = "Cysteine (Cys)"
		aaInfo.F = "Phenylalanine (Phe)"
		aaInfo.Y = "Tyrosine (Tyr)"
		aaInfo.W = "Tryptophan (Trp)"
		aaInfo.H = "Histidine (His)"
		aaInfo.K = "Lysine (Lys)"
		aaInfo.R = "Arginine (Arg)"
		aaInfo.Q = "Glutamine (Gln)"
		aaInfo.N = "Asparagine (Asn)"
		aaInfo.E = "Glutamic Acid (Glu)"
		aaInfo.D = "Aspartic Acid (Asp)"
		aaInfo.S = "Serine (Ser)"
		aaInfo.T = "Threonine (Thr)"
		
		def aaCount = [:]
		def splitter = seq.split('')
		splitter.each { letter->
			if (aaCount."${letter}"){
				aaCount."${letter}" += 1
			}else{
				 aaCount."${letter}" = 1
			}
		}
		def aaData = []
		aaCount.each{
			if (aaInfo."${it.key}"){
				//def aa = [(it.value/seq.length())*100,"'"+aaInfo."${it.key}"+"'"]
				def aa = [it.value,"'"+aaInfo."${it.key}"+"'"]
				aaData.add(aa)
			}
		}
		return aaData
	}
}
