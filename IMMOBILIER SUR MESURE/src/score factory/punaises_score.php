<?php
session_start();

function calcul_score_punaises ($array) {

    $array_punaises = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "punaises_p") {
            array_push($array_punaises, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_punaises[0][$i]["distance"]);
    }

    $_SESSION['score_punaises'] = 0;
    $cpt = 4;
    $array_effectif = array(5, 10, 15, 30, 40);

    foreach ($array_distances as $distance) {
        if ($distance <= 25 ) {
            $_SESSION['score_punaises'] += 0;
        } else if ( $distance <= 100) {
            $_SESSION['score_punaises'] += 0.2 * $array_effectif[$cpt];
        } else if ( $distance <= 150) {
            $_SESSION['score_punaises'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 200) {
            $_SESSION['score_punaises'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 250) {
            $_SESSION['score_punaises'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance > 300) {
            $_SESSION['score_punaises'] += 1 * $array_effectif[$cpt];
        }
        $cpt--;
    }
}
?>