package com.sectorId.tenMillion;
class Metadata {
        short fileName;
        int lineNumber;
        
        public short getFileName() {
            return fileName;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public Metadata(short fileName, int lineNumber) {
            super();
            this.fileName = fileName;
            this.lineNumber = lineNumber;
        }

        @Override
        public String toString() {
            return "Metadata [fileName=" + fileName + ", lineNumber=" + lineNumber + "]";
        }
        
        
    }