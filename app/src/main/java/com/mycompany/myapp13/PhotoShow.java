package com.mycompany.myapp13;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class PhotoShow extends Activity
{

	ListView g;
	List<Bitmap>f=new ArrayList<Bitmap>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		g=(ListView) findViewById(R.id.gridView);
		for(File f:getExternalCacheDir().listFiles()){
			if(f.isFile()){
				this.f.add(BitmapFactory.decodeFile(f.getAbsolutePath()));
			}
		}
		g.setAdapter(new MyAdapter(this,f));
	}
	
}class MyAdapter extends BaseAdapter{
	private LayoutInflater layoutInflater;
	private List<Bitmap> images;
	private String[] text;
	public MyAdapter(Context context,List<Bitmap> images){
		this.images = images;
		layoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = layoutInflater.inflate(R.layout.image_item,null);
		ImageView iv = (ImageView) v.findViewById(R.id.image_itemImageView);
		
		iv.setImageBitmap(images.get(position));
		return v;
	
    }
    // base adapter is a method in andriod library and we learn it from internet to display our
	//pictures saved.
}
