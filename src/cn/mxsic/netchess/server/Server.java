package cn.mxsic.netchess.server;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class Server extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image image=null;
	JLabel jlPort=new JLabel("端口");
	JTextField jtfPort=new JTextField("9999");
	JButton jbStart=new JButton("启动");
	JButton jbStop=new JButton("停止");
	JButton jbRefresh=new JButton("刷新");
	JPanel jps=new JPanel();
	JList<String> jlUserOnline=new JList<String>();
	JScrollPane jspx=new JScrollPane(jlUserOnline);
	JSplitPane jspz=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jspx,jps);
	
	ServerSocket ss;
	ServerThread st;
	Vector<ServerAgentThread> onlineList=new Vector<ServerAgentThread>();
	public Server() {
		// TODO Auto-generated constructor stub
		this.initialComponent();
		this.addListener();
		this.initialFrame();
	}
	
	
	private void initialComponent() {
		// TODO Auto-generated method stub
		jps.setLayout(null);
		jlPort.setBounds(20,20,50,20);
		jps.add(jlPort);
		this.jtfPort.setBounds(85,20,60,20);
		jps.add(this.jtfPort);
		this.jbStart.setBounds(18,50,60,20);
		jps.add(this.jbStart);
		this.jbStop.setBounds(85,50,60,20);
		jps.add(this.jbStop);
		this.jbRefresh.setBounds(18,80,120,20);
		jps.add(this.jbRefresh);
		jps.setForeground(Color.red);
		this.jbStop.setEnabled(false);
	}

 public static void main(String[] args) {
	new Server();
}
	private void addListener() {
		this.jbStart.addActionListener(this);
		this.jbStop.addActionListener(this);
		this.jbRefresh.addActionListener(this);
	}
	
	private void initialFrame() {
		this.setTitle("中国象棋网络版服务器端");
		this.setIconImage(image);
		this.add(jspz);
		jspz.setDividerLocation(250);
		jspz.setDividerSize(4);
		this.setBounds(400,230,420,320);
		this.setVisible(true);
		this.addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e){
						if(st==null){
							System.exit(0);
							return;
						}
						try{
							Vector<ServerAgentThread> v=onlineList;
							int size=v.size();
							for (int i = 0; i < size; i++) {
								ServerAgentThread tempSat=v.get(i); 
								tempSat.dout.writeUTF("<#SERVER_DOWN#>"); 
								tempSat.flag=false; 
							}
							st.flag=false;
							st=null;
							ss.close();
							v.clear();
							onlineList.clear();
							refreshList();
						}catch(Exception ea){
							ea.printStackTrace();
						}
						System.exit(0);
					}
				}	);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.jbStart){
			this.jbStart_event();
		}
		if(e.getSource()==this.jbStop){
			this.jbStop_event();
		}
		if(e.getSource()==this.jbRefresh){
			this.refreshList();
		}
	}

	private void jbStart_event() {
		int port=0;
		try{
			port=Integer.parseInt(this.jtfPort.getText().trim());
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "端口请输入数据1024〜65535","错误",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		if(port>65535||port<1024){
			JOptionPane.showMessageDialog(this, "端口不合法","错误",JOptionPane.ERROR_MESSAGE);
			return;
			}
		try{
			this.jbStart.setEnabled(false);
			this.jtfPort.setEnabled(false);
			this.jbStop.setEnabled(true);
			ss=new ServerSocket(port);
			st=new ServerThread(this);
			st.start();
			refreshList();
			JOptionPane.showMessageDialog(this, "启动成功","提示",JOptionPane.INFORMATION_MESSAGE);
		}catch(Exception ee){
			JOptionPane.showMessageDialog(this, "启动失败","提示",JOptionPane.INFORMATION_MESSAGE);
			this.jbStart.setEnabled(true);
			this.jtfPort.setEditable(true);
			this.jbStop.setEnabled(false);
		}
	}

	private void jbStop_event() {
		try{
			Vector<ServerAgentThread> v=onlineList;
			int size=v.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread tempSat=v.get(i);
				tempSat.dout.writeUTF("<#SERVER_DOWN#>");
				tempSat.flag=false;
			}
			st.flag=false;
			st=null;
			ss.close();
			onlineList.clear();
			v.clear();
			refreshList();
			this.jbStart.setEnabled(true);
			this.jtfPort.setEnabled(true);
			this.jbStop.setEnabled(false);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}

	
	public void refreshList(){
		Vector<String> v=new Vector<String>();
		int size=this.onlineList.size();
		for (int i = 0; i < size; i++) {
			ServerAgentThread tempSat=onlineList.get(i);
			String temps=tempSat.sc.getInetAddress().toString();
			temps="提示:"+temps+"   "+"  用户名:"+tempSat.getName();
			v.add(temps);
		}
		this.jlUserOnline.setListData(v); 
	}
}
