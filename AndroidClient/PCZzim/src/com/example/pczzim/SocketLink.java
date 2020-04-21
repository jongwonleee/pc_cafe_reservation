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
//���� Ŭ���̾�Ʈ ���α׷����� ���� �κ��� ����
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
		addr="192.168.1.11";//�̺κп��� ip�ּҸ� �Է����ش�
		port="3500";
	}
	@SuppressWarnings("deprecation")
	public void disconnect()//�� �κ��� ���۸��ؼ� �����ؿԴ�
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
	
	//������ ������ �κ��̴�
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
	
	//������ �����Ű�� �κ��̴�
	public Thread connecting = new Thread(){//�̺κе� ���۸��ؼ� ������ �� �κ��̴�
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

	//������ ������ ������ ���� �޾ƿ��� �κ��̴�
	 private Thread reciever = new Thread(){//�� �κе� ���۸����� ������ �� �κ������� ���� �������
	        public void run() {
	            try {
	                while (recieve) {
	                    // �Է� ��Ʈ������ �޽����� �д´�
	                	String temp = reader.readLine();    	
	                	try
	                	{
	                		if(temp!=null)
	                		{//�޾ƿ� �������� �迭�̸� �迭�� �޾ƿ��� �ƴϸ� �ϳ��� �޾ƿ�
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

