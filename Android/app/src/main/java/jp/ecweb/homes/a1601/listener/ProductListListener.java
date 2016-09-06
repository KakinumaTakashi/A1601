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

import jp.ecweb.homes.a1601.callback.ProductListCallbacks;
import jp.ecweb.homes.a1601.model.Product;

/**
 * Created by KakinumaTakashi on 2016/09/06.
 */
public class ProductListListener implements Response.Listener<JSONObject>, Response.ErrorListener {
	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	private Activity activity;

	public ProductListListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onResponse(JSONObject response) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onResponse start");

		List<Product> productList = new ArrayList<>();

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

			// JSONをListに展開
			for (int i = 0; i < data.length(); i++) {
				JSONObject jsonObject = data.getJSONObject(i);

				Product product = new Product();
				product.setId(jsonObject.getString("ID"));
				product.setMaterialID(jsonObject.getString("MATERIALID"));
				product.setCategory1(jsonObject.getString("CATEGORY1"));
				product.setCategory2(jsonObject.getString("CATEGORY2"));
				product.setCategory3(jsonObject.getString("CATEGORY3"));
				product.setMaterialName(jsonObject.getString("MATERIALNAME"));
				product.setName(jsonObject.getString("NAME"));
				product.setMaker(jsonObject.getString("MAKER"));
				product.setBrand(jsonObject.getString("BRAND"));
				product.setAlcoholDegree((float) jsonObject.getDouble("ALCOHOLDEGREE"));
				product.setThumbnailURL(jsonObject.getString("THUMBNAILURL"));
				product.setItemCode(jsonObject.getString("ITEMCODE"));

				// リストを更新
				productList.add(product);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Activityのコールバック呼び出し
		((ProductListCallbacks) activity).ListResponseCallback(productList);
	}

	@Override
	public void onErrorResponse(VolleyError error) {

	}
}
