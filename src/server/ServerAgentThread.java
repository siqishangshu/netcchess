package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread {

	Server father;
	Socket sc;
	DataInputStream din;//输入流
	DataOutputStream dout;//输出流
	boolean flag=true;
	public ServerAgentThread(Server father,Socket sc) {
		// TODO Auto-generated constructor stub
		this.father=father;
		this.sc=sc;
		try{
			din=new DataInputStream(sc.getInputStream());
			dout=new DataOutputStream(sc.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void run(){
		while(flag){
			try{
				String msg=din.readUTF().trim();
				if(msg.startsWith("<#NICK_NAME#>")){this.nick_name(msg);}
				else if(msg.startsWith("<#CLIENT_LEAVE#>")){this.client_leave(msg);}
				else if(msg.startsWith("<#TIAO_ZHAN#>")){this.tiao_zhan(msg);}
				else if(msg.startsWith("<#TONG_YI#>")){this.tong_yi(msg);}
				else if(msg.startsWith("<#BU_TONG_YI#>")){this.bu_tong_yi(msg);}
				else if(msg.startsWith("<#BUSY#>")){this.busy(msg);}
				else if(msg.startsWith("<#MOVE#>")){this.move(msg);}
				else if(msg.startsWith("<#REN_SHU#>")){this.ren_shu(msg);}
				else if(msg.startsWith("<#MESSAGE#>")){this.message(msg);}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	private void message(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(11);//获得接收方的名字
			String[] na=name.split("\\|");
			Vector<ServerAgentThread> tempv=father.onlineList;//获得在线列表
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(na[0])){
					satTemp.dout.writeUTF(msg);//将信息转发给对方
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void tiao_zhan(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(13);//获得接收方的名字
			String[] na=name.split("\\|");
			Vector<ServerAgentThread> tempv=father.onlineList;//获得在线列表
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(na[0])){
					satTemp.dout.writeUTF(msg);//将信息转发给对方
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void ren_shu(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(11);//获得接收方的名字
			String[] na=name.split("\\|");
			Vector<ServerAgentThread> tempv=father.onlineList;//获得在线列表
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(na[0])){
					satTemp.dout.writeUTF("<#REN_SHU#>"+na[1]);//将信息转发给对方
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void move(String msg) {
		// TODO Auto-generated method stub
		try{
			//System.out.println(msg);
			
			String name=msg.substring(8,msg.length()-4);//获得接收方的名字
			Vector<ServerAgentThread> tempv=father.onlineList;//获得在线列表
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(name)){
					satTemp.dout.writeUTF(msg);//将信息转发给对方
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void busy(String msg) {
		// TODO Auto-generated method stub
				try{
					String name=msg.substring(8);//获得提出挑战用户的名字
					String[] na=name.split("\\|");
					Vector<ServerAgentThread> tempv=father.onlineList;//获得在线列表
					int size=tempv.size();
					for (int i = 0; i < size; i++) {
						ServerAgentThread satTemp=tempv.get(i);
						if(satTemp.getName().equals(na[1])){
							satTemp.dout.writeUTF("<#BUSY#>"+na[0]);//向该用户发送正在忙的信息
							break;
						}
					}
				}catch(IOException e){e.printStackTrace();	}
	}
	private void bu_tong_yi(String msg) {
		// TODO Auto-generated method stub
				try{
					String name=msg.substring(14);//获得提出挑战用户的名字
					String[] na=name.split("\\|");
					Vector<ServerAgentThread> tempv=father.onlineList;//获得在线列表
					int size=tempv.size();
					for (int i = 0; i < size; i++) {
						ServerAgentThread satTemp=tempv.get(i);
						if(satTemp.getName().equals(na[0])){
							satTemp.dout.writeUTF("<#BU_TONG_YI#>"+na[1]);//向该用户发送对方拒绝的信息
							break;
						}
					}
				}catch(IOException e){e.printStackTrace();	}
	}
	private void tong_yi(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(11);//获得提出挑战用户的名字
			String[] na=name.split("\\|");
			Vector<ServerAgentThread> tempv=father.onlineList;//获得在线列表
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(na[0])){
					satTemp.dout.writeUTF("<#TONG_YI#>"+na[1]);
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void client_leave(String msg) {
		// TODO Auto-generated method stub
		try{
			Vector<ServerAgentThread> tempv=father.onlineList;//获得在线列表
			//String name=msg.substring(16);
			tempv.remove(this);//移除该用户
			int size=tempv.size();
			String nl="<#NICK_LIST#>";
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				//向各个客户端发送用户离线信息
				satTemp.dout.writeUTF("<#MSG#>"+this.getName()+"离线了......");
				nl=nl+"|"+satTemp.getName();//组织新的在线用户列表 
			}
			for (int i = 0; i < size; i++) {//将最新的列表信息发送到每个客户端
				ServerAgentThread satTemp=tempv.get(i);
				satTemp.dout.writeUTF(nl);//发送信息
			}
			this.flag=false;//终止该服务器代理线程
			father.onlineList.remove(this);//移除该用户
			father.refreshList();//更新服务器在线用户列表
		}catch(IOException e){e.printStackTrace();	}
	}
	private void nick_name(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(13);//获得用户的昵称
			this.setName(name);
			Vector<ServerAgentThread> v=father.onlineList;//获得在线用户表
			boolean isChongMing=false;//是否重名标示
			int size=v.size();//获得用户列表的大小
			for (int i = 0; i <size; i++) {
				ServerAgentThread tempSat=v.get(i);
				if(tempSat.getName().equals(name)){
					isChongMing=true;
					break;
				}
			}
			if(isChongMing==true){//如果重名
				dout.writeUTF("<#NAME_CHONGMING#>");
				din.close();
				dout.close();
				sc.close();
				flag=false;
			}else{//如果不重名
				v.add(this);//将该线程添加到在线列表
				father.refreshList();//刷新服务器在线信息
				String nickListMsg="";//在线列表昵称字符串
				size=v.size();//列表大小
				for (int i = 0; i < size; i++) {
					ServerAgentThread tempSat=(ServerAgentThread)v.get(i);//得到代理线程
					nickListMsg=nickListMsg+"|"+tempSat.getName();//将在线列表内容组织成字符串
				}
				nickListMsg="<#NICK_LIST#>"+nickListMsg;
				Vector<ServerAgentThread> tempv=father.onlineList;
				size=tempv.size();
				for (int i = 0; i <size; i++) {//遍历在线列表
					ServerAgentThread satTemp=tempv.get(i);
					satTemp.dout.writeUTF(nickListMsg);
					if(satTemp!=this){
						satTemp.dout.writeUTF("<#MSG#>"+this.getName()+"上线了.....");
					}
				}
				
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
