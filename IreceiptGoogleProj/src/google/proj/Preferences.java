package google.proj;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

public class Preferences extends PreferenceActivity {
	SharedPreferences preferences;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getPreferenceManager().setSharedPreferencesName(Preference.this);
		addPreferencesFromResource(R.xml.preferences);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		// getPrefs(); *************************************************

		// Get the custom preference
		/*
		 * Preference customPref = (Preference) findPreference("customPref");
		 * customPref .setOnPreferenceClickListener(new
		 * OnPreferenceClickListener() {
		 * 
		 * public boolean onPreferenceClick(Preference preference) {
		 * Toast.makeText(getBaseContext(),
		 * "The custom preference has been clicked", Toast.LENGTH_LONG).show();
		 * SharedPreferences customSharedPreference = getSharedPreferences(
		 * "myCustomSharedPrefs", Activity.MODE_PRIVATE);
		 * SharedPreferences.Editor editor = customSharedPreference .edit();
		 * editor.putString("myCustomPref", "The preference has been clicked");
		 * editor.commit(); return true; } });
		 */
		/*
		 * Button prefBtn = (Button) findViewById(R.id.prefButton);
		 * prefBtn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent settingsActivity = new
		 * Intent(getBaseContext(), Preferences.class);
		 * startActivity(settingsActivity); } });
		 */
	}

	public void onBackPressed() {
		Double limit = 0.0, periodLimit = 0.0;
		int day, month, year;
		boolean delete_sync = preferences.getBoolean("delete_sync", false);
		boolean payment_alert = preferences.getBoolean("payment_alert", false);
		if (payment_alert) {
			String str_limit = preferences.getString("limit", "0");
			Double double_limit;
			try {
				double_limit = Double.parseDouble(str_limit);
				idan.settings.setMaxMonth(double_limit);
			} catch (NumberFormatException ex) {
				idan.settings.setMaxMonth(-1.0);
			}
		}
		String str_duration = preferences.getString("duration", null);
		int duration = Integer.parseInt(str_duration);
		boolean periodAlert = preferences.getBoolean("limit_from_to", false);
		if (periodAlert) { // only if checkbok == true
			String str_date = preferences.getString("Choose date to start",
					null);
			IDate i_date = listview.getDate(str_date);
			if (i_date != null) {
				String str_period_limit = preferences.getString("limit_period",
						null);
				try {
					periodLimit = Double.parseDouble(str_period_limit);
					day = i_date.getDay();
					month = i_date.getMonth();
					year = i_date.getYear();
					Date date = new Date(year - 1900, month - 1, day);
					idan.settings.setDate(date);
					idan.settings.setMaxUniquely(periodLimit);
				} catch (NumberFormatException ex) {
					idan.settings.setMaxUniquely(-1.0);
				}
			} else {
				idan.settings.setDate(null);
				idan.settings.setMaxUniquely(-1.0);
			}
		}

		if (duration == 1) // week
			idan.settings.setdaysToStay(7);
		if (duration == 2)// month
			idan.settings.setdaysToStay(28);
		if (duration == 3)// year
			idan.settings.setdaysToStay(365);
		if (duration == 4)// forever
			idan.settings.setdaysToStay(-1);
		idan.settings.setDeleteOnServer(delete_sync);

		// String limit = preferences.getString("limit", "0").toString();
		// int limit2 = Integer.parseInt(limit);
		/*
		 * EditText limit2 = (EditText) findViewById(R.id.limit); String limit3
		 * = limit2.getText().toString(); לא עובד!!!
		 */
		// int limit2 = preferences.getInt("limit", 0); לא עובד!!
		// int duration = preferences.getInt("duration", 4); לא עובד!!

		// Float limit3 = preferences.getFloat("limit", 0); לא עובד!!

		// String limit = preferences.getString("limit", "0");
		// setResult(555);
		finish();
	}

	private void getPrefs() {
		boolean delete_sync;
		String ListPreference;
		String editTextPreference;
		String ringtonePreference;
		String secondEditTextPreference;
		String customPref;
		// Get the xml/preferences.xml preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		delete_sync = prefs.getBoolean("delete_sync", false);
		ListPreference = prefs.getString("listPref", "nr1");
		editTextPreference = prefs.getString("editTextPref",
				"Nothing has been entered");
		ringtonePreference = prefs.getString("ringtonePref",
				"DEFAULT_RINGTONE_URI");
		secondEditTextPreference = prefs.getString("SecondEditTextPref",
				"Nothing has been entered");
		// Get the custom preference
		SharedPreferences mySharedPreferences = getSharedPreferences(
				"myCustomSharedPrefs", Activity.MODE_PRIVATE);
		customPref = mySharedPreferences.getString("myCusomPref", "");
	}

	/*
	 * public void onStart() { // Bundle savedInstanceState // public void
	 * onStart() { boolean CheckboxPreference; String ListPreference; String
	 * editTextPreference; String ringtonePreference; String
	 * secondEditTextPreference; String customPref; super.onStart();
	 * getPreferenceManager().setSharedPreferencesName();
	 */

	/*
	 * public void getPrefs() { // Get the xml/preferences.xml preferences
	 * SharedPreferences prefs = PreferenceManager
	 * .getDefaultSharedPreferences(getBaseContext()); CheckboxPreference =
	 * prefs.getBoolean("checkboxPref", true); ListPreference =
	 * prefs.getString("listPref", "nr1"); editTextPreference =
	 * prefs.getString("editTextPref", "Nothing has been entered");
	 * ringtonePreference = prefs.getString("ringtonePref",
	 * "DEFAULT_RINGTONE_URI"); secondEditTextPreference =
	 * prefs.getString("SecondEditTextPref", "Nothing has been entered"); // Get
	 * the custom preference SharedPreferences mySharedPreferences =
	 * getSharedPreferences( "myCustomSharedPrefs", Activity.MODE_PRIVATE);
	 * customPref = mySharedPreferences.getString("myCusomPref", ""); }
	 */
	// }
}

/*
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * addPreferencesFromResource(R.xml.preferences); // Get the custom preference
 * Preference customPref = (Preference) findPreference("customPref"); customPref
 * .setOnPreferenceClickListener(new OnPreferenceClickListener() {
 * 
 * public boolean onPreferenceClick(Preference preference) {
 * Toast.makeText(getBaseContext(), "The custom preference has been clicked",
 * Toast.LENGTH_LONG).show(); SharedPreferences customSharedPreference =
 * getSharedPreferences( "myCustomSharedPrefs", Activity.MODE_PRIVATE);
 * SharedPreferences.Editor editor = customSharedPreference .edit();
 * editor.putString("myCustomPref", "The preference has been clicked");
 * editor.commit(); return true; }
 * 
 * }); } }
 */