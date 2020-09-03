<?php

function calcul_score_cafe ($array) {

    $array_cafe = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "Café") {
            array_push($array_cafe, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_cafe[0][$i]["distance"]);
    }

    $_SESSION['score_cafe'] = 0;
    $cpt = 4;
    $array_effectif = array(5, 10, 15, 20, 50);

    foreach ($array_distances as $distance) {
        if ($distance <= 150 ) {
            $_SESSION['score_cafe'] += 1 * $array_effectif[$cpt];
        } else if ( $distance <= 250) {
            $_SESSION['score_cafe'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance <= 600) {
            $_SESSION['score_cafe'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 800) {
            $_SESSION['score_cafe'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 1300) {
            $_SESSION['score_cafe'] += 0.2 * $array_effectif[$cpt];
        }
        $cpt--;
    }
}
?>