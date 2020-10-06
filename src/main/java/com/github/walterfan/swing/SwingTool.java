package com.github.walterfan.swing;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

@Slf4j
public abstract class SwingTool extends JFrame {


    private static final long serialVersionUID = 1L;
    public static final Color DEFAULT_COLOR = new Color(0x99, 0xFF, 0xCC);
    public static final Font DEFAULT_FONT = new Font(Font.MONOSPACED,
            Font.PLAIN, 14);
    public static final int TEXT_SIZE = 8;

    protected JMenuBar menuBar = new JMenuBar();

    protected JMenu fileMenu = new JMenu("Usage");

    protected JMenu helpMenu = new JMenu("Help");

    protected String appTitle_ = "MyApp v1.0";

    protected String appHelp_ = "";

    protected JButton btnAbout_ = createOrangeButton("About", "about the tool", 24,
            24);

    protected JButton btnHelp_ = createOrangeButton("Help", "help", 24, 24);

    protected JButton btnExit_ = createOrangeButton("Exit", "exit", 24, 24);

    protected StatusBar statusBar_ = new StatusBar();

    protected String appVersion_ = "1.0";

    protected JToolBar toolBar_ = new JToolBar();

    private volatile boolean initFlag = false;

    public String getAppHelp() {
        return appHelp_;
    }

    public void setAppHelp(String appHelp) {
        this.appHelp_ = appHelp;
    }

    public String getAppVersion() {
        return appVersion_;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion_ = appVersion;
    }

    public SwingTool() {
        super();
    }

    public SwingTool(String title) {
        super(title);
        this.appTitle_ = title;
    }

    public void setAppTitle(String title) {
        this.appTitle_ = title;
        this.setTitle(title);
    }

    public String getAppTitle() {
        return appTitle_;
    }

    public void init() {
        if (initFlag) {
            return;
        }
        StringBuilder sb = new StringBuilder("<html><body>");
        sb.append("<center><strong><u>&nbsp;" + this.appTitle_
                + "&nbsp;</u></strong></center><br/><br/>");
        sb.append("</body></html>");
        if (StringUtils.isBlank(appHelp_)) {
            setAppHelp(sb.toString());
        }
        SwingUtils.setFont(DEFAULT_FONT);
        this.setBackground(DEFAULT_COLOR);

        // Container container = this.getContentPane();
        // container.setLayout(new BorderLayout(10,10));
        // container.add(this.statusBar,BorderLayout.SOUTH);
        // statusBar.setSize(200, 10);
        // statusBar.setText("wrote by Walter Fan Ya Min");

        initToolbar(this.toolBar_);
        initTopMenu(this.menuBar);

        initComponents();

        setJMenuBar(this.menuBar);
        setToolBar(this.toolBar_);

        initFlag = true;
    }

    public abstract void initComponents();

    // {
    // logger.debug("Please override method initComponents method of " +
    // this.getClass().getSimpleName());
    // }

    public void setToolBar(JToolBar aToolbar) {
        Container container = getContentPane();
        container.add(aToolbar, BorderLayout.NORTH);
    }

    public void initToolbar(JToolBar aToolBar) {
        btnAbout_.addActionListener(ActionHandlerFactory.createAboutHandler(
                this, "About " + appTitle_ + " " + appVersion_,
                " Wrote by walter.fan@gmail.com ", 320, 100));

        btnHelp_.addActionListener(ActionHandlerFactory.createHelpHandler(this,
                getAppTitle(), getAppHelp(), 480, 240));

        btnExit_.addActionListener(ActionHandlerFactory.createExitHandler());

        aToolBar.add(btnAbout_);
        aToolBar.add(btnHelp_);
        aToolBar.add(btnExit_);

        aToolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public JButton createOrangeButton(String strText, String toolTipText, int width,
                                      int height) {

        JButton button = new JButton();
        button.setText(strText);
        button.setToolTipText(toolTipText);
        button.setSize(width, height);
        button.setBackground(Color.ORANGE);
        return button;
    }

    public JButton createButton(String strText, String toolTipText, int width,
                                int height) {

        JButton button = new JButton();
        button.setText(strText);
        button.setToolTipText(toolTipText);
        button.setSize(width, height);
        return button;
    }
  
    public JButton createImageButton(String imageName, String toolTipText,
                                     String altText) {
        // Look for the image.
        String imgLocation = "icons/" + imageName + ".png";
        URL imageURL = Thread.currentThread().getContextClassLoader()
                .getResource(imgLocation); // HttpTool.class.getResource(imgLocation);

        // Create and initialize the button.
        JButton button = new JButton();

        button.setToolTipText(toolTipText);

        if (imageURL != null) { // image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else { // no image found
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }
        button.setSize(24, 24);
        return button;
    }

    public void initTopMenu(JMenuBar aMenuBar) {

        aMenuBar.add(fileMenu);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(ActionHandlerFactory.createExitHandler());
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                InputEvent.ALT_MASK, false));
        fileMenu.add(exitItem);

        aMenuBar.add(helpMenu);
        JMenuItem helpItem = new JMenuItem("Help");
        ActionListener listener = ActionHandlerFactory.createHelpHandler(this,
                getAppTitle(), getAppHelp(), 320, 240);
        helpItem.addActionListener(listener);
        // this.getContentPane().addHyperlinkListener(listener);

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(ActionHandlerFactory.createAboutHandler(
                this, "About " + getAppTitle() + " " + appVersion_,
                " Wrote by walterfan@qq.com ", 320, 240));

        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);

    }

    protected JMenuItem insertBtn2ToolbarAndMenu(String aTitle, JButton btn) {
        btn.setBackground(Color.ORANGE);
        toolBar_.add(btn, 0);
        
        JMenuItem item = new JMenuItem(aTitle);
        ActionListener[] listeners = btn.getActionListeners();
        if(null != listeners) {
            for(ActionListener listener : listeners) {
                item.addActionListener(listener);
            }
        }
        
        fileMenu.insert(item, 0);   
        return item;
    }

    public void printInTextArea(JTextArea jta, String msg) {
        jta.append("["
                + DateFormatUtils.format(System.currentTimeMillis(),
                        "yyyy-MM-dd HH:mm:ss") + "]("
                + Thread.currentThread().getId() + "): " + msg + "\n");
    }
}
