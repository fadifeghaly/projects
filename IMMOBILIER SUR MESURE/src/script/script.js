function verifierFormulaire() {
	var checked = document.querySelectorAll('input:checked');
	//vérifier qu'au moins 1 critère est sélectionné
	if (checked.length === 0) {
		alert("Aucun critère sélectionné");
		return false;
	} else {
		var adr = document.getElementById("adresse");
		if (adr.value.length == 0) {
			alert("Adresse non valide");
			return false;
		} else {

			//vérifier que l'adresse est bien entrée

			var geocoder = new google.maps.Geocoder();
			geocoder.geocode({
				'address': adr.value
			}, function(results, status) {
				if (status === google.maps.GeocoderStatus.OK && results.length > 0) {
					//adresse valide
					adr.value = results[0].formatted_address;
					return true;
				} else {
					alert("Adresse invalide");
					return false;
				}
			});

			return true;
		}
	}
}

window.addEventListener('keydown', function(e) {
	if (e.keyIdentifier == 'U+000A' || e.keyIdentifier == 'Enter' || e.keyCode == 13) {
		if (e.target.nodeName == 'INPUT' && e.target.type == 'text') {
			e.preventDefault();
			return false;
		}
	}
}, true);