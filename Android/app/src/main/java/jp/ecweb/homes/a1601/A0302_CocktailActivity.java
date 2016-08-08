package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Adapter.RecipeListAdapter;
import jp.ecweb.homes.a1601.Cocktail.Cocktail;
import jp.ecweb.homes.a1601.Cocktail.Recipe;
import jp.ecweb.homes.a1601.Database.MySQLiteOpenHelper;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;

public class A0302_CocktailActivity extends AppCompatActivity {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private RecipeListAdapter mListViewAdapter;					// アダプター格納用

	private TextView mCocktailNameView;                     // カクテル名
	private NetworkImageView mCocktailImageView;            // 写真
	private TextView mMethodsTextView;                      // 製法
	private TextView mGrassTextView;                        // グラス
	private TextView mAlcoholDegreeTextView;                // アルコール度数
	private ListView mCocktailRecipeView;					// 材料
	private TextView mHowToTextView;                        // 作り方

	private Cocktail mCocktail = new Cocktail();
	private List<Recipe> mRecipeList = new ArrayList<>();	// リスト表示内容

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(LOG_TAG, LOG_CLASSNAME + "onCreate start");

		// 画面を縦方向に固定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_a0302__cocktail);

		// 広告を表示
		MobileAds.initialize(getApplicationContext(), "ca-app-pub-2276647365248742~3207890318");
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		// インテントからカクテルIDを取得
		Intent intent = getIntent();
		String selectedCocktailID = intent.getStringExtra("ID");

		// サーバーからカクテル情報を取得
		String url = getString(R.string.server_URL) + "getCocktail.php" +
				"?id=" + selectedCocktailID;

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						Log.d(LOG_TAG, LOG_CLASSNAME +
								"Response=" + response.toString()
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

							mCocktail.setId(data.getString("ID"));
							mCocktail.setName(data.getString("Name"));
							String PhotoURL = data.getString("PhotoURL");
							if (PhotoURL.equals("")) {
								PhotoURL = getString(R.string.NoImage_URL);
							}
							mCocktail.setPhotoUrl(
									getString(R.string.server_URL) +
									getString(R.string.photo_URL) +
									PhotoURL
							);
							String ThumbnailURL = data.getString("ThumbnailURL");
							if (ThumbnailURL.equals("")) {
								ThumbnailURL = getString(R.string.NoThumbnail_URL);
							}
							mCocktail.setThumbnailUrl(
									getString(R.string.server_URL) +
											getString(R.string.photo_URL) +
											ThumbnailURL
							);
							mCocktail.setMethod(data.getString("Method"));
							mCocktail.setGrass(data.getString("Grass"));
							mCocktail.setAlcoholDegree((float) data.getDouble("AlcoholDegree"));
							mCocktail.setHowTo(data.getString("HowTo"));

							// レシピ部処理
							JSONArray jsonArray = data.getJSONArray("Recipes");

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								Recipe recipe = new Recipe();

								recipe.setId(jsonObject.getString("ID"));
								recipe.setCocktailID(jsonObject.getString("CocktailID"));
								recipe.setMatelialID(jsonObject.getString("MatelialID"));
								recipe.setCategory1(jsonObject.getString("Category1"));
								recipe.setCategory2(jsonObject.getString("Category2"));
								recipe.setCategory3(jsonObject.getString("Category3"));
								recipe.setMatelialName(jsonObject.getString("Name"));
								recipe.setQuantity(jsonObject.getInt("Quantity"));
								recipe.setUnit(jsonObject.getString("Unit"));

								mRecipeList.add(recipe);
							}
							mListViewAdapter.notifyDataSetChanged();

						} catch (JSONException e) {
							e.printStackTrace();
						}

						// カクテル名
						mCocktailNameView =
								(TextView) findViewById(R.id.A0302_CocktailNameView);
						mCocktailNameView.setText(mCocktail.getName());

						// カクテル写真
						mCocktailImageView =
								(NetworkImageView) findViewById(R.id.A0302_CocktailImageView);
						ImageLoader imageLoader =
								NetworkSingleton.getInstance(getApplicationContext()).getImageLoader();
						mCocktailImageView.setImageUrl(mCocktail.getPhotoUrl(), imageLoader);

						// 製法
						mMethodsTextView =
								(TextView) findViewById(R.id.A0302_MethodsTextView);
						mMethodsTextView.setText(mCocktail.getMethod());

						// グラス
						mGrassTextView =
								(TextView) findViewById(R.id.A0302_GrassTextView);
						mGrassTextView.setText(mCocktail.getGrass());

						// アルコール度数
						mAlcoholDegreeTextView =
								(TextView) findViewById(R.id.A0302_AlcoholDegreeTextView);
						mAlcoholDegreeTextView.setText(
								String.valueOf(mCocktail.getAlcoholDegree()) + " %"
						);

						// 作り方
						mHowToTextView =
								(TextView) findViewById(R.id.A0302_HowToTextView);
						mHowToTextView.setText(mCocktail.getHowTo().replaceAll("\\n", "\n"));

					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// エラーメッセージを表示
						AlertDialog.Builder alert =
								new AlertDialog.Builder(A0302_CocktailActivity.this);
						alert.setTitle(R.string.ERR_VolleyTitle_text);
						alert.setMessage(R.string.ERR_VolleyMessage_text);
						alert.setPositiveButton("OK", null);
						alert.show();

						Log.e(LOG_TAG, LOG_CLASSNAME +
								"カクテル情報の取得に失敗しました。(" +
								error.toString() + ")"
						);
					}
				}
		);
		// カクテルリスト取得のリクエストを送信
		NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

		// ListViewのアダプターを登録
		mListViewAdapter = new RecipeListAdapter(A0302_CocktailActivity.this,
				R.layout.activity_recipe_list_item, mRecipeList);
		mCocktailRecipeView = (ListView) findViewById(R.id.RecipeListView);
		mCocktailRecipeView.setAdapter(mListViewAdapter);


		// お気に入り
		ToggleButton favoriteButton = (ToggleButton) findViewById(R.id.favoriteButton);

		// 持っているボタンの初期値を所持製品DBから取得
		final MySQLiteOpenHelper mSQLiteHelper = new MySQLiteOpenHelper(this);
		SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();

		// カクテルIDをキーにDBを検索
		String sql =
				"SELECT CocktailID FROM favorite WHERE CocktailID=" +
						"\"" + selectedCocktailID + "\"";
		Cursor cursor= database.rawQuery(sql, null);
		// カクテルIDがDBに登録されていたらボタンの初期値をON
		if (cursor.moveToFirst()) {
			favoriteButton.setChecked(true);
		} else {
			favoriteButton.setChecked(false);
		}

		cursor.close();
		database.close();

		// お気に入りボタンにカクテルIDをタグ付け
		favoriteButton.setTag(R.string.TAG_CocktailID_Key, selectedCocktailID);

		// お気に入りボタンタップ時のリスナーを登録
		favoriteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;

				SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();

				if (btn.isChecked()) {
					// ボタンがONになった場合 所持製品DBに製品ID・素材IDを登録
					ContentValues values = new ContentValues();
					values.put("CocktailID", (String) btn.getTag(R.string.TAG_CocktailID_Key));

					database.insert("favorite", null, values);
				} else {
					// ボタンがOFFになった場合 所持製品DBから製品ID・素材IDを削除
					database.delete("favorite", "CocktailID=?",
							new String[]{(String) btn.getTag(R.string.TAG_CocktailID_Key)});
				}

				database.close();
			}
		});

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
		getMenuInflater().inflate(R.menu.menu_a0302__cocktail, menu);

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
