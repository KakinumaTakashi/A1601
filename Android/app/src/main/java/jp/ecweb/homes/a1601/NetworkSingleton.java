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

	// メンバ変数
	private static NetworkSingleton ourInstance;			// インスタンス
	private RequestQueue mRequestQueue;					// リクエストキュー
	private static Context mContext;						// コンテキスト
	private ImageLoader mImageLoader;					// イメージローダー

	// 固定値
	private static final Object TAG_REQUEST_QUEUE =	// キャンセル用タグ
			new Object();

	// インスタンスの取得・生成
	public static synchronized NetworkSingleton getInstance(Context context) {
		// インスタンスが生成されていない場合は作成する
		if (ourInstance == null) {
			ourInstance = new NetworkSingleton(context);
		}

		return ourInstance;
	}

	// コンストラクタ
	private NetworkSingleton(Context context) {
		// コンテキスト・リクエストキューをメンバ変数に格納
		mContext = context;
		mRequestQueue = getRequestQueue();

		// イメージローダーのインスタンスをメンバ変数に格納
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

	// リクエストキューの取得・生成
	public RequestQueue getRequestQueue() {
		// リクエストキューが生成されていない場合は作成する
		if (mRequestQueue == null) {
			mRequestQueue =
					Volley.newRequestQueue(mContext.getApplicationContext());
		}

		return mRequestQueue;
	}

	// リクエストキューへのリクエスト追加
	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG_REQUEST_QUEUE);
		getRequestQueue().add(req);
	}

	// イメージローダーの取得
	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	// リクエストの停止
	public void cancelAll() {
		mRequestQueue.cancelAll(TAG_REQUEST_QUEUE);
	}
}
