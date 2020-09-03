// Ajax call : formulaire de recherche rapide permettant de saisir deux dates
function getData() {
	var date1 = document.getElementById("date1");
	var date2 = document.getElementById("date2");

	var entry = {
		date1: date1.value,
		date2: date2.value
	}; 
	
	path = window.origin

	$.ajax({
		url: path.concat('/contrevenants'),
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(entry),
		success: function (response) {
			console.log(response);
			createTableFromJson(JSON.parse(response));},
			error: function(xhr, error) {
				console.log(xhr.responseText);
				console.log(error);}
			});
}

// Ajax call : Liste déroulante de tous les contrevenants
$("document").ready(function() {
	$("#send2").click(function() {
		var etablissement = document.getElementById("listeEtablissements");
		var nomEtablissement = etablissement.options[etablissement.selectedIndex].text;
		
		var entry = {
			etablissement: nomEtablissement
		}; 
		
		path = window.origin

		$.ajax({
			url: '/dashboard',
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(entry),
			success: function (response) {
				console.log(response);
				createTableFromJson(JSON.parse(response));},
				error: function(xhr, error) {
					console.log(xhr.responseText);
					console.log(error);}
				});
	});	
});

// Tableau dynamique
function createTableFromJson(response) {								

	var col = [];
	for (var i = 0; i < response.length; i++) {
		for (var key in response[i]) {
			if (col.indexOf(key) === -1) {
				col.push(key);
			}
		}
	}

	var table = document.createElement("table");

	var tr = table.insertRow(-1);                  

	for (var i = 0; i < col.length; i++) {
		var th = document.createElement("th");   
		th.innerHTML = col[i];
		tr.appendChild(th);
	}

	for (var i = 0; i < response.length; i++) {
		tr = table.insertRow(-1);
		for (var j = 0; j < col.length; j++) {
			var tabCell = tr.insertCell(-1);
			tabCell.innerHTML = response[i][col[j]];
		}
	}

	if (table.rows[0].cells.length != 0) {
		var divContainer = document.getElementById("ajaxCall");
		divContainer.innerHTML = "";
		divContainer.appendChild(table);
		document.getElementById("arrow").style.visibility="visible";
		document.getElementById("ajaxCall").style.visibility="visible";
		document.getElementById("arrow1").style.visibility="visible";

	}

}

// Validation des deux dates 
function champEstVide() {
	if (document.getElementById("date1").value == "" || document.getElementById("date2").value == "") {
		document.getElementById('erreurDate').innerHTML = 'Oups! les deux dates sont requises';
		return true;
	}
	return false;
}

// Validation des deux dates 
function validerDate() {
	var now = new Date();
	var date = now.getDate();
	var month = now.getMonth() + 1;
	var year = now.getFullYear();
	var dateStr;
	
	if (date < 10)
		date = '0' + date;
	if (month < 10)
		month = '0' + month;
	
	dateStr = year + '-' + month + '-' + date;	
	
	if (!champEstVide()) {
		if (isNaN(Date.parse(document.getElementById('date1').value)) || isNaN(Date.parse(document.getElementById('date2').value))) {
			document.getElementById('erreurDate').innerHTML = 'Oups! le format est invalide';
			return false;  	
		} else if (Date.parse(document.getElementById('date1').value) > Date.parse(document.getElementById('date2').value)) {
			document.getElementById('erreurDate').innerHTML = 'Oups! la première date doit être inférieure à la deuxième';
			return false;  	
		} else if (document.getElementById('date1').value > dateStr || document.getElementById('date2').value > dateStr) {
			document.getElementById('erreurDate').innerHTML = "Oups! impossible d'utiliser une date ultérieure à celle d'aujourd'hui";
			return false;  	
		}
		document.getElementById('erreurDate').innerHTML = '';
		getData();
	}
}
