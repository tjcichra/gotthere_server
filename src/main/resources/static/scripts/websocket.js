var stompClient = null;
var map = null;
var markers = [];
var lastMarker = null;

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

function connectWebsocket() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            var startDateTimeL = document.getElementById("startdatetime").value;
            var endDateTimeL = document.getElementById("enddatetime").value;

            var location = JSON.parse(greeting.body);

            if(location.dateTime >= startDateTimeL && location.dateTime <= endDateTimeL) {
                markLocation(location);

                if(document.getElementById("follow").checked) {
                    centerMap(location);
                }
            }
            console.log(greeting);
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

//Gets the start and end date-times and sends them out as a JSON message to the server.
function requestLocations() {
    var startDateTimeL = document.getElementById("startdatetime").value;
    var endDateTimeL = document.getElementById("enddatetime").value;

    //Only continue if start date-time is before the end date-time.
    if(startDateTimeL < endDateTimeL) {
        var requestObject = {
            startDateTime: startDateTimeL,
            endDateTime: endDateTimeL
        };

        stompClient.send("/app/hello", {}, JSON.stringify(requestObject));
    } else {
        $("#locations").append("Cannot have start time be after end time.<br>");
    }
}

$(function () {
    //$("form").on('submit', function (e) {
    //    e.preventDefault();
    //});
    //$( "#connect" ).click(function() { connect(); });
    //$( "#disconnect" ).click(function() { disconnect(); });
    //$( "#send" ).click(function() { sendName(); });
    
});