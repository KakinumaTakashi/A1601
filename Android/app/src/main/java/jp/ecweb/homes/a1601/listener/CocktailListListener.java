package jp.ecweb.homes.a1601.listener;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.callback.CocktailListCallbacks;
import jp.ecweb.homes.a1601.dao.HavingProductDAO;
import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.model.Recipe;

/**
 * Created by KakinumaTakashi on 2016/09/04.
 */
public class CocktailListListener implements Response.Listener<JSONObject>, Response.ErrorListener {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	private Activity activity;


	public CocktailListListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onResponse(JSONObject response) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onResponse start");

		List<Cocktail> cocktailList = new ArrayList<>();

		Log.d(LOG_TAG, LOG_CLASSNAME +
				"Response=" +
				response.toString()
		);

		try {
			// ヘッダ部処理
			String status = response.getString("status");

			if (status.equals("NG")) {
				// サーバーにてエラーが発生した場合はエラーをスロー
				throw new JSONException(response.getString("message"));
			}

			// データ部処理
			JSONArray data = response.getJSONArray("data");

			for (int i = 0; i < data.length(); i++) {
				Cocktail cocktail = new Cocktail();

				try {
					JSONObject jsonCocktailObject = data.getJSONObject(i);

					// JSONデータから値を取り出してテーブルに設定
					// カクテル情報部
					cocktail.setId(jsonCocktailObject.getString("ID"));
					cocktail.setName(jsonCocktailObject.getString("NAME"));
					cocktail.setThumbnailUrl(jsonCocktailObject.getString("THUMBNAILURL"));

					// レシピ情報部
					List<Recipe> recipeList = new ArrayList<>();
					SpannableStringBuilder recipeString = new SpannableStringBuilder();

					JSONArray jsonRecipeArray = jsonCocktailObject.getJSONArray("RECIPES");

					if (jsonRecipeArray.length() > 0) {
						HavingProductDAO havingProductDAO = new HavingProductDAO(activity);

						for (int j = 0; j < jsonRecipeArray.length(); j++) {
							Recipe recipe = new Recipe();
							TextAppearanceSpan textAppearanceSpan =	new TextAppearanceSpan(
									activity.getApplicationContext(), R.style.ListViewHaveItem);

							JSONObject jsonRecipeObject = jsonRecipeArray.getJSONObject(j);

							recipe.setId(jsonRecipeObject.getString("ID"));
							recipe.setCocktailID(jsonRecipeObject.getString("COCKTAILID"));
							recipe.setMatelialID(jsonRecipeObject.getString("MATERIALID"));
							recipe.setCategory1(jsonRecipeObject.getString("CATEGORY1"));
							recipe.setCategory2(jsonRecipeObject.getString("CATEGORY2"));
							recipe.setCategory3(jsonRecipeObject.getString("CATEGORY3"));
							recipe.setMatelialName(jsonRecipeObject.getString("NAME"));
							recipe.setQuantity(jsonRecipeObject.getInt("QUANTITY"));
							recipe.setUnit(jsonRecipeObject.getString("UNIT"));

							recipeList.add(recipe);

							// ListView表示用レシピ連結
							if (j > 0) {
								recipeString.append("／");
							}
							if (havingProductDAO.ExistMaterialID(jsonRecipeObject.getString("MATERIALID"))) {
								// 所持商品と一致した場合は文字色を設定
								int startPos = recipeString.length();
								recipeString.append(jsonRecipeObject.getString("NAME"));
								recipeString.setSpan(
										textAppearanceSpan,
										startPos,
										recipeString.length(),
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
								);
							} else {
								recipeString.append(jsonRecipeObject.getString("NAME"));
							}
						}
						havingProductDAO.close();

					} else {
						recipeString.append("レシピ準備中");
					}

					cocktail.setRecipes(recipeList);
					cocktail.setRecipeStringBuffer(recipeString);

					cocktailList.add(cocktail);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			cocktailList = null;
		}

		// Activityのコールバック呼び出し
		((CocktailListCallbacks)activity).ListResponseCallback(cocktailList);
	}

	@Override
	public void onErrorResponse(VolleyError error) {

	}

}
