package net.rcelma.feb8_17_permissions;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.health.PackageHealthStats;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	private EditText etN;
	private EditText etT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etN = (EditText) findViewById(R.id.etN);
		etT = (EditText) findViewById(R.id.etT);
	}

	public void launchSMS(View view) {

		sendSMS();
	}

	private void sendSMS() {

		Integer permGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
		if (permGranted != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
				builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 3);
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			} else {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 3);
			}
		} else {
			doSendSMS();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 3:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage("phoneNo", null, "sms message", null, null);
					doSendSMS();
				} else {
					Toast.makeText(this, "NO PERMISSION T_T", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private void doSendSMS() {

		String phoneNo = etN.getText().toString();
		String message = etT.getText().toString();
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNo, null, message, null, null);
		Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
	}
}