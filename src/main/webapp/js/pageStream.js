var stompClient = null;

var mediaSource = new MediaSource();

var audNode = document.querySelector('audio');

var sourceBuffer;

var playing = false;

var subscription = null;

var activeButton = null;

$(function() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {});
});

function btnConnect(button) {
    if (subscription != null) {
        subscription.unsubscribe();
        subscription = null;
    }
    if (button == activeButton) {
        setButtonState(button, "pause");
        showPlayer(false);
        stop();
        return;
    }
    if (activeButton != null) {
        setButtonState(activeButton, "pause");
        stop();
    }
    subscription = stompClient.subscribe('/topic/' + button.id, function (data) {
        setButtonState(button, "play");
        showPlayer(true);
        play(data);

        activeButton = button;
    });
}

function setButtonState(button, state) {
    var icon = $('#' + button.id + ' i');
    switch (state) {
        case 'pause':
            icon.removeClass('fa-pause');
            icon.addClass('fa-play');
            break;
        case 'play':
            icon.removeClass('fa-play');
            icon.addClass('fa-pause');
    }
}

function stop() {
    audNode.pause();
    activeButton = null;
    initialize();
}

function showPlayer(state) {
    if (state) {
        $('#player').css('bottom', '0%');
    }
    else {
        $('#player').css('bottom', '-10%');
    }
}
// ------------------------------------------------------------

function getArrayBuffer(data) {
    var arrayBuffer = new ArrayBuffer(data.length);
    var bufferView = new Int8Array(arrayBuffer);
    for (i = 0; i < data.length; i++) {
        bufferView[i] = data[i];
    }
    return arrayBuffer;
}

var initialized = false;

function play(incoming) {
    var bits = JSON.parse(incoming.body);
    var buffer = getArrayBuffer(bits);

    if (!initialized) {
        initialize(buffer);
    }
    else {
        sourceBuffer.appendBuffer(buffer);
    }

    audNode.play();
}

function initialize(buffer) {
    audNode = document.querySelector('audio');
    mediaSource = new MediaSource();
    mediaSource.addEventListener('sourceopen', function() {
        sourceBuffer = mediaSource.addSourceBuffer('audio/mpeg');
        sourceBuffer.appendBuffer(buffer);
    });
    audNode.src = window.URL.createObjectURL(mediaSource);
    initialized = true;
}