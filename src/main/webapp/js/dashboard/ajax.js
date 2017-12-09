var previousRow = null;
var firstConnect = true;

$(function(){
    $('#btn-next').click(function() {
        next();
    });

    $('#btn-prev').click(function () {
        previous();
    });

    $('#btn-play').click(function () {
        playOrPause();
    });

    $('#btn-delete').click(function () {
        deleteTracks();
    });

    $('#uploadForm').submit(function (e) {
        e.preventDefault();
        var form = document.forms[0];
        var formData = new FormData(form);
        uploadFiles(formData);
    });

    var fixHelper = function(e, ui) {
        ui.children().each(function() {
            $(this).width($(this).width());
        });
        return ui;
    };

    var prevPos = -1;
    var currentPos = -1;
    $("#content tbody").sortable({
        helper: fixHelper,
        start: function (event, ui) {
            prevPos = ui.item.index();
        },
        stop: function( event, ui ) {
            currentPos = ui.item.index();
            ui.item[0].firstElementChild.innerHTML = currentPos + 1;

            updateList(prevPos, currentPos);
        },
        axis: 'y'
    }).disableSelection();

    $('input:checkbox').click(function (e) {
        setCheckBoxOnclick(e.target);
    });

    setListDisplay();

    dragNdrop();

    // ----------------------------------------------------------------------------

    var stompClient = null;
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/dashboard/' + user, function (data) {
            var parsedData = JSON.parse(data.body);
            if (firstConnect) {
                if (!isEmpty(parsedData)) {
                    var playFA = $('.fa-play');
                    playFA.removeClass('fa-play');
                    playFA.addClass('fa-pause');
                }
                firstConnect = false;
            }
            if (!isEmpty(parsedData)) {
                if(parsedData.position == -1) {
                    previousRow.css('background-color', '');
                    var pauseFA = $('.fa-pause');
                    pauseFA.removeClass('fa-pause');
                    pauseFA.addClass('fa-play')
                    return;
                }
                if (previousRow != null && previousRow.length == 1) {
                    previousRow.css('background-color', '');
                }
                previousRow = $('tr > [name="position"]').eq(parsedData.position).parent();
                previousRow.css('background-color', '#a3c0f5');
            }
        });

        stompClient.send('/topic/dashboard/status/' + user, {}, user);
    });
});

function isEmpty(obj) {
    for(var prop in obj) {
        if(obj.hasOwnProperty(prop))
            return false;
    }

    return JSON.stringify(obj) === JSON.stringify({});
}

function setCheckBoxOnclick(e) {
    if (!e.checked) {
        if (lastChecked()) {
            $('.delete-hidden').hide();
        }
    }
    else {
        $('.delete-hidden').show();
    }
}

function setListDisplay() {
    var rows = $('#content tr');
    if (!rows.length) {
        $('#content').hide();
        $('#drop-zone-empty-table').show();
    }
    else {
        $('#content').show();
        $('#drop-zone-empty-table').hide();
    }
}

function lastChecked() {
    var boxes = $('input:checkbox');
    var count = 0;
    for (var i = 0; i < boxes.length; i++) {
        if (boxes[i].checked) {
            count++;
        }
    }

    return count === 0;
}

