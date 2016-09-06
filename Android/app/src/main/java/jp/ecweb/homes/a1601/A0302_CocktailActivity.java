package jp.ecweb.homes.a1601;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Locale;

import jp.ecweb.homes.a1601.Adapter.RecipeListAdapter;
import jp.ecweb.homes.a1601.callback.CocktailCallbacks;
import jp.ecweb.homes.a1601.dao.FavoriteDAO;
import jp.ecweb.homes.a1601.listener.CocktailListener;
import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.model.Favorite;
import jp.ecweb.homes.a1601.model.Recipe;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;

public class A0302_CocktailActivity extends AppCompatActivity implements CocktailCallbacks {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private FavoriteDAO mFavoriteDAO;                       // SQLite お気に入りテーブル
	private Cocktail mCocktail;                             // カクテル情報

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

		// 広告を表示
		MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		// インテントからカクテルIDを取得
		Intent intent = getIntent();
		String selectedCocktailID = intent.getStringExtra("ID");

		// メンバ変数の初期化
		mFavoriteDAO = new FavoriteDAO(this);
		mCocktail = new Cocktail();
		mCocktail.setRecipes(new ArrayList<Recipe>());

		// サーバーからカクテル情報を取得
		String url = getString(R.string.server_URL) + "getCocktail.php" +
				"?id=" + selectedCocktailID;

		Log.d(LOG_TAG, LOG_CLASSNAME + "WEB API URL=" + url);

		CocktailListener cocktailListener = new CocktailListener(this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				url,
				null,
				cocktailListener,
				cocktailListener
		);

		// カクテル情報取得のリクエストを送信
		NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
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

/*--------------------------------------------------------------------------------------------------
	コールバック処理
--------------------------------------------------------------------------------------------------*/
	@Override
	public void CocktailCallback(Cocktail cocktail) {
		Log.d(LOG_TAG, LOG_CLASSNAME + "CocktailCallback start");

		// カクテル情報を設定
		mCocktail = cocktail;

		// カクテル名
		TextView cocktailNameView = (TextView) findViewById(R.id.A0302_CocktailNameView);
		cocktailNameView.setText(mCocktail.getName());

		// カクテル写真
		String photoUrl = mCocktail.getPhotoUrl();

		NetworkImageView cocktailImageView =
				(NetworkImageView) findViewById(R.id.A0302_CocktailImageView);
		ImageLoader imageLoader =
				NetworkSingleton.getInstance(getApplicationContext()).getImageLoader();
		cocktailImageView.setDefaultImageResId(R.drawable.nothumbnail);
		cocktailImageView.setErrorImageResId(R.drawable.noimage);

		Log.d(LOG_TAG, LOG_CLASSNAME + "PhotoUrl:" + mCocktail.getPhotoUrl());

		if (photoUrl.equals("")) {
			cocktailImageView.setImageUrl(null, imageLoader);
		} else {
			cocktailImageView.setImageUrl(photoUrl, imageLoader);
		}

		// 製法
		TextView methodsTextView = (TextView) findViewById(R.id.A0302_MethodsTextView);
		methodsTextView.setText(mCocktail.getMethod());

		// グラス
		TextView grassTextView = (TextView) findViewById(R.id.A0302_GrassTextView);
		grassTextView.setText(mCocktail.getGrass());

		// アルコール度数
		TextView alcoholDegreeTextView = (TextView) findViewById(R.id.A0302_AlcoholDegreeTextView);
		alcoholDegreeTextView.setText(String.format(Locale.US, "%.1f ％", mCocktail.getAlcoholDegree()));

		// 著作権表記
		TextView copyrightText = (TextView) findViewById(R.id.copyrightText);
		if (!mCocktail.getCopylight().equals("")) {
			String copylight = getResources().getText(R.string.Photo_text) + ":" + mCocktail.getCopylight();
			copyrightText.setText(copylight);
		}

		// 作り方
		TextView howToTextView = (TextView) findViewById(R.id.A0302_HowToTextView);
		howToTextView.setText(mCocktail.getHowTo().replaceAll("\\n", "\n"));

		// お気に入り
		ToggleButton favoriteButton = (ToggleButton) findViewById(R.id.favoriteButton);

		// 持っているボタンの初期値を所持製品DBから取得
		if (mFavoriteDAO.ExistCocktailId(mCocktail.getId())) {
			favoriteButton.setChecked(true);
		} else {
			favoriteButton.setChecked(false);
		}

		// お気に入りボタンにカクテルIDをタグ付け
		favoriteButton.setTag(R.string.TAG_CocktailID_Key, mCocktail.getId());

		// お気に入りボタンタップ時のリスナーを登録
		favoriteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;

				if (btn.isChecked()) {
					// ボタンがONになった場合はお気に入りテーブルにカクテルIDを追加
					Favorite favorite = new Favorite();
					favorite.setCocktailId((String) btn.getTag(R.string.TAG_CocktailID_Key));

					mFavoriteDAO.insertFavorite(favorite);
				} else {
					// ボタンがOFFになった場合はお気に入りテーブルからカクテルIDを削除
					Favorite favorite = new Favorite();
					favorite.setCocktailId((String) btn.getTag(R.string.TAG_CocktailID_Key));

					mFavoriteDAO.deleteFavorite(favorite);
				}
			}
		});

		// ListViewのアダプターを登録
		RecipeListAdapter adapter = new RecipeListAdapter(this,
				R.layout.activity_recipe_list_item,
				mCocktail.getRecipes());
		ListView recipeView = (ListView) findViewById(R.id.RecipeListView);
		recipeView.setAdapter(adapter);
	}

}
