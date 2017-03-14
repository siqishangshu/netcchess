package client;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class XiangQi extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Color bgcolor=new Color(245,250,160);//棋盘的背景色
	public static final Color focusbg=new Color(242, 242, 242);//棋子选中后的背景色
	public static final Color focuschar=new Color(96, 95, 91);//棋子选中后字符的颜色
	public   Color color1=Color.yellow;//黄方颜色
	public   Color color2=Color.white;//白方颜色
	JLabel jlHost=new JLabel("主机名");
	JLabel jlPort=new JLabel("端口号");
	JLabel jlNickName=new JLabel("昵   称");
	public JTextField jtfHost=new JTextField("127.0.0.1");
	public JTextField jtfPort=new JTextField("9999");
	public JTextField jtfNickName=new JTextField("");
	public JButton jbConnect=new JButton("连   接");
	public JButton jbDisconnect=new JButton("断   开");
	public JButton jbFail=new JButton("认   输");
	public JButton jbChallenge=new JButton("挑   战");
	public JButton jbYChallenge=new JButton("接受");
	public JButton jbNChallenge=new JButton("拒绝");
	public JButton jbChonglai=new JButton("重来");
	public JLabel jlvs=new JLabel("playone   VS   playtwo ");//对战消息
	public JComboBox<String> jcbNickList=new JComboBox<String>();//创建存放当前用户的下拉 表
	public JTextField message=new JTextField();
	private JButton send=new JButton("发送");
	private JButton canel=new JButton("取消");
	private  Date now;//消息时间 
	public JList<String> jlmessage=new JList<String>();
	int width=60;//设置棋子两线之间的距离
	QiZi[][] qizi=new QiZi[9][10];//创建棋子数组
	public QiPan jpz=new QiPan(qizi,width,this);//创建棋盘
	public JTextArea jtmove=new JTextArea("提示信息");//显示走棋路子
	JPanel jpleft=new JPanel();//整体右边的面板
	Vector<String> vformessage=new Vector<String>();//消息列表
	JScrollPane jsptxt=new JScrollPane(jlmessage);//加入到消息面板
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jpleft);//整体左右分
	public boolean caiPan=false;//可否走棋的标志位。。。。裁判
	public int color=0;//0代表红棋，1代表白棋
	public Socket sc;
	public ClientAgentThread cat;//声明客户端线程的引用
	public XiangQi(){
		this.initialComponent();
		this.addListener();
		this.initialState();
		this.initialQiZi();
		this.initialFrame();
	}
	private void initialFrame() {
		// TODO Auto-generated method stub
		this.setTitle("中国象棋--客户端");
		Image image=new ImageIcon("ico.gif").getImage();
		this.setIconImage(image);
		String s="暂时没有聊天记录!";vformessage.add(s);
		jlmessage.setListData(vformessage);
		jsp.setDividerLocation(730);//分割线位置
		jsp.setDividerSize(4);//分割线宽度
		//jsp.setEnabled(false);//设置分割线是否可动
		this.add(this.jsp);
		this.setBounds(30,30,930,730);
		this.setVisible(true);
		this.addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e){
						if(cat==null){
							System.exit(0);
						}
						try{
							if(cat.tiaoZhanZhe!=null){
								try{
								cat.dout.writeUTF("<#REN_SHU#>"+cat.tiaoZhanZhe+jtfNickName);
								}catch(Exception ee){
							ee.printStackTrace();
						}}
							cat.dout.writeUTF("<#CLIENT_LEAVE#>"+jtfNickName.getText().trim());//向服务器发送离开消息
							cat.flag=false;
							cat=null;
						}catch(Exception ee){
							ee.printStackTrace();
						}
						System.exit(0);
					}
					
				});
	}

	private void initialQiZi() {
		// TODO Auto-generated method stub
		qizi[0][0]=new QiZi(color1,"车",0,0);
		qizi[1][0]=new QiZi(color1,"马",1,0);
		qizi[2][0]=new QiZi(color1,"相",2,0);
		qizi[3][0]=new QiZi(color1,"士",3,0);
		qizi[4][0]=new QiZi(color1,"帅",4,0);
		qizi[5][0]=new QiZi(color1,"士",5,0);
		qizi[6][0]=new QiZi(color1,"相",6,0);
		qizi[7][0]=new QiZi(color1,"马",7,0);
		qizi[8][0]=new QiZi(color1,"车",8,0);
		qizi[1][2]=new QiZi(color1,"炮",1,2);
		qizi[7][2]=new QiZi(color1,"炮",7,2);
		qizi[0][3]=new QiZi(color1,"兵",0,3);
		qizi[2][3]=new QiZi(color1,"兵",2,3);
		qizi[4][3]=new QiZi(color1,"兵",4,3);
		qizi[6][3]=new QiZi(color1,"兵",6,3);
		qizi[8][3]=new QiZi(color1,"兵",8,3);
		//摩尔
		
		qizi[0][6]=new QiZi(color2,"卒",0,6);
		qizi[2][6]=new QiZi(color2,"卒",2,6);
		qizi[4][6]=new QiZi(color2,"卒",4,6);
		qizi[6][6]=new QiZi(color2,"卒",6,6);
		qizi[8][6]=new QiZi(color2,"卒",8,6);
		qizi[1][7]=new QiZi(color2,"砲",1,7);
		qizi[7][7]=new QiZi(color2,"砲",7,7);
		qizi[0][9]=new QiZi(color2,"车",0,9);
		qizi[1][9]=new QiZi(color2,"马",1,9);
		qizi[2][9]=new QiZi(color2,"象",2,9);
		qizi[3][9]=new QiZi(color2,"仕",3,9);
		qizi[4][9]=new QiZi(color2,"将",4,9);
		qizi[5][9]=new QiZi(color2,"仕",5,9);
		qizi[6][9]=new QiZi(color2,"象",6,9);
		qizi[7][9]=new QiZi(color2,"马",7,9);
		qizi[8][9]=new QiZi(color2,"车",8,9);
	}



	private void initialState() {
		// TODO Auto-generated method stub
		this.jbDisconnect.setEnabled(false);
		this.jbChallenge.setEnabled(false);
		this.jbYChallenge.setEnabled(false);
		this.jbNChallenge.setEnabled(false);
		this.jbFail.setEnabled(false);
		this.jtmove.setEnabled(false);
		this.jbChonglai.setEnabled(false);
	}



	private void addListener() {
		// TODO Auto-generated method stub
		this.jbConnect.addActionListener(this);
		this.jbDisconnect.addActionListener(this);
		this.jbChallenge.addActionListener(this);
		this.jbFail.addActionListener(this);
		this.jbYChallenge.addActionListener(this);
		this.jbNChallenge.addActionListener(this);
		this.jbChonglai.addActionListener(this);
		this.send.addActionListener(this);
		this.canel.addActionListener(this);
	}



	private void initialComponent() {
		// TODO Auto-generated method stub
		jpleft.setLayout(null);
		this.message.setBounds(10,580,160,20);jpleft.add(this.message);//消息框
		this.send.setBounds(10,600,60,20);jpleft.add(this.send);//发送消息
		this.canel.setBounds(100,600,60,20);jpleft.add(this.canel);//取消发送
		this.jsptxt.setBounds(10, 300, 160, 250);jpleft.add(this.jsptxt);
		this.jlvs.setBounds(20, 250, 160, 20);jpleft.add(this.jlvs);//vs对战消息
		this.jtmove.setBounds(20, 270, 140, 20);jpleft.add(this.jtmove);//提示消息
		this.jlHost.setBounds(10,10,50,20);jpleft.add(this.jlHost);//添加“主机号”标签
		this.jtfHost.setBounds(70,10,80,20);jpleft.add(this.jtfHost);
		this.jlPort.setBounds(10,40,50,20);jpleft.add(this.jlPort);
		this.jtfPort.setBounds(70,40,80,20);jpleft.add(this.jtfPort);
		this.jlNickName.setBounds(10,70,50,20);jpleft.add(this.jlNickName);
		this.jtfNickName.setBounds(70,70,80,20);jpleft.add(this.jtfNickName);
		this.jbConnect.setBounds(10,100,80,20);jpleft.add(this.jbConnect);
		this.jbDisconnect.setBounds(95,100,80,20);jpleft.add(this.jbDisconnect);
		this.jcbNickList.setBounds(20,130,130,20);jpleft.add(this.jcbNickList);
		this.jbChallenge.setBounds(5,160,80,20);jpleft.add(this.jbChallenge);
		this.jbFail.setBounds(95,160,80,20);jpleft.add(this.jbFail);
		this.jbYChallenge.setBounds(5,190,80,20);jpleft.add(this.jbYChallenge);
		this.jbNChallenge.setBounds(95,190,80,20);jpleft.add(this.jbNChallenge);
		this.jbChonglai.setBounds(50,220,80,20);jpleft.add(this.jbChonglai);
		jpleft.setBackground(new Color(99, 220, 33));
		jpz.setBounds(0,0,700,700);
	}

