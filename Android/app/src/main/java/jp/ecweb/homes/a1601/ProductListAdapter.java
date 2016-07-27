package jp.ecweb.homes.a1601;

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

import jp.ecweb.homes.a1601.Cocktail.Product;

/**
 * Created by Takashi Kakinuma on 2016/07/20.
 */
public class ProductListAdapter extends ArrayAdapter<Product> {

	// メンバ変数
	private LayoutInflater inflater;
	private List<Product> items;
	private int resourceId;

	public MySQLiteOpenHelper mSQLiteHelper;

	// コンストラクタ
	public ProductListAdapter(Context context, int resource, List<Product> objects) {
		super(context, resource, objects);

		this.inflater =
				(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.items = objects;
		this.resourceId = resource;

		mSQLiteHelper = new MySQLiteOpenHelper(context);
	}

	// アイテム描画
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = this.inflater.inflate(this.resourceId, null);
		} else {
			view = convertView;
		}

		Product item = this.items.get(position);

		// サムネイル
		ImageLoader imageLoader = NetworkSingleton.getInstance(parent.getContext()).getImageLoader();
		NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.ProductImageView);
		imageView.setImageUrl(item.getThumbnailURL(), imageLoader);

		// 製品名
		TextView productNameView = (TextView) view.findViewById(R.id.ProductNameView);
		productNameView.setText(item.getName());

		// 持っているボタン
		ToggleButton productHavingButton =
				(ToggleButton) view.findViewById(R.id.PuductHavingButton);

		// 持っているボタンの初期値を所持製品DBから取得
		SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();

		// 製品IDをキーにDBを検索
		String productID = item.getId();
		String sql =
				"SELECT ProductID,MaterialID FROM HavingProduct WHERE ProductID=" +
				"\"" + productID + "\"";
		Cursor cursor= database.rawQuery(sql, null);
		// 製品IDがDBに登録されていたらボタンの初期値をON
		if (cursor.moveToFirst()) {
			productHavingButton.setChecked(true);
		} else {
			productHavingButton.setChecked(false);
		}

		database.close();

		// 持っているボタンに製品ID・素材IDをタグ付け
		productHavingButton.setTag(R.string.TAG_ProductID_Key, productID);
		productHavingButton.setTag(R.string.TAG_MaterialID_Key, item.getMaterialID());

		// 持っているボタンタップ時のリスナーを登録
		productHavingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;

				SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();

				if (btn.isChecked()) {
					// ボタンがONになった場合 所持製品DBに製品ID・素材IDを登録
					ContentValues values = new ContentValues();
					values.put("ProductID", (String) btn.getTag(R.string.TAG_ProductID_Key));
					values.put("MaterialID", (String) btn.getTag(R.string.TAG_MaterialID_Key));

					database.insert("HavingProduct", null, values);
				} else {
					// ボタンがOFFになった場合 所持製品DBから製品ID・素材IDを削除
					database.delete("HavingProduct", "ProductID=?",
						new String[]{(String) btn.getTag(R.string.TAG_ProductID_Key)});
				}

				database.close();
			}
		});

		return view;
	}
}
