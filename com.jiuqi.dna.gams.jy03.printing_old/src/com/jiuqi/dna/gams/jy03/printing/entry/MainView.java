package com.jiuqi.dna.gams.jy03.printing.entry;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class MainView extends JFrame {
	private static final long serialVersionUID = 4859858380825456963L;
	private JPanel contentPane;
	private final String URL = "http://localhost:8081/printing";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		UIUtils.setPreferredLookAndFeel();
		NativeInterface.open();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setExtendedState(Frame.MAXIMIZED_BOTH);
					frame.setAlwaysOnTop(true);
					frame.setUndecorated(true);// true可去掉窗口的表体栏
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		NativeInterface.runEventPump();
	}

	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		setBounds(0, 0, width, height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JWebBrowser jWebBrowser = new JWebBrowser();
		jWebBrowser.setBounds(0, 0, width, height);
		jWebBrowser.navigate(URL);
		jWebBrowser.setButtonBarVisible(false);
		jWebBrowser.setMenuBarVisible(false);
		jWebBrowser.setBarsVisible(false);
		jWebBrowser.setStatusBarVisible(false);
		contentPane.add(jWebBrowser);
	}

}