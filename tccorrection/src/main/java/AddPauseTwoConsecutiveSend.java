
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;


public class AddPauseTwoConsecutiveSend {

    private static boolean isToUpdate = false;
    private static boolean isCommentedCode = false;

    private static boolean isLastTagSend = false;
    
    private static String pauseString ="<pause milliseconds = \"101\"/>" ;
    public static void main(String[] args) {
        try {

            System.out.println("Adding pause between two consecutive send");
            // File dir = new
            // File("C:/FEE/GIT_Clone/ims_seesv/moduletest/Transcoding/Faults/IMS-62023_Required_100rel/01_3GPP_INV_183NoSDP_200SDP_switchB2B");
           //  File dir = new
            // File("Y:/m5jain/ims_Master/ims_seesv/moduletest/Transcoding/Features/RE123_106824_Reason_Header/Epic3_DynamicPayload_RtpMap/Update/Invite_sdp_UpdatefromB_200ok");
            File dir = new File("./");
            Collection<File> files = FileUtils.listFiles(dir, new RegexFileFilter("Client[AB].xml"), DirectoryFileFilter.DIRECTORY);

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
                    if (processingFile.getAbsolutePath().contains("ClientA") 
                            || processingFile.getAbsolutePath().contains("ClientB") ) {
                        
                        
                        if(processingLine.trim().startsWith("<send") && isLastTagSend){
                            if (discover(processingFile.getAbsolutePath()) == FileType.WINDOWS) {
                                newLines =  newLines + pauseString +  "\r\n"  ;
                            } else if (discover(processingFile.getAbsolutePath()) == FileType.UNIX) {
                                newLines =  newLines + pauseString +  "\n" ;
                            }
                            isToUpdate = true;
                        }
                        
                        if (processingLine.trim().startsWith("<send")) {
                            isLastTagSend = true;
                        }
                        
                        if(processingLine.trim().startsWith("<recv") || processingLine.trim().startsWith("<pause"))
                            isLastTagSend = false;
                                                
                    }


                    if (discover(processingFile.getAbsolutePath()) == FileType.WINDOWS) {
                            newLines = newLines + processingLine + "\r\n";
                    } else if (discover(processingFile.getAbsolutePath()) == FileType.UNIX) {
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
                                
                isLastTagSend = false;
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
