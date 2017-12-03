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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matt
 */
public class ExtractMeropsFasta {

    public static void main(String[] args) {
        try {
            for (int i = 2; i < 87; i++) {
                ArrayList<String> mgGeneIds = new ArrayList<String>();
                PrintWriter out;
                Scanner scBlast;
                Scanner scFa;
                if (i < 10) {
                    scFa = new Scanner(new File("/home/matt/Documents/metagenomes-prodigal/MH000" + i + ".prodigal"));//update to prodigal file 
                    scBlast = new Scanner(new File("/home/matt/Documents/filtered-merops.prod.blast/MH000" + i + ".blast.e-val0.01.filtered"));
                    out = new PrintWriter("/home/matt/Documents/filtered-merops.prod.blast/MH000" + i + ".merops.blast.fasta");
                } else if (i == 29) {
                    continue;
                } else {
                    scFa = new Scanner(new File("/home/matt/Documents/metagenomes-prodigal/MH00" + i + ".prodigal"));//update to prodigal file 
                    scBlast = new Scanner(new File("/home/matt/Documents/filtered-merops.prod.blast/MH00" + i + ".blast.e-val0.01.filtered"));
                    out = new PrintWriter("/home/matt/Documents/filtered-merops.prod.blast/MH00" + i + ".merops.blast.fasta");
                }
                while (scBlast.hasNext()) {
                    mgGeneIds.add(scBlast.nextLine().split("\t")[0]);
                }
                boolean goi = false;
                while (scFa.hasNextLine()) {
                    String fastaLine = scFa.nextLine();
                    if (fastaLine.startsWith(">")) {
                        String fastaId = fastaLine.substring(1).split(" ")[0];
//                        System.out.println(fastaId);
                        if (mgGeneIds.contains(fastaId)) {
                            goi = true;
                            out.println(">" + fastaId);
                        } else {
                            goi = false;
                        }

                    } else if (goi) {
                        out.println(fastaLine);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
