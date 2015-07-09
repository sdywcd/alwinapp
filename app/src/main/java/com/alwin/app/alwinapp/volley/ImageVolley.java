package com.alwin.app.alwinapp.volley;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;
/**
 * volley 图片缓存
* @ClassName: ImageVolley 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author jcchen
* @date 2015年2月5日 下午7:13:24 
*
 */
public class ImageVolley extends Application{

	private static final String TAG="VolleyApplication";
	
	private RequestQueue mRequestQueue; 
	
	private static ImageVolley instance;
	
	private ImageLoader mImageLoader;
	
	public static ImageVolley getInstance(){
		return instance;
	}
	
	public RequestQueue getRequestQueue(){
		return mRequestQueue;
	}
	
	public ImageLoader getImageLoader(){
		return mImageLoader;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		File cacheDir=new File(this.getCacheDir(),"volley");
		mRequestQueue=new RequestQueue(new DiskBasedCache(cacheDir), new BasicNetwork(new HurlStack()),3);
		instance=this;
		
		MemoryCache mCache=new MemoryCache();
		mImageLoader=new ImageLoader(mRequestQueue, mCache);
		mRequestQueue.start();
		
	}
	
	
	
	
}
