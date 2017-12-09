var stompClient = null;
window.onload = init;
var context;    // Audio context
var played = false;

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
            //playByteArray(JSON.parse(data.body));

            lol(data);

            // play(JSON.parse(data.body));
            // audioChunkReceived(context, JSON.parse(data.body), context.sampleRate);
            //audioChunkReceived(context, JSON.parse(data.body)['payload'], context.sampleRate);
            // addChunkToQueue(JSON.parse(data.body));

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
    if (!window.AudioContext) {
        if (!window.webkitAudioContext) {
            alert("Your browser does not support any AudioContext and cannot play back this audio.");
            return;
        }
        window.AudioContext = window.webkitAudioContext;
    }

    context = new AudioContext();
}

function audioChunkReceived (context, byteArray, sample_rate) {

    var arrayBuffer = new ArrayBuffer(byteArray.length);
    var bufferView = new Uint16Array(arrayBuffer);
    for (i = 0; i < byteArray.length; i++) {
        bufferView[i] = byteArray[i];
    }

    var audioBuffer = context.createBuffer(2, bufferView.length, 48000.0);
    audioBuffer.getChannelData(0).set(arrayBuffer);
    var source = context.createBufferSource(); // creates a sound source
    source.buffer = audioBuffer;
    source.connect(context.destination);
    source.start(0);
}

function addChunkToQueue( buffer ) {
    var bufferView = new Uint16Array(buffer);
    // for (i = 0; i < byteArray.length; i++) {
    //     bufferView[i] = byteArray[i];

    if (!nextStartTime) {
        // we've not yet started the queue - just queue this up,
        // leaving a "latency gap" so we're not desperately trying
        // to keep up.  Note if the network is slow, this is going
        // to fail.  Latency gap here is 1 second.
        nextStartTime = context.currentTime + 1;
    }

    var audioBuffer = context.createBuffer(2, bufferView.length, 48000.0);
    audioBuffer.getChannelData(0).set(buffer);
    var bsn = context.createBufferSource();
    bsn.buffer = audioBuffer;
    bsn.connect( context.destination );
    bsn.start( 0 );

    // Ensure the next chunk will start at the right time
    nextStartTime += buffer.duration;
}

function test() {
    var request = new XMLHttpRequest();
    request.open('GET', '/getAudio', true);
    request.responseType = 'arraybuffer';

    // Decode asynchronously
    request.onload = function() {
        addChunkToQueue(request.response);
    };
    request.send();
}

function playSound(buffer) {
    var source = context.createBufferSource(); // creates a sound source
    source.buffer = buffer;                    // tell the source which sound to play
    source.connect(context.destination);       // connect the source to the context's destination (the speakers)
    source.start(0);                           // play the source now
               test();                                // note: on older systems, may have to use deprecated noteOn(time);
}

var nextStartTime = 0;
var myAudioBuffer;

function BLESSRNG(buffer) {
    var view = new Float64Array(JSON.parse(buffer.body));
    var time = 2;
    //audioBuffer = context.createBuffer(1, view.length, 48000); //22050
    if (myAudioBuffer !== undefined) {
        time = myAudioBuffer.duration;
    }
    if (nextStartTime == 0) {
        nextStartTime = context.currentTime;
    }

    var frameCount = 4096;
    var channels = 2;
    myAudioBuffer = context.createBuffer(channels, view.length, 96000);
    for (var channel = 0; channel < channels; channel++) {

        myAudioBuffer.getChannelData(channel, 16, 48000).set(view);
    }
    // audioBuffer.getChannelData(1).set(view);
    source = context.createBufferSource();
    source.buffer = myAudioBuffer;
    source.connect(context.destination);

    source.start(nextStartTime);
    // Ensure the next chunk will start at the right time
    nextStartTime += myAudioBuffer.duration;
}

function play(soundName){
    // Create an empty two second stereo buffer at the
    // sample rate of the AudioContext
    var frameCount = 4096;
    var channels = 2;
    var myAudioBuffer = context.createBuffer(channels, frameCount, 48000);
    for (var channel = 0; channel < channels; channel++) {

        var nowBuffering = myAudioBuffer.getChannelData(channel,16,48000);
        for (var i = 0; i < frameCount; i++) {
            nowBuffering[i] = (soundName[i] / 0x8000);
        }
    }
    // Get an AudioBufferSourceNode.
    // This is the AudioNode to use when we want to play an AudioBuffer
    var source = context.createBufferSource();
    // set the buffer in the AudioBufferSourceNode
    source.buffer = myAudioBuffer;
    // connect the AudioBufferSourceNode to the
    // destination so we can hear the sound
    source.connect(context.destination);
    // start the source playing
    source.start();
}

var playing = false;
var position = 0;
var stack;
var mediaSource = new MediaSource();
function lol(incoming) {
    var audNode = document.querySelector('audio');

    mediaSource.addEventListener('sourceopen', function() {
        var sourceBuffer = mediaSource.addSourceBuffer('audio/mpeg');

        function onAudioLoaded(data, index) {

            // Append the ArrayBuffer data into our new SourceBuffer.
            sourceBuffer.appendBuffer(data);
        }
        var bits = JSON.parse(incoming.body);
        var arrayBuffer = new ArrayBuffer(bits.length);
        var bufferView = new Int8Array(arrayBuffer);
        for (i = 0; i < bits.length; i++) {
            bufferView[i] = bits[i];
        }
        // Retrieve an audio segment via XHR.  For simplicity, we're retrieving the
        // entire segment at once, but we could also retrieve it in chunks and append
        // each chunk separately.  MSE will take care of assembling the pieces.
        onAudioLoaded(arrayBuffer, 0);
    });
    // var sourceBuffer = ms.addSourceBuffer('audio/mpeg');
    // sourceBuffer.appendBuffer(data);
    audNode.src = window.URL.createObjectURL(mediaSource);

    if (!playing) {
        audNode.play();
        playing = true;
    }

    stompClient.send("/hello/received", {}, true);
}