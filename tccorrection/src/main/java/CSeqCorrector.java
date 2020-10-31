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


public class CSeqCorrector {

    private static int prevCseq = 0;
    private static int currCseq = 0;
    private static int invCseq = 0;
    private static int otherMethodCseq = 0;

    private static boolean isToUpdate = false;
    private static boolean isCommentedCode = false;
    private static boolean isResponse = false;
    private static boolean  isFirstInvite = false;

    private static HashMap<String, String> replaceMapForClientA = new HashMap<String, String>(); 
    private static HashMap<String, String> replaceMapForClientB = new HashMap<String, String>();

    public static void main(String[] args) {
        try {
            // File[] faFiles = new
            // File("X:/master/ims_seesv/moduletest/Transcoding/Faults/IMS-31119/").listFiles();
            // X:/master/ims_seesv_master/moduletest/Transcoding/Faults/IMS-66184
            //Z:\master\ims_seesv\moduletest\Transcoding\Faults\IMS-63879_PRBT_VideoEarlyUpdate\06_Invite_183AV_port0FromMRFC
            
            
            System.out.println("Manoj Testing");
            //File dir = new File("Z:/master/ims_seesv/moduletest/Transcoding/Faults/IMS-63879_PRBT_VideoEarlyUpdate/06_Invite_183AV_port0FromMRFC");
            //File dir = new File("Y:/m5jain/ims_Master/ims_seesv/moduletest/Transcoding/Faults/IMS-38409_Call_Hold_ET_to_MT/EE925064");
            //File dir = new File("Z:/master/ims_seesv/moduletest/Transcoding/Faults/IMS-40642_NegativeResponseFromMRFC/03_ReInvite_ActiveToActive_FourthInviteToMRFCFailed");
            File dir = new File("./");
            Collection<File> files = FileUtils.listFiles(dir, new RegexFileFilter("Client[AB].xml" ), DirectoryFileFilter.DIRECTORY);

            // File("X:/master/ims_seesv/moduletest/Transcoding/Faults/IMS-30980_Detect_Textmerge/ClientB.xml");
            for (File file : files) {
                currCseq = 0;
                prevCseq = 0;
                invCseq = 0;
                isToUpdate = false;
                String directory = null;
                if(file.getAbsolutePath().contains("ClientA")){
                    directory = file.getAbsolutePath().replace("ClientA.xml", "");
                }else  if(file.getAbsolutePath().contains("ClientB")){
                    directory = file.getAbsolutePath().replace("ClientB.xml", "");
                }
                System.out.println(" file = " + file.getAbsolutePath());
                System.out.println("File Name " + file.getName());
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = "";
                String newTest = "";
                
                
                
                while ((line = reader.readLine()) != null) {
                    String method = null;
                    if (line.contains("<!--")) {
                        isCommentedCode = true;
                    }
                    if (line.contains("-->")) {
                        isCommentedCode = false;
                    }
                    if (isCommentedCode) {
                        if(discover(file.getAbsolutePath()) == FileType.WINDOWS){
                            newTest = newTest + line + "\r\n";
                        }
                        else if(discover(file.getAbsolutePath())== FileType.UNIX){
                            newTest = newTest + line + "\n";
                        }
                        continue;
                    }
                    
                    if(line.trim().startsWith("SIP/2.0")){
                        isResponse= true;
                    }
                    
                    if (line.contains("]]>")) {
                        isResponse = false;
                    }
                    
                    if (isResponse) {
                        if(discover(file.getAbsolutePath()) == FileType.WINDOWS){
                            newTest = newTest + line + "\r\n";
                        }
                        else if(discover(file.getAbsolutePath())== FileType.UNIX){
                            newTest = newTest + line + "\n";
                        }
                        continue;
                    }
                    
                    if (line.trim().startsWith("CSeq")) {
                        System.out.println("Processing Line" + line);

                        //method = line.split(":")[1].split(" ")[2];
                        //currCseq = Integer.parseInt(line.split(":")[1].split(" ")[1]);
                        method = line.substring(line.lastIndexOf(" "), line.length()).trim();
                        currCseq = Integer.parseInt(line.substring(line.indexOf(":")+1, line.lastIndexOf(" ")).trim());

                        if (prevCseq == 0) {
                            prevCseq = currCseq;
                            otherMethodCseq = currCseq;
                            isFirstInvite = true;
                        }

                        if ("INVITE".equals(method) && isFirstInvite) {
                            //invCseq = Integer.parseInt(line.split(":")[1].split(" ")[1]);
                            invCseq = Integer.parseInt(line.substring(line.indexOf(":")+1, line.lastIndexOf(" ")).trim());
                        } else if ("INVITE".equals(method)) {
                            invCseq = otherMethodCseq;
                        }
                        
                        if ((prevCseq + 1) != currCseq && !method.equals("ACK") && !method.equals("CANCEL") && !isFirstInvite) {
                            System.out.println("Before replacing other method " + line + " " + prevCseq + " " + currCseq);
                            //line = line.replace("CSeq: " + currCseq + " " + method, "CSeq: " + (prevCseq + 1) + " " + method);
                            line = line.replace(line, line.substring(0, line.indexOf("CSeq"))+"CSeq: " + (prevCseq + 1) + " " + method);
                            System.out.println("After replacing other method " + line + " " + prevCseq + " " + currCseq+ otherMethodCseq);
                            isToUpdate = true;
                            if (file.getName().contains("ClientA")) {
                                replaceMapForClientA.put(currCseq+" "+method, (prevCseq + 1)+" "+method);
                            } else if (file.getName().contains("ClientB")) {
                                replaceMapForClientB.put(currCseq+" "+method, (prevCseq + 1)+" "+method);
                            }
                        }

                        if (("ACK").equals(method) || ("CANCEL").equals(method)) {
                            if (invCseq != currCseq) {
                                System.out.println("Before replacing ACK method " + line + " " + invCseq + " " + currCseq);
                                //line = line.replace("CSeq: " + currCseq + " " + method, "CSeq: " + (invCseq) + " " + method);
                                line = line.replace(line, line.substring(0, line.indexOf("CSeq"))+ "CSeq: " + (invCseq) + " " + method);
                                System.out.println("After replacing ACK method " + line + " " + invCseq + " " + currCseq+ otherMethodCseq);
                                isToUpdate = true;
                                if (file.getName().contains("ClientA")) {
                                    replaceMapForClientA.put(currCseq+" "+method, invCseq+" "+method);
                                } else if (file.getName().contains("ClientB")) {
                                    replaceMapForClientB.put(currCseq+" "+method, invCseq+" "+method);
                                }
                            }
                        }

                        if (!method.equals("ACK") && !method.equals("CANCEL") && !isFirstInvite) {
                            System.out.println("Increasing othersCseq: "+otherMethodCseq);
                            otherMethodCseq = otherMethodCseq + 1;
                            if(method.equals("INVITE")){
                                invCseq = otherMethodCseq;
                            }
                        }

                        prevCseq = otherMethodCseq;
                        isFirstInvite = false;
                    }
                    if(discover(file.getAbsolutePath()) == FileType.WINDOWS){
                        newTest = newTest + line + "\r\n";
                    }
                    else if(discover(file.getAbsolutePath())== FileType.UNIX){
                        newTest = newTest + line + "\n";
                    }
                }

                reader.close();

                if (isToUpdate) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(newTest);
                    writer.close();
                    //convertDosToUnixUtil(file);
                }
                
                
                File tcFile = new File(directory+"tc.txt");
                if(!tcFile.exists()){
                    replaceMapForClientA.clear();
                    replaceMapForClientB.clear();
                    continue;
                }
                if (file.getAbsolutePath().contains("ClientA")) {
                    BufferedReader tcReader = new BufferedReader(new FileReader(tcFile));
                    String tcLine;
                    String newTcTxt = "";
                    boolean isToWriteTc = false;
                    while ((tcLine = tcReader.readLine()) != null) {
                        
                        if ((tcLine.contains("_sent_") || tcLine.contains("_rcvd_")) && 
                                tcLine.toLowerCase().contains("clienta")) {
                            
                            String tcCseq = "";
                            int indexOfLastFourSpaceOrTab = -1;
                            indexOfLastFourSpaceOrTab = tcLine.lastIndexOf("    ");
                            if(indexOfLastFourSpaceOrTab == -1)
                                indexOfLastFourSpaceOrTab = tcLine.lastIndexOf("\t");
                            try{
                                 if(tcLine.contains("_sent_"))       
                                   tcCseq = tcLine.substring(tcLine.indexOf("_sent_")+6, indexOfLastFourSpaceOrTab);
                                 else if(tcLine.contains("_rcvd_"))
                                     tcCseq = tcLine.substring(tcLine.indexOf("_rcvd_")+6, indexOfLastFourSpaceOrTab);
                            }catch(Exception e){
                                System.out.println("Exception for indexOfLastFourSpaceOrTab for A" + e);
                                continue;
                            }
                            if (replaceMapForClientA.containsKey(tcCseq)) {
                                tcLine = tcLine.replace(tcCseq, replaceMapForClientA.get(tcCseq));
                                isToWriteTc = true;
                            }
                            
                        }
                        
                        if(discover(tcFile.getAbsolutePath()) == FileType.WINDOWS){
                            newTcTxt = newTcTxt + tcLine + "\r\n";
                        }
                        else if(discover(tcFile.getAbsolutePath())== FileType.UNIX){
                            newTcTxt = newTcTxt + tcLine + "\n";
                        }
                    }
                    tcReader.close();
                    if (isToWriteTc) {
                        FileWriter writer = new FileWriter(tcFile);
                        writer.write(newTcTxt);
                        writer.close();
                        //convertDosToUnixUtil(file);
                    }
                    replaceMapForClientA.clear();
                }
                
               
                if (file.getAbsolutePath().contains("ClientB")) {
                    BufferedReader tcReader = new BufferedReader(new FileReader(tcFile));
                    String tcLine;
                    String newTcTxt = "";
                    boolean isToWriteTc = false;
                    while ((tcLine = tcReader.readLine()) != null) {
                        
                        if ((tcLine.contains("_sent_") || tcLine.contains("_rcvd_")) && 
                                tcLine.toLowerCase().contains("clientb")) {
                            
                            String tcCseq = "";
                            int indexOfLastFourSpaceOrTab = -1;
                            indexOfLastFourSpaceOrTab = tcLine.lastIndexOf("    ");
                            if(indexOfLastFourSpaceOrTab == -1)
                                indexOfLastFourSpaceOrTab = tcLine.lastIndexOf("\t");
                            try{
                                if(tcLine.contains("_sent_"))       
                                    tcCseq = tcLine.substring(tcLine.indexOf("_sent_")+6, indexOfLastFourSpaceOrTab);
                                else if(tcLine.contains("_rcvd_"))
                                    tcCseq = tcLine.substring(tcLine.indexOf("_rcvd_")+6, indexOfLastFourSpaceOrTab);
                            }catch(Exception e){
                                System.out.println("Exception for indexOfLastFourSpaceOrTab for B" + e);
                                continue;
                            }
                            if (replaceMapForClientB.containsKey(tcCseq)) {
                                tcLine = tcLine.replace(tcCseq, replaceMapForClientB.get(tcCseq));
                                isToWriteTc = true;
                            }
                            
                        }
                        
                        if(discover(tcFile.getAbsolutePath()) == FileType.WINDOWS){
                            newTcTxt = newTcTxt + tcLine + "\r\n";
                        }
                        else if(discover(tcFile.getAbsolutePath())== FileType.UNIX){
                            newTcTxt = newTcTxt + tcLine + "\n";
                        }
                    }
                    tcReader.close();
                    if (isToWriteTc) {
                        FileWriter writer = new FileWriter(tcFile);
                        writer.write(newTcTxt);
                        writer.close();
                        //convertDosToUnixUtil(file);
                    }
                    replaceMapForClientB.clear();
                }
                
                
                
/*                Runtime rt = Runtime.getRuntime();
                Process proc;
                String cmd = "dos2unix " + file.getAbsolutePath();
                proc = rt.exec(cmd);*/
                System.out.println("Successfully finish"+isToUpdate);
            }
        } catch (Exception e) {
            System.out.println("Exception caught " + e);
            e.printStackTrace();
        }
    }
    

//    public static void convertDosToUnixUtil(File confFile) throws Exception {
//        BufferedReader reader = new BufferedReader(new FileReader(confFile));
//        String line = "", oldtext = "";
//        while((line = reader.readLine()) != null)
//        {
//            oldtext += line + "\n";
//        }
//        reader.close();
//        
//        confFile.delete();
//        confFile.createNewFile();
//       
//        FileWriter writer = new FileWriter(confFile);
//        writer.write(oldtext);
//        writer.close();
//    }

    public enum FileType { WINDOWS, UNIX, MAC, UNKNOWN }

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
            switch(c) {        
            case LF: return FileType.UNIX;
            case CR: {
                if (reader.read() == LF) return FileType.WINDOWS;
                return FileType.MAC;
            }
            default: continue;
            }
        }
        return FileType.UNKNOWN;
    }
}