package GDB

import groovy.sql.Sql
def sql = new Sql(dataSource)
//
//add the gene info
def infoMap = [:]
infoMap.contig_id = 'contig_2'
infoMap.coverage = ' 100.1'
infoMap.gene_id = 'k_14761'
infoMap.intron = '3'
infoMap.nuc = 'ATGGCGAAGATTGTTAATGGGAATCATGACGACGAGCATGGGTCGGTGGTCTGGCTCTGGATTCTCATTTCATGCGTGGATCTGTCTGGAGAATCCACCTTCTTCTCTCGGGGAATTTGGTTTGGCCAGGAGACAGCTTTACAAATGTCTGGTGGAAGTGGTTTTGAGGCGTTCTTTACGTCTGAGGCTGTCCTTGTTGTGTCTGTCTTCTCAAAAAAGGAACGCAATGACATTCCTGTAATGGACTGCCCTATTGTCTGCAACAAATGGTACTGCATAGACGTTGTGCATACGTCATCGAAGCGACCTTTTGGACAGAGTCAGCTACCGTCGTCTGCGCAGTCGGTTGCGGAATCGGATGGCGGGGGGCATGGAGAAGGACGAGAGGTGCGACATGGGTCGGTTGTTTCCTTGTTTGGTCAACTTTCCGAAGTCATCATCTTCATGGATTCGCTTATTGATACGCATGTTCAGTCCCTTTATCGATTAGGTCCTAACAGACATCTGAGCCAGATGAAGGATGGCGGGGTTCCAGCTGATCTACCTGTAAAGCCACTTTTGGTGTATCATGCAAAGTTTCCAGAAACATGTTTAGATCTGTCAGGCAATGGTCATCATGCAAAAGTTGCTGGACATTTATGTTCAGCCAGCAACATTAAGGAAATGATCAACACCATTGGTGGTGTCCAGCTACTCTACCTTCTTCTGGAAGAGGCAGCAGTACGACCTGGTGCCAGTTCAGACTTGATCAGTTTGGAAGATGATCGGACAAGGAACAATGGCCGGCTTAAGGAAGACATGGACACACTGGAGCCTTTCTTTGTCCTGCTTCGTTCCTTCATTCAAAGTTGTCCGATCAACCAAGAGGCCTTATTACGTAACGATGGCATTGCTGCATTGGGAGTCATTCTGCAAAAGTATCTGCAGGCTGTCATAAAGGAAGATCGGAAGTATTTTCGAAAGAAGTATGGTGTGCAATTTTTCCTGGATGCGGTTCGCATAACGCCATCCTCAACTCATCTGACCAGTGACAACAAGCAGGAGATTCGTGCTGAACTTCTGGACATCGTACACTCGTATGTTGCCCGTGAAATTAATGTTGCCGAACTCAGCCATATCTTGGCCTTTGTTGCAGCTGTGCAAGATGAAGTATTGGTCAGTGATGTCCTTGGTATTGTGAAATCTCTGCTTGAGGCTCGTGGCAAGAGAGACCAGATCTACTTGTTGCTGTTTGAACCAGGCATGGCAGATATGCTGTACGTGCTTTTAACCATCAAGGACTATTCAGTGGACCTCAAAGAGAAAAATTTTTCAGAATCTTCCGCTAGCATCAATGCAGCCATCTACATCATTCAAACTCTTCATCTCTGTGATGTTGATGTGAAACTGGAGGCTGTTCGCCAGCTCATGAAAGTCCTTCTGTCGAAGTCAACGATTCCAAAGCTGTTTGCAAAGCAACTTGGTTGGCAGATCGCTTTGATCCGTCTGCTCATCCGTCGTCCGTCTCAATCGACGGGCAGCAAATGGCGATCTAGTTTCGACGACGGTGAGTCGCAGCAGCGTCTGGTCCAGCGTGCGGATTCTGTTTATGAGGATCCACCGGAGCGACTCATAGACCTACCGCAAGACGACGAACCTTCGTTTGACCCCAAAGCCGAAGATGACGACCTCCTCATGAACAAGTTTCCACTTTGCTCAGATTTTGATCCGTATGCCTTCCCGGATGGAAGAAGCAGTCGTAGCTTCAGCATTGTGAGTTGTCCTGACTCGGGAGACGACGCGGGACTCAGCTTCAAGGAGCCGACGCGCAGCACAAGTACGGTTAGCAGTCTGTCGTTGGGTTGTGACAGTCGATCGAGGGCGCTGTTTAAAGAGGGAAGTGAAAGCAGTGCGGTGATGATGGCGTTGAAGGAACTTGGTTTGACGGTGAAACCTTGCGCAGAGAACATGGAGAAGACAGAGGAGCTGTGTCTCAATCTGTTGATTGTTCTCTTCCTCATTCTCTGGAAAGGCATTGACCAAGCGGACGAGACAGCTTGGCAGCAAAGAGGACAAGTGTTTGCTGCCATTACTGACACCGGTGAAGACAATGAATTACTTATGTCAAAGACAGAACTGTTGCGAAAGTTATTGGAGCTTCTTCTTCAAGGGTGCCTTATAGATATTCAAGATACAGGTGTTTCAAATGTTAACAATTCAAGGAATGCACTCAACCTCATGAAACTTTTATATCAGTTTTTGTGCATGGACAATGGACTGCCAGCATTGAAGTTTAGCAAGCAGCTGCTTGAAGATCTGATGGTGCTGTTGGAGGATTTTTCCATTTGGCAGACTGGTTGTGAGTGGAAAGAGATGGAACAGATGTCTCTGCGCTGTCTTCTGGCTTTCGCAGGACAGGACAACTCGGATTTGTGTGGCATTGCTGCGGCCCGTCTTCATGCAATGGTTCGTGATCGACCTGTGGAAAAGTTCCAGGAATCTTGTTTCCTCGTTGGAACCATAGATGACATACTGGAGAGGGCGCTGCAAGGCCATGGAGATCAGTATGGTTTCCTGGTCCAAGTGATGAAGTCACTGCTGGAGAAATGTGACGACCTCTTGAACATACCGTATTATCTGCCATCTCTGCCTTCCGTCCGCAACAGTACGTCCTTCATGGAAGCGTTCAAGACGTACAGTCGTTCCGACGAGTGGATTGCCTTCATCAAAAAACAAGTGCAACCTTCAAAGGATCAGTACCTGGTGAATATCTACGAGGGACTTCGTATCCAAATGACATCATTCATGGGTGAATGCCATGAGGAAATGATGGTTGCTGGGCATAAAAGAAATCGAGAAAAAGGGGAAGCAAAGCTCAAGAGGAGTTTGTGTCAACTTTTAAAAGACTGCTCAAGACAAGAGAATAAACGATTCAAGAGTATTCGACTTCAGCTGCATAATCAGAGTTTGGCCGCTTGGCGTCAGTGGACTTTGACTAAATCGTTCTTCACCAGTGAAAGAGGAATGTGGCCAGATAAAAACACATTTCCGACGCACTGGAAATCGTCTCTGAATGAAAACTATTCAAGAATGCGACCAAAAATGATTCCAAATGAAAAATTTGAGAACCATTTGGATGCTAGCAGACTTCGAGATAATGAAGCGTCTGAGGAGAGCAATCTGGCCGTTTCTGCGTTGTCGATTATGAACGAGGTGCGAGCAAAAGAATCCGCTGGAGATGATCGACTTGAAGATGACGACTGGATTCTCATAAGCAGCACTGATTCAGCAAATGTGCCGACGTCGAGCAAGGAATCGATGCTTTCAGAAGAATGTGAGCTTGTCACAATAACCGAAGTGATCAAGGGTCGTCTCGACGTCACGACGAGTCACATCTACTTCTTTGACTGTACTCCGCTTAGAGAGGATGGAGGAATCGTCGACCTCAAGTGGTCACTCGATGAAATCAAAGAGATCCACTTTCGACGGCACAACCTGCAAAGGCGCGCTCTCGAGTTCTTCCTTGTCGACCAGACCAACTATTTCATCAATTTTCAGAAAAAGGTTCGAAATAAAGTCTACTCGAGAATTTTAGCTCTTCGGCCGCCAAACTTGATTTACAATCAGTCAAGATCTCCAGCAGAACTTCTAAAAGCATCTGGACTAACTCAGAAATGGGTCCAGCAGGAGATTAGTAATTTCGATTACCTGATGTGTCTGAATACAATAGCTGGACGCACTTACAATGACCTCAGCCAATACCCTGTGTTTCCATGGATTCTTGCTGACTACAATTCGGAGACTTTGGACCTCAATGATCCACGAAGTTATCGTGATTTGTCACTCCCTATCGGGGTTTCTAACCCTAAGAATGAGAAACAAGTCCGAGAAAAATATGAGAACTTTGAAGACCCGACTGGAACAATTGCTAAGTTCCACTATGGTACGCATTATTCAAATCCCGCTGGAGTGATGCACTACCTGATACGCATTGAACCATTCACTACACTGCACATTCAACTGCAGAGTGGCAGGTTTGATGTGGCTGATCGACAGTTTAACTCGGTGCCTGCAATGTGGGACGGTCTTATGGACAACCCAAACGATGTCAAGGAACTCATTCCAGAGTTCTTTTATCTGCCGGAATTCCTTGTCAACTTGAATGATTTTGATCTTGGGCGACTACAGTTTGGAGATGGAAGAGTGGACGATGTTATCCTACCACGATGGGCGTCTACACCCTATGAATTTATCCATAAGCATAGGATGGCTTTGGAGTCCGATTATGTTTCAGAACATTTGCACAACTGGATTGACTTGATCTTTGGTTACAAGCAGCAAGGTCCTGCGGCGATTGAAGCGTTGAATGTCTTCTATTACTGTACATACGAAGGAGCCGTTGATCTTGACACTGTTACCGATGAGAAGAGAAGACGTGCATTTGAAGGCATGATCAACAACTTTGGTCAAACACCTTGTCAACTTCTTAAGGAGCCGCATCCAAAGAGAATGACGCTTCTGGAAATTACGTCACGCGTAACAAAACCAGAACGTCTCTTGAAAGTTTTCCTCTTTCCAGACAAACTGAAAACCTTCACTGTTGAGGCTGCACCACCGAATGACCCATTGGTGTTTGTTATGATACCTAGGAATCAGACAACTTCATTCATTTCACACTGGACAACCGAGATAATGGTTGCTGTTACTGAGAGTGGCATTGTGTACAGTCATGGATGGCTTCCATACGACAAAAACATCCCGGGCTTCTTTACTTTCGATCGTGACGTAACCATGAACAACAATAAAACAAAGAAGAAATTACCTGGACCGATGATGCCTGGTCTGAAAGTAACGTCCAAGATGTTCGCTTTGTCCCATGATGCAAAGCTCCTGTTCAGTTGTGGTCACTGGGACAATAGTCTGCGCGTGTATAACTTTGCACGCAGCAGACAGGTGGCACATGTCGTCAGACATCGAGATATAGTAACGTGTGTGTCGTTGGATCGTTCAAGTCGACGACTTATCTCTGGTTCACGTGATACGACTTGTATGATCTGGGAAATTGTGCACCAGGCTGGTGCCAGCTTTGGCATCAATCCACAGCCACTTCACGTTCTGTACGGCCACGACGACCACGTCACCTGCGTTGCCATAGCAACGGAACTGGACATGGCCGTGTCTGGTTCAAGGGATGGAACTGTGATTGTTCACACCGTTAAGCAGGGCACATACCTTCGAACGCTGAGGCCTCCGTATGAGAAAGGTTGGCAGTTGAATATACAGCTTCTGGCATTGTCCTACATGGGGCAGATCTGTGTTTACTGTCAGCATAGCCAAAGAAATTCAACAGGACATGAACTGGACAAGTTATCTCTTCATCTGTACTCGGTCAATGGGAAACACTTGTCAAAGGAGCTTCTTCCGTCACCAATTTCAGACATGGTCATTACGGGCGATCACCTCATCCTTGGCCATGCATGA'
infoMap.pep = 'MAKIVNGNHDDEHGSVVWLWILISCVDLSGESTFFSRGIWFGQETALQMSGGSGFEAFFTSEAVLVVSVFSKKERNDIPVMDCPIVCNKWYCIDVVHTSSKRPFGQSQLPSSAQSVAESDGGGHGEGREVRHGSVVSLFGQLSEVIIFMDSLIDTHVQSLYRLGPNRHLSQMKDGGVPADLPVKPLLVYHAKFPETCLDLSGNGHHAKVAGHLCSASNIKEMINTIGGVQLLYLLLEEAAVRPGASSDLISLEDDRTRNNGRLKEDMDTLEPFFVLLRSFIQSCPINQEALLRNDGIAALGVILQKYLQAVIKEDRKYFRKKYGVQFFLDAVRITPSSTHLTSDNKQEIRAELLDIVHSYVAREINVAELSHILAFVAAVQDEVLVSDVLGIVKSLLEARGKRDQIYLLLFEPGMADMLYVLLTIKDYSVDLKEKNFSESSASINAAIYIIQTLHLCDVDVKLEAVRQLMKVLLSKSTIPKLFAKQLGWQIALIRLLIRRPSQSTGSKWRSSFDDGESQQRLVQRADSVYEDPPERLIDLPQDDEPSFDPKAEDDDLLMNKFPLCSDFDPYAFPDGRSSRSFSIVSCPDSGDDAGLSFKEPTRSTSTVSSLSLGCDSRSRALFKEGSESSAVMMALKELGLTVKPCAENMEKTEELCLNLLIVLFLILWKGIDQADETAWQQRGQVFAAITDTGEDNELLMSKTELLRKLLELLLQGCLIDIQDTGVSNVNNSRNALNLMKLLYQFLCMDNGLPALKFSKQLLEDLMVLLEDFSIWQTGCEWKEMEQMSLRCLLAFAGQDNSDLCGIAAARLHAMVRDRPVEKFQESCFLVGTIDDILERALQGHGDQYGFLVQVMKSLLEKCDDLLNIPYYLPSLPSVRNSTSFMEAFKTYSRSDEWIAFIKKQVQPSKDQYLVNIYEGLRIQMTSFMGECHEEMMVAGHKRNREKGEAKLKRSLCQLLKDCSRQENKRFKSIRLQLHNQSLAAWRQWTLTKSFFTSERGMWPDKNTFPTHWKSSLNENYSRMRPKMIPNEKFENHLDASRLRDNEASEESNLAVSALSIMNEVRAKESAGDDRLEDDDWILISSTDSANVPTSSKESMLSEECELVTITEVIKGRLDVTTSHIYFFDCTPLREDGGIVDLKWSLDEIKEIHFRRHNLQRRALEFFLVDQTNYFINFQKKVRNKVYSRILALRPPNLIYNQSRSPAELLKASGLTQKWVQQEISNFDYLMCLNTIAGRTYNDLSQYPVFPWILADYNSETLDLNDPRSYRDLSLPIGVSNPKNEKQVREKYENFEDPTGTIAKFHYGTHYSNPAGVMHYLIRIEPFTTLHIQLQSGRFDVADRQFNSVPAMWDGLMDNPNDVKELIPEFFYLPEFLVNLNDFDLGRLQFGDGRVDDVILPRWASTPYEFIHKHRMALESDYVSEHLHNWIDLIFGYKQQGPAAIEALNVFYYCTYEGAVDLDTVTDEKRRRAFEGMINNFGQTPCQLLKEPHPKRMTLLEITSRVTKPERLLKVFLFPDKLKTFTVEAAPPNDPLVFVMIPRNQTTSFISHWTTEIMVAVTESGIVYSHGWLPYDKNIPGFFTFDRDVTMNNNKTKKKLPGPMMPGLKVTSKMFALSHDAKLLFSCGHWDNSLRVYNFARSRQVAHVVRHRDIVTCVSLDRSSRRLISGSRDTTCMIWEIVHQAGASFGINPQPLHVLYGHDDHVTCVAIATELDMAVSGSRDGTVIVHTVKQGTYLRTLRPPYEKGWQLNIQLLALSYMGQICVYCQHSQRNSTGHELDKLSLHLYSVNGKHLSKELLPSPISDMVITGDHLILGHA'
infoMap.rep = '1'
infoMap.source = 'Maker'
infoMap.start = '1000'
infoMap.stop = '5000'
new GeneInfo(infoMap).save()

