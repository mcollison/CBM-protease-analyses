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
public class MeropsCbmMatrixGenerator {

    public static void main(String[] args) {
        try {
            String fileroot = "E://website/cbm-protease/";
            String dataroot = fileroot + "data/cbm-protease-csv/";
            Scanner sc = null;
            FileWriter out = new FileWriter(new File(fileroot + "results/merops-cbm-matrix.csv"));
            BufferedWriter bw = new BufferedWriter(out);
            Map<String, Map<String, Integer>> merMatrix = new HashMap<String, Map<String, Integer>>();

            for (int i = 1; i < 87; i++) {
                if (i < 10) {
                    sc = new Scanner(new File(dataroot + "MH000" + i + ".merops.cbm.csv"));
                } else if (i == 29) {
                    continue;
                } else {
                    sc = new Scanner(new File(dataroot + "MH00" + i + ".merops.cbm.csv"));
                }
                while (sc.hasNext()) {
                    String[] ids = sc.nextLine().split("\t");
                    if (merMatrix.get(ids[0]) != null) {
                        if (merMatrix.get(ids[0]).get(ids[1]) != null) {
                            int num = merMatrix.get(ids[0]).get(ids[1]) + 1;
                            merMatrix.get(ids[0]).put(ids[1], num);
                        } else {
                            merMatrix.get(ids[0]).put(ids[1], 1);
                        }
                    } else {
                        merMatrix.put(ids[0], new HashMap<String, Integer>());
                        merMatrix.get(ids[0]).put(ids[1], 1);
                    }
                }
            }

            Scanner pepunitSc = new Scanner(new File(fileroot + "data/MEROPS-pepunit/MEROPS_pepunit.lib"));
            Map<String, String> meridMatrix = new HashMap<String, String>();
            while (pepunitSc.hasNextLine()) {
//            for (int i = 0; i < 2000; i++) {
                String idLine = pepunitSc.nextLine();
                if (idLine.startsWith(">")) {
                    String MERID = idLine.split(" ")[0].substring(1);
                    String[] details = idLine.split("#");
                    String family = details[0].substring(details[0].length() - 8, details[0].length() - 5);
                    String clan = details[1];
                    String info = details[0];
                    meridMatrix.put(MERID, "," + family + "," + clan + "," + info);
                }
            }
            List cbmIds = new ArrayList<String>();
            for (String merId : merMatrix.keySet()) {
                for (String cbmId : merMatrix.get(merId).keySet()) {
                    if (!cbmIds.contains(cbmId)) {
                        cbmIds.add(cbmId);
                    }
                }
            }
            bw.write("\t,");
            for (int i = 0; i < cbmIds.size(); i++) {
                bw.write(cbmIds.get(i).toString() + ",");
            }
            bw.write("\n");
            for (String merId : merMatrix.keySet()) {
                bw.write(merId + ",");
                System.out.print(merId + ",");
                for (int i = 0; i < cbmIds.size(); i++) {
//                    System.out.print(cbmIds.get(i));
                    if (merMatrix.get(merId).get(cbmIds.get(i)) == null) {
                        bw.write("0,");
                        System.out.print("0,");
                    } else {
                        bw.write(merMatrix.get(merId).get(cbmIds.get(i)) + ",");
                        System.out.print(merMatrix.get(merId).get(cbmIds.get(i)) + ",");
                    }
                }
                bw.write(meridMatrix.get(merId) + "\n");
                System.out.println();
            }

            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
