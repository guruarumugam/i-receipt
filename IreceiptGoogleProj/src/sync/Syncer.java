package sync;

import google.proj.iReceipt;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Base64;

public class Syncer {

	Date lastsync;


	public void sendSync(List<iReceipt> list){
		// Create a new HttpClient and Post Header
		
		
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://87.69.168.154:8888/sync");

		// Add your data
		if(list==null)
			return;
		if(list.isEmpty())
			return;
		try{
			int i=0;
			//send the size of the list
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("size", list.size()+""));
			for (iReceipt ireceipt : list)
				nameValuePairs.add(new BasicNameValuePair(i+"", Base64.encodeToString(ireceipt.toByteArray(),Base64.DEFAULT)));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			lastsync=new Date();


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();				

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();}





	}


}



