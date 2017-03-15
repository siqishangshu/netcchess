package cn.mxsic.netchess.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class ClientAgentThread extends Thread {
	public App father;
	public boolean flag=true;
	public DataInputStream din;
	public DataOutputStream dout;
	public String tiaoZhanZhe=null;
	public ClientAgentThread(App father) {
		this.father=father;
		try{
			din=new DataInputStream(father.sc.getInputStream());
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
				String msg=din.readUTF().trim();
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
			String name=msg.substring(11);
			String[] na=name.split("\\|");
			this.father.vformessage.add(na[1]);
			this.father.vformessage.add(na[2]);
			this.father.jlmessage.setListData(this.father.vformessage);
	}
	private void msg(String msg) {
		father.vformessage.add(msg.substring(7));
		father.jlmessage.setListData(father.vformessage);
	}
	private void ren_shu(String msg) {
		String name=msg.substring(11);
		this.tiaoZhanZhe=null;
		this.father.jlvs.setText("  palyone  VS  palytwo  ");
		JOptionPane.showMessageDialog(this.father, "你赢了!"+name+"认输了!!!","恭喜",JOptionPane.INFORMATION_MESSAGE);
		this.father.color=0;
		this.father.caiPan=false;
		this.father.next();
		this.father.jtfHost.setEnabled(false);
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
		int Sge,Sxian,Ege,Exian;
		String mess="";
		Sge=1+startJ;Sxian=1+startI;Ege=1+endJ;Exian=1+endI;
		if(name.equals("将")||name.equals("炮")||name.equals("卒")||name.equals("车")){
			if(Sxian==Exian){
					if(Sge<Ege){
						mess=tiaoZhanZhe+name+Sxian+"进"+(Ege-Sge);
					}else if(Sge>Ege){
						mess=tiaoZhanZhe+name+""+Sxian+"退"+(Sge-Ege);
					}
				}else
				if(Sge==Ege){
					   mess=tiaoZhanZhe+name+Sxian+"平"+Exian;
				}
			}
			if(name.equals("象")||name.equals("仕")||name.equals("马")){
				if(Sxian<Exian){
					mess=tiaoZhanZhe+name+Sxian+"进"+Exian;
				}else
					if(Sxian>Exian){
					mess=tiaoZhanZhe+name+Sxian+"退"+Exian;
				}
			}
		this.father.jtmove.setText("等待你走棋!!!");
		this.father.vformessage.add(mess);
		this.father.jlmessage.setListData(this.father.vformessage);
	}
	private void busy(String msg) {
		this.father.caiPan=false;
		this.father.color=0;
		this.father.jtfHost.setEnabled(false);
		this.father.jtfPort.setEnabled(false);
		this.father.jtfNickName.setEnabled(false);
		this.father.jbConnect.setEnabled(false);
		this.father.jbDisconnect.setEnabled(true);
		this.father.jbChallenge.setEnabled(true);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		this.father.jcbNickList.setEnabled(true);
		JOptionPane.showMessageDialog(this.father, msg+"对方正在对战中!!!","提示",JOptionPane.INFORMATION_MESSAGE);
		this.tiaoZhanZhe=null;
	}
	private void bu_tong_yi(String msg) {
		String name=msg.substring(14);
		this.father.caiPan=false;
		this.father.color=0;
		this.father.jtfHost.setEnabled(false);
		this.father.jtfPort.setEnabled(false);
		this.father.jtfNickName.setEnabled(false);
		this.father.jbConnect.setEnabled(false);
		this.father.jbDisconnect.setEnabled(true);
		this.father.jbChallenge.setEnabled(true);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		this.father.jcbNickList.setEnabled(true);
		JOptionPane.showMessageDialog(this.father, name+"对方拒绝了你的挑战!!!","提示",JOptionPane.INFORMATION_MESSAGE);
		this.tiaoZhanZhe=null;
	}
	private void tong_yi(String msg) {
		String name=msg.substring(11);
		this.father.jtfHost.setEnabled(false);
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
		JOptionPane.showMessageDialog(this.father, name+"对方!"+name+"接受了你的挑战!","提示",JOptionPane.INFORMATION_MESSAGE);
	}
	private void tiao_zhan(String msg) {
		try{
			String name=msg.substring(13);
			String[] na=name.split("\\|");
			if(this.tiaoZhanZhe==null){
				tiaoZhanZhe=na[1];//Ϊtiaozhenzhe
				this.father.jtfHost.setEnabled(false);
				this.father.jtfPort.setEnabled(false);
				this.father.jtfNickName.setEnabled(false);
				this.father.jbConnect.setEnabled(false);
				this.father.jbDisconnect.setEnabled(false);
				this.father.jbChallenge.setEnabled(false);
				this.father.jbYChallenge.setEnabled(true);
				this.father.jbNChallenge.setEnabled(true);
				this.father.jbFail.setEnabled(false);
				JOptionPane.showMessageDialog(this.father, na[1]+"向你发起了挑战!!!","提示",JOptionPane.INFORMATION_MESSAGE);
			}else{
				this.father.vformessage.add(na[1]+"向你发起挑战!!!");
				this.father.jlmessage.setListData(this.father.vformessage);
				this.dout.writeUTF("<#BUSY#>"+name);
			}
			}catch(IOException e){e.printStackTrace();}
	}
	private void server_down() {
		this.father.jtfHost.setEnabled(true);
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
		JOptionPane.showMessageDialog(this.father, "服务器被停机!!","提示",JOptionPane.INFORMATION_MESSAGE);
		
	}
	private void nick_list(String msg) {
			String name=msg.substring(13); 
			String[] na=name.split("\\|"); 
			Vector<String> v=new Vector<String>();
			for (int i = 0; i < na.length; i++) {
				if(na[i].trim().length()!=0&&(!na[i].trim().equals(father.jtfNickName.getText().trim()))){
					v.add(na[i]);
				}
			}
			father.jcbNickList.setModel(new DefaultComboBoxModel<String>(v));
	}
	private void name_chongming() {
		try{
		JOptionPane.showMessageDialog(this.father, "你使用的名称已经被占用!!","错误",JOptionPane.ERROR_MESSAGE);
		din.close();
		dout.close();
		this.father.jtfHost.setEnabled(true);
		this.father.jtfPort.setEnabled(true);
		this.father.jtfNickName.setEnabled(true);
		this.father.jbConnect.setEnabled(true);
		this.father.jbDisconnect.setEnabled(false);
		this.father.jbChallenge.setEnabled(false);
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		father.sc.close();father.sc=null;
		father.cat=null;
		flag=false;
		}catch(IOException e){e.printStackTrace();}
	}
	
}
