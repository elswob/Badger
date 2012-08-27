<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>


<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name='layout' content='main'/>
    <title>Home</title>
  <parameter name="home" value="selected"></parameter>
</head>
<body
  <h1>
    We are sequencing the genome of <i>Bicyclus anynana</i>
  </h1>
<sec:ifNotLoggedIn>
  <!--form method="POST" action="${resource(file: 'j_spring_security_check')}">
    <table>
      <tr>
        <td>Username:</td><td><g:textField name="j_username"/></td>
      </tr>
      <tr>
        <td>Password:</td><td><input name="j_password" type="password"/></td>
      </tr>
      <tr id="remember_me_holder">
        <td>
      <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
      <label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>
      </td>
      </tr>
      <tr>
        <td colspan="2"><g:submitButton name="login" value="Login"/></td>
      </tr>
    </table>
  </form-->
</sec:ifNotLoggedIn>
<sec:ifAllGranted roles="ROLE_ADMIN">
  <h1>Welcome <sec:username />. You are logged in as admin</h1>
</sec:ifAllGranted>
<sec:ifAllGranted roles="ROLE_USER">
  <h1>Welcome <sec:username />. You are logged in as user </h1>
</sec:ifAllGranted>
<sec:ifLoggedIn>
  More details about the project for those logged in....
</sec:ifLoggedIn>
</body>
</html>