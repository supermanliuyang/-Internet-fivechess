package ChessGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import util.CloseUtil;
import util.GameUtil;

public class ChessGame1 extends JFrame implements MouseListener {
	private int blackcount = 0;//记录赢的局数
	private int whitecount = 0;
	private boolean flag = true;
	private boolean whitewin;
	public BufferedReader console;
	public static DataOutputStream dos;//接收输出
	public boolean isRunning = true;
	private boolean flash = false;
	public boolean tp = true;
	public static DataInputStream dis;//接收输入
	public int ax = 0;
	public int ay = 0;
	public Timer time;
	private ArrayList<mypoint> blacklist;
	private ArrayList<mypoint> whitelist;

	public ChessGame1() throws UnknownHostException, IOException {
		setTitle("五子棋");
		setSize(700, 750);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		blacklist = new ArrayList<mypoint>();
		whitelist = new ArrayList<mypoint>();
		this.addMouseListener(this);// 增加鼠标监听器
		new Thread(new Runnable() {// 接收坐标线程
			@Override
			public void run() {
				while (isRunning) {
					tp=true;
					try {
						ax = dis.readInt();
						ay = dis.readInt();
						if (ax != 0 && flag == false) {
							tp=!tp;
							whitelist.add(new mypoint(ax, ay));
							repaint();
						} else if (ax != 0 && flag == true) {
							tp=!tp;
							blacklist.add(new mypoint(ax, ay));
							repaint();
						}
					} catch (IOException e2) {
						// TODO 自动生成的 catch 块
						// e.printStackTrace();
						isRunning = false;
						CloseUtil.closeAll(dis);
					}

				}
			}
		}).start();
		;
	}

	@Override

	public void paint(Graphics g) {// 绘图

		flag = !flag;
		if (flash == true) {
			super.paint(g);
		}
		g.setColor(new Color(180, 150, 100));
		g.fillRect(0, 0, 700, 750);
		g.setColor(Color.black);
		flash = false;
		for (int i = 2; i <= 30; i++) {
			g.drawLine(40, 20 * i, 600, 20 * i);// 横线
			g.drawLine(20 * i, 40, 20 * i, 600);// 竖线
		}

		Image img = GameUtil.getImage("images/2.1.png");
		for (int i = 0; i < blacklist.size(); i++) {// 绘画黑棋

			g.drawImage(img, (int) blacklist.get(i).getX() * 20 + 12, (int) blacklist.get(i).getY() * 20 + 32, null);
		}

		Image img1 = GameUtil.getImage("images/1.1.png");
		for (int i = 0; i < whitelist.size(); i++) {// 绘画白棋

			g.drawImage(img1, (int) whitelist.get(i).getX() * 20 + 12, (int) whitelist.get(i).getY() * 20 + 32, null);
		}
		this.checkwin(blacklist, 0, 0, 0, 0);// 判断输赢
		this.checkwin(whitelist, 0, 0, 0, 1);
		g.setColor(Color.black);
		String str1 = "黑方胜利局数为:" + blackcount;
		g.drawString(str1, 10, 700);
		String str2 = "白方胜利局数为:" + whitecount;
		g.drawString(str2, 150, 700);

	}

	public static void main(String[] args) throws UnknownHostException, IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
//        String str = null; 
//        System.out.println("请输入IP地址:"); 
		@SuppressWarnings("resource")
		Socket client = new Socket("192.168.191.1", 7777);
		dis = new DataInputStream(client.getInputStream());
		dos = new DataOutputStream(client.getOutputStream());
		
