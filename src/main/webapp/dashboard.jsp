<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tr" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="td" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: DDshka
  Date: 27.10.2017
  Time: 17:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sonis - Dashboard</title>
    <link rel="stylesheet" href="css/test.css">
    <link rel="stylesheet" href="webjars/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="webjars/bootstrap/3.2.0/css/bootstrap.min.css">
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
    <script type="text/javascript" src="webjars/sockjs-client/1.1.1/sockjs.min.js"></script>
    <script type="text/javascript" src="js/dashboard/ajax.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script>

    </script>
    <script>
        var crsf = "${_csrf.parameterName}=${_csrf.token}";
        var index = ${tracks.size()};
        var user = "${user.name}";
    </script>
</head>
<body>
    <%@include file="header.jsp"%>
    <div class="container">
        <div class="row auth-row">
            <div class="col-sm-4">
                <ul class="list-inline">
                    <li>Hi there, ${user.name}! | <a href="/logout">Logout</a></li>
                </ul>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-10">

                <div class="upload-drop-zone" id="drop-zone-empty-table">
                    Oops. No files yet...
                    Just drag and drop files here
                </div>

                <table class="table" id="content">
                    <tbody>
                        <c:forEach var="track" items="${tracks}" varStatus="status">
                            <tr>
                                <td name="position">${status.count}</td>
                                <td class="ui-state-disabled">${track.artist}</td>
                                <td class="ui-state-disabled">${track.title}</td>
                                <td><input type="checkbox" class="checkbox"></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

            </div>
          
            <div class="col-sm-2 row-center">
                <div class="row">
                    <button id="btn-play"
                            class="control-button">
                        <i class="fa fa-play" aria-hidden="true"></i>
                    </button>

                </div>

                <div class="row">
                    <button id="btn-prev"
                            class="control-button">
                        <i class="fa fa-angle-left" aria-hidden="true"></i>
                    </button>

                    <button id="btn-next"
                            class="control-button">
                        <i class="fa fa-angle-right" aria-hidden="true"></i>
                    </button>
                </div>

                <div class="row">
                    <button id="btn-upload"
                            data-toggle="modal"
                            data-target="#uploadModal"
                            class="control-button">
                        <i class="fa fa-file-audio-o " aria-hidden="true"></i>
                    </button>
                </div>

                <div class="row delete-hidden">
                    <button id="btn-delete"
                            class="control-button">
                        <i class="fa fa-trash-o" aria-hidden="true"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="uploadModal" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Upload</h4>
                </div>
                <div class="modal-body">
                    <h4>Select files from your computer</h4>
                    <form:form enctype="multipart/form-data" id="uploadForm">
                        <div class="form-inline">
                            <div class="form-group">
                                <input name="files" id="fileToUpload" type="file" multiple>
                            </div>
                            <button type="submit" class="btn btn-sm btn-primary" id="js-upload-submit">Upload files</button>
                        </div>
                    </form:form>

                    <!-- Drop Zone -->
                    <h4>Or drag and drop files below</h4>
                    <div class="upload-drop-zone" id="drop-zone">
                        Just drag and drop files here
                    </div>

                    <!-- Progress Bar -->
                    <div class="progress">
                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>

        </div>
    </div>
 </body>
</html>
