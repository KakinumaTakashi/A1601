package jp.ecweb.homes.a1601.Network;

import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Cocktail.Cocktail;
import jp.ecweb.homes.a1601.Cocktail.Recipe;
import jp.ecweb.homes.a1601.R;

/**
 * Created by KakinumaTakashi on 2016/08/05.
 *
 * サーバー通信関連
 *
 */
public class ServerCommunication {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

/*--------------------------------------------------------------------------------------------------
	メンバ変数
--------------------------------------------------------------------------------------------------*/


/*--------------------------------------------------------------------------------------------------
	メソッド
--------------------------------------------------------------------------------------------------*/
	// カクテル一覧のリスト取得・表示
	public void getCocktailList(final Context context,
	                            final ArrayAdapter<Cocktail> adapter,
	                            final List<Cocktail> cocktails,
	                            final String category1,
	                            final String category2) {

		// サーバーのカクテルリスト取得URL
		String url = context.getString(R.string.server_URL) + "getCocktailList.php";

		List<String> dummyList = new ArrayList<>();

		// POST用のデータ作成
		JSONObject postData = new JSONObject();
		try {
			postData.put("Category1", category1);
			postData.put("Category2", category2);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		getCocktailListFromServer(context, url, postData, adapter, cocktails, dummyList);
	}

	// 該当カクテル一覧のリスト取得・表示
	public void getProductToCocktailList(final Context context,
	                                     final ArrayAdapter<Cocktail> adapter,
	                                     final List<Cocktail> cocktails,
	                                     final List<String> haveMaterials) {

		// 所持リストを連結する
		StringBuilder stringBuilder = new StringBuilder();

		for (String item:haveMaterials) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append(item);
		}

		// サーバーのカクテルリスト取得URL
		String url = context.getString(R.string.server_URL) + "getProductToCocktailList.php";

		// POST用のデータ作成
		JSONObject postData = new JSONObject();
		try {
			postData.put("id", stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		getCocktailListFromServer(context, url, postData, adapter, cocktails, haveMaterials);
	}

	// カクテル一覧のリスト取得・表示
	public void getFavoriteCocktailList(final Context context,
	                            final ArrayAdapter<Cocktail> adapter,
	                            final List<Cocktail> cocktails,
	                            final List<String> CocktailID) {

		// サーバーのカクテルリスト取得URL
		String url = context.getString(R.string.server_URL) + "getFavoriteCocktailList.php";

		List<String> dummyList = new ArrayList<>();

		// POST用のデータ作成
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < CocktailID.size(); i++) {
			if (i > 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append(CocktailID.get(i));
		}

		JSONObject postData = new JSONObject();
		try {
			postData.put("id", stringBuilder);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		getCocktailListFromServer(context, url, postData, adapter, cocktails, dummyList);
	}



	// サーバーからカクテル一覧を取得しListViewに反映
	private void getCocktailListFromServer(final Context context,
	                                       final String url,
	                                       final JSONObject postData,
	                                       final ArrayAdapter<Cocktail> adapter,
	                                       final List<Cocktail> cocktails,
	                                       final List<String> haveMaterials) {

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);
		Log.d(LOG_TAG, "PostRequest=" + postData.toString());

		// カクテルリストの受信処理(JSONをListViewに表示)
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				postData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

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
							cocktails.clear();
							JSONArray data = response.getJSONArray("data");

							for (int i = 0; i < data.length(); i++) {
								Cocktail cocktail = new Cocktail();

								try {
									JSONObject jsonCocktailObject = data.getJSONObject(i);

									// JSONデータから値を取り出してテーブルに設定
									// カクテル情報部
									cocktail.setId(jsonCocktailObject.getString("ID"));
									cocktail.setName(jsonCocktailObject.getString("Name"));
									cocktail.setThumbnailUrl(jsonCocktailObject.getString("ThumbnailURL"));

									// レシピ情報部
									List<Recipe> recipeList = new ArrayList<>();
									SpannableStringBuilder recipeString = new SpannableStringBuilder();

									JSONArray jsonRecipeArray = jsonCocktailObject.getJSONArray("Recipes");

									if (jsonRecipeArray.length() > 0) {
										for (int j = 0; j < jsonRecipeArray.length(); j++) {
											Recipe recipe = new Recipe();
											TextAppearanceSpan textAppearanceSpan =
													new TextAppearanceSpan(context, R.style.ListViewHaveItem);

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

											recipeList.add(recipe);

											// ListView表示用レシピ連結
											if (j > 0) {
												recipeString.append("／");
											}
											if (haveMaterials.contains(jsonRecipeObject.getString("MatelialID"))) {
												// 所持商品と一致した場合は文字色を設定
												int startPos = recipeString.length();
												recipeString.append(jsonRecipeObject.getString("Name"));
												recipeString.setSpan(
														textAppearanceSpan,
														startPos,
														recipeString.length(),
														Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
												);
											} else {
												recipeString.append(jsonRecipeObject.getString("Name"));
											}
										}

									} else {
										recipeString.append("レシピ準備中");
									}

									cocktail.setRecipes(recipeList);
									cocktail.setRecipeStringBuffer(recipeString);

									cocktails.add(cocktail);

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							// ListViewを更新
							adapter.notifyDataSetChanged();

						} catch (JSONException e) {
							// エラーメッセージを表示
							AlertDialog.Builder alert = new AlertDialog.Builder(context);
							alert.setTitle(R.string.ERR_VolleyTitle_text);
							alert.setMessage(R.string.ERR_VolleyMessage_text);
							alert.setPositiveButton("OK", null);
							alert.show();

							Log.e(LOG_TAG, LOG_CLASSNAME +
									"カクテル情報の解析に失敗しました。(" +
									e.toString() + ")"
							);

							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					// Volley通信エラー処理
					@Override
					public void onErrorResponse(VolleyError error) {
						// エラーメッセージを表示
						AlertDialog.Builder alert = new AlertDialog.Builder(context);
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
		NetworkSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
	}

}
