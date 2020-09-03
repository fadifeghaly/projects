<?php

function calcul_score_restaurant ($array) {

    $array_restaurant = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "Restaurant") {
            array_push($array_restaurant, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_restaurant[0][$i]["distance"]);
    }

    $_SESSION['score_restaurant'] = 0;
    $cpt = 4;
    $array_effectif = array(5, 10, 15, 20, 50);

    foreach ($array_distances as $distance) {
        if ($distance <= 150 ) {
            $_SESSION['score_restaurant'] += 1 * $array_effectif[$cpt];
        } else if ( $distance <= 450) {
            $_SESSION['score_restaurant'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance <= 900) {
            $_SESSION['score_restaurant'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 1300) {
            $_SESSION['score_restaurant'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 1800) {
            $_SESSION['score_restaurant'] += 0.2 * $array_effectif[$cpt];
        }
        $cpt--;
    }
}

?>