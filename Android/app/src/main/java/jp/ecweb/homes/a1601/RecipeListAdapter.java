package jp.ecweb.homes.a1601;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.ecweb.homes.a1601.Cocktail.Recipe;

/**
 * Created by Takashi Kakinuma on 2016/07/19.
 */
public class RecipeListAdapter extends ArrayAdapter<Recipe> {

	private LayoutInflater inflater;
	private List<Recipe> items;
	private int resourceId;

	public RecipeListAdapter(Context context, int resource, List<Recipe> objects) {
		super(context, resource, objects);

		this.inflater =
				(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.items = objects;
		this.resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = this.inflater.inflate(this.resourceId, null);
		} else {
			view = convertView;
		}
Log.d("RecipeListAdapter", "getView start.");
		Recipe item = this.items.get(position);

		TextView matelialNameView = (TextView) view.findViewById(R.id.MatelialNameView);
		matelialNameView.setText(item.getMatelialName());

		TextView quantityView = (TextView) view.findViewById(R.id.QuantityView);
		quantityView.setText(String.valueOf(item.getQuantity()));

		TextView unitView = (TextView) view.findViewById(R.id.UnitView);
		unitView.setText(item.getUnit());

		return view;
	}
}
