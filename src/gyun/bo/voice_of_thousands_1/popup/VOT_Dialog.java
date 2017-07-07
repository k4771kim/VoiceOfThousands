package gyun.bo.voice_of_thousands_1.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import gyun.bo.voice_of_thousands_1.R;
import gyun.bo.voice_of_thousands_1.Record_Fragment;
import gyun.bo.voice_of_thousands_1.VOT_GCM_DefineConstant;

public class VOT_Dialog extends Dialog {
	Button never, ok;

	public VOT_Dialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.vot_dialog);

		setLayout();

	}

	private void setLayout() {
		// TODO Auto-generated method stub

		never = (Button) findViewById(R.id.dialog_never);
		ok = (Button) findViewById(R.id.dialog_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		never.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences prefs = getContext()
						.getSharedPreferences(VOT_GCM_DefineConstant.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor prefsEditor = prefs.edit();

				prefsEditor.putBoolean("record_dialog", true);
				prefsEditor.commit();
				onBackPressed();
			}
		});

	}

}
