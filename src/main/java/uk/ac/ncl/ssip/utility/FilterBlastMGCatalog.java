/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ncl.ssip.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author nmc91
 */
public class FilterBlastMGCatalog {

    public static void main(String[] args) {
        try {

            for (int i = 3; i < 10; i++) {//change to 87 after blast is done on server
                InputStream fileStream = null;
                InputStream gzipStream = null;
                Reader decoder = null;
                BufferedReader buffered = null;
                FileWriter out = null;
                BufferedWriter bw = null;
                if (i < 10) {
                    for (int j = 6; j < 7; j++) {
                        String filePath = "/home/matt/Documents/MG-catalog-blast/MH000" + i + "xa" + String.valueOf((char) (j + 64)).toLowerCase() + ".gz";
                        fileStream = new FileInputStream(filePath);
                        gzipStream = new GZIPInputStream(fileStream);
                        decoder = new InputStreamReader(gzipStream, "UTF-8");
                        buffered = new BufferedReader(decoder);
                        System.out.println("Reading file: " + filePath);
                        out = new FileWriter(new File(filePath + ".blast.e-val0.01.filtered"));
                        bw = new BufferedWriter(out);
                        System.out.println("Writing to file: " + filePath + ".blast.e-val0.01.filtered");
                        String query = "";
                        String target = "";
                        String outputLine = "";
                        String line = "";
                        while ((line = buffered.readLine()) != null) {
                            String[] lineSplit = line.split("\t");
                            if (!lineSplit[0].equals(query)) {
//                                System.out.println(outputLine);
                                bw.write(outputLine + "\n");
                                query = lineSplit[0];
                                target = lineSplit[2];
                                outputLine = query + "\n" + target;
                            } else if (!lineSplit[2].equals(target)) {
                                target = lineSplit[2];
                                if (Double.valueOf(lineSplit[11]) < 0.01) {
                                    outputLine = outputLine + "\t" + target;
                                }
                            }

                        }
                        bw.write(outputLine);
                        bw.close();
                    }
                } else if (i == 29) {
                    continue;
                } else {
                    for (int j = 1; j < 27; j++) {
                        String filePath = "/home/matt/Documents/MG-catalog-blast/MH00" + i + "xa" + String.valueOf((char) (j + 64)).toLowerCase();
                        fileStream = new FileInputStream(filePath);
                        gzipStream = new GZIPInputStream(fileStream);
                        decoder = new InputStreamReader(gzipStream, "UTF-8");
                        buffered = new BufferedReader(decoder);

                        out = new FileWriter(new File("/home/matt/Documents/MG-catalog-blast/MH00"
                                + i + "xa" + String.valueOf((char) (j + 64)).toLowerCase() + ".blast.e-val0.01.filtered"));
                        bw = new BufferedWriter(out);
                        String query = "";
                        String target = "";
                        String outputLine = "";
                        String line = "";
                        while ((line = buffered.readLine()) != null) {
                            String[] lineSplit = line.split("\t");
                            if (!lineSplit[0].equals(query)) {
                                System.out.println(outputLine);
                                bw.write(outputLine + "\n");
                                query = lineSplit[0];
                                target = lineSplit[2];
                                outputLine = query + "\n" + target;
                            } else if (!lineSplit[2].equals(target)) {
                                target = lineSplit[2];
                                if (Double.valueOf(lineSplit[11]) < 0.01) {
                                    outputLine = outputLine + "\t" + target;
                                }
                            }

                        }
                        bw.write(outputLine);
                        bw.close();
                    }
                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeropsCbmMatrixGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
