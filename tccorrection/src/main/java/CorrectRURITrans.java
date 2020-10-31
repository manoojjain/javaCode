import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.StringUtils;

public class CorrectRURITrans {

    private static boolean isToUpdate = false;
    private static boolean isCommentedCode = false;
    private static boolean isResponse = false;
    private static boolean isActionAdded = false;

    private static boolean isSendAfterAction = false;
    private static boolean isDummpyHeaderAdded = false;
    
    private static boolean isActionPresent = false;
    private static boolean isFirstRecv = false;
    
    private static boolean isFirstRouteHeader = false;
    private static boolean isToReomeRoute = false;
    
    private static boolean isLastCSeqCancel = false;
    
    
    private static String recvAppendStringWoAction = "<ereg regexp=\"(.(sip:(.*);app(.*)).)\" search_in=\"hdr\" header=\"Contact\" check_it=\"true\" assign_to=\"13,14,15,16\"/>";
    private static String recvAppendString = "<action><ereg regexp=\"(.(sip:(.*);app(.*)).)\" search_in=\"hdr\" header=\"Contact\" check_it=\"true\" assign_to=\"13,14,15,16\"/></action>";
    private static String dummyHeader = "Dummy: [$13][$14][$15][$16]";

    private static HashMap<String, String> replaceMapForClientA = new HashMap<String, String>();
    private static HashMap<String, String> replaceMapForClientB = new HashMap<String, String>();

