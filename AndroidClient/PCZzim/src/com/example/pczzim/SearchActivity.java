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
//�˻��� ������ �����ִ� â

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
		
		//������ ���� ���� ������� �Ľ��� ���� �߶� ����Ʈ�信 �־���
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
			prime[i] = Integer.parseInt(temp.substring(0, temp.indexOf('^')));//pc���� ������ȣ
			temp = temp.substring( temp.indexOf('^')+1);
			names[i] = temp.substring(0, temp.indexOf('^'));//pc�� �̸�
			Log.d("par",names[i]);
			temp = temp.substring( temp.indexOf('^')+1);
			sitlist[i] = temp.substring(0,temp.indexOf('^'));//pc�� �¼�����
			int emptysit=0;
			for(int k=0;k<sitlist[i].length();k++)
			{
				if(sitlist[i].charAt(k)=='0')
					emptysit++;
			}
				
			sits[i]=emptysit;//pc�� ���� ��
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
			
			if(dis>1000)//pc�� �������� �Ÿ�
			{
				dis = Math.round(dis/10)/100;
				distance[i] = Double.toString(dis)+"km";
			}else
			{
				dis = Math.round(dis);
				distance[i] = Double.toString(dis)+"m";
			}
		}
		for(int i=0;i<len;i++)//����Ʈ�信 �־��ִ� �κ�
		{
			adapter.add(names[i],distance[i],sits[i]);
		}
		
		lv.setOnItemClickListener(itemListViewListner);
		
		//�ʱ�ȭ������ ���ư��� ��ư
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
	
	//����Ʈ�� Ŭ���� ��ư
	private OnItemClickListener itemListViewListner = new OnItemClickListener()
	{
	      public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
	        {
				Intent in = new Intent(SearchActivity.this,InfoActivity.class);
				in.putExtra("recv",recv[pos]);//Ŭ���� ������ ���� ��Ƽ��Ƽ�� ������
				startActivity(in);
				overridePendingTransition(R.anim.left_in, R.anim.left_out); 
				finish();
	        }
	 };


	
	
	
}
