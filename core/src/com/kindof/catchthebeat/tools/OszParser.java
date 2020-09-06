package com.kindof.catchthebeat.tools;

import com.badlogic.gdx.Gdx;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

// In progress
public class OszParser {
    private static final int BUFFER_SIZE = 1024;

    public static boolean unzipOsz(File oszFile, File destinationDirectory) {
        try {
            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdirs();
            }

            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(oszFile));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File file = Gdx.files.absolute(destinationDirectory.getAbsolutePath() + "/" + zipEntry.getName()).file();
                if (!zipEntry.isDirectory()) {
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read;
                    while ((read = zipInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.close();
                } else {
                    file.mkdirs();
                }
                zipInputStream.closeEntry();
            }
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
