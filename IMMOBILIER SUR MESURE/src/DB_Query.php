<?php

session_start();
include('ConnexionBD.php');
include ('Query_Factory.php');

function receive_query() {
    $bd = (new ConnexionBD())->getBd();
    $_SESSION['retours_bd'] = array();
    for ($i = 0; $i < count($_SESSION['criteres']); $i++) {
        $_SESSION['resultat'] = pg_exec($bd, generate_Query($_SESSION['lng'],
            $_SESSION['lat'],
            $_SESSION['criteres'][$i]));

        $temp = 0;
        while ( $resultat_ligne= pg_fetch_array( $_SESSION['resultat'], NULL, PGSQL_ASSOC ) ) {
            $reponse[$temp] = $resultat_ligne;
            $temp++;
        }

        array_push($_SESSION['retours_bd'], $reponse, $_SESSION['criteres'][$i]);
    }



    return $_SESSION['retours_bd'];
}



function query_danger_inon($longi,$lati)
{
    //$longi = $_SESSION['lng'];

    //$lati = $_SESSION['lat'];


    $requeteInon = "select vuln_cat as label, st_asgeojson(zone.geom) as geojson from zone_a_risque_inondations_s as zone where  
                    ST_Within(ST_GeomFromText('POINT({$longi} {$lati})', 4326),zone.geom )";

/*    $requeteInon = "select vuln_cat as label, st_asgeojson(zone.geom) as geojson from
                    zone_a_risque_inondations_s as zone where  
                    ST_Within(ST_GeomFromText('POINT(-73.8013 45.513074)' , 4326),zone.geom )";*/


    //$requeteInon = "select * from zone_a_risque_inondations_s";


    $bd = (new ConnexionBD())->getBd();

    $_SESSION['retours_inon'] = array();


    $resultatsinon = pg_exec($bd, $requeteInon);


    $_SESSION['retours_inon'] = pg_fetch_all($resultatsinon);

    /*if (is_null($_SESSION['retours_inon']) || empty($_SESSION['retours_inon'])) {
        unset($_SESSION['retours_inon']);
    }*/
    pg_close($bd);

    return $_SESSION['retours_inon'];

}


function query_danger_carriere($longi,$lati)
{

    // svp ne pas effacer le commentaire suivant; sert a tester si fonctionne avec une coordonne qui tombe dans la zone

    $requeteCarr = "select st_asgeojson(carri.geom)as geojson from carrieres_s as carri where
                        ST_Distance(st_transform(ST_GeomFromText('POINT({$longi} {$lati})', 4326),3857),
                        st_transform(carri.geom,3857))<= 500";

        // svp ne pas effacer le commentaire suivant; sert a tester si fonctionne avec une coordonne qui tombe dans la zone
        /*$requeteCarr = "select st_asgeojson(carri.geom)as geojson from carrieres_s as carri where
                        ST_Distance(st_transform(ST_GeomFromText('POINT(-73.561433 45.53456)', 4326),3857),
                        st_transform(carri.geom,3857))<= 500";*/


    $bd = (new ConnexionBD())->getBd();

    $_SESSION['retours_carr'] = array();


    $resultatsCarr = pg_exec($bd, $requeteCarr);



    $_SESSION['retours_carr'] = pg_fetch_all($resultatsCarr);

    /*if (is_null($_SESSION['retours_carr']) || empty($_SESSION['retours_carr'])) {
        unset($_SESSION['retours_carr']);
    }*/
    pg_close($bd);

    return $_SESSION['retours_carr'];

}


?>