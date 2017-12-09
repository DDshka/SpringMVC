<%@ page import="java.util.Enumeration" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tr" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="th" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
    <head>
        <title>Sonis - Login</title>
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
                <c:if test="${param.logout != null}">
                    <div class="alert alert-success">
                        <p>You have been logged out successfully.</p>
                    </div>
                </c:if>

                <c:if test="${param.error != null}">
                    <div class="alert alert-danger">
                        <p>Wrong username or password.</p>
                    </div>
                </c:if>

                <c:if test="${not empty user}">
                    <form:form method="POST" action="login" modelAttribute="user">
                        <h2 class="text-center">Log In</h2>
                        <spring:bind path="name">
                            <div class="form-group ${status.error ? 'has-error' : ''}">
                                <form:input path="name" placeholder="Username..." autofocus="true" class="form-control"/>
                                <form:errors path="name" class="test"></form:errors>
                            </div>
                        </spring:bind>

                        <spring:bind path="password">
                            <div class="form-group ${status.error ? 'has-error' : ''}">
                                <form:input type="password" path="password" placeholder="Password..." class="form-control"/>
                                <form:errors path="password" class="test"></form:errors>
                            </div>
                        </spring:bind>
                            <button type="submit" class="btn btn-lg btn-primary btn-block">Log in</button>
                            <h4 class="text-center"><a href="/signUp">Create an account</a></h4>
                    </form:form>
                </c:if>
            </div>
            <div class="col-sm-4"></div>
        </div>

    </body>
</html>