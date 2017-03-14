package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class ClientAgentThread extends Thread {
	public XiangQi father;
	public boolean flag=true;
	public DataInputStream din;
	public DataOutputStream dout;
	public String tiaoZhanZhe=null;
	public ClientAgentThread(XiangQi father) {
		// TODO Auto-generated method stub
		this.father=father;
		try{
			din=new DataInputStream(father.sc.getInputStream());//创建数据输入输出流
			dout=new DataOutputStream(father.sc.getOutputStream());
			String name=father.jtfNickName.getText().trim();
			dout.writeUTF("<#NICK_NAME#>"+name);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void run(){
		while(flag){
			try{
				String msg=din.readUTF().trim();//获得服务器发来的信息
				if(msg.startsWith("<#NAME_CHONGMING#>")){this.name_chongming();}
				else if(msg.startsWith("<#NICK_LIST#>")){this.nick_list(msg);}
				else if(msg.startsWith("<#SERVER_DOWN#>")){this.server_down();}
				else if(msg.startsWith("<#TIAO_ZHAN#>")){this.tiao_zhan(msg);}
				else if(msg.startsWith("<#TONG_YI#>")){this.tong_yi(msg);}
				else if(msg.startsWith("<#BU_TONG_YI#>")){this.bu_tong_yi(msg);}
				else if(msg.startsWith("<#BUSY#>")){this.busy(msg);}
				else if(msg.startsWith("<#MOVE#>")){this.move(msg);}
				else if(msg.startsWith("<#REN_SHU#>")){this.ren_shu(msg);}
				else if(msg.startsWith("<#MSG#>")){this.msg(msg);}
				else if(msg.startsWith("<#MESSAGE#>")){this.message(msg);}
			}catch(Exception e){
				flag=false;
			}
		}
	}
	private void message(String msg) {
		// TODO Auto-generated method stub
			String name=msg.substring(11);//获得接收方的名字
			String[] na=name.split("\\|");
			this.father.vformessage.add(na[1]);
			this.father.vformessage.add(na[2]);
			this.father.jlmessage.setListData(this.father.vformessage);
	}
	private void msg(String msg) {
		// TODO Auto-generated method stub
		father.vformessage.add(msg.substring(7));//将消息加入显示
		father.jlmessage.setListData(father.vformessage);
	}
	private void ren_shu(String msg) {
		// TODO Auto-generated method stub
		String name=msg.substring(11);
		this.tiaoZhanZhe=null;
		this.father.jlvs.setText("  palyone  VS  palytwo  ");
		JOptionPane.showMessageDialog(this.father, "恭喜您,你获胜!"+name+"认输!!!","提示",JOptionPane.INFORMATION_MESSAGE);
		this.father.color=0;
		this.father.caiPan=false;
		this.father.next();
		this.father.jtfHost.setEnabled(false);//设置各个控件
		this.father.jtfPort.setEnabled(false);
		this.father.jtfNickName.setEnabled(false);
		this.father.jbConnect.setEnabled(false);
		this.father.jbDisconnect.setEnabled(true);
		this.father.jbChallenge.setEnabled(true);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		this.father.jcbNickList.setEnabled(true);
	}
	private void move(String msg) {
		// TODO Auto-generated method stub
		int length=msg.length();
		int startI=Integer.parseInt(msg.substring(length-4,length-3));
		int startJ=Integer.parseInt(msg.substring(length-3,length-2));
		int endI=Integer.parseInt(msg.substring(length-2,length-1));
		int endJ=Integer.parseInt(msg.substring(length-1));
		startI=8-startI;startJ=9-startJ;endI=8-endI;endJ=9-endJ;
		showShuYu(father.qizi[startI][startJ].getName(), startI, startJ, endI, endJ);
		this.father.jpz.move(startI, startJ,endI,endJ);
	}
public void showShuYu(String name,int startI,int startJ,int endI,int endJ){
		int Sge,Sxian,Ege,Exian;//ge就是上下的，J，xian就是左右的I
		String mess="";
		Sge=1+startJ;Sxian=1+startI;Ege=1+endJ;Exian=1+endI;
		if(name.equals("帅")||name.equals("炮")||name.equals("兵")||name.equals("车")){
				if(Sxian==Exian){//在同一格上走有进退
					if(Sge<Ege){
						mess=tiaoZhanZhe+name+Sxian+"进"+(Ege-Sge);//y文字 转换成中文
					}else if(Sge>Ege){//在同线上
						mess=tiaoZhanZhe+name+""+Sxian+"退"+(Sge-Ege);
					}
				}else
				if(Sge==Ege){//在同一条横线上左右平
					   mess=tiaoZhanZhe+name+Sxian+"平"+Exian;
				}
			}
			if(name.equals("相")||name.equals("士")||name.equals("马")){
				if(Sxian<Exian){//向右进
					mess=tiaoZhanZhe+name+Sxian+"进"+Exian;//y文字 转换成中文
				}else
					if(Sxian>Exian){//向左退
					mess=tiaoZhanZhe+name+Sxian+"退"+Exian;
				}
			}
		this.father.jtmove.setText("等待您走棋!!!");
		this.father.vformessage.add(mess);
		this.father.jlmessage.setListData(this.father.vformessage);
	}
	private void busy(String msg) {
		// TODO Auto-generated method stub
		this.father.caiPan=false;
		this.father.color=0;
		this.father.jtfHost.setEnabled(false);//设置各个控件
		this.father.jtfPort.setEnabled(false);
		this.father.jtfNickName.setEnabled(false);
		this.father.jbConnect.setEnabled(false);
		this.father.jbDisconnect.setEnabled(true);
		this.father.jbChallenge.setEnabled(true);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		this.father.jcbNickList.setEnabled(true);
		JOptionPane.showMessageDialog(this.father, msg+"在忙碌中!!!","提示",JOptionPane.INFORMATION_MESSAGE);
		this.tiaoZhanZhe=null;
	}
	private void bu_tong_yi(String msg) {
		// TODO Auto-generated method stub
		String name=msg.substring(14);
		this.father.caiPan=false;
		this.father.color=0;
		this.father.jtfHost.setEnabled(false);//设置各个控件
		this.father.jtfPort.setEnabled(false);
		this.father.jtfNickName.setEnabled(false);
		this.father.jbConnect.setEnabled(false);
		this.father.jbDisconnect.setEnabled(true);
		this.father.jbChallenge.setEnabled(true);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		this.father.jcbNickList.setEnabled(true);
		JOptionPane.showMessageDialog(this.father, name+"拒绝您的挑战!!!","提示",JOptionPane.INFORMATION_MESSAGE);
		this.tiaoZhanZhe=null;
	}
	private void tong_yi(String msg) {
		// TODO Auto-generated method stub
		String name=msg.substring(11);
		this.father.jtfHost.setEnabled(false);//设置各个控件
		this.father.jtfPort.setEnabled(false);
		this.father.jtfNickName.setEnabled(false);
		this.father.jbConnect.setEnabled(false);
		this.father.jbDisconnect.setEnabled(false);
		this.father.jbChallenge.setEnabled(false);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(true);
		this.father.color=1;
		this.father.jlvs.setText(this.tiaoZhanZhe+"    VS    "+this.father.jtfNickName.getText().trim());
		JOptionPane.showMessageDialog(this.father, name+"接受您的挑战!"+name+"先走!","提示",JOptionPane.INFORMATION_MESSAGE);
	}
	private void tiao_zhan(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(13);
			String[] na=name.split("\\|");
			if(this.tiaoZhanZhe==null){
				tiaoZhanZhe=na[1];//为tiaozhenzhe赋值
				this.father.jtfHost.setEnabled(false);//设置各个控件
				this.father.jtfPort.setEnabled(false);
				this.father.jtfNickName.setEnabled(false);
				this.father.jbConnect.setEnabled(false);
				this.father.jbDisconnect.setEnabled(false);
				this.father.jbChallenge.setEnabled(false);
				this.father.jbYChallenge.setEnabled(true);
				this.father.jbNChallenge.setEnabled(true);
				this.father.jbFail.setEnabled(false);
				JOptionPane.showMessageDialog(this.father, na[1]+"向你挑战!!!","提示",JOptionPane.INFORMATION_MESSAGE);
			}else{
				this.father.vformessage.add(na[1]+"您发出了挑战请求!!!");
				this.father.jlmessage.setListData(this.father.vformessage);
				this.dout.writeUTF("<#BUSY#>"+name);
			}
			}catch(IOException e){e.printStackTrace();}
	}
	private void server_down() {
		// TODO Auto-generated method stub
		this.father.jtfHost.setEnabled(true);//设置各个控件
		this.father.jtfPort.setEnabled(true);
		this.father.jtfNickName.setEnabled(true);
		this.father.jbConnect.setEnabled(true);
		this.father.jbDisconnect.setEnabled(false);
		this.father.jbChallenge.setEnabled(false);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		this.flag=false;
		father.cat=null;
		JOptionPane.showMessageDialog(this.father, "服务器停止!!","提示",JOptionPane.INFORMATION_MESSAGE);
		
	}
	private void nick_list(String msg) {
		// TODO Auto-generated method stub
			String name=msg.substring(13);//获得接收方的名字
			String[] na=name.split("\\|");//将名字拆分
			Vector<String> v=new Vector<String>();//获得在线列表
			for (int i = 0; i < na.length; i++) {
				if(na[i].trim().length()!=0&&(!na[i].trim().equals(father.jtfNickName.getText().trim()))){
					v.add(na[i]);
				}
			}
			father.jcbNickList.setModel(new DefaultComboBoxModel<String>(v));//设置下拉表的值
	}
	private void name_chongming() {
		// TODO Auto-generated method stub
		try{
		JOptionPane.showMessageDialog(this.father, "该名称已经被占用!请重新填写!!","错误",JOptionPane.ERROR_MESSAGE);
		din.close();
		dout.close();
		this.father.jtfHost.setEnabled(true);//设置各个控件
		this.father.jtfPort.setEnabled(true);
		this.father.jtfNickName.setEnabled(true);
		this.father.jbConnect.setEnabled(true);
		this.father.jbDisconnect.setEnabled(false);
		this.father.jbChallenge.setEnabled(false);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		father.sc.close();father.sc=null;//关闭socket，
		father.cat=null;//cat设为null
		flag=false;//终止客户端代理线程
		}catch(IOException e){e.printStackTrace();}
	}
	
}
