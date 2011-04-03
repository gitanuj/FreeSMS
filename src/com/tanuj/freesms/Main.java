package com.tanuj.freesms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener
{
	private Button b1;
	private EditText et1;
	private EditText et2;
	private EditText et3;
	private EditText et4;
	String username;
	String password;
	String phonenumber;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		b1 = (Button) findViewById(R.id.b1);
		et1 = (EditText)findViewById(R.id.et1);
		et2 = (EditText)findViewById(R.id.et2);
		et3 = (EditText)findViewById(R.id.et3);
		et4 = (EditText)findViewById(R.id.et4);
		b1.setTag("b1");
		b1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		username = et1.getText().toString();
		password = et2.getText().toString();
		phonenumber = et3.getText().toString();
		message = et4.getText().toString();
		
		FreeSMS.send(getApplicationContext(), username, password, phonenumber, message);
	}
	
}
