<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!-- Spring Form Tag Library -->
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="main.css">
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="font-awesome/css/font-awesome.min.css" />

<script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>



<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Log In</title>
</head>
<body>

	<nav class="navbar navbar-default" role="navigation">
	<div class="container"> <!-- Trying to center these things up top! -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand purpleClass" href="#">needaJob</a>
		</div>
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			
			<div class="col-sm-3 col-md-3 pull-right"></div>
		</div>
	</div>
	</nav>



	<div class="container" style="margin-top: 40px">
		<div class="row">
			<div class="col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-4 dropMed">
				<div class="panel panel-default">
					<div class="panel-heading">
						<strong> Login with your Email and Password!</strong>
					</div>
					<div class="panel-body">

						
						<div class="row">
							<div class="col-sm-12 col-md-10  col-md-offset-1 ">
							<div><center>
									<img class="profile-img centeredObject thumb"
										src="images/thumbs.ico" alt="">
								</center></div>


								<form:form action="verifyPassword.html" method="POST">

									<div class="form-group">
										<div class="input-group">
											<span class="input-group-addon"> <i
												class="glyphicon glyphicon-user"></i>
											</span>
											<form:input  type="text" placeholder="Email Address"
												class="form-control" path="email" ></form:input>
											<%-- 													<form:errors path="email" cssClass="error" /> --%>
										</div>
									</div>


									<div class="form-group">
										<div class="input-group">
											<span class="input-group-addon"> <i
												class="glyphicon glyphicon-lock"></i>
											</span>
											<form:input  placeholder="Password" class="form-control"
												path="password" type="password" ></form:input>
											<%-- 													<form:errors path="password" type="password" --%>
											<%-- 														cssClass="error" /> --%>
										</div>
									</div>
									<div class="form-group">
										<input type="submit" class="btn btn-lg btn-success btn-block"
											value="Submit">
									</div>
								</form:form>



							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>