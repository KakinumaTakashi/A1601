package jp.ecweb.homes.a1601.Cocktail;

import android.text.SpannableStringBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Takashi Kakinuma on 2016/07/14.
 *
 * カクテルクラス
 *
 */
public class Cocktail {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
    private String id;					// カクテルID
    private String name;				// カクテル名
    private String photoUrl;			// 写真
    private String thumbnailUrl;		// サムネイルURL
	private String Method;              // 製法
	private String Grass;               // グラス
	private float AlcoholDegree;        // アルコール度数
//    private String detail;		    	// 詳細
//    private String recipeId;			// レシピID
    private List<Recipe> Recipes;
	private SpannableStringBuilder RecipeStringBuffer;
	private String HowTo;               // 作り方

/*--------------------------------------------------------------------------------------------------
	メンバ関数
--------------------------------------------------------------------------------------------------*/
	// JSON形式のレスポンスからカクテルテーブルを生成
	public static List<Cocktail> getJSONtoCocktailList(List<Cocktail> cocktails, JSONArray jsonArray) {

		// テーブルをクリア
		cocktails.clear();

		for (int i = 0; i < jsonArray.length(); i++) {
			Cocktail cocktail = new Cocktail();

			try {
				JSONObject jsonCocktailObject = jsonArray.getJSONObject(i);

				// JSONデータから値を取り出してテーブルに設定
				// カクテル情報部
				cocktail.setId(jsonCocktailObject.getString("ID"));
				cocktail.setName(jsonCocktailObject.getString("Name"));
				cocktail.setThumbnailUrl(jsonCocktailObject.getString("ThumbnailURL"));

				// レシピ情報部
				cocktail.Recipes = new ArrayList<>();
				JSONArray jsonRecipeArray = jsonCocktailObject.getJSONArray("Recipes");

				for (int j = 0; j < jsonRecipeArray.length(); j++) {
					Recipe recipe = new Recipe();

					JSONObject jsonRecipeObject = jsonRecipeArray.getJSONObject(j);

					recipe.setId(jsonRecipeObject.getString("ID"));
					recipe.setCocktailID(jsonRecipeObject.getString("CocktailID"));
					recipe.setMatelialID(jsonRecipeObject.getString("MatelialID"));
					recipe.setCategory1(jsonRecipeObject.getString("Category1"));
					recipe.setCategory2(jsonRecipeObject.getString("Category2"));
					recipe.setCategory3(jsonRecipeObject.getString("Category3"));
					recipe.setMatelialName(jsonRecipeObject.getString("Name"));
					recipe.setQuantity(jsonRecipeObject.getInt("Quantity"));
					recipe.setUnit(jsonRecipeObject.getString("Unit"));

					cocktail.Recipes.add(recipe);
				}

				cocktails.add(cocktail);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return cocktails;
	}

/*--------------------------------------------------------------------------------------------------
	Getter / Setter
--------------------------------------------------------------------------------------------------*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
/*
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
*/
/*
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
*/
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public List<Recipe> getRecipes() {
		return Recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.Recipes = recipes;
	}

	public String getMethod() {
		return Method;
	}

	public void setMethod(String method) {
		Method = method;
	}

	public String getGrass() {
		return Grass;
	}

	public void setGrass(String grass) {
		Grass = grass;
	}

	public float getAlcoholDegree() {
		return AlcoholDegree;
	}

	public void setAlcoholDegree(float alcoholDegree) {
		AlcoholDegree = alcoholDegree;
	}

	public String getHowTo() {
		return HowTo;
	}

	public void setHowTo(String howTo) {
		HowTo = howTo;
	}

	public SpannableStringBuilder getRecipeStringBuffer() {
		return RecipeStringBuffer;
	}

	public void setRecipeStringBuffer(SpannableStringBuilder recipeStringBuffer) {
		RecipeStringBuffer = recipeStringBuffer;
	}
}
