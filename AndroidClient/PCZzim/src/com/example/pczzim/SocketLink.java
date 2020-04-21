package com.example.pczzim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
//소켓 클라이언트 프로그램으로 따온 부분이 많음
public class SocketLink {
	
	public String[] recv;
	private boolean con = false;
	private boolean recieve = false;
	private String addr;
	private String port;
	
	private Socket socket=null;
	private Context cnt;
	private BufferedReader reader;
	private BufferedWriter writer;

	public SocketLink (Context _cnt)
	{
		cnt = _cnt;
		addr="192.168.1.11";//이부분에서 ip주소를 입력해준다
		port="3500";
	}
	@SuppressWarnings("deprecation")
	public void disconnect()//이 부분은 구글링해서 복사해왔다
	{
		try{
			if(socket!=null)
			{
				recieve=false;
				Log.d("tag","Disconnected");
				socket.close();
				socket=null;
			}
		}catch(Exception e){
			Log.d("tag","Socket close error");
		}
	}
	
	public void setSocket(String _addr,String _port)
	{
		addr=_addr;
		port=_port;
	}
	
	public void connect()
	{
		con=true;
		connecting.start();
	}
	
	//서버에 보내는 부분이다
	public void send(String _send)
	{
		if(recieve){
			try{
				Log.d("rcv","1");
				PrintWriter out = new PrintWriter(writer,true);
				Log.d("rcv","2");
				out.println(_send);
				Log.d("rcv","3");
				recv=null;
				Log.d("rcv","JOJOJ");
			}catch(Exception e){
				Log.d("snd",e.toString());
			}
		}
	}
	
	//서버에 연결시키는 부분이다
	public Thread connecting = new Thread(){//이부분도 구글링해서 복사해 온 부분이다
		public void run(){
			while(con)
			{
				try{
					disconnect();
					int nPort = Integer.parseInt(port);
					
					socket = new Socket(addr,nPort);
					
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					recieve = true;
					Log.d("tag","connection success");
					reciever.start();
					
					con = false;
				}catch(IOException e){
					Log.d("tag",e.toString());
					
				}catch(Exception e){
					Log.d("tag",e.toString());
				}
			}
		}
	};

	//서버로 정보를 보내고 값을 받아오는 부분이다
	 private Thread reciever = new Thread(){//이 부분도 구글링에서 복사해 온 부분이지만 많이 수정됬다
	        public void run() {
	            try {
	                while (recieve) {
	                    // 입력 스트림에서 메시지를 읽는다
	                	String temp = reader.readLine();    	
	                	try
	                	{
	                		if(temp!=null)
	                		{//받아온 변수들이 배열이면 배열로 받아오고 아니면 하나만 받아옴
	                			Log.d("rcv",temp);
		                		int num = Integer.parseInt(temp);
		                		String[] tmp  = new String[num];
		                		for(int i=0;i<num;i++)
		                		{
		                			tmp[i] = reader.readLine();
		                			Log.d("rcv",tmp[i]);
		                		}
		                		recv = new String[num];
		                		recv = tmp;
	                		}else
	                		{
	                			Log.d("rcv","no recieved data");
	                		}
	                		
	                	}
	                	catch(NumberFormatException e)
	                	{
	                		recv = new String[1];
	                		recv[0] = temp;
	                	}
	                	catch(NullPointerException e)
	                	{
	                		Log.d("rcv",e.toString());
	                	}
	                    
	                }
	            } catch (Exception e) {
	                Log.d("tag", e.toString());
	            }
	        }	 
	 };
}

