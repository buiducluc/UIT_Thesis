<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Football QA System</title>
  <link rel="stylesheet" type="text/css" href="form.css">
 <script src="http://code.jquery.com/jquery-1.10.2.js"
    type="text/javascript"></script>
<script src="js/app-ajax.js" type="text/javascript"></script>
<link rel="stylesheet" href="form.css"/>
</head>
<body >

    <form class="form-wrapper cf">
         	<input type="text" id="userName" placeholder="Search here..." required>
			<button type="button" onclick="run()" >Search</button>
    </form>
   
   <div  align="center" >
    <textarea id="ajaxGetUserServletResponse"   ></textarea>
    </div>
</body>
</html>