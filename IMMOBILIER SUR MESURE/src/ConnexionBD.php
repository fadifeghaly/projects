<?php

class ConnexionBD
{
    protected $bd;

    /**
     * ConnexionBD constructor.
     */
    public function __construct()
    {
        $param_connection = parse_ini_file('database.ini');

        if ($param_connection === false) {
            echo "Erreur de connection- verifier le fichier de configuration";
        } else {
            $chaine_connexion = "host=" . $param_connection['host'] .
                " dbname=" . $param_connection['database'] .
                " user=" . $param_connection['user'] .
                " password=" . $param_connection['password'];

            $varConnect = NULL;

            try {
                $varConnect = pg_connect($chaine_connexion);
                $this->bd = $varConnect;
            } catch (Exception $e) {
                echo 'ERROR: ' . $e->getMessage();
            }

        }

    }

    /**
     * @return mixed
     */
    public function getBd()
    {
        return $this->bd;
    }


}
?>