//add the gene blast info
//

def addGeneBlast(blastFile,db){
    def annoMap = [:]
    annoMap.anno_db = db
    blastFile.eachLine { line ->
        if ((matcher = line =~ /<Iteration_query-def>(.*?)<\/Iteration_query-def>/)){
                annoMap.gene_id = matcher[0][1]
        }
        if ((matcher = line =~ /<Hit_id>(.*?)<\/Hit_id>/)){
                annoMap.anno_id = matcher[0][1]
        }
        if ((matcher = line =~ /<Hit_def>(.*?)<\/Hit_def>/)){
                annoMap.descr = matcher[0][1]
        }
        //get HSP info
        if ((matcher = line =~ /<Hsp_score>(.*?)<\/Hsp_score>/)){
                annoMap.score = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_query-from>(.*?)<\/Hsp_query-from>/)){
                annoMap.anno_start = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_query-to>(.*?)<\/Hsp_query-to>/)){
                annoMap.anno_stop = matcher[0][1]
        }
        //find an end of an HSP and save data
        if ((matcher = line =~ /<\/Hsp>/)){
            //println "$annoMap\n"
            new GeneAnno(annoMap).save()
        } 
    }
}

def inFile

//inFile = new File('data/sprot.out').text
//addGeneBlast(inFile, 'SwissProt')

