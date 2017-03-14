package cn.mxsic.netchess.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int width;
	boolean focus=false;
	int jiang1_i=4; 
	int jiang1_j=0;
	int jiang2_i=4;
	int jiang2_j=9;
	int startI=-1;int startJ=-1;
	int endI=-1;int endJ=-1;
	public Chess qizi[][];
	App xq=null;
	Rule guize;
	public Board(Chess qizi[][] , int width, App xq){
		this.xq=xq;this.qizi=qizi;this.width=width;
		guize=new Rule(qizi);
		this.addMouseListener(this);
		this.setBounds(0,0,700,700);
		this.setLayout(null);
	}
	public void paint(Graphics g1){
		Graphics2D g=(Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color c=g.getColor();
		g.setColor(App.bgcolor);
		g.fill3DRect(60, 30, 580, 630, false);
		g.setColor(Color.black);
		for (int i = 80; i <=620; i=i+60) {
			g.drawLine(110, i, 590, i);
		}
		g.drawLine(110, 80, 110, 620);
		g.drawLine(590, 80, 590, 620);
		for (int i =170; i <=530; i=i+60) {
			g.drawLine(i, 80, i, 320);
			g.drawLine(i, 380, i, 620);
		}
		g.drawLine(290, 80, 410, 200);
		g.drawLine(290, 200, 410, 80);
		g.drawLine(290, 620, 410, 500);
		g.drawLine(290, 500, 410, 620);

		this.smallLine(g,7,7);
		this.smallLine(g,1,7);
		this.smallLine(g,1,2);
		this.smallLine(g,7,2);
		
		g.setColor(Color.black);
		Font font1=new Font("宋体",Font.BOLD,50);
		g.setFont(font1);
		g.drawString("楚  河", 150, 365);
		g.drawString("汉  界", 390, 365);
		Font font=new Font("宋体",Font.BOLD,30);
		g.setFont(font);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				if(qizi[i][j]!=null){
					if(this.qizi[i][j].getFocus()!=false){
						
						g.setColor(App.focusbg);
						g.fillOval((110+i*60)-25, (80+j*60)-25, 50, 50);
						g.setColor(App.focuschar);
					}else{
						g.fillOval(110+i*60-25,80+j*60-25, 50, 50);
						g.setColor(qizi[i][j].getColor());
					}
					g.drawString(qizi[i][j].getName(), 110+i*60-18, 80+j*60+10);
					g.setColor(Color.black);
				}
			}
		}
		g.setColor(c);
	}
	
	private void smallLine(Graphics2D g, int i, int j) {
		int x=110+60*i; int y=80+60*j;
		if(i>0){g.drawLine(x-3, y-3, x-20, y-3);g.drawLine(x-3, y-3, x-3, y-20);}
		if(i<8){g.drawLine(x+3, y-3, x+20, y-3);g.drawLine(x+3, y-3, x+3, y-20);}
		if(i>0){g.drawLine(x-3, y+3, x-20, y+3);g.drawLine(x-3, y+3, x-3, y+20);}
		if(i<8){g.drawLine(x+3, y+3, x+20, y+3);g.drawLine(x+3, y+3, x+3, y+20);}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.xq.caiPan==true){
			int i=-1,j=-1;
			int[] pos=GetPos(e);
			i=pos[0];j=pos[1];
			if(i>=0&&i<=8&&j>=0&&j<=9){
				if(focus==false){
					this.noFocus(i, j);
					
				}else{
					if(qizi[i][j]!=null){
						if(qizi[i][j].getColor()==qizi[startI][startJ].getColor()){
							qizi[startI][startJ].setFocus(false);
							qizi[i][j].setFocus(true);
							startI=i;startJ=j;
						}else{
							endI=i;endJ=j;
							String name=qizi[startI][startJ].getName();
							boolean canMove=guize.canMove(startI, startJ, endI, endJ, name);
							if(canMove){
								try{
									this.xq.cat.dout.writeUTF("<#MOVE#>"+this.xq.cat.tiaoZhanZhe+startI+startJ+endI+endJ);
									this.xq.caiPan=false;
									showShuYu(name,startI, startJ, endI, endJ);
								if(qizi[endI][endJ].getName().equals("帅")||qizi[endI][endJ].getName().equals("将")){
									this.success();
								}else{
									this.noJiang();
								}
								}catch(Exception ea){ea.printStackTrace();}
							}
						}
					}else{
						endI=i;endJ=j;
						String name=qizi[startI][startJ].getName();
						boolean canMove=guize.canMove(startI, startJ, endI, endJ, name);
						if(canMove){
							showShuYu(name,startI, startJ, endI, endJ);
							this.noQiZi();
						}
					}
				}
				
			}
			
			this.xq.repaint();
		}
	}

	private void noJiang() {
		qizi[endI][endJ]=qizi[startI][startJ];
		qizi[startI][startJ]=null;
		qizi[endI][endJ].setFocus(false);
		this.xq.repaint();
		if(qizi[endI][endJ].getName().equals("帅")){
			jiang1_i=endI;jiang1_j=endJ;
		}else if(qizi[endI][endJ].getName().equals("将")){
			jiang2_i=endI;jiang2_j=endJ;
		}
		if(jiang1_i==jiang2_i){
			int count=0;
			for (int jiang_j=jiang1_j+1; jiang_j< jiang2_j; jiang_j++) {
				if(qizi[jiang1_i][jiang_j]!=null){
					count++;break;
				}
			}
			if(count==0){
				JOptionPane.showMessageDialog(this.xq, "照将!!!你失败了!!!","提示",JOptionPane.INFORMATION_MESSAGE);
					this.xq.cat.tiaoZhanZhe=null;
					this.xq.color=0;
					this.xq.caiPan=false;
					this.xq.next();
					jiang1_i=4;jiang1_j=0;
					jiang2_i=4;jiang2_j=9;
					
			}
		}
		startI=-1;startJ=-1;
		endI=-1;endJ=-1;
		focus=false;
	}
	private void success() {
		qizi[endI][endJ]=qizi[startI][startJ];
		qizi[startI][startJ]=null;
		this.xq.repaint();
		JOptionPane.showMessageDialog(this.xq, "恭喜您，您获胜了","提示",JOptionPane.INFORMATION_MESSAGE);

		this.xq.cat.tiaoZhanZhe=null;
		this.xq.color=0;
		this.xq.caiPan=false;
		this.xq.next();
		startI=-1;startJ=-1;
		endI=-1;endJ=-1;
		jiang1_i=4;jiang1_j=0;
		jiang2_i=4;jiang2_j=9;
		focus=false;
	}
	private void noQiZi() {
		try{
			this.xq.cat.dout.writeUTF("<#MOVE#>"+this.xq.cat.tiaoZhanZhe+startI+startJ+endI+endJ);
			this.xq.caiPan=false;
			qizi[endI][endJ]=qizi[startI][startJ];
			qizi[startI][startJ]=null;
			qizi[endI][endJ].setFocus(false);
			this.xq.repaint();
			if(qizi[endI][endJ].getName().equals("帅")){
				jiang1_i=endI;jiang1_j=endJ;
			}else if(qizi[endI][endJ].getName().equals("将")){
				jiang2_i=endI;jiang2_j=endJ;
			}
			if(jiang1_i==jiang2_i){
				int count=0;
				for (int jiang_j=jiang1_j+1; jiang_j< jiang2_j; jiang_j++) {
					if(qizi[jiang1_i][jiang_j]!=null){
						count++;break;
					}
				}if(count==0){
					JOptionPane.showMessageDialog(this.xq, "照将!!!你失败了!!!",
							"提示",JOptionPane.INFORMATION_MESSAGE);
						this.xq.cat.tiaoZhanZhe=null;
						this.xq.color=0;
						this.xq.caiPan=false;
						this.xq.next();
						jiang1_i=4;jiang1_j=0;
						jiang2_i=4;jiang2_j=9;
						
				}
			}
			startI=-1;startJ=-1;
			endI=-1;endJ=-1;
			focus=false;
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	private void noFocus(int i, int j) {
		if(this.qizi[i][j]!=null){
			if(this.xq.color==0){
				if(this.qizi[i][j].getColor().equals(this.xq.color1)){
					this.qizi[i][j].setFocus(true);
					focus=true;
					startI=i;startJ=j;
				}
			}else{
				if(this.qizi[i][j].getColor().equals(this.xq.color2)){
					this.qizi[i][j].setFocus(true);
					focus=true;
					startI=i;startJ=j;
				}
			}
		}
	}
	private int[] GetPos(MouseEvent e) {
		int[] pos=new int[2];
		pos[0]=-1;pos[1]=-1;
		Point p=e.getPoint();
		double x=p.getX();
		double y=p.getY();
		if(Math.abs((x-110)/1%60)<=25){
			pos[0]=Math.round((float)(x-110))/60;
		}else if(Math.abs((x-110)/1%60)>=25){
			pos[0]=Math.round((float)(x-110))/60+1;
		}
		if(Math.abs((y-80)/1%60)<=25){
			pos[1]=Math.round((float)(y-80))/60;
		}else if(Math.abs((y-80)/1%60)>=25){
			pos[1]=Math.round((float)(y-80))/60+1;
		}
		return pos;
	}
	public void showShuYu(String name,int startI,int startJ,int endI,int endJ){
		int Sge,Sxian,Ege,Exian;
		String mess="";
		if(this.xq.caiPan){
			Sge=10-startJ;Sxian=9-startI;Ege=10-endJ;Exian=9-endI;
			if(name.equals("将")||name.equals("炮")||name.equals("卒")||name.equals("车")){
				if(Sxian==Exian){//在同一格上走有进退
					if(Sge<Ege){
						mess="我:"+name+toWord(Sxian)+"进"+toWord(Ege-Sge);//y文字 转换成中文
					}else if(Sge>Ege){//在同线上
						mess="我:"+name+""+toWord(Sxian)+"退"+toWord(Sge-Ege);
					}
				}else
				if(Sge==Ege){//在同一条横线上左右平
					   mess="我:"+name+toWord(Sxian)+"平"+toWord(Exian);
				}
			}
			if(name.equals("象")||name.equals("仕")||name.equals("马")){
				if(Sxian<Exian){//向右进
					mess="我:"+name+toWord(Sxian)+"进"+toWord(Exian);//y文字 转换成中文
				}else
					if(Sxian>Exian){//向左退
					mess="我:"+name+toWord(Sxian)+"退"+toWord(Exian);
				}
			}
		this.xq.jtmove.setText("等待对方走棋!!!");
		}
		this.xq.vformessage.add(mess);
		this.xq.jlmessage.setListData(this.xq.vformessage);
	}
	public String toWord(int i){
		String[] str={"","一","二","三","四","五","六","七","八","九"};
		return str[i];
	}
	public void move(int startI,int startJ,int endI,int endJ){
		if(qizi[endI][endJ]!=null&&(qizi[endI][endJ].getName().equals("帅")||
				qizi[endI][endJ].getName().equals("将"))){//如果将被吃了
			qizi[endI][endJ]=qizi[startI][startJ];
			qizi[startI][startJ]=null;//走棋
			this.xq.repaint();//重绘
			JOptionPane.showMessageDialog(this.xq, "很遗憾!!您失败了!!!","提示",JOptionPane.INFORMATION_MESSAGE);
			//this.xq.cat.tiaoZhanZhe=null;
			this.xq.color=0;//还原棋盘
			this.xq.caiPan=false;
			this.xq.next();//进入下一盘
			jiang1_i=4;jiang1_j=0;//将，帅的i,j的坐标
			jiang2_i=4;jiang2_j=9;
		}else{//如果不是将
			showShuYu(qizi[startI][startJ].getName(),startI, startJ, endI, endJ);
			qizi[endI][endJ]=qizi[startI][startJ];
			qizi[startI][startJ]=null;//走棋
			this.xq.repaint();//重绘
			this.xq.caiPan=true;
			if(qizi[endI][endJ].getName().equals("帅")){//如果移动的是帅
				jiang1_i=endI;jiang1_j=endJ;//更新帅的人位置
			}else if(qizi[endI][endJ].getName().equals("将")){
				jiang2_i=endI;jiang2_j=endJ;
			}
			if(jiang1_i==jiang2_i){//老将见面
				int count=0;
				for (int jiang_j=jiang1_j+1; jiang_j< jiang2_j; jiang_j++) {//遍历这条竖线
					if(qizi[jiang1_i][jiang_j]!=null){
						count++;break;//如果存在棋子，退出循环
					}
				}if(count==0){//如果等于0则照易将
					JOptionPane.showMessageDialog(this.xq, "对方照将!!!你胜利了!!!","提示",JOptionPane.INFORMATION_MESSAGE);
						//this.xq.cat.tiaoZhanZhe=null;
						this.xq.color=0;//还原棋盘
						this.xq.caiPan=false;
						this.xq.next();//进入下一盘
						jiang1_i=4;jiang1_j=0;//将，帅的i,j的坐标
						jiang2_i=4;jiang2_j=9;
						
				}}
		}
		this.xq.repaint();//重绘
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
