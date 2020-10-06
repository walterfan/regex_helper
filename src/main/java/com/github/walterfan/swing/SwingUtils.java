package com.github.walterfan.swing;


import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A few utilities that simplify using windows in Swing. 1998-99 Marty Hall,
 * http://www.apl.jhu.edu/~hall/java/
 */

public class SwingUtils {
    public static final Color DEFAULT_COLOR = new Color(0x99, 0xFF, 0xCC);
    public static final Font DEFAULT_FONT = new Font(Font.MONOSPACED,
            Font.PLAIN, 14);
    /**
     * Tell system to use native look and feel, as in previous releases. Metal
     * (Java) LAF is the default otherwise.
     */

    public static void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }
    }

    public static void setJavaLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager
                    .getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting Java LAF: " + e);
        }
    }

    public static void setMotifLookAndFeel() {
        try {
            UIManager
                    .setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception e) {
            System.out.println("Error setting Motif LAF: " + e);
        }
    }

    /**
     * A simplified way to see a JPanel or other Container. Pops up a JFrame
     * with specified Container as the content pane.
     */

    public static JFrame openInJFrame(Container content, int width, int height,
                                      String title, Color bgColor) {
        JFrame frame = new JFrame(title);
        frame.setBackground(bgColor);
        content.setBackground(bgColor);
        frame.setSize(width, height);
        frame.setContentPane(content);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        return (frame);
    }

    /** Uses Color.white as the background color. */

    public static JFrame openInJFrame(Container content, int width, int height,
                                      String title) {
        return (openInJFrame(content, width, height, title, Color.white));
    }

    /**
     * Uses Color.white as the background color, and the name of the Container's
     * class as the JFrame title.
     */

    public static JFrame openInJFrame(Container content, int width, int height) {
        return (openInJFrame(content, width, height, content.getClass()
                .getName(), Color.white));
    }

    public static void moveToCenter(JFrame frame) {
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2
                - windowHeight / 2);
    }

    public static void run(final JFrame f) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setVisible(true);
                f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);

            }
        });
    }

    public static void run(final JFrame f, final int width, final int height) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(width, height);
                f.setVisible(true);
                moveToCenter(f);
            }
        });
    }

    public static void setFont(Font f) {
        // Font f = new Font("Tahoma",Font.PLAIN,11);
        UIManager.put("TextField.font", f);
        UIManager.put("TextArea.font", f);
        UIManager.put("Label.font", f);
        UIManager.put("ComboBox.font", f);
        UIManager.put("MenuBar.font", f);
        UIManager.put("Menu.font", f);
        UIManager.put("ToolTip.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("List.font", f);
        UIManager.put("Button.font", f);
        UIManager.put("Table.font", f);
    }

    public static void alert(String message) {
        JOptionPane.showMessageDialog(null, message, "Alert",
                JOptionPane.WARNING_MESSAGE);
    }

    public static String ask(String title, String message) {
        return JOptionPane.showInputDialog(null,
                StringUtils.substring(message, 0, 1024), title,
                JOptionPane.QUESTION_MESSAGE);
    }

    public static int yesOrNo(String title, String message) {
        return JOptionPane.showConfirmDialog(null,
                StringUtils.substring(message, 0, 1024), title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    
    public static void prompt(String title, String message) {
        JOptionPane.showMessageDialog(null,
                StringUtils.substring(message, 0, 1024), title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static JPanel createHorizontalPanel(Component left,
                                               Component center, Component right) {

        JPanel pane = new JPanel(new BorderLayout());
        if (left != null)
            pane.add(left, BorderLayout.WEST);
        if (center != null)
            pane.add(center, BorderLayout.CENTER);
        if (right != null)
            pane.add(right, BorderLayout.EAST);

        return pane;
    }

    public static JPanel createHorizontalPanel(Component[] cms) {
        return createHorizontalPanel(cms, 0);
    }

    public static JPanel createHorizontalPanel(Component[] cms, int hgap) {
        JPanel pane = new JPanel(new GridLayout(1, cms.length, hgap, 0));
        for (Component c : cms) {
            pane.add(c);
        }
        return pane;
    }

    public static JPanel createVerticalPanel(Component[] cms) {
        return createVerticalPanel(cms, 0);
    }

    public static JPanel createVerticalPanel(Component[] cms, int vgap) {

        JPanel pane = new JPanel(new GridLayout(cms.length, 1, 0, vgap));
        for (Component c : cms) {
            pane.add(c);
        }
        return pane;
    }

    public static JPanel createVerticalPanel(Component topCmp,
                                             Component centerCmp) {
        JPanel outPane = new JPanel();
        outPane.setLayout(new BorderLayout());
        outPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        outPane.add(topCmp, BorderLayout.NORTH);
        outPane.add(centerCmp, BorderLayout.CENTER);

        return outPane;
    }

    public static JSplitPane createHSplitPane(Component left, Component right,
                                              int dividNum) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                left, right);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(dividNum);
        return splitPane;
    }

    public static JSplitPane createVSplitPane(Component left, Component right,
                                              int dividNum) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, left,
                right);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(dividNum);
        return splitPane;
    }

    public static JSplitPane createVSplitPane(Component left, Component right,
                                              double dividNum) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, left,
                right);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(dividNum);
        return splitPane;
    }

    public static void createAndShowGUI(final String titile, final JPanel pane) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(titile);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                pane.setOpaque(true);
                frame.setContentPane(pane);
                frame.pack();
                // frame.setSize(width, height);
                frame.setVisible(true);
                moveToCenter(frame);
            }
        });

    }

    public void printJTable(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    public static JPopupMenu createStdEditPopupMenu(
            final JTextComponent[] fields) {
        final JPopupMenu popupMenu = new JPopupMenu();

        /* text fields popup menu: "Cut" */
        final JMenuItem cutMenuItem = new JMenuItem("Cut", 't');
        cutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Component c = popupMenu.getInvoker();

                if (c instanceof JTextComponent) {
                    ((JTextComponent) c).cut();
                }
            }
        });
        popupMenu.add(cutMenuItem);

        /* text fields popup menu: "Copy" */
        final JMenuItem copyMenuItem = new JMenuItem("Copy", 'C');
        copyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Component c = popupMenu.getInvoker();

                if (c instanceof JTextComponent) {
                    ((JTextComponent) c).copy();
                }
            }
        });
        popupMenu.add(copyMenuItem);

        /* text fields popup menu: "Paste" */
        final JMenuItem pasteMenuItem = new JMenuItem("Paste", 'P');
        pasteMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Component c = popupMenu.getInvoker();

                if (c instanceof JTextComponent) {
                    ((JTextComponent) c).paste();
                }
            }
        });
        popupMenu.add(pasteMenuItem);
        popupMenu.addSeparator();

        /* text fields popup menu: "Select All" */
        final JMenuItem selectAllMenuItem = new JMenuItem("Select All", 'A');
        selectAllMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Component c = popupMenu.getInvoker();

                if (c instanceof JTextComponent) {
                    ((JTextComponent) c).selectAll();
                }
            }
        });
        popupMenu.add(selectAllMenuItem);

        /* add mouse listeners to the specified fields */
        for (final JTextComponent f : fields) {
            f.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    processMouseEvent(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    processMouseEvent(e);
                }

                private void processMouseEvent(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        popupMenu.setInvoker(f);
                    }
                }
            });
        }
        return popupMenu;
    }

    public static JScrollPane createScrollPane(JTextArea textArea, Font font,
                                               Color bgColor) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        if (null != font) {
            textArea.setFont(font);
        }
        textArea.setOpaque(true);
        if (bgColor != null) {
            textArea.setBackground(bgColor);
        }

        return scrollPane;
    }

    public static JPanel createHTextComponentPane(String labelText,
                                                  JTextComponent textComp) {
        return createTextComponentPane(labelText, textComp, DEFAULT_FONT, DEFAULT_COLOR,
                false);
    }

    public static JPanel createVTextComponentPane(String labelText,
                                                  JTextComponent textComp) {
        return createTextComponentPane(labelText, textComp, DEFAULT_FONT, DEFAULT_COLOR, true);
    }

    public static JPanel createTextComponentPane(String labelText,
                                                 JTextComponent textComp, Font font, Color bgColor,
                                                 boolean isVertical) {
        JPanel outPane = new JPanel();
        outPane.setLayout(new BorderLayout());
        outPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel labelOut = new JLabel(labelText, SwingConstants.LEFT);
        if (isVertical) {
            outPane.add(labelOut, BorderLayout.NORTH);
        } else {
            outPane.add(labelOut, BorderLayout.WEST);
        }
        if (textComp instanceof JTextArea) {
            outPane.add(createScrollPane((JTextArea) textComp, font, bgColor),
                    BorderLayout.CENTER);
        } else {
            textComp.setBackground(bgColor);
            textComp.setFont(font);
            outPane.add(textComp, BorderLayout.CENTER);
        }
        return outPane;
    }

    public static void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof Highlighter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
}
