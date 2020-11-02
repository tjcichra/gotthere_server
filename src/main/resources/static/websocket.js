var stompClient = null;
var map = null;
var markers = [];

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
        //setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            var startDateTimeL = document.getElementById("startdatetime").value;
            var endDateTimeL = document.getElementById("enddatetime").value;

            var location = JSON.parse(greeting.body);

            if(location.dateTime >= startDateTimeL && location.dateTime <= endDateTimeL) {
                markLocation(location);

                var isFollowing = document.getElementById("follow").checked;

                if(isFollowing) {
                    centerMap(location);
                }
            }
            console.log(greeting);
        });

        stompClient.subscribe('/topic/greetings2', function (greeting) {
            markers.forEach(marker => marker.remove());
            console.log("Got locations message");
            var locationsArray = JSON.parse(greeting.body).locations;

            locationsArray.forEach(location => markLocation(location));
        });

        stompClient.subscribe('/topic/greetings3', function (greeting) {
            var loginAccepted = JSON.parse(greeting.body).accepted;

            if(loginAccepted) {
                setWebsiteCookies();
            }
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

//Gets the start and end date-times and sends them out as a JSON message to the server.
function requestLogin() {
    var usernameL = document.getElementById("username").value;
    var passwordL = document.getElementById("password").value;

    //Only continue if the username and password is not emtpy
    if(usernameL && passwordL) {
        var requestObject = {
            username: usernameL,
            password: passwordL
        };

        stompClient.send("/app/hello2", {}, JSON.stringify(requestObject));
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