<?php

function gen_query_pt_interets($x, $y, $selectionClasse){
    return
        "SELECT DISTINCT
        ST_X(poi.geom) AS x,
        ST_Y(poi.geom) AS y,
        ST_distance(ST_Transform(poi.geom, 3857),ST_Transform(ST_GeomFromText('POINT({$x} {$y})', 4326), 3857)) as distance,
        name AS label
        FROM points_interets_p as poi
        WHERE poi.classe = '{$selectionClasse}' and st_dwithin(ST_Transform(poi.geom, 3857),ST_Transform(ST_GeomFromText('POINT({$x} {$y})', 4326), 3857),10000)
        ORDER BY distance LIMIT 5";
}

function generate_Query($x, $y, $nom_critere)
{

    if ($nom_critere == "ecoles") {
        return
            "SELECT DISTINCT
        ST_X({$nom_critere}.geom) AS x,
        ST_Y({$nom_critere}.geom) AS y,
        ST_distance(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857)) as distance,
        nom_court AS label
        FROM ecoles_montreal_p as ecoles
        WHERE st_dwithin(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857),10000) AND ordre_ens = 'Primaire'
        ORDER BY distance LIMIT 5";
    } else if ($nom_critere == "crimes_p") {
        return
            "SELECT DISTINCT
        ST_X({$nom_critere}.geom) AS x,
        ST_Y({$nom_critere}.geom) AS y,
        ST_distance(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857)) as distance,
        categorie as label
        FROM {$nom_critere}			  
        WHERE st_dwithin(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857),10000) 
        ORDER BY distance LIMIT 5";
    } else if ($nom_critere == "stations_metro_p") {
        return
            "SELECT DISTINCT
        toponymere as label,
        ST_X({$nom_critere}.geom) AS x,
        ST_Y({$nom_critere}.geom) AS y,
        ST_distance(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857)) as distance,
        toponymere as label
        FROM {$nom_critere}           
        WHERE st_dwithin(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857),10000) AND type_entit = 'Station de métro'
        ORDER BY distance LIMIT 5";
    } else if ($nom_critere == "punaises_p") {
        return
            "SELECT DISTINCT
        ST_X({$nom_critere}.geom) AS x,
        ST_Y({$nom_critere}.geom) AS y,
        ST_distance(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857)) as distance
        FROM {$nom_critere}           
        WHERE st_dwithin(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857),10000)
        ORDER BY distance LIMIT 5";
    } else if ($nom_critere == "jardins_communautaires_p"){
        return
            "SELECT DISTINCT
        ST_X({$nom_critere}.geom) AS x,
        ST_Y({$nom_critere}.geom) AS y,
        ST_distance(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857)) as distance,
        text_ as label
        FROM {$nom_critere}           
        WHERE st_dwithin(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857),10000)
        ORDER BY distance LIMIT 5";

    } else if ($nom_critere == "Café") {
        return gen_query_pt_interets($x, $y, "Café");
    } else if ($nom_critere == "Lavoir") {
        return gen_query_pt_interets($x, $y, "Lavoir");
    } else if ($nom_critere == "Pharmacies") {
        return gen_query_pt_interets($x, $y, "Pharmacies");
    } else if ($nom_critere == "Épicerie") {
        return gen_query_pt_interets($x, $y, "Épicerie");
    } else if ($nom_critere == "Bar") {
        return gen_query_pt_interets($x, $y, "Bar");
    } else if ($nom_critere == "Restaurant") {
        return gen_query_pt_interets($x, $y, "Restaurant");

    } else if ($nom_critere == "stations_bixi_p"){
        return
            "SELECT DISTINCT
        ST_X({$nom_critere}.geom) AS x,
        ST_Y({$nom_critere}.geom) AS y,
        ST_distance(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857)) as distance,
        label
        FROM {$nom_critere}           
        WHERE st_dwithin(
        ST_Transform({$nom_critere}.geom, 3857),
        ST_Transform(
        ST_GeomFromText('POINT({$x} {$y})', 4326), 3857),10000)
        ORDER BY distance LIMIT 5";
    }


}



?>