<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	  <meta name='layout' content='main'/>
	  <title>Bicyclus anynana genome home</title>
	  <parameter name="home" value="selected"></parameter>
	  <ckeditor:resources/>
	  
        <script type="text/javascript">
            delete CKEDITOR.instances[ 'myeditor}' ];

            function CKupdate() {
                for (instance in CKEDITOR.instances) {
                    CKEDITOR.instances[instance].updateElement();
                }
            }
        </script>
	  
  </head>

  <body>
		Edits for ${params.pageName}
		${params.edits}	
  </body>
</html>
