<!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>jQuery LoadMask Demo</title>
                <link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery.loadmask.css')}" type="text/css"></link>
		<script type="text/javascript" src="http://code.jquery.com/jquery-latest.pack.js"></script>
                <script src="${resource(dir: 'js', file: 'jquery.loadmask.min.js')}" type="text/javascript"></script>

		<style>
			body{font-size:11px;font-family:tahoma;}
			#content { padding:5px;width:200px; }
			#buttons { padding-left:40px; }
		</style>

	</head>
	<body>
          
		Please fill out the form:
		<div id="content">
			<div>Field1: <input type="text"></div>
			<div>Field2: <input type="text"></div>
			<div>Field3: <input type="text"></div>
			<div>Field4: <input type="text"></div>

		</div>
		<div id="buttons">
			<input type="button" value="Process" id="process">
			<input type="button" value="Cancel" id="cancel">
		</div>
		
		<script>
			$(document).ready(function(){
				$("#process").bind("click", function () {
					$("#content").mask("Waiting...");
				});
				
				$("#cancel").bind("click", function () {
					$("#content").unmask();
				});
			});
		</script>
	</body>
</html>