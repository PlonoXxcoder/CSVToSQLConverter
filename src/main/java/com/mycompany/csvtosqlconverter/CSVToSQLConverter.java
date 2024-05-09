/**
 * @file CSVToSQLConverter.java
 * @brief Classe pour convertir les fichiers CSV en instructions SQL.
 * @author PlonoXxCoder
 * @version 0.4.0-beta
 * @date 01/05/2024 
 */

package com.mycompany.csvtosqlconverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
            "Test.csv",
            "Testv2.csv",// Add other CSV file paths here
        };

        for (String csvFile : csvFiles) {
            convertCSVToSQL(csvFile);
        }
    }

    /**
     * Méthode pour convertir un fichier CSV en instructions SQL.
     *
     * @param csvFile Chemin du fichier CSV à convertir.
     */
    public static void convertCSVToSQL(String csvFile) {
        String line = "";
        String cvsSplitBy = ",";

        // Extraire le nom du fichier sans l'extension
        String tableName = Paths.get(csvFile).getFileName().toString().replaceFirst("[.][^.]+$", "");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String[] columnNames = br.readLine().split(cvsSplitBy);
            String[] dataTypes = new String[columnNames.length];

            // Lire toutes les lignes de données pour détecter les types de données
            List<String[]> valuesList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                valuesList.add(line.split(cvsSplitBy));
            }

            for (int i = 0; i < columnNames.length; i++) {
                String[] columnValues = new String[valuesList.size()];
                for (int j = 0; j < valuesList.size(); j++) {
                    columnValues[j] = valuesList.get(j)[i];
                }
                dataTypes[i] = detectColumnDataType(columnValues).toString();
            }

            // Réinitialiser le lecteur pour relire le fichier CSV
            BufferedReader br2 = new BufferedReader(new FileReader(csvFile));

            // Ignorer la première ligne (noms de colonnes)
            br2.readLine();

            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");

            // Remplacer les caractères spéciaux par 'e' dans les noms de colonnes
            for (int i = 0; i < columnNames.length; i++) {
                String replacedName = columnNames[i].replace('�', 'e');
                sql.append("\n").append(replacedName).append(" ").append(dataTypes[i]).append(",");
            }

            sql = new StringBuilder(sql.substring(0, sql.length() - 1) + "\n);");

            System.out.println(sql.toString());

            while ((line = br2.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);

                sql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");

                // Remplacer les caractères spéciaux par 'e' dans les données
                for (String value : values) {
                    String replacedValue = value.replace('�', 'e');
                    sql.append("'").append(replacedValue).append("', ");
                }

                sql = new StringBuilder(sql.substring(0, sql.length() - 2) + ");");

                System.out.println(sql.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enumération des types de données SQL.
     */
    public enum DataType {
        INT,
        FLOAT,
        DATE,
        VARCHAR;
    }

    /**
     * Méthode pour détecter le type de données d'une valeur.
     *
     * @param value Valeur à analyser.
     * @return Type de données détecté.
     */
    public static DataType detectDataType(String value) {
        // Vérifier si la valeur est un entier
        if (value.matches("-?\\d+")) {
            return DataType.INT;
        }

        // Vérifier si la valeur est un réel
        if (value.matches("-?\\d+(\\.\\d+)?")) {
            return DataType.FLOAT;
        }

        // Vérifier si la valeur est une date (format : YYYY-MM-DD)
        if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return DataType.DATE;
        }

        // Si aucun type spécifique n'est détecté, utiliser VARCHAR par défaut
        return DataType.VARCHAR;
    }

    /**
     * Méthode pour détecter le type de données d'une colonne.
     *
     * @param values Liste des valeurs de la colonne.
     * @return Type de données détecté.
     */
    public static String detectColumnDataType(String[] values) {
        DataType dataType = DataType.VARCHAR;

        // Calculer la longueur maximale des valeurs
        int length = 0;
        for (String value : values) {
            if (value != null && value.length() > length) {
                length = value.length();
            }
        }

        // Vérifier si toutes les valeurs sont des entiers
        boolean allIntegers = true;
        for (String value : values) {
            if (value == null || !value.matches("\\d+")) {
                allIntegers = false;
                break;
            }
        }
        if (allIntegers) {
            dataType = DataType.INT;
        }

        // Vérifier si toutes les valeurs sont des réels
        boolean allReals = true;
        for (String value : values) {
            if (value == null || !value.matches("\\d+(\\.\\d+)?")) {
                allReals = false;
                break;
            }
        }
        if (allReals) {
            dataType = DataType.FLOAT;
        }

        // Vérifier si toutes les valeurs sont des dates
        boolean allDates = true;
        for (String value : values) {
            if (value == null || !value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                allDates = false;
                break;
            }
        }
        if (allDates) {
            dataType = DataType.DATE;
        }

        // Ajouter une marge de 10 à la longueur de VARCHAR
        if (dataType == DataType.VARCHAR) {
            return "VARCHAR(" + (length + 10) + ")";
        }

        // Sinon, renvoyer le type de données détecté
        return dataType.toString();
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