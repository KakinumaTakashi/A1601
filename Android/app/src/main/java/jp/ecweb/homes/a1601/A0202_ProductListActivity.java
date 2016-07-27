package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import jp.ecweb.homes.a1601.Cocktail.Product;

public class A0202_ProductListActivity extends AppCompatActivity {

    // ログ出力
    private final String LOG_TAG = "A1601";
    private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

    // メンバ変数
    private ListView mListView;                                        // ListView格納用
    private List<Product> mProductList = new ArrayList<Product>();           // リスト表示内容

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

        // サーバーの商品リスト取得URL
        String url = getString(R.string.server_URL) + "getProductList.php";

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

        // 商品リストの受信処理(JSONをListViewに表示)
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
							// リストをクリア
							mProductList.clear();

                            // JSONをListに展開
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

								Log.d(LOG_TAG, LOG_CLASSNAME +
										"Response[" + String.valueOf(i) + "]=" +
										jsonObject.toString()
								);

								Product product = new Product();
                                product.setId(jsonObject.getString("ID"));
                                product.setMaterialID(jsonObject.getString("MatelialID"));
                                product.setCategory1(jsonObject.getString("Category1"));
                                product.setCategory2(jsonObject.getString("Category2"));
                                product.setCategory3(jsonObject.getString("Category3"));
                                product.setMaterialName(jsonObject.getString("MaterialName"));
                                product.setName(jsonObject.getString("Name"));
                                product.setMaker(jsonObject.getString("Maker"));
                                product.setBrand(jsonObject.getString("Brand"));
                                product.setCapacity(jsonObject.getLong("Capacity"));
                                product.setUnit(jsonObject.getString("Unit"));
                                product.setAlcoholDegree(
                                        (float) jsonObject.getDouble("AlcoholDegree"));

                                String ThumbnailURL = jsonObject.getString("ThumbnailURL");
                                if (ThumbnailURL.equals("")) {
                                    ThumbnailURL = getString(R.string.NoThumbnail_URL);
                                }
                                product.setThumbnailURL(
										getString(R.string.server_URL) +
										getString(R.string.photo_URL) +
										ThumbnailURL
								);

                                String PhotoURL = jsonObject.getString("PhotoURL");
                                if (PhotoURL.equals("")) {
                                    PhotoURL = getString(R.string.NoImage_URL);
                                }
                                product.setPhotoURL(getString(R.string.server_URL) +
										getString(R.string.photo_URL) +
										PhotoURL
								);

                                // リストを更新
								mProductList.add(product);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // ListViewのアダプターを登録
                        ProductListAdapter adapter = new ProductListAdapter(getBaseContext(),
                                R.layout.activity_product_list_item, mProductList);
                        mListView = (ListView) findViewById(R.id.listView);
                        mListView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
						// エラーメッセージを表示
						AlertDialog.Builder alert =
								new AlertDialog.Builder(A0202_ProductListActivity.this);
						alert.setTitle(R.string.ERR_VolleyTitle_text);
						alert.setMessage(R.string.ERR_VolleyMessage_text);
						alert.setPositiveButton("OK", null);
						alert.show();

						Log.e(LOG_TAG, LOG_CLASSNAME +
								"商品リストの取得に失敗しました。(" +
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
}
