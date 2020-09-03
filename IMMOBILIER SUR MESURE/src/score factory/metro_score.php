<?php
session_start();

function calcul_score_metro ($array) {

    $array_stations_metro = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "stations_metro_p") {
            array_push($array_stations_metro, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_stations_metro[0][$i]["distance"]);
    }

    $_SESSION['score_metro'] = 0;
    $cpt = 0;
    $array_effectif = array(40, 30, 15, 10, 5);

    foreach ($array_distances as $distance) {
        if ($distance <= 200 ) {
            $_SESSION['score_metro'] += 1 * $array_effectif[$cpt];
        } else if ( $distance <= 1000) {
            $_SESSION['score_metro'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance <= 1500) {
            $_SESSION['score_metro'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 2000) {
            $_SESSION['score_metro'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 2500) {
            $_SESSION['score_metro'] += 0.2 * $array_effectif[$cpt];
        } else if ( $distance > 2500) {
            $_SESSION['score_metro'] += 0;
        }
        $cpt++;
    }

}

?>