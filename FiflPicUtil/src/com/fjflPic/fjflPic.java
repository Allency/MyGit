package com.fjflPic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import sun.misc.BASE64Decoder;

public class fjflPic extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 749984616293428373L;
	private JLabel jLabel, jLabel1, jLabel2, jLabel3;
	private JTextField jTextField, jTextField1;
	private JButton jButton1, jButton2, jButton3, jButton4;
	@SuppressWarnings("rawtypes")
	private JComboBox jComboBox;
	private JTextArea jtextarea, jTextArea2;
	private JScrollPane sp, sp1, sp2;
	@SuppressWarnings("rawtypes")
	private JList jList;
	private String result = null;
	private String url = null;

	public fjflPic() {
		super();
		this.setSize(700, 600);
		this.getContentPane().setLayout(null);
		this.add(getJLabel1(), null);
		this.add(getJTextField1(), null);
		this.add(getJLabel2(), null);
		this.add(getJComboBox(), null);
		this.add(getJButton3(), null);
		this.add(getJLabel3(), null);
		// this.add(getJTextArea1(), null);
		// this.add(getJButton4(), null);
		this.add(getJList(), null);
		this.add(getJLabel(), null);
		this.add(getJTextField(), null);
		this.add(getJButtoncontent(), null);
		this.add(getJButtonselect(), null);
		this.setBackground(Color.red);
		this.add(getJTextArea(), null);
		this.setTitle("查询电子监控违章图片");
		this.setLocationRelativeTo(null);// 设置窗口在屏幕中心
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);// 设置窗口大小不能改变
	}

	private JScrollPane getJTextArea() {
		if (jtextarea == null) {
			jtextarea = new JTextArea();
			// jtextarea.setBounds(5, 45, 650, 400);
		}
		// jtextarea.setLineWrap(true);
		sp = new JScrollPane(jtextarea);
		sp.setBounds(5, 150, 650, 400);
		// sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return sp;
	}

	@SuppressWarnings("unused")
	private JScrollPane getJTextArea1() {
		if (jTextArea2 == null) {
			jTextArea2 = new JTextArea();
			// jtextarea.setBounds(5, 45, 650, 400);
			jTextArea2.setLineWrap(true);
			jTextArea2.setWrapStyleWord(true);
		}
		// jtextarea.setLineWrap(true);
		sp1 = new JScrollPane(jTextArea2);
		sp1.setBounds(100, 50, 200, 50);
		// sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return sp1;
	}

	@SuppressWarnings("rawtypes")
	private JScrollPane getJList() {
		if (jList == null) {
			jList = new JList();
			// jList.setBounds(100, 50, 200, 50);
		}
		sp2 = new JScrollPane(jList);
		sp2.setBounds(100, 50, 200, 50);
		// sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return sp2;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setBounds(30, 10, 150, 30);
			jLabel1.setText("车牌号");
			jLabel1.setFont(new java.awt.Font("Dialog", 0, 15));
		}
		return jLabel1;
	}

	private JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new javax.swing.JTextField();
			jTextField1.setBounds(100, 10, 150, 30);
		}
		return jTextField1;
	}

	private JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setBounds(280, 10, 80, 30);
			jLabel2.setText("车牌类型");
			jLabel2.setFont(new java.awt.Font("Dialog", 0, 15));
		}
		return jLabel2;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setBounds(360, 10, 80, 30);
			jComboBox.addItem("小型车");
			jComboBox.addItem("大型车");
		}
		return jComboBox;
	}

	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setBounds(530, 10, 138, 30);
			jButton3.setText("监控编号查询");
			jButton3.addActionListener(this);
		}
		return jButton3;
	}

	@SuppressWarnings("unused")
	private JButton getJButton4() {
		if (jButton4 == null) {
			jButton4 = new JButton();
			jButton4.setBounds(300, 60, 200, 30);
			jButton4.setText("选择监控编号，并确定");
			jButton4.addActionListener(this);
		}
		return jButton4;
	}

	private JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setBounds(30, 45, 200, 60);
			jLabel3.setText("查询结果");
			jLabel3.setFont(new java.awt.Font("Dialog", 0, 15));
		}
		return jLabel3;
	}

	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setBounds(30, 105, 150, 30);
			jLabel.setText("监控编号");
			jLabel.setFont(new java.awt.Font("Dialog", 0, 15));
		}
		return jLabel;
	}

	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new javax.swing.JTextField();
			jTextField.setBounds(100, 105, 160, 30);
		}
		return jTextField;
	}

	private JButton getJButtoncontent() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(553, 105, 100, 30);
			jButton1.setText("生成文件");
			jButton1.addActionListener(this);
		}
		return jButton1;
	}

	private JButton getJButtonselect() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(265, 105, 80, 30);
			jButton2.setText("查询");
			jButton2.addActionListener(this);
		}
		return jButton2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jButton3) {
			// 清空历史文本內容
			// jTextArea2.setText("");、
			// ((DefaultListModel)jList.getModel()).removeAllElements();
			methodJb3();
		}

		if (e.getSource() == jButton4) {
			String[] list = jTextArea2.getText().split("\n");
			String count = this.showLocationLineDialogJkbh();
			for (int i = 0; i < list.length; i++) {
				String string = list[Integer.valueOf(count) - 1];
				jTextField.setText(string);
			}
		}

		if (e.getSource() == jButton2) {
			methodJb();
		}

		if (e.getSource() == jButton1) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfc.setCurrentDirectory(new File("."));// 设置当前目录
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.showDialog(new JLabel(), "选择图片保存的位置");
			File file = jfc.getSelectedFile();

			ArrayList<String> listFileName = new ArrayList<String>();
			getAllFileName(file.getAbsolutePath(), listFileName);
			// getAllFileName(file.getName(), listFileName);
			for (String name : listFileName) {
				if (jTextField.getText().equals(name)) {
					int overwriteSelect = JOptionPane.showConfirmDialog(this,
							"文件已存在，是否覆盖?", "是否覆盖?", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (overwriteSelect != JOptionPane.YES_OPTION) {
						return;
					}
				}
				System.out.println(name);
			}
			// if (file.exists()) {
			// // 判断文件是否已存在
			// int overwriteSelect = JOptionPane.showConfirmDialog(this,
			// "<html><font size=3>文件" + file.getName()
			// + "已存在，是否覆盖?</font><html>", "是否覆盖?",
			// JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			// if (overwriteSelect != JOptionPane.YES_OPTION) {
			// return;
			// }
			// }
			if (file.isDirectory()) {
				// 判断文件夹
				System.out.println("文件夹:" + file.getAbsolutePath());
				JOptionPane.showConfirmDialog(null,
						"文件将被保存在" + file.getAbsolutePath() + "目录下", "友情提示",
						JOptionPane.DEFAULT_OPTION);
			} else if (file.isFile()) {
				System.out.println("文件:" + file.getAbsolutePath());
			}
			url = file.getAbsolutePath();
			methodJb1();
		}
	}

	public boolean accept(java.io.File f) {
		if (f.isDirectory())
			return true;
		return f.getName().endsWith(".class"); // 设置为选择以.class为后缀的文件
	}

	public String getDescription() {
		return ".class";
	}

	public void methodJb() {
		String xzqh = null;
		String jkbh = jTextField.getText();
		System.out.println("jkbh==" + jkbh);
		if (jkbh.length() == 0) {
			JOptionPane.showConfirmDialog(null, "监控编号为空,请重新输入！", "友情提示",
					JOptionPane.DEFAULT_OPTION);
		}
		if (jkbh.length() != 16) {
			JOptionPane.showConfirmDialog(null, "监控编号长度不为16位,请重新输入！", "友情提示",
					JOptionPane.DEFAULT_OPTION);
		}
		if (jkbh != null && jkbh.startsWith("32")) {
			xzqh = jkbh.substring(0, 4) + "00";
		}
		System.out.println("xzqh==" + xzqh);
		// 根据XZQH取JKXLH和URL---配置文件
		String url1 = PlatUtil.get6in1Value(xzqh + "_URL1");
		String url2 = PlatUtil.get6in1Value(xzqh + "_URL2");
		String jkxlh = PlatUtil.get6in1Value(xzqh + "_XLH");
		String xtlb = "04";
		String jkid = "04C04";
		String method = "queryObjectOut";
		String QueryXmlDoc = buildXmlString(jkbh);

		// 组装参数调用接口
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("xtlb", xtlb);
		params.put("jkxlh", jkxlh);
		params.put("jkid", jkid);
		params.put("yhbz", "");
		params.put("dwmc", "");
		params.put("dwjgdm", "");
		params.put("yhxm", "");
		params.put("zdbs", "10.32.207.36");
		params.put("QueryXmlDoc", QueryXmlDoc);
		try {
			result = WebServiceUtil.call6In1(url1, method, params);
		} catch (Exception e) {
			System.out.printf("调用[" + url1 + "]失败。", e);
			// 用URL2调用
			try {
				result = WebServiceUtil.call6In1(url2, method, params);
			} catch (Exception e1) {
				System.out.printf("调用[" + url2 + "]失败。", e1);
			}
		}
		jtextarea.setText(result);
		if (result != null) {
			JOptionPane.showConfirmDialog(null, "六合一接口数据查询成功！", "友情提示",
					JOptionPane.DEFAULT_OPTION);
			System.out.println("六合一接口数据查询成功");
		} else {
			JOptionPane.showConfirmDialog(null, "六合一接口数据查询失败，请确认监控编号的正确性！",
					"友情提示", JOptionPane.DEFAULT_OPTION);
		}

	}

	// 拼装XML请求
	private String buildXmlString(String xh) {
		String QueryXmlDoc = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
		QueryXmlDoc += "<root>";
		QueryXmlDoc += "<QueryCondition>";
		QueryXmlDoc += "<xh>" + xh + "</xh>";
		QueryXmlDoc += "</QueryCondition>";
		QueryXmlDoc += "</root>";

		QueryXmlDoc = QueryXmlDoc.replace("<", "&lt;").replace(">", "&gt;");

		return QueryXmlDoc;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void methodJb1() {
		String jkbh = jTextField.getText();
		String path = url + "/" + jkbh + "/";
		Document document = null;
		try {
			document = DocumentHelper.parseText(result);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		Element rootElement = document.getRootElement();
		Element bodyElement = rootElement.element("body");
		List<String> jljh = new ArrayList<String>();
		List<Element> elements = bodyElement.elements();
		if (elements.size() > 0) {
			Element viojdczp = bodyElement.element("viojdczp");
			for (Iterator<Element> it = viojdczp.elementIterator(); it
					.hasNext();) {
				Element e = it.next();
				String name = e.getName();
				if (name.startsWith("zpstr")) {
					// 写图片
					String zpstr = e.getStringValue();
					if (StringUtils.isNotEmpty(zpstr)) {
						jljh.add(zpstr);
					}
				}
			}
		} else {
			JOptionPane.showConfirmDialog(null, "查询无图片！", "友情提示",
					JOptionPane.DEFAULT_OPTION);
		}
		System.out.println("jljh==" + jljh);
		List<String> pic = new ArrayList<String>();
		for (int i = 0; i < jljh.size(); i++) {
			writePics(jljh.get(i), jkbh, "picture" + i);
			pic.add(path + "picture" + i + ".jpg");
		}
		if (pic != null) {
			JOptionPane.showConfirmDialog(null, "图片生成成功！", "友情提示",
					JOptionPane.DEFAULT_OPTION);
		} else {
			JOptionPane.showConfirmDialog(null, "图片生成失败！", "友情提示",
					JOptionPane.DEFAULT_OPTION);
		}

		// 清空文本內容
		// SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		// jtextarea.setText("");
		// }
		// });
	}

	private void writePics(String srcStr, String jkbh, String picName) {
		String path = url + "/" + jkbh;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}

		File picFile = new File(path, picName + ".jpg");

		try {
			FileOutputStream fos = new FileOutputStream(picFile);
			fos.write(new BASE64Decoder().decodeBuffer(srcStr));
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void methodJb3() {
		String hphm = jTextField1.getText();
		String hpzl = jComboBox.getSelectedItem().toString();
		System.out.println("号牌号码:" + hphm + "号牌种类:" + hpzl);
		if ("小型车".equals(hpzl)) {
			hpzl = "02";
		} else if ("大型车".equals(hpzl)) {
			hpzl = "01";
		}
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
			System.out.println("开始尝试连接数据库！");
			String url = OracleJdbcUtil.getJdbcValue("URL");
			String user = OracleJdbcUtil.getJdbcValue("USER");// 用户名,系统默认的账户名
			String password = OracleJdbcUtil.getJdbcValue("PASSWORD");// 你安装时选设置的密码
			Connection con = DriverManager.getConnection(url, user, password);// 获取连接
			System.out.println("连接成功！");
			String sql = "select xh as jkbh from vio_surveil where hphm=? and hpzl=?";// 预编译语句，“？”代表参数
			// 创建预编译语句对象，一般都是用这个而不用Statement
			PreparedStatement pstm = null;
			ResultSet result = null;// 创建一个结果集对象
			// 计算数据库student表中数据总数
			pstm = con.prepareStatement(sql);
			// 执行插入数据操作
			pstm.setString(1, hphm);
			pstm.setString(2, hpzl);
			result = pstm.executeQuery();
			Vector<String> vector = new  Vector<String>();
			while (result.next()) {
				// 当结果集不为空时
				// jTextArea2.append(result.getString("jkbh") + "\r\n");
				vector.add(result.getString("jkbh"));
			}
			jList.setListData(vector);
			result.close();
			pstm.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showConfirmDialog(null, "无法找到指定类异常！", "友情提示",
					JOptionPane.DEFAULT_OPTION);
		}

		// 自动选择监控编号
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
				   jTextField.setText((String)theList.getSelectedValue());
				   System.out.println(theList.getSelectedValue());
				}
			}
		};
		jList.addMouseListener(mouseListener);
	}

	public static void getAllFileName(String path, ArrayList<String> fileName) {
		File file = new File(path);
		File[] files = file.listFiles();
		String[] names = file.list();
		if (names != null)
			fileName.addAll(Arrays.asList(names));
		for (File a : files) {
			if (a.isDirectory()) {
				getAllFileName(a.getAbsolutePath(), fileName);
			}
		}
	}

	/**
	 * 获取定位的监控编号
	 */
	private String showLocationLineDialogJkbh() {

		// 取得总行数
		int totalLineCount = jTextArea2.getLineCount();
		System.out.println("总行数===" + totalLineCount);
		if (totalLineCount <= 1) {
			return String.valueOf(totalLineCount);
		}
		String title = "跳转至行：(1..." + totalLineCount + ")";
		String line = JOptionPane.showInputDialog(this, title);
		if (line == null || "".equals(line.trim())) {
			return null;
		}
		try {
			int intLine = Integer.parseInt(line);
			if (intLine > totalLineCount) {
				return String.valueOf(intLine);
			}
			// JTextArea起始行号是0，所以此处做减一处理
			int selectionStart = jTextArea2.getLineStartOffset(intLine - 1);
			int selectionEnd = jTextArea2.getLineEndOffset(intLine - 1);

			// 如果是不是最后一行，selectionEnd做减一处理，是为了使光标与选中行在同一行
			if (intLine != totalLineCount) {
				selectionEnd--;
			}
			jTextArea2.requestFocus(); // 获得焦点
			jTextArea2.setSelectionStart(selectionStart);
			jTextArea2.setSelectionEnd(selectionEnd);
			return String.valueOf(intLine);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		fjflPic w = new fjflPic();
		w.setVisible(true);
	}
}