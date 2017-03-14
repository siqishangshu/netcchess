package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
	Server father;
	ServerSocket ss;
	boolean flag=true;
	public ServerThread(Server father) {
		// TODO Auto-generated constructor stub
		this.father=father;
		ss=father.ss;
	}
	public void run(){
		while(flag){
			try{
				Socket sc=ss.accept();
				ServerAgentThread sat=new ServerAgentThread(father,sc);
				sat.start();
			}catch(Exception e){
				flag=false;
				}
		}
	}
}
