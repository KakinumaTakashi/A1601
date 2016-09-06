package jp.ecweb.homes.a1601.listener;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.callback.CocktailListCallbacks;
import jp.ecweb.homes.a1601.model.Category;

/**
 * カクテル一覧受信処理クラス
 *
 * Created by KakinumaTakashi on 2016/09/03.
 */
public class CocktailCategoryListener implements Response.Listener<JSONObject>, Response.ErrorListener {

/*--------------------------------------------------------------------------------------------------
	フィールド
--------------------------------------------------------------------------------------------------*/
	// ログ出力
	private final String LOG_TAG = "A1601";     // ログ識別タグ
	private final String LOG_CLASSNAME =        // クラス名
			this.getClass().getSimpleName() + " : ";

	private Activity activity;                  // 呼び出し元コンテキスト

/*--------------------------------------------------------------------------------------------------
	コンストラクタ
--------------------------------------------------------------------------------------------------*/
	public CocktailCategoryListener(Activity activity) {
		this.activity = activity;
	}

/*--------------------------------------------------------------------------------------------------
	メソッド
--------------------------------------------------------------------------------------------------*/
	// カクテル一覧受信処理
	@Override
	public void onResponse(JSONObject response) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onResponse start");

		Category category = new Category();

		Log.d(LOG_TAG, LOG_CLASSNAME +
				"Response=" +
				response.toString()
		);

		try {
			// ヘッダ部処理
			String status = response.getString("status");

			if (status.equals("NG")) {
				// サーバーにてエラーが発生した場合
				throw new JSONException(response.getString("message"));
			}

			// データ部処理
			JSONObject data = response.getJSONObject("data");

			// Category1Items
			JSONArray category1Items = data.getJSONArray("CATEGORY1ITEMS");

			for (int i = 0; i < category1Items.length(); i++) {
				JSONObject jsonObject = category1Items.getJSONObject(i);

				category.getCategory1List().add(jsonObject.getString("CATEGORY1"));
				category.getCategory1NumList().add(jsonObject.getString("CATEGORY1NUM"));
			}

			// Category2Items
			JSONArray category2Items = data.getJSONArray("CATEGORY2ITEMS");

			for (int i = 0; i < category2Items.length(); i++) {
				JSONObject jsonObject = category2Items.getJSONObject(i);

				category.getCategory2List().add(jsonObject.getString("CATEGORY2"));
				category.getCategory2NumList().add(jsonObject.getString("CATEGORY2NUM"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
			category = null;
		}

		// Activityのコールバック呼び出し
		((CocktailListCallbacks)activity).CategoryCallback(category);

		Log.d(LOG_TAG, LOG_CLASSNAME + "onResponse end");
	}

	// 通信エラー処理
	@Override
	public void onErrorResponse(VolleyError error) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onErrorResponse start");

		Log.d(LOG_TAG, LOG_CLASSNAME + "onErrorResponse end");
	}
}
