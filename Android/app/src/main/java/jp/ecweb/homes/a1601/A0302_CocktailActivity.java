package jp.ecweb.homes.a1601;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

	private TextView cocktailNameView;
	private NetworkImageView cocktailImageView;
	private ListView cocktailRecipeView;
	private TextView cocktailDetailView;

	private Cocktail cocktail = new Cocktail();
	private List<Recipe> recipeList = new ArrayList<Recipe>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 画面を縦方向に固定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_a0302__cocktail);

		Intent intent = getIntent();
		String selectedCocktailID = intent.getStringExtra("ID");

// カクテル情報の受信処理
		// サーバーからカクテル情報を取得
		String url = getString(R.string.server_URL) + "getCocktail.php" +
				"?id=" + selectedCocktailID;

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
//						Cocktail cocktail = new Cocktail();

						try {
							cocktail.setId(response.getString("ID"));
							cocktail.setName(response.getString("Name"));
							cocktail.setPhotoUrl(getString(R.string.server_URL) +
									getString(R.string.photo_URL) +
									response.getString("PhotoURL"));
							cocktail.setThumbnailURL(getString(R.string.server_URL) +
									getString(R.string.photo_URL) +
									response.getString("ThumbnailURL"));
							cocktail.setDetail(response.getString("Detail"));
//							cocktail.setRecipeId(response.getString("RecipeID"));
						} catch (JSONException e) {
							e.printStackTrace();
						}

						// カクテル名
						cocktailNameView =
								(TextView) findViewById(R.id.A0302_CocktailNameView);
						cocktailNameView.setText(cocktail.getName());

						// カクテル写真
						cocktailImageView =
								(NetworkImageView) findViewById(R.id.A0302_CocktailImageView);
						ImageLoader imageLoader =
								NetworkSingleton.getInstance(getBaseContext()).getImageLoader();
						cocktailImageView.setImageUrl(cocktail.getPhotoUrl(), imageLoader);

						// カクテル詳細
						cocktailDetailView =
								(TextView) findViewById(R.id.A0302_CocktailDetailView);
						cocktailDetailView.setText(cocktail.getDetail());
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("A1601 onErrorResponse", error.toString());
					}
				}
		);
		// カクテルリスト取得のリクエストを送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

// レシピリストの受信処理
		// サーバーからレシピリストを取得
		url = getString(R.string.server_URL) + "getRecipe.php" +
				"?id=" + String.valueOf(selectedCocktailID);
		Log.d("A0302_CocktailActivity", "url = " + url);

		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						try {
							Log.d("A0302_CocktailActivity",
									"JSON Request Successful num = " +
											String.valueOf(response.length()));

							// JSONをListに展開
							for (int i = 0; i < response.length(); i++) {
								JSONObject jsonObject = response.getJSONObject(i);
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
								recipeList.add(recipe);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						// ListViewのアダプターを登録
						RecipeListAdapter adapter = new RecipeListAdapter(getBaseContext(),
								R.layout.activity_recipe_list_item, recipeList);
						cocktailRecipeView = (ListView) findViewById(R.id.RecipeListView);
						cocktailRecipeView.setAdapter(adapter);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("A1601 onErrorResponse", error.toString());
					}
				}
		);
		// カクテルリスト取得のリクエストを送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
	}
}
