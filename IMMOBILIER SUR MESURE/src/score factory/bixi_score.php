<?php
session_start();

function calcul_score_bixi($array)
{

    $array_bixi = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "stations_bixi_p") {
            array_push($array_bixi, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_bixi[0][$i]["distance"]);
    }

    $_SESSION['score_bixi'] = 0;
    $cpt = 0;
    $array_effectif = array(40, 30, 15 , 10 ,5);

    foreach ($array_distances as $distance) {
        if ($distance <= 150) {
            $_SESSION['score_bixi'] += 1 * $array_effectif[$cpt];
        } else if ($distance <= 500) {
            $_SESSION['score_bixi'] += 0.8 * $array_effectif[$cpt];
        } else if ($distance <= 850) {
            $_SESSION['score_bixi'] += 0.6 * $array_effectif[$cpt];
        } else if ($distance <= 1000) {
            $_SESSION['score_bixi'] += 0.4 * $array_effectif[$cpt];
        } else if ($distance <= 1500) {
            $_SESSION['score_bixi'] += 0.2 * $array_effectif[$cpt];
        }
        $cpt++;
    }

}

?>