package bicyclus_anynana
import grails.plugins.springsecurity.Secured
@Secured(['ROLE_ADMIN'])
class RegisterController extends grails.plugins.springsecurity.ui.RegisterController {
}
