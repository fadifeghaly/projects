<?php

session_start();
?>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Score final</title>
  <meta content="width=device-width" name="viewport">
  <link href="style.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet">
  <style >
    body {
     font-family: 'Montserrat', serif;
     color: black;
     margin: 0;
     padding: 0;
     height: 100%;
     background-image: linear-gradient(to right, #8170dc, #0a5a9f);
     min-width: 350px;
   }</style>
 </head>
 <body onload="myFunction()" style="margin:0;">
  <div id="loader"></div>
  <div class="animate-bottom" id="myDiv" style="display:none;">
    <h2>En fonction des critères séléctionnés,<br>voici le score final obtenu !</h2>
    <div class="container2">
      <div id="danger"> 
       <?php
       if ($_SESSION['retours_carr']){
        echo "<p style='font-size: larger; color:red;'>Attention Danger !!!! :<br> Votre résidence se situe a l'intérieur ou près d'une ancienne carrière<br><a href='http://ville.montreal.qc.ca/portal/page?_pageid=7237,142322407&_dad=portal&_schema=PORTAL'>INFORMATION SUR LE RISQUE (Ville de Montréal)</a></p>";
      };
      if ($_SESSION['retours_inon']){
       echo "<p style='font-size: larger;color:red; '>Attention Danger !!!! :<br>Votre résidence se situe a l'intérieur ou près d'une zone inondable cartographiée !!! <br><a href='http://ville.montreal.qc.ca/portal/page?_pageid=7637,82391659&_dad=portal&_schema=PORTAL'>INFORMATION SUR LE RISQUE (Ville de Montréal)</a></p>";

     };
     ?>
   </div>
   <div class="gauge" id="gg1"><br></div>
</div>
<div class="container3">
  <div>
    <a href="../../index.html">
      <img alt="home" src="../image/search.png" width="50" height="50">
    </a>
    <p style="color: white">Faire une autre recherche</p>
  </div>
  <div>
    <a href="explained.php">
      <img alt="details" src="../image/report.png" width="50" height="50">
    </a>
    <p style="color: white">Version détaillée du score final</p>
  </div>
  <div>
    <a href="cartocriteres.php">
      <img alt="carto" src="../image/map.png" width="50" height="50">
    </a>
    <p style="color: white">Cartographie de la résidence et des critères</p>
  </div>
</div>
<script src="../raphael-2.1.4.min.js"></script>
<script src="../justgage.js"></script>
<script>
  document.addEventListener("DOMContentLoaded", function(event) {
    var score = "<?php echo $_SESSION['score_final']; ?>";
    var gg1 = new JustGage({
      id: "gg1",
      value : score,
      min: 0,
      max: 100,
      decimals: 2,
      title: "Score final",
      gaugeWidthScale: 0.6,
      customSectors: [{
        color : "#ff0000",
        lo : 0,
        hi : 50
      },{
        color : "#fbf911",
        lo : 51,
        hi : 69
      },{
        color : "#00ff00",
        lo : 70,
        hi : 100
      }],
      counter: true
    });
  });
</script>

</body>
</html>