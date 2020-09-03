<?php
function calcul_score_epicerie ($array) {

    $array_epicerie = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "Épicerie") {
            array_push($array_epicerie, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_epicerie[0][$i]["distance"]);
    }

    $_SESSION['score_epicerie'] = 0;
    $cpt = 5;
    $array_effectif = array(5, 10, 12 , 18 , 22 , 33);

    foreach ($array_distances as $distance) {
        if ($distance <= 400 ) {
            $_SESSION['score_epicerie'] += 1 * $array_effectif[$cpt];
        } else if ( $distance <= 800) {
            $_SESSION['score_epicerie'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance <= 1200) {
            $_SESSION['score_epicerie'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 2500) {
            $_SESSION['score_epicerie'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 3300) {
            $_SESSION['score_epicerie'] += 0.2 * $array_effectif[$cpt];
        } else if ( $distance > 3300) {
            $_SESSION['score_epicerie'] += 0.1 * $array_effectif[$cpt];
        }
        $cpt--;
    }
}
?>