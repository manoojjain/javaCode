package com.sectorId.tenMillion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class SectorIDGenMetaDataSingle {

    public static void main(String[] args) throws Exception {

        // int countLines = 2147483647;
        int totalSectIdLines = 10000000;
        int fileName = 1;

        String path = null;
        if (args.length == 0) {
            System.out.print("Can not proceed with out path..Please provide path");
            return;
        }
        path = args[0];
//        
//        path = "D:\\userdata\\m5jain\\Desktop\\RoboCalling_Related\\SectorId\\singleFile\\";
        String sectorIdFilePath = path + fileName;
        File sectorIdFile = checkFilePresent(sectorIdFilePath);
        BufferedWriter bwForSectorIdFile = new BufferedWriter(new FileWriter(sectorIdFile, true));

        String metaDataFilePath = path + "0";
        File metaDataFile = checkFilePresent(metaDataFilePath);
        BufferedWriter bwForMetaDataFile = new BufferedWriter(new FileWriter(metaDataFile, true));

        String strContent1 = "a,123456789012345678901234";
        Integer myInt = new Integer(0);
        String strContent2 = ", 123456789, 123458, 1, 123456, 1234567, 1, 1-2-3, 2, 2, H,e1=123456789;e2=234567891;e3=345678912;e4=456789123;e5=132456789;e6=213456789;e7=123465789;e8=123456798;e9=123456987;e10=123476589;e11=123456789;e12=123456789;e13=123456789;";

//        sectorIdFilePath = path + fileName;
//      sectorIdFile = checkFilePresent(sectorIdFilePath);
//      BufferedWriter bwForSectorIdFile = new BufferedWriter(new FileWriter(sectorIdFilePath, true));

        
        for (int i = 0; i < totalSectIdLines; i++) {


            bwForSectorIdFile.write(strContent1);
            myInt++;
            String myStringRepOfInt = String.format("%08d", myInt);
            bwForSectorIdFile.write(myStringRepOfInt);
            bwForSectorIdFile.write(strContent2);
            bwForSectorIdFile.newLine();
            bwForSectorIdFile.flush();

            StringTokenizer lineTokenizer = new StringTokenizer(strContent1 + myStringRepOfInt, ",");
            int a = 0;
            while (lineTokenizer.hasMoreTokens()) {
                String sectorId = lineTokenizer.nextToken();
                if (a == 1) {
                    bwForMetaDataFile.write(sectorId + " " + fileName + " " + (i+1));
                    bwForMetaDataFile.newLine();
                    bwForMetaDataFile.flush();
                }
                a++;
            }

        }
        
        bwForSectorIdFile.close();
        bwForMetaDataFile.close();
        System.out.println("Generated Successfully!!!");
    }

    private static File checkFilePresent(String strFilePath) throws IOException {
        File f = new File(strFilePath);
        if (!f.exists())
            f.createNewFile();
        else {
            f.delete();
            f.createNewFile();
        }
        return f;
    }

}
