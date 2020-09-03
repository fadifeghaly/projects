

/**
 * Initialise la map et le autocomplete.
 * Le viewport de la map est la ville entiere de MTL
 * Les resultats du autocomplete sont biaises vers des resultats
 * contenant des address physique uniquement (ne suggere pas de nom de restaurant, ecole, etc.)
 * 
 * 
 */
function initMap() {
	// Ceci restreint le plus possible les resultats du autocomplete a des adresse
	// de mtl
	const mtlBounds = new google.maps.LatLngBounds(
	{lat: 45.391008, lng: -74.010043}, 
	{lat: 45.723828, lng: -73.4336760});

	const map = new google.maps.Map(document.querySelector('#map'), {
	center: {lat: 45.50884, lng: -73.58781},
	zoom: 10
	});

	const autocomplete = new google.maps.places.Autocomplete(document.querySelector('.search-bar'));
	autocomplete.bindTo('bounds', map);
	autocomplete.setFields(['address_components', 'geometry', 'name']);
	autocomplete.setBounds(mtlBounds);

	autocomplete.addListener('place_changed', () => {changeViewPort(autocomplete.getPlace(), map)});
}

/**
 * On cree un marqueur a l'address selectionnee et on place les coordonnees de cette
 * adresse dans les hidden inputs du form.
 * 
 * @param place resultat du autocomplete. Le viewport changera a cette emplacement
 * @param map		A changer le viewport de cette map 
 */
const changeViewPort = (place,  map) => {
	const marker = new google.maps.Marker({
		position: place.geometry.location,
		map: map
	});

	if (place.geometry.viewport) {
		map.fitBounds(place.geometry.viewport);
	} else {
		map.setCenter(place.geometry.location);
		map.setZoom(17);
	}
	setCoordinates(place);

	marker.addListener('click', () => {displayInfoWindow(place, map, marker)});
}

/**
 * Ouvre une fenetre sur le marquer dans la map lorsque celui est a ete clique
 * 
 * @param place 
 * @param map 
 * @param marker 
 */
const displayInfoWindow = (place, map, marker) => {
	const iw = new google.maps.InfoWindow();
	iw.setContent(makeInfoWindowContent(place));
	iw.open(map, marker);
}


const makeInfoWindowContent = place => {
	console.log(place);
	const latContent = `<p class="color-grey">Latitude: ${place.geometry.location.lat()}</p>`;
	const lngContent = `<p class="color-grey">Latitude: ${place.geometry.location.lng()}</p>`;

	return latContent + lngContent;
}

const setCoordinates = place => {
	const latInput = document.querySelector('.lat');
	const lngInput = document.querySelector('.lng');

	latInput.value = place.geometry.location.lat();
	lngInput.value = place.geometry.location.lng();
}