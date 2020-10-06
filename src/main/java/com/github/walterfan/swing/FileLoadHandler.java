package com.github.walterfan.swing;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

public class FileLoadHandler implements ActionListener {
    final JTextArea txtArea_;
    final Component component_;

    public FileLoadHandler(JTextArea txtArea, Component component) {
        txtArea_ = txtArea;
        component_ = component;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser c = null;
        if (StringUtils.isNotEmpty(getFilePath())) {
            c = new JFileChooser(getFilePath());
        } else {
            c = new JFileChooser(".");
        }
        int rVal = c.showOpenDialog(component_);
        if (rVal == JFileChooser.APPROVE_OPTION) {

            File srcFile = c.getSelectedFile();
            saveFilePath(srcFile.getAbsolutePath());
            try {
                String content = readFileToString(srcFile);
                txtArea_.setText(content);
            } catch (Exception e1) {
                SwingUtils.alert(e1.getMessage());
            }
        }
    }

    protected String readFileToString(File srcFile) throws Exception {
        return FileUtils.readFileToString(srcFile, "GBK");
    }

    public String getFilePath() {
        Preferences pref = Preferences.userRoot().node(
                component_.getClass().getName());
        String lastPath = pref.get("lastPath", "");
        return lastPath;
    }

    public void saveFilePath(String path) {
        Preferences pref = Preferences.userRoot().node(
                component_.getClass().getName());
        pref.put("lastPath", path);

    }
}