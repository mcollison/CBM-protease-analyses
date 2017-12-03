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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author nmc91
 */
public class MapGid2Taxonomy {

    public static void main(String[] args) {
        try {
            Logger logger = Logger.getLogger("MyLog");
            FileHandler fh = new FileHandler("C:/temp/test/MyLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages  
            int i = 0;
            int k = 19;
            logger.info("" + i * k);

            //MGid->GID->taxid->taxname 
            String fileroot = "~/";

            String gi2taxidFilepath = "taxonomy/gi_taxid_prot.dmp";
            File gi2TaxidFile = new File(fileroot + gi2taxidFilepath);
            Scanner gi2taxidSc = new Scanner(gi2TaxidFile);
            Map<String, String> gi2taxid = new HashMap<String, String>();
            while (gi2taxidSc.hasNextLine()) {
//                for (int i = 0; i < 1000; i++) {
//                if (gi2taxidSc.hasNextLine()){
                String[] splits = gi2taxidSc.nextLine().split("\t");
                if (splits.length < 3) {
                    gi2taxid.put(splits[0], splits[1]);
                } else {
                    System.out.println(splits);
                }
//                }
            }
            System.out.println("Map generation complete: GI to taxid.");

            String taxid2taxnameFilepath = "taxonomy/names.dmp";
            File taxid2TaxonFile = new File(fileroot + taxid2taxnameFilepath);
            Scanner taxid2taxnameSc = new Scanner(taxid2TaxonFile);
            Map<String, String> taxid2name = new HashMap<String, String>();
            while (taxid2taxnameSc.hasNextLine()) {
                String split = taxid2taxnameSc.nextLine();
                String[] splits = split.split("|");
                if (splits.length > 2) {
                    taxid2name.put(splits[0].trim(), splits[1].trim());
                } else {
                    System.out.println(split);
                }
            }
            System.out.println("Map generation complete: taxid to names.");

            //make annoatations iterate 
            File annotationsFile = new File(fileroot + "cbm-protease-annotations/");
            for (File file : annotationsFile.listFiles()) {

                FileWriter output = new FileWriter(new File(fileroot + "/taxonomy/" + file.getName() + ".taxon"));
                BufferedWriter outputbuffer = new BufferedWriter(output);

//                if (file.getName().equals("A01.annotations")) {
                Scanner annScan = new Scanner(file);
                while (annScan.hasNextLine()) {
                    String[] annSplit = annScan.nextLine().split("\t");
                    outputbuffer.write(file.getName().substring(0, 3) + "\t"
                            + annSplit[0] + "\t" + annSplit[3] + "\t"
                            + gi2taxid.get(annSplit[3]) + "\t" + taxid2name.get(gi2taxid.get(annSplit[3])) + "\n");
                }
//                }
            }

        } catch (Exception ex) {
            Logger.getLogger(MapGid2Taxonomy.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }
}
