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

import jp.ecweb.homes.a1601.callback.ProductCallbacks;
import jp.ecweb.homes.a1601.model.RakutenItem;
import jp.ecweb.homes.a1601.model.RakutenResponse;

/**
 * Created by KakinumaTakashi on 2016/09/06.
 */
public class ProductListener implements Response.Listener<JSONObject>, Response.ErrorListener {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	private Activity activity;

	public ProductListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onResponse(JSONObject response) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onResponse start");

		Log.d(LOG_TAG, LOG_CLASSNAME +
				"Response=" + response.toString()
		);

		RakutenResponse rakutenResponse = new RakutenResponse();

		try {

			// ヘッダ部処理
			rakutenResponse.setCount(response.getInt("count"));
			rakutenResponse.setPage(response.getInt("page"));
			rakutenResponse.setFirst(response.getInt("first"));
			rakutenResponse.setLast(response.getInt("last"));
			rakutenResponse.setHits(response.getInt("hits"));
			rakutenResponse.setCarrier(response.getInt("carrier"));
			rakutenResponse.setPageCount(response.getInt("pageCount"));

			// データ部処理
			JSONArray data = response.getJSONArray("Items");

			// JSONをListに展開
			List<RakutenItem> rakutenItems = new ArrayList<>();

			for (int i = 0; i < data.length(); i++) {
				JSONObject jsonObject = data.getJSONObject(i);

				RakutenItem rakutenItem = new RakutenItem();
				rakutenItem.setItemName(jsonObject.getString("itemName"));
				rakutenItem.setCatchcopy(jsonObject.getString("catchcopy"));
				rakutenItem.setItemCode(jsonObject.getString("itemCode"));
				rakutenItem.setItemPrice(jsonObject.getInt("itemPrice"));
				rakutenItem.setItemCaption(jsonObject.getString("itemCaption"));
				rakutenItem.setItemUrl(jsonObject.getString("itemUrl"));
				rakutenItem.setAffiliateUrl(jsonObject.getString("affiliateUrl"));
				rakutenItem.setShopAffiliateUrl(jsonObject.getString("shopAffiliateUrl"));
				rakutenItem.setImageFlag(jsonObject.getInt("imageFlag"));

				List<String> smallImageUrlList = new ArrayList<>();
				for (int j = 0; j < jsonObject.getJSONArray("smallImageUrls").length(); j++) {
					smallImageUrlList.add(jsonObject.getJSONArray("smallImageUrls").getString(j));
				}
				rakutenItem.setSmallImageUrls(smallImageUrlList);

				List<String> mediumImageUrlList = new ArrayList<>();
				for (int j = 0; j < jsonObject.getJSONArray("mediumImageUrls").length(); j++) {
					mediumImageUrlList.add(jsonObject.getJSONArray("mediumImageUrls").getString(j));
				}
				rakutenItem.setMediumImageUrls(mediumImageUrlList);

				rakutenItem.setAvailability(jsonObject.getInt("availability"));
				rakutenItem.setTaxFlag(jsonObject.getInt("taxFlag"));
				rakutenItem.setPostageFlag(jsonObject.getInt("postageFlag"));
				rakutenItem.setCreditCardFlag(jsonObject.getInt("creditCardFlag"));
				rakutenItem.setShopOfTheYearFlag(jsonObject.getInt("shopOfTheYearFlag"));
				rakutenItem.setShipOverseasFlag(jsonObject.getInt("shipOverseasFlag"));
				rakutenItem.setShipOverseasArea(jsonObject.getString("shipOverseasArea"));
				rakutenItem.setAsurakuFlag(jsonObject.getInt("asurakuFlag"));
				rakutenItem.setAsurakuClosingTime(jsonObject.getString("asurakuClosingTime"));
				rakutenItem.setAsurakuArea(jsonObject.getString("asurakuArea"));
				rakutenItem.setAffiliateRate(jsonObject.getInt("affiliateRate"));
				rakutenItem.setStartTime(jsonObject.getString("startTime"));
				rakutenItem.setEndTime(jsonObject.getString("endTime"));
				rakutenItem.setReviewCount(jsonObject.getInt("reviewCount"));
				rakutenItem.setReviewAverage(jsonObject.getDouble("reviewAverage"));
				rakutenItem.setPointRate(jsonObject.getInt("pointRate"));
				rakutenItem.setPointRateStartTime(jsonObject.getString("pointRateStartTime"));
				rakutenItem.setPointRateEndTime(jsonObject.getString("pointRateEndTime"));
				rakutenItem.setGiftFlag(jsonObject.getInt("giftFlag"));
				rakutenItem.setShopName(jsonObject.getString("shopName"));
				rakutenItem.setShopCode(jsonObject.getString("shopCode"));
				rakutenItem.setShopUrl(jsonObject.getString("shopUrl"));

				rakutenItems.add(rakutenItem);
			}

			rakutenResponse.setRakutenItems(rakutenItems);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Activityのコールバック呼び出し
		((ProductCallbacks) activity).ProductCallback(rakutenResponse);

	}

	@Override
	public void onErrorResponse(VolleyError error) {

	}
}
