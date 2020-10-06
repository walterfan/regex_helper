package com.github.walterfan.swing;

import com.github.walterfan.util.TextTransfer;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author walter
 * 
 */
public class ActionHandlerFactory {

    public static class ExitActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

    public static class ClearActionHandler implements ActionListener {
        private final JTextComponent txt_;

        public ClearActionHandler(JTextComponent txt) {
            this.txt_ = txt;
        }

        public void actionPerformed(ActionEvent event) {
            txt_.setText("");
        }
    }

    public static class CopyActionHandler implements ActionListener {
        private final JTextComponent txt_;

        public CopyActionHandler(JTextComponent txt) {
            this.txt_ = txt;
        }

        public void actionPerformed(ActionEvent event) {
            TextTransfer trans = new TextTransfer();
            trans.setClipboardContents(txt_.getText());  
            
        }
    }
    
    public static class TodoHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            SwingUtils.prompt("Todo it in future",
                    "The feature will be implemented in future.");
        }
    }

    public static class AboutHandler implements ActionListener,
            HyperlinkListener {
        private AboutDialog dialog;
        private final JFrame owner;
        private String title;
        private String content;
        private int width;
        private int height;

        public AboutHandler(JFrame owner, String title, String content,
                            int width, int height) {
            this.owner = owner;
            this.title = title;
            this.content = content;
            this.width = width;
            this.height = height;
        }

        public void actionPerformed(ActionEvent event) {
            if (dialog == null) // first time
                dialog = new AboutDialog(owner, title, content, width, height);
            dialog.setVisible(true); // pop up dialog
        }

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                JEditorPane pane = (JEditorPane) e.getSource();
                if (e instanceof HTMLFrameHyperlinkEvent) {
                    HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                    HTMLDocument doc = (HTMLDocument) pane.getDocument();
                    doc.processHTMLFrameHyperlinkEvent(evt);
                } else {
                    try {
                        pane.setPage(e.getURL());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }

        }
    }

    public static class HelpHandler implements ActionListener {
        private JFrame popupWindow;
        private String content;
        // private JEditorPane editPane;
        private HtmlPanel htmlPanel;

        public HelpHandler(JFrame owner, String title, String content,
                           int width, int height) {
            this.content = content;

            popupWindow = new JFrame(title);
            popupWindow.setUndecorated(true);

            popupWindow.getContentPane().setLayout(new BorderLayout());
            ((JComponent) popupWindow.getContentPane()).setBorder(BorderFactory
                    .createEtchedBorder());

            JButton aButton = new JButton("close");
            aButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    popupWindow.setVisible(false);
                }

            });

            htmlPanel = new HtmlPanel();
            /*
             * editPane = new JEditorPane(); editPane.setEditable(false);
             * editPane.addHyperlinkListener(this);
             * editPane.setContentType("text/html"); editPane.setText(content);
             */

            popupWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            popupWindow.getContentPane().add(new JScrollPane(htmlPanel),
                    BorderLayout.CENTER);
            popupWindow.getContentPane().add(aButton, BorderLayout.SOUTH);
            // popupWindow.pack();
            popupWindow.setSize(width, height);

        }

        public void actionPerformed(ActionEvent event) {
            htmlPanel.load("text/html", content);
            SwingUtils.moveToCenter(popupWindow);
            popupWindow.setVisible(true);
            // popupWindow.requestFocus();
            // popupWindow.pack();
        }

    }

    public static ActionListener createExitHandler() {
        return new ExitActionHandler();
    }

    public static ActionListener createClearHandler(JTextComponent txt) {
        return new ClearActionHandler(txt);
    }

    public static ActionListener createAboutHandler(JFrame owner, String title,
                                                    String content, int width, int height) {
        return new AboutHandler(owner, title, content, width, height);
    }

    public static ActionListener createHelpHandler(JFrame owner, String title,
                                                   String content, int width, int height) {
        return new HelpHandler(owner, title, content, width, height);
    }
}
