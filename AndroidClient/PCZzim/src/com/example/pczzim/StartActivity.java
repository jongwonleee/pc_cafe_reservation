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
//������
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
        
        //�������� �˻��ϴ� ��ư
        but_loc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recv=null;
				
				//���� +- 0.01, �浵 +- 0.01 �������� ���� �ǽù��� �� �����´�
				String query = "Select primenum,name_room,seatnum,loc_x,loc_y from owner where loc_x between "+ Double.toString(lat-0.01)+" and "+Double.toString(lat+0.01);
				//������ ���ϰ��� �޾ƿ��� ������
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
									{//�˻� ����� ������
										recv = new String[socket.recv.length];
										recv = socket.recv;
										if(recv[0].contains("null"))
										{
											edit_pc.setText("");
											edit_pc.setFocusable(true);
											Toast toast = Toast.makeText(getApplicationContext(),
									                "�˻� ����� �����ϴ�.", Toast.LENGTH_SHORT);
									        toast.show();
										}else
										{//���� �� ��  ��Ƽ��Ƽ�� ������

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
        
        //�̸����� �˻��ϴ� ��ư
        but_name.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			recv=null;
    			String name = edit_pc.getText().toString();
    			//edit_pc�� ������ ������ ����ִ� pc���� �� �ҷ���
    			String query = "Select primenum,name_room,seatnum,loc_x,loc_y from owner where name_room like '%"+name+"%'";
    			
    			//������ ���ϰ��� �޾ƿ��� ������
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
									{//�˻� ����� ���� ��
										recv = new String[socket.recv.length];
										recv = socket.recv;
										Log.d("rcv",recv[0]);
										if(recv[0].contains("null"))
										{
											edit_pc.setText("");
											edit_pc.setFocusable(true);
											Toast toast = Toast.makeText(getApplicationContext(),
									                "�˻� ����� �����ϴ�.", Toast.LENGTH_SHORT);
									        toast.show();
										}else
										{//���� �� ���ο� ��Ƽ��Ƽ�� �����Ų��

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
        
        //�α��� ��ư
        but_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String _id = edit_id.getText().toString();
				String _pw = edit_pw.getText().toString();
				String query = "Select ID from custom where ID = '"+_id+"' and PW = '"+_pw+"'";
				
				recv=null;
				
				//�� �޾ƿ��� ������
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
									                "�α��� ����.\n�ٽ� �α��� �� �ּ���.", Toast.LENGTH_SHORT);
									        toast.show();
										}else
										{
											login=true;
											id=_id;
											check_login();
											Toast toast = Toast.makeText(getApplicationContext(),
									                _id+" �� ȯ���մϴ�.", Toast.LENGTH_SHORT);
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
        
        //ȸ������ ��ư���� �Ѿ
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

    
    //�α��� ���ο� ���� �˻�â�� �������� �α���â�� �������� ������
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
