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
public class AddFastaAnnotations {

    public static void main(String[] args) {
        try {
            String dataroot = "E://website/cbm-protease/data/";// /usr/local/apache2/htdocs/cbm-protease/data/";
            String fileroot = dataroot + "cbm-protease-fastas/";// Hard drive path - "E://website/cbm-protease/data/cbm-protease-fastas/"
            File dbCANfolder = new File(fileroot + "nr-unannotated/");
            for (File file : dbCANfolder.listFiles()) {
//                if (file.getName().endsWith(".fasta")) {
                if (    file.getName().endsWith("C02.fasta") || 
                        file.getName().endsWith("M10.fasta") ||
                        file.getName().endsWith("M12.fasta") || 
                        file.getName().endsWith("N06.fasta") || 
                        file.getName().endsWith("N10.fasta") || 
                        file.getName().endsWith("S16.fasta") || 
                        file.getName().endsWith("S55.fasta") ) {

//                if (file.getName().endsWith(".fasta") && (!file.getName().endsWith("C40.fasta")) && (!file.getName().endsWith("M23.fasta")) 
//                        && (!file.getName().endsWith("I43.fasta")) ) {
                    System.out.println(file.getName());
                    FileWriter cbmout = new FileWriter(fileroot + file.getName() + ".fasta");
                    BufferedWriter cbmbw = new BufferedWriter(cbmout);
                    FileWriter csv = new FileWriter(fileroot + file.getName() + ".csv");
                    BufferedWriter csvbw = new BufferedWriter(csv);
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        if (line.startsWith(">")) {
                            String id = line.split("\t")[0].substring(1);
                            System.out.println(id);
//                            for (int i = 1; i < 87; i++) {
                            for (int i = 1; i < 87; i++) {
                                Scanner hmmSc;
                                if (i < 10) {
                                    hmmSc = new Scanner(new File(dataroot + "dbCAN/tabular/MH000" + i + ".dmps"));
                                } else if (i == 29) {
                                    continue;
                                } else {
                                    hmmSc = new Scanner(new File(dataroot + "dbCAN/tabular/MH00" + i + ".dmps"));
                                }
                                while (hmmSc.hasNextLine()) {
                                    String hmmline = hmmSc.nextLine();
                                    if (hmmline.split("\t")[2].equals(id)) {
                                        line = line + "\t" + hmmline.split("\t")[0] + "\t" + hmmline.split("\t")[7] + "\t" + hmmline.split("\t")[8];
                                    }
                                }

                            }
                            for (int i = 1; i < 87; i++) {
//                            for (int i = 67; i < 87; i++) {
                                Scanner hmmSc;
                                if (i < 10) {
                                    hmmSc = new Scanner(new File(dataroot + "MEROPS-blast/MH000" + i + ".merops.blast"));
                                } else if (i == 29) {
                                    continue;
                                } else {
                                    hmmSc = new Scanner(new File(dataroot + "MEROPS-blast/MH00" + i + ".merops.blast"));
                                }
                                while (hmmSc.hasNextLine()) {
                                    String hmmline = hmmSc.nextLine();
                                    if (hmmline.split("\t")[0].equals(id)) {
                                        line = line + "\t" + hmmline.split("\t")[1] + "\t" + hmmline.split("\t")[6] + "\t" + hmmline.split("\t")[7];
                                        break;
                                    }
                                }

                            }
                            csvbw.write(line + "\n");
                        }
                        cbmbw.write(line + "\n");
                    }
                    cbmbw.close();
                    cbmout.close();
                    csvbw.close();
                    csv.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Not reading MSA files correctly");
            ex.printStackTrace();
        }
    }
}
