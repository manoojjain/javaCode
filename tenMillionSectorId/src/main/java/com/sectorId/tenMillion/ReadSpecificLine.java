package com.sectorId.tenMillion;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Stream;

public class ReadSpecificLine {

    public static void main(String[] args) {
        
        
        TreeMap<String, Metadata> metaDataHashMap = new TreeMap<String, Metadata>();
        
//        if (args.length == 0 || args[0].equalsIgnoreCase("") ) {
//            System.out.println("Can not proceed without file path");
//            return;
//        }
//            
//        System.out.println("Print argument at position 0: "+args[0]);
        String path = "D:\\userdata\\m5jain\\Desktop\\RoboCalling_Related\\SectorId\\";
//        String path = args[0];//D:\\userdata\\m5jain\\Desktop\\UST_Exception\\
//        String fileName = "SectorID.dmp";
        
        String sectorId =null;
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
                if( md.getFileName() != 0 && md.getLineNumber() != 0)
                    metaDataHashMap.put(sectorId, md);
                else
                    System.out.println("filename or line number is not available");
            }
            
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Exception occur for filename or line number is not available");
        }
        
        
        System.out.println("Reading of metadata completed. Size of meta data: "+metaDataHashMap.size()+"Total time taken in miliseconds :"+(System.currentTimeMillis()-beginTime));
        System.out.println("Please run jmap -histo:live ");
        
        try {
            System.out.println("Sleeping for 30 secs");
            Thread.sleep(30000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

//        Metadata testData = new Metadata("1", 35);
//        metaDataHashMap.put("34567890123456789022", testData);
        

        Set<String> metaDataKeySet = metaDataHashMap.keySet();
        int count = 0;
        Random rd = new Random();
/*        List<String> randSectorIdstr = new ArrayList<String>();
        for (int i = 0; i < 10000; i++) {
            randSectorIdstr.add();
        }*/

        ArrayList<String> arrayList = new ArrayList<String>(metaDataKeySet);
        String randSectorId = null;
        Metadata findMetadata = null;
        Long endTime = System.currentTimeMillis()+1000;
            while (true) {
                randSectorId = arrayList.get(rd.nextInt(metaDataHashMap.size()));
                count++;
                findMetadata = metaDataHashMap.get(randSectorId);
                
                try (Stream<String> lines = Files.lines(Paths.get(path + findMetadata.getFileName()))) {

                    if (lines != null) {
                        String sectorIdFullLine = lines.skip(findMetadata.getLineNumber() - 1).findFirst().get();
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