public static void main(String[] args) {
	new XiangQi();
}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==jbConnect){
			this.jbConnect_event();
		}
		if(e.getSource()==jbDisconnect){
			this.jbDisconnect_event();
		}
		if(e.getSource()==jbChallenge){
			this.jbChallenge_event();
		}
		if(e.getSource()==jbYChallenge){
			this.jbYChallenge_event();
		}
		if(e.getSource()==jbNChallenge){
			this.jbNChallenge_event();
		}
		if(e.getSource()==jbFail){
			this.jbFail_event();
		}
		if(e.getSource()==jbChonglai){
			this.next();
		}
		if(e.getSource()==send){
			this.send();
		}
		if(e.getSource()==canel){
			this.canel();
		}
	}
	private void canel() {//取消发送
		// TODO Auto-generated method stub
		this.message.setText(null);
	}



	private void send() {//发送消息
		// TODO Auto-generated method stub
		now=new Date();
		if(!(message.getText().trim().equals(""))||(message.getText().trim().equals(null))){//发送消息不能为空
		@SuppressWarnings("deprecation")
		String time=(now.getMonth()+1)+"-"+(now.getDate())+"  "+now.getHours()+":"+
		now.getMinutes()+":"+now.getSeconds();
		String ms=" "+message.getText();
		this.vformessage.remove("暂时没有聊天记录!");
		this.vformessage.add(time);
		this.vformessage.add(ms);
		jlmessage.setListData(vformessage);
		try {
			this.cat.dout.writeUTF("<#MESSAGE#>"+this.cat.tiaoZhanZhe+"|"+jtfNickName.getText().trim()+time+"|"+ms);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.canel();
		}
	}


	public void next(){
		//这局完一，下一局
		for (int i = 0; i <9; i++) {
			for (int j = 0; j < 10; j++) {
				qizi[i][j]=null;
			}
		}
		vformessage.removeAllElements();
		this.initialQiZi();
		this.repaint();
	}

	private void jbFail_event() {
		// TODO Auto-generated method stub
		try{
			this.cat.dout.writeUTF("<#REN_SHU#>"+this.cat.tiaoZhanZhe+
					"|"+this.jtfNickName.getText().trim());
			this.cat.tiaoZhanZhe=null;
			this.color=0;
			this.caiPan=false;
			this.next();//开始下一盘
			this.jtfHost.setEnabled(false);//设为不可用
			this.jtfPort.setEnabled(false);
			this.jtfNickName.setEnabled(false);
			this.jbConnect.setEnabled(false);
			this.jbDisconnect.setEnabled(true);
			this.jbChallenge.setEnabled(true);
			this.jbYChallenge.setEnabled(false);
			this.jbNChallenge.setEnabled(false);
			this.jbFail.setEnabled(false);
			this.jcbNickList.setEnabled(true);
			this.jlvs.setText("playone   VS   playtwo ");
		}catch(Exception e){
			e.printStackTrace();
		}	
	}



	private void jbNChallenge_event() {
		try{
		this.cat.dout.writeUTF("<#BU_TONG_YI#>"+this.cat.tiaoZhanZhe+"|"+this.jtfNickName.getText().trim());
		this.cat.tiaoZhanZhe=null;
		this.jtfHost.setEnabled(false);//设为不可用
		this.jtfPort.setEnabled(false);
		this.jtfNickName.setEnabled(false);
		this.jbConnect.setEnabled(false);
		this.jbDisconnect.setEnabled(true);
		this.jbChallenge.setEnabled(true);
		this.jbYChallenge.setEnabled(false);
		this.jbNChallenge.setEnabled(false);
		this.jbFail.setEnabled(false);
		this.jcbNickList.setEnabled(true);
	}catch(Exception e){
		e.printStackTrace();
	}	
	}



	private void jbYChallenge_event() {
		// TODO Auto-generated method stub
		try{
			this.cat.dout.writeUTF("<#TONG_YI#>"+this.cat.tiaoZhanZhe+"|"+this.jtfNickName.getText().trim());
			this.caiPan=true;
			this.color=1;
			this.jtfHost.setEnabled(false);//设为不可用
			this.jtfPort.setEnabled(false);
			this.jtfNickName.setEnabled(false);
			this.jbConnect.setEnabled(false);
			this.jbDisconnect.setEnabled(false);
			this.jbChallenge.setEnabled(false);
			this.jbYChallenge.setEnabled(false);
			this.jbNChallenge.setEnabled(false);
			this.jbFail.setEnabled(true);
			this.jcbNickList.setEnabled(false);
			this.jlvs.setText(this.cat.tiaoZhanZhe+"    VS    "+this.jtfNickName.getText().trim());
			this.jtmove.setText("该您走棋!!!");
			Color c;
			c=color1;
			color1=color2;
			color2=c;
			initialQiZi();
			this.repaint();
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	private void jbChallenge_event() {
		// TODO Auto-generated method stub
		Object o=this.jcbNickList.getSelectedItem();//得到选 中的对象
		if(o==null||((String)o).equals("")){
			JOptionPane.showMessageDialog(this, "请选择挑战对象!!!","错误",JOptionPane.ERROR_MESSAGE);
		}else{
			String name2=(String)this.jcbNickList.getSelectedItem();//获得挑战对象
			try{
			this.jtfHost.setEnabled(false);//设为不可用
			this.jtfPort.setEnabled(false);
			this.jtfNickName.setEnabled(false);
			this.jbConnect.setEnabled(false);
			this.jbDisconnect.setEnabled(true);
			this.jbChallenge.setEnabled(false);
			this.jbYChallenge.setEnabled(false);
			this.jbNChallenge.setEnabled(false);
			this.jbFail.setEnabled(true);
			this.jcbNickList.setEnabled(false);
			this.cat.tiaoZhanZhe=name2;
			this.caiPan=false;//将裁判可用
			this.color=0;
			this.cat.dout.writeUTF("<#TIAO_ZHAN#>"+name2+"|"+this.jtfNickName.getText().trim());//向服务器发送挑战消息
			JOptionPane.showMessageDialog(this, "已向"+name2+"提出挑战,请等待恢复....(可以认输，取消挑战)","提示",JOptionPane.INFORMATION_MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
		}
		}
	}



	private void jbDisconnect_event() {
		// TODO Auto-generated method stub
		try{
			if(!(this.cat.tiaoZhanZhe==null)){
				this.jbFail_event();
			}
			this.cat.dout.writeUTF("<#CLIENT_LEAVE#>"+this.jtfNickName.getText().trim());//向服务器发送离开消息
			this.cat.flag=false;//终止客户端代理线程
			this.cat=null;
			this.jtfHost.setEnabled(true);//设为不可用
			this.jtfPort.setEnabled(true);
			this.jtfNickName.setEnabled(true);
			this.jbConnect.setEnabled(true);
			this.jbDisconnect.setEnabled(false);
			this.jbChallenge.setEnabled(false);
			this.jbYChallenge.setEnabled(false);
			this.jbNChallenge.setEnabled(false);
			this.jbFail.setEnabled(false);
			this.jbChonglai.setEnabled(true);
			this.jlvs.setText("  palyone  VS  palytwo  ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	private void jbConnect_event() {
		// TODO Auto-generated method stub
		int port=0;
		try{
			port=Integer.parseInt(this.jtfPort.getText().trim());
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "端口号只能是整数 ","错误",JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(port>65535||port<0){
			JOptionPane.showMessageDialog(this, "端口号只能是0-65535的整数","错误",JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String name=this.jtfNickName.getText().trim();
		if(name.length()==0){
			JOptionPane.showMessageDialog(this, "玩家姓名不能为空","错误",JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try{
			sc=new Socket(this.jtfHost.getText().trim(),port);
			cat=new ClientAgentThread(this);//创建客户端代理线程
			cat.start();//启动客户端代理线程
			this.jtfHost.setEnabled(false);//设为不可用
			this.jtfPort.setEnabled(false);
			this.jtfNickName.setEnabled(false);
			this.jbConnect.setEnabled(false);
			this.jbDisconnect.setEnabled(true);
			this.jbChallenge.setEnabled(true);
			this.jbYChallenge.setEnabled(false);
			this.jbNChallenge.setEnabled(false);
			this.jbFail.setEnabled(false);
			this.jcbNickList.setEnabled(true);
			this.jbChonglai.setEnabled(false);
			JOptionPane.showMessageDialog(this, "已连接到服务器","提示",JOptionPane.INFORMATION_MESSAGE);
		}catch(ConnectException  e){
			 JOptionPane.showMessageDialog(this, "服务器未开启","错误",JOptionPane.INFORMATION_MESSAGE);
			return;
		}catch(Exception ee){
			JOptionPane.showMessageDialog(this, "连接服务器失败","错误",JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}

}
