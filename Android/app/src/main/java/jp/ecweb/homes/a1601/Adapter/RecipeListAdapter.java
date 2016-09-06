package jp.ecweb.homes.a1601.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.ecweb.homes.a1601.model.Recipe;
import jp.ecweb.homes.a1601.R;

/**
 * Created by Takashi Kakinuma on 2016/07/19.
 *
 * レシピ一覧用アダプタ
 *
 */
public class RecipeListAdapter extends ArrayAdapter<Recipe> {

	// メンバ変数
	private LayoutInflater inflater;
	private List<Recipe> items;
	private int resourceId;

	// コンストラクタ
	public RecipeListAdapter(Context context, int resource, List<Recipe> objects) {
		super(context, resource, objects);

		this.inflater =
				(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.items = objects;
		this.resourceId = resource;
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

		Recipe item = this.items.get(position);

		// 素材名
		TextView matelialNameView = (TextView) view.findViewById(R.id.MatelialNameView);
		matelialNameView.setText(item.getMatelialName());

		// 分量(0の場合は空欄)
		TextView quantityView = (TextView) view.findViewById(R.id.QuantityView);
		if (item.getQuantity() == 0) {
			quantityView.setText(item.getUnit());
		} else {
			quantityView.setText(String.valueOf(item.getQuantity()) + " " + item.getUnit());
		}

		return view;
	}
}
