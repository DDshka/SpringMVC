<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: DDshka
  Date: 09.11.2017
  Time: 16:20
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>Sonis - Registration</title>
    <link rel="stylesheet" href="webjars/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/test.css">
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script>
        var crsf = "${_csrf.parameterName}=${_csrf.token}";
    </script>

</head>
<body>
<%@include file="header.jsp"%>
<div class="container">

    <div class="col-sm-4"></div>
    <div class="col-sm-4">
        <c:if test="${not empty user}">
            <form:form method="POST" action="/signUpSubmit" modelAttribute="user">
                <h2 class="text-center">Registration</h2>
                <spring:bind path="name">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input path="name" placeholder="Username..." autofocus="true" class="form-control"/>
                        <form:errors path="name" style="color:red;"></form:errors>
                    </div>
                </spring:bind>

                <spring:bind path="password">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input type="password" path="password" placeholder="Password..." class="form-control"/>
                        <form:errors path="password" style="color:red;"></form:errors>
                    </div>
                </spring:bind>
                <button type="submit" class="btn btn-lg btn-primary btn-block">Register</button>
            </form:form>
        </c:if>
    </div>
    <div class="col-sm-4"></div>
</div>

</body>
</html>