package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Adapter.CocktailListAdapter;
import jp.ecweb.homes.a1601.dao.FavoriteDAO;
import jp.ecweb.homes.a1601.listener.CocktailCategoryListener;
import jp.ecweb.homes.a1601.callback.CocktailListCallbacks;
import jp.ecweb.homes.a1601.listener.CocktailListListener;
import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.model.Category;
import jp.ecweb.homes.a1601.model.Favorite;

public class A0301_CocktailListActivity extends AppCompatActivity implements CocktailListCallbacks {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private CocktailListAdapter mListViewAdapter;					// ListViewアダプター
	private FavoriteDAO mFavoriteDAO;                               // SQLite お気に入りテーブル

	private List<Cocktail> mCocktailList = new ArrayList<>();		// カクテル一覧
	private Category mCategory = new Category();                    // 選択カテゴリ

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
		MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		// メンバ変数の初期化
		mFavoriteDAO = new FavoriteDAO(this);

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

		// 絞り込み用カテゴリ一覧の取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getCocktailCategory.php";
		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		// Volleyリクエストの作成
		CocktailCategoryListener cocktailCategoryListener = new CocktailCategoryListener(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				url,
				null,
				cocktailCategoryListener,
				cocktailCategoryListener
		);

		// リクエストの送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

		Log.d(LOG_TAG, LOG_CLASSNAME + "onCreate end");
	}

	@Override
	protected void onStart() {
		super.onStart();

		Log.d(LOG_TAG, LOG_CLASSNAME + "onStart start");

		// カクテル一覧の取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getCocktailList.php";
		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		// POSTデータの作成
		JSONObject postData = new JSONObject();
		try {
			postData.put("Category1", mCategory.getCategory1());
			postData.put("Category2", mCategory.getCategory2());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(LOG_TAG, "PostRequest=" + postData.toString());

		// Volleyリクエストの作成
		CocktailListListener cocktailListListener = new CocktailListListener(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				postData,
				cocktailListListener,
				cocktailListListener
		);

		// リクエストの送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

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
		mCategory.resetCategoryAll();

		// カクテル一覧の取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getCocktailList.php";
		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		// POSTデータの作成
		JSONObject postData = new JSONObject();
		try {
			postData.put("Category1", mCategory.getCategory1());
			postData.put("Category2", mCategory.getCategory2());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(LOG_TAG, "PostRequest=" + postData.toString());

		// Volleyリクエストの作成
		CocktailListListener cocktailListListener = new CocktailListListener(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				postData,
				cocktailListListener,
				cocktailListListener
		);

		// リクエストの送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

		// リストを先頭に戻す
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setSelection(0);
	}

	// 五十音
	public void onJapaneseSyllabaryButtonTapped(View view) {
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(A0301_CocktailListActivity.this);

		//ダイアログタイトルをセット
		builder.setTitle("頭文字を選択");

		// 表示項目の作成
		CharSequence[] items = new CharSequence[mCategory.getCategory1List().size()];

		for (int i = 0; i < mCategory.getCategory1List().size(); i++) {
			items[i] = mCategory.getCategory1List().get(i) + "  （" +
						mCategory.getCategory1NumList().get(i) + " 件）";
		}

		// 表示項目・リスナーの登録
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 選択した五十音をセット(ベースの絞り込みは解除)
				mCategory.setCategory1(mCategory.getCategory1List().get(which));
				mCategory.resetCategory2();

				// カクテル一覧の取得
				// WEB API Url
				String url = getString(R.string.server_URL) + "getCocktailList.php";
				Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

				// POSTデータの作成
				JSONObject postData = new JSONObject();
				try {
					postData.put("Category1", mCategory.getCategory1());
					postData.put("Category2", mCategory.getCategory2());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.d(LOG_TAG, "PostRequest=" + postData.toString());

				// Volleyリクエストの作成
				CocktailListListener cocktailListListener =
						new CocktailListListener(A0301_CocktailListActivity.this);

				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
						Request.Method.POST,
						url,
						postData,
						cocktailListListener,
						cocktailListListener
				);

				// リクエストの送信
				NetworkSingleton.getInstance(A0301_CocktailListActivity.this)
						.addToRequestQueue(jsonObjectRequest);

				// リストを先頭に戻す
				ListView listView = (ListView) findViewById(R.id.listView);
				listView.setSelection(0);
			}
		});

		// ダイアログ表示
		builder.show();
	}

	// ベース
	public void onBaseButtonTapped(View view) {
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(A0301_CocktailListActivity.this);

		//ダイアログタイトルをセット
		builder.setTitle("ベースを選択");

		// 表表示項目の作成
		CharSequence[] items = new CharSequence[mCategory.getCategory2List().size()];

		for (int i = 0; i < mCategory.getCategory2List().size(); i++) {
			items[i] = mCategory.getCategory2List().get(i) + "  （" +
					mCategory.getCategory2NumList().get(i) + " 件）";
		}


		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 選択したベースをセット(五十音の絞り込みは解除)
				mCategory.resetCategory1();
				mCategory.setCategory2(mCategory.getCategory2List().get(which));

				// カクテル一覧の取得
				// WEB API Url
				String url = getString(R.string.server_URL) + "getCocktailList.php";
				Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

				// POSTデータの作成
				JSONObject postData = new JSONObject();
				try {
					postData.put("Category1", mCategory.getCategory1());
					postData.put("Category2", mCategory.getCategory2());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.d(LOG_TAG, "PostRequest=" + postData.toString());

				// Volleyリクエストの作成
				CocktailListListener cocktailListListener =
						new CocktailListListener(A0301_CocktailListActivity.this);

				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
						Request.Method.POST,
						url,
						postData,
						cocktailListListener,
						cocktailListListener
				);

				// リクエストの送信
				NetworkSingleton.getInstance(A0301_CocktailListActivity.this)
						.addToRequestQueue(jsonObjectRequest);

				// リストを先頭に戻す
				ListView listView = (ListView) findViewById(R.id.listView);
				listView.setSelection(0);
			}
		});

		// ダイアログ表示
		builder.show();
	}

	// お気に入り
	public void onFavoriteButtonTapped(View view) {

		// お気に入り一覧を取得
//		FavoriteDAO favoriteDAO = new FavoriteDAO(this);
		List<Favorite> favoriteList = mFavoriteDAO.getFavoriteList();

		// カクテル一覧の取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getFavoriteCocktailList.php";
		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		// POSTデータの作成
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < favoriteList.size(); i++) {
			if (i > 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append(favoriteList.get(i).getCocktailId());
		}

		JSONObject postData = new JSONObject();
		try {
			postData.put("id", stringBuilder);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(LOG_TAG, "PostRequest=" + postData.toString());

		// Volleyリクエストの作成
		CocktailListListener cocktailListListener =
				new CocktailListListener(A0301_CocktailListActivity.this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				postData,
				cocktailListListener,
				cocktailListListener
		);

		// リクエストの送信
		NetworkSingleton.getInstance(A0301_CocktailListActivity.this)
				.addToRequestQueue(jsonObjectRequest);

		// リストを先頭に戻す
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setSelection(0);
	}

/*--------------------------------------------------------------------------------------------------
	非同期コールバック処理
--------------------------------------------------------------------------------------------------*/
	// カテゴリ一覧取得処理のコールバック処理
	@Override
	public void CategoryCallback(Category category) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "CategoryCallback start");

		// カテゴリ情報を設定
		mCategory = category;
	}

	// カクテル一覧取得処理のコールバック処理
	@Override
	public void ListResponseCallback(List<Cocktail> cocktailList) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "ListResponseCallback start");

		// ListView表示用カクテル一覧を更新
		mCocktailList.clear();
		mCocktailList.addAll(cocktailList);

		// ListViewに更新を通知
		mListViewAdapter.notifyDataSetChanged();
	}

}
