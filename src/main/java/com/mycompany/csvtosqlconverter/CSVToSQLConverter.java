package com.mycompany.csvtosqlconverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class CSVToSQLConverter {
    public static void main(String[] args) {
        String csvFile = "C:\\Users\\paulb\\Desktop\\travail\\CSVToSQLConverter\\liste_complete_entreprises_2023_2024.csv";
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
