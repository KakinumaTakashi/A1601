package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Adapter.CocktailListAdapter;
import jp.ecweb.homes.a1601.Cocktail.Cocktail;
import jp.ecweb.homes.a1601.Database.MySQLiteOpenHelper;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.Network.ServerCommunication;

public class A0301_CocktailListActivity extends AppCompatActivity {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private CocktailListAdapter mListViewAdapter;					// アダプター格納用
	ServerCommunication mServerCommunication = new ServerCommunication();

	private List<Cocktail> mCocktailList = new ArrayList<>();		// リスト表示内容(カクテル情報)
	private CharSequence[] mJapaneseSyllabaryItems;
	private List<String> mJapaneseSyllabary;
	private CharSequence[] mBaseItems;
	private List<String> mBase;

	private String mCategory1 = "All";
	private String mCategory2 = "All";

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

		// 広告を表示
		MobileAds.initialize(getApplicationContext(), "ca-app-pub-2276647365248742~3207890318");
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		// ListViewのアダプターを登録
		mListViewAdapter = new CocktailListAdapter(this,
				R.layout.activity_cocktail_list_item, mCocktailList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(mListViewAdapter);

		// アイテムのリスナーを登録(詳細画面にカクテルIDを渡して遷移)
		listView.setOnItemClickListener(
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

		Log.d(LOG_TAG, LOG_CLASSNAME + "onCreate end");
	}

	@Override
	protected void onStart() {
		super.onStart();

		Log.d(LOG_TAG, LOG_CLASSNAME + "onStart start");

		// リスト生成
		mServerCommunication.getCocktailList(this, mListViewAdapter,
				mCocktailList, mCategory1, mCategory2);

		Log.d(LOG_TAG, LOG_CLASSNAME + "onStart end");
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

/*--------------------------------------------------------------------------------------------------
	ボタンタップイベント処理
--------------------------------------------------------------------------------------------------*/
	// 全て
	public void onAllButtonTapped(View view) {
		// 絞り込みをリセット
		mCategory1 = "All";
		mCategory2 = "All";

		// ListViewを更新
		mServerCommunication.getCocktailList(this, mListViewAdapter,
				mCocktailList, mCategory1, mCategory2);
	}

	// 五十音
	public void onJapaneseSyllabaryButtonTapped(View view) {

		// サーバーの五十音取得URL
		String url = getString(R.string.server_URL) + "getCocktailCategory1.php";

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		// 五十音の受信処理(JSONレスポンスをダイアログに表示)
		final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

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
							JSONArray data = response.getJSONArray("data");

							mJapaneseSyllabaryItems = new CharSequence[data.length()];
							mJapaneseSyllabary = new ArrayList<>();

							for (int i = 0; i < data.length(); i++) {
								JSONObject jsonObject = data.getJSONObject(i);

								mJapaneseSyllabaryItems[i] =
										jsonObject.getString("Category1") + "  （" +
										jsonObject.getString("Category1Num") + " 件）";

								mJapaneseSyllabary.add(i, jsonObject.getString("Category1"));
							}

							// ダイアログを表示
							AlertDialog.Builder builder =
									new AlertDialog.Builder(A0301_CocktailListActivity.this);

							//ダイアログタイトルをセット
							builder.setTitle("頭文字を選択");
							builder.setCancelable(false);

							// 表示項目とリスナの設定
							builder.setItems(mJapaneseSyllabaryItems, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// 選択した五十音をセット(ベースの絞り込みは解除)
									mCategory1 = mJapaneseSyllabary.get(which);
									mCategory2 = "All";
									// ListViewを更新
									mServerCommunication.getCocktailList(
											A0301_CocktailListActivity.this,
											mListViewAdapter,
											mCocktailList, mCategory1, mCategory2);
								}
							});

							builder.show();

						} catch (JSONException e) {
							// エラーメッセージを表示
							AlertDialog.Builder alert =
									new AlertDialog.Builder(A0301_CocktailListActivity.this);
							alert.setTitle(R.string.ERR_VolleyTitle_text);
							alert.setMessage(R.string.ERR_VolleyMessage_text);
							alert.setPositiveButton("OK", null);
							alert.show();

							Log.e(LOG_TAG, LOG_CLASSNAME +
									"五十音情報の解析に失敗しました。(" +
									e.toString() + ")"
							);

							e.printStackTrace();
						}
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
								"五十音情報の取得に失敗しました。(" +
								error.toString() + ")"
						);
					}
				}
		);
		// 五十音取得のリクエストを送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
	}

	// ベース
	public void onBaseButtonTapped(View view) {

		// サーバーのベース取得URL
		String url = getString(R.string.server_URL) + "getCocktailCategory2.php";

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		// ベースの受信処理(JSONレスポンスをダイアログに表示)
		final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

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
							JSONArray data = response.getJSONArray("data");

							mBaseItems = new CharSequence[data.length()];
							mBase = new ArrayList<>();

							for (int i = 0; i < data.length(); i++) {
								JSONObject jsonObject = data.getJSONObject(i);

								mBaseItems[i] =
										jsonObject.getString("Category2") + "  （" +
										jsonObject.getString("Category2Num") + " 件）";

								mBase.add(i, jsonObject.getString("Category2"));
							}

							// ダイアログを表示
							AlertDialog.Builder builder =
									new AlertDialog.Builder(A0301_CocktailListActivity.this);

							//ダイアログタイトルをセット
							builder.setTitle("ベースを選択");
							builder.setCancelable(false);

							// 表示項目とリスナの設定
							builder.setItems(mBaseItems, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// 選択したベースをセット(五十音の絞り込みは解除)
									mCategory1 = "All";
									mCategory2 = mBase.get(which);
									// ListViewを更新
									mServerCommunication.getCocktailList(
											A0301_CocktailListActivity.this,
											mListViewAdapter,
											mCocktailList, mCategory1, mCategory2);
								}
							});

							builder.show();

						} catch (JSONException e) {
							// エラーメッセージを表示
							AlertDialog.Builder alert =
									new AlertDialog.Builder(A0301_CocktailListActivity.this);
							alert.setTitle(R.string.ERR_VolleyTitle_text);
							alert.setMessage(R.string.ERR_VolleyMessage_text);
							alert.setPositiveButton("OK", null);
							alert.show();

							Log.e(LOG_TAG, LOG_CLASSNAME +
									"ベース情報の解析に失敗しました。(" +
									e.toString() + ")"
							);

							e.printStackTrace();
						}
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
								"ベース情報の取得に失敗しました。(" +
								error.toString() + ")"
						);
					}
				}
		);
		// ベース取得のリクエストを送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
	}

	// お気に入り
	public void onFavoriteButtonTapped(View view) {

		// お気に入りDBを開く
		MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
		SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();

		// カクテルIDを取得
		String sql = "SELECT CocktailID FROM favorite";
		Cursor cursor= database.rawQuery(sql, null);
		cursor.moveToFirst();

		// カクテルIDをListに格納
		List<String> cocktailID = new ArrayList<>();

		for (int i = 0; i < cursor.getCount(); i++) {
			String value = cursor.getString(cursor.getColumnIndex("CocktailID"));
			cocktailID.add(value);
			cursor.moveToNext();
		}

		cursor.close();
		database.close();

		// ListViewを更新
		mServerCommunication.getFavoriteCocktailList(this, mListViewAdapter,
				mCocktailList, cocktailID);
	}

/*--------------------------------------------------------------------------------------------------
	Activity固有処理
--------------------------------------------------------------------------------------------------*/

}
