package com.leoch.sie.custom.utils;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Color;

public class Demo extends JPanel {
	
	// ��ȡ��Ļ���ڴ�С
		public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
		public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
		public JFrame frame ;
	private static final long serialVersionUID = 1L;
	
	public  void start() {
		
		frame = new JFrame();
		//���ش���װ��
		frame.setUndecorated(true);
		//��ӵ�����
		frame.getContentPane().add(new Demo(), BorderLayout.CENTER);
		// ���ô��ڳ�ʼλ��
		frame.setLocation((WIDTH -300) / 2, (HEIGHT - 240) / 2);
		// ���ô��ڴ�С
		frame.setSize(300, 240);
		//��ʾ����
		frame.setVisible(true);
	}

	public Demo() {
		setBackground(new Color(254, 254, 254));
		setLayout(new FlowLayout(FlowLayout.CENTER));
		System.out.println();
		JLabel label = new JLabel();
//		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setIcon(new ImageIcon(Demo.class.getResource("/resources/loading2.gif")));
		this.add(label);
	}
	
	public void close() {
		frame.dispose();
	}

}