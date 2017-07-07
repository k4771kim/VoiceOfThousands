package gyun.bo.voice_of_thousands_1;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore.Audio;

public class BookItemEntityObject {

	public int book_id, voice_hit, total_cnum, current_cnum,book_mark,history_id;
	public String book_title, book_writer, book_publisher, book_genre ,content_text;

	public String book_image,voice_book,member_image;
	public String reject_reason , book_synopsis;                      //record_list
	public int content_num,content_status,member_id ,book_count ,participation_count;
	public String listenbook_path, member_name,member_gender ,result,book_img_url;
	Bitmap bookimg,memberimg;
	public BookItemEntityObject() {

	}

	BookItemEntityObject(int book_id, int voice_hit, int total_cnum, int current_cnum, String book_title,
			String book_writer, String book_publisher, String book_genre, String book_image, String voice_book) {
		super();

		this.book_id = book_id;
		this.voice_hit = voice_hit;
		this.total_cnum = total_cnum;
		this.current_cnum = current_cnum;
		this.book_title = book_title;
		this.book_writer = book_writer;
		this.book_publisher = book_publisher;
		this.book_genre = book_genre;
		this.book_image = book_image;
		this.voice_book = voice_book;
	}
	
	
	




	public void setobject() {
		// TODO Auto-generated method stub
		
	}
}
