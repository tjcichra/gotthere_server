var stompClient = null;

function connectWebsocket() {
	var protocol = window.location.protocol === "https:" ? "wss:" : "ws:";

    var port = window.location.port;
    if(port) {
        port = ":" + port;
    }

    stompClient = new StompJs.Client({
        brokerURL: protocol + "//" + window.location.hostname + port + "/web-socket-stomp",
        debug: function (str) {
            console.log(str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
    });

    stompClient.onConnect = function(frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/web-socket/location', function (greeting) {
            var startDateTimeL = document.getElementById("startdatetime").value;
            var endDateTimeL = document.getElementById("enddatetime").value;

            var location = JSON.parse(greeting.body);

            if(location.dateTime >= startDateTimeL && location.dateTime <= endDateTimeL) {
                markLocation(location);

                if(document.getElementById("follow").checked) {
                    centerMap(location.latitude, location.longitude);
                }
            }
            console.log(greeting.body);
        });
    };

    stompClient.onStompError = function(frame) {
        console.log('Broker reported error: ' + frame.headers['message']);
        console.log('Additional details: ' + frame.body);
    }

    stompClient.activate();
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

$(function () {
    //$("form").on('submit', function (e) {
    //    e.preventDefault();
    //});
    //$( "#connect" ).click(function() { connect(); });
    //$( "#disconnect" ).click(function() { disconnect(); });
    //$( "#send" ).click(function() { sendName(); });
    
});