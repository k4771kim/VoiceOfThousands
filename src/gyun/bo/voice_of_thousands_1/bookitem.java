package gyun.bo.voice_of_thousands_1;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class bookitem extends FrameLayout {

	public bookitem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	public bookitem(Context context) {
		super(context);
		init();
	}

	TextView title_txt, pub_txt, aut_txt, part_txt, con_txt;
	ImageView BookImg1;
	ProgressBar progress;

	private void init() {

		LayoutInflater.from(getContext()).inflate(R.layout.readitem, this);
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "SEOULHANGANGB.TTF");

		title_txt = (TextView) findViewById(R.id.title_txt);
		con_txt = (TextView) findViewById(R.id.book_con);
		pub_txt = (TextView) findViewById(R.id.publisher_txt);
		aut_txt = (TextView) findViewById(R.id.author_txt);
		part_txt = (TextView) findViewById(R.id.part_txt);
		title_txt.setTypeface(tf);
		con_txt.setTypeface(tf);
		pub_txt.setTypeface(tf);
		aut_txt.setTypeface(tf);
		part_txt.setTypeface(tf);
		progress = (ProgressBar) findViewById(R.id.progress_cureent);
		part_txt = (TextView) findViewById(R.id.part_txt);
		BookImg1 = (ImageView) findViewById(R.id.BookImg);
	}

	bookitemdata mData;

	public void setItemData(bookitemdata data) {

		mData = data;
		Bitmap bitmap;

		// BookImg1.setImageBitmap(data.bookimg);
		title_txt.setText(data.title);
		aut_txt.setText(data.author);
		part_txt.setText(data.part_per
				+ "%");/*
						 * 
						 * progress.setScaleX((float)((
						 * data.part_per)*(100/23)));
						 *//*
							 * progress.setLayoutParams(new
							 * LayoutParams(LayoutParams.WRAP_CONTENT*data.
							 * part_per/10,LayoutParams.MATCH_PARENT));
							 */
		ImageLoader.getInstance().displayImage(data.book_img_url, BookImg1, read_book_list_fragment.options);
		progress.setProgress(data.part_per);
		pub_txt.setText(data.publisher);
		con_txt.setText(data.book_Synop);
	}

	public class CustomImageView extends ImageView {

		public CustomImageView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			Drawable d = getDrawable();

			if (d != null) {
				int width = MeasureSpec.getSize(widthMeasureSpec);
				int height = (int) Math
						.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
				setMeasuredDimension(width, height);
			} else {
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}

		}

	}
}
