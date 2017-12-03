/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ncl.ssip.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nmc91
 */
public class ExtractCbmProteaseFasta {

    public static void main(String[] args) {
        try {
            Scanner fastaSc = null;
            Scanner blastSc = null;
            Scanner cdCANsc = null;
            FileWriter out = null;
            BufferedWriter bw = null;
            FileWriter cbmout = null;
            BufferedWriter cbmbw = null;
            String pathprefix = "E://website/cbm-protease/data/";
            Scanner pepunitSc = new Scanner(new File(pathprefix + "MEROPS-pepunit/MEROPS_pepunit.lib"));
            Map<String, String> mer2fam = new HashMap<String, String>();
            Map<String, String> mer2fullfam = new HashMap<String, String>();            
            while (pepunitSc.hasNextLine()) {
                String idLine = pepunitSc.nextLine();
                if (idLine.startsWith(">")) {
                    String MERID = idLine.split(" ")[0].substring(1);
                    String[] details = idLine.split("#");
                    String fullfam = details[0];
                    String family = details[0].substring(details[0].length() - 8, details[0].length() - 5);
//                        String clan = details[1];
//                        String info = details[0];
                    mer2fam.put(MERID, family);
                    mer2fullfam.put(MERID, fullfam);
                }
            }
            int k = 0;
            for (String s : mer2fam.keySet()) {
                if(!mer2fullfam.get(s).endsWith("]")){
//                if (k < 10) {

                    System.out.println(s + ": " + mer2fam.get(s));
                    System.out.println(s + ": " + mer2fullfam.get(s));
                }
//                }
//                k++;
            }
            pepunitSc.close();
            Map<String, List<String>> cbmMerSequences = new HashMap<String, List<String>>();
            Map<String, String> scaf2mer = new HashMap<String, String>();
            for (int i = 80; i < 87; i++) {

                if (i < 10) {
                    fastaSc = new Scanner(new File(pathprefix + "metaHIT-prodigal-fastas/MH000" + i + ".prodigal"));
                    cdCANsc = new Scanner(new File(pathprefix + "dbCAN/tabular/MH000" + i + ".dmps"));
                    blastSc = new Scanner(new File(pathprefix + "MEROPS-blast/filtered/MH000" + i + ".blast.e-val0.01.filtered"));
//                    out = new FileWriter(new File("C:/Users/nmc91/MH000" + i + ".merops.all-dbCAN.csv"));
//                    bw = new BufferedWriter(out);
//                    cbmout = new FileWriter(new File("C:/Users/nmc91/MH000" + i + ".merops.cbm.FIGURE.csv"));
//                    cbmbw = new BufferedWriter(cbmout);

                } else if (i == 29) {
                    continue;
                } else {
                    fastaSc = new Scanner(new File(pathprefix + "metaHIT-prodigal-fastas/MH00" + i + ".prodigal"));
                    cdCANsc = new Scanner(new File(pathprefix + "dbCAN/tabular/MH00" + i + ".dmps"));
                    blastSc = new Scanner(new File(pathprefix + "MEROPS-blast/filtered/MH00" + i + ".blast.e-val0.01.filtered"));
//                    out = new FileWriter(new File("C:/Users/nmc91/MH00" + i + ".merops.all-dbCAN.csv"));
//                    bw = new BufferedWriter(out);
//                    cbmout = new FileWriter(new File("C:/Users/nmc91/MH00" + i + ".merops.cbm.FIGURE.csv"));
//                    cbmbw = new BufferedWriter(cbmout);
                }
                while (blastSc.hasNext()) {
                    String[] lineSplit = blastSc.nextLine().split("\t");
                    scaf2mer.put(lineSplit[0], lineSplit[1]);
                }
                blastSc.close();
                Map<String, String> scaf2seq = new HashMap<String, String>();
                int l = 0;
                String line = fastaSc.nextLine();
                String scaf = line.split(" ")[0].substring(1);
                String seq = "";
                while (fastaSc.hasNext()) {
                    line = fastaSc.nextLine();
                    if (line.startsWith(">")) {
                        l++;
                        scaf2seq.put(scaf, seq);
                        if (l < 10) {
//                            System.out.println(scaf + ": " + seq);
                        }
                        scaf = line.split(" ")[0].substring(1);
                        seq = "";
                    } else {
                        seq = seq + line;
                    }
                }
                scaf2seq.put(scaf, seq);

                while (cdCANsc.hasNextLine()) {
                    String[] lineSplit = cdCANsc.nextLine().split("\t");
                    if (Double.valueOf(lineSplit[4]) < 0.01) {
//                        bw.write(scaf2mer.get(lineSplit[2]) + "\t" + lineSplit[0] + "\n");
                        if (lineSplit[0].startsWith("CBM")) {

                            if (cbmMerSequences.get(mer2fam.get(scaf2mer.get(lineSplit[2]))) != null) {
                                cbmMerSequences.get(mer2fam.get(scaf2mer.get(lineSplit[2]))).add(">" + lineSplit[2] + "\n" + scaf2seq.get(lineSplit[2]));
                            } else {
                                List<String> sequences = new ArrayList<String>();
                                sequences.add(">" + lineSplit[2] + "\n" + scaf2seq.get(lineSplit[2]));
                                cbmMerSequences.put(mer2fam.get(scaf2mer.get(lineSplit[2])), sequences);
                            }

//                            cbmbw.write(lineSplit[2] +"\t" + lineSplit[0] + "\t" + lineSplit[7]+ "\t" + lineSplit[8] 
//                                    + "\t" + scaf2mer.get(lineSplit[2]) + "\n");
                        }
                    }

                }
                cdCANsc.close();
//                bw.close();
//                cbmbw.close();
//                    }
//                }
            }
            for (String merFam : cbmMerSequences.keySet()) {
//                Use true option to append and no second variable for oevrwrite 
//                cbmout = new FileWriter(new File(pathprefix + "cbm-protease-fastas/" + merFam + ".msa"));
                cbmout = new FileWriter(new File(pathprefix + "cbm-protease-fastas/" + merFam + ".msa"),true);
                cbmbw = new BufferedWriter(cbmout);
                for (String seqs : cbmMerSequences.get(merFam)) {
                    cbmbw.write(seqs + "\n");
                }
                cbmbw.close();
                cbmout.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
