package jp.ecweb.homes.a1601.listener;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.callback.CocktailCallbacks;
import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.model.Recipe;

/**
 * カクテル情報　非同期通信用リスナー
 *
 * Created by KakinumaTakashi on 2016/09/05.
 */
public class CocktailListener implements Response.Listener<JSONObject>, Response.ErrorListener {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	private Activity activity;


	public CocktailListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onResponse(JSONObject response) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onResponse start");

		Cocktail cocktail = new Cocktail();

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

			cocktail.setId(data.getString("ID"));
			cocktail.setName(data.getString("NAME"));
			cocktail.setPhotoUrl(data.getString("PHOTOURL"));
			cocktail.setThumbnailUrl(data.getString("THUMBNAILURL"));
			cocktail.setMethod(data.getString("METHOD"));
			cocktail.setGrass(data.getString("GRASS"));
			cocktail.setAlcoholDegree((float) data.getDouble("ALCOHOLDEGREE"));
			cocktail.setHowTo(data.getString("HOWTO"));
			cocktail.setCopylight(data.getString("COPYLIGHT"));

			// レシピ部処理
			List<Recipe> recipeList = new ArrayList<>();
			JSONArray jsonArray = data.getJSONArray("RECIPES");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Recipe recipe = new Recipe();

				recipe.setId(jsonObject.getString("ID"));
				recipe.setCocktailID(jsonObject.getString("COCKTAILID"));
				recipe.setMatelialID(jsonObject.getString("MATERIALID"));
				recipe.setCategory1(jsonObject.getString("CATEGORY1"));
				recipe.setCategory2(jsonObject.getString("CATEGORY2"));
				recipe.setCategory3(jsonObject.getString("CATEGORY3"));
				recipe.setMatelialName(jsonObject.getString("NAME"));
				recipe.setQuantity(jsonObject.getInt("QUANTITY"));
				recipe.setUnit(jsonObject.getString("UNIT"));

				recipeList.add(recipe);
			}
			cocktail.setRecipes(recipeList);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Activityのコールバック呼び出し
		((CocktailCallbacks) activity).CocktailCallback(cocktail);
	}

	@Override
	public void onErrorResponse(VolleyError error) {

	}
}
