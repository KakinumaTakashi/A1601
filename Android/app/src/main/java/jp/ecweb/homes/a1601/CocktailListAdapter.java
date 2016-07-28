package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ecweb.homes.a1601.Cocktail.Cocktail;

/**
 * Created by Takashi Kakinuma on 2016/07/14.
 */
public class CocktailListAdapter extends ArrayAdapter<Cocktail> {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private LayoutInflater mInflater;
	private List<Cocktail> mCocktailList;
	private Map<String, String> mRecipeList = new HashMap<String, String>();
	private int mResourceId;

	// コンストラクタ
	public CocktailListAdapter(final Context context, int resource, List<Cocktail> objects) {
		super(context, resource, objects);

		this.mInflater =
				(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.mResourceId = resource;
		this.mCocktailList = objects;

		for (final Cocktail cocktail : mCocktailList) {
			// サーバーからレシピリストを取得
			String url = context.getString(R.string.server_URL) + "getRecipe.php" +
					"?id=" + cocktail.getId();

			Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

			JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
					Request.Method.GET,
					url,
					null,
					new Response.Listener<JSONArray>() {
						@Override
						public void onResponse(JSONArray response) {
							String recipeString = "";

							try {
								// JSONをListに展開
								for (int i = 0; i < response.length(); i++) {
									JSONObject jsonObject = response.getJSONObject(i);

									Log.d(LOG_TAG, LOG_CLASSNAME +
											"Response[" + String.valueOf(i) + "]=" +
											jsonObject.toString()
									);

									if (recipeString.equals("") == true) {
										recipeString = jsonObject.getString("Name");
									} else {
										recipeString = recipeString + "、" +
												jsonObject.getString("Name");
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

							mRecipeList.put(cocktail.getId(), recipeString);
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// エラーメッセージを表示
							AlertDialog.Builder alert = new AlertDialog.Builder(context);
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
			NetworkSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
		}

		Log.d(LOG_TAG, "MAP=" + mRecipeList.toString());
	}

	// リストの更新とListViewの再描画
	public void UpdateItemList(List<Cocktail> objects) {
		this.mCocktailList = objects;
		notifyDataSetChanged();
	}

	// アイテム描画
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = this.mInflater.inflate(this.mResourceId, null);
		} else {
			view = convertView;
		}

		Cocktail item = this.mCocktailList.get(position);

		// カクテル名
		TextView cocktailNameView = (TextView) view.findViewById(R.id.cocktailNameView);
		cocktailNameView.setText(item.getName());

		// サムネイル
		ImageLoader imageLoader = NetworkSingleton.getInstance(parent.getContext()).getImageLoader();
		NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.cocktailImageView);
		imageView.setImageUrl(item.getThumbnailURL(), imageLoader);

		// レシピ
		TextView recipeView = (TextView) view.findViewById(R.id.recipeView);
		recipeView.setText(mRecipeList.get(item.getId()));

		return view;
	}
}
