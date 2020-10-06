package com.github.walterfan.swing;


import com.github.walterfan.util.ByteUtil;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class BinaryFileLoadHandler extends FileLoadHandler {
    public BinaryFileLoadHandler(JTextArea txtArea, Component component) {
        super(txtArea,component);
    }
    
    protected String readFileToString(File srcFile) throws Exception {
        byte[] bytes = FileUtils.readFileToByteArray(srcFile);
        Object obj = ByteUtil.bytes2Object(bytes);
        if(null == obj) {
            return "null";
        }
        return obj.toString();
    }
}
