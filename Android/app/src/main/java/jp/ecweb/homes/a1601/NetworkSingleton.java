package jp.ecweb.homes.a1601;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Takashi Kakinuma on 2016/07/15.
 */
public class NetworkSingleton {
	private static NetworkSingleton ourInstance;
	private RequestQueue mRequestQueue;
	private static Context mContext;
	private ImageLoader mImageLoader;

	public static synchronized NetworkSingleton getInstance(Context context) {
		if (ourInstance == null) {
			ourInstance = new NetworkSingleton(context);
		}
		return ourInstance;
	}

	private NetworkSingleton(Context context) {
		mContext = context;
		mRequestQueue = getRequestQueue();
		mImageLoader = new ImageLoader(mRequestQueue,
				new ImageLoader.ImageCache() {
					private final LruCache<String, Bitmap> cache =
							new LruCache<String, Bitmap>(20);

					@Override
					public Bitmap getBitmap(String url) {
						return cache.get(url);
					}

					@Override
					public void putBitmap(String url, Bitmap bitmap) {
						cache.put(url, bitmap);
					}
				});
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue =
					Volley.newRequestQueue(mContext.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
}
