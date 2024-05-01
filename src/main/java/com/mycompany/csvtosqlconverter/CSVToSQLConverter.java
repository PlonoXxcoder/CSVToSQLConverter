/**
 * Classe pour convertir des fichiers CSV en instructions SQL.
 *
 * @author PlonoXxCoder
 * @version 0.3.1-beta
 * @01/05/2024 
 */


package com.mycompany.csvtosqlconverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @class CSVToSQLConverter
 * @brief Classe pour convertir les fichiers CSV en requêtes SQL.
 */
public class CSVToSQLConverter {

    /**
     * Point d'entrée du programme.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        // Liste des fichiers CSV
        String[] csvFiles = {
            "Votre chemin ",
            "Second chemin",
            // Add other CSV file paths here
        };

        for (String csvFile : csvFiles) {
            convertCSVToSQL(csvFile);
        }
    }

    /**
     * Méthode pour convertir un fichier CSV en instructions SQL.
     *
     * @param csvFile Chemin du fichier CSV à convertir.
     * @brief Cette fonction lit un fichier CSV, extrait les noms de colonnes et les valeurs, puis génère des instructions SQL
     *        pour créer une table et insérer des données dans cette table.
     * @fn convertCSVToSQL
     */
    public static void convertCSVToSQL(String csvFile) {
        String line = "";
        String cvsSplitBy = ",";

        // Extraire le nom du fichier sans l'extension
        String tableName = Paths.get(csvFile).getFileName().toString().replaceFirst("[.][^.]+$", "");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String[] columnNames = br.readLine().split(cvsSplitBy);

            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (String columnName : columnNames) {
                sql.append("\n").append(columnName).append(" VARCHAR(255),");
            }

            sql = new StringBuilder(sql.substring(0, sql.length() - 1) + "\n);");

            System.out.println(sql.toString());

            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);

                sql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");

                for (String value : values) {
                    sql.append("'").append(value).append("', ");
                }

                sql = new StringBuilder(sql.substring(0, sql.length() - 2) + ");");

                System.out.println(sql.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * @mainpage PlonoXxcoder
 *
 * @section intro_sec Introduction
 *
 * Cette documentation décrit un outil de conversion de fichiers CSV en instructions SQL.
 * L'outil lit un fichier CSV, extrait les noms de colonnes et les valeurs, puis génère des instructions SQL
 * pour créer une table et insérer des données dans cette table.
 *
 * @section install_sec Instructions d'installation
 *
 * Aucune installation spécifique n'est requise pour cet outil. Assurez-vous simplement d'avoir un environnement
 * Java fonctionnel et les dépendances nécessaires.
 *
 * @section usage_sec Instructions d'utilisation
 *
 * Pour utiliser l'outil, exécutez simplement la classe CSVToSQLConverter avec les chemins des fichiers CSV en tant
 * qu'arguments. Les instructions SQL générées seront affichées dans la console.
 *
 * @section auth_sec Informations sur l'auteur
 *
 * Cet outil a été développé par PlonoXxCoder.
 * 
 * Profil githhub : https://github.com/PlonoXxcoder
 */