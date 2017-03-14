package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread {

	Server father;
	Socket sc;
	DataInputStream din;//������
	DataOutputStream dout;//�����
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
			String name=msg.substring(11);//��ý��շ�������
			String[] na=name.split("\\|");
			Vector<ServerAgentThread> tempv=father.onlineList;//��������б�
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(na[0])){
					satTemp.dout.writeUTF(msg);//����Ϣת�����Է�
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void tiao_zhan(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(13);//��ý��շ�������
			String[] na=name.split("\\|");
			Vector<ServerAgentThread> tempv=father.onlineList;//��������б�
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(na[0])){
					satTemp.dout.writeUTF(msg);//����Ϣת�����Է�
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void ren_shu(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(11);//��ý��շ�������
			String[] na=name.split("\\|");
			Vector<ServerAgentThread> tempv=father.onlineList;//��������б�
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(na[0])){
					satTemp.dout.writeUTF("<#REN_SHU#>"+na[1]);//����Ϣת�����Է�
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void move(String msg) {
		// TODO Auto-generated method stub
		try{
			//System.out.println(msg);
			
			String name=msg.substring(8,msg.length()-4);//��ý��շ�������
			Vector<ServerAgentThread> tempv=father.onlineList;//��������б�
			int size=tempv.size();
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				if(satTemp.getName().equals(name)){
					satTemp.dout.writeUTF(msg);//����Ϣת�����Է�
					break;
				}
			}
		}catch(IOException e){e.printStackTrace();	}
	}
	private void busy(String msg) {
		// TODO Auto-generated method stub
				try{
					String name=msg.substring(8);//��������ս�û�������
					String[] na=name.split("\\|");
					Vector<ServerAgentThread> tempv=father.onlineList;//��������б�
					int size=tempv.size();
					for (int i = 0; i < size; i++) {
						ServerAgentThread satTemp=tempv.get(i);
						if(satTemp.getName().equals(na[1])){
							satTemp.dout.writeUTF("<#BUSY#>"+na[0]);//����û���������æ����Ϣ
							break;
						}
					}
				}catch(IOException e){e.printStackTrace();	}
	}
	private void bu_tong_yi(String msg) {
		// TODO Auto-generated method stub
				try{
					String name=msg.substring(14);//��������ս�û�������
					String[] na=name.split("\\|");
					Vector<ServerAgentThread> tempv=father.onlineList;//��������б�
					int size=tempv.size();
					for (int i = 0; i < size; i++) {
						ServerAgentThread satTemp=tempv.get(i);
						if(satTemp.getName().equals(na[0])){
							satTemp.dout.writeUTF("<#BU_TONG_YI#>"+na[1]);//����û����ͶԷ��ܾ�����Ϣ
							break;
						}
					}
				}catch(IOException e){e.printStackTrace();	}
	}
	private void tong_yi(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(11);//��������ս�û�������
			String[] na=name.split("\\|");
			Vector<ServerAgentThread> tempv=father.onlineList;//��������б�
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
			Vector<ServerAgentThread> tempv=father.onlineList;//��������б�
			//String name=msg.substring(16);
			tempv.remove(this);//�Ƴ����û�
			int size=tempv.size();
			String nl="<#NICK_LIST#>";
			for (int i = 0; i < size; i++) {
				ServerAgentThread satTemp=tempv.get(i);
				//������ͻ��˷����û�������Ϣ
				satTemp.dout.writeUTF("<#MSG#>"+this.getName()+"������......");
				nl=nl+"|"+satTemp.getName();//��֯�µ������û��б� 
			}
			for (int i = 0; i < size; i++) {//�����µ��б���Ϣ���͵�ÿ���ͻ���
				ServerAgentThread satTemp=tempv.get(i);
				satTemp.dout.writeUTF(nl);//������Ϣ
			}
			this.flag=false;//��ֹ�÷����������߳�
			father.onlineList.remove(this);//�Ƴ����û�
			father.refreshList();//���·����������û��б�
		}catch(IOException e){e.printStackTrace();	}
	}
	private void nick_name(String msg) {
		// TODO Auto-generated method stub
		try{
			String name=msg.substring(13);//����û����ǳ�
			this.setName(name);
			Vector<ServerAgentThread> v=father.onlineList;//��������û���
			boolean isChongMing=false;//�Ƿ�������ʾ
			int size=v.size();//����û��б�Ĵ�С
			for (int i = 0; i <size; i++) {
				ServerAgentThread tempSat=v.get(i);
				if(tempSat.getName().equals(name)){
					isChongMing=true;
					break;
				}
			}
			if(isChongMing==true){//�������
				dout.writeUTF("<#NAME_CHONGMING#>");
				din.close();
				dout.close();
				sc.close();
				flag=false;
			}else{//���������
				v.add(this);//�����߳���ӵ������б�
				father.refreshList();//ˢ�·�����������Ϣ
				String nickListMsg="";//�����б��ǳ��ַ���
				size=v.size();//�б��С
				for (int i = 0; i < size; i++) {
					ServerAgentThread tempSat=(ServerAgentThread)v.get(i);//�õ������߳�
					nickListMsg=nickListMsg+"|"+tempSat.getName();//�������б�������֯���ַ���
				}
				nickListMsg="<#NICK_LIST#>"+nickListMsg;
				Vector<ServerAgentThread> tempv=father.onlineList;
				size=tempv.size();
				for (int i = 0; i <size; i++) {//���������б�
					ServerAgentThread satTemp=tempv.get(i);
					satTemp.dout.writeUTF(nickListMsg);
					if(satTemp!=this){
						satTemp.dout.writeUTF("<#MSG#>"+this.getName()+"������.....");
					}
				}
				
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
