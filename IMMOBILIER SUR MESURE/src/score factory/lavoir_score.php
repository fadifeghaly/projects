<?php
function calcul_score_lavoir ($array) {

    $array_lavoir = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "Lavoir") {
            array_push($array_lavoir, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_lavoir[0][$i]["distance"]);
    }

    $_SESSION['score_lavoir'] = 0;
    $cpt = 4;
    $array_effectif = array(5, 10, 15, 20, 50);

    foreach ($array_distances as $distance) {
        if ($distance <= 500 ) {
            $_SESSION['score_lavoir'] += 1 * $array_effectif[$cpt];
        } else if ( $distance <= 1500) {
            $_SESSION['score_lavoir'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance <= 2500) {
            $_SESSION['score_lavoir'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 3500) {
            $_SESSION['score_lavoir'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 5000) {
            $_SESSION['score_lavoir'] += 0.2 * $array_effectif[$cpt];
        }
        $cpt--;
    }
}
?>