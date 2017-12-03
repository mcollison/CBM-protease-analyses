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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nmc91
 */
public class MeropsMgOccurenceMatrix {
    
    public static void main(String[] args){
        parseFilteredBlastToHitMatrix();
        addFamilyClanIds();
    }
        private static void parseFilteredBlastToHitMatrix() {
        try {
            Scanner sc = null;
            FileWriter out = new FileWriter(new File("C:/Users/nmc91/merops-hit-table.csv"));
            BufferedWriter bw = new BufferedWriter(out);
            Map<String, Integer[]> hitMatrix = new HashMap<String, Integer[]>();
            for (int i = 1; i < 87; i++) {
                if (i < 10) {
                    sc = new Scanner(new File("C:/Users/nmc91/MH000" + i + ".blast.e-val0.01.filtered"));
                } else if (i == 29) {
                    continue;
                } else {
                    sc = new Scanner(new File("C:/Users/nmc91/MH00" + i + ".blast.e-val0.01.filtered"));
                }
                while (sc.hasNext()) {
                    String meropsId = sc.nextLine().split("\t")[1];
                    if (hitMatrix.get(meropsId) != null) {
                        hitMatrix.get(meropsId)[i - 1]++;
                    } else {
                        hitMatrix.put(meropsId, new Integer[86]);
                        hitMatrix.get(meropsId)[i - 1]++;
                    }
                }
            }
            for (String meropsId : hitMatrix.keySet()) {
                System.out.print(meropsId + ",");
                bw.write(meropsId + ",");
                for (int i = 0; i < 86; i++) {
                    bw.write(hitMatrix.get(meropsId)[i] + ",");
                    System.out.print(hitMatrix.get(meropsId)[i] + ",");
                }
                System.out.println();
                bw.write("\n");
            }
            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void addFamilyClanIds() {
        try {
            Scanner hitListSc = new Scanner(new File("C:/Users/nmc91/merops-hit-table.csv"));
            Scanner pepunitSc = new Scanner(new File("C:/Users/nmc91/MEROPS_pepunit.lib"));
            FileWriter out = new FileWriter(new File("C:/Users/nmc91/merops-family-hit-table.csv"));
            BufferedWriter bw = new BufferedWriter(out);
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
                } else {
//                    System.out.println(idLine);
                }
            }
            while (hitListSc.hasNextLine()) {
                String line = hitListSc.nextLine();
                if (meridMatrix.containsKey(line.split(",")[0])) {
                    bw.write(line + line.split(",")[0] + "," + meridMatrix.get(line.split(",")[0]) + "\n");
                } else {
                    System.out.println(line);
                }

            }

            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
