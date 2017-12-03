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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nmc91
 */
public class BlastFilter {

    public static void main(String[] args) {
        try {
            Scanner sc = null;
            FileWriter out = null;
            BufferedWriter bw = null;
            for (int i = 1; i < 87; i++) {
                if (i < 10) {
                    sc = new Scanner(new File("D:/merops_blast-Copy/metaHIT/results/MH000" + i + ".merops.blast"));
                    out = new FileWriter(new File("C:/Users/nmc91/MH000" + i + ".blast.e-val0.01.filtered"));
                    bw = new BufferedWriter(out);

                } else if (i == 29) {
                    continue;
                } else {
                    sc = new Scanner(new File("D:/merops_blast-Copy/metaHIT/results/MH00" + i + ".merops.blast"));
                    out = new FileWriter(new File("C:/Users/nmc91/MH00" + i + ".blast.e-val0.01.filtered"));
                    bw = new BufferedWriter(out);
                }
                String query = "";
                while (sc.hasNext()) {
                    String[] lineSplit = sc.nextLine().split("\t");
                    if (!lineSplit[0].equals(query)) {
                        query = lineSplit[0];
                        if (Double.valueOf(lineSplit[10]) < 0.01) {
                            bw.write(query + "\t" + lineSplit[1] + "\t" + lineSplit[10] + "\n");
                        }
                    }
                }
                bw.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
