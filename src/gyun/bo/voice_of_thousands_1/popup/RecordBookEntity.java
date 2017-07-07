package gyun.bo.voice_of_thousands_1.popup;

import java.io.File;

import android.os.Environment;

public class RecordBookEntity {

	public File rec = new File(
			Environment.getExternalStorageDirectory().getAbsolutePath() + "/VOT/" + "VOT_Record.wav");
	public int bookid, contentnum;
	public String memberid;
}
