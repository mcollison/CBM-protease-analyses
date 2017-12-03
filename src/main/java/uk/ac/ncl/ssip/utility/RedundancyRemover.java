/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ncl.ssip.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 *
 * @author nmc91
 */
public class RedundancyRemover {

    public static void main(String[] args) {
        try {
            File dir = new File("E://website/cbm-protease/data/cbm-protease-fastas/cbm-protease-fastas/");
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith("C40.msa.fasta")) {

                    FileWriter fw = new FileWriter(new File("E://website/cbm-protease/data/cbm-protease-fastas/" + file.getName().substring(0, file.getName().length() - 4) + ".fasta"));
                    BufferedWriter bw = new BufferedWriter(fw);

                    Scanner sc = new Scanner(file);
                    String id = "";
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        if (line.startsWith(">")) {
                            if (line.equals(id)) {
                                sc.nextLine();
                                continue;
                            }
                            id = line;
                        }
                        bw.write(line + "\n");
                    }

                    bw.close();
                    fw.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR");
            ex.printStackTrace();
        }
    }
}
