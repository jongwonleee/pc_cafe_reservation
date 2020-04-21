package com.example.pczzim;

import java.util.ArrayList;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//커스텀 리스트뷰 어뎁터
//기본적인 틀은 구글에서 복사했지만 내 나름대로 바꿨다
public class SearchLvAdapter extends BaseAdapter {
    private ArrayList<String>   name_list;
    private ArrayList<String>   distance_list;
    private ArrayList<Integer>   sit_list;
     
    // 생성자, 이름만 그대로 가져왔다
    public SearchLvAdapter() {
        name_list = new ArrayList<String>();
        distance_list = new ArrayList<String>();
        sit_list = new ArrayList<Integer>();
    }
    

 
    // 현재 아이템의 수를 리턴, 다 복사했다
    @Override
    public int getCount() {
        return name_list.size();
    }
 
 
    // 아이템 position의 ID 값 리턴, 다복사했다
    @Override
    public long getItemId(int position) {
        return position;
    }
    
 
    // 출력 될 아이템 관리, 많이바뀌였다
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        
        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_lv, parent, false);
        }
        //여기까지 복사
        
        
        TextView text_pc = (TextView)convertView.findViewById(R.id.text_pc);
        TextView text_sit = (TextView)convertView.findViewById(R.id.text_sit);
        TextView text_dis = (TextView)convertView.findViewById(R.id.text_dis);
        
        text_pc.setText(name_list.get(pos));
        text_dis.setText(distance_list.get(pos));
        text_sit.setText(Integer.toString(sit_list.get(pos)));

        return convertView;
    }



    //이름만 복사해왔다
	public void add(String name,String distance, int sit) {
        name_list.add(name);
        sit_list.add(sit);
        distance_list.add(distance);
    }
     
    // 외부에서 아이템 삭제 요청 시 사용,조금 추가됬다
    public void remove(int _position) {
        name_list.remove(_position);
        sit_list.remove(_position);
        distance_list.remove(_position);
    }
    
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
}