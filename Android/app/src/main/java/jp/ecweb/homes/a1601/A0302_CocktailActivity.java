package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Cocktail.Cocktail;
import jp.ecweb.homes.a1601.Cocktail.Recipe;

public class A0302_CocktailActivity extends AppCompatActivity {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private TextView mCocktailNameView;
	private NetworkImageView mCocktailImageView;
	private ListView mCocktailRecipeView;									// ListView格納用

	private Cocktail mCocktail = new Cocktail();
	private List<Recipe> mRecipeList = new ArrayList<Recipe>();					// リスト表示内容

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
						try {
							Log.d(LOG_TAG, LOG_CLASSNAME +
									"Response=" + response.toString()
							);

							mCocktail.setId(response.getString("ID"));
							mCocktail.setName(response.getString("Name"));
							String PhotoURL = response.getString("PhotoURL");
							if (PhotoURL.equals("")) {
								PhotoURL = getString(R.string.NoImage_URL);
							}
							mCocktail.setPhotoUrl(
									getString(R.string.server_URL) +
									getString(R.string.photo_URL) +
									PhotoURL
							);
							String ThumbnailURL = response.getString("ThumbnailURL");
							if (ThumbnailURL.equals("")) {
								ThumbnailURL = getString(R.string.NoThumbnail_URL);
							}
							mCocktail.setThumbnailURL(
									getString(R.string.server_URL) +
											getString(R.string.photo_URL) +
											ThumbnailURL
							);
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
								NetworkSingleton.getInstance(getBaseContext()).getImageLoader();
						mCocktailImageView.setImageUrl(mCocktail.getPhotoUrl(), imageLoader);
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

		// サーバーからレシピリストを取得
		url = getString(R.string.server_URL) + "getRecipe.php" +
				"?id=" + String.valueOf(selectedCocktailID);

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						try {
							// JSONをListに展開
							for (int i = 0; i < response.length(); i++) {
								JSONObject jsonObject = response.getJSONObject(i);

								Log.d(LOG_TAG, LOG_CLASSNAME +
										"Response[" + String.valueOf(i) + "]=" +
										jsonObject.toString()
								);

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
						} catch (JSONException e) {
							e.printStackTrace();
						}

						// ListViewのアダプターを登録
						RecipeListAdapter adapter = new RecipeListAdapter(getBaseContext(),
								R.layout.activity_recipe_list_item, mRecipeList);
						mCocktailRecipeView = (ListView) findViewById(R.id.RecipeListView);
						mCocktailRecipeView.setAdapter(adapter);
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
								"レシピ情報の取得に失敗しました。(" +
								error.toString() + ")"
						);
					}
				}
		);
		// カクテルリスト取得のリクエストを送信
		NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
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
