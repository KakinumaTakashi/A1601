package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ecweb.homes.a1601.Adapter.CocktailListAdapter;
import jp.ecweb.homes.a1601.Cocktail.Cocktail;
import jp.ecweb.homes.a1601.Database.MySQLiteOpenHelper;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.Network.ServerCommunication;

public class A0201_ProductToCocktailActivity extends AppCompatActivity {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private CocktailListAdapter mListViewAdapter;					// アダプター格納用
	private List<Cocktail> mCocktailList = new ArrayList<>();		// リスト表示内容
	private Map<String, SpannableStringBuilder> mRecipeList = new HashMap<>();
																	// リスト表示内容(レシピ情報)

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
	    MobileAds.initialize(getApplicationContext(), "ca-app-pub-2276647365248742~3207890318");
	    AdView mAdView = (AdView) findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    mAdView.loadAd(adRequest);

		// ListViewのアダプターを登録
		mListViewAdapter = new CocktailListAdapter(this,
				R.layout.activity_cocktail_list_item, mCocktailList);
//	    mListViewAdapter = new CocktailListAdapter(A0201_ProductToCocktailActivity.this);
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

		final List<String> havingProduct = new ArrayList<>();

		// 所持商品DB(Local SQLite)から所持している材料IDのリストを取得
		MySQLiteOpenHelper mySQLHelper = new MySQLiteOpenHelper(this);
		SQLiteDatabase database = mySQLHelper.getWritableDatabase();

		StringBuilder stringBuilder = new StringBuilder();
		String sql = "SELECT DISTINCT MaterialID FROM HavingProduct";
		Cursor cursor = database.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				// リクエスト用に材料IDを連結
				String value = cursor.getString(cursor.getColumnIndex("MaterialID"));
				if (i > 0) {
					stringBuilder.append(",");
				}
				stringBuilder.append(value);

				// ビュー表示用にリストを作成
				havingProduct.add(value);

				cursor.moveToNext();
			}
		}

		cursor.close();
		database.close();

		Log.d(LOG_TAG, LOG_CLASSNAME + "SQLite検索結果=" +
				stringBuilder.toString());

		// リスト生成
		ServerCommunication serverCommunication = new ServerCommunication();
		serverCommunication.getProductToCocktailList(this, mListViewAdapter,
				mCocktailList, havingProduct);

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
}
