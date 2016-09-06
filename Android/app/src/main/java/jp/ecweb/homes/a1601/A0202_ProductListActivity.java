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

import jp.ecweb.homes.a1601.Adapter.ProductListAdapter;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.callback.ProductListCallbacks;
import jp.ecweb.homes.a1601.dao.HavingProductDAO;
import jp.ecweb.homes.a1601.listener.ProductCategoryListener;
import jp.ecweb.homes.a1601.listener.ProductListListener;
import jp.ecweb.homes.a1601.model.Category;
import jp.ecweb.homes.a1601.model.HavingProduct;
import jp.ecweb.homes.a1601.model.Product;

public class A0202_ProductListActivity extends AppCompatActivity implements ProductListCallbacks {

    // ログ出力
    private final String LOG_TAG = "A1601";
    private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

    // メンバ変数
	private ProductListAdapter mListViewAdapter;					// アダプター格納用
	private HavingProductDAO mHavingProductDAO;                     // 所持製品テーブル操作クラス

    private List<Product> mProductList = new ArrayList<>();         // 商品一覧
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
        setContentView(R.layout.activity_a0202__product_list);

	    // 広告を表示
	    MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
	    AdView mAdView = (AdView) findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    mAdView.loadAd(adRequest);

	    // メンバ変数の初期化
	    mHavingProductDAO = new HavingProductDAO(this);

	    // ListViewのアダプターを登録
	    mListViewAdapter = new ProductListAdapter(getBaseContext(),
			    R.layout.activity_product_list_item, mProductList);
		ListView listView = (ListView) findViewById(R.id.listView);
	    listView.setAdapter(mListViewAdapter);

	    // アイテムタップ時のリスナーを登録
	    listView.setOnItemClickListener(
			    new AdapterView.OnItemClickListener() {
				    @Override
				    public void onItemClick(AdapterView<?> parent, View view,
				                            int position, long id) {
					    // タップされたアイテムのインスタンスを取得
					    Product product = (Product) parent.getItemAtPosition(position);

					    Log.d(LOG_TAG, LOG_CLASSNAME + "Select product=" +
							    "ID:" + product.getId() +
							    "/Name:" + product.getName()
					    );

					    // 詳細画面に遷移
					    Intent intent = new Intent(
							    A0202_ProductListActivity.this,
							    A0203_ProductActivity.class);
					    intent.putExtra("itemCode", product.getItemCode());
					    intent.putExtra("productId", product.getId());
					    intent.putExtra("materialId", product.getMaterialID());

					    startActivity(intent);
				    }
			    }
	    );

	    // 絞り込み用カテゴリ一覧の取得
	    // WEB API Url
	    String url = getString(R.string.server_URL) + "getProductCategory.php";
	    Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

	    // Volleyリクエストの作成
	    ProductCategoryListener productCategoryListener = new ProductCategoryListener(this);

	    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
			    Request.Method.GET,
			    url,
			    null,
			    productCategoryListener,
			    productCategoryListener
	    );

	    // リクエストの送信
	    NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

	    Log.d(LOG_TAG, LOG_CLASSNAME + "onCreate end");
    }

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(LOG_TAG, LOG_CLASSNAME + "onStart start");

		// 商品リストの取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getProductList.php";

		// POSTデータ作成
		JSONObject postData = new JSONObject();
		try {
			postData.put("Maker", mCategory.getCategory1());
			postData.put("MaterialId", mCategory.getCategory2());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Volleyリクエストの作成
		ProductListListener productListListener = new ProductListListener(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				postData,
				productListListener,
				productListListener
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
        getMenuInflater().inflate(R.menu.menu_a0202__product_list, menu);

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
		Log.d(LOG_TAG, LOG_CLASSNAME + "onAllButtonTapped start");

		// 絞り込みをリセット
		mCategory.resetCategoryAll();

		// 商品リストの取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getProductList.php";

		// POSTデータ作成
		JSONObject postData = new JSONObject();
		try {
			postData.put("Maker", mCategory.getCategory1());
			postData.put("MaterialId", mCategory.getCategory2());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Volleyリクエストの作成
		ProductListListener productListListener = new ProductListListener(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				postData,
				productListListener,
				productListListener
		);

		// リクエストの送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

		// リストを先頭に戻す
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setSelection(0);
	}

	// 製造・販売
	public void onMakerButtonTapped(View view) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onMakerButtonTapped start");

		CharSequence[] makerListItems = new CharSequence[mCategory.getCategory1List().size()];

		for (int i = 0; i < mCategory.getCategory1List().size(); i++) {
			makerListItems[i] = mCategory.getCategory1List().get(i) + "  （" +
					mCategory.getCategory1NumList().get(i) + " 件）";
		}

		// ダイアログを表示
		AlertDialog.Builder builder =
				new AlertDialog.Builder(A0202_ProductListActivity.this);

		//ダイアログタイトルをセット
		builder.setTitle("製造/販売会社を選択");

		// 表示項目とリスナの設定
		builder.setItems(makerListItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 選択した製造/販売会社をセット(カテゴリの絞り込みは解除)
				mCategory.setCategory1(mCategory.getCategory1ValueList().get(which));
				mCategory.resetCategory2();

				// 商品リストの取得
				// WEB API Url
				String url = getString(R.string.server_URL) + "getProductList.php";

				// POSTデータ作成
				JSONObject postData = new JSONObject();
				try {
					postData.put("Maker", mCategory.getCategory1());
					postData.put("MaterialId", mCategory.getCategory2());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.d(LOG_TAG, "PostRequest=" + postData.toString());

				// Volleyリクエストの作成
				ProductListListener productListListener =
						new ProductListListener(A0202_ProductListActivity.this);

				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
						Request.Method.POST,
						url,
						postData,
						productListListener,
						productListListener
				);

				// リクエストの送信
				NetworkSingleton.getInstance(A0202_ProductListActivity.this).
						addToRequestQueue(jsonObjectRequest);

				// リストを先頭に戻す
				ListView listView = (ListView) findViewById(R.id.listView);
				listView.setSelection(0);
			}
		});

		builder.show();
	}

	// 材料カテゴリ
	public void onCategoryButtonTapped(View view) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onCategoryButtonTapped start");

		CharSequence[] categoryListItems = new CharSequence[mCategory.getCategory2List().size()];

		for (int i = 0; i < mCategory.getCategory2List().size(); i++) {
			// 表示用文字列構築
			categoryListItems[i] = mCategory.getCategory2List().get(i) + "  （" +
					mCategory.getCategory2NumList().get(i) + " 件）";
		}

		// ダイアログを表示
		AlertDialog.Builder builder =
				new AlertDialog.Builder(A0202_ProductListActivity.this);

		//ダイアログタイトルをセット
		builder.setTitle("材料カテゴリを選択");

		// 表示項目とリスナの設定
		builder.setItems(categoryListItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 選択した材料カテゴリをセット(製造/販売会社の絞り込みは解除)
				mCategory.resetCategory1();
				mCategory.setCategory2(mCategory.getCategory2ValueList().get(which));

				// 商品リストの取得
				// WEB API Url
				String url = getString(R.string.server_URL) + "getProductList.php";

				// POSTデータ作成
				JSONObject postData = new JSONObject();
				try {
					postData.put("Maker", mCategory.getCategory1());
					postData.put("MaterialId", mCategory.getCategory2());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.d(LOG_TAG, "PostRequest=" + postData.toString());

				// Volleyリクエストの作成
				ProductListListener productListListener =
						new ProductListListener(A0202_ProductListActivity.this);

				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
						Request.Method.POST,
						url,
						postData,
						productListListener,
						productListListener
				);

				// リクエストの送信
				NetworkSingleton.getInstance(A0202_ProductListActivity.this).
						addToRequestQueue(jsonObjectRequest);

				// リストを先頭に戻す
				ListView listView = (ListView) findViewById(R.id.listView);
				listView.setSelection(0);
			}
		});

		builder.show();
	}

	// 持っている
	public void onHoldButtonTapped(View view) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onHoldButtonTapped start");

		// 所持商品IDを取得
		List<HavingProduct> havingProductList = mHavingProductDAO.getProductList();

		// 商品リストの取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getProductHaveList.php";

		// POST用のデータ作成
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < havingProductList.size(); i++) {
			if (i > 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append(havingProductList.get(i).getProductID());
		}

		JSONObject postData = new JSONObject();
		try {
			postData.put("id", stringBuilder);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Volleyリクエストの作成
		ProductListListener productListListener =
				new ProductListListener(A0202_ProductListActivity.this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				postData,
				productListListener,
				productListListener
		);

		// リクエストの送信
		NetworkSingleton.getInstance(A0202_ProductListActivity.this).
				addToRequestQueue(jsonObjectRequest);

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

	// 商品一覧取得処理のコールバック処理
	@Override
	public void ListResponseCallback(List<Product> productList) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "ListResponseCallback start");

		// ListView表示用商品一覧を更新
		mProductList.clear();
		mProductList.addAll(productList);

		// ListViewに更新を通知
		mListViewAdapter.notifyDataSetChanged();
	}
}

