<?php
session_start();
include("DB_Query.php");
include("score factory/ecoles_score.php");
include("score factory/crimes_score.php");
include("score factory/metro_score.php");
include("score factory/punaises_score.php");
include("score factory/restaurant_score.php");
include("score factory/bar_score.php");
include("score factory/cafe_score.php");
include("score factory/lavoir_score.php");
include("score factory/pharmacie_score.php");
include("score factory/epicerie_score.php");
include("score factory/bixi_score.php");
include("score factory/jardins_score.php");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
	$_SESSION['criteres'] = $_POST['criteres'];
	$_SESSION['lat'] = $_POST['lat'];
	$_SESSION['lng'] = $_POST['lng'];
    $_SESSION['adresse'] = $_POST['adresse'];


    $crimes_slider = 0;
    $ecoles_slider = 0;
    $stations_metro_slider = 0;
    $punaises_slider = 0;
    $bar_slider = 0;
    $cafe_slider = 0;
    $lavoir_slider = 0;
    $pharmacie_slider = 0;
    $restaurant_slider = 0;
    $epicerie_slider = 0;
    $bixi_slider = 0;
    $jardins_slider = 0;

    $_SESSION['score_ecoles'] = 0;
    $_SESSION['score_crimes'] = 0;
    $_SESSION['score_metro'] = 0;
    $_SESSION['score_punaises'] = 0;
    $_SESSION['score_bar'] = 0;
    $_SESSION['score_cafe'] = 0;
    $_SESSION['score_lavoir'] = 0;
    $_SESSION['score_pharmacie'] = 0;
    $_SESSION['score_restaurant'] = 0;
    $_SESSION['score_epicerie'] = 0;
    $_SESSION['score_bixi'] = 0;
    $_SESSION['score_jardins'] = 0;

    if (count($_SESSION['criteres']) != 0) {
      receive_query();
      foreach ($_SESSION['criteres'] as $selectionne) {
       if ($selectionne == 'crimes_p') {
        $crimes_slider = $_POST['slider1'];
        calcul_score_crimes($_SESSION['retours_bd']);
    }

    if ($selectionne == 'ecoles') {
        $ecoles_slider = $_POST['slider2'];
        calcul_score_ecoles($_SESSION['retours_bd']);
    }

    if ($selectionne == 'stations_metro_p') {
        $stations_metro_slider = $_POST['slider3'];
        calcul_score_metro($_SESSION['retours_bd']);
    }

    if ($selectionne == 'punaises_p') {
        $punaises_slider = $_POST['slider4'];
        calcul_score_punaises($_SESSION['retours_bd']);
    }
    if ($selectionne == 'Restaurant') {
        $restaurant_slider = $_POST['slider5'];
        calcul_score_restaurant($_SESSION['retours_bd']);

    }

    if ($selectionne == 'Café') {
        $cafe_slider = $_POST['slider6'];
        calcul_score_cafe($_SESSION['retours_bd']);

    }

    if ($selectionne == 'Pharmacies') {
        $pharmacie_slider = $_POST['slider7'];
        calcul_score_pharmacie($_SESSION['retours_bd']);

    }

    if ($selectionne == 'Épicerie') {
        $epicerie_slider = $_POST['slider8'];
        calcul_score_epicerie($_SESSION['retours_bd']);

    }

    if ($selectionne == 'Lavoir') {
        $lavoir_slider = $_POST['slider9'];
        calcul_score_lavoir($_SESSION['retours_bd']);

    }

    if ($selectionne == 'Bar') {
        $bar_slider = $_POST['slider10'];
        calcul_score_bar($_SESSION['retours_bd']);

    }

    if ($selectionne == 'stations_bixi_p') {
        $bixi_slider = $_POST['slider11'];
        calcul_score_bixi($_SESSION['retours_bd']);

    }

    if ($selectionne == 'jardins_communautaires_p') {
        $jardins_slider = $_POST['slider12'];
        calcul_score_jardins($_SESSION['retours_bd']);

    }

}
}

$somme_sliders = ($crimes_slider + $ecoles_slider + $punaises_slider + $stations_metro_slider
    + $restaurant_slider + $bar_slider +$cafe_slider + $lavoir_slider + $pharmacie_slider
    + $epicerie_slider + $bixi_slider + $jardins_slider) * 100;

      //---------------------------------------TEMPORAIRE----------------------------------------//
$_SESSION['gage_ecoles'] = $_SESSION['score_ecoles'];
$_SESSION['gage_crimes'] = $_SESSION['score_crimes'];
$_SESSION['gage_metro'] = $_SESSION['score_metro'];
$_SESSION['gage_punaises'] = $_SESSION['score_punaises'];
$_SESSION['gage_bar'] = $_SESSION['score_bar'];
$_SESSION['gage_cafe'] = $_SESSION['score_cafe'];
$_SESSION['gage_lavoir'] = $_SESSION['score_lavoir'];
$_SESSION['gage_pharmacie'] = $_SESSION['score_pharmacie'];
$_SESSION['gage_restaurant'] = $_SESSION['score_restaurant'];
$_SESSION['gage_epicerie'] = $_SESSION['score_epicerie'];
$_SESSION['gage_bixi'] = $_SESSION['score_bixi'];
$_SESSION['gage_jardins'] = $_SESSION['score_jardins'];
	 //-----------------------------------------------------------------------------------------//

$_SESSION['score_final'] = (((($ecoles_slider * 100) / $somme_sliders) * $_SESSION['score_ecoles'])
    + ((($crimes_slider * 100) / $somme_sliders) * $_SESSION['score_crimes'])
    + ((($stations_metro_slider * 100) / $somme_sliders) * $_SESSION['score_metro'])
    + ((($bar_slider * 100) / $somme_sliders) * $_SESSION['score_bar'])
    + ((($cafe_slider * 100) / $somme_sliders) * $_SESSION['score_cafe'])
    + ((($lavoir_slider * 100) / $somme_sliders) * $_SESSION['score_lavoir'])
    + ((($pharmacie_slider * 100) / $somme_sliders) * $_SESSION['score_pharmacie'])
    + ((($restaurant_slider * 100) / $somme_sliders) * $_SESSION['score_restaurant'])
    + ((($epicerie_slider * 100) / $somme_sliders) * $_SESSION['score_epicerie'])
    + ((($punaises_slider * 100) / $somme_sliders) * $_SESSION['score_punaises'])
    + ((($bixi_slider * 100) / $somme_sliders) * $_SESSION['score_bixi'])
    + ((($jardins_slider * 100) / $somme_sliders) * $_SESSION['score_jardins']));

    //query_danger_inon($_SESSION['lng'] ,$_SESSION['lat']);
    //query_danger_carriere($_SESSION['lng'] ,$_SESSION['lat']);

$path = "copyright free gage js/score final/score.php";
header("Location: $path");
exit;
}

?>