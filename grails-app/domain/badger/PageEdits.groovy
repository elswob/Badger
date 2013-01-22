package badger

class PageEdits {
    String page
    String edit
    Date dateString
    static constraints = {
        page(blank:false)
        edit(blank:false)
        dateString(blank:false)
    }
    static mapping = {
        edit type: "text"
    }
}
