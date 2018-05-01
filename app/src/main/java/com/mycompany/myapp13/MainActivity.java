package com.mycompany.myapp13;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import android.view.View.*;
import android.content.*;

public class MainActivity extends Activity 
{
	ImageView image;
	float x1 = 0;  
    float x2 = 0;  
    float y1 = 0;  
    float y2 = 0;  
	
	String str="https://api.i-meto.com/bing";
	ProgressBar p;
	File f;
	long first=0;
	Bitmap b;
	Handler h=new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			if(msg.what==1){
				b=(Bitmap) msg.obj;
				image.setImageBitmap(b);
				p.setVisibility(View.GONE);
				image.setVisibility(View.VISIBLE);
			}else{
				//Toast.makeText(MainActivity.this, msg.obj,5000).show();
				Toast.makeText(getApplicationContext(),(String)msg.obj,(int)5000).show();
			}
		}
		
	};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		//https://uploadbeta.com/api/pictures/random/?key=BingEverydayWallpaperPicture
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		image=(ImageView) findViewById(R.id.mainImageView);
		p=(ProgressBar) findViewById(R.id.mainProgressBar1);
		getPictute();
		image.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View p1, MotionEvent event)
				{

					//继承了Activity的onTouchEvent方法，直接监听点击事件\
					//这个方法是手指滑动屏幕的时候，读取下一张照片；条件：x 轴的位移超过50units，就会启动get
					//activity()
					if(event.getAction() == MotionEvent.ACTION_DOWN) {  
						//当手指按下的时候  
						x1 = event.getX();  
						y1 = event.getY();  
					}  
					if(event.getAction() == MotionEvent.ACTION_UP) {  
						//当手指离开的时候  
						x2 = event.getX();  
						y2 = event.getY();  
						if(x1 - x2 > 50||x2-x1>50) {  
							getPictute();
						} 
					}  
					return false;
				}
			});
		image.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					//长按触发saveimage，"Saved" 图标延迟设为3800 units
					File f=new File(getExternalCacheDir(),System.currentTimeMillis()+".jpg");
					saveImage(b,f);
					Toast.makeText(MainActivity.this,"Saved",(int)3800).show();
					
					return false;
				}
			});
    }
    //这个部分整个都是api，URL http://120.79.74.193:8888/bg.php， quest是"img"

	public void getPictute(){
		image.setVisibility(View.GONE);
		p.setVisibility(View.VISIBLE);
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					try{
					//api
					    Document d=  Jsoup.connect("http://120.79.74.193:8888/bg.php").get();
						Element e= d.select("img").get(0);
						str=e.attr("src");
						Message m=new Message();
						m.what=1;
						//look getURLimgae method below
						Bitmap b=getURLimage(str);
						m.obj=b;
						h.sendMessage(m);
						}catch(Exception e){
							Message m=new Message();
							m.what=2;
							m.obj=e.toString();
							h.sendMessage(m);
						}
				}
			}).start();
	}
	//this is from someone more experienced since we have no experience to deal with stream of
	//pictures.
	Bitmap getURLimage(String url) {  
        Bitmap bmp = null;  
        try {  
            URL myurl = new URL(url);  
            // 获得连接  
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();  
            conn.setConnectTimeout(6000);//设置超时  
            conn.setDoInput(true);  
            
            conn.connect();  
            InputStream is = conn.getInputStream();//获得图片的数据流  
            bmp = BitmapFactory.decodeStream(is);  
			
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bmp;  
    }  
	
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        //继承了Activity的onTouchEvent方法，直接监听点击事件  
        if(event.getAction() == MotionEvent.ACTION_DOWN) {  
            //当手指按下的时候  
            x1 = event.getX();  
            y1 = event.getY();  
        }  
        if(event.getAction() == MotionEvent.ACTION_UP) {  
            //当手指离开的时候  
            x2 = event.getX();  
            y2 = event.getY();  
             if(x1 - x2 > 50||x2-x1>50) {  
                getPictute();
            } 
        }  
        return super.onTouchEvent(event);  
    


	}
	// this part is saving the picture after long click.
	public static void saveImage(Bitmap bmp,File f) {
		
		File file = f;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(Bitmap. CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveBitmap(View view, String filePath){

		// 创建对应大小的bitmap
		Bitmap  bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
											 Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		//存储
		FileOutputStream outStream = null;
		File file=new File(filePath);
		if(file.isDirectory()){//如果是目录不允许保存 if it is directory, do not save
			return;
		}
		try {
			outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				bitmap.recycle();
				if(outStream!=null){
					outStream.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

@Override
public boolean onCreateOptionsMenu(Menu menu)
{
 // TODO: Implement this method
 getMenuInflater().inflate(R.menu.main,menu);
return super.onCreateOptionsMenu(menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item)
{
	if(item.getItemId()==R.id.save){
		Intent i=new Intent(this,PhotoShow.class);
		startActivity(i);
	}
	return super.onOptionsItemSelected(item);
}

	
}
