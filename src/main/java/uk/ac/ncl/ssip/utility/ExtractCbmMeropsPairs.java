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
public class ExtractCbmMeropsPairs {
    
    private static void main(String[] args) {
        try {
            Scanner blastSc = null;
            Scanner cdCANsc = null;
            FileWriter out = null;
            BufferedWriter bw = null;
            FileWriter cbmout = null;
            BufferedWriter cbmbw = null;
            for (int i = 1; i < 87; i++) {
                if (i < 10) {
                    cdCANsc = new Scanner(new File("D:/dbCAN/MH000" + i + ".dmps"));
                    blastSc = new Scanner(new File("C:/Users/nmc91/MH000" + i + ".blast.e-val0.01.filtered"));
                    out = new FileWriter(new File("C:/Users/nmc91/MH000" + i + ".merops.all-dbCAN.csv"));
                    bw = new BufferedWriter(out);
                    cbmout = new FileWriter(new File("C:/Users/nmc91/MH000" + i + ".merops.cbm.csv"));
                    cbmbw = new BufferedWriter(cbmout);

                } else if (i == 29) {
                    continue;
                } else {
                    cdCANsc = new Scanner(new File("D:/dbCAN/MH00" + i + ".dmps"));
                    blastSc = new Scanner(new File("C:/Users/nmc91/MH00" + i + ".blast.e-val0.01.filtered"));
                    out = new FileWriter(new File("C:/Users/nmc91/MH00" + i + ".merops.all-dbCAN.csv"));
                    bw = new BufferedWriter(out);
                    cbmout = new FileWriter(new File("C:/Users/nmc91/MH00" + i + ".merops.cbm.csv"));
                    cbmbw = new BufferedWriter(cbmout);
                }
                Map<String, String> scaf2mer = new HashMap<String, String>();
                while (blastSc.hasNext()) {
                    String[] lineSplit = blastSc.nextLine().split("\t");
                    scaf2mer.put(lineSplit[0], lineSplit[1]);
                }
                while (cdCANsc.hasNextLine()) {
                    String[] lineSplit = cdCANsc.nextLine().split("\t");
                    if (Double.valueOf(lineSplit[4]) < 0.01) {
                        bw.write(scaf2mer.get(lineSplit[2]) + "\t" + lineSplit[0] + "\n");
                        if (lineSplit[0].startsWith("CBM")) {
                            cbmbw.write(scaf2mer.get(lineSplit[2]) + "\t" + lineSplit[0] + "\n");
                        }
                    }

                }
                bw.close();
                cbmbw.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
