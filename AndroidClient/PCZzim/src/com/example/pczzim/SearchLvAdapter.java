package com.example.pczzim;

import java.util.ArrayList;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//Ŀ���� ����Ʈ�� ���
//�⺻���� Ʋ�� ���ۿ��� ���������� �� ������� �ٲ��
public class SearchLvAdapter extends BaseAdapter {
    private ArrayList<String>   name_list;
    private ArrayList<String>   distance_list;
    private ArrayList<Integer>   sit_list;
     
    // ������, �̸��� �״�� �����Դ�
    public SearchLvAdapter() {
        name_list = new ArrayList<String>();
        distance_list = new ArrayList<String>();
        sit_list = new ArrayList<Integer>();
    }
    

 
    // ���� �������� ���� ����, �� �����ߴ�
    @Override
    public int getCount() {
        return name_list.size();
    }
 
 
    // ������ position�� ID �� ����, �ٺ����ߴ�
    @Override
    public long getItemId(int position) {
        return position;
    }
    
 
    // ��� �� ������ ����, ���̹ٲ��
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        
        // ����Ʈ�� ������鼭 ���� ȭ�鿡 ������ �ʴ� �������� converView�� null�� ���·� ��� ��
        if ( convertView == null ) {
            // view�� null�� ��� Ŀ���� ���̾ƿ��� ��� ��
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_lv, parent, false);
        }
        //������� ����
        
        
        TextView text_pc = (TextView)convertView.findViewById(R.id.text_pc);
        TextView text_sit = (TextView)convertView.findViewById(R.id.text_sit);
        TextView text_dis = (TextView)convertView.findViewById(R.id.text_dis);
        
        text_pc.setText(name_list.get(pos));
        text_dis.setText(distance_list.get(pos));
        text_sit.setText(Integer.toString(sit_list.get(pos)));

        return convertView;
    }



    //�̸��� �����ؿԴ�
	public void add(String name,String distance, int sit) {
        name_list.add(name);
        sit_list.add(sit);
        distance_list.add(distance);
    }
     
    // �ܺο��� ������ ���� ��û �� ���,���� �߰����
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