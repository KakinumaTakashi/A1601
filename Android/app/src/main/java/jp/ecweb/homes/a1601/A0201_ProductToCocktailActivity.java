package jp.ecweb.homes.a1601;

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
import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.callback.CocktailListCallbacks;
import jp.ecweb.homes.a1601.dao.HavingProductDAO;
import jp.ecweb.homes.a1601.listener.CocktailListListener;
import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.model.Category;
import jp.ecweb.homes.a1601.model.HavingProduct;

public class A0201_ProductToCocktailActivity extends AppCompatActivity implements CocktailListCallbacks {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private CocktailListAdapter mListViewAdapter;					// アダプター格納用
	private HavingProductDAO mHavingProductDAO;                     // SQLite 所持製品テーブル操作クラス

	private List<Cocktail> mCocktailList = new ArrayList<>();		// リスト表示内容

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Log.d(LOG_TAG, LOG_CLASSNAME + "onCreate start");

		// 画面を縦方向に固定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_a0201__product_to_cocktail);

	    // 広告を表示
	    MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
	    AdView mAdView = (AdView) findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    mAdView.loadAd(adRequest);

	    // メンバ変数の初期化
	    mHavingProductDAO = new HavingProductDAO(this);

	    // ListViewのアダプターを登録
		mListViewAdapter = new CocktailListAdapter(this,
				R.layout.activity_cocktail_list_item, mCocktailList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(mListViewAdapter);

		// アイテムタップ時のリスナーを登録
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
								A0201_ProductToCocktailActivity.this,
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

		// 所持製品テーブルから所持している製品・材料のリストを取得
		List<HavingProduct> productList = mHavingProductDAO.getProductList();

		// カクテル一覧の取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getProductToCocktailList.php";

		// POSTリクエスト用に材料IDを連結
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < productList.size(); i++) {
			if (i > 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append(productList.get(i).getMaterialID());
		}

		// POSTデータ作成
		JSONObject postData = new JSONObject();
		try {
			postData.put("id", stringBuilder.toString());
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
		getMenuInflater().inflate(R.menu.menu_a0201__product_to_cocktail, menu);

		// タップリスナーの登録
		//材料一覧
		menu.findItem(R.id.menu_productlist).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						// 材料一覧画面に遷移
						Intent intent = new Intent(getApplicationContext(),
								A0202_ProductListActivity.class);
						startActivity(intent);
						return true;
					}
				}
		);
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
	非同期コールバック処理
--------------------------------------------------------------------------------------------------*/
	// カテゴリ一覧取得処理のコールバック処理
	@Override
	public void CategoryCallback(Category category) { }

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
