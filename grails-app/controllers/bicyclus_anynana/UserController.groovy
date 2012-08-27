package bicyclus_anynana
import grails.plugins.springsecurity.Secured
@Secured(['ROLE_ADMIN'])
class UserController extends grails.plugins.springsecurity.ui.UserController {
}