//inFile = new File('data/nr.out').text
//addGeneBlast(inFile, 'nr')


//add the EST info

//add Unigene annotations
def addUnigeneBlast(blastFile,db){
    def annoMap = [:]
    def count_check = 0
    annoMap.anno_db = db
    blastFile.eachLine { line ->		
        if ((matcher = line =~ /<Iteration_query-def>(.*?)<\/Iteration_query-def>/)){
                annoMap.contig_id = matcher[0][1]
                count_check = 0
        }                	
        if ((matcher = line =~ /<Hit_num>(.*?)<\/Hit_num>/)){
                count_check++
        }
        if ((matcher = line =~ /<Hit_id>(.*?)<\/Hit_id>/)){
                annoMap.anno_id = matcher[0][1]
        }
        if ((matcher = line =~ /<Hit_def>(.*?)<\/Hit_def>/)){
                annoMap.descr = matcher[0][1]
        }
        //get HSP info
        if ((matcher = line =~ /<Hsp_score>(.*?)<\/Hsp_score>/)){
                annoMap.score = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_query-from>(.*?)<\/Hsp_query-from>/)){
                annoMap.anno_start = matcher[0][1]
        }
        if ((matcher = line =~ /<Hsp_query-to>(.*?)<\/Hsp_query-to>/)){
                annoMap.anno_stop = matcher[0][1]
        }
        //find an end of an HSP and save data
        if ((matcher = line =~ /<\/Hsp>/)){
            //only add set number of elements from each blast
            if (count_check < 10){
            	new UnigeneAnno(annoMap).save()
            }
        } 
    }
}

