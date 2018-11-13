/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author mnm
 */
public class io {
    public void writeToFile(String path, String key)  {
        File f = new File(path);
        try {
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter(f, true)));
            out.println(key);
            out.close();
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }
    public String readFromFile(String path)  {
        File f = new File(path);
        String out = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                out += (sCurrentLine);
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return out;
    }
}
