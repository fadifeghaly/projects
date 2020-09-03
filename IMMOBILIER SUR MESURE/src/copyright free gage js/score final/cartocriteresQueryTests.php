<!DOCTYPE html>


<?php
/**
 * Created by PhpStorm.
 * User: jeanphilippepouliot
 * Date: 2019-03-26
 * Time: 21:30
 *
 *
 */

session_start();
var_dump($_SESSION['score_final']);
var_dump($_SESSION['retours_bd']);
var_dump($_SESSION['lng']);
var_dump($_SESSION['lat']);
var_dump($_SESSION['adresse']);
var_dump($_SESSION['retours_inon']);
var_dump($_SESSION['retours_carr']);
?>


<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Cartographie des différentes données qui ont servie pour le calcul du score</title>


    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" type="image/x-icon" href="docs/images/favicon.ico"/>

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.4.0/dist/leaflet.css"
          integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
          crossorigin=""/>
    <script src="https://unpkg.com/leaflet@1.4.0/dist/leaflet.js"
            integrity="sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg=="
            crossorigin=""></script>


</head>
<body>


<div id="mapid" style="width: 100rem; height: 50rem;"></div>
<script>


    var latMaison = <?php echo $_SESSION['lat']; ?>;
    var longMaison = <?php echo $_SESSION['lng']; ?>;
    var retourbd = <?php echo json_encode($_SESSION['retours_bd']);?>;
    var retourdangerinon = <?php echo json_encode($_SESSION['retours_inon']);?>;
    var retourdangercarr = <?php echo json_encode($_SESSION['retours_carr']);?>;

    var maisonIcon = L.icon({
        iconUrl: 'iconeMaisonPetit.png',
        popupAnchor: [13, 0]
    });


    var fondRues = L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; ' +
            '<a href="https://carto.com/attributions">CARTO</a>',
        subdomains: 'abcd',
        maxZoom: 19
    });

    var OpenStreetMap= L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });



    var fondRuesPale = L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; ' +
            '<a href="https://carto.com/attributions">CARTO</a>',
        subdomains: 'abcd',
        maxZoom: 19
    });

    var satellite = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
        {
            attribution: 'Tiles &copy; Esri &mdash; Source: Esri, GeoEye'
        });


    var mymap = L.map('mapid', {
        center: [latMaison, longMaison],
        zoom: 14,
        layers: [fondRuesPale]

    });

    var baseLayers = {
        "Rues(estompé)": fondRuesPale,
        "Rues": fondRues,
        "Rues et points d'intérêts (OSM)": OpenStreetMap,
        "Satellite": satellite
    };


    var couches = {};

    for (var i = 0; i < retourbd.length; i++) {
        if (typeof retourbd[i] === 'string') {
            //valeurs_encode.push([retourbd[i],retourbd[i-1]])
            var coord_json_array = retourbd[i - 1];
            var nomcouche = retourbd[i];
            var iconePNG = L.icon({iconUrl: nomcouche + '.png', popupAnchor: [13, 0]});
            //creationMarker([retourbd[i],retourbd[i-1]])

            var groupLayer = L.layerGroup();

            for (var j = 0; j < coord_json_array.length; j++) {

                leLabel = "";

                if (coord_json_array[j].label) {
                    leLabel = coord_json_array[j].label
                } else if (coord_json_array[j].name) {
                    leLabel = coord_json_array[j].name

                } else if (coord_json_array[j].nom) {
                    leLabel = coord_json_array[j].nom
                }


                if (leLabel) {
                    var strPopup = '<p><b>' + String(coord_json_array[j].label) +
                        '</b><br><br><em>Distance  de la résidence: ' +
                        String(((coord_json_array[j].distance) / 1000).toFixed(2)) + ' KM</em></p>';
                    groupLayer.addLayer(L.marker([coord_json_array[j].y, coord_json_array[j].x], {icon: iconePNG}).bindPopup(strPopup));
                } else {
                    groupLayer.addLayer(L.marker([coord_json_array[j].y, coord_json_array[j].x], {icon: iconePNG}).bindPopup
                    ('<br><em>Distance  de la résidence: ' + String(((coord_json_array[j].distance) / 1000).toFixed(2)) + ' KM</em></p>'))
                }


            }
            couches[nomcouche] = groupLayer;

        }
    }


    var glayerInon= L.layerGroup();

    if (retourdangerinon){
        for (var i = 0; i < retourdangerinon.length; i++) {

            var lelabel = "Danger:<br> " + String(retourdangerinon[i].label);
            console.log(lelabel);
            var layerGeoJson = L.geoJSON((JSON.parse(retourdangerinon[i].geojson)));

            //var layerGeojsonInon = L.geoJSON(legeoJson);
            //console.log(legeoJson);
            if (layerGeoJson) {
                layerGeoJson.bindPopup(lelabel)
                glayerInon.addLayer(layerGeoJson);
                couches["zone inondable concerné"] = glayerInon;
            }
        }
    }



    var glayerCarr = L.layerGroup();

    if (retourdangercarr){
        for (let i = 0; i < retourdangercarr.length; i++) {

            let lelabel = "Danger:<br> Votre résidence se situe a l'intérieur ou près d'une ancienne carrière<br><a href='http://ville.montreal.qc.ca/portal/page?_pageid=7237,142322407&_dad=portal&_schema=PORTAL'>INFORMATION SUR LE RISQUE (Ville de Montréal)</a>";

            let layerGeoJson = L.geoJSON((JSON.parse(retourdangercarr[i].geojson)));

            if (layerGeoJson) {
                layerGeoJson.bindPopup(lelabel)
                glayerCarr.addLayer(layerGeoJson);
                couches["carrières concernés"] = glayerCarr;
            }
        }
    }




    controleur = L.control.layers(baseLayers, couches, {collapsed: false, checked: true}).addTo(mymap);
    controleur.setPosition('bottomleft');

    //L.control.layers(baseLayers, couches, {collapsed: false}).addTo(mymap);


    var checkBoxPanel = document.getElementsByClassName("leaflet-control-layers-selector");

    for (let i = 0; i < checkBoxPanel.length; i++){
        if (checkBoxPanel[i].getAttribute('type')==='checkbox'){
            checkBoxPanel[i].dispatchEvent(new MouseEvent('click'))
        }
    }

    var markerMaison = L.marker([latMaison, longMaison], {icon: maisonIcon});

    var texteBulle = "<b>Votre résidence choisie: </b><br><?php echo $_SESSION['adresse']; ?>";
    markerMaison.bindPopup(texteBulle);


    markerMaison.addTo(mymap);


</script>


</body>
</html>

