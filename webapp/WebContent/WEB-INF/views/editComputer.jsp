<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"> Application - Computer
				Database </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<div class="label label-default pull-right">id:"${ComputerDTO.id}"</div>
					<h1><spring:message code="editComputer"/></h1>
                    <form:form action="editComputer" modelAttribute="ComputerDTO" method="POST">
                    <input type="hidden" value="${ComputerDTO.id}" id="id" name="id" />
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><spring:message code="computerName"/></label> 
                                <form:input type="text" class="form-control" id="computerName" name="computerName"
                                    placeholder="Computer name" pattern ="[A-Za-z1-9]{1,30}" path="name"/> 
                                 <form:errors path="name"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="computer.introduced"/></label> 
                            <form:input type="date" class="form-control" id="introduced" name="introduced"
                                    placeholder="Introduced date" path="introduced"/>
                                    <form:errors path="introduced"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="computer.discontinued"/></label> 
                                <form:input type="date" class="form-control" id="discontinued" name="discontinued"
                                    placeholder="Discontinued date" path="discontinued"/>
                                    <form:errors path="discontinued"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="companyId"><spring:message code="computer.company"/></label> 
                                <form:select class="form-control" id="companyId" name="companyId" path="company.id">
                                    <option value="0">--</option>
                                    <c:forEach items="${companyList}" var="company">
                                        <option value="${company.id}">${company.name}</option>
                                    </c:forEach>
                            </form:select>
                            <form:errors path="company"></form:errors>
                            </div>
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="add"/>" class="btn btn-primary">
                            <spring:message code="or"/> <a href="dashboard" class="btn btn-default"><spring:message code="cancel"/></a>
                        </div>
                    </form:form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>