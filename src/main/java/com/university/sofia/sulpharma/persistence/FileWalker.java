package com.university.sofia.sulpharma.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The type File walker.
 */
@Component
@Slf4j
public class FileWalker {

    /**
     * Walk a file path and get files.
     *
     * @param path the path
     * @return the list
     */
    public List<File> walk(String path) {
        List<File> csvFiles = new ArrayList<>();
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            log.info("No files found inside directory" + path);
            return csvFiles;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                csvFiles.addAll(walk(f.getAbsolutePath()));
                log.info("Dir:" + f.getAbsoluteFile());
            } else {
                if (isCsvFile(f.getName())) {
                    csvFiles.add(f.getAbsoluteFile());
                }
            }
        }

        return csvFiles;
    }

    private boolean isCsvFile(String filename) {
        if (filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1).equals("csv");
        }
        return false;
    }

}
