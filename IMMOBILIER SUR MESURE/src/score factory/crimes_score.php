<?php
session_start();

function calcul_score_crimes ($array) {

    $array_crimes = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "crimes_p") {
            array_push($array_crimes, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_crimes[0][$i]["distance"]);
    }

    $_SESSION['score_crimes'] = 0;
    $cpt = 4;
    $array_effectif = array(5, 10, 15, 30, 40);

    foreach ($array_distances as $distance) {
        if ($distance <= 50 ) {
            $_SESSION['score_crimes'] += 1 * $array_effectif[$cpt];
        } else if ( $distance <= 75) {
            $_SESSION['score_crimes'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance <= 100) {
            $_SESSION['score_crimes'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 300) {
            $_SESSION['score_crimes'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 500) {
            $_SESSION['score_crimes'] += 0.2 * $array_effectif[$cpt];}
        $cpt--;
    }
}
?>
