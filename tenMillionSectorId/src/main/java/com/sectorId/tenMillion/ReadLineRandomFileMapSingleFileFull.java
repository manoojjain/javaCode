package com.sectorId.tenMillion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class ReadLineRandomFileMapSingleFileFull {

    public static void main(String[] args) throws IOException {

        Map<String, SectorIdColMetaData> last3FileLineNumberMap = new HashMap<String, SectorIdColMetaData>();
        Map<String, Map<String, SectorIdColMetaData>> last6FileLineNumberMap = new HashMap<String, Map<String, SectorIdColMetaData>>();
        Map<String, Map<String, Map<String, SectorIdColMetaData>>> last9FileLineNumberMap = new HashMap<String, Map<String, Map<String, SectorIdColMetaData>>>();
        Map<String, Map<String, Map<String, Map<String, SectorIdColMetaData>>>> nicLast9NumMap = new HashMap<String, Map<String, Map<String, Map<String, SectorIdColMetaData>>>>();
        Map<String, Map<String, Map<String, Map<String, Map<String, SectorIdColMetaData>>>>> tacNicMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, SectorIdColMetaData>>>>>();
        Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, SectorIdColMetaData>>>>>> mncTacMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Map<String, SectorIdColMetaData>>>>>>();
        Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, SectorIdColMetaData>>>>>>> mccMncMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, SectorIdColMetaData>>>>>>>();

        if (args.length == 0 || args[0].equalsIgnoreCase("")) {
            System.out.println("Can not proceed without file path");
            return;
        }

        System.out.println("Print argument at position 0: " + args[0]);
        String path = args[0];// D:\\userdata\\m5jain\\Desktop\\UST_Exception\\
//        String path = "D:\\userdata\\m5jain\\Desktop\\RoboCalling_Related\\SectorId\\singleFile\\";

        // String fileName = "SectorID.dmp";

        String sectorId = null;
        String mcc = null;
        String mnc = null;
        String tac = null;
        String nic = null;

        String last9to6 = null;
        String last6to3 = null;
        String last3to0 = null;

        long beginTime = 0l;
        String remainingSectorId = null;
        BufferedReader reader;
        try {
            beginTime = System.currentTimeMillis();
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path + "1")));
            for (String line; (line = reader.readLine()) != null;) {
                try {

                    sectorId = line.substring(2, 23);
                    remainingSectorId = line.substring(25, line.length() - 1);

                    mcc = sectorId.substring(0, 3);
                    mnc = sectorId.substring(3, 6);
                    tac = sectorId.substring(6, 9);
                    nic = sectorId.substring(9, 12);
                    last9to6 = sectorId.substring(12, 15);
                    last6to3 = sectorId.substring(15, 18);
                    last3to0 = sectorId.substring(18, 21);

                } catch (Exception e) {
                    System.out.println("Exception occur for filename or line number is not available. Keep reading next line");
                    e.printStackTrace();
                    continue;
                }
                SectorIdColMetaData sdmd = new SectorIdColMetaData(remainingSectorId);

                last3FileLineNumberMap.put(last3to0, sdmd);
                last6FileLineNumberMap.put(last6to3, last3FileLineNumberMap);
                last9FileLineNumberMap.put(last9to6, last6FileLineNumberMap);
                nicLast9NumMap.put(nic, last9FileLineNumberMap);
                tacNicMap.put(tac, nicLast9NumMap);
                mncTacMap.put(mnc, tacNicMap);
                mccMncMap.put(mcc, mncTacMap);
            }
            reader.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Exception occur for filename or line number is not available");
        }

        System.out.println("Reading of metadata completed. Size of last3FileLineNumberMap: " + last3FileLineNumberMap.size());
        System.out.println("Reading of metadata completed. Size of last6FileLineNumberMap: " + last6FileLineNumberMap.size());
        System.out.println("Reading of metadata completed. Size of last9FileLineNumberMap: " + last9FileLineNumberMap.size());
        System.out.println("Total time taken to read metadata in miliseconds :" + (System.currentTimeMillis() - beginTime));
        System.out.println("Please run jmap -histo:live ");

        try {
            System.out.println("Sleeping for 30 secs");
            Thread.sleep(30000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        int count = 0;
        Random rd = new Random();

        String randSectorId = null;
        SectorIdColMetaData findPsapForSecId = null;
        String myStringRepOfInt = null;
        String sectorIdFullLine = null;

        randSectorId = "1234567890123";
        mcc = randSectorId.substring(0, 3);
        mnc = randSectorId.substring(3, 6);
        tac = randSectorId.substring(6, 9);
        nic = randSectorId.substring(9, 12);

        long endTime = System.currentTimeMillis() + 1000;
        while (true) {

            int random = rd.nextInt(10000000);
            if (random == 0) {
                continue;
            }
            myStringRepOfInt = String.format("%08d", random);

            randSectorId = "1234567890123" + myStringRepOfInt;

            last9to6 = randSectorId.substring(12, 15);
            last6to3 = randSectorId.substring(15, 18);
            last3to0 = randSectorId.substring(18, 21);

            findPsapForSecId = mccMncMap.get(mcc).get(mnc).get(tac).get(nic).get(last9to6).get(last6to3).get(last3to0);

            if (findPsapForSecId == null) {
                System.out.println("my Random sector ID: " + randSectorId);
                System.out.println("fileMetadata is null " + findPsapForSecId);
                break;
            } else {
                count++;
                 System.out.println("my Random sector ID: " + randSectorId + "Full line: " + findPsapForSecId);
            }

            if (System.currentTimeMillis() >= endTime) {
                System.out.println("Number of lines processed in 1 sec: " + count);
                endTime = System.currentTimeMillis() + 1000;
                count = 0;
            }
        }
    }
}
