<?php
session_start();

function calcul_score_ecoles($array)
{

    $array_ecoles = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "ecoles") {
            array_push($array_ecoles, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_ecoles[0][$i]["distance"]);
    }

    $_SESSION['score_ecoles'] = 0;
    $cpt = 0;
    $array_effectif = array(40, 30, 15 , 10 ,5);

    foreach ($array_distances as $distance) {
        if ($distance <= 350) {
            $_SESSION['score_ecoles'] += 1 * $array_effectif[$cpt];
        } else if ($distance <= 800) {
            $_SESSION['score_ecoles'] += 0.8 * $array_effectif[$cpt];
        } else if ($distance <= 1400) {
            $_SESSION['score_ecoles'] += 0.6 * $array_effectif[$cpt];
        } else if ($distance <= 1700) {
            $_SESSION['score_ecoles'] += 0.4 * $array_effectif[$cpt];
        } else if ($distance <= 3000) {
            $_SESSION['score_ecoles'] += 0.2 * $array_effectif[$cpt];
        }
        $cpt++;
    }

}

?>