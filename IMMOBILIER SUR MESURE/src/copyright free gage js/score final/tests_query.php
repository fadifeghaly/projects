<?php
/**
 * Created by PhpStorm.
 * User: Jian, jp; classe de test
 * Date: 4/16/2019
 * Time: 9:00 PM
 */

session_start();

echo "<br>SCORE FINAL<br>";
var_dump($_SESSION['score_final']);
echo "<br>RETOURS_BD<br>";
var_dump($_SESSION['retours_bd']);
echo "<br>lng (longitude)<br>";
var_dump($_SESSION['lng']);
echo "<br>lat (longitude)<br>";
var_dump($_SESSION['lat']);
echo "<br>adresse<br>";
var_dump($_SESSION['adresse']);
echo "<br>retours_inon (si null pas dans zone)<br>";
var_dump($_SESSION['retours_inon']);
echo "<br>retours_carr (si null pas dans zone)<br>";
var_dump($_SESSION['retours_carr']);
