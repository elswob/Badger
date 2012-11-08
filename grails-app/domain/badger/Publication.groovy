package badger

class Publication {
	int pubmedId
    String journal
    String journal_short
    String volume
    String issue
    String title
    String authors
    String abstractText
    Date dateString
    String doi
    static constraints = {
        pubmedId(blank:false)
        journal(blank:false)
        journal_short(blank:false)
        volume(blank:false)
        issue(blank:false)
        title(blank:false)
        authors(blank:false)
        abstractText(blank:false)
        dateString(blank:false)
        doi(blank:false)
    }
    static mapping = {
        abstractText type: "text"
        title type: "text"
        authors type: "text"
        journal type: "text"
    }
    static belongsTo = [ meta: MetaData ]
}
