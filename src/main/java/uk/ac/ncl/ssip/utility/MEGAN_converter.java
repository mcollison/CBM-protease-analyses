/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ncl.ssip.utility;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author nmc91
 */
public class MEGAN_converter {

    public static void main(String[] args) {
        try {
            File dir = new File("C://Users/nmc91/M23-test/");
            for (File f : dir.listFiles()) {
                if (f.getName().endsWith(".tax")) {
                    try{
                    Scanner sc = new Scanner(new File("C://Users/nmc91/M23-test/" + f.getName()));
                    PrintWriter writer = new PrintWriter("C://Users/nmc91/M23-test/" + f.getName() + ".meg", "UTF-8");
                    System.out.println(f.getName());
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        String[] splits = line.split("\t");
                        writer.println(splits[3] + "\t" + splits[5] + "\t" + splits[4]);
                    }
                    writer.close();
                    }catch(Exception ex){
                        
                    }
                }
            }

        } catch (Exception ex) {

        }
    }
}
