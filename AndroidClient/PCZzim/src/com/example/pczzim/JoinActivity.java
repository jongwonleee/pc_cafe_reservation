package com.example.pczzim;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends Activity {
	
	SocketLink socket;
	boolean repeatence = false;
	public String[] recv;
	int qnum;
	EditText edit_phone;
	EditText edit_name;
	EditText edit_pw;
	EditText edit_pw_r;
	EditText edit_email;
	EditText edit_id;
	final Handler receivingHandle = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		socket = new SocketLink(getApplicationContext());
		Button but_exit = (Button)findViewById(R.id.but_exit);
		Button but_join = (Button)findViewById(R.id.but_join);
		Button but_rep = (Button)findViewById(R.id.but_rep);
		
		edit_phone = (EditText)findViewById(R.id.edit_phone);
		edit_pw = (EditText)findViewById(R.id.edit_pw);
		edit_pw_r = (EditText)findViewById(R.id.edit_pw_repeat);
		edit_email = (EditText)findViewById(R.id.edit_email);
		edit_id = (EditText)findViewById(R.id.edit_id);
		socket.connect();
		recv=new String[1];
		recv[0]="!";
		
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		edit_phone.setText( mTelephonyMgr.getLine1Number());
		
		but_exit.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
    			Intent intent = new Intent(JoinActivity.this,StartActivity.class);
 
    			socket.disconnect();
    			startActivity(intent);
    			overridePendingTransition(R.anim.left_in, R.anim.left_out); 
    			finish();
			}
		});
		//id 중복 검사 버튼 이벤트
		but_rep.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 String id = edit_id.getText().toString();
				 String query = "Select ID from custom where ID = '"+id+"'";
				 recv=null;
				 
				qnum=1;
				Thread getanswer = new Thread(new Runnable()
				 {
					 @Override
					 public void run(){
						 receivingHandle.post(new Runnable(){
							 @Override
							 public void run(){
								while(recv==null)
								{
							        if(socket.recv!=null)
							        {
							    	    recv = socket.recv;
							    	    if(qnum==1)
							    	    {
								   	       	check_rep();
								   	    }else if(qnum==2)
								   	       	check_join();
								   	}
								 }
							 }				 	
						 });
					 }
				 });
				getanswer.start();
				socket.send(query);
			}
		});
		
		//회원가입 버튼 이벤트
		but_join.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String id,name,pw,email,phone;
				if(repeatence)
				{
					id = edit_id.getText().toString();
					String pwr=edit_pw_r.getText().toString();
					pw = edit_pw.getText().toString();
					if(pw.equals(pwr))
					{
						email = edit_email.getText().toString();
						phone = edit_phone.getText().toString();
						qnum=2;
						String query = "Insert into custom (ID,PW,email,phonenum) values ('"+id+"','"+pw+"','"+email+"','"+phone+"')";
						recv=null;
						Thread getanswer = new Thread(new Runnable()
						 {
							 @Override
							 public void run(){
								 receivingHandle.post(new Runnable(){
									 @Override
									 public void run(){
										while(recv==null)
										{
									        if(socket.recv!=null)
									        {
									    	    recv = socket.recv;
									    	    if(qnum==1)
									    	    {
										   	       	check_rep();
										   	    }else if(qnum==2)
										   	       	check_join();
										   	}
										 }
									 }				 	
								 });
							 }
						 });
						getanswer.start();
						socket.send(query);
						
					}else
					{
						Toast toast = Toast.makeText(getApplicationContext(),
				                "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT);
						toast.show();
						edit_pw.setText("");
						edit_pw_r.setText("");
						edit_pw.setFocusable(true);
					}
				}		
				else
				{
					Toast toast = Toast.makeText(getApplicationContext(),
			                "중복확인을 해주세요.", Toast.LENGTH_SHORT);
			        toast.show();
			        edit_id.setFocusable(true);
				}
					
			}
		});
		
	}
	
	//쿼리문을 보낸 후 값을 받아오는 쓰레드임
	 private Thread returning = new Thread(new Runnable()
	 {
		 @Override
		 public void run(){
			 receivingHandle.post(new Runnable(){
				 @Override
				 public void run(){
					while(recv==null)
					{
				        if(socket.recv!=null)
				        {
				    	    recv = socket.recv;
				    	    if(qnum==1)
				    	    {
					   	       	check_rep();
					   	    }else if(qnum==2)
					   	       	check_join();
					   	}
					 }
				 }				 	
			 });
		 }
	 });
	 
	 //아이디 중복 체크 함수
	 private void check_rep()
	 {
			if(recv[0].contains("null"))
			{
				Toast toast = Toast.makeText(getApplicationContext(),
		                "사용 가능한 ID입니다.", Toast.LENGTH_SHORT);
		        toast.show();
		        repeatence=true;
		        edit_pw.setFocusable(true);
			}else
			{
				Toast toast = Toast.makeText(getApplicationContext(),
		                "중복된 ID입니다.", Toast.LENGTH_SHORT);
		        toast.show();
		        edit_id.setText("");
		        edit_id.setFocusable(true);
			}
	 }
	 
	 //회원가입 완료 시 함수
	 private void check_join()
	 {
		Intent intent = new Intent(JoinActivity.this,StartActivity.class);	
		startActivity(intent);
		socket.disconnect();
		overridePendingTransition(R.anim.left_in, R.anim.left_out); 
		finish();
	 }
}
