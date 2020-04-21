package com.example.pczzim;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity implements LocationListener {
//시작폼
	private Button but_name,but_loc,but_join,but_login;
	private EditText edit_id,edit_pw,edit_pc;
	public boolean login=false;
	private BackPressHandler backPressHandler;
	private LinearLayout ln_login,ln_logedin;
	SocketLink socket;
	public String id;
	private String[] recv;
	LocationManager locManager;
	private double lat,lng;
	Geocoder geoCoder;
	final Handler receivingHandle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        backPressHandler = new BackPressHandler(this);
        socket = new SocketLink(getApplicationContext());
		socket.connect();
        but_name = (Button)findViewById(R.id.but_name);
        but_loc = (Button)findViewById(R.id.but_loc);
        but_join = (Button)findViewById(R.id.but_join);
        but_login = (Button)findViewById(R.id.but_login);
        edit_id = (EditText)findViewById(R.id.edit_id);
        edit_pw=(EditText)findViewById(R.id.edit_pw);
        edit_pc=(EditText)findViewById(R.id.edit_pcname);
        ln_login = (LinearLayout)findViewById(R.id.Layout_Login);
        ln_logedin = (LinearLayout)findViewById(R.id.Layout_logedin);
        check_login();
        locManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER ,1000, 0,this);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER ,0, 0,this);
        geoCoder = new Geocoder(this, Locale.KOREAN); 
        
        //지역으로 검색하는 버튼
        but_loc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recv=null;
				
				//위도 +- 0.01, 경도 +- 0.01 기준으로 내부 피시방은 다 가져온다
				String query = "Select primenum,name_room,seatnum,loc_x,loc_y from owner where loc_x between "+ Double.toString(lat-0.01)+" and "+Double.toString(lat+0.01);
				//쿼리의 리턴값을 받아오는 쓰레드
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
									{//검색 결과가 없을때
										recv = new String[socket.recv.length];
										recv = socket.recv;
										if(recv[0].contains("null"))
										{
											edit_pc.setText("");
											edit_pc.setFocusable(true);
											Toast toast = Toast.makeText(getApplicationContext(),
									                "검색 결과가 없습니다.", Toast.LENGTH_SHORT);
									        toast.show();
										}else
										{//있을 때 새  액티비티를 열어줌

											Intent intent = new Intent(StartActivity.this,SearchActivity.class);
											intent.putExtra("recv", recv);
											intent.putExtra("lat", lat);
											intent.putExtra("lng", lng);
											socket.disconnect();
							    			startActivity(intent);
							    			overridePendingTransition(R.anim.left_in, R.anim.left_out); 
							    			finish();	
										}
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
        
        //이름으로 검색하는 버튼
        but_name.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			recv=null;
    			String name = edit_pc.getText().toString();
    			//edit_pc에 쓰여진 내용이 들어있는 pc방을 다 불러옴
    			String query = "Select primenum,name_room,seatnum,loc_x,loc_y from owner where name_room like '%"+name+"%'";
    			
    			//쿼리의 리턴값을 받아오는 쓰레드
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
									{//검색 결과가 없을 떄
										recv = new String[socket.recv.length];
										recv = socket.recv;
										Log.d("rcv",recv[0]);
										if(recv[0].contains("null"))
										{
											edit_pc.setText("");
											edit_pc.setFocusable(true);
											Toast toast = Toast.makeText(getApplicationContext(),
									                "검색 결과가 없습니다.", Toast.LENGTH_SHORT);
									        toast.show();
										}else
										{//있을 때 새로운 액티비티를 실행시킨다

											Intent intent = new Intent(StartActivity.this,SearchActivity.class);
											intent.putExtra("recv", recv);
											intent.putExtra("lat", lat);
											intent.putExtra("lng", lng);
											socket.disconnect();
							    			startActivity(intent);
							    			overridePendingTransition(R.anim.left_in, R.anim.left_out); 
							    			finish();	
										}
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
        
        //로그인 버튼
        but_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String _id = edit_id.getText().toString();
				String _pw = edit_pw.getText().toString();
				String query = "Select ID from custom where ID = '"+_id+"' and PW = '"+_pw+"'";
				
				recv=null;
				
				//값 받아오는 쓰레드
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
										if(recv[0].contains("null"))
										{
											edit_id.setText("");
											edit_pw.setText("");
											edit_id.setFocusable(true);
											Toast toast = Toast.makeText(getApplicationContext(),
									                "로그인 실패.\n다시 로그인 해 주세요.", Toast.LENGTH_SHORT);
									        toast.show();
										}else
										{
											login=true;
											id=_id;
											check_login();
											Toast toast = Toast.makeText(getApplicationContext(),
									                _id+" 님 환영합니다.", Toast.LENGTH_SHORT);
									        toast.show();
											
										}
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
        
        //회원가입 버튼으로 넘어감
        but_join.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			Intent intent = new Intent(StartActivity.this,JoinActivity.class);
    			socket.disconnect();
    			startActivity(intent);
    			
    			overridePendingTransition(R.anim.top_out, R.anim.top_in); 
    			finish();

    		}
    	});
        
        
    }

    
    //로그인 여부에 따라 검색창을 보여줄지 로그인창을 보여줄지 결정함
    private void check_login()
    {
    	if (login)
    	{
    		ln_logedin.setVisibility(View.VISIBLE);
    		ln_logedin.setEnabled(true);
    		TextView text_name = (TextView)findViewById(R.id.text_name);
    		text_name.setText(id);
    		ln_login.setVisibility(View.INVISIBLE);
    		ln_login.setEnabled(false);

    	}else
    	{
    		ln_login.setVisibility(View.VISIBLE);
    		ln_login.setEnabled(true);
    		ln_logedin.setVisibility(View.INVISIBLE);
    		ln_logedin.setEnabled(false);
    	}
    }
    
	@Override
	public void onBackPressed() {
	    backPressHandler.onBackPressed();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		lat =location.getLatitude();
		lng = location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
    
}