function updateList(previousPosition, newPosition) {

    if (newPosition === previousPosition) {
        return;
    }
    else if (newPosition > previousPosition) {
        changeRowIndexesBefore(newPosition);
    }
    else {
        changeRowIndexesAfter(newPosition);
    }

    var jsonPost = {
        "previousPosition" : previousPosition,
        "newPosition" : newPosition
    };

    console.log(new Date().toLocaleString());
    $('body').hide();
    $.ajax({
        url: '/changeTrackPosition?' + crsf +
            '&previousPosition=' + previousPosition +
            '&newPosition=' + newPosition,
        type: 'POST',
        contentType: "application/json",
        success: function (data) {
            $('body').show();
            console.log(new Date().toLocaleString());
            console.log(data);
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function changeRowIndexesBefore(position) {
    var rows = $('[name="position"]');
    for (var i = 0; i < position; i++) {
        rows[i].innerHTML = i + 1;
    }
}

function changeRowIndexesAfter(position) {
    var rows = $('[name="position"]');
    for (var i = position + 1; i < rows.length; i++) {
        rows[i].innerHTML = i + 1;
    }
}


function addToTable(data) {
    var table = document.getElementById('content');
    var startIndex = index;
    var lastIndex = index + data.length;
    for (var i = 0; i < data.length; i++) {
        var row = table.insertRow(-1);
        var posCell = row.insertCell(0);
        posCell.setAttribute("name", "position");

        posCell.innerHTML = ++index;
        row.insertCell(1).innerHTML = data[i].artist;
        row.insertCell(2).innerHTML = data[i].title;
        row.insertCell(3).innerHTML = "<input type=\"checkbox\" class=\"checkbox\">";
    }

    for (var i = startIndex; i < lastIndex; i++) {
        $('input:checkbox').eq(i).click(function (e) {
           setCheckBoxOnclick(e.target);
        });
    }
}

function uploadFiles(data){
    $.ajax({
        url: '/upload?' + crsf,
        type: 'POST',
        data: data,
        dataType: 'JSON',
        processData: false,
        contentType: false,
        xhr: function() {
            var xhr = new window.XMLHttpRequest();
            $('.progress-bar').css("width", 0 + '%');
            $('.progress').show();
            // Upload progress
            xhr.upload.addEventListener("progress", function(evt){
                if (evt.lengthComputable) {
                    var percentComplete = (evt.loaded / evt.total) * 100;
                    $('.progress-bar').css("width", percentComplete + '%');
                }
            }, false);
            return xhr;
        },
        success: function(data){
            $('.progress').hide();
            $('#uploadModal').modal('hide');
            console.log(data);
            addToTable(data);
            setListDisplay();
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function dragNdrop() {
    var dropZone = document.getElementById('drop-zone');
    var dropZoneEmptyTable = document.getElementById('drop-zone-empty-table');
    var uploadForm = document.getElementById('uploadForm');

    var startUpload = function(files) {
        console.log(files);
        var fd = new FormData();

        for (var x = 0; x < files.length; x++) {
            fd.append("files", files[x]);
        }

        uploadFiles(fd);
    };

    dropZone.ondrop = function(e) {
        e.preventDefault();
        this.className = 'upload-drop-zone';

        startUpload(e.dataTransfer.files)
    };

    dropZone.ondragover = function() {
        this.className = 'upload-drop-zone drop';
        return false;
    };

    dropZone.ondragleave = function() {
        this.className = 'upload-drop-zone';
        return false;
    };

    dropZoneEmptyTable.ondrop = function(e) {
        e.preventDefault();
        this.className = 'upload-drop-zone';

        startUpload(e.dataTransfer.files)
    };

    dropZoneEmptyTable.ondragover = function() {
        this.className = 'upload-drop-zone drop';
        return false;
    };

    dropZoneEmptyTable.ondragleave = function() {
        this.className = 'upload-drop-zone';
        return false;
    };
}

function playOrPause() {
    var playFA = $('.fa-play');
    var pauseFA = $('.fa-pause');

    if (playFA.length) {
        play();
        playFA.removeClass('fa-play');
        playFA.addClass('fa-pause');
    }
    else {
        pause();
        pauseFA.removeClass('fa-pause');
        pauseFA.addClass('fa-play');
    }
}

function play() {
    $.ajax({
        url: '/play?' + crsf,
        type: 'POST',
        dataType: 'JSON',
        processData: false,
        contentType: false,
        success: function(data){
            console.log("play success");
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function pause() {
    $.ajax({
        url: '/pause?' + crsf,
        type: 'POST',
        dataType: 'JSON',
        processData: false,
        contentType: false,
        success: function(data){
            console.log("pause success");
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function next() {
    $.ajax({
        url: '/next?' + crsf,
        type: 'POST',
        dataType: 'JSON',
        processData: false,
        contentType: false,
        success: function(data){
            console.log("next success");
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function previous() {
    $.ajax({
        url: '/previous?' + crsf,
        type: 'POST',
        dataType: 'JSON',
        processData: false,
        contentType: false,
        success: function(data){
            console.log("previous success");
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function deleteTracks() {
    var rows = $('#content tr');
    var tracksToDelete = [];
    var j = 0;
    for (var i = 0; i < rows.length; i++) {
        var position = $('[name="position"]').eq(i)[0];
        var box = $('input:checkbox').eq(i)[0];
        if (box.checked) {
            tracksToDelete[j++] = position.innerHTML - 1;
            index--;
        }
    }

    $.ajax({
        url: '/delete?' + crsf,
        type: 'POST',
        data: JSON.stringify(tracksToDelete),
        dataType: 'JSON',
        processData: false,
        contentType: false,
        traditional: true,
        success: function(data){
            console.log("delete success");
            var table = document.getElementById("content");
            for (var i = 0; i < tracksToDelete.length; i++) {
                table.deleteRow(tracksToDelete[i]);
                for (var j = i; j < tracksToDelete.length; j++) {
                    tracksToDelete[j] -= 1;
                }
            }

            var rows = $('[name="position"]');
            for (var i = 0; i < rows.length; i++) {
                rows[i].innerHTML = i + 1;
            }

            $('.delete-hidden').hide();
            setListDisplay();
        },
        error: function (error) {
            console.log(error);
        }
    });
}