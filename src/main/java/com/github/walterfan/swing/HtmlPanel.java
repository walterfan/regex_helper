package com.github.walterfan.swing;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class HtmlPanel extends JPanel {
	private class LinkListener implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent evt) {
			if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				new LoaderThread(evt.getURL());
			}
		}
	}

	private class LoaderThread extends Thread {
		private URL urlToLoad;

		LoaderThread(URL url) { // constructor starts the thread.
			urlToLoad = url;
			start();
		}

		public void run() {
			InputStream in = null;
			try {
				editPane.setContentType("text/plain");
				editPane.setText("Loading URL " + urlToLoad + "...");

				/*
				 * Open a URL connection just for the purpose of reading the
				 * content type. I only want to show the document if the content
				 * type is supported by JEditorPane.
				 */

				URLConnection connection = urlToLoad.openConnection();
				in = connection.getInputStream();
				String contentType = connection.getContentType();
				if (contentType == null)
					throw new Exception("Can't determine content type of url.");
				if (!(contentType.startsWith("text/plain")
						|| contentType.startsWith("text/html") || contentType
						.startsWith("text/rtf")))
					throw new Exception("Can't display content type "
							+ contentType);
				editPane.setText("Retrieving document contents...");

				in.close(); // I don't want to actual read from the connection!
				in = null;
				editPane.setPage(urlToLoad);
			} catch (Exception e) {
				editPane.setContentType("text/html");
				editPane.setText("<html><body><p>Sorry, the requested document was not found\n"
								+ "or cannot be displayed.\n\nError:" + e
								+ "</p></html></body>");
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
					}
				}
			}
		}
	}

	private JEditorPane editPane;

	public HtmlPanel() {
		setBackground(Color.BLACK);
		setLayout(new BorderLayout(1, 1));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		editPane = new JEditorPane();
		editPane.setEditable(false);
		editPane.addHyperlinkListener(new LinkListener());
		add(new JScrollPane(editPane), BorderLayout.CENTER);

	}

	public void load(String location) {
		URL url;
		try {
			if (location.length() == 0)
				throw new Exception();
			if (!location.contains("://"))
				location = "http://" + location;
			url = new URL(location);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(HtmlPanel.this,
					"The Location input box does not\nccontain a legal URL.");
			return;
		}
		new LoaderThread(url);
	}

	public void load(URL url) {
		new LoaderThread(url);
	}
	
	public void load(String contentType, String content) {
		editPane.setContentType(contentType);
		editPane.setText(content);
	}
	
	public static void main(String[] args) {
		JFrame window = new JFrame("HtmlPanel");
		HtmlPanel content = new HtmlPanel();
		window.setContentPane(content);
		window.setSize(600, 500);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation((screenSize.width - window.getWidth()) / 2,
				(screenSize.height - window.getHeight()) / 2);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		content.load("file:///D:/opt/workspace/jwhat/site/mockup/Studio/index.html");
	}
}
