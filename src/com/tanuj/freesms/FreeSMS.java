package com.tanuj.freesms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.widget.Toast;

public class FreeSMS
{
	public static void send(Context context, String username, String password, String phonenumber, String message)
	{
		if(username.equals("")||password.equals("")||phonenumber.equals("")||message.equals(""))
			Toast.makeText(context, "No field should be empty", Toast.LENGTH_SHORT).show();
		else
		{
			HttpPost post = null;
			List <NameValuePair> parameters;
			UrlEncodedFormEntity sendentity = null;
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = null;
	        StringBuilder stringbuilder = null;
	        BufferedReader reader = null;
	        String line;
	        String ID;
	        Matcher m;
	        
			//	Create the Login POST request
			post = new HttpPost("http://m.160by2.com/LoginCheck.asp?l=1&txt_msg=&mno=");
			parameters = new ArrayList <NameValuePair>();
			parameters.add(new BasicNameValuePair("txtUserName", username));  
	        parameters.add(new BasicNameValuePair("txtPasswd", password));
	        parameters.add(new BasicNameValuePair("RememberMe", "Yes"));
	        parameters.add(new BasicNameValuePair("cmdSubmit", "Login"));
	        
	        try
	        {
	        	sendentity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
	        	post.setEntity(sendentity);
	        }
	        catch(UnsupportedEncodingException e)
	        {
	        	Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	        
	        try
	        {
	        	response = client.execute(post);
	        	stringbuilder = new StringBuilder();  
		        reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));  
		        while ((line = reader.readLine()) != null)  
		            stringbuilder.append(line).append("\n");
	        }
	        catch(ClientProtocolException e)
	        {
	        	Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	        catch(IOException e)
	        {
	        	Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	        
	        line = stringbuilder.toString();
	        if(line.indexOf("<font size=\"1\" style=\"color:Red;\">Invalid password</font>") != -1)
	        {
	        	Toast.makeText(context, "Invalid password", Toast.LENGTH_SHORT).show();
	        }
	        else if(line.indexOf("<font size=\"1\" style=\"color:Red;\">Invalid username</font>") != -1)
	        {
	        	Toast.makeText(context, "Invalid username", Toast.LENGTH_SHORT).show();
	        }
	        else
	        {
		        m = Pattern.compile("[a-zA-Z_0-9]{40}").matcher(line);
		        m.find();
		        ID = m.group();	// User ID retrieved (Login Successful)
		        
		        //	Create sendMessage POST request
		        post = new HttpPost("http://m.160by2.com/SaveCompose.asp?l=1");
		        parameters.clear();
		        parameters.add((new BasicNameValuePair("txt_mobileno", phonenumber)));
		        parameters.add((new BasicNameValuePair("txt_msg", message)));
		        parameters.add((new BasicNameValuePair("cmdSend", "Send+SMS")));
		        parameters.add((new BasicNameValuePair("TID", "")));
		        parameters.add((new BasicNameValuePair("T_MsgId", "")));
		        parameters.add((new BasicNameValuePair("Msg", ID)));
		        
		        try
		        {
		        	sendentity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
		        	post.setEntity(sendentity);
		        }
		        catch(UnsupportedEncodingException e)
		        {
		        	Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		        }
		        
		        try
		        {
		        	response = client.execute(post);
		        	reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));  
		            while ((line = reader.readLine()) != null)  
		                stringbuilder.append(line).append("\n");
		        }
		        catch(ClientProtocolException e)
		        {
		        	Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		        }
		        catch(IOException e)
		        {
		        	Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		        }
		        
		        line = stringbuilder.toString();
		        if(line.indexOf("SMS Sent Successfully") != -1)
		        {
		        	Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
		        }
		        else
		        {
		        	Toast.makeText(context, "Message not sent", Toast.LENGTH_SHORT).show();
		        }
	        }
	        client.getConnectionManager().shutdown();
		}
	}
}
