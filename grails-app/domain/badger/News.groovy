package badger

class News {
    String titleString
    String dataString
    Date dateString
    static constraints = {
        titleString(blank:false, unique: true)
        dataString(blank:false)
    }
    static mapping = {
        titleString type: "text"
        dataString type: "text"
    }
}
