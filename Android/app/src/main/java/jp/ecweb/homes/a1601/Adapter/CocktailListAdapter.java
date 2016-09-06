package jp.ecweb.homes.a1601.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jp.ecweb.homes.a1601.dao.FavoriteDAO;
import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.model.Favorite;

/**
 * カクテル一覧用アダプタ
 *
 * Created by Takashi Kakinuma on 2016/07/14.
 */

public class CocktailListAdapter extends ArrayAdapter<Cocktail> {

	// メンバ変数
	private Context mContext;                       // 呼び出し元コンテキスト
	private LayoutInflater mInflater;               // セルレイアウト
	private int mResourceId;                        // セルに表示するリソースID

	private FavoriteDAO mFavoriteDAO;               // SQLite操作用

	public List<Cocktail> mCocktailList;            // カクテル一覧

/*--------------------------------------------------------------------------------------------------
	インナークラス
--------------------------------------------------------------------------------------------------*/
	// セルのビュー保存用ビューホルダー
	private class ViewHolder {
		TextView cocktailNameView;
		NetworkImageView thumbnailImageView;
		TextView recipeView;
		ToggleButton favoriteButton;
	}

/*--------------------------------------------------------------------------------------------------
	コンストラクタ
--------------------------------------------------------------------------------------------------*/
	public CocktailListAdapter(Context context, int resource, List<Cocktail> cocktailList) {
		super(context, resource, cocktailList);

		this.mContext = context;
		this.mInflater =
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mResourceId = resource;
		this.mCocktailList = cocktailList;

		this.mFavoriteDAO = new FavoriteDAO(mContext);

	}

/*--------------------------------------------------------------------------------------------------
	メソッド
--------------------------------------------------------------------------------------------------*/
	// セル描画
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			// セルにリソースを展開
			convertView = mInflater.inflate(mResourceId, null);

			// ビューホルダーに各ビューを保存
			holder = new ViewHolder();
			holder.cocktailNameView = (TextView) convertView.findViewById(R.id.cocktailNameView);
			holder.thumbnailImageView = (NetworkImageView) convertView.findViewById(R.id.cocktailImageView);
			holder.recipeView = (TextView) convertView.findViewById(R.id.recipeView);
			holder.favoriteButton = (ToggleButton) convertView.findViewById(R.id.favoriteButton);

			// タグにビューホルダーのインスタンスを保存
			convertView.setTag(holder);

		} else {
			// タグからビューホルダーのインスタンスを取得
			holder = (ViewHolder) convertView.getTag();
		}

		// 各ビューにカクテル情報を設定
		Cocktail item = mCocktailList.get(position);

		// カクテル名
		holder.cocktailNameView.setText(item.getName());

		// サムネイル
		String thumbnailUrl = item.getThumbnailUrl();
		ImageLoader imageLoader = NetworkSingleton.getInstance(parent.getContext()).getImageLoader();
		holder.thumbnailImageView.setDefaultImageResId(R.drawable.nothumbnail);
		holder.thumbnailImageView.setErrorImageResId(R.drawable.nothumbnail);

		if (thumbnailUrl.equals("")) {
			holder.thumbnailImageView.setImageUrl(null, imageLoader);
		} else {
			holder.thumbnailImageView.setImageUrl(thumbnailUrl, imageLoader);
		}

		// レシピ
		holder.recipeView.setText(item.getRecipeStringBuffer());

		// お気に入り
		// お気に入りテーブルを検索しトグルのチェックを設定
		String cocktailId = item.getId();

		if (mFavoriteDAO.ExistCocktailId(cocktailId)) {
			holder.favoriteButton.setChecked(true);
		} else {
			holder.favoriteButton.setChecked(false);
		}

		// お気に入りボタンにカクテルIDをタグ付け
		holder.favoriteButton.setTag(R.string.TAG_CocktailID_Key, cocktailId);

		// お気に入りボタンタップ時のリスナーを登録
		holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
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

		return convertView;
	}

}
