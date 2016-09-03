<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="main.css">



<script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
			<a class="navbar-brand" href="#">needaJob</a>
		</div>
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			
			<div class="col-sm-3 col-md-3 pull-right"></div>
		</div>
	</div>
	</nav>
	
		<div class="container drop">
			<div class="row">
				<form action="welcome.html">
					<br>
					<div class="col-md-4 col-md-offset-4 all">
					<h1>
						<center>needaJob</center>
					</h1>
						<input class="form-control" type="text" name="query"
							placeholder="Enter Job Title" method="get"> <br>
						<div id="divOne"><p id="stateText">State </p></div>
						<div id="divTwo"><span class="centered"> <select name="state">
								<option value="AL">AL</option>
								<option value="AK">AK</option>
								<option value="AZ">AZ</option>
								<option value="AR">AR</option>
								<option value="CA">CA</option>
								<option value="CO">CO</option>
								<option value="CT">CT</option>
								<option value="DE">DE</option>
								<option value="DC">DC</option>
								<option value="FL">FL</option>
								<option value="GA">GA</option>
								<option value="HI">HI</option>
								<option value="ID">ID</option>
								<option value="IL">IL</option>
								<option value="IN">IN</option>
								<option value="IA">IA</option>
								<option value="KS">KS</option>
								<option value="KY">KY</option>
								<option value="LA">LA</option>
								<option value="ME">ME</option>
								<option value="MD">MD</option>
								<option value="MA">MA</option>
								<option value="MI">MI</option>
								<option value="MN">MN</option>
								<option value="MS">MS</option>
								<option value="MO">MO</option>
								<option value="MT">MT</option>
								<option value="NE">NE</option>
								<option value="NV">NV</option>
								<option value="NH">NH</option>
								<option value="NJ">NJ</option>
								<option value="NM">NM</option>
								<option value="NY">NY</option>
								<option value="NC">NC</option>
								<option value="ND">ND</option>
								<option value="OH">OH</option>
								<option value="OK">OK</option>
								<option value="OR">OR</option>
								<option value="PA">PA</option>
								<option value="RI">RI</option>
								<option value="SC">SC</option>
								<option value="SD">SD</option>
								<option value="TN">TN</option>
								<option value="TX">TX</option>
								<option value="UT">UT</option>
								<option value="VT">VT</option>
								<option value="VA">VA</option>
								<option value="WA">WA</option>
								<option value="WV">WV</option>
								<option value="WI">WI</option>
								<option value="WY">WY</option>
						</select> <input type="hidden" value="${ID}" name="currentUserID" /> <input
							class="btn btn-success spacer" type="submit" value="Submit">
						</span>
						</div>
					</div>
				</form>
			</div>
		</div>
	
</body>
</html>