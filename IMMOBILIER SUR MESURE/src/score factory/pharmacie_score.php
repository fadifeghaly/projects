<?php

function calcul_score_pharmacie ($array) {

    $array_pharmacie = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "Pharmacies") {
            array_push($array_pharmacie, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_pharmacie[0][$i]["distance"]);
    }

    $_SESSION['score_pharmacie'] = 0;
    $cpt = 4;
    $array_effectif = array(5, 10, 15, 20, 50);

    foreach ($array_distances as $distance) {
        if ($distance <= 200 ) {
            $_SESSION['score_pharmacie'] += 1 * $array_effectif[$cpt];
        } else if ( $distance <= 600) {
            $_SESSION['score_pharmacie'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance <= 1000) {
            $_SESSION['score_pharmacie'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 1500) {
            $_SESSION['score_pharmacie'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 3000) {
            $_SESSION['score_pharmacie'] += 0.2 * $array_effectif[$cpt];
        }
        $cpt--;
    }
}
?>