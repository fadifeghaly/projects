<?php
function calcul_score_bar ($array) {

    $array_bar = array();
    for ($i = 0; $i < count($array); $i++) {
        if ($array[$i] == "Bar") {
            array_push($array_bar, $array[$i - 1]);
        }
    }

    $array_distances = array();
    for ($i = 0; $i < 5; $i++) {
        array_push($array_distances, $array_bar[0][$i]["distance"]);
    }

    $_SESSION['score_bar'] = 0;
    $cpt = 4;
    $array_effectif = array(5, 10, 15, 20, 50);

    foreach ($array_distances as $distance) {
        if ($distance <= 200 ) {
            $_SESSION['score_bar'] += 1 * $array_effectif[$cpt];
        } else if ( $distance <= 400) {
            $_SESSION['score_bar'] += 0.8 * $array_effectif[$cpt];
        } else if ( $distance <= 700) {
            $_SESSION['score_bar'] += 0.6 * $array_effectif[$cpt];
        } else if ( $distance <= 1100) {
            $_SESSION['score_bar'] += 0.4 * $array_effectif[$cpt];
        } else if ( $distance <= 1600) {
            $_SESSION['score_bar'] += 0.2 * $array_effectif[$cpt];
        }
        $cpt--;
    }
}
?>