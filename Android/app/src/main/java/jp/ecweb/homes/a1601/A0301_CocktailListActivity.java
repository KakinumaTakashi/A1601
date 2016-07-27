package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Cocktail.Cocktail;

public class A0301_CocktailListActivity extends AppCompatActivity {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private ListView mListView;										// ListView格納用
	private List<Cocktail> mCocktailList = new ArrayList<Cocktail>();		// リスト表示内容

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(LOG_TAG, LOG_CLASSNAME + "onCreate start");

		// 画面を縦方向に固定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_a0301__cocktail_list);

		// サーバーのカクテルリスト取得URL
		String url = getString(R.string.server_URL) + "getCocktailList.php";

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		// カクテルリストの受信処理(JSONをListViewに表示)
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						try {
							// リストをクリア
							mCocktailList.clear();

							// JSONをListに展開
							for (int i = 0; i < response.length(); i++) {
								JSONObject jsonObject = response.getJSONObject(i);

								Log.d(LOG_TAG, LOG_CLASSNAME +
										"Response[" + String.valueOf(i) + "]=" +
										jsonObject.toString()
								);

								Cocktail cocktail = new Cocktail();
								cocktail.setId(jsonObject.getString("ID"));
								cocktail.setName(jsonObject.getString("Name"));
								String ThumbnailURL = jsonObject.getString("ThumbnailURL");
								if (ThumbnailURL.equals("")) {
									ThumbnailURL = getString(R.string.NoThumbnail_URL);
								}
								cocktail.setThumbnailURL(
										getString(R.string.server_URL) +
												getString(R.string.photo_URL) +
												ThumbnailURL
								);

								mCocktailList.add(cocktail);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						// ListViewのアダプターを登録
						CocktailListAdapter adapter = new CocktailListAdapter(getBaseContext(),
								R.layout.activity_cocktail_list_item, mCocktailList);
						mListView = (ListView) findViewById(R.id.listView);
						mListView.setAdapter(adapter);

						// アイテムのリスナーを登録(詳細画面にカクテルIDを渡して遷移)
						mListView.setOnItemClickListener(
								new AdapterView.OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> parent, View view,
															int position, long id) {
										// タップされたアイテムのカクテルIDを取得
										Cocktail cocktail =
												(Cocktail) parent.getItemAtPosition(position);

										Log.d(LOG_TAG, LOG_CLASSNAME + "Select Cocktail=" +
												"ID:" + cocktail.getId() +
												"/Name:" + cocktail.getName()
										);

										// 詳細画面に遷移(タップされたカクテルIDを引き渡す)
										Intent intent = new Intent(
												A0301_CocktailListActivity.this,
												A0302_CocktailActivity.class);
										intent.putExtra("ID", cocktail.getId());
										startActivity(intent);
									}
								}
						);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// エラーメッセージを表示
						AlertDialog.Builder alert =
								new AlertDialog.Builder(A0301_CocktailListActivity.this);
						alert.setTitle(R.string.ERR_VolleyTitle_text);
						alert.setMessage(R.string.ERR_VolleyMessage_text);
						alert.setPositiveButton("OK", null);
						alert.show();

						Log.e(LOG_TAG, LOG_CLASSNAME +
								"カクテルリストの取得に失敗しました。(" +
								error.toString() + ")"
						);
					}
				}
		);

		// カクテルリスト取得のリクエストを送信
		NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);

		Log.d(LOG_TAG, LOG_CLASSNAME + "onCreate end");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(LOG_TAG, LOG_CLASSNAME + "onStart start");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG, LOG_CLASSNAME + "onResume start");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(LOG_TAG, LOG_CLASSNAME + "onPause start");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(LOG_TAG, LOG_CLASSNAME + "onStop start");

		// サーバーへのリクエストをストップ
		NetworkSingleton.getInstance(getApplicationContext()).cancelAll();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(LOG_TAG, LOG_CLASSNAME + "onRestart start");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, LOG_CLASSNAME + "onDestroy start");
	}

/*--------------------------------------------------------------------------------------------------
	メニューイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// リソースの登録
		getMenuInflater().inflate(R.menu.menu_a0301__cocktail_list, menu);

		// タップリスナーの登録
		// 戻る
		menu.findItem(R.id.menu_back).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						finish();
						return true;
					}
				}
		);

		return true;
	}

}
