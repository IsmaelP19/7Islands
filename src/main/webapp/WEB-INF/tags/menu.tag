<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />">
				<img src="/resources/images/7islands-logo-60.png">
			</a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>

		
		
		<sec:authorize access="hasAuthority('player')">
		<div class="navbar-collapse collapse" id="main-navbar">

			<sec:authorize access="hasAnyAuthority('admin', 'player')">
			<ul class="nav navbar-nav">

				<sevenislands:menuItem url="/games/rooms"
					title="Enter rooms page">
					<span>Rooms</span>
				</sevenislands:menuItem>

				<sevenislands:menuItem url="#"
					title="Enter ranking page">
					<span>Ranking</span>
				</sevenislands:menuItem>

				<sevenislands:menuItem url="#"
					title="Enter forum page">
					<span>Forum</span>
				</sevenislands:menuItem>

				<sevenislands:menuItem url="#"
					title="Enter viewer mode page">
					<span>Viewer Mode</span>
				</sevenislands:menuItem>

				<sec:authorize access="hasAuthority('player')">
					<sevenislands:menuItem url="/games/new" title="Create new room for playing">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
						<span>New game</span>
					</sevenislands:menuItem>
				</sec:authorize>

			</ul>
			</sec:authorize>

			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/users/new" />">Register</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
											<p>
												<a href="/players/profile/${id}" class="btn btn-primary btn-block">My Profile</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>

						</ul></li>
				</sec:authorize>
			</ul>
		</div>
		</sec:authorize>

		<sec:authorize access="hasAuthority('admin')">
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">
				
				<sevenislands:menuItem active="${name eq 'rooms'}" url="/games"
					title="Enter rooms page">
					<span>Rooms</span>
				</sevenislands:menuItem>

				<sevenislands:menuItem active="${name eq 'ranking'}" url="#"
					title="Enter ranking page">
					<span>Ranking</span>
				</sevenislands:menuItem>

				<sevenislands:menuItem active="${name eq 'forum'}" url="#"
					title="Enter forum page">
					<span>Forum</span>
				</sevenislands:menuItem>

				<sevenislands:menuItem active="${name eq 'viewerMode'}" url="#"
					title="Enter viewer mode page">
					<span>Viewer Mode</span>
				</sevenislands:menuItem>

				<sevenislands:menuItem active="${name eq 'newgame'}" url="/games/new"
					title="trigger a RuntimeException to see how it is handled">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					<span>New game</span>
				</sevenislands:menuItem>
				

			</ul>



			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/users/new" />">Register</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
											<p>
												<a href="/players/profile/${id}" class="btn btn-primary btn-block">My Profile</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>

						</ul></li>
				</sec:authorize>
			</ul>
		</div>
		</sec:authorize>

		<sec:authorize access="!isAuthenticated()">
			<div class="navbar-collapse collapse" id="main-navbar">
				<ul class="nav navbar-nav">
					<sevenislands:menuItem active="${name eq 'Home'}" url="/"
					title="Enter welcome page">
					<span>Home</span>
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					</sevenislands:menuItem>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<sec:authorize access="!isAuthenticated()">
						<li><a href="<c:url value="/login" />">Login</a></li>
						<li><a href="<c:url value="/users/new" />">Register</a></li>
					</sec:authorize>
					<sec:authorize access="isAuthenticated()">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
								<strong><sec:authentication property="name" /></strong> <span
								class="glyphicon glyphicon-chevron-down"></span>
						</a>
							<ul class="dropdown-menu">
								<li>
									<div class="navbar-login">
										<div class="row">
											<div class="col-lg-4">
												<p class="text-center">
													<span class="glyphicon glyphicon-user icon-size"></span>
												</p>
											</div>
											<div class="col-lg-8">
												<p class="text-left">
													<strong><sec:authentication property="name" /></strong>
												</p>
												<p class="text-left">
													<a href="<c:url value="/logout" />"
														class="btn btn-primary btn-block btn-sm">Logout</a>
												</p>
												<p>
													<a href="/players/profile/${id}" class="btn btn-primary btn-block">My Profile</a>
												</p>
											</div>
										</div>
									</div>
								</li>
								<li class="divider"></li>
							</ul></li>
					</sec:authorize>
				</ul>
			</div>
		</sec:authorize>
	</div>
</nav>



