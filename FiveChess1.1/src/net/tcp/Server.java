package net.tcp;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.MediaSize.Other;

import org.omg.CORBA.PRIVATE_MEMBER;

import ChessGame.ChessGame1;
import util.CloseUtil;

public class Server {


public Server() throws UnknownHostException, IOException {
		super();
	}
private List <MyChannel> all=new ArrayList<MyChannel>();
	public static void main(String[] args) throws IOException  {
new Server().start();
		}
	public void start() throws IOException{
			ServerSocket server=new ServerSocket(7777);
while (true) {
	
			Socket client=server.accept();
			MyChannel channel=new MyChannel(client);
			all.add(channel);
			new Thread(channel).start();
}
	}
	class MyChannel implements Runnable{
private DataInputStream dis;
private DataOutputStream dos;
private boolean isRunning=true;
 public MyChannel(Socket client) {
	 try {
		dis=new DataInputStream(client.getInputStream());
		 dos=new DataOutputStream(client.getOutputStream());

	} catch (IOException e) {
		// TODO 自动生成的 catch 块
		//e.printStackTrace();
		isRunning=false;
		CloseUtil.closeAll(dos,dis);
		
		
		
		
	}
	 
	
	 
	
}
 private int receiveA(){
		 int a=0;
		 try {
			a=dis.readInt();
		
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			//e.printStackTrace();
			isRunning=false;
			CloseUtil.closeAll(dis);
			all.remove(this);
		}
		 return a;
		 
	 }
 private int receiveB(){
	 int b=0;
	 
	 try {
		b=dis.readInt();
	
	} catch (IOException e) {
		// TODO 自动生成的 catch 块
		//e.printStackTrace();
		isRunning=false;
		CloseUtil.closeAll(dis);
		all.remove(this);
	}
	 return b;
	 
 }

 private void send(int c){
	 
	if(c==0) {
		return;
	}
	 try {
		dos.writeInt(c);;
		dos.flush();
	} catch (IOException e) {
		// TODO 自动生成的 catch 块
		//e.printStackTrace();
		isRunning=false;
		CloseUtil.closeAll(dos);
	}
 }
 
 
private void sandOthers(){
	
	int a=this.receiveA();
	int b=this.receiveB();
	for(MyChannel other:all){
		if(other==this)
		{
				continue;
		}
		other.send(a);
		other.send(b);
	}
	

}
 
 
		@Override
		public void run() {
		while (isRunning) {
			
			sandOthers();
		}
		}
		
		
		
		
		
	}
	
	
	

}
