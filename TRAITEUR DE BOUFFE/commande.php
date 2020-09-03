<!DOCTYPE html>
<html lang="fr">
<head>
	<title>Commande | MR. FRESH</title>
	<meta charset="utf-8">
	<link href="https://fonts.googleapis.com/css2?family=Abel&display=swap" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="styles.css">
	<style>
	body {
		background-image: url("images/background2.jpg");
	}
	.erreur {
		color: #FF0000;
	}
</style>
<?php
$nomparent = $_POST['nom-parent'];
$nomenfant = $_POST['nom-enfant'];
$nomecole = $_POST['ecole'];
$age = $_POST['age'];
$lundi = $_POST['lundi'];
$mardi = $_POST['mardi'];
$mercredi = $_POST['mercredi'];
$jeudi = $_POST['jeudi'];
$vendredi = $_POST['vendredi'];
?>
</head>

<body>

	<?php

	if (isset($_POST['submit'])) {
		if (!empty($nomparent) && (strpos($nomparent, ",") === false) && !empty($nomenfant) && 
			(strpos($nomenfant, ",") === false) && !empty($nomecole) && (strpos($nomecole, ",") === false) &&
			!empty($age) && $age >= 4 && $age <= 12 && !empty($lundi) && !empty($mardi) && !empty($mercredi) &&
			!empty($jeudi) && !empty($vendredi)) {
			header("Location: commande_recue.html");
		$file = fopen("commande_archive.txt", a);
		fwrite($file, 'Parent : ');
		fwrite($file, $nomparent.', ');
		fwrite($file, 'Enfant : ');
		fwrite($file, $nomenfant.', ');
		fwrite($file, 'Âge : ');
		fwrite($file, $age.', ');
		fwrite($file, 'École : ');
		fwrite($file, $nomecole.', ');
		fwrite($file, 'Lundi : ');
		fwrite($file, $lundi.', ');
		fwrite($file, 'Mardi : ');
		fwrite($file, $mardi.', ');
		fwrite($file, 'Mercredi : ');
		fwrite($file, $mercredi.', ');
		fwrite($file, 'Jeudi : ');
		fwrite($file, $jeudi.', ');
		fwrite($file, 'Vendredi : ');
		fwrite($file, $vendredi."\n");
		fclose($file);
	} else {
		$parentErr = $enfantErr = $ecoleErr = $ageErr = $lundiErr = $mardiErr = $mercrediErr = $jeudiErr = $vendrediErr = "";
		if  (empty($nomparent)) {
			$parentErr = "ce champ ne peut pas être vide";		
		}
		if  (empty($nomenfant)) {
			$enfantErr = "ce champ ne peut pas être vide";
		}
		if  (empty($nomecole)) {
			$ecoleErr = "ce champ ne peut pas être vide";
		}
		if  (!is_numeric($age)) {
			$ageErr = "ce champ ne peut pas être vide";
		}
		if (strpos($nomparent, ",") !== false) {
			$parentErr = "ce champ ne peut pas contenir une virgule";
			$nomparent = "";
		}
		if (strpos($nomenfant, ",") !== false) {
			$enfantErr = "ce champ ne peut pas contenir une virgule";
			$nomenfant = "";
		}
		if (strpos($nomecole, ",") !== false) {
			$ecoleErr = "ce champ ne peut pas contenir une virgule";
			$nomecole = "";
		}
		if (($age < 4 || $age > 12) && is_numeric($age)) {
			$ageErr = "l'âge doit être entre 4 et 12 inclusivement";
			$age = "";
		}
		if (empty($lundi)) {
			$lundiErr = "vous n'avez pas choisi de plat pour cette journée";
		}
		if (empty($mardi)) {
			$mardiErr = "vous n'avez pas choisi de plat pour cette journée";
		}
		if (empty($mercredi)) {
			$mercrediErr = "vous n'avez pas choisi de plat pour cette journée";
		}
		if (empty($jeudi)) {
			$jeudiErr = "vous n'avez pas choisi de plat pour cette journée";
		}
		if (empty($vendredi)) {
			$vendrediErr = "vous n'avez pas choisi de plat pour cette journée";
		}
	}
}
?>

<div id="social">
	<img src="icon/social.png" alt="social" width="100" height=30>
