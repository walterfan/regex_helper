package com.github.walterfan;

import com.github.walterfan.swing.ActionHandlerFactory;
import com.github.walterfan.swing.EntrySelectListener;
import com.github.walterfan.swing.KeyValueTablePane;
import com.github.walterfan.swing.SwingTool;

import com.github.walterfan.swing.SwingUtils;
import com.github.walterfan.swing.UnderlineHighlighter;
import com.github.walterfan.util.RegexUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 
 * 1. frequency used regex pattern list 2. basic regex elements list 3. regex
 * verification text area
 * 
 * refer to JDK Pattern class
 * 
 * @author walter
 * 
 */
public class RegexTool extends SwingTool {

    private class SplitHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            final String strUrl = StringUtils.trim(txtText.getText());
            final String strRegex = StringUtils.trim(txtRegex.getText());
            try {
                Pattern pattern = Pattern.compile(strRegex);
                String[] arrRet = pattern.split(strUrl);
                if (null != arrRet) {
                    StringBuilder sb = new StringBuilder();
                    for (String str : arrRet) {
                        sb.append(str);
                        sb.append("\n");
                    }
                    txtResult.setText(sb.toString());
                } else {
                    txtResult.setText("not found");
                }
            } catch (Exception e) {
                txtResult.setText(e.getMessage());
            }
        }
    }

    private class ReplaceHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            final String strUrl = StringUtils.trim(txtText.getText());
            final String strRegex = StringUtils.trim(txtRegex.getText());
            final String strReplace = StringUtils.trim(txtReplace.getText());
            try {
                String strRet = strUrl.replaceAll(strRegex, strReplace);
                txtResult.setText(strRet);

            } catch (Exception e) {
                txtResult.setText(e.getMessage());
            }
        }
    }

    private class VerifyHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            final String strUrl = StringUtils.trim(txtText.getText());
            final String strRegex = StringUtils.trim(txtRegex.getText());

            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    Highlighter hilite = txtResult.getHighlighter();

                    SwingUtils.removeHighlights(txtResult);

                    Pattern pattern = null;
                    try {
                        pattern = Pattern.compile(strRegex);
                    } catch (PatternSyntaxException e) {
                        txtResult.setText("pattern error: " + e.getMessage());
                        try {
                            hilite.addHighlight(0,
                                    txtResult.getText().length(),
                                    new UnderlineHighlighter.UnderlineHighlightPainter(Color.red));
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                        return;
                    }
                    boolean ret = RegexUtils.isMatched(strUrl, pattern);
                    if (ret) {
                        txtResult
                                .setText("Congratulation, The regular expression matched your text :)");
                    } else {
                        txtResult
                                .setText("The regular expression do not match your text! :( ");
                        try {
                            hilite.addHighlight(0,
                                    txtResult.getText().length(),
                                    new UnderlineHighlighter.UnderlineHighlightPainter(Color.red));
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }

                }

            });

        }
    }

    private class LoadButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JFileChooser c = new JFileChooser("./etc");

            int rVal = c.showOpenDialog(RegexTool.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {

                File file = c.getSelectedFile();

                try {
                    String content = FileUtils.readFileToString(file);
                    txtText.setText(content);
                } catch (IOException e) {
                    SwingUtils.alert(e.getMessage());
                }

            }
        }
    }

    private class RegexListSelectionListener implements ListSelectionListener {
        // This method is called each time the user changes the set of selected
        // items
        public void valueChanged(ListSelectionEvent evt) {
            // When the user release the mouse button and completes the
            // selection,
            // getValueIsAdjusting() becomes false
            if (!evt.getValueIsAdjusting()) {
                JList list = (JList) evt.getSource();

                Object[] selected = list.getSelectedValues();

                if (selected != null && selected.length > 0) {
                    String str = (String) selected[0];
                    int idx = StringUtils.indexOf(str, ':');
                    String regex = StringUtils.substring(str, idx + 1);
                    txtRegex.setText(StringUtils.trim(regex));
                }
            }
        }
    }

    private class ClearHandler implements ActionListener {

        JTextArea txtArea = null;

        public ClearHandler(JTextArea txtArea) {
            this.txtArea = txtArea;
        }

        public void actionPerformed(ActionEvent event) {
            txtArea.setText("");
        }
    }

    private JLabel labelText = new JLabel("Text: ", SwingConstants.LEFT);

    private JLabel labelRegex = new JLabel("Regular expression: ",
            SwingConstants.LEFT);

    private JLabel labelExample = new JLabel("Regular example: ",
            SwingConstants.LEFT);

    private JLabel labelHint = new JLabel("Regular elements: ",
            SwingConstants.LEFT);

    private JLabel labelReplace = new JLabel("Replace: ", SwingConstants.LEFT);

    private JLabel labelResult = new JLabel("Result: ", SwingConstants.LEFT);

    private JButton btnVerify = new JButton("Match");

    private JButton btnReplace = new JButton("Replace");

    private JButton btnSplit = new JButton("Split");

    private JTextArea txtText = new JTextArea(3, 28);

    private JTextArea txtRegex = new JTextArea(3, 28);

    private JTextArea txtReplace = new JTextArea(2, 15);

    private JTextArea txtResult = new JTextArea(2, 15);

    private JButton btnClear1 = new JButton("Clear Text");

    private JButton btnClear2 = new JButton("Clear Regex");

    // private JButton btnReplace = new JButton("Replace");

    private JButton btnAbout = new JButton("About");

    private JButton btnHelp = new JButton("Help");

    private JButton btnExit = new JButton("Exit");

    JList regexList = new JList(new DefaultListModel());

    Map<Object, Object> regexMap = new LinkedHashMap<Object, Object>();

    public RegexTool() {
        super("Regular Expression Tool");
    }

    public RegexTool(String title) {
        super(title);
    }

    @Override
    public void initComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        assert (txtText != null);
        txtText.setLineWrap(true);
        txtText.setBackground(DEFAULT_COLOR);

        txtRegex.setLineWrap(true);
        txtRegex.setBackground(DEFAULT_COLOR);

        JPanel textPane = new JPanel();
        textPane.setLayout(new BorderLayout());
        textPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        textPane.add(labelText, BorderLayout.NORTH);
        textPane.add(SwingUtils.createScrollPane(txtText, DEFAULT_FONT,
                DEFAULT_COLOR), BorderLayout.CENTER);

        JPanel regexPane = new JPanel();
        regexPane.setLayout(new BorderLayout());
        regexPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        regexPane.add(labelRegex, BorderLayout.NORTH);
        regexPane.add(SwingUtils.createScrollPane(txtRegex, DEFAULT_FONT,
                DEFAULT_COLOR), BorderLayout.CENTER);

        JPanel replacePane = new JPanel();
        replacePane.setLayout(new BorderLayout());
        replacePane.setBorder(new EmptyBorder(5, 5, 5, 5));
        replacePane.add(labelReplace, BorderLayout.NORTH);
        replacePane.add(SwingUtils.createScrollPane(txtReplace, DEFAULT_FONT,
                DEFAULT_COLOR), BorderLayout.CENTER);

        JPanel resultPane = new JPanel();
        resultPane.setLayout(new BorderLayout());
        resultPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        resultPane.add(labelResult, BorderLayout.NORTH);
        resultPane.add(SwingUtils.createScrollPane(txtResult, DEFAULT_FONT,
                DEFAULT_COLOR), BorderLayout.CENTER);

        JSplitPane topPane = SwingUtils.createHSplitPane(textPane, regexPane,
                300);
        JSplitPane bottomPane = SwingUtils.createHSplitPane(replacePane,
                resultPane, 200);

        JPanel examPane = createRegexExamPanel();
        JPanel hintPane = createRegexHintPane();
        JSplitPane rightPane = SwingUtils.createVSplitPane(hintPane, examPane,
                300);

        JSplitPane leftPane = SwingUtils.createVSplitPane(topPane, bottomPane,
                330);
        JSplitPane mainPane = SwingUtils.createHSplitPane(leftPane, rightPane,
                500);
        // JPanel mainPanel = SwingUtils.createHorizontalPanel(new JComponent[]
        // {
        // textPane, regexPane, createRegexListPanel() });
        // contentPane.add(mainPanel, BorderLayout.CENTER);

        contentPane.add(mainPane, BorderLayout.CENTER);

        SwingUtils.createStdEditPopupMenu(new JTextComponent[] { txtText,
                txtRegex });
        btnClear1.addActionListener(new ClearHandler(this.txtText));
        btnClear2.addActionListener(new ClearHandler(this.txtRegex));

        btnVerify.addActionListener(new VerifyHandler());
        btnSplit.addActionListener(new SplitHandler());
        btnReplace.addActionListener(new ReplaceHandler());

        btnAbout.addActionListener(ActionHandlerFactory.createAboutHandler(
                this, "About " + appTitle_ + " " + appVersion_,
                " Wrote by walterfan@qq.com ", 320, 100));

        btnHelp.addActionListener(ActionHandlerFactory.createHelpHandler(this,
                getAppTitle(), getAppHelp(), 480, 240));

        btnExit.addActionListener(ActionHandlerFactory.createExitHandler());

        JPanel bottomPanel = SwingUtils.createHorizontalPanel(new JComponent[] {
                btnVerify, btnSplit, btnReplace, btnClear1, btnClear2, btnHelp,
                btnAbout, btnExit });
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void initToolbar(JToolBar aToolBar) {
        JButton btnSubmit = createOrangeButton("match",
                "Verify text as the regular expression ", 24, 24);
        btnSubmit.addActionListener(new VerifyHandler());
        aToolBar.add(btnSubmit);

        JButton btnSplit = createOrangeButton("split",
                "split text as the regular expression ", 24, 24);
        btnSplit.addActionListener(new SplitHandler());
        aToolBar.add(btnSplit);

        JButton btnReplace = createOrangeButton("replace",
                "replace text as the regular expression ", 24, 24);
        btnReplace.addActionListener(new ReplaceHandler());
        aToolBar.add(btnReplace);

        JButton btnOpen = createOrangeButton("open", "Load text file", 24, 24);
        btnOpen.addActionListener(new LoadButtonHandler());
        aToolBar.add(btnOpen);

        JButton btnExit = createOrangeButton("exit", "Exit", 24, 24);
        btnExit.addActionListener(ActionHandlerFactory.createExitHandler());
        aToolBar.add(btnExit);

        aToolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public JPanel createRegexHintPane() {
        KeyValueTablePane tabPane = new KeyValueTablePane("Element", "Explain",
                regexMap);
        tabPane.setSelectListener(new EntrySelectListener() {

            public void onSelect(Object key, Object value) {
                int pos = txtRegex.getCaretPosition();
                txtRegex.insert(String.valueOf(key), pos);
                Highlighter hilite = txtRegex.getHighlighter();

                SwingUtils.removeHighlights(txtRegex);

                try {
                    hilite.addHighlight(pos,
                            pos + String.valueOf(key).length(),
                            new UnderlineHighlighter.UnderlineHighlightPainter(Color.blue));
                } catch (BadLocationException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

        });

        JPanel regexPanel = new JPanel();

        regexPanel.setLayout(new BorderLayout());
        regexPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        regexPanel.add(labelHint, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(tabPane);
        regexPanel.add(scrollPane, BorderLayout.CENTER);
        tabPane.setOpaque(false);
        /*
         * regexPanel.setBackground(Color.ORANGE);
         * tabPane.setBackground(Color.ORANGE);
         * scrollPane.setBackground(Color.ORANGE);
         */

        return regexPanel;
    }

    public JPanel createRegexExamPanel() {
        regexList = new JList(new DefaultListModel());
        regexList.setVisibleRowCount(5);
        regexList.ensureIndexIsVisible(2);
        regexList.setSelectedIndex(0);
        regexList.setFixedCellWidth(60);
        regexList.addListSelectionListener(new RegexListSelectionListener());
        regexList.setBackground(Color.ORANGE);

        JPanel regexPanel = new JPanel();
        regexPanel.setLayout(new BorderLayout());
        regexPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        regexPanel.add(labelExample, BorderLayout.NORTH);
        regexPanel.add(new JScrollPane(regexList), BorderLayout.CENTER);

        return regexPanel;
    }

    public void pushRegex(String regex) {
        DefaultListModel model = (DefaultListModel) regexList.getModel();
        model.add(model.getSize(), regex);

    }

    @Override
    public void init() {
        StringBuilder sb = new StringBuilder("<html><body>");
        sb.append("<center><strong><u>Regular Expression Tool</u></strong></center><br/><br/>");
        sb.append("&nbsp; Fill the text and regular expression , then click the verify button \n<br/>");
        sb.append("&nbsp; Refer to \n<br/>");
        sb.append("<li> <a href='https://developer.mozilla.org/en/Core_JavaScript_1.5_Guide/Regular_Expressions'>JavaScript Regular Expressions Reference</a>");
        sb.append("<li> <a href='http://java.sun.com/docs/books/tutorial/essential/regex/index.html'>Java Tutorials: Regular Expressions</a>");
        sb.append("<li> http://regexlib.com");
        sb.append("</body></html>");
        this.setAppHelp(sb.toString());

        regexMap.put(".",
                "Any character (may or may not match line terminators");
        regexMap.put("\\d", "digit: [0-9");
        regexMap.put("\\D", "non-digit: [^0-9] ");
        regexMap.put("\\s", "whitespace character: [ \\t\\n\\x0B\\f\\r]");
        regexMap.put("\\S", "non-whitespace character: [^\\s] ");
        regexMap.put("\\w", "word character: [a-zA-Z_0-9]");
        regexMap.put("\\W", "non-word character: [^\\w]");

        regexMap.put("[abc]", "a, b, or c (simple class)");
        regexMap.put("[^abc]", "Any character except a, b, or c (negation)");
        regexMap.put("[a-zA-Z]",
                "a through z or A through Z, inclusive (range)");
        regexMap.put("[a-d[m-p]]",
                "a through d, or m through p: [a-dm-p] (union)");
        regexMap.put("[a-z&&[def]]", "d, e, or f (intersection)");
        regexMap.put("[a-z&&[^bc]]",
                "a through z, except for b and c: [ad-z] (subtraction)");
        regexMap.put("[a-z&&[^m-p]]",
                "a through z, and not m through p: [a-lq-z](subtraction)");

        regexMap.put("^", "The beginning of a line");
        regexMap.put("$", "The end of a line");
        regexMap.put("X?", "X, once or not at all");
        regexMap.put("X*", "X, zero or more times");
        regexMap.put("X+", "X, one or more times");
        regexMap.put("X{n}", "X, exactly n times");
        regexMap.put("X{n,}", "X, at least n times");
        regexMap.put("X{n,m}", "X, at least n but not more than m times");

        /*
         * regexMap.put("\\p{Lower}","A lower-case alphabetic character: [a-z]");
         * regexMap
         * .put("\\p{Upper}","An upper-case alphabetic character:[A-Z]");
         * regexMap.put("\\p{ASCII}","All ASCII:[\\x00-\\x7F]");
         * regexMap.put("\\p{Alpha}"
         * ,"An alphabetic character:[\\p{Lower}\\p{Upper}]");
         * regexMap.put("\\p{Digit}","A decimal digit: [0-9]");
         * regexMap.put("\\p{Alnum}"
         * ,"An alphanumeric character:[\\p{Alpha}\\p{Digit}]");
         * regexMap.put("\\p{Punct}"
         * ,"Punctuation: One of !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~");
         * regexMap.put
         * ("\\p{Graph}","A visible character: [\\p{Alnum}\\p{Punct}]");
         * regexMap
         * .put("\\p{Print}","A printable character: [\\p{Graph}\\x20]");
         * regexMap.put("\\p{Blank}","A space or a tab: [\\t]");
         * regexMap.put("\\p{Cntrl}","A control character: [\\x00-\\x1F\\x7F]");
         * regexMap.put("\\p{XDigit}","A hexadecimal digit: [0-9a-fA-F]");
         * regexMap
         * .put("\\p{Space}","A whitespace character: [ \\t\\n\\x0B\\f\\r]");
         */

        super.init();

        pushRegex("Email: " + RegexUtils.PATTERN_EMAIL.toString());
        pushRegex("Mobile: " + RegexUtils.PATTERN_MOBILE);
        pushRegex("Time: " + RegexUtils.PATTERN_TIME);
        pushRegex("Alpha: " + RegexUtils.PATTERN_ALPHA);
        pushRegex("Digital: " + RegexUtils.PATTERN_DIGITAL);
        pushRegex("URL: "
                + "^(http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}"
                + "(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*$");
        pushRegex("IDCard: " + RegexUtils.PATTERN_IDCARD_18);
        pushRegex("Telephone: " + RegexUtils.PATTERN_TEL);
        pushRegex("HasRepeat: " + RegexUtils.PATTERN_REPEAT);
        pushRegex("IP: " + RegexUtils.PATTERN_IP);
        pushRegex("Link: " + RegexUtils.PATTERN_LINK);
        pushRegex("Chinese: " + RegexUtils.PATTERN_CHINESE);
        pushRegex("Other: "
                + "(.|\\r|\\n)*type=meeting(.|\\r|\\n)*<flag>1</flag>(.|\\r|\\n)*");

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        RegexTool rt = new RegexTool("Regular Expression Tool");

        rt.init();
        SwingUtils.run(rt, 800, 600);

    }

}
