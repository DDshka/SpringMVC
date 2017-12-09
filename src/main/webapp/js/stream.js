var stompClient = null;

var mediaSource = new MediaSource();

var audNode = document.querySelector('audio');

var sourceBuffer;

var playing = false;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        //console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            //console.log("ENCODED MESSAGE: " + JSON.parse(greeting.body)['payload']);
            //showGreeting(toUTF8Array(JSON.parse(greeting.body)['payload']));
        });

        stompClient.subscribe('/topic/play', function (data) {
            // loadNextChunk(JSON.parse(data.body));
            lol(data);
            // stompClient.send("/hello/received", {}, true);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

function init() {
    mediaSource.addEventListener('sourceopen', open, false);
}

// ------------------------------------------------------------

function open() {
    // mediaSource = this;
    sourceBuffer = mediaSource.addSourceBuffer('audio/mpeg');
    sourceBuffer.addEventListener('updateend', function (_) {
        mediaSource.endOfStream();
        audNode.play();
        //console.log(mediaSource.readyState); // ended
    });
}

function getArrayBuffer(data) {
    var arrayBuffer = new ArrayBuffer(data.length);
    var bufferView = new Int8Array(arrayBuffer);
    for (i = 0; i < data.length; i++) {
        bufferView[i] = data[i];
    }
    return arrayBuffer;
}

var once = false;

function lol(incoming) {
    if (audNode == undefined) {
        audNode = document.querySelector('audio');
    }

    var bits = JSON.parse(incoming.body);

    if (once == false) {
        mediaSource.addEventListener('sourceopen', function() {
            sourceBuffer = mediaSource.addSourceBuffer('audio/mpeg');
            var buffer = getArrayBuffer(bits);
            sourceBuffer.appendBuffer(buffer);
        });
        once = true;
    }
    else {
        var buffer = getArrayBuffer(bits);
        sourceBuffer.appendBuffer(buffer);
    }

    if (audNode.src == "") {
        audNode.src = window.URL.createObjectURL(mediaSource);
    }

    if (!playing) {
        audNode.play();
        playing = true;
    }

    stompClient.send("/hello/received", {}, true);
}