function authentification() {
	var courriel = document.getElementById("c_courriel");
	var mdp = document.getElementById("c_pass");
	var entry = {
		courriel: courriel.value,
		mdp: mdp.value
	};
	path = window.origin
	var homepages = ['/', 'index.html', 'localhost:5000/', 'localhost:5000/consulter_rdv', '/consulter_rdv'];
	$.ajax({
		url: path.concat('/authentification'),
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(entry),
		success: function (response) {
			$('#connexion').fadeOut();
			document.getElementsByClassName("main_container")[0].style.filter = "blur(0px)";
			if (homepages.indexOf(window.location.pathname) >= 0) {
				disableSignin();
				window.location.reload();
			} else {
				disableError();
			}
		},
		error: function (response) {
			alert(response.responseText);
		}
	});
}

function getCoordinates(adresseClinique, adresseClient, id) {
	var lat1, lat2, long1, long2;
	$.when(
		$.ajax({
			url: "https://maps.googleapis.com/maps/api/geocode/json?key\
			=AIzaSyC90OEbAzJwj2nDA--i89biOxfScxEjpt4&components=postal_code:".concat(adresseClinique),
			type: 'GET',
			dataType: 'json',
			success: function (res) {
				lat1 = res.results[0].geometry.location.lat;
				long1 = res.results[0].geometry.location.lng;
			}
		}),
		$.ajax({
			url: "https://maps.googleapis.com/maps/api/geocode/json?key\
			=AIzaSyC90OEbAzJwj2nDA--i89biOxfScxEjpt4&components=postal_code:".concat(adresseClient),
			type: 'GET',
			dataType: 'json',
			success: function (res) {
				lat2 = res.results[0].geometry.location.lat;
				long2 = res.results[0].geometry.location.lng;
				distance = (google.maps.geometry.spherical.computeDistanceBetween(new google.maps.LatLng(lat1, long1),
					new google.maps.LatLng(lat2, long2)) / 1000).toFixed(1);
				if (isNaN(distance))
					getCoordinates(adresseClinique, adresseClient, id);
				else
					$('#'.concat(id)).html(distance + " KM");
			}
		}),
	).then(function () {
		return;
	});
}

function paintMap(id) {
	adresse = id;
	showMap(id);
	$.ajax({
		url: "https://maps.googleapis.com/maps/api/geocode/json?key\
		=AIzaSyC90OEbAzJwj2nDA--i89biOxfScxEjpt4&components=postal_code:".concat(adresse),
		type: 'GET',
		dataType: 'json',
		success: function (res) {
			lat = res.results[0].geometry.location.lat;
			long = res.results[0].geometry.location.lng;
			var pin = {
				lat: lat,
				lng: long
			};
			var map = new google.maps.Map(
				document.getElementById('map'.concat(id)), {
					zoom: 15,
					center: pin
				});
			var marker = new google.maps.Marker({
				position: pin,
				map: map
			});
		}
	});
}

$(document).ready(function () {
	$("#redirect_signin").click(function () {
		$('#connexion').show();
	});
});

function disableError() {
	document.getElementById("connexion_requise").style.display = "none";
	document.getElementById("redirect_signin").style.display = "none";
}

function disableSignin() {
	document.getElementById("signin").onclick = null;
}

function disableDateAndHour() {
	document.getElementById("date").disabled = true;
	document.getElementById("hour").disabled = true;
}

function showMap(id) {
	$('#map'.concat(id)).fadeIn();
	$('#btn'.concat(id)).fadeIn();
}

function hideMap(id) {
	$('#map'.concat(id)).fadeOut();
	$('#btn'.concat(id)).fadeOut();
}

function connexion() {
	$('#connexion').fadeIn();
	document.getElementsByClassName("main_container")[0].style.filter = "blur(15px)";
}

$(document).ready(function() {
	setTimeout(function() {
			document.getElementsByClassName("booking")[0].style.visibility = "visible";
			document.getElementById("retour").style.visibility = "visible";
			document.getElementsByClassName("spinner")[0].style.display = "none";
		}, 5500);
});

function fermerConnexion() {
	$('#connexion').fadeOut();
	document.getElementsByClassName("main_container")[0].style.filter = "blur(0px)";
	window.history.pushState("", "", '/');
}

