package bicyclus_anynana

class BlastService {

    def runBlast(def blastId) {
    //def runBlast (){
        println "running blast $blastId"
        def p = "/Users/Ben/Work/software/blast/ncbi-blast-2.2.23+/bin/blastn -db /Users/Ben/Work/data/lumbribase.fasta -outfmt 6 -num_threads 1 -query /Users/Ben/Work/data/test.fa".execute()
        //def p = "/Users/Ben/Work/software/blast/ncbi-blast-2.2.23+/bin/blastn -db /Users/Ben/Work/data/lumbribase.fasta -outfmt 6 -num_threads 1 -query /Users/Ben/Work/data/lumbribase.fasta".execute()
        //def p "/Users/Ben/Work/software/blast/ncbi-blast-2.2.23+/bin/blastn -db /Users/Ben/Work/data/lumbribase.fasta -outfmt 6 -num_threads 1 -query $blastId"
        p.waitFor()
        println "finished BLAST"
        // Get output from process
        //println p.text
        redirect(action: 'blast_results')
        //return [blast_result: p.text]        
    }
}
