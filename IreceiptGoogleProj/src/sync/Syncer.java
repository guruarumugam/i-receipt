package sync;

import google.proj.iReceipt;
import google.proj.idan;
import google.proj.rec_view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Syncer {

	boolean alreadysync = false;
	private Date lastsync;
	List<iReceipt> update_rec_list;
	private String account = null;
	private List<String> delete_rec_list;

	public Syncer(String account) {
		update_rec_list = new ArrayList<iReceipt>();
		delete_rec_list = new ArrayList<String>();
		lastsync = new Date(100, 5, 5);
		this.setAccount(account);
	}

	public void sendSync() {
		sendSync(this.update_rec_list);
	}

	public void sendSync(List<iReceipt> list) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://ireceipt.assaf.in/sync");

		// Add your data
		if (list == null)
			return;
		// if (list.isEmpty()&&this.delete_rec_list.isEmpty())
		// return;
		try {
			int i = 0;
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = 3;
			// send the size of the list
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("size", list.size() + ""));
			for (iReceipt ireceipt : list) {
				// add the receipt
				byte[] by = ireceipt.toByteArray();
				byte[] encoded = Base64.encode(by, Base64.DEFAULT);
				String str = new String(encoded, "ASCII");
				nameValuePairs.add(new BasicNameValuePair(i + "", str));
				// add receipt image
				if (ireceipt.getFilepath() != null
						&& !ireceipt.getFilepath().equals("")) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					Bitmap bam = BitmapFactory.decodeFile(
							ireceipt.getFilepath(), bitmapOptions);
					bam.compress(Bitmap.CompressFormat.JPEG, 70, baos);
					byte[] b = baos.toByteArray();
					encoded = Base64.encode(b, Base64.DEFAULT);
					str = new String(encoded, "ASCII");
					nameValuePairs
							.add(new BasicNameValuePair(i + "_image", str));
					baos.close();
				}
				i++;
			}
			if (!this.delete_rec_list.isEmpty()) {
				nameValuePairs.add(new BasicNameValuePair("delete",
						this.delete_rec_list.size() + ""));
				for (int j = 0; j < this.delete_rec_list.size(); j++) {
					nameValuePairs.add(new BasicNameValuePair("delete" + j,
							this.delete_rec_list.get(j)));
				}
			}
			nameValuePairs.add(new BasicNameValuePair("account", String
					.valueOf(account)));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			// /////////////////////////////////////////////////////////////
			int size = 0;
			if (response.getHeaders("size") != null
					&& response.getHeaders("size").length != 0) {
				size = Integer.parseInt(response.getHeaders("size")[0]
						.getValue());
			}
			int delete;
			if (response.getHeaders("delete").length == 0
					|| response.getHeaders("delete")[0].getValue() == null)
				delete = 0;
			else
				delete = Integer.parseInt(response.getHeaders("delete")[0]
						.getValue());
			int delete_counter = 0;
			for (int j = 0; j < Math.max(size, delete); j++) {
				String in;
				if (response.getHeaders(j + "").length == 0)
					in = null;
				else
					in = response.getHeaders(j + "")[0].getValue();

				iReceipt rec = null;
				if (in != null) {
					byte[] decoded = Base64.decode(in.getBytes("ASCII"),
							Base64.DEFAULT);
					rec = new iReceipt();
					rec.fromByteArray(decoded);
				}
				String delete_in;
				if (response.getHeaders("delete" + delete_counter).length == 0)
					delete_in = null;
				else
					delete_in = response.getHeaders("delete" + delete_counter)[0]
							.getValue();
				for (iReceipt rr : idan.rec_arr) {
					if (rec != null) {
						if (rr.getUniqueIndex() == rec.getUniqueIndex()) { // same
							// receipt\
							if (rec.getUpdate().after(rr.getUpdate())) {
								rr.setCategory(rec.getCategory());
								rr.setFlaged(rec.isFlaged());
								rr.setNotes(rec.getNotes());
								rr.setProcessed(rec.isProcessed());
								rr.setRdate(rec.getRdate());
								rr.setStoreName(rec.getStoreName());
								rr.setTotal(rec.getTotal());
								rr.setUpdate(rec.getUpdate());
								rr.setSync(rec.getSyncdate());
							}
						}

					}
					if (delete_in != null) {
						if ((rr.getUniqueIndex() + "").equals(delete_in)) {
							idan.rec_arr.remove(rr);
							delete_counter++;
						}
					}
				}
			}
			// //////////////////////////////////
			// delete receipts

			lastsync = new Date();
			this.clearDelete_rec_list();
			this.clearUpdateList();
			alreadysync = true;

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addtoUpdateList(iReceipt r) {
		update_rec_list.add(r);
	}

	public List<iReceipt> getUpdateList() {
		return update_rec_list;
	}

	public void clearUpdateList() {
		update_rec_list.clear();
	}

	public boolean needtoSync() {
		Date d1 = new Date();
		long l1 = d1.getTime();
		long l2 = lastsync.getTime();
		if ((l1 - l2) >= 86400000) {
			lastsync = new Date();
			return true;
		} else
			return false;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	public Date getLastsync() {
		return lastsync;
	}

	public void setLastsync(Date lastsync) {
		this.lastsync = lastsync;
	}

	public void setDelete_rec_list(List<String> delete_rec_list) {
		this.delete_rec_list = delete_rec_list;
	}

	public List<String> getDelete_rec_list() {
		return delete_rec_list;
	}

	public void clearDelete_rec_list() {
		this.delete_rec_list.clear();
	}

	public void addToDeleteList(String id) {
		delete_rec_list.add(id);
	}
}