package gyun.bo.voice_of_thousands_1;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Entity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

public class ParseDataParseHandler {

	public static ArrayList<BookItemEntityObject> getJSONReadBookList(StringBuilder buf) {

		ArrayList<BookItemEntityObject> jsonReadList = null;
		JSONArray bookList = null;
		JSONArray mainJSON = null;
		JSONObject mainObj = null;
		JSONObject firstObj = null;
		try {
			mainObj = new JSONObject(buf.toString());

			String result = mainObj.getString("result");
			if (result.equalsIgnoreCase("success")) {

				bookList = mainObj.getJSONArray("ongoing_list");
				jsonReadList = new ArrayList<BookItemEntityObject>(bookList.length());

				for (int i = 0, size = bookList.length(); i < size; i++) {
					BookItemEntityObject entity = new BookItemEntityObject();
					JSONObject ongoingreadOB = bookList.getJSONObject(i);
					entity.result = result;
					entity.book_id = ongoingreadOB.getInt("book_id");
					entity.book_synopsis = ongoingreadOB.getString("book_synopsis");
					entity.book_title = ongoingreadOB.getString("book_title");

					entity.book_publisher = ongoingreadOB.getString("book_publisher");

					entity.book_writer = ongoingreadOB.getString("book_writer");

					entity.book_genre = ongoingreadOB.getString("book_genre");

					/*
					 * entity.book_image =
					 * ongoingreadOB.getString("book_image");
					 */
					// URL url = new URL(ongoingreadOB.getString("book_image"));
					entity.book_img_url = ongoingreadOB.getString("book_image");
					// entity.bookimg =
					// BitmapFactory.decodeStream(url.openStream());

					entity.current_cnum = ongoingreadOB.getInt("current_cnum");

					entity.total_cnum = ongoingreadOB.getInt("total_cnum");

					jsonReadList.add(entity);

				}
			} else {
				BookItemEntityObject entity = new BookItemEntityObject();
				entity.result = result;
				jsonReadList.add(entity);
			}
		} catch (Exception e) {
			Log.e("ParseDataParseHandler.getJSONReadBookList", e.toString());
		}
		return jsonReadList;

	}

	public static ArrayList<BookItemEntityObject> getJSONListenBookList(StringBuilder buf) {
		ArrayList<BookItemEntityObject> jsonListenList = null;

		JSONObject mainObj = null;
		JSONArray bookList = null;

		try {
			mainObj = new JSONObject(buf.toString());

			String result = mainObj.getString("result");

			if (result.equalsIgnoreCase("success")) {

				bookList = mainObj.getJSONArray("complete_list");
				int length = bookList.length();
				if (length != 0) {

					jsonListenList = new ArrayList<BookItemEntityObject>(length);

					for (int i = 0; i < length; i++) {
						BookItemEntityObject entity = new BookItemEntityObject();

						JSONObject history = bookList.getJSONObject(i);
						entity.result = result;
						entity.total_cnum = mainObj.getInt("total_page");
						entity.current_cnum = mainObj.getInt("current_page");
						entity.book_id = history.getInt("book_id");
						entity.book_title = history.getString("book_title");
						entity.book_image = history.getString("book_image");

						jsonListenList.add(entity);

					}
				} else {
					Log.e("ParseDataParseHandler.getJSONMemberInfo", "fail");
			/*		Toast.makeText(VOTApplication.getContext(), "유유.",Toast.LENGTH_SHORT).show();
			*/		BookItemEntityObject entity = new BookItemEntityObject();
					entity = new BookItemEntityObject();
					entity.total_cnum =0;
					entity.result = result; // fail
					jsonListenList.add(entity);
				}

			} else {
				Log.e("ParseDataParseHandler.getJSONListenBookList", "fail");
				BookItemEntityObject entity = new BookItemEntityObject();
				entity.total_cnum = 0;
				entity = new BookItemEntityObject();
				entity.result = "fail"; // fail
				jsonListenList.add(entity);
			}

		} catch (Exception e) {
			Log.e("ParseDataParseHandler.getJSONListenBookList", e.toString());
		}

		return jsonListenList;
	}

	public static ArrayList<BookItemEntityObject> getJSONMemberInfo(StringBuilder buf) {
		ArrayList<BookItemEntityObject> jsonListenList = null;
		String result = null;
		JSONObject mainObj = null;
		JSONArray info = null;
		BookItemEntityObject entity = null;
		try {
			mainObj = new JSONObject(buf.toString());
			result = mainObj.getString("result");

			if (result.equalsIgnoreCase("success")) { // 이력이 있음.

			//	Log.e("이력 리스트를 불러왔따데스", "성공이다요");

				info = mainObj.getJSONArray("member_history");
				int length = info.length();
				if (length != 0) {

					jsonListenList = new ArrayList<BookItemEntityObject>(length);

					for (int i = 0, size = length; i < size; i++) {

						JSONObject infoOB = info.getJSONObject(i);
						entity = new BookItemEntityObject();
						entity.result = result;
						entity.total_cnum = mainObj.getInt("total_page");
						entity.current_cnum = mainObj.getInt("current_page");
						entity.book_id = infoOB.getInt("book_id");
						entity.book_title = infoOB.getString("book_title");
						entity.reject_reason = infoOB.getString("reject_reason");
						entity.content_status = infoOB.getInt("content_status");
						entity.content_num = infoOB.getInt("content_num");
						entity.history_id = infoOB.getInt("history_id");
						entity.listenbook_path = infoOB.getString("content_voice");
						jsonListenList.add(entity);
						/* jsonListenList.add(entity); */
					}
				}
			} else if (result.equalsIgnoreCase("fail")) { // 이력이 없을때
				jsonListenList = new ArrayList<BookItemEntityObject>(1);
				entity = new BookItemEntityObject();
				entity.result = "fail";
				entity.current_cnum = 0;
				entity.total_cnum = 1;
				jsonListenList.add(entity);
			}

		} catch (Exception e) {
			Log.e("ParseDataParseHandler.getJSONMemberInfo", e.toString());
		}

		return jsonListenList;
	}

	public static ArrayList<BookItemEntityObject> getJSONListenMode(StringBuilder buf) {
		ArrayList<BookItemEntityObject> jsonListenBookInfo = null;

		jsonListenBookInfo = new ArrayList<BookItemEntityObject>(1);
		JSONObject mainObj = null;
		JSONArray info = null;
		BookItemEntityObject entity = new BookItemEntityObject();
		try {
			mainObj = new JSONObject(buf.toString());
			String result = mainObj.getString("result");
			entity.result = result;
			if (result.equalsIgnoreCase("success")) {

				/*
				 * ListenPage_Fragment.path =
				 * mainObj.getString("content_voice");
				 * ListenPage_Fragment.membername =
				 * mainObj.getString("member_name");
				 * ListenPage_Fragment.content_num =
				 * mainObj.getInt("content_cnum");
				 */

				entity.listenbook_path = mainObj.getString("content_voice");

				entity.member_name = mainObj.getString("member_name");

				entity.content_num = mainObj.getInt("content_cnum");

				URL url = new URL(mainObj.getString("member_image"));

				entity.bookimg = BitmapFactory.decodeStream(url.openStream());
				if (ListenPage_Fragment.imgW != 0) {
					entity.memberimg = Bitmap.createScaledBitmap(entity.bookimg, ListenPage_Fragment.imgW,
							ListenPage_Fragment.imgH, true);
				}
				/*
				 * URL url = new URL(mainObj.getString("book_image"));
				 * entity.memberimg =
				 * BitmapFactory.decodeStream(url.openStream());
				 */

			} else {

			}
			jsonListenBookInfo.add(entity);

		} catch (Exception e) {
			Log.e("ParseDataParseHandler.getJSONListenMode", e.toString());
		}

		return jsonListenBookInfo;
	}

	public static ArrayList<BookItemEntityObject> getJSONMemberLOGIN(StringBuilder buf) {

		ArrayList<BookItemEntityObject> jsonLoginData = null;

		JSONObject mainObj = null;

		try {
			mainObj = new JSONObject(buf.toString());
			String result = mainObj.getString("result");
			if (!result.equalsIgnoreCase("success")) {
				Log.e("ParseDataParser.getJSONMemberLOGIN","불러올 데이타가 없습니다");
			} else {
				BookItemEntityObject entity = new BookItemEntityObject();
				jsonLoginData = new ArrayList<BookItemEntityObject>(1);
				JSONObject memberinfo = mainObj.getJSONObject("member_info");
				entity.member_id = memberinfo.getInt("member_id");
				entity.member_name = memberinfo.getString("member_name");
				entity.member_gender = memberinfo.getString("member_gender");
				jsonLoginData.add(entity);

			}

		} catch (Exception e) {

			Log.e("ParseDataParseHandler.getJSONMemberLOGIN", e.toString());

		}

		return jsonLoginData;

	}

	public static ArrayList<BookItemEntityObject> getJSONBooking(StringBuilder buf) {
		ArrayList<BookItemEntityObject> jsonListenList = new ArrayList<BookItemEntityObject>(1);

		JSONObject mainObj = null;
		JSONObject firstObj = null;
		BookItemEntityObject entity = new BookItemEntityObject();

		try {

			mainObj = new JSONObject(buf.toString());

			entity.result = mainObj.getString("result");

			if (entity.result.equalsIgnoreCase("success")) {

				firstObj = mainObj.getJSONObject("booking_info");

				entity.book_id = firstObj.getInt("book_id");
				entity.content_num = firstObj.getInt("content_num");
				entity.book_title = firstObj.getString("book_title");
				entity.content_text = firstObj.getString("content_text");
				entity.total_cnum = firstObj.getInt("total_cnum");
				entity.book_count = mainObj.getInt("count");
			} else if (entity.result.equalsIgnoreCase("fail")) {

				entity.reject_reason = mainObj.getString("result_msg");

			} else {
				Log.e("ParseDataParseHandler.getJSONListenBookList", "접속에 실패함");
				entity.result = "fail";

			}
			jsonListenList.add(entity);
		} catch (Exception e)

		{
			Log.e("ParseDataParseHandler.getJSONListenBooking", e.toString());
		}

		return jsonListenList;

	}

	public static ArrayList<BookItemEntityObject> getJSONListenBookInfo(StringBuilder buf) {
		ArrayList<BookItemEntityObject> jsonListenList = null;

		JSONObject mainObj = null;
		JSONObject firstObj = null;
		try {

			mainObj = new JSONObject(buf.toString());

			String result = mainObj.getString("result");

			if (result.equalsIgnoreCase("success")) {

				firstObj = mainObj.getJSONObject("listen_info");

				jsonListenList = new ArrayList<BookItemEntityObject>(1);

				BookItemEntityObject entity = new BookItemEntityObject();
				entity.result = result;
				entity.book_id = firstObj.getInt("book_id");
				entity.book_title = firstObj.getString("book_title");
				entity.book_writer = firstObj.getString("book_writer");
				entity.book_publisher = firstObj.getString("book_publisher");
				entity.book_img_url = firstObj.getString("book_image");
				URL url = new URL(firstObj.getString("book_image"));
				entity.bookimg = BitmapFactory.decodeStream(url.openStream());
				entity.book_mark = firstObj.getInt("book_mark");
				entity.total_cnum = firstObj.getInt("total_cnum");
				jsonListenList.add(entity);
			} else {
				BookItemEntityObject entity = new BookItemEntityObject();
				entity.result = result;
				jsonListenList.add(entity);
			}

		} catch (Exception e) {
			Log.e("ParseDataParseHandler.getJSONListenBookInfo", e.toString());
		}

		return jsonListenList;
	}

	public static ArrayList<BookItemEntityObject> getJSONSample(StringBuilder buf) {
		// TODO Auto-generated method stub

		ArrayList<BookItemEntityObject> jsonSample = new ArrayList<BookItemEntityObject>(1);
		BookItemEntityObject entity = new BookItemEntityObject();
		JSONObject mainObj = null;

		try {
			mainObj = new JSONObject(buf.toString());
			String result = mainObj.getString("result");
			entity.result = result;
			if (result.equalsIgnoreCase("success")) {
				entity.listenbook_path = mainObj.getString("sample_voice");

			} else {
				Log.e("ParseDataParseHandler.getJSONSample", "녹음된 샘플이 없음.");
			}
			jsonSample.add(entity);
		} catch (Exception e) {
			Log.e("ParseDataParseHandler.getJSONSample", e.toString());
		}

		return jsonSample;
	}

	public static ArrayList<BookItemEntityObject> DeleteHistory(StringBuilder buf) {
		ArrayList<BookItemEntityObject> jsonDel = new ArrayList<BookItemEntityObject>(1);
		BookItemEntityObject entity;
		JSONObject mainObj = null;
		try {
			entity = new BookItemEntityObject();
			mainObj = new JSONObject(buf.toString());
			String result = mainObj.getString("result");
			if (result.equalsIgnoreCase("success")) {
				
			entity.result = mainObj.getString("result");
			jsonDel.add(entity);

			} else {
				Log.e("ParseDataParseHandler.DeleteHistory", "히스토리 삭제 실패함");
			}
		} catch (Exception e) {

			Log.e("ParseDataParseHandler.DeleteHistory", e.toString());
		}

		// TODO Auto-generated method stub
		return jsonDel;
	}

	public static ArrayList<BookItemEntityObject> getJSONHistoryListen(StringBuilder buf) {
		// TODO Auto-generated method stub

		return null;
	}

	public static ArrayList<BookItemEntityObject> getJsonDupcheck(StringBuilder buf) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ArrayList<BookItemEntityObject> getJsonMP3Download(StringBuilder buf) {
		ArrayList<BookItemEntityObject> jsonMP3 = new ArrayList<BookItemEntityObject>(1);
		BookItemEntityObject entity;
		JSONObject mainObj = null;
		try {
			entity = new BookItemEntityObject();
			mainObj = new JSONObject(buf.toString());
			String result = mainObj.getString("result");
			if (result.equalsIgnoreCase("success")) {
				
			entity.result = mainObj.getString("result");
			entity.listenbook_path = mainObj.getString("book_file");
			jsonMP3.add(entity);

			} else {
				Log.e("ParseDataParseHandler.getJsonMP3Download", "mp3 다운로드 실패함");
			}
		} catch (Exception e) {

			Log.e("ParseDataParseHandler.getJsonMP3Download", e.toString());
		}

		// TODO Auto-generated method stub
		return jsonMP3;
	}

}
