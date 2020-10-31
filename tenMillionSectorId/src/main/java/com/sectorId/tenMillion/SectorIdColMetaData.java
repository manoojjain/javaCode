package com.sectorId.tenMillion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;


public class SectorIdColMetaData {

    String sectorIdCol[] = new String[10];
    HashMap<String, String> esnHashMap = new HashMap<String, String>();

    public SectorIdColMetaData(String sectorIdCol) {
        parseString(sectorIdCol);
    }

    @Override
    public String toString() {
        return "SectorIdColMetaData [sectorIdCol=" + Arrays.toString(sectorIdCol) + ", esnHashMap=" + esnHashMap + "]";
    }

    private void parseString(String str) {
        String keyValueEsn;
        String key, value;
        StringTokenizer esnLineTokenizer;
        // String str = "000, 00, 5, 00, 00, 5, 5-5-5, 5, 5, H,e5=000;e5=0567895;e5=0678955;e5=5567890;e5=0556789;e6=0556789;e7=0565789;e8=0556798;e9=0556987;e50=0576589;e55=0556789;e55=0556789;e55=0556789;";
        StringTokenizer lineTokenizer = new StringTokenizer(str, ",");
        int i = 0;
        while (lineTokenizer.hasMoreTokens()) {

            if (i == 10) {
                esnLineTokenizer = new StringTokenizer(lineTokenizer.nextToken(), ";");
                while (esnLineTokenizer.hasMoreTokens()) {
                    keyValueEsn = esnLineTokenizer.nextToken();
                    key = keyValueEsn.split("=")[0];
                    value = keyValueEsn.split("=")[1];
                    if (key != null && value != null)
                        esnHashMap.put(key, value);
                }
            } else {
                sectorIdCol[i] = lineTokenizer.nextToken();
            }

            i++;
        }
    }

    public static void main(String arg[]) {
        SectorIdColMetaData sd = new SectorIdColMetaData("000, 00, 5, 00, 00, 5, 5-5-5, 5, 5, H,e5=000;e5=0567895;e5=0678955;e5=5567890;e5=0556789;e6=0556789;e7=0565789;e8=0556798;e9=0556987;e50=0576589;e55=0556789;e55=0556789;e55=0556789;");
        System.out.println(sd);
    
    }

}
