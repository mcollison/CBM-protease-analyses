/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ncl.ssip.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author nmc91
 */
public class MapGenes2CatalogAndTaxonomy {

    public static void main(String[] args) {
        try {

            //map gene id to config label
            String root = "/usr/local/apache2/htdocs/cbm-protease/data/";
            String fileroot = root + "cbm-protease-fastas/nr-unannotated/";

            File fastaFolder = new File(fileroot);
            for (File file : fastaFolder.listFiles()) {

                String filename = file.getName().substring(0, 3);

                System.out.println(fileroot + filename);
//                File file = new File(fileroot + filename + ".fasta");
                Scanner fastaSc = new Scanner(file);

                FileWriter out = new FileWriter(root + "cbm-protease-annotations/" + filename + ".annotations");
                BufferedWriter outbuff = new BufferedWriter(out);
                //for each sequence get the top catalog hit with % identity and coverage 
                //for each sequence then get the top NR hit with % identity and coverage and retrieve taxa

                String geneid = "";
                while (fastaSc.hasNextLine()) {
                    String line = fastaSc.nextLine();
                    if (line.startsWith(">")) {
                        geneid = line.split("\t")[0].substring(1);
                        outbuff.write(geneid + "\t");

                        File bgiBlastfile = new File(root + "/BGI-blast/" + filename + "-BGI.blast");
                        Scanner bgiBlastSc = new Scanner(bgiBlastfile);
                        File nrBlastfile = new File(root + "NR-blast/" + filename + "-NR.blast");
                        Scanner nrBlastSc = new Scanner(nrBlastfile);
                        while (bgiBlastSc.hasNextLine()) {
                            String bgiblastline = bgiBlastSc.nextLine();
                            if (bgiblastline.startsWith(geneid)) {
                                String[] bgisplits = bgiblastline.split("\t");
                                outbuff.write(bgisplits[1].split("#")[0] + "\t" + bgisplits[2] + "\t");
                                break;
                            }
                        }
                        boolean nrhit = false;
                        while (nrBlastSc.hasNextLine()) {
                            String nrblastline = nrBlastSc.nextLine();
                            if (nrblastline.startsWith(geneid)) {
                                String[] nrsplits = nrblastline.split("\t");
                                String[] gi = nrsplits[1].split("\\|");
                                outbuff.write(gi[1] + "\t" + nrsplits[2] + "\n");
                                nrhit=true;
                                break;
                            }
                        }
                        if(!nrhit){
                            outbuff.write("\n");
                        }

                    }
                }
                outbuff.close();
                out.close();
            }
        } catch (Exception ex) {
            System.out.println("exception thing ");
            ex.printStackTrace();
        }
    }
}
