package com.stpa.ws.firma.util;

import java.io.FileOutputStream;
import java.io.IOException;

public class CRLFOutputStream {

	private FileOutputStream fileOutput;

	public CRLFOutputStream(FileOutputStream file) {
        fileOutput= file;
    }

    public void write(int i) throws IOException {
        if (i== '\n') {
           fileOutput.write('\r');
     	   fileOutput.write('\n');
        } else {
        	fileOutput.write(i);
        }
    }
    public void write(byte[] buf) throws IOException {
        this .write(buf, 0, buf.length);
    }

    public void write(byte buf[], int off, int len) throws IOException {
        for (int i = off; i != off + len; i++) {
            this .write(buf[i]);
        }
    }
	
}