</div>
<div style="position: relative">
	<div class="header">
		<div id="logo">
			<a href="index.html"><img src="logo/mrfresh.png" alt="png" width="150" height=150></a>
		</div>
		<div class="menu">
			<ul>
				<li><a href="informations.html">Informations</a></li>
				<li><a href="commande.php">Commande</a></li>
				<li><a href="contact.html">Contact</a></li>
			</ul>
		</div>
	</div>
	<div class="choix">
		<p><span class="erreur">* champs requis</span></p>
		<form action="commande.php" method="post">
			<div>
				<label for="nom-parent">Nom complet du parent : <br></label>
				<input id="nom-parent" type="text" name="nom-parent" size="25" value = "<?php echo $nomparent;?>">
				<span class="erreur">* <?php echo $parentErr;?></span>
			</div>
			<div>
				<label for="nom-enfant">Nom complet de l'enfant : <br></label>
				<input id="nom-enfant" type="text" name="nom-enfant" size="25" value = "<?php echo $nomenfant;?>">
				<span class="erreur">* <?php echo $enfantErr;?></span>
			</div>
			<div>
				<label for="ecole">Nom de l'école : <br></label>
				<input id="ecole" type="text" name="ecole" size="25" value = "<?php echo $nomecole;?>">
				<span class="erreur">* <?php echo $ecoleErr;?></span>
			</div>
			<div>
				<label for="age">L'âge de votre enfant : <br></label>
				<input id="age" type="text" name="age" size="5" value = "<?php echo $age;?>">
				<span class="erreur">* <?php echo $ageErr;?></span>
			</div>
			<div><br><br>
				<h2>Veuillez choisir un choix par jour : </h2><br>
				Lundi : <span class="erreur">* <?php echo $lundiErr;?></span><br>
				<input type="radio" name="lundi" value="Pizza" <?php if(isset($lundi) && $lundi == "Pizza")  echo 'checked="checked"';?>> Pizza<br>
				<input type="radio" name="lundi" value="Pita shish kebab" <?php if(isset($lundi) && $lundi == "Pita shish kebab")  echo 'checked="checked"';?>> Pita shish kebab<br>
			</div>
			<br>
			<div>
				Mardi : <span class="erreur">* <?php echo $mardiErr;?></span><br>
				<input type="radio" name="mardi" value="Burger"<?php if(isset($mardi) && $mardi == "Burger")  echo 'checked="checked"';?>> Burger<br>
				<input type="radio" name="mardi" value="Tacos" <?php if(isset($mardi) && $mardi == "Tacos")  echo 'checked="checked"';?>> Tacos<br>
			</div>
			<br>
			<div>
				Mercredi : <span class="erreur">* <?php echo $mercrediErr;?></span><br>
				<input type="radio" name="mercredi" value="Salade aux oeufs" <?php if(isset($mercredi) && $mercredi == "Salade aux oeufs")  echo 'checked="checked"';?>> Salade aux oeufs<br>
				<input type="radio" name="mercredi" value="Hot Dog" <?php if(isset($mercredi) && $mercredi == "Hot Dog")  echo 'checked="checked"';?>> Hot Dog<br>
			</div>
			<br>
			<div>
				Jeudi : <span class="erreur">* <?php echo $jeudiErr;?></span><br>
				<input type="radio" name="jeudi" value="Combo sushi" <?php if(isset($jeudi) && $jeudi == "Combo sushi")  echo 'checked="checked"';?>> Combo sushi<br>
				<input type="radio" name="jeudi" value="Ailes de poulet"<?php if(isset($jeudi) && $jeudi == "Ailes de poulet")  echo 'checked="checked"';?>> Ailes de poulet<br>
			</div>
			<br>
			<div>
				Vendredi : <span class="erreur">* <?php echo $vendrediErr;?></span><br>
				<input type="radio" name="vendredi" value="Bagel jambon/fromage" <?php if(isset($vendredi) && $vendredi == "Bagel jambon/fromage")  echo 'checked="checked"';?>> Bagel jambon/fromage<br>
				<input type="radio" name="vendredi" value="Soupe du jour" <?php if(isset($vendredi) && $vendredi == "Soupe du jour")  echo 'checked="checked"';?>> Soupe du jour<br>
			</div>
			<br><br>
			<input type="submit" value="Passez votre commande" name="submit">
		</form>
	</div>
	<div id="menu_semaine">
		<img src="images/menu_semaine.png" alt="menu_semaine" width="650" height=900>
	</div>
</div>
</body>
</html>	