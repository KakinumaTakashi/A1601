package jp.ecweb.homes.a1601;

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

import jp.ecweb.homes.a1601.Cocktail.Product;

/**
 * Created by Takashi Kakinuma on 2016/07/20.
 */
public class ProductListAdapter extends ArrayAdapter<Product> {

	private LayoutInflater inflater;
	private List<Product> items;
	private int resourceId;

	public MySQLiteOpenHelper mySQLHelper;
	public SQLiteDatabase database;

	public ProductListAdapter(Context context, int resource, List<Product> objects) {
		super(context, resource, objects);

		this.inflater =
				(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.items = objects;
		this.resourceId = resource;

	}

	public void UpdateItemList(List<Product> objects) {
		this.items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		Log.d("ProductListAdapter",
				"Start getView. position is " + String.valueOf(position));
		if (convertView == null) {
			view = this.inflater.inflate(this.resourceId, null);
		} else {
			view = convertView;
		}

		Product item = this.items.get(position);

		ImageLoader imageLoader = NetworkSingleton.getInstance(this.getContext()).getImageLoader();
		NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.ProductImageView);
		imageView.setImageUrl(item.getThumbnailURL(), imageLoader);

		TextView productNameView = (TextView) view.findViewById(R.id.ProductNameView);
		productNameView.setText(item.getName());

		ToggleButton productHavingButton =
				(ToggleButton) view.findViewById(R.id.PuductHavingButton);

		mySQLHelper = new MySQLiteOpenHelper(this.getContext());
		database = mySQLHelper.getWritableDatabase();

		String productID = item.getId();
		String sql =
				"SELECT ProductID,MaterialID FROM HavingProduct WHERE ProductID=" +
				"\"" + productID + "\"";
		Log.d("ProductListAdapter", sql);
		Cursor cursor= database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			Log.d("ProductListAdapter", cursor.getString(cursor.getColumnIndex("ProductID")));
			Log.d("ProductListAdapter", "Check Set" + productID);
			productHavingButton.setChecked(true);
		} else {
			Log.d("ProductListAdapter", "Check Unset" + productID);
			productHavingButton.setChecked(false);
		}

		productHavingButton.setTag(R.string.TAG_ProductID_Key, productID);
		productHavingButton.setTag(R.string.TAG_MaterialID_Key, item.getMaterialID());

		productHavingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;
				if (btn.isChecked()) {
					Log.d("ProductListAdapter",
							"ProfuctID = " +
							btn.getTag(R.string.TAG_ProductID_Key) + " OnClick! -> ON");

					ContentValues values = new ContentValues();
					values.put("ProductID", (String) btn.getTag(R.string.TAG_ProductID_Key));
					values.put("MaterialID", (String) btn.getTag(R.string.TAG_MaterialID_Key));
					database.insert("HavingProduct", null, values);
				} else {
					Log.d("ProductListAdapter",
					"ProfuctID = " +
							btn.getTag(R.string.TAG_ProductID_Key) + " OnClick! -> OFF");

					database.delete("HavingProduct", "ProductID=?",
						new String[]{(String) btn.getTag(R.string.TAG_ProductID_Key)});
				}
			}
		});

		return view;
	}
}
