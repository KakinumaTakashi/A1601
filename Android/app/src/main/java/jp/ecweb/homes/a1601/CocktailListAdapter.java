package jp.ecweb.homes.a1601;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jp.ecweb.homes.a1601.Cocktail.Cocktail;

/**
 * Created by Takashi Kakinuma on 2016/07/14.
 */
public class CocktailListAdapter extends ArrayAdapter<Cocktail> {

	private LayoutInflater inflater;
	private List<Cocktail> items;
	private int resourceId;

	public CocktailListAdapter(Context context, int resource, List<Cocktail> objects) {
		super(context, resource, objects);

		this.inflater =
				(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.items = objects;
		this.resourceId = resource;

	}

	public void UpdateItemList(List<Cocktail> objects) {
		this.items = objects;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		View view;

		if (convertView == null) {
			view = this.inflater.inflate(this.resourceId, null);
		} else {
			view = convertView;
		}

		Cocktail item = this.items.get(position);

		TextView cocktailIDView = (TextView) view.findViewById(R.id.cocktailIDView);
		cocktailIDView.setText(item.getId());

		TextView cocktailNameView = (TextView) view.findViewById(R.id.cocktailNameView);
		cocktailNameView.setText(item.getName());

		ImageLoader imageLoader = NetworkSingleton.getInstance(this.getContext()).getImageLoader();
		NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.cocktailImageView);
		imageView.setImageUrl(item.getThumbnailURL(), imageLoader);

		return view;
	}
}