		new ChessGame1().setVisible(true);

	}

	public boolean checkExist(mypoint a) {
		int ax = 0;
		int ay = 0;
		for (int i = 0; i < whitelist.size(); i++) {
			if (whitelist.get(i).getX() == a.getX() && whitelist.get(i).getY() == a.getY()) {
				return true;
			}
		}
		for (int i = 0; i < blacklist.size(); i++) {
			if (blacklist.get(i).getX() == a.getX() && blacklist.get(i).getY() == a.getY()) {
				return true;
			}
		}
		return false;
	}
	public void mouseClicked(MouseEvent e) {
		if(tp==true){
			
			double x = e.getX();
		double y = e.getY();
		if (x >= 40 && x <= 600 && y >= 40 && y <= 600) {
			double k = 20;
			x = x / k - 1;
			y = y / k - 2;
			ax = (int) Math.round(x);
			ay = (int) Math.round(y);
			if (checkExist(new mypoint(ax, ay))) {// 判断棋子是否存在
				JOptionPane.showMessageDialog(null, "该位置已有棋子！");
				repaint();
			
		}
		
			} 
				if (flag == true) {
					// 发送黑棋坐标线程

					try {
						if (ax != 0) {
							tp=!tp;
							dos.writeInt(ax);
							dos.flush();
							dos.writeInt(ay);
							dos.flush();// 强制刷新
						}
					} catch (IOException e2) {
						// TODO 自动生成的 catch 块
						// e2.printStackTrace();
						isRunning = false;
						CloseUtil.closeAll(dos, console);
					}

					blacklist.add(new mypoint(ax, ay));// 把坐标加入到数组中去
				}
				if (flag == false) {
					// 发送白棋坐标线程
					try {
						if (ax != 0) {
							tp=!tp;
							dos.writeInt(ax);
							dos.flush();
							dos.writeInt(ay);
							dos.flush();// 强制刷新
						}
					} catch (IOException e2) {
						// TODO 自动生成的 catch 块
						// e2.printStackTrace();
						isRunning = false;
						CloseUtil.closeAll(dos, console);
					}

					whitelist.add(new mypoint(ax, ay));
				}

				repaint();

			
		}
	}

	public void checkwin(ArrayList<mypoint> list, int pos, int direct, int count, int type) {
		if (count == 0) {
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.size(); j++) {

					if (list.get(i).getY() == list.get(j).getY() && (list.get(j).getX() - list.get(i).getX() == -1)) {
						direct = 2;
						count++;
						checkwin(list, j, 2, count, type);
						count = 0;
					}

					if (list.get(i).getX() == list.get(j).getX() && (list.get(j).getY() - list.get(i).getY() == -1)) {
						direct = 0;
						count++;
						checkwin(list, j, 0, count, type);
						count = 0;
					}
					if (list.get(j).getY() == list.get(i).getY() - 1
							&& (list.get(j).getX() - list.get(i).getX() == 1)) {
						direct = 4;
						count++;
						checkwin(list, j, 4, count, type);
						count = 0;
					}
					if (list.get(j).getY() == list.get(i).getY() + 1
							&& (list.get(j).getX() - list.get(i).getX() == 1)) {
						direct = 5;
						count++;

						checkwin(list, j, 5, count, type);
						count = 0;
					}

				}
			}

		} else {
			if (direct == 4) {
				for (int i = 0; i < list.size(); i++) {
					if (i != pos) {
						if (list.get(i).getX() == list.get(pos).getX() + 1
								&& (list.get(i).getY() - list.get(pos).getY() == -1)) {

							count = count + 1;
							if (count == 4) {
								if (type == 0) {
									System.out.println("blackwin!");
									blackcount++;
									JOptionPane.showMessageDialog(null, "黑方赢了！");
									restart();
								}
								if (type == 1) {
									System.out.println("whitewin!");
									whitecount++;
									JOptionPane.showMessageDialog(null, "白方赢了！");
									restart();
								}
							} else {
								checkwin(list, i, direct, count, type);
							}
						}
					}
				}
			}
			if (direct == 5) {
				for (int i = 0; i < list.size(); i++) {
					if (i != pos) {
						if (list.get(i).getX() == list.get(pos).getX() + 1
								&& (list.get(i).getY() - list.get(pos).getY() == 1)) {

							count = count + 1;
							if (count == 4) {
								if (type == 0) {
									System.out.println("blackwin!");
									blackcount++;
									JOptionPane.showMessageDialog(null, "黑方赢了！");
									restart();
								}
								if (type == 1) {
									System.out.println("whitewin!");
									whitecount++;
									JOptionPane.showMessageDialog(null, "白方赢了！");
									restart();
								}
							} else {
								checkwin(list, i, direct, count, type);
							}
						}
					}
				}
			}

			if (direct == 0) {
				for (int i = 0; i < list.size(); i++) {
					if (i != pos) {
						if (list.get(pos).getX() == list.get(i).getX()
								&& (list.get(i).getY() - list.get(pos).getY() == -1)) {

							count = count + 1;
							if (count == 4) {
								if (type == 0) {
									System.out.println("blackwin!");
									blackcount++;
									JOptionPane.showMessageDialog(null, "黑方赢了！");
									restart();
								}
								if (type == 1) {
									System.out.println("whitewin!");
									whitecount++;
									JOptionPane.showMessageDialog(null, "白方赢了！");
									restart();
								}
							} else {
								checkwin(list, i, direct, count, type);
							}
						}
					}
				}
			}

			if (direct == 2) {
				for (int i = 0; i < list.size(); i++) {
					if (i != pos) {
						if (list.get(pos).getY() == list.get(i).getY()
								&& (list.get(i).getX() - list.get(pos).getX() == -1)) {

							count = count + 1;
							if (count == 4) {
								if (type == 0) {
									System.out.println("blackwin!");
									blackcount++;
									JOptionPane.showMessageDialog(null, "黑方赢了！");
									restart();
								}
								if (type == 1) {
									System.out.println("whitewin!");
									whitecount++;
									JOptionPane.showMessageDialog(null, "白方赢了！");
									restart();
								}
							} else {
								checkwin(list, i, direct, count, type);
							}
						}
					}
				}
			}
		}

	}

	public void restart() {
		whitelist = new ArrayList<mypoint>();
		blacklist = new ArrayList<mypoint>();
		flash = true;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

class mypoint {
	int x;
	int y;

	public mypoint(int a, int b) {
		this.x = a;
		this.y = b;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
