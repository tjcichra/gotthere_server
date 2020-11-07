//Runs when the user clicks on "Login".
$("#login").click(function() {
	var usernameValue = $("#username").val();
	var passwordValue = $("#password").val();

	//Create an object with the username and password to be deserialized.
	var login = {
		username: usernameValue,
		password: passwordValue
	}

	//Sends a POST request with the username and password to /login.
	$.post({
		contentType: "application/json",
		url: "/login",
		data: JSON.stringify(login),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(data) {
			//When it gets a response, check if the login was accepted from the serialized object.
			if(data.accepted) {
				setWebsiteCookies();
			} else {
				document.getElementById("errorinfo").innerHTML = "ERROR";
			}
		},
		error: function(e) {
			console.log("Error: " + e)
		}
	});
});

//Runs when the user presses enter in a text box.
$("input[name='logininfo']").keypress(function(e) {
	//If the key pressed is "Enter", simulate clicking the "Login" button.
	if(e.which == 13) {
		$(login).trigger("click");
	}
});