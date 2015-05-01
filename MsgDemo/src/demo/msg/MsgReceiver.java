package demo.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MsgReceiver extends BroadcastReceiver {

	private String TAG = "MY_TAG";
	
	@Override
	public void onReceive(Context context, Intent intent) {
        Log.v(TAG, ">>>>>>>onReceive start");
        
        //��ȡ���ŵ����ݺͷ�����
        StringBuilder body = new StringBuilder();			// ��������
        StringBuilder number = new StringBuilder();			// ���ŷ�����

        Bundle bundle = intent.getExtras();
        
        if (bundle != null) {
            Object[] _pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] message = new SmsMessage[_pdus.length];
            for (int i = 0; i < _pdus.length; i++) {
                message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
            }
            for (SmsMessage currentMessage : message) {
                body.append(currentMessage.getDisplayMessageBody());
                number.append(currentMessage.getDisplayOriginatingAddress());
            }
            
            String smsBody = body.toString();
            String smsNumber = number.toString();
            if (smsNumber.contains("+86")) {
                smsNumber = smsNumber.substring(3);
            }

            Toast.makeText(context, smsNumber, Toast.LENGTH_LONG).show();
            
			// ȡ���㲥����������������մ˹㲥
            this.abortBroadcast();
            
            // ����Activity.Main����Activity������Ϣ
            // ����ActivityΪSingleInstance��
            // ������� onPause-->onNewIntent-->onRestart-->onStart-->onResume
            Intent intent1 = new Intent();
            intent1.setClass(context, Main.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("phone", smsNumber);
            bundle1.putString("content", smsBody);
            intent1.putExtras(bundle1);
            context.startActivity(intent1);
        }
        
        Log.v(TAG, ">>>>>>>onReceive end");
    }
}
