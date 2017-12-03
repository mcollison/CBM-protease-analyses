/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ncl.ssip.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author matt
 */
public class GeneConfigCsv {

    public static void main(String[] args) {
        //scan fasta file input gene family from file name get overall cohort spread 
        //line by line add configuration to map keyset add gene ids as values populate overall cohort occruence array 
        //scan blast hits 
        //add gene ids as keyset and gene catalog IDs as the values 
        //print family ID (with overall spread on top row), gene configurations, distinct hits (taxonomy), total hits, cohort spread 

        //map gene ids to config labels
        try {
            String fileroot = "/usr/local/apache2/htdocs/cbm-protease/data/";
//            String filename = "C40.msa.fasta";

            //map gene id to config label
            File filefolder = new File(fileroot + "cbm-protease-fastas/");
            for (File file : filefolder.listFiles()) {

                System.out.println(file.getName());
                Scanner fastaSc = new Scanner(file);

                Map<String, List<String>> geneconfig2geneids = new HashMap<String, List<String>>();
                Map<String, String> geneid2config = new HashMap<String, String>();
                while (fastaSc.hasNextLine()) {
                    String line = fastaSc.nextLine();
                    String[] splits = new String[0];
                    if (line.startsWith(">")) {
                        splits = line.split("\t");
                        String config = file.getName().substring(0, 3);
                        for (int i = 0; i < (splits.length - 4) / 3; i++) {
                            if (splits[i * 3 + 1].substring(0, (splits[i * 3 + 1].length() - 4)).contains("CBM")) {
                                config = config + "-" + splits[i * 3 + 1].substring(0, (splits[i * 3 + 1].length() - 4));
                            }
                        }
                        if (geneconfig2geneids.get(config) == null) {
                            final String temp = splits[0];
                            List<String> geneidList = new ArrayList<String>() {
                                {
                                    add(temp);
                                }
                            };
                            geneconfig2geneids.put(config, geneidList);
                        } else {
                            List<String> templist = geneconfig2geneids.get(config);
                            templist.add(splits[0]);
                            geneconfig2geneids.put(config, templist);
                        }
                        geneid2config.put(splits[0], config);
                    }
                }
                System.out.println("Done gene ID to config mapping");

                //map gene ids to catalog ids
                Scanner blastSc = new Scanner(new File(fileroot + "BGI-blast/" + file.getName().substring(0, 3) + "-BGI.blast"));
                Map<String, String> geneid2catalogid = new HashMap<String, String>();
                String gid = "";
                while (blastSc.hasNextLine()) {
                    String[] splits = blastSc.nextLine().split("\t");
                    if (!gid.equals(splits[0])) {
                        if (Double.parseDouble(splits[3]) > 0.95) {
                            gid = splits[0];
                            String catid = splits[1].split("#")[0];
                            geneid2catalogid.put(gid, catid);
                        }
                    }
                }
                System.out.println("Done gene ID to catalog ID mapping");

                //map catalog ids to list of gene ids
                Map<String, List<String>> genecat2geneids = new HashMap<String, List<String>>();
                for (String geneid : geneid2catalogid.keySet()) {
                    if (genecat2geneids.get(geneid2catalogid.get(geneid)) == null) {
                        final String temp = geneid;
                        List<String> geneidList = new ArrayList<String>() {
                            {
                                add(temp);
                            }
                        };
                        genecat2geneids.put(geneid2catalogid.get(geneid), geneidList);
                    } else {
                        List<String> templist = genecat2geneids.get(geneid2catalogid.get(geneid));
                        templist.add(geneid);
                        genecat2geneids.put(geneid2catalogid.get(geneid), templist);
                    }
                }
                System.out.println("Done catalog ID to gene ID mapping");

                //map gene configurations to distinct genes 
                //for all gene ids get cat id and config 
                //if config not in map, create list and add entry 
                //else add entry to list 
                Map<String, List<String>> config2catids = new HashMap<String, List<String>>();
                for (String geneid : geneid2config.keySet()) {
                    System.out.println(geneid);
                    final String catid = geneid2catalogid.get(geneid.substring(1));
                    System.out.println(catid);
                    final String config = geneid2config.get(geneid);
                    System.out.println(config);
                    if (config2catids.get(config) == null) {
                        List<String> templist = new ArrayList<String>() {
                            {
                                add(catid);
                            }
                        };
                        config2catids.put(config, templist);
                    } else if (!config2catids.get(config).contains(catid)) {
                        config2catids.get(config).add(catid);
                    }
                }
                System.out.println("Done configuration to catalog ID mapping");

                //print data structures to file
                System.out.println(geneconfig2geneids.keySet());
                System.out.println(geneid2catalogid.keySet());
                FileWriter output = new FileWriter(new File(fileroot + "/final-gene-tables/" + file.getName() + ".csv"));
                BufferedWriter outputbuffer = new BufferedWriter(output);
                FileWriter output2 = new FileWriter(new File(fileroot + "/final-gene-tables/" + file.getName() + "-v2.csv"));
                BufferedWriter outputbuffer2 = new BufferedWriter(output2);
                //first column family 
                outputbuffer.write(file.getName().substring(0, 3));
                outputbuffer2.write(file.getName().substring(0, 3));
                //2nd 3rd and 4th columns configuration, total hits, link to image
                for (String config : geneconfig2geneids.keySet()) {
                    outputbuffer.write("\t" + config + "\t" + geneconfig2geneids.get(config).size() 
//                            + "\t\\includegraphics[width=4cm, height=6mm]{./images/" + config + ".png}"
                            + "\t" + config2catids.get(config).size() + "\n");
                    int[] hits = new int[86];
                    for (String geneid : geneconfig2geneids.get(config)) {
                        int index = Integer.parseInt(geneid.split("MH00")[1].substring(0, 2));
//                    System.out.println(geneid + "\t" + index);
                        hits[index - 1]++;
                    }
                    outputbuffer2.write("\t" + config + "\t" + geneconfig2geneids.get(config).size() + "\t");
                    for (int i = 0; i < hits.length; i++) {
                        outputbuffer2.write(hits[i] + "\t");
                    }

                    //list gene cat ids and numbers 
                    //for each gene cat id of this config 
                    //get the sequence list size and spread 
                    List<String> genecatids = config2catids.get(config);
//                outputbuffer.write(genecatids.get(0) + "\t" + genecat2geneids.get(genecatids.get(0)).size()
//                        + "\t" + genecatids.get(0) + ".png\n" );

                    hits = new int[86];
                    for (String geneid : genecat2geneids.get(genecatids.get(0))) {
                        int index = Integer.parseInt(geneid.split("MH00")[1].substring(0, 2));
//                    System.out.println(geneid + "\t" + index);
                        hits[index - 1]++;
                    }
                    outputbuffer2.write(genecatids.get(0) + "\t" + genecat2geneids.get(genecatids.get(0)).size());
                    for (int i = 0; i < hits.length; i++) {
                        outputbuffer2.write("\t" + hits[i]);
                    }
                    outputbuffer2.write("\n");

                    for (int i = 1; i < genecatids.size(); i++) {
//                    outputbuffer.write("\t\t\t\t"
                        outputbuffer2.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
                                + genecatids.get(i) + "\t" + genecat2geneids.get(genecatids.get(i)).size());
                        hits = new int[86];
                        for (String geneid : genecat2geneids.get(genecatids.get(i))) {
                            int index = Integer.parseInt(geneid.split("MH00")[1].substring(0, 2));
//                    System.out.println(geneid + "\t" + index);
                            hits[index - 1]++;
                        }
//                    outputbuffer2.write(genecatids.get(i) + "\t" + genecat2geneids.get(genecatids.get(i)).size());
                        for (int j = 0; j < hits.length; j++) {
                            outputbuffer2.write("\t" + hits[j]);
                        }
                        outputbuffer2.write("\n");

                    }

                }
                outputbuffer.close();
                outputbuffer2.close();
                output.close();
                output2.close();
            }
        } catch (Exception ex) {
            System.out.println("ERROR");
            ex.printStackTrace();

        }
    }
}
