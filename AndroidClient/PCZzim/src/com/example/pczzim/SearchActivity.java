package com.example.pczzim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;



public class SearchActivity extends FragmentActivity {
//검색된 내용을 보여주는 창

	public static Context con;
	private ListView lv;
	String[] recv;
	double lng,lat;
	double[] _lng,_lat;
	String[] sitlist;
	int prime[];
	String[] names;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		SearchLvAdapter adapter = new SearchLvAdapter();
		lv = (ListView)findViewById(R.id.list_search);
		lv.setAdapter(adapter);
		Intent intent = getIntent();
		recv=intent.getExtras().getStringArray("recv");
		lng=intent.getExtras().getDouble("lng");
		lat=intent.getExtras().getDouble("lat");
		int len = recv.length;
		
		//서버로 부터 받은 내용들을 파싱을 통해 잘라 리스트뷰에 넣어줌
		names = new String[len];
		String[] distance = new String[len];
		sitlist = new String[len];
		_lng=new double[len];
		_lat=new double[len];
		int[] sits = new int[len];
		prime = new int[len];
		for(int i=0;i<len;i++)
		{
			String temp = recv[i];
			prime[i] = Integer.parseInt(temp.substring(0, temp.indexOf('^')));//pc방의 고유번호
			temp = temp.substring( temp.indexOf('^')+1);
			names[i] = temp.substring(0, temp.indexOf('^'));//pc방 이름
			Log.d("par",names[i]);
			temp = temp.substring( temp.indexOf('^')+1);
			sitlist[i] = temp.substring(0,temp.indexOf('^'));//pc방 좌석정보
			int emptysit=0;
			for(int k=0;k<sitlist[i].length();k++)
			{
				if(sitlist[i].charAt(k)=='0')
					emptysit++;
			}
				
			sits[i]=emptysit;//pc방 여석 수
			temp = temp.substring( temp.indexOf('^')+1);
			_lat[i] = Double.parseDouble(temp.substring(0,temp.indexOf('^')));
			 _lng[i] = Double.parseDouble(temp.substring( temp.indexOf('^')+1,temp.lastIndexOf('^')));
			Location a=new Location("PointA");
			a.setLatitude(lat);
			a.setLongitude(lng);
			Location b= new Location("PointB");
			b.setLatitude(_lat[i]);
			b.setLongitude(_lng[i]);
			double dis = a.distanceTo(b);
			
			if(dis>1000)//pc방 까지와의 거리
			{
				dis = Math.round(dis/10)/100;
				distance[i] = Double.toString(dis)+"km";
			}else
			{
				dis = Math.round(dis);
				distance[i] = Double.toString(dis)+"m";
			}
		}
		for(int i=0;i<len;i++)//리스트뷰에 넣어주는 부분
		{
			adapter.add(names[i],distance[i],sits[i]);
		}
		
		lv.setOnItemClickListener(itemListViewListner);
		
		//초기화면으로 돌아가는 버튼
		ImageButton but = (ImageButton)findViewById(R.id.but_home);
		but.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(SearchActivity.this,StartActivity.class);
				startActivity(in);
				overridePendingTransition(R.anim.top_out, R.anim.top_in); 
				finish();
			}
		});

	}
	
	//리스트뷰 클릭시 버튼
	private OnItemClickListener itemListViewListner = new OnItemClickListener()
	{
	      public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
	        {
				Intent in = new Intent(SearchActivity.this,InfoActivity.class);
				in.putExtra("recv",recv[pos]);//클릭된 내용을 다음 액티비티로 보내줌
				startActivity(in);
				overridePendingTransition(R.anim.left_in, R.anim.left_out); 
				finish();
	        }
	 };


	
	
	
}
