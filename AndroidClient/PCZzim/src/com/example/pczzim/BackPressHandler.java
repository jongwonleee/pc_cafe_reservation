package com.example.pczzim;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
//�ڷΰ��� �ι������� ������ ����� �κ�
//���� ���۸��ؼ� �����ؿԴ�
public class BackPressHandler {
 
    private long backKeyPressedTime = 0;
    private Toast toast;
 
    private Activity activity;
 
    public BackPressHandler(Activity context) {
        this.activity = context;
    }
 
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }
 
    public void showGuide() {
        toast = Toast.makeText(activity,
                "\'�ڷ�\'��ư�� �ѹ� �� �����ø� ����˴ϴ�.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
