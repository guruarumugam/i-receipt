package google.proj;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.util.DateTime;
import com.google.api.client.xml.atom.AtomParser;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;

@SuppressWarnings("unused")
public class loginpage extends Activity {
	
	private static final String PREF = "MyPrefs";
	private String authToken;
	private static final int DIALOG_ACCOUNTS = 0;
	private static final String AUTH_TOKEN_TYPE="mail";
	//private GoogleTransport transport; *****************************************************
	private static final int REQUEST_AUTHENTICATE = 0;
	protected static String  accountname;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loginpage);
		gotAccount(false);
		  }

	@Override
	  protected Dialog onCreateDialog(int id) {
	    switch (id) {
	      case DIALOG_ACCOUNTS:
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        
	        final AccountManager manager = AccountManager.get(this);
	        final Account[] accounts = manager.getAccountsByType("com.google");
	        final int size = accounts.length;
	        if (size!=0){
	        	builder.setTitle("Select a Google account");
	        String[] names = new String[size];
	        for (int i = 0; i < size; i++) {
	          names[i] = accounts[i].name;
	        }
	        builder.setItems(names, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int which) {
	            gotAccount(manager, accounts[which]);
	          }
	        });
	        return builder.create();
	    }
	        else {
	        	builder.setTitle("This application require google account");
	        	builder.setNeutralButton("Set google account",new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
					startActivityForResult((new Intent(Settings.ACTION_SYNC_SETTINGS)),5);
					}	
				});
	        	builder.setNegativeButton("Exit", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						finish();						
					}
				});
	        	return builder.create();
	        }
	    }
	    return null;
	  }

	  private void gotAccount(boolean tokenExpired) {
		    SharedPreferences settings = getSharedPreferences(PREF, 0);
		    String accountName = settings.getString("accountName", null);
		    if (accountName != null) {
		      AccountManager manager = AccountManager.get(this);
		      Account[] accounts = manager.getAccountsByType("com.google");
		      int size = accounts.length;
		      for (int i = 0; i < size; i++) {
		        Account account = accounts[i];
		        if (accountName.equals(account.name)) {
		          if (tokenExpired) {
		            manager.invalidateAuthToken("com.google", this.authToken);
		          }
		          gotAccount(manager, account);
		          return;
		        }
		      }
		    }
		    showDialog(DIALOG_ACCOUNTS);
		  }

	  private void gotAccount(final AccountManager manager, final Account account) {
		    SharedPreferences settings = getSharedPreferences(PREF, 0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString("accountName", account.name);
		    editor.commit();
		   
		        try {
		          final Bundle bundle =
		              manager.getAuthToken(account, AUTH_TOKEN_TYPE, true, null, null)
		                  .getResult();

		              try {
		                if (bundle.containsKey(AccountManager.KEY_INTENT)) {
		                  Intent intent =
		                      bundle.getParcelable(AccountManager.KEY_INTENT);
		                  int flags = intent.getFlags();
		                  flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;
		                  intent.setFlags(flags);
		                  startActivityForResult(intent, REQUEST_AUTHENTICATE);
		                } else if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
		                	accountname=account.name;
		                	authenticatedClientLogin(
		                      bundle.getString(AccountManager.KEY_AUTHTOKEN));
		                }
		              } catch (Exception e) {
		               // handleException(e);
		              }
		            
		        } catch (Exception e) {
		          //handleException(e);
		        }
		      }
	  
	  private void authenticatedClientLogin(String authToken) {
		    this.authToken = authToken;
		   // ((GoogleHeaders) transport.defaultHeaders).setGoogleLogin(authToken);
		    authenticated();
		  }

	private void authenticated() {		
		Intent i =new Intent(this,idan.class);
		startActivityForResult(i, 5);
	}
	
	 @Override
	  protected void onActivityResult(
	      int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	      case REQUEST_AUTHENTICATE:
	        if (resultCode == RESULT_OK) {
	          gotAccount(false);
	        } else {
	          showDialog(DIALOG_ACCOUNTS);
	        }
	        break;
	        default: finish();
	    }
	  }
}