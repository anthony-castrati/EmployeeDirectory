<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <title>Employee Directory</title>
	    <meta name="description" content="">
	    <!--[if lt IE 9]>
            <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <!-- Place favicon.ico and apple-touch-icon(s) in the root directory -->
	    <link href='http://fonts.googleapis.com/css?family=Lato' rel='stylesheet' type='text/css'>
	    <link rel="stylesheet" href="resources/css/normalize.css">
	    <link rel="stylesheet" href="resources/css/main.css">
	    <script type="text/javascript">
		  var _gaq = _gaq || [];
		  _gaq.push(['_setAccount', 'UA-9880547-4']);
		  _gaq.push(['_setDomainName', 'corecon.org']);
		  _gaq.push(['_trackPageview']);

		  (function() {
		    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		  })();
		</script>
	</head>
<body>
	<header>
		<h1>Employee Directory</h1>
	</header>
	<div class="wrapper">
			<article>
				<section>
					<form:form commandName="searchForm" method="post" role="form">
						<ul>
							<li>
								<form:label path="name" for="Name">
								<form:errors path="name" cssClass="error"></form:errors>
								<form:input type="text" placeholder="Name" id="Name" path="name"/>
								</form:label>
								<form:input type="hidden" path="showall" value="false"></form:input>
							</li>
							<li>
								<form:label for="Department" path="department">
									<form:select id="Department" path="department" value="${dept}">
										<option value="All Departments">All Departments</option>
										<form:options items="${departments.departments}" />
								</form:select>
								</form:label>
							</li>
							<li id="ErrorMessage">
							</li>
							<li>
								<label for="Search">
									<input type="submit" id="Search" value="Search" class="button" />
								</label>
							</li>
						</ul>
					</form:form>
					<h2>Results [${count}]</h2>
					<hr/>
				</section>
				
				<c:forEach items="${results}" var="item" begin="0" end="${max }">
				
				<section class="person">
					<div class="image">
						<img data-src="images/${item.employeeID }" onerror="imgError(this);">
					</div>
					<div class="contactInfo">
						<ul>
							<li><h3><a href="mailto:${item.email}" class="icon-mail">${item.name }</a></h3></li>
							<c:if test="${not empty item.title}"><li><em>${item.title}</em></li></c:if>
							<li>${item.department }<c:if test="${not empty item.division}"> - ${item.division }</c:if></li>
						</ul>
						<ul>
							<c:if test="${not empty item.phone}">
								<li class="icon-phone"><a href="tel:${item.phoneTel}">${item.phone}</a></li>
							</c:if>
							<c:if test="${not empty item.mobile}">
								<li class="icon-mobile"><a href="tel:${item.mobileTel}">${item.mobile}</a></li>
							</c:if>
							<c:if test="${not empty item.office}">
								<li class="icon-location">
								<c:choose>
									<c:when test="${not empty item.street }">
										<a class="toggle">${item.office }</a>
									</c:when>
									<c:otherwise>
										${item.office }
									</c:otherwise>
								</c:choose>
									
								</li>
							</c:if>
						</ul>
					</div>
					<c:if test="${not empty item.street}">
						<div class="location hidden">
							<div class="arrow-up"></div>
							<ul>
								<c:if test="${not empty item.office}"><li>${item.office }</li></c:if>
								<c:if test="${not empty item.street}"><li>${item.street }<br>${item.city }, ${item.state } ${item.zip }</li></c:if>
							</ul>
						</div>
					</c:if>
				</section>
					<hr/>
				</c:forEach>
				<c:if test="${(count > 20) && max < 20}">
					<button id="ShowAll" type="button">Show All</button>
				</c:if>
			</article>
		</div>
		<footer>
			<p><a href="http://corecon.org" target="_blank">corecon</a> | <a href="http://www.raleighnc.gov" target="_blank">raleighnc.gov</a></p>
		</footer>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script src="resources/js/more.js"></script>
</body>
</html>
