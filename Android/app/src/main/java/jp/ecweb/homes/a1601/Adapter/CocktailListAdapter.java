package jp.ecweb.homes.a1601.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jp.ecweb.homes.a1601.Cocktail.Cocktail;
import jp.ecweb.homes.a1601.Database.MySQLiteOpenHelper;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.R;

/**
 * Created by Takashi Kakinuma on 2016/07/14.
 *
 * カクテル一覧用アダプタ
 *
 */

public class CocktailListAdapter extends ArrayAdapter<Cocktail> {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

	// メンバ変数
	private LayoutInflater mInflater;
	private List<Cocktail> mCocktailList;
	private int mResourceId;

	public MySQLiteOpenHelper mSQLiteHelper;

	// コンストラクタ
	public CocktailListAdapter(Context context, int resource, List<Cocktail> cocktailList) {
		super(context, resource, cocktailList);

		this.mInflater =
				(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.mResourceId = resource;
		this.mCocktailList = cocktailList;

		mSQLiteHelper = new MySQLiteOpenHelper(context);
	}

	// リストの更新とListViewの再描画
	public void UpdateItemList(List<Cocktail> cocktailList) {
		this.mCocktailList = cocktailList;
		notifyDataSetChanged();
	}

	// アイテム描画
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(mResourceId, null);
		} else {
			view = convertView;
		}

		Cocktail item = mCocktailList.get(position);

		// カクテル名
		TextView cocktailNameView = (TextView) view.findViewById(R.id.cocktailNameView);
		cocktailNameView.setText(item.getName());

		// サムネイル
		String thumbnailUrl = item.getThumbnailUrl();

		if (thumbnailUrl.equals("")) {
			// 画像未登録の場合はNoImageアイコンを表示
			thumbnailUrl = getContext().getString(R.string.NoThumbnail_URL);
		}
		// URLを絶対パスに整形
		thumbnailUrl = getContext().getString(R.string.server_URL) +
				getContext().getString(R.string.photo_URL) +
				thumbnailUrl;

		ImageLoader imageLoader = NetworkSingleton.getInstance(parent.getContext()).getImageLoader();
		NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.cocktailImageView);
		imageView.setImageUrl(thumbnailUrl, imageLoader);

		// レシピ
		TextView recipeView = (TextView) view.findViewById(R.id.recipeView);
		recipeView.setText(item.getRecipeStringBuffer());

		// お気に入り
		ToggleButton favoriteButton = (ToggleButton) view.findViewById(R.id.favoriteButton);

		// 持っているボタンの初期値を所持製品DBから取得
		SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();

		// カクテルIDをキーにDBを検索
		String cocktailID = item.getId();
		String sql =
				"SELECT CocktailID FROM favorite WHERE CocktailID=" +
						"\"" + cocktailID + "\"";
		Cursor cursor= database.rawQuery(sql, null);
		// カクテルIDがDBに登録されていたらボタンの初期値をON
		if (cursor.moveToFirst()) {
			favoriteButton.setChecked(true);
		} else {
			favoriteButton.setChecked(false);
		}

		cursor.close();
		database.close();

		// お気に入りボタンにカクテルIDをタグ付け
		favoriteButton.setTag(R.string.TAG_CocktailID_Key, cocktailID);

		// お気に入りボタンタップ時のリスナーを登録
		favoriteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;

				SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();

				if (btn.isChecked()) {
					// ボタンがONになった場合 所持製品DBに製品ID・素材IDを登録
					ContentValues values = new ContentValues();
					values.put("CocktailID", (String) btn.getTag(R.string.TAG_CocktailID_Key));

					database.insert("favorite", null, values);
				} else {
					// ボタンがOFFになった場合 所持製品DBから製品ID・素材IDを削除
					database.delete("favorite", "CocktailID=?",
							new String[]{(String) btn.getTag(R.string.TAG_CocktailID_Key)});
				}

				database.close();
			}
		});

		return view;
	}

}
