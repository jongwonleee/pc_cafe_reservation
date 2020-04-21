package com.example.pczzim;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends FragmentActivity {
	//pc방의 상세정보를 보여주는 창
	private GoogleMap map;
	private boolean[] res = new boolean[50];
	private int[] ownsit = new int[50];
	TextView text_exp;
	final Handler receivingHandle = new Handler();
	TextView text_info;
	SocketLink socket;
	CheckBox[] sit = new CheckBox[50];
	String recv;
	String name;
	int prime;
	double lng,lat;
	String[] rc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		text_exp = (TextView)findViewById(R.id.text_exp);
		Intent in = getIntent();
		recv = in.getExtras().getString("recv");
		socket = new SocketLink(getApplicationContext());
		//socket.setSocket("172.30.1.", "3500");
		socket.connect();
		text_info = (TextView)findViewById(R.id.text_info);
		for(int i=0;i<50;i++)
			res[i]=false;
		
		sit[0]=(CheckBox)findViewById(R.id.sit1);
		sit[1]=(CheckBox)findViewById(R.id.sit2);
		sit[2]=(CheckBox)findViewById(R.id.sit3);
		sit[3]=(CheckBox)findViewById(R.id.sit4);
		sit[4]=(CheckBox)findViewById(R.id.sit5);
		sit[5]=(CheckBox)findViewById(R.id.sit6);
		sit[6]=(CheckBox)findViewById(R.id.sit7);
		sit[7]=(CheckBox)findViewById(R.id.sit8);
		sit[8]=(CheckBox)findViewById(R.id.sit9);
		sit[9]=(CheckBox)findViewById(R.id.sit10);
		sit[10]=(CheckBox)findViewById(R.id.sit11);
		sit[11]=(CheckBox)findViewById(R.id.sit12);
		sit[12]=(CheckBox)findViewById(R.id.sit13);
		sit[13]=(CheckBox)findViewById(R.id.sit14);
		sit[14]=(CheckBox)findViewById(R.id.sit15);
		sit[15]=(CheckBox)findViewById(R.id.sit16);
		sit[16]=(CheckBox)findViewById(R.id.sit17);
		sit[17]=(CheckBox)findViewById(R.id.sit18);
		sit[18]=(CheckBox)findViewById(R.id.sit19);
		sit[19]=(CheckBox)findViewById(R.id.sit20);
		sit[20]=(CheckBox)findViewById(R.id.sit21);
		sit[21]=(CheckBox)findViewById(R.id.sit22);
		sit[22]=(CheckBox)findViewById(R.id.sit23);
		sit[23]=(CheckBox)findViewById(R.id.sit24);
		sit[24]=(CheckBox)findViewById(R.id.sit25);
		sit[25]=(CheckBox)findViewById(R.id.sit26);
		sit[26]=(CheckBox)findViewById(R.id.sit27);
		sit[27]=(CheckBox)findViewById(R.id.sit28);
		sit[28]=(CheckBox)findViewById(R.id.sit29);
		sit[29]=(CheckBox)findViewById(R.id.sit30);
		sit[30]=(CheckBox)findViewById(R.id.sit31);
		sit[31]=(CheckBox)findViewById(R.id.sit32);
		sit[32]=(CheckBox)findViewById(R.id.sit33);
		sit[33]=(CheckBox)findViewById(R.id.sit34);
		sit[34]=(CheckBox)findViewById(R.id.sit35);
		sit[35]=(CheckBox)findViewById(R.id.sit36);
		sit[36]=(CheckBox)findViewById(R.id.sit37);
		sit[37]=(CheckBox)findViewById(R.id.sit38);
		sit[38]=(CheckBox)findViewById(R.id.sit39);
		sit[39]=(CheckBox)findViewById(R.id.sit40);
		sit[40]=(CheckBox)findViewById(R.id.sit41);
		sit[41]=(CheckBox)findViewById(R.id.sit42);
		sit[42]=(CheckBox)findViewById(R.id.sit43);
		sit[43]=(CheckBox)findViewById(R.id.sit44);
		sit[44]=(CheckBox)findViewById(R.id.sit45);
		sit[45]=(CheckBox)findViewById(R.id.sit46);
		sit[46]=(CheckBox)findViewById(R.id.sit47);
		sit[47]=(CheckBox)findViewById(R.id.sit48);
		sit[48]=(CheckBox)findViewById(R.id.sit49);
		sit[49]=(CheckBox)findViewById(R.id.sit50);
		for(int i=0;i<50;i++)
		{
			sit[i].setOnClickListener(sitClickListner);
		}
		setForm();
		
		//초기화면으로 돌아가는 버튼
		ImageButton but_home = (ImageButton)findViewById(R.id.but_home);
		but_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(InfoActivity.this,StartActivity.class);
				startActivity(in);
				overridePendingTransition(R.anim.top_out, R.anim.top_in); 
				finish();
			}
		});
		
		//예약 버튼
		Button but_res = (Button)findViewById(R.id.but_resv);
		but_res.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sit="";
				for(int i=0;i<50;i++)//눌린 정보들을 토대로 새로운 좌석 정보를 만듬
				{
					if(res[i]==true)
					{
						ownsit[i]=2;
					}
					sit=sit+Integer.toString(ownsit[i]);
				}
				rc=null;
				//예약이 되었는지를 받아오는 쓰레드
				Thread getanswer = new Thread(new Runnable()
				 {
					
					 @Override
					 public void run(){
						 receivingHandle.post(new Runnable(){
							 @Override
							 public void run(){
								while(rc==null)
								{
									if(socket.recv!=null)
									{
										rc=socket.recv;
										Toast toast = Toast.makeText(getApplicationContext(), "예약완료.", Toast.LENGTH_LONG);
										toast.setGravity(Gravity.CENTER,0,0);
										toast.show();
										Intent in = new Intent(InfoActivity.this,StartActivity.class);
										startActivity(in);
										overridePendingTransition(R.anim.top_out, R.anim.top_in); 
										finish();
									}
								}
							 }				 	
						 });
					 }
				 });
				getanswer.start();
				//새로운 좌석정보로 업데이트하는 쿼리문을 보냄
				String query = "Update owner set seatnum = '"+sit+"' where primenum = "+prime;
				socket.send(query);
			}
		});
		String query = "Select name_room,seatnum,loc_x,loc_y from owner where primenum = "+Integer.toString(prime);
		recv=null;
		socket.send(query);
	}
	private void setForm()
	{//서버에서 가져온 내용들로 폼 구성하는 함수
		
		String temp = recv;
		prime = Integer.parseInt(temp.substring(0, temp.indexOf('^')));
		temp = temp.substring( temp.indexOf('^')+1);
		name = temp.substring(0, temp.indexOf('^'));
		text_exp.setText(name);
		temp = temp.substring( temp.indexOf('^')+1);
		String sitlist = temp.substring(0,temp.indexOf('^'));
		
		//화면에 표시되는 좌석 정보 갱신
		for(int k=0;k<sitlist.length();k++)
		{
			if(sitlist.charAt(k)!='0')
			{
				sit[k].setEnabled(false);
				ownsit[k]=sitlist.charAt(k)-'0';
				
			}else
			{			
				ownsit[k]=0;
			}
				
		}
			
		
		temp = temp.substring( temp.indexOf('^')+1);
		lat = Double.parseDouble(temp.substring(0,temp.indexOf('^')));
		lng = Double.parseDouble(temp.substring( temp.indexOf('^')+1,temp.lastIndexOf('^')));
		
		//이부분은 구글맵 이용 부분으로 구글에서 거의 따옴
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		map= mapFragment.getMap();
		LatLng MAP_ADDRESS = new LatLng(lat,lng);
		CameraPosition cp = new CameraPosition.Builder().target(MAP_ADDRESS).zoom(17).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
		MarkerOptions optFirst = new MarkerOptions();
        optFirst.position(MAP_ADDRESS);
        optFirst.title(name);
        map.addMarker(optFirst).showInfoWindow();
	}
	
	//좌석 버튼 클릭
	private OnClickListener sitClickListner= new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			CheckBox sit = (CheckBox)v;
			
			int sit_no = Integer.parseInt(sit.getText().toString())-1;
			String info ="";
			if(sit.isChecked())
			{
				res[sit_no]=true;
			}else
			{
				res[sit_no]=false;	
			}
			
			//누른 좌석들 번호 출력
			for(int i=0;i<50;i++)
			{
				if(res[i])
				{
					info = info + Integer.toString(i+1)+ ", ";			
				}
			}
			if(info.length()!=0)
			{
				info = info.substring(0, info.length()-2);
			}
			text_info.setText(info);
		}
	};
	
	
}
