package jp.ecweb.homes.a1601.listener;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.callback.ProductListCallbacks;
import jp.ecweb.homes.a1601.model.Category;

/**
 * Created by KakinumaTakashi on 2016/09/06.
 */
public class ProductCategoryListener implements Response.Listener<JSONObject>, Response.ErrorListener {

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
	public ProductCategoryListener(Activity activity) {
		this.activity = activity;
	}

/*--------------------------------------------------------------------------------------------------
	メソッド
--------------------------------------------------------------------------------------------------*/
	// 材料カテゴリ一覧受信処理
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

				category.getCategory1List().add(jsonObject.getString("MAKER"));
				category.getCategory1ValueList().add(jsonObject.getString("MAKER"));
				category.getCategory1NumList().add(jsonObject.getString("MAKERNUM"));
			}

			// Category2Items
			JSONArray category2Items = data.getJSONArray("CATEGORY2ITEMS");

			for (int i = 0; i < category2Items.length(); i++) {
				JSONObject jsonObject = category2Items.getJSONObject(i);

				// 表示用文字列構築
				StringBuilder stringBuilder = new StringBuilder();

				stringBuilder.append(jsonObject.getString("CATEGORY1"));
				if (!jsonObject.getString("CATEGORY2").equals("")) {
					stringBuilder.append("/");
					stringBuilder.append(jsonObject.getString("CATEGORY2"));
				}
				if (!jsonObject.getString("CATEGORY3").equals("")) {
					stringBuilder.append("/");
					stringBuilder.append(jsonObject.getString("CATEGORY3"));
				}

				category.getCategory2List().add(stringBuilder.toString());
				category.getCategory2ValueList().add(jsonObject.getString("ID"));
				category.getCategory2NumList().add(jsonObject.getString("CATEGORYNUM"));
			}

/*
			mMakerListItems = new CharSequence[data.length()];
			mMakerList = new ArrayList<>();

			for (int i = 0; i < data.length(); i++) {
				JSONObject jsonObject = data.getJSONObject(i);

				mMakerListItems[i] =
						jsonObject.getString("MAKER") + "  （" +
								jsonObject.getString("MAKERNUM") + " 件）";

				mMakerList.add(i, jsonObject.getString("MAKER"));
			}

			// ダイアログを表示
			AlertDialog.Builder builder =
					new AlertDialog.Builder(A0202_ProductListActivity.this);

			//ダイアログタイトルをセット
			builder.setTitle("製造/販売会社を選択");
//							builder.setCancelable(false);

			// 表示項目とリスナの設定
			builder.setItems(mMakerListItems, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// 選択した製造/販売会社をセット(カテゴリの絞り込みは解除)
					mMaker = mMakerList.get(which);
					mMaterialId = "All";
					// ListViewを更新
					mServerCommunication.getProductList(
							A0202_ProductListActivity.this,
							mListViewAdapter,
							mProductList, mMaker, mMaterialId);

					// リストを先頭に戻す
					ListView listView = (ListView) findViewById(R.id.listView);
					listView.setSelection(0);
				}
			});

			builder.show();
*/
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Activityのコールバック呼び出し
		((ProductListCallbacks) activity).CategoryCallback(category);

	}

	@Override
	public void onErrorResponse(VolleyError error) {

	}
}
