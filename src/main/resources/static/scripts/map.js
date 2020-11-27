var markers = [];

var geojson = {
	'type': 'Feature',
	'properties': {},
	'geometry': {
		'type': 'LineString',
		'coordinates': []
	}
};

function javaScriptDateToFormattedDate(javaScriptDate) {
	var splitDate = javaScriptDate.split(/-|T|:|\./, 6);
	var hourInt = parseInt(splitDate[3]);

	var ampm = " PM";
	if(hourInt < 12) {
		ampm = " AM"
	}

	if(hourInt >= 13) {
		hourInt = hourInt - 12;
	}

	return splitDate[1] + "/" + splitDate[2] + "/" + splitDate[0] + " " + hourInt + ":" + splitDate[4] + ":" + splitDate[5] + ampm;
}

//Makes sure the location is valid.
function validateLocation(location) {
	return location.latitude >= -90 && location.latitude <= 90 &&
		location.longitude >= -180 && location.longitude <= 180 &&
		location.bearing >= 0 && location.speed >= 0;
}

function markLocation(location) {
	if(validateLocation(location)) {
		//Create line on map.
		geojson.geometry.coordinates.push([location.longitude, location.latitude]);
		map.getSource("lines").setData(geojson);

		if(markers.length > 0) {
		    lastMarker = markers[markers.length - 1];
		    lastMarker.getElement().removeAttribute("id");
		}

		//Create HTML element for marker.
		var el = document.createElement("div");
		el.className = "marker";
		el.id = "latest";

		//Add marker with information popup.
		var popup = new mapboxgl.Popup({ offset: 25 }).setHTML("<span class=\"popuptext\">Time: " + javaScriptDateToFormattedDate(location.dateTime) + "<br>Insertion Time: " + javaScriptDateToFormattedDate(location.insertionDateTime) + "<br>Bearing: " + location.bearing + "&#176<br>Speed: " + (location.speed * 2.23694) + " mph</span>");
		var marker = new mapboxgl.Marker(el).setPopup(popup).setLngLat([location.longitude, location.latitude]).addTo(map);

		el.addEventListener("mouseenter", () => marker.togglePopup());
		el.addEventListener("mouseleave", () => marker.togglePopup());

		markers.push(marker);
	}
}

function removeMarkers() {
	//Remove each marker from the map, clear the marker array, and remove the lines.
	markers.forEach(marker => marker.remove())
	markers = [];
	geojson.geometry.coordinates = [];
	map.getSource("lines").setData(geojson);
}

//Method to request all locations between the start date-time and the end date-time.
function getLocationsFromDateTimes(forceFollow) {
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
			success: function(locations) {
				//Remove current markers and add markers from response object.
				removeMarkers();
				locations.forEach(location => markLocation(location));

				if((forceFollow || document.getElementById("follow").checked) && locations.length > 0) {
					var lastLocation = locations[locations.length - 1];
					centerMap(lastLocation.latitude, lastLocation.longitude);
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

function centerMap(latitude, longitude) {
    map.panTo([longitude, latitude], {
        duration: 1500
    });
	//map.setCenter([longitude, latitude]);
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
document.getElementById("startdatetime").value = dateString + "T00:00:00.00";
document.getElementById("enddatetime").value = dateString + "T23:59:00.00";

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

	map.loadImage("line.png", function(err, image) {
	    map.addImage("lineimage", image);

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
        			'line-width': 30,
        			'line-opacity': 0.8,
        			'line-pattern': 'lineimage'
        		}
        	});
	});

	//Request locations
    $.post({
        url: "/startDateTimeNumber?locationNumber=5",
        dataType: "json",
        cache: false,
        timeout: 600000,
        success: function(startDateTime) {
            if(startDateTime != null){
                var formattedStartDateTime = startDateTime.substring(0, 16);
                document.getElementById("startdatetime").value = formattedStartDateTime;
                getLocationsFromDateTimes(true);
            }
        },
        error: function(e) {
            console.log("Error: " + e)
        }
    });
});

$("#follow").change(function() {
	if(this.checked && markers.length > 0) {
		lastMarker = markers[markers.length - 1];
		centerMap(lastMarker.getLngLat().lat, lastMarker.getLngLat().lng);
	}
});