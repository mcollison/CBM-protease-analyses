/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ncl.ssip.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author matt
 */
public class TaxonomyQuickFix {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

//        File dir = new File("/home/matt/Documents/thesis-data/cbm-protease/data/cbm-protease-annotations/");
        File dir = new File("C://Users/nmc91/M23-test/");
        System.out.println(dir.getAbsolutePath());
        for (File f : dir.listFiles()) {
            System.out.println(f.getAbsolutePath());
            if (f.getName().endsWith(".annotations")) {
                try {
                    PrintWriter writer = new PrintWriter(f.getName() + ".tax", "UTF-8");
                    Scanner sc = new Scanner(f);
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        String[] strs = line.split("\t");

                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(new URL("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=protein&id=" + strs[3] + "&retmode=xml").openStream());

                        doc.getDocumentElement().normalize();
                        System.out.print(line + "\t");
                        writer.print(line + "\t");

                        NodeList nList = doc.getElementsByTagName("GBSeq_taxonomy");
                        for (int temp = 0; temp < nList.getLength(); temp++) {
                            Node nNode = nList.item(temp);
                            Element eElement = (Element) nNode;
                            System.out.print(eElement.getTextContent() + "\t");
                            writer.print(eElement.getTextContent() + "\t");

                        }
                        NodeList nList2 = doc.getElementsByTagName("GBSeq_organism");
                        for (int temp = 0; temp < nList2.getLength(); temp++) {
                            Node nNode = nList2.item(temp);
                            Element eElement = (Element) nNode;
                            System.out.println(eElement.getTextContent());
                            writer.println(eElement.getTextContent());
                        }

                    }
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error in " + f.getName());
                }
            }
        }
//// or if you prefer DOM:
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
////        Document doc = db.parse(new URL("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=protein&id=CZT55942").openStream());
//        Document doc = db.parse(new URL("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=protein&id=CZT55942&retmode=xml").openStream());
//
//        doc.getDocumentElement().normalize();
//
//        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//        NodeList nList = doc.getElementsByTagName("GBSeq_taxonomy");
//
//        System.out.println();
//        System.out.println("----------------------------");
//
//        for (int temp = 0; temp < nList.getLength(); temp++) {
//            Node nNode = nList.item(temp);
//
////		System.out.println("\nCurrent Element :" + nNode.getNodeName());
////		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//            Element eElement = (Element) nNode;
//            System.out.println(eElement.getTextContent());
//
////            if (eElement.getAttribute("Name").equals("TaxId")) {
////
////                System.out.println("Staff id : " + eElement.getAttribute("Name")
////                        + eElement.getTextContent());
////            }
////                }
//        }
    }

    public static void method() {
        try {
            Map<String, Integer> tax2count = new HashMap<String, Integer>();
            Map<String, Integer> acc2count = new HashMap<String, Integer>();
            Scanner sc = new Scanner(new File("D://thesis-data/cbm-protease/data/cbm-protease-annotations/S08.annotations"));
//            for(int i=0;i<10;i++){
            while (sc.hasNextLine()) {
                String line = "";
                String uniprot_id = "";
                try {
                    line = sc.nextLine();
                    uniprot_id = line.split("\t")[3];
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println(line);
                }
                if (acc2count.get(uniprot_id) == null) {
                    acc2count.put(uniprot_id, 1);
                } else {
                    acc2count.put(uniprot_id, acc2count.get(uniprot_id) + 1);
                }
            }

            for (String key : acc2count.keySet()) {
                System.out.println(key + ": " + acc2count.get(key));
            }

            Scanner sc1 = new Scanner(new File("D://thesis-data/cbm-protease/data/prot.accession2taxid"));
            sc1.nextLine();//skip heading line
            while (sc1.hasNextLine()) {
                String line = sc1.nextLine();
//                for (String str : acc2count.keySet()) {
                if (acc2count.keySet().contains(line.split("\t")[0])) {
                    System.out.println(line.split("\t")[0] + ": " + line.split("\t")[2]);
                }
//                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TaxonomyQuickFix.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