function inscription() {
	document.getElementById("connexion").style.display = "none";
	document.getElementById("inscription").style.display = "block";
	document.getElementsByClassName("main_container")[0].style.filter = "blur(15px)";
}

function goBack() {
	window.history.back();
}

function fermerInscription() {
	$('#inscription').fadeOut();
	document.getElementsByClassName("main_container")[0].style.filter = "blur(0px)";
	window.history.pushState("", "", '/');
}

function getTodaysDate() {
	var today = new Date();
	var day = today.getDate();
	var month = today.getMonth() + 1;
	var year = today.getFullYear();
	if (day < 10)
		day = '0' + day
	if (month < 10)
		month = '0' + month
	return year + '-' + month + '-' + day;
}

function initCalendar(index, datePicker, clinique, docteur, HRSelectorID) {
	today = getTodaysDate().split(/[--]/);
	year = today[0];
	month = today[1];
	day = today[2];
	nextMonth = Number(month) + 2;
	if (nextMonth < 10)
		nextMonth = '0' + nextMonth
	var options = $.extend({},
		$.datepicker.regional["fr"], {
			dateFormat: "yy-mm-dd",
			minDate: getTodaysDate(),
			maxDate: new Date(year + '-' + nextMonth + '-01'),
			beforeShowDay: $.datepicker.noWeekends,
			showOn: "button",
			buttonImage: "/static/images/datePicker.png",
			buttonImageOnly: true,
			onSelect: function (date) {
				getAvailabilities(index, datePicker, clinique, docteur, HRSelectorID, date);
			}
		}
	);
	$('#'.concat(datePicker)).datepicker(options);
	$('#'.concat(datePicker)).datepicker("setDate", getTodaysDate());

	//Highlight dates
	for (i = day; i <= 31; i++)
		getAvailabilities(index, datePicker, clinique, docteur, HRSelectorID, year + '-' + month + '-' + i);
	nextMonth = Number(nextMonth) - 1;
	if (nextMonth < 10)
		nextMonth = '0' + nextMonth;
	for (i = 0; i <= 31; i++)
		getAvailabilities(index, datePicker, clinique, docteur, HRSelectorID, year + '-' + nextMonth + '-' + i);
}

function getAvailabilities(index, datePicker, clinique, docteur, HRSelectorID, date) {
	var hrs = {
		"10": '10',
		"11": '11',
		"12": '12',
		"13": '13',
		"14": '14',
		"15": '15',
		"16": '16',
		"17": '17',
		"18": '18',
		"19": '19',
		"20": '20'
	}
	var entry = {
		clinique: clinique,
		docteur: docteur,
		jour: date
	};
	path = window.origin
	$.ajax({
		url: path.concat('/disponibilites'),
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(entry),
		success: function (response) {
			json_data = JSON.parse(response);
			if (json_data.length == 11) {
				highlightCalendarDates(datePicker, date);
				$('#scheduleAppointment'.concat(index)).prop('disabled', true);
			} else {
				highlightCalendarDates(datePicker, '');
				$('#scheduleAppointment'.concat(index)).prop('disabled', false);
			}
			for (var i in json_data)
				delete hrs[json_data[i].Heure];
			var $HRPicker = $('#'.concat(HRSelectorID));
			$HRPicker.empty();
			$.each(hrs, function (key, value) {
				var $option = $("<option/>", {
					value: key,
					text: value.concat(":00")
				});
				$HRPicker.append($option);
			});
		},
		error: function (xhr, error) {
			console.log(xhr.responseText);
			console.log(error);
		}
	});
}

var unavailableDates = {};

function highlightCalendarDates(datePicker, day) {
	fd = day.split(/[--]/);
	year = fd[0];
	month = fd[1];
	day = fd[2];
	unavailableDates[new Date(month + '/' + day + '/' + year)] = new Date(month + '/' + day + '/' + year);
	$('#'.concat(datePicker)).datepicker("option", "beforeShowDay",
		function (date) {
			var highlight = unavailableDates[date];
			if (highlight) {
				var formattedDate = jQuery.datepicker.formatDate('yy-mm-dd', date);
				return [true, "unavailable_dates", formattedDate];
			} else {
				return [true, 'available_dates', ''];
			}
		}
	);
}