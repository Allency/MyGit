package com.qp;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class HitMouse  extends JFrame implements ActionListener, MouseListener {
	boolean isOver = false;// 设置标记，游戏是否结束
//	private String dir = "./images/";// 图片目录，当前工程下
	JLabel jlbMouse;// 地鼠
	Timer timer;// 时间定时器
	Random random;// 随机数对象，即生成地鼠的位置
	int delay = 1100;// 延迟时间
	Toolkit tk;
	Image image;
	Cursor myCursor;
	JLabel showNum, currentGrade, hitNum;
	int showNumber = 0, hitNumber = 0, currentGrades = 1;

	public HitMouse() {
		super("打地鼠");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(449, 395);
		this.setLocationRelativeTo(null);// 设置窗口在屏幕中心
		setbackground();// 设置背景
		this.getContentPane().setLayout(null);// 设置框架布局模式为空，只有这样，才能知道图片的真正位置
		// 设置鼠标为锤子图片
		tk = Toolkit.getDefaultToolkit();
//		image = tk.createImage(dir + "chui1.png");
		image = tk.createImage(getClass().getResource("chui1.png"));
		myCursor = tk.createCustomCursor(image, new Point(10, 10), "xxx");
		this.setCursor(myCursor);
		

		setMessage();// 设置一些提示信息
		// 在背景图片的基础上设置地鼠图片
//		ImageIcon imageMouse = new ImageIcon(dir + "dishu.png");
		ImageIcon imageMouse = new ImageIcon(getClass().getResource("dishu.png"));
		jlbMouse = new JLabel(imageMouse);
		jlbMouse.setSize(80, 80);
		this.getContentPane().add(jlbMouse);
		jlbMouse.setVisible(false);
		jlbMouse.addMouseListener(this);// 添加鼠标监听
		// 定时器
		timer = new Timer(delay, this);
		random = new Random();
		timer.start();

		addMenu();// 添加菜单

		this.setResizable(false);// 设置窗口大小不能改变
		this.setVisible(true);
	}

	private void addMenu() {
		JMenuBar menubar = new JMenuBar();
		this.setJMenuBar(menubar);
		JMenu game = new JMenu("游戏");
		JMenuItem jitemNew = new JMenuItem("新游戏");
		jitemNew.setActionCommand("new");
		jitemNew.addActionListener(this);
		JMenuItem jitemPause = new JMenuItem("暂停");
		jitemPause.setActionCommand("pause");
		jitemPause.addActionListener(this);
		JMenuItem jitemExit = new JMenuItem("退出");
		jitemExit.setActionCommand("exit");
		jitemExit.addActionListener(this);
		game.add(jitemNew);
		game.add(jitemPause);
		game.addSeparator();// 菜单里设置分隔线
		game.add(jitemExit);
		menubar.add(game);
	}

	private void setbackground() {
		((JPanel) (this.getContentPane())).setOpaque(false);// 如果为
															// true，则该组件绘制其边界内的所有像素。否则该组件可能不绘制部分或所有像素，从而允许其底层像素透视出来。
//		ImageIcon bgImage = new ImageIcon("images/beijing.jpg");
		ImageIcon bgImage = new ImageIcon(getClass().getResource("beijing.jpg"));
		JLabel bgLabel = new JLabel(bgImage);
		bgLabel.setBounds(0, 25, bgImage.getIconWidth(),
				bgImage.getIconHeight());
		this.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));// 设置背景图片的层次最低

	}

	private void setMessage() {
//		ImageIcon showNumb = new ImageIcon(dir + "chuxiancishu.png");
		ImageIcon showNumb = new ImageIcon(getClass().getResource("chuxiancishu.png"));
		JLabel showLabel = new JLabel(showNumb);
		showLabel.setBounds(8, 8, 92, 80);
		this.getContentPane().add(showLabel);
		showNum = new JLabel("0");
		showNum.setBounds(110, 8, 92, 80);
		this.getContentPane().add(showNum);

//		ImageIcon hitNumb = new ImageIcon(dir + "dazhongcishu.png");
		ImageIcon hitNumb = new ImageIcon(getClass().getResource("dazhongcishu.png"));
		JLabel hitLabel = new JLabel(hitNumb);
		hitLabel.setBounds(148, 8, 92, 80);
		this.getContentPane().add(hitLabel);
		hitNum = new JLabel("0");
		hitNum.setBounds(251, 8, 92, 80);
		this.getContentPane().add(hitNum);

//		ImageIcon grade = new ImageIcon(dir + "dangqiandengji.png");
		ImageIcon grade = new ImageIcon(getClass().getResource("dangqiandengji.png"));
		JLabel gradeLabel = new JLabel(grade);
		gradeLabel.setBounds(288, 8, 92, 80);
		this.getContentPane().add(gradeLabel);
		currentGrade = new JLabel("1");
		currentGrade.setBounds(391, 8, 92, 80);
		this.getContentPane().add(currentGrade);
	}

	public static void main(String[] args) {
		new HitMouse();
	}

	public void actionPerformed(ActionEvent e) {
		// 对菜单项注册事件监听
		if (e.getSource() instanceof JMenuItem) {
			menuItemFun(e);
		}

		int ran = random.nextInt(9);// 随机生成一个0~9（不包括9）的随机数
//		ImageIcon imageMouse = new ImageIcon(dir + "dishu.png");// 保证每次随机生成的地鼠图片都是为没被打时的图片
		ImageIcon imageMouse = new ImageIcon(getClass().getResource("dishu.png"));// 保证每次随机生成的地鼠图片都是为没被打时的图片
		jlbMouse.setIcon(imageMouse);
		switch (ran) {
		case 0:
			jlbMouse.setLocation(55, 63);
			break;
		case 1:
			jlbMouse.setLocation(321, 204);
			break;
		case 2:
			jlbMouse.setLocation(184, 204);
			break;
		case 3:
			jlbMouse.setLocation(47, 203);
			break;
		case 4:
			jlbMouse.setLocation(297, 133);
			break;
		case 5:
			jlbMouse.setLocation(161, 133);
			break;
		case 6:
			jlbMouse.setLocation(21, 133);
			break;
		case 7:
			jlbMouse.setLocation(310, 63);
			break;
		case 8:
			jlbMouse.setLocation(185, 63);
			break;
		}

		jlbMouse.setVisible(true);

		showNumber++;
		showNum.setText("" + showNumber);

		if (!gamePlan()) {// 判断游戏是否结束，并显示游戏进程
			timer.stop();
		}

	}

	// 监听菜单功能功能
	private void menuItemFun(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("new")) {// 新游戏
			timer.stop();
			showNumber = 0;
			hitNumber = 0;
			currentGrades = 1;
			delay = 1000;
			isOver = false;
			showNum.setText("" + showNumber);
			hitNum.setText("" + hitNumber);
			currentGrade.setText("" + currentGrades);
			timer = new Timer(delay, this);
			timer.start();
		}
		if (e.getActionCommand().equalsIgnoreCase("exit")) {// 退出
			System.exit(EXIT_ON_CLOSE);
		}

		if (e.getActionCommand().equalsIgnoreCase("pause")) {// 暂停
			timer.stop();
			JOptionPane.showMessageDialog(this, "继续请按“确定”");
			timer.start();
		}
	}

	private boolean gamePlan() {
		if (showNumber - hitNumber > 8) {
			JOptionPane.showMessageDialog(this, "Game Over !");
			isOver = true;
			return false;
		}
		if (hitNumber > 5) {
			hitNumber = 0;
			showNumber = 0;
			currentGrades++;
			if (delay > 100) {
				delay -= 50;
			} else if (delay >= 500) {
				delay = 500;
			}
			timer.setDelay(delay);
			hitNum.setText("" + hitNumber);
			showNum.setText("" + showNumber);
			currentGrade.setText("" + currentGrades);
		}
		return true;
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		if (isOver) {
			return;
		}
//		image = tk.createImage(dir + "chui2.png");
		image = tk.createImage(getClass().getResource("chui2.png"));
		myCursor = tk.createCustomCursor(image, new Point(10, 10), "xxx");
		this.setCursor(myCursor);// 鼠标按下时，鼠标显示打下去的图片，模拟打的动作
		// 如果打中地鼠，则地鼠换成被打中的图片，模拟地鼠被打
		if (e.getSource() == jlbMouse) {
//			ImageIcon imageIconHit = new ImageIcon(dir + "datou.png");
			ImageIcon imageIconHit = new ImageIcon(getClass().getResource("datou.png"));
			jlbMouse.setIcon(imageIconHit);
			jlbMouse.setVisible(true);
		}

		hitNumber++;
		hitNum.setText("" + hitNumber);
	}

	public void mouseReleased(MouseEvent e) {
		if (isOver) {
			return;
		}
		// 当鼠标放松以后，鼠标变回原来没按下时的图片
//		image = tk.createImage(dir + "chui1.png");
		image = tk.createImage(getClass().getResource("chui1.png"));
		myCursor = tk.createCustomCursor(image, new Point(10, 10), "xxx");
		this.setCursor(myCursor);
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
	
}
