package com.sectorId.tenMillion;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Stream;

public class ReadSpecificLineRandomFile {

    public static void main(String[] args) throws IOException {
        
        
        TreeMap<String, Metadata> metaDataHashMap = new TreeMap<String, Metadata>();
        
        if (args.length == 0 || args[0].equalsIgnoreCase("") ) {
            System.out.println("Can not proceed without file path");
            return;
        }
            
        System.out.println("Print argument at position 0: "+args[0]);
//        String path = "D:\\userdata\\m5jain\\Desktop\\RoboCalling_Related\\SectorId\\";
        String path = args[0];//D:\\userdata\\m5jain\\Desktop\\UST_Exception\\
//        String fileName = "SectorID.dmp";
        
        String sectorId =null;
        short fileNameFrmMetaData = 0;
        int lineNumberFromMetaData = 0;
        long beginTime = 0l;
        
        int numberOfFiles = 100;
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
                if( md.getFileName() != 0 && md.getLineNumber() != 0) {
                    metaDataHashMap.put(sectorId, md);
                } else
                    System.out.println("filename or line number is not available");
            }
            
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Exception occur for filename or line number is not available");
        }
        
        
        System.out.println("Reading of metadata completed. Size of meta data: "+metaDataHashMap.size()+"Total time taken in miliseconds :"+(System.currentTimeMillis()-beginTime));
        System.out.println("Please run jmap -histo:live ");
        
//        try {
//            System.out.println("Sleeping for 30 secs");
//            Thread.sleep(30000);
//        } catch (InterruptedException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
        Set<String> metaDataKeySet = metaDataHashMap.keySet();
        int count = 0;
        Random rd = new Random();

        ArrayList<String> metaDataArrayList = new ArrayList<String>(metaDataKeySet);

        String randSectorId = null;
        Metadata findMetadata = null;
        Long endTime = System.currentTimeMillis()+1000;
        ArrayList<Long> [] filePointerArrayOfArrayList = new ArrayList[numberOfFiles];
        RandomAccessFile [] rafArray = new RandomAccessFile[numberOfFiles];
        for (int i = 0; i < rafArray.length ; i++) {
            beginTime = System.currentTimeMillis();
            rafArray[i] = new RandomAccessFile(path+(i+1),"r"); 
            //above 'r' means open in read only mode
            filePointerArrayOfArrayList[i] = new ArrayList<Long>();
            String cur_line = "";
            while((cur_line=rafArray[i].readLine())!=null)
            {
                filePointerArrayOfArrayList[i].add(rafArray[i].getFilePointer());
            }
            System.out.println("Read one file time taken in miliseconds :"+(System.currentTimeMillis()-beginTime));
        }
        
        System.out.println("Reading of filepointers completed " );

        
        while (true) {
            randSectorId = metaDataArrayList.get(rd.nextInt(metaDataHashMap.size()));
            count++;
            findMetadata = metaDataHashMap.get(randSectorId);

            rafArray[findMetadata.getFileName()-1].seek(filePointerArrayOfArrayList[findMetadata.getFileName()-1].get(findMetadata.getLineNumber() - 1));
            
            String sectorIdFullLine = rafArray[findMetadata.getFileName()-1].readLine();
//            if (sectorIdFullLine != null) {
//                System.out.println(sectorIdFullLine);
//            } else {
//                System.out.println("Not able to find the looking line number for: "+randSectorId);
//                System.out.println("Not able to find the looking line number for: "+rafArray[findMetadata.getFileName()-1].readLine());
//                continue;
//            }

            if (System.currentTimeMillis() >= endTime) {
                System.out.println("Number of lines processed in 1 sec: " + count);
                endTime = System.currentTimeMillis() + 1000;
                count = 0;
            }
        }
        
//        for (int i = 0; i < rafArray.length; i++) {
//            rafArray[i].close();;
//        }
    }
}
