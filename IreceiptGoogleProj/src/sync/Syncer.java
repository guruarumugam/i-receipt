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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Syncer {

	Date lastsync;
	List<iReceipt> update_rec_list;

	public Syncer(){

		update_rec_list=new ArrayList<iReceipt>();
	}

	public void sendSync(String account){
		sendSync(this.update_rec_list,account);

	}

	public void sendSync(List<iReceipt> list,String account){
		// Create a new HttpClient and Post Header

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://receipt1234.appspot.com/sync");

		// Add your data
		if(list==null)
			return;
		if(list.isEmpty())
			return;
		try{
			int i=0;
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
			bitmapOptions.inSampleSize = 3; 
			//send the size of the list
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("size", list.size()+""));
			for (iReceipt ireceipt : list){
				//add the receipt
				byte[] by=ireceipt.toByteArray();
				byte[]encoded=Base64.encode(by, Base64.DEFAULT);
				String str=new String(encoded,"ASCII");
				nameValuePairs.add(new BasicNameValuePair(i+"", str));
				//add receipt image
				if (ireceipt.getFilepath()!=null){
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					Bitmap bam = BitmapFactory.decodeFile(ireceipt.getFilepath(), bitmapOptions);
					bam.compress(Bitmap.CompressFormat.JPEG, 70, baos);
					byte[] b = baos.toByteArray();
					encoded=Base64.encode(b, Base64.DEFAULT);
					str=new String(encoded,"ASCII");
					nameValuePairs.add(new BasicNameValuePair(i+"_image", str));
					baos.close();
				}
				i++;
			}
			nameValuePairs.add(new BasicNameValuePair("account",String.valueOf(account)));
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
	public void addtoUpdateList(iReceipt r){
		update_rec_list.add(r);
	}
	public List<iReceipt> getUpdateList(){
		return update_rec_list;
	}

	public void deleteupdatelist(){
		update_rec_list.clear();
	}


}



