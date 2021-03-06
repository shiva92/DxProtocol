package com.android.dxbprotocol;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dxbprotocol.classes.UnCaughtException;
import com.android.dxbprotocol.config.Constants;
import com.android.dxbprotocol.dialogs.AppAlert;
import com.android.dxbprotocol.storage.Preferences;
import com.android.dxbprotocol.utility.Utility;

/**
 * AppActivity - Common class extended by all the activities in this application
 * to provide utility methods and common methods.12321231232312
 * 
 */
public class AppActivity extends Activity {

	public static final int DIRECTION_FRONT = 0, DIRECTION_BACK = 1;
	private ProgressDialog prgLoading = null;
	Preferences preference;

	TextView txtTitle;

	AppActivity activity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		preference = new Preferences(this);

		// Thread.setDefaultUncaughtExceptionHandler(new
		// UnCaughtException(this));
		if (Constants.DEBUG) {
			try {
				showBundleValues();
			} catch (Exception e) {

			}
		}
	}

	// Show Bundle Data
	private void showBundleValues() {
		try {
			debug("Bundle Values:\n---------------\nCLASS ::"
					+ this.getClass().getName());
			Bundle bundle = this.getIntent().getExtras();
			if (bundle != null) {
				Set<String> keys = bundle.keySet();
				if (keys != null) {
					Iterator<?> itr = keys.iterator();
					while (itr.hasNext()) {
						String key = (String) itr.next();
						String val = (String) bundle.get(key);
						debug("Bundle Value[" + key + "]-->" + val);
					}
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @return Returns the application name from the Resources.
	 */
	public String getAppName() {
		return getString(R.string.app_name);
	}

	/**
	 * To set the application title
	 */
	protected void setAppTitle(int layoutID) {

		try {

			if (preference.isLanguageIsEnglish()) {
				preference.setLanguage(Preferences.KEY_LANGUAGE_ENGLISH);
				Configuration config = new Configuration();
				config.locale = Locale.ENGLISH;
				getResources().updateConfiguration(config, null);
			} else {
				preference.setLanguage(Preferences.KEY_LANGUAGE_ARABIC);
				Locale locale = new Locale("ar");
				Locale.setDefault(locale);
				Configuration config = new Configuration();
				config.locale = locale;
				getBaseContext().getResources().updateConfiguration(config,
						getBaseContext().getResources().getDisplayMetrics());
			}

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(layoutID);
			// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
			// R.layout.app_titlebar);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// txtTitle = (TextView) findViewById(R.id.txtviewTitle);
		// txtTitle.setText(Constants.APP_NAME);
		// print("Setting Title...");
		// doApplyFont();
	}

	/**
	 * To print the log
	 */
	public void print(String msg) {
		if (Constants.DEBUG)
			android.util.Log.i(
					"[" + Constants.APP_NAME + " - " + this.getLocalClassName()
							+ "]", msg);
	}

	/**
	 * To print the messages for debugging
	 */
	public void debug(String msg) {
		if (Constants.DEBUG)
			android.util.Log.i(
					"[" + Constants.APP_NAME + " - " + this.getLocalClassName()
							+ "]", msg);
	}

	/**
	 * To print the messages for debugging
	 */
	public void msg(String msg) {
		if (Constants.DEBUG) {
			toast(msg);
			debug(msg);
		}
	}

	/**
	 * To display the Toast message
	 */
	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * To display the Toast message
	 */
	public void toast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
	}

	/**
	 * To display the short Toast message
	 */
	protected void stoast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * To display the alert dialog
	 * 
	 * @param control
	 *            The EditText control which needs to get focus after alert
	 *            comes
	 * @param sMessage
	 *            The message to show in alert dialog
	 */
	public void alert(final EditText control, String sMessage) {
		try {
			hideLoading();
		} catch (Exception e) {

		}
		new AppAlert(this, sMessage) {
			@Override
			public void okClickListener() {
				control.setFocusableInTouchMode(true);
				control.requestFocusFromTouch();
			}
		};
	}

	/**
	 * To display the alert dialog
	 * 
	 * @param control
	 *            The EditText control which needs to get focus after alert
	 *            comes
	 * @param sMessage
	 *            The message to show in alert dialog
	 */
	public void alert(final EditText control, String sMsgCode, String sMessage) {
		new AppAlert(this, sMsgCode, sMessage) {
			@Override
			public void okClickListener() {
				control.setFocusableInTouchMode(true);
				control.requestFocusFromTouch();
			}
		};
	}

	/**
	 * To display the alert dialog
	 * 
	 * @param sMessage
	 *            The message to show in alert dialog
	 */
	public void alert(String sMessage) {
		new AppAlert(this, sMessage);
	}

	/**
	 * To show the alert dialog with title and message.
	 * 
	 * @param sTitle
	 *            The title to be keep in alert
	 * @param sMessage
	 */
	public void alert(String sTitle, String sMessage) {
		new AppAlert(this, sTitle, sMessage);
	}

	/**
	 * To show error message
	 * 
	 * @param sMessage
	 *            The message to show
	 */
	public void error(String sMessage) {
		new AppAlert(this, sMessage);
	}

	/**
	 * Method to start the activity
	 * 
	 * @param cls
	 *            The class of activity which needs to open
	 */
	public void showActivity(Class<?> cls) {
		Intent i = new Intent(this, cls);
		showIntent(i);
	}

	/**
	 * To start the activity with directions
	 * 
	 * @param cls
	 *            The class of activity which needs to open
	 * @param direction
	 *            The direction whether front or back.
	 */
	public void showActivity(Class<?> cls, int direction) {
		Intent i = new Intent(this, cls);
		showIntent(i, direction);
	}

	/**
	 * To start the intent
	 * 
	 * @param intent
	 *            The intent to start
	 */
	public void showIntent(Intent intent) {
		showIntent(intent, DIRECTION_FRONT);
	}

	/**
	 * To start the intent
	 * 
	 * @param intent
	 *            The intent to start
	 * @param direction
	 *            The direction whether front or back.
	 */
	public void showIntent(Intent intent, int direction) {
		Utility util = new Utility();
		util.startIntent(this, intent, direction);
	}

	/**
	 * To start the intent
	 * 
	 * @param act
	 *            The activity which starting another activity
	 * @param intent
	 *            The intent to start
	 * @param direction
	 *            The direction whether front or back.
	 */

	public void showIntent(Activity act, Intent intent, int direction) {
		Utility util = new Utility();
		util.startIntent(this, intent, direction);
	}

	/**
	 * To show the loading dialog with given message
	 * 
	 * @param msg
	 *            Message to show when showing dialog.
	 */
	public void showLoading(String msg) {

		if (prgLoading != null && prgLoading.isShowing())
			prgLoading.setMessage(msg);
		else
			prgLoading = ProgressDialog.show(this, "", msg);

	}

	/**
	 * To close the loading dialog
	 */

	protected void hideLoading() {
		try {
			if (prgLoading != null && prgLoading.isShowing()) {
				prgLoading.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Typeface font;

	/**
	 * To set the custom font for all methods.
	 * 
	 * @param view
	 *            View to sent the font.
	 */
	public void setFont(View view) {

		if (view instanceof TextView) {
			TextView txt = (TextView) view;

			txt.setTypeface(font);
		} else if (view instanceof EditText) {
			EditText txt = (EditText) view;
			txt.setTypeface(font);
		} else if (view instanceof Button) {
			Button btn = (Button) view;
			btn.setTypeface(font);
		} else if (view instanceof RadioButton) {
			RadioButton btn = (RadioButton) view;
			btn.setTypeface(font);
		} else if (view instanceof CheckBox) {
			CheckBox btn = (CheckBox) view;
			btn.setTypeface(font);
		} else {
			// print("Unknown Field");
		}

	}

	/**
	 * To initialize custom font and start to apply font from parent view.
	 */

	public void doApplyFont() {
		font = Typeface.createFromAsset(this.getAssets(), "fonts/ARIAL.TTF");

		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Kingthings_Calligraphica_2.ttf");
		txtTitle.setTypeface(font);

		applyFont(((ViewGroup) findViewById(android.R.id.content))
				.getChildAt(0));
	}

	/**
	 * Recursive method to loop through child views
	 * 
	 * @param view
	 *            View to iterate the child to apply font.
	 */
	public void applyFont(View view) {
		int n = 0;
		if (view instanceof LinearLayout) {
			LinearLayout lyt = ((LinearLayout) view);
			n = lyt.getChildCount();
			if (n < 1) {
				return;
			} else {
				for (int lyt_i = 0; lyt_i < n; lyt_i++)
					applyFont(lyt.getChildAt(lyt_i));
			}
		} else if (view instanceof ScrollView) {
			ScrollView scl = ((ScrollView) view);
			n = scl.getChildCount();
			if (n < 1) {
				return;
			} else {
				for (int lyt_i = 0; lyt_i < n; lyt_i++)
					applyFont(scl.getChildAt(lyt_i));
			}
		} else if (view instanceof TableLayout) {
			TableLayout tbl = ((TableLayout) view);
			n = tbl.getChildCount();
			if (n < 1) {
				return;
			} else {
				for (int lyt_i = 0; lyt_i < n; lyt_i++)
					applyFont(tbl.getChildAt(lyt_i));
			}
		} else if (view instanceof TableRow) {
			TableRow tr = ((TableRow) view);
			n = tr.getChildCount();
			if (n < 1) {
				return;
			} else {
				for (int lyt_i = 0; lyt_i < n; lyt_i++)
					applyFont(tr.getChildAt(lyt_i));
			}
		} else {
			setFont(view);
		}

	}

	/**
	 * To handle the back button process
	 */
	public void goBack() {
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				goBack();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
