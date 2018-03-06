
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"> Application -
				Computer Database </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${computerCount}computersfound.</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">
						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addcomputer">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>




		<form id="deleteForm" action="#" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th>Computer name</th>
						<th>Introduced date</th>
						<!-- Table header for Discontinued Date -->
						<th>Discontinued date</th>
						<!-- Table header for Company -->
						<th>Company</th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->

				<tbody id="results">
					<c:forEach items="${computerList}" var="computer">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="0"></td>
							<td><a href="editComputer" onclick="">${computer.name}</a></td>
							<td>${computer.introduced}</td>
							<td>${computer.discontinued}</td>
							<td>${computer.company.name}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">

			<ul class="pagination">

				<c:if test="${pageNb > 1 }">
					<li><a href="dashboard?page=${pageNb-1}" aria-label="Previous">
							<span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:choose>
					<c:when test="${maxPage <= 27}">
						<c:forEach begin="1" end="${maxPage}" step="1" var="i">
							<c:choose>
								<c:when test="${i == pageNb}">
									<li><a href="dashboard?page=${i}"><b>${i}</b></a></li>
								</c:when>
								<c:otherwise>
									<li><a href="dashboard?page=${i}">${i}</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:when>

					<c:when test="${maxPage > 27 && pageNb <= 10}">
						<c:forEach begin="1" end="15" step="1" var="i">
							<c:choose>
								<c:when test="${i == pageNb}">
									<li><a href="dashboard?page=${i}"><b>${i}</b></a></li>
								</c:when>
								<c:otherwise>
									<li><a href="dashboard?page=${i}">${i}</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<li><a>...</a></li>
						<c:forEach begin="${maxPage-5}" end="${maxPage}" step="1" var="i">
							<li><a href="dashboard?page=${i}">${i}</a></li>
						</c:forEach>
					</c:when>

					<c:when
						test="${maxPage > 27 && pageNb > 10 && pageNb < maxPage - 10}">
						<c:forEach begin="1" end="5" step="1" var="i">
							<li><a href="dashboard?page=${i}">${i}</a></li>
						</c:forEach>
						<li><a>...</a></li>
						<c:forEach begin="${pageNb-5}" end="${pageNb+5}" step="1" var="i">
							<c:choose>
								<c:when test="${i == pageNb}">
									<li><a href="dashboard?page=${i}"><b>${i}</b></a></li>
								</c:when>
								<c:otherwise>
									<li><a href="dashboard?page=${i}">${i}</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<li><a>...</a></li>
						<c:forEach begin="${maxPage-5}" end="${maxPage}" step="1" var="i">
							<li><a href="dashboard?page=${i}">${i}</a></li>
						</c:forEach>
					</c:when>

					<c:when test="${maxPage > 27 && pageNb >= maxPage - 10}">
						<c:forEach begin="1" end="5" step="1" var="i">
							<li><a href="dashboard?page=${i}">${i}</a></li>
						</c:forEach>
						<li><a>...</a></li>
						<c:forEach begin="${maxPage-11}" end="${maxPage}" step="1" var="i">
							<c:choose>
								<c:when test="${i == pageNb}">
									<li><a href="dashboard?page=${i}"><b>${i}</b></a></li>
								</c:when>
								<c:otherwise>
									<li><a href="dashboard?page=${i}">${i}</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:when>

				</c:choose>
				<c:if test="${pageNb < maxPage }">
					<li><a href="dashboard?page=${pageNb + 1}" aria-label="Next">
							<span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>

			<div class="btn-group btn-group-sm pull-right" role="group">
				<a href="dashboard?nbComputerByPage=10"><button type="button" class="btn btn-default">10</button></a>
				<a href="dashboard?nbComputerByPage=50"><button type="button" class="btn btn-default">50</button></a>
				<a href="dashboard?nbComputerByPage=100"><button type="button" class="btn btn-default">100</button></a>
			</div>
		</div>
	</footer>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>