package com.sectorId.tenMillion;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Stream;

public class ReadSpecificLineMapOfMap {

    public static void main(String[] args) {
        
        Map<String, Metadata> last3FileLineNumberMap = new HashMap<String, Metadata>();
        Map<String, Map<String, Metadata>> last6FileLineNumberMap = new HashMap<String, Map<String, Metadata>>();
        Map<String, Map<String, Map<String, Metadata>>> last9FileLineNumberMap = new HashMap<String, Map<String, Map<String, Metadata>>>();        
        Map<String, Map<String, Map<String, Map<String, Metadata>>>> nicLast9NumMap = new HashMap<String, Map<String, Map<String, Map<String, Metadata>>>>();
        Map<String, Map<String, Map<String, Map<String, Map<String, Metadata>>>>> tacNicMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Metadata>>>>>();

        Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Metadata>>>>>> mncTacMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Map<String, Metadata>>>>>>();
        Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Metadata>>>>>>> mccMncMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Metadata>>>>>>>();
        

        if (args.length == 0 || args[0].equalsIgnoreCase("") ) {
            System.out.println("Can not proceed without file path");
            return;
        }
            
        System.out.println("Print argument at position 0: "+args[0]);
        String path = args[0];
//        String path = "D:\\userdata\\m5jain\\Desktop\\RoboCalling_Related\\SectorId\\";
        
        String sectorId =null;
        
//        String str = "12345678901234567890123400000074";
        String mcc =null; 
        String mnc =null; 
        String tac =null; 
        String nic =null; 
        
        String last9to6 =null;
        String last6to3 =null;
        String last3to0 =null;
        
        short fileNameFrmMetaData = 0;
        int lineNumberFromMetaData = 0;
        long beginTime = 0l;
        BufferedReader reader;
        try {
            beginTime = System.currentTimeMillis();
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path+"0")));
            for (String line; (line = reader.readLine()) != null;) {
                StringTokenizer lineTokenizer = new StringTokenizer(line, " ");
                try {
                    while (lineTokenizer.hasMoreTokens()) {
                        sectorId = lineTokenizer.nextToken();
                        
                        mcc = sectorId.substring(0, 3);
                        mnc = sectorId.substring(3, 6);
                        tac = sectorId.substring(6, 12);
                        nic = sectorId.substring(12, 23);
                        last9to6 = sectorId.substring(23, 26);
                        last6to3 = sectorId.substring(26, 29);
                        last3to0 = sectorId.substring(29, 32);
                        
                        if (lineTokenizer.hasMoreTokens())
                            fileNameFrmMetaData = Short.parseShort(lineTokenizer.nextToken());
                        if (lineTokenizer.hasMoreTokens())
                            lineNumberFromMetaData = Integer.parseInt(lineTokenizer.nextToken());
                    }
                } catch (Exception e) {
                    System.out.println("Exception occur for filename or line number is not available. Keep reading next line" );
                    e.printStackTrace();
                    continue;
                }
                Metadata md = new Metadata(fileNameFrmMetaData, lineNumberFromMetaData);
                if( md.getFileName() != 0 && md.getLineNumber() != 0) {
                    
                    last3FileLineNumberMap.put(last3to0, md);
                    last6FileLineNumberMap.put(last6to3, last3FileLineNumberMap);
                    last9FileLineNumberMap.put(last9to6, last6FileLineNumberMap);
                    nicLast9NumMap.put(nic, last9FileLineNumberMap);  
                    tacNicMap.put(tac, nicLast9NumMap);
                    mncTacMap.put(mnc, tacNicMap);
                    mccMncMap.put(mcc, mncTacMap);
                } else
                    System.out.println("filename or line number is not available");
            }
            
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Exception occur for filename or line number is not available");
        }
        
        
        System.out.println("Reading of metadata completed. Size of last3FileLineNumberMap: "+last3FileLineNumberMap.size());
        System.out.println("Reading of metadata completed. Size of last6FileLineNumberMap: "+last6FileLineNumberMap.size());
        System.out.println("Reading of metadata completed. Size of last9FileLineNumberMap: "+last9FileLineNumberMap.size());
        System.out.println("Total time taken in miliseconds :"+(System.currentTimeMillis()-beginTime));
        System.out.println("Please run jmap -histo:live ");
        
        try {
            System.out.println("Sleeping for 30 secs");
            Thread.sleep(30000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        int count = 0;
        Random rd = new Random();

        String randSectorId = null;
        Metadata findMetadata = null;
        String myStringRepOfInt = null;
        String sectorIdFullLine = null;
        long beginTimeForMap = 0l ;
        
        
        randSectorId = "123456789012345678901234";
        mcc = randSectorId.substring(0, 3);
        mnc = randSectorId.substring(3, 6);
        tac = randSectorId.substring(6, 12);
        nic = randSectorId.substring(12, 23);
        
        Long endTime = System.currentTimeMillis()+1000;
            while (true) {
                count++;
                beginTimeForMap = System.nanoTime();
                myStringRepOfInt = String.format("%08d", rd.nextInt(10000000));
                randSectorId = "123456789012345678901234"+myStringRepOfInt;
                if (count == 1)
                    System.out.println("Time taken for random nymber in nanoseconds: "+(System.nanoTime()-beginTimeForMap));                

                beginTimeForMap = System.nanoTime();
                last9to6 = randSectorId.substring(23, 26);
                last6to3 = randSectorId.substring(26, 29);
                last3to0 = randSectorId.substring(29, 32);
                beginTimeForMap = System.nanoTime();
                if (count == 1)
                    System.out.println("Time taken for substring in nanoseconds: "+(System.nanoTime()-beginTimeForMap));                

                
                beginTimeForMap = System.nanoTime();
                findMetadata = mccMncMap.get(mcc).get(mnc).get(tac).get(nic).get(last9to6).get(last6to3).get(last3to0);
                if (count == 1)
                    System.out.println("Time taken for Map to read in nanoseconds: "+(System.nanoTime()-beginTimeForMap));
                
                beginTimeForMap = System.nanoTime();
               
                try (Stream<String> lines = Files.lines(Paths.get(path + findMetadata.getFileName()))) {

                    if (lines != null) {
                        sectorIdFullLine = lines.skip(findMetadata.getLineNumber() - 1).findFirst().get();
                        if (count == 1)
                            System.out.println("Time taken for file to be read in nanoseconds: "+(System.nanoTime()-beginTimeForMap));
//                        System.out.println(sectorIdFullLine);
                    } else {
                        System.out.println("Not able to fine the looking line number");
                        continue;
                    }
                    
                    if(System.currentTimeMillis() >= endTime) {
                        System.out.println("Number of lines processed in 1 sec: "+count);
                        endTime = System.currentTimeMillis()+1000; 
                        count = 0;
                    }
                    
                } catch (IOException e) {
                    System.out.println("Exception occur");
                    e.printStackTrace();
                    continue;
                }
            }
    }
}