//inFile = new File('data/cap3_public_e5_b10_v10_sprot_blastx.out').text
//addUnigeneBlast(inFile, 'SwissProt')

//add the UniGenes
def contigFile = new File('demo/sequence.fasta.cap.contigs_and_singlets_renamed.fa').text
//def contigFile = new File('demo/test.fa').text
def sequence=""
def contig_id=""
def count=0
def count_gc

def contigMap = [:]
contigFile.split("\n").each{
    if ((matcher = it =~ /^>(.*)/)){
        if (sequence != ""){
            println "Adding $contig_id - $count"
            count++
            //get gc
            count_gc = sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
            def gc = (count_gc/sequence.length())*100
            gc = sprintf("%.2f",gc)
            //add data to map
            contigMap.contig_id = contig_id
            contigMap.gc = gc
            contigMap.length = sequence.length()
            contigMap.sequence = sequence
            contigMap.coverage = '0.0'
            new UnigeneInfo(contigMap).save()
            //println contigMap
            sequence=""
        }
        contig_id = matcher[0][1]
        count_gc = 0
    }else{
        sequence += it
    }
} 
//catch the last one
count_gc = sequence.toUpperCase().findAll({it=='G'|it=='C'}).size()
def gc = (count_gc/sequence.length())*100
contigMap.contig_id = contig_id
contigMap.gc = gc
contigMap.length = sequence.length()
contigMap.sequence = sequence
contigMap.coverage = '0.0'
new UnigeneInfo(contigMap).save()
