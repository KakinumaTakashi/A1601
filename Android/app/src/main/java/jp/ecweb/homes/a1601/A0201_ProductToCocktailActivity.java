package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class A0201_ProductToCocktailActivity extends AppCompatActivity {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	private ListView mListView;
	private CocktailListAdapter mListViewAdapter;
	private List<Cocktail> mCocktailList = new ArrayList<Cocktail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Log.d(LOG_TAG, LOG_CLASSNAME + "Start onCreate");

		// 画面を縦方向に固定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_a0201__product_to_cocktail);

		// ListViewのアダプターを登録
		mListViewAdapter = new CocktailListAdapter(A0201_ProductToCocktailActivity.this,
				R.layout.activity_cocktail_list_item, mCocktailList);
		mListView = (ListView) findViewById(R.id.listView);
		mListView.setAdapter(mListViewAdapter);

		// アイテムタップ時のリスナーを登録
		mListView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						// タップされたアイテムのカクテルIDを取得
						Cocktail cocktail =
								(Cocktail) parent.getItemAtPosition(position);
						Log.d(LOG_TAG, LOG_CLASSNAME + "Select Cocktail" +
								" ID : " + cocktail.getId() +
								" Name : " + cocktail.getName());

						// 詳細画面に遷移(タップされたカクテルIDを引き渡す)
						Intent intent = new Intent(
								A0201_ProductToCocktailActivity.this,
								A0302_CocktailActivity.class);
						intent.putExtra("ID", cocktail.getId());
						startActivity(intent);
					}
				}
		);

		Log.d(LOG_TAG, LOG_CLASSNAME + "End onCreate");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(LOG_TAG, LOG_CLASSNAME + "Start onStart");

// 所持商品DBから商品リストを取得
		MySQLiteOpenHelper mySQLHelper = new MySQLiteOpenHelper(this);
		SQLiteDatabase database = mySQLHelper.getWritableDatabase();

		StringBuilder stringBuilder = new StringBuilder();
		String sql = "SELECT DISTINCT MaterialID FROM HavingProduct";
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				String value = cursor.getString(cursor.getColumnIndex("MaterialID"));
				if (i > 0) {
					stringBuilder.append(",");
				}
				stringBuilder.append(value);
				cursor.moveToNext();
			}
		}
		database.close();
		Log.d(LOG_TAG, LOG_CLASSNAME + "SQLite検索結果=" +
				stringBuilder.toString());

// サーバーから商品をレシピで使用しているカクテルリストを取得
		// サーバーのカクテルリスト取得URL
		String url = getString(R.string.server_URL) + "getProductToCocktailList.php" +
				"?id=" + stringBuilder.toString();
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
						mListViewAdapter.UpdateItemList(mCocktailList);
/*
						// ListViewのアダプターを登録
						CocktailListAdapter adapter = new CocktailListAdapter(getBaseContext(),
								R.layout.activity_cocktail_list_item, mCocktailList);
						mListView = (ListView) findViewById(R.id.mListView);
						mListView.setAdapter(adapter);

						// アイテムタップ時のリスナーを登録
						mListView.setOnItemClickListener(
								new AdapterView.OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> parent, View view,
															int position, long id) {
										// タップされたアイテムのカクテルIDを取得
										Cocktail cocktail =
												(Cocktail) parent.getItemAtPosition(position);
										Log.d(LOG_TAG, LOG_CLASSNAME + "Select Cocktail" +
												" ID : " + cocktail.getId() +
												" Name : " + cocktail.getName());

										// 詳細画面に遷移(タップされたカクテルIDを引き渡す)
										Intent intent = new Intent(
												A0201_ProductToCocktailActivity.this,
												A0302_CocktailActivity.class);
										intent.putExtra("ID", cocktail.getId());
										startActivity(intent);
									}
								}
						);
*/
					}
				},
				new Response.ErrorListener() {
					// Volley通信エラー処理
					@Override
					public void onErrorResponse(VolleyError error) {
						// エラーメッセージを表示
						AlertDialog.Builder alert =
								new AlertDialog.Builder(A0201_ProductToCocktailActivity.this);
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
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

		Log.d(LOG_TAG, LOG_CLASSNAME + "End onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG, LOG_CLASSNAME + "Start onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(LOG_TAG, LOG_CLASSNAME + "Start onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(LOG_TAG, LOG_CLASSNAME + "Start onStop");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(LOG_TAG, LOG_CLASSNAME + "Start onRestart");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, LOG_CLASSNAME + "Start onDestroy");
	}

	public void onMaterialListButton(View view) {
        Intent intent = new Intent(this, A0202_ProductListActivity.class);
        startActivity(intent);
    }

    public void onBackButtonTapped(View view) {
        finish();
    }
}
