var markers = [];

var geojson = {
	'type': 'Feature',
	'properties': {},
	'geometry': {
		'type': 'LineString',
		'coordinates': []
	}
};

function markLocation(location) {
	//Create line on map.
	geojson.geometry.coordinates.push([location.longitude, location.latitude]);
	map.getSource("lines").setData(geojson);

	//Create HTML element for marker.
	var el = document.createElement("div");
	el.className = "marker";

	//Add marker with information popup.
	var popup = new mapboxgl.Popup({ offset: 25 }).setHTML("Time: " + location.dateTime + "<br>Bearing: " + location.bearing + "&#176<br>Speed: " + location.speed + " mph");
	var marker = new mapboxgl.Marker(el).setPopup(popup).setLngLat([location.longitude, location.latitude]).addTo(map);
	markers.push(marker);
}

function removeMarkers() {
	//Remove each marker from the map, clear the marker array, and remove the lines.
	markers.forEach(marker => marker.remove())
	markers = [];
	geojson.geometry.coordinates = [];
}

//Method to request all locations between the start date-time and the end date-time.
function getLocationsFromDateTimes() {
	var startDateTimeValue = $("#startdatetime").val();
	var endDateTimeValue = $("#enddatetime").val();

	//Only continue if start date-time is before the end date-time.
	if(startDateTimeValue < endDateTimeValue) {
		document.getElementById("startdatetime").max = endDateTimeValue;
		document.getElementById("enddatetime").min = startDateTimeValue;

		//Request locations
		$.post({
			url: "/locations?startDateTime="+startDateTimeValue+"&endDateTime="+endDateTimeValue,
			dataType: "json",
			cache: false,
			timeout: 600000,
			success: function(data) {
				//Remove current markers and add markers from response object.
				removeMarkers();
				data.locations.forEach(location => markLocation(location));

				if(document.getElementById("follow").checked && data.locations.length > 0) {
					var lastLocation = data.locations[data.locations.length - 1];
					centerMap(lastLocation);
				}
			},
			error: function(e) {
				console.log("Error: " + e)
			}
		});
	} else {
		$("#locations").append("Cannot have start time be after end time.<br>");
	}
}

function centerMap(location) {
	map.setCenter([location.longitude, location.latitude]);
}

//Connect the web socket to recieve real-time location updates.
connectWebsocket();

//Get today's date.
var today = new Date();

//today.getMonth() returns 0-11. "today.getMonth() < 9" is the equivalent to "today.getMonth() + 1 < 10".
var monthStringPre = today.getMonth() < 9 ? "0" : "";
var dayStringPre = today.getDate() < 10 ? "0" : "";

var dateString = today.getFullYear() + "-" + monthStringPre + (today.getMonth() + 1) + "-" + dayStringPre + today.getDate();

//Set the default values of the datetime pickers to the beginning and end of today and request locations.
document.getElementById("startdatetime").defaultValue = dateString + "T00:00:00.00";
document.getElementById("enddatetime").defaultValue = dateString + "T23:59:00.00";

getLocationsFromDateTimes();

mapboxgl.accessToken = 'pk.eyJ1IjoicmFpbmJvd2x1aWdpMjgxIiwiYSI6ImNrZ2ZxMWE3dTBxZDQyeHBheWUyMXhhamkifQ.asrVZzC3ppQBhZt0R4Bh-A';
map = new mapboxgl.Map({
	container: 'map',
	style: 'mapbox://styles/mapbox/satellite-streets-v11'
});



map.on("load", function() {
	map.addSource("lines", {
		"type": "geojson",
		'data': geojson
	});

	map.addLayer({
		'id': 'lines',
			'type': 'line',
			'source': 'lines',
			'layout': {
				'line-cap': 'round',
				'line-join': 'round'
		},
		'paint': {
			'line-color': '#FFFF00',
			'line-width': 5,
			'line-opacity': 0.8
		}
	});
});

//$(".datetime").on("change", getLocationsFromDateTimes);