package com.kindof.catchthebeat.tools;

import com.badlogic.gdx.Gdx;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Util {
    private static final int BUFFER_SIZE = 1024;

    public static boolean unzip(File zipFile, File destinationDirectory) {
        try {
            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdirs();
            }

            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
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

    public static float round(float number, int digits) {
        if (digits <= 0) {
            throw new InvalidParameterException("Util.round(" + number + ", " + digits + ") - \"digits\" cant be less than 1");
        }

        double pow10 = Math.pow(10, digits);
        return (float) (((int) (number * pow10)) / pow10);
    }
}
