package jp.ecweb.homes.a1601.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import jp.ecweb.homes.a1601.dao.HavingProductDAO;
import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.model.HavingProduct;
import jp.ecweb.homes.a1601.model.Product;
import jp.ecweb.homes.a1601.helper.MySQLiteOpenHelper;
import jp.ecweb.homes.a1601.Network.NetworkSingleton;
import jp.ecweb.homes.a1601.R;

/**
 * 材料からカクテルを探すビュー用アダプタ
 *
 * Created by Takashi Kakinuma on 2016/07/20.
 */
public class ProductListAdapter extends ArrayAdapter<Product> {

	// メンバ変数
	private Context mContext;                       // 呼び出し元コンテキスト
	private LayoutInflater mInflater;               // セルレイアウト
	private int mResourceId;                        // セルに表示するリソースID

	private HavingProductDAO mHavingProductDAO;     // SQLite操作用

	public List<Product> mProductList;              // 製品一覧

/*--------------------------------------------------------------------------------------------------
	インナークラス
--------------------------------------------------------------------------------------------------*/
	// セルのビュー保存用ビューホルダー
	private class ViewHolder {
		NetworkImageView thumbnailImageView;
		TextView categoryTextView;
		TextView productNameView;
		TextView makerTextView;
		ToggleButton productHavingButton;
	}

/*--------------------------------------------------------------------------------------------------
	コンストラクタ
--------------------------------------------------------------------------------------------------*/
	public ProductListAdapter(Context context, int resource, List<Product> productList) {
		super(context, resource, productList);

		this.mContext = context;
		this.mInflater =
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mResourceId = resource;
		this.mProductList = productList;

		this.mHavingProductDAO = new HavingProductDAO(mContext);
	}

/*--------------------------------------------------------------------------------------------------
	メソッド
--------------------------------------------------------------------------------------------------*/
	// セル描画
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			// セルにリソースを展開
			convertView = mInflater.inflate(mResourceId, null);

			// ビューホルダーに各ビューを保存
			holder = new ViewHolder();
			holder.thumbnailImageView = (NetworkImageView) convertView.findViewById(R.id.ProductImageView);
			holder.categoryTextView = (TextView) convertView.findViewById(R.id.CategoryTextView);
			holder.productNameView = (TextView) convertView.findViewById(R.id.ProductNameView);
			holder.makerTextView = (TextView) convertView.findViewById(R.id.MakerTextView);
			holder.productHavingButton = (ToggleButton) convertView.findViewById(R.id.PuductHavingButton);

			// タグにビューホルダーのインスタンスを保存
			convertView.setTag(holder);

		} else {
			// タグからビューホルダーのインスタンスを取得
			holder = (ViewHolder) convertView.getTag();
		}

		// 各ビューにカクテル情報を設定
		Product item = mProductList.get(position);

		// サムネイル
		ImageLoader imageLoader = NetworkSingleton.getInstance(parent.getContext()).getImageLoader();
		holder.thumbnailImageView.setDefaultImageResId(R.drawable.nothumbnail);
		holder.thumbnailImageView.setErrorImageResId(R.drawable.nothumbnail);

		if (item.getThumbnailURL().equals("")) {
			holder.thumbnailImageView.setImageUrl(null, imageLoader);
		} else {
			holder.thumbnailImageView.setImageUrl(item.getThumbnailURL(), imageLoader);
		}

		// メーカー
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(item.getCategory1());
		if (!item.getCategory2().equals("")) {
			stringBuilder.append("/");
			stringBuilder.append(item.getCategory2());
		}
		if (!item.getCategory3().equals("")) {
			stringBuilder.append("/");
			stringBuilder.append(item.getCategory3());
		}
		holder.categoryTextView.setText(stringBuilder.toString());

		// 製品名
		holder.productNameView.setText(item.getName());

		// メーカー
		if (item.getMaker().equals("")) {
			holder.makerTextView.setVisibility(View.GONE);
		} else {
			holder.makerTextView.setText(item.getMaker());
		}

		// 持っているボタンの初期値を所持製品DBから取得
		// 製品IDをキーにDBを検索
		final HavingProduct product = new HavingProduct();
		product.setProductID(item.getId());
		product.setMaterialID(item.getMaterialID());

		// 製品IDがDBに登録されていたらボタンの初期値をON
		if (mHavingProductDAO.ExistProductID(product.getProductID())) {
			holder.productHavingButton.setChecked(true);
		} else {
			holder.productHavingButton.setChecked(false);
		}

		// 持っているボタンに製品ID・材料IDをタグ付け
		holder.productHavingButton.setTag(R.string.TAG_ProductID_Key, product.getProductID());
		holder.productHavingButton.setTag(R.string.TAG_MaterialID_Key, product.getMaterialID());

		// 持っているボタンタップ時のリスナーを登録
		holder.productHavingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;

				if (btn.isChecked()) {
					// ボタンがONになった場合 所持製品テーブルに製品ID・材料IDを登録
					mHavingProductDAO.insertProduct(product);
				} else {
					// ボタンがOFFになった場合 所持製品テーブルから製品ID・材料IDを削除
					mHavingProductDAO.deleteProduct(product);
				}
			}
		});

		return convertView;
	}
}
