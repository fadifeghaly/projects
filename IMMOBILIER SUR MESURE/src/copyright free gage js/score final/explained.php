<?php
session_start();
?>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Score détaillé</title>
  <meta content="width=device-width" name="viewport">
  <link href="style.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet">
  <style>
    body {
     font-family: 'Montserrat', serif;
     color: black;
     margin: 0;
     padding: 0;
     height: 100%;
     background-image: linear-gradient(to right, #8170dc, #0a5a9f);
     min-width: 350px;
   }
   
   .position {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    grid-template-rows: 1fr 1fr 1fr;
  }

</style>
</head>
<body onload="myFunction()" style="margin:0;">
  <div class="container1">
    <div class="position">
      <div id="crimes"></div>
      <div id="ecoles"></div>
      <div id="stations"></div>
      <div id="punaises"></div>
      <div id="cafe"></div>
      <div id="bar"></div>
      <div id="epicerie"></div>
      <div id="lavoir"></div>
      <div id="pharmacie"></div>
      <div id="restaurant"></div>
      <div id="bixi"></div>
      <div id="jardins"></div>
    </div>
    <h2>Tableau de bord</h2><br>
    <br><a href="score.php">
      <img alt="back" src="../image/back.png" width="50" height="50">
    </a>
    <p style="color: white">Retour à la page précédente</p>
  </div>

  <script src="../raphael-2.1.4.min.js"></script>
  <script src="../justgage.js"></script>
  <script>
    document.addEventListener("DOMContentLoaded", function(event) {
      var gage_ecoles = "<?php echo $_SESSION['gage_ecoles']; ?>";
      var gage_crimes = "<?php echo $_SESSION['gage_crimes']; ?>";
      var gage_stations = "<?php echo $_SESSION['gage_metro']; ?>";
      var gage_punaises = "<?php echo $_SESSION['gage_punaises']; ?>";
      var gage_cafe = "<?php echo $_SESSION['gage_cafe']; ?>";
      var gage_bar = "<?php echo $_SESSION['gage_bar']; ?>";
      var gage_epicerie = "<?php echo $_SESSION['gage_epicerie']; ?>";
      var gage_lavoir = "<?php echo $_SESSION['gage_lavoir']; ?>";
      var gage_pharmacie = "<?php echo $_SESSION['gage_pharmacie']; ?>";
      var gage_restaurant = "<?php echo $_SESSION['gage_restaurant']; ?>";
      var gage_bixi = "<?php echo $_SESSION['gage_bixi']; ?>";
      var gage_jardins = "<?php echo $_SESSION['gage_jardins']; ?>";

      var gg1 = new JustGage({
        id: "crimes",
        value : gage_crimes,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Criminalité",
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

      var gg2 = new JustGage({
        id: "ecoles",
        value : gage_ecoles,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Écoles primaires",
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

      var gg3 = new JustGage({
        id: "stations",
        value : gage_stations,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Stations de métro",
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

      var gg4 = new JustGage({
        id: "punaises",
        value : gage_punaises,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Punaises de lit",
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


      var gg5 = new JustGage({
        id: "bar",
        value : gage_bar,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Bar",
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

      var gg6 = new JustGage({
        id: "cafe",
        value : gage_cafe,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Café",
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


      var gg7 = new JustGage({
        id: "epicerie",
        value : gage_epicerie,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Épiceries",
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


      var gg8 = new JustGage({
        id: "lavoir",
        value : gage_lavoir,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Buanderie",
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


      var gg9 = new JustGage({
        id: "pharmacie",
        value : gage_pharmacie,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Pharmacie",
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


      var gg10 = new JustGage({
        id: "restaurant",
        value : gage_restaurant,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Restaurant",
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

      var gg11 = new JustGage({
        id: "bixi",
        value : gage_bixi,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Stations de Bixi",
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

      var gg12 = new JustGage({
        id: "jardins",
        value : gage_jardins,
        min: 0,
        max: 100,
        decimals: 2,
        title: "Jardins communautaires",
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