    public static void main(String[] args) {
        try {

            System.out.println("RURI Testing");
            //File dir = new File("C:/FEE/GIT_Clone/ims_seesv/moduletest/Transcoding/Faults/IMS-62023_Required_100rel/01_3GPP_INV_183NoSDP_200SDP_switchB2B");
            //File dir = new File("Y:/m5jain/ims_Master/ims_seesv/moduletest/Transcoding/Faults/IDS-3166_IMS-49346/01_TermMode_InviteCancelFromA");
            // File dir = new
            // File("Z:/master/ims_seesv/moduletest/Transcoding/Faults/IMS-40642_NegativeResponseFromMRFC/03_ReInvite_ActiveToActive_FourthInviteToMRFCFailed");
            File dir = new File("./");
            Collection<File> files = FileUtils.listFiles(dir, new RegexFileFilter("Client[AB].xml"), DirectoryFileFilter.DIRECTORY);

            // File("X:/master/ims_seesv/moduletest/Transcoding/Faults/IMS-30980_Detect_Textmerge/ClientB.xml");
            for (File processingFile : files) {
                isToUpdate = false;
                String directory = null;

                System.out.println("Processing file path:" + processingFile.getAbsolutePath());
                System.out.println("Processing file Name: " + processingFile.getName());

                BufferedReader reader = new BufferedReader(new FileReader(processingFile));
                String processingLine = "";
                String newLines = "";

                while ((processingLine = reader.readLine()) != null) {
                    if (processingLine.contains("<!--")) {
                        isCommentedCode = true;
                    }
                    if (processingLine.contains("-->")) {
                        isCommentedCode = false;
                    }

                    if (isCommentedCode || processingLine.contains("-->")) {
                        if (discover(processingFile.getAbsolutePath()) == FileType.WINDOWS) {
                            newLines = newLines + processingLine + "\r\n";
                        } else if (discover(processingFile.getAbsolutePath()) == FileType.UNIX) {
                            newLines = newLines + processingLine + "\n";
                        }
                        continue;
                    }
                    if (processingFile.getAbsolutePath().contains("ClientA")) {
                        
                        if(processingLine.trim().startsWith("Route:") && isFirstRouteHeader==true ){
                            isToReomeRoute = true;
                            isToUpdate = true;
                        }else{
                            isToReomeRoute = false;
                        }
                        
                        
                        if(processingLine.trim().startsWith("Route:") && isFirstRouteHeader==false ){
                            isFirstRouteHeader = true;
                        }
                        
                        
                        if(processingLine.trim().startsWith("CSeq:") && processingLine.toLowerCase().contains("cancel")){
                            isLastCSeqCancel = true;
                        }else if(processingLine.trim().startsWith("CSeq:")){
                            isLastCSeqCancel = false;
                        }
                      
                        
                        if (processingLine.trim().startsWith("<recv response")) {
                            int recvResponseNumber = Integer.parseInt(processingLine.split("\"")[1]);
                            if (recvResponseNumber > 100 && recvResponseNumber < 300) {
                                System.out.println("First response between 100 to 300 " + recvResponseNumber);
                                isFirstRecv = true;
                            }
                        }
                                
                         if(isFirstRecv && isActionAdded == false && isLastCSeqCancel == false ) {
                            if(processingLine.trim().contains("</recv") ){ 
                                isFirstRecv = false;
                                System.out.println("Processing Line" + processingLine);
                                if(processingLine.trim().contains("<action")){
                                    isActionPresent = true;
                                }
                                if(processingLine.trim().contains("</action") && isActionPresent){
                                    String tempLine1 = processingLine.substring(0, processingLine.indexOf("</action"));
                                    String tempLine2 = processingLine.substring(processingLine.indexOf("</action"), processingLine.length());
        
                                    processingLine =  tempLine1+ recvAppendStringWoAction   + tempLine2;
                                    System.out.println("Manoj Pro!!! " + processingLine);
                                    isActionAdded = true;
                                    isToUpdate = true;
                                }
                                if(isActionAdded == false && isActionPresent == false){
                                    String tempLine1 = processingLine.substring(0, processingLine.indexOf("</recv>"));
                                    String tempLine2 = processingLine.substring(processingLine.indexOf("</recv>"), processingLine.length());
        
                                    processingLine =  tempLine1+ recvAppendString   + tempLine2;
                                    System.out.println("Manoj Pro!!! " + processingLine);
                                    isActionAdded = true;
                                    isToUpdate = true;
                                    }
                            }
                            if(isFirstRecv && isActionAdded == false ){
                                if(processingLine.trim().contains("<action")){
                                    isActionPresent = true;
                                }
                                
                            }if(isActionPresent && processingLine.contains("</action>")){

                                String tempLine1 = processingLine.substring(0, processingLine.indexOf("</action"));
                                String tempLine2 = processingLine.substring(processingLine.indexOf("</action"), processingLine.length());
    
                                processingLine =  tempLine1+ recvAppendStringWoAction   + tempLine2;
                                System.out.println("Manoj Pro!!! " + processingLine);
                                isActionAdded = true;
                                isToUpdate = true;
                                
                            } 
                        }
    

                        if (isActionAdded) {
                            if (processingLine.trim().startsWith("<send") && isDummpyHeaderAdded == false) {
                                isSendAfterAction = true;
                            }

                            if (processingLine.trim().endsWith("SIP/2.0")) {
                                processingLine = processingLine.trim().split(" ")[0] + " [$15] " + "SIP/2.0";
                            }
                        }

                        if (processingLine.trim().startsWith("[last_Call-ID:]") && isSendAfterAction) {
                            if (discover(processingFile.getAbsolutePath()) == FileType.WINDOWS) {
                                newLines = newLines + dummyHeader + "\r\n";
                            } else if (discover(processingFile.getAbsolutePath()) == FileType.UNIX) {
                                newLines = newLines + dummyHeader + "\n";
                            }
                            isSendAfterAction = false;
                            isDummpyHeaderAdded = true;
                        }
                    }
         
                    else  if(processingFile.getAbsolutePath().contains("ClientB")) {
                        
                        if(processingLine.trim().startsWith("Route:") ){
                            isToReomeRoute = true;
                            isToUpdate = true;
                        }else{
                            isToReomeRoute = false;
                        }
                        
                        
                        if(processingLine.trim().startsWith("CSeq:") && processingLine.toLowerCase().contains("cancel")){
                            isLastCSeqCancel = true;
                        }else if(processingLine.trim().startsWith("CSeq:")){
                            isLastCSeqCancel = false;
                        }
                        
                        
                        if (processingLine.trim().startsWith("<recv request=")) {
                            isFirstRecv = true;
                        }

                                
                         if(isFirstRecv && isActionAdded == false && isLastCSeqCancel==false) {
                            if(processingLine.trim().contains("</recv") ){ 
                                isFirstRecv = false;
                                System.out.println("Processing Line" + processingLine);
                                if(processingLine.trim().contains("<action")){
                                    isActionPresent = true;
                                }
                                if(processingLine.trim().contains("</action") && isActionPresent){
                                    String tempLine1 = processingLine.substring(0, processingLine.indexOf("</action"));
                                    String tempLine2 = processingLine.substring(processingLine.indexOf("</action"), processingLine.length());
        
                                    processingLine =  tempLine1+ recvAppendStringWoAction   + tempLine2;
                                    System.out.println("Manoj Pro!!! " + processingLine);
                                    isActionAdded = true;
                                    isToUpdate = true;
                                }
                                if(isActionAdded == false && isActionPresent == false){
                                    String tempLine1 = processingLine.substring(0, processingLine.indexOf("</recv>"));
                                    String tempLine2 = processingLine.substring(processingLine.indexOf("</recv>"), processingLine.length());
        
                                    processingLine =  tempLine1+ recvAppendString   + tempLine2;
                                    System.out.println("Manoj Pro!!! " + processingLine);
                                    isActionAdded = true;
                                    isToUpdate = true;
                                    }
                            }
                            if(isFirstRecv && isActionAdded == false ){
                                if(processingLine.trim().contains("<action")){
                                    isActionPresent = true;
                                }
                                
                            }if(isActionPresent && processingLine.contains("</action>")){

                                String tempLine1 = processingLine.substring(0, processingLine.indexOf("</action"));
                                String tempLine2 = processingLine.substring(processingLine.indexOf("</action"), processingLine.length());
    
                                processingLine =  tempLine1+ recvAppendStringWoAction   + tempLine2;
                                System.out.println("Manoj Pro!!! " + processingLine);
                                isActionAdded = true;
                                isToUpdate = true;
                                
                            } 
                        }
                         

                        if (isActionAdded) {
                            if (processingLine.trim().startsWith("<send") && isDummpyHeaderAdded == false) {
                                isSendAfterAction = true;
                            }

                            if (processingLine.trim().endsWith("SIP/2.0")) {
                                processingLine = processingLine.trim().split(" ")[0] + " [$15] " + "SIP/2.0";
                            }
                        }

                        if (processingLine.trim().startsWith("[last_Call-ID:]") && isSendAfterAction) {
                            if (discover(processingFile.getAbsolutePath()) == FileType.WINDOWS) {
                                newLines = newLines + dummyHeader + "\r\n";
                            } else if (discover(processingFile.getAbsolutePath()) == FileType.UNIX) {
                                newLines = newLines + dummyHeader + "\n";
                            }
                            isSendAfterAction = false;
                            isDummpyHeaderAdded = true;
                        }

                        
                    }
                    
                    
                    if (discover(processingFile.getAbsolutePath()) == FileType.WINDOWS) {
                        if(isToReomeRoute==false)
                            newLines = newLines + processingLine + "\r\n";
                    } else if (discover(processingFile.getAbsolutePath()) == FileType.UNIX) {
                        if(isToReomeRoute==false)
                            newLines = newLines + processingLine + "\n";
                    }
                    
                }
                    reader.close();

                    if (isToUpdate) {
                        FileWriter writer = new FileWriter(processingFile);
                        writer.write(newLines);
                        writer.close();
                    }
                    isToUpdate = false;
                    isCommentedCode = false;
                    isResponse = false;
                    isActionAdded = false;

                    isSendAfterAction = false;
                    isDummpyHeaderAdded = false;
                    
                    isActionPresent = false;
                    isFirstRecv = false;
                    
                    isToReomeRoute = false;
                    isFirstRouteHeader = false;
                    
                    isLastCSeqCancel= false;
                System.out.println("Successfully Finish");
            }
        } catch (Exception e) {
            System.out.println("Exception caught main " + e);
            e.printStackTrace();
        }
    }

    public enum FileType {
        WINDOWS, UNIX, MAC, UNKNOWN
    }

    private static final char CR = '\r';
    private static final char LF = '\n';

    public static FileType discover(String fileName) throws IOException {

        Reader reader = new BufferedReader(new FileReader(fileName));
        FileType result = discover(reader);
        reader.close();
        return result;
    }

    private static FileType discover(Reader reader) throws IOException {
        int c;
        while ((c = reader.read()) != -1) {
            switch (c) {
            case LF:
                return FileType.UNIX;
            case CR: {
                if (reader.read() == LF)
                    return FileType.WINDOWS;
                return FileType.MAC;
            }
            default:
                continue;
            }
        }
        return FileType.UNKNOWN;
    }
}
