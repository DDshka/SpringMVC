<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: DDshka
  Date: 30.10.2017
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sonis - home</title>
    <link rel="stylesheet" href="webjars/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="webjars/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/test.css">
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script src="webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
    <script src="webjars/sockjs-client/1.1.1/sockjs.min.js"></script>
    <script src="js/pageStream.js"></script>
    <script src="js/home/ajaxHome.js"></script>
    <script>
        var crsf = "${_csrf.parameterName}=${_csrf.token}";
    </script>

</head>
<body>
    <%@include file="header.jsp"%>
    <div class="container">
        <div class="row auth-row">
            <div class="col-sm-4">
                <ul class="list-inline">
                    <c:choose>
                        <c:when test="${not empty user}">
                            <li>Hi there, ${user.name}! | <a href="/logout">Logout</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="/signUp">Sign Up</a></li>
                            <li><a href="/login">Sign In</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
            <div class="col-sm-4"></div>
            <div class="col-sm-4" style="text-align: right  ">
                <c:choose>
                    <c:when test="${not empty user}">
                        <a href="/dashboard">Dashboard</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/dashboard">Start your own broadcast</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-2"></div>
            <div class="col-sm-8">
                <table class="table" id="content">
                    <c:choose>
                        <c:when test="${broadcasts.size() > 0}">
                            <c:forEach var = "broadcast" items="${broadcasts}">
                                <tr>
                                    <td>${broadcast.user.name}</td>
                                    <td>${broadcast.songName}</td>
                                    <td>
                                        <button id="${broadcast.user.name}" class="control-button">
                                            <i class="fa fa-play" aria-hidden="true"></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <div class="nobroadcasts">
                                Oops! No broadcasts now!
                            </div>
                        </c:otherwise>
                    </c:choose>
                </table>
            </div>
            <div class="col-sm-2"></div>
        </div>
    </div>
    <div id="player" >
        <div class="container">
            <audio name="aud" controls>
                TRASH
            </audio>
        </div>
    </div>
</body>
</html>
