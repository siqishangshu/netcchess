package client;

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

public class QiPan extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int width;//��������֮��ľ���
	boolean focus=false;//���ӵ�״̬
	int jiang1_i=4;//˧��i����
	int jiang1_j=0;//˧��j����
	int jiang2_i=4;//����i����
	int jiang2_j=9;//����j����
	int startI=-1;int startJ=-1;//���ӿ�ʼ��λ��
	int endI=-1;int endJ=-1;//������ֹ��λ��
	public QiZi qizi[][];//��������
	XiangQi xq=null;
	GuiZe guize;
	public QiPan(QiZi qizi[][] ,int width,XiangQi xq){
		this.xq=xq;this.qizi=qizi;this.width=width;
		guize=new GuiZe(qizi);
		this.addMouseListener(this);
		this.setBounds(0,0,700,700);
		this.setLayout(null);
	}
	public void paint(Graphics g1){
		Graphics2D g=(Graphics2D)g1;//���Grahpics2D����
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�򿪒q���
		Color c=g.getColor();//��û�����ɫ
		g.setColor(XiangQi.bgcolor);//��������Ϊ������ɫ
		g.fill3DRect(60, 30, 580, 630, false);//����һ����������
		g.setColor(Color.black);
		for (int i = 80; i <=620; i=i+60) {
			g.drawLine(110, i, 590, i);//���������еĺ���
		}
		g.drawLine(110, 80, 110, 620);//���������
		g.drawLine(590, 80, 590, 620);//�����ұ���
		for (int i =170; i <=530; i=i+60) {
			g.drawLine(i, 80, i, 320);
			g.drawLine(i, 380, i, 620);//�����м������
		}
		g.drawLine(290, 80, 410, 200);//�������ߵ�б��
		g.drawLine(290, 200, 410, 80);
		g.drawLine(290, 620, 410, 500);//�������ߵ�б��
		g.drawLine(290, 500, 410, 620);
		//���� �����Ĳ��ִ��룬������
		this.smallLine(g,7,7);//���ư��ڳ��ڵ�λ�ñ�ʾ
		this.smallLine(g,1,7);
		this.smallLine(g,1,2);
		this.smallLine(g,7,2);
		
		g.setColor(Color.black);
		Font font1=new Font("����",Font.BOLD,50);//��������
		g.setFont(font1);
		g.drawString("��  ��", 150, 365);//��������
		g.drawString("��  ��", 390, 365);
		Font font=new Font("����",Font.BOLD,30);//��������
		g.setFont(font);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {//��������
				if(qizi[i][j]!=null){
					if(this.qizi[i][j].getFocus()!=false){//�Ƿ�ѡ��
						
						g.setColor(XiangQi.focusbg);//ѡ�к�ı���ɫ
						g.fillOval((110+i*60)-25, (80+j*60)-25, 50, 50);//���Ƹ�����
						g.setColor(XiangQi.focuschar);//������ɫ
					}else{
						g.fillOval(110+i*60-25,80+j*60-25, 50, 50);//���Ƹ�����
						g.setColor(qizi[i][j].getColor());//���û�����ɫ
					}
					g.drawString(qizi[i][j].getName(), 110+i*60-18, 80+j*60+10);
					g.setColor(Color.black);
				}
			}
		}
		g.setColor(c);
	}
	
	private void smallLine(Graphics2D g, int i, int j) {
		// TODO Auto-generated method stub
		int x=110+60*i; int y=80+60*j;//��������
		if(i>0){g.drawLine(x-3, y-3, x-20, y-3);g.drawLine(x-3, y-3, x-3, y-20);}//�������Ϸ��ı�ʾ
		if(i<8){g.drawLine(x+3, y-3, x+20, y-3);g.drawLine(x+3, y-3, x+3, y-20);}//�������Ϸ��ı�ʾ
		if(i>0){g.drawLine(x-3, y+3, x-20, y+3);g.drawLine(x-3, y+3, x-3, y+20);}//�������·��ı�ʾ
		if(i<8){g.drawLine(x+3, y+3, x+20, y+3);g.drawLine(x+3, y+3, x+3, y+20);}//�������·��ı�ʾ
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(this.xq.caiPan==true){//�ж��Ƿ��ֵ����������
			int i=-1,j=-1;
			int[] pos=GetPos(e);//�õ��¼���������
			i=pos[0];j=pos[1];//xy����
			if(i>=0&&i<=8&&j>=0&&j<=9){//���������������
				if(focus==false){//���֮ǰû�����ӱ�ѡ��
					 //û��ѡ ������
					this.noFocus(i, j);
					
				}else{//���֮ǰѡ�й�����
					if(qizi[i][j]!=null){//�������������
						if(qizi[i][j].getColor()==qizi[startI][startJ].getColor()){//������Լ�������
							qizi[startI][startJ].setFocus(false);//����ѡ�ж���
							qizi[i][j].setFocus(true);
							startI=i;startJ=j;//�����޸�
						}else{//����ǶԷ�����  ���˶Է�������
							endI=i;endJ=j;//����õ�
							String name=qizi[startI][startJ].getName();//�õ�������
							boolean canMove=guize.canMove(startI, startJ, endI, endJ, name);
							if(canMove){
								try{
									this.xq.cat.dout.writeUTF("<#MOVE#>"+this.xq.cat.tiaoZhanZhe+startI+startJ+endI+endJ);
									this.xq.caiPan=false;
									showShuYu(name,startI, startJ, endI, endJ);
								if(qizi[endI][endJ].getName().equals("˧")||qizi[endI][endJ].getName().equals("��")){
									this.success();//����յ�Է��Ľ���
								}else{
									this.noJiang();//����յ㲻�ǶԷ��Ľ�
								}
								}catch(Exception ea){ea.printStackTrace();}
							}
						}
					}else{//���û������ û������
						endI=i;endJ=j;//�����յ�
						String name=qizi[startI][startJ].getName();//�õ������ӵ�����
						boolean canMove=guize.canMove(startI, startJ, endI, endJ, name);
						if(canMove){
							showShuYu(name,startI, startJ, endI, endJ);
							this.noQiZi();
						}
					}
				}
				
			}
			
			this.xq.repaint();//�ػ�
		}
	}

	private void noJiang() {
		// TODO Auto-generated method stub
		qizi[endI][endJ]=qizi[startI][startJ];
		qizi[startI][startJ]=null;//����
		qizi[endI][endJ].setFocus(false);//��������Ϊ��ѡ��״̬
		this.xq.repaint();//�ػ�
		if(qizi[endI][endJ].getName().equals("˧")){//����ƶ�����˧
			jiang1_i=endI;jiang1_j=endJ;//����˧����λ��
		}else if(qizi[endI][endJ].getName().equals("��")){
			jiang2_i=endI;jiang2_j=endJ;
		}
		if(jiang1_i==jiang2_i){//�Ͻ�����
			int count=0;
			for (int jiang_j=jiang1_j+1; jiang_j< jiang2_j; jiang_j++) {//������������
				if(qizi[jiang1_i][jiang_j]!=null){
					count++;break;//����������ӣ��˳�ѭ��
				}
			}
			if(count==0){//�������0�����׽�
				JOptionPane.showMessageDialog(this.xq, "�ս�!!!��ʧ����!!!","��ʾ",JOptionPane.INFORMATION_MESSAGE);
					this.xq.cat.tiaoZhanZhe=null;
					this.xq.color=0;//��ԭ����
					this.xq.caiPan=false;
					this.xq.next();//������һ��
					jiang1_i=4;jiang1_j=0;//����˧��i,j������
					jiang2_i=4;jiang2_j=9;
					
			}
		}
		startI=-1;startJ=-1;//��ԭ�����
		endI=-1;endJ=-1;
		focus=false;
	}
	private void success() {
		// TODO Auto-generated method stub
		qizi[endI][endJ]=qizi[startI][startJ];//�Ե�������
		qizi[startI][startJ]=null;//��ԭ����λ�����
		this.xq.repaint();//�ػ�
		JOptionPane.showMessageDialog(this.xq, "��ϲ��������ʤ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
		//������ʤ��Ϣ
		this.xq.cat.tiaoZhanZhe=null;//��ս��ʧ��
		this.xq.color=0;
		this.xq.caiPan=false;
		this.xq.next();
		startI=-1;startJ=-1;
		endI=-1;endJ=-1;
		jiang1_i=4;jiang1_j=0;//����˧��i,j������
		jiang2_i=4;jiang2_j=9;
		focus=false;
	}
	private void noQiZi() {
		// TODO Auto-generated method stub
		try{//���ƶ���Ϣ���͸��Է�
			this.xq.cat.dout.writeUTF("<#MOVE#>"+this.xq.cat.tiaoZhanZhe+startI+startJ+endI+endJ);
			this.xq.caiPan=false;
			qizi[endI][endJ]=qizi[startI][startJ];
			qizi[startI][startJ]=null;//����
			qizi[endI][endJ].setFocus(false);//��������Ϊ��ѡ��״̬
			this.xq.repaint();//�ػ�
			if(qizi[endI][endJ].getName().equals("˧")){//����ƶ�����˧
				jiang1_i=endI;jiang1_j=endJ;//����˧����λ��
			}else if(qizi[endI][endJ].getName().equals("��")){
				jiang2_i=endI;jiang2_j=endJ;
			}
			if(jiang1_i==jiang2_i){//�Ͻ�����
				int count=0;
				for (int jiang_j=jiang1_j+1; jiang_j< jiang2_j; jiang_j++) {//������������
					if(qizi[jiang1_i][jiang_j]!=null){
						count++;break;//����������ӣ��˳�ѭ��
					}
				}if(count==0){//�������0�����׽�
					JOptionPane.showMessageDialog(this.xq, "�ս�!!!��ʧ����!!!","��ʾ",JOptionPane.INFORMATION_MESSAGE);
						this.xq.cat.tiaoZhanZhe=null;
						this.xq.color=0;//��ԭ����
						this.xq.caiPan=false;
						this.xq.next();//������һ��
						jiang1_i=4;jiang1_j=0;//����˧��i,j������
						jiang2_i=4;jiang2_j=9;
						
				}
			}
			startI=-1;startJ=-1;//��ԭ�����
			endI=-1;endJ=-1;
			focus=false;
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	private void noFocus(int i, int j) {
		// TODO Auto-generated method stub
		if(this.qizi[i][j]!=null){//�����λ��������
			if(this.xq.color==0){//����Ǻ췽��
				if(this.qizi[i][j].getColor().equals(this.xq.color1)){//��������Ǻ�ɫ
					this.qizi[i][j].setFocus(true);//����������Ϊѡ��״̬
					focus=true;//��ʾ�Ѿ���ѡ�еĽ���
					startI=i;startJ=j;//����������
				}
			}else{//����ǰ׷���
				if(this.qizi[i][j].getColor().equals(this.xq.color2)){//��������ǰ�ɫ
					this.qizi[i][j].setFocus(true);//����������Ϊѡ��״̬
					focus=true;//��ʾ�Ѿ���ѡ�еĽ���
					startI=i;startJ=j;//����������
				}
			}
		}
	}
	private int[] GetPos(MouseEvent e) {
		// TODO Auto-generated method stub
		int[] pos=new int[2];
		pos[0]=-1;pos[1]=-1;
		Point p=e.getPoint();//����¼�����������
		double x=p.getX();
		double y=p.getY();
		if(Math.abs((x-110)/1%60)<=25){
			pos[0]=Math.round((float)(x-110))/60;//��ö�Ӧ��x�±��λ��
		}else if(Math.abs((x-110)/1%60)>=25){
			pos[0]=Math.round((float)(x-110))/60+1;//��ö�Ӧx�±��λ��
		}
		if(Math.abs((y-80)/1%60)<=25){
			pos[1]=Math.round((float)(y-80))/60;//��ö�Ӧ��y�±��λ��
		}else if(Math.abs((y-80)/1%60)>=25){
			pos[1]=Math.round((float)(y-80))/60+1;//��ö�Ӧy�±��λ��
		}
		return pos;
	}
	public void showShuYu(String name,int startI,int startJ,int endI,int endJ){
		int Sge,Sxian,Ege,Exian;//ge�������µģ�J��xian�������ҵ�I
		String mess="";
		if(this.xq.caiPan){
			Sge=10-startJ;Sxian=9-startI;Ege=10-endJ;Exian=9-endI;
			if(name.equals("��")||name.equals("�h")||name.equals("��")||name.equals("��")){
				if(Sxian==Exian){//��ͬһ�������н���
					if(Sge<Ege){
						mess="��:"+name+toWord(Sxian)+"��"+toWord(Ege-Sge);//y���� ת��������
					}else if(Sge>Ege){//��ͬ����
						mess="��:"+name+""+toWord(Sxian)+"��"+toWord(Sge-Ege);
					}
				}else
				if(Sge==Ege){//��ͬһ������������ƽ
					   mess="��:"+name+toWord(Sxian)+"ƽ"+toWord(Exian);
				}
			}
			if(name.equals("��")||name.equals("��")||name.equals("��")){
				if(Sxian<Exian){//���ҽ�
					mess="��:"+name+toWord(Sxian)+"��"+toWord(Exian);//y���� ת��������
				}else
					if(Sxian>Exian){//������
					mess="��:"+name+toWord(Sxian)+"��"+toWord(Exian);
				}
			}
		this.xq.jtmove.setText("�ȴ���������!!!");
		}
		this.xq.vformessage.add(mess);
		this.xq.jlmessage.setListData(this.xq.vformessage);
	}
	public String toWord(int i){
		String[] str={"","һ","��","��","��","��","��","��","��","��"};
		return str[i];
	}
	public void move(int startI,int startJ,int endI,int endJ){
		if(qizi[endI][endJ]!=null&&(qizi[endI][endJ].getName().equals("˧")||
				qizi[endI][endJ].getName().equals("��"))){//�����������
			qizi[endI][endJ]=qizi[startI][startJ];
			qizi[startI][startJ]=null;//����
			this.xq.repaint();//�ػ�
			JOptionPane.showMessageDialog(this.xq, "���ź�!!��ʧ����!!!","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			this.xq.cat.tiaoZhanZhe=null;
			this.xq.color=0;//��ԭ����
			this.xq.caiPan=false;
			this.xq.next();//������һ��
			jiang1_i=4;jiang1_j=0;//����˧��i,j������
			jiang2_i=4;jiang2_j=9;
		}else{//������ǽ�
			this.xq.caiPan=true;
			qizi[endI][endJ]=qizi[startI][startJ];
			qizi[startI][startJ]=null;//����
			this.xq.repaint();//�ػ�
			if(qizi[endI][endJ].getName().equals("˧")){//����ƶ�����˧
				jiang1_i=endI;jiang1_j=endJ;//����˧����λ��
			}else if(qizi[endI][endJ].getName().equals("��")){
				jiang2_i=endI;jiang2_j=endJ;
			}
			if(jiang1_i==jiang2_i){//�Ͻ�����
				int count=0;
				for (int jiang_j=jiang1_j+1; jiang_j< jiang2_j; jiang_j++) {//������������
					if(qizi[jiang1_i][jiang_j]!=null){
						count++;break;//����������ӣ��˳�ѭ��
					}
				}if(count==0){//�������0�����׽�
					JOptionPane.showMessageDialog(this.xq, "�Է��ս�!!!��ʤ����!!!","��ʾ",JOptionPane.INFORMATION_MESSAGE);
						this.xq.cat.tiaoZhanZhe=null;
						this.xq.color=0;//��ԭ����
						this.xq.caiPan=false;
						this.xq.next();//������һ��
						jiang1_i=4;jiang1_j=0;//����˧��i,j������
						jiang2_i=4;jiang2_j=9;
						
				}
			}
		}
		this.xq.repaint();//�ػ�
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
