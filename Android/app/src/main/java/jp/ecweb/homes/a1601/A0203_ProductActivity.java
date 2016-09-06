package jp.ecweb.homes.a1601;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.callback.ProductCallbacks;
import jp.ecweb.homes.a1601.dao.HavingProductDAO;
import jp.ecweb.homes.a1601.helper.MySQLiteOpenHelper;
import jp.ecweb.homes.a1601.listener.ProductListener;
import jp.ecweb.homes.a1601.model.HavingProduct;
import jp.ecweb.homes.a1601.model.Product;
import jp.ecweb.homes.a1601.model.RakutenItem;
import jp.ecweb.homes.a1601.model.RakutenResponse;

public class A0203_ProductActivity extends AppCompatActivity implements ProductCallbacks {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private String mItemCode;
	private String mProductId;
	private String mMaterialId;

	private HavingProductDAO mHavingProductDAO;
	private RakutenResponse mRakutenResponse;

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a0203__product);

		Log.d(LOG_TAG, LOG_CLASSNAME + "onCreate start");

		// 画面を縦方向に固定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_a0203__product);

		// 広告を表示
		MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		// インテントから楽天商品コードを取得
		Intent intent = getIntent();
		mItemCode = intent.getStringExtra("itemCode");
		mProductId = intent.getStringExtra("productId");
		mMaterialId = intent.getStringExtra("materialId");

		// メンバ変数の初期化
		mHavingProductDAO = new HavingProductDAO(this);

		// 楽天商品検索APIから商品情報を取得
		String url = "https://app.rakuten.co.jp/services/api/IchibaItem/Search/20140222" +
				"?applicationId=1032687951669207220" +
				"&affiliateId=152b14d0.1ddf0510.152b14d1.b83ab2b3" +
				"&formatVersion=2" +
				"&itemCode=" + mItemCode;

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		ProductListener productListener = new ProductListener(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				url,
				null,
				productListener,
				productListener
		);

		// 商品情報取得のリクエストを送信
		NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

	}

/*--------------------------------------------------------------------------------------------------
	メニューイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// リソースの登録
		getMenuInflater().inflate(R.menu.menu_a0203__product, menu);

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

/*--------------------------------------------------------------------------------------------------
	ボタンタップイベント処理
--------------------------------------------------------------------------------------------------*/
	// 購入
	public void onRakutenShopButtonTapped(View view) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "onRakutenShopButtonTapped start");

		// ブラウザでURLを開く
		Uri uri = Uri.parse(mRakutenResponse.getRakutenItems().get(0).getAffiliateUrl());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}


/*--------------------------------------------------------------------------------------------------
	コールバック処理
--------------------------------------------------------------------------------------------------*/
	@Override
	public void ProductCallback(RakutenResponse rakutenResponse) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "CocktailCallback start");

		// 商品情報を設定
		mRakutenResponse = rakutenResponse;

		// 商品名
		TextView itemNameView = (TextView) findViewById(R.id.itemNameView);
		itemNameView.setText(mRakutenResponse.getRakutenItems().get(0).getItemName());

		// 商品画像
		NetworkImageView imageView =
				(NetworkImageView) findViewById(R.id.ProductImageView);
		ImageLoader imageLoader =
				NetworkSingleton.getInstance(getApplicationContext()).getImageLoader();
		imageView.setImageUrl(
				mRakutenResponse.getRakutenItems().get(0).getMediumImageUrls().get(0),
				imageLoader);
		imageView.setErrorImageResId(R.drawable.nothumbnail);

		// 商品説明文
		TextView itemCaptionView = (TextView) findViewById(R.id.itemCaptionView);
		itemCaptionView.setText(mRakutenResponse.getRakutenItems().get(0).getItemCaption());

		// 店舗名
		TextView shopNameView = (TextView) findViewById(R.id.shopNameView);
		shopNameView.setText(mRakutenResponse.getRakutenItems().get(0).getShopName());

		// 商品コード
		TextView itemCodeView = (TextView) findViewById(R.id.itemCodeView);
		itemCodeView.setText(mRakutenResponse.getRakutenItems().get(0).getItemCode());

		// 持っているボタン
		ToggleButton productHavingButton =
				(ToggleButton) findViewById(R.id.PuductHavingButton);

		// 商品IDがテーブルに登録されていたらボタンの初期値をON
		if (mHavingProductDAO.ExistProductID(mProductId)) {
			productHavingButton.setChecked(true);
		} else {
			productHavingButton.setChecked(false);
		}

		// 持っているボタンに商品ID・材料IDをタグ付け
		productHavingButton.setTag(R.string.TAG_ProductID_Key, mProductId);
		productHavingButton.setTag(R.string.TAG_MaterialID_Key, mMaterialId);

		// 持っているボタンタップ時のリスナーを登録
		productHavingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;

				HavingProduct product = new HavingProduct();
				product.setProductID((String) btn.getTag(R.string.TAG_ProductID_Key));
				product.setMaterialID((String) btn.getTag(R.string.TAG_MaterialID_Key));

				if (btn.isChecked()) {
					// ボタンがONになった場合 所持商品テーブルに商品ID・材料IDを登録
					mHavingProductDAO.insertProduct(product);

				} else {
					// ボタンがOFFになった場合 所持商品テーブルから商品ID・材料IDを削除
					mHavingProductDAO.deleteProduct(product);
				}
			}
		});
	}


}
