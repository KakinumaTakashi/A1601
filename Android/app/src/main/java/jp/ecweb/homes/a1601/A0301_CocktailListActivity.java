package jp.ecweb.homes.a1601;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Cocktail.Cocktail;

public class A0301_CocktailListActivity extends AppCompatActivity {

	private ListView listView;
	private List<Cocktail> cocktailList = new ArrayList<Cocktail>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 画面を縦方向に固定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_a0301__cocktail_list);

		// サーバーのカクテルリスト取得URL
		String url = getString(R.string.server_URL) + "getCocktailList.php";

		// カクテルリストの受信処理(JSONをListViewに表示)
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						try {
							Log.d("A1601", "Start JsonArrayRequest.onResponse");

							// JSONをListに展開
							for (int i = 0; i < response.length(); i++) {
								JSONObject jsonObject = response.getJSONObject(i);
								Cocktail cocktail = new Cocktail();
								cocktail.setId(jsonObject.getString("ID"));
								cocktail.setName(jsonObject.getString("Name"));
								cocktail.setThumbnailURL(getString(R.string.server_URL) +
										getString(R.string.photo_URL) +
										jsonObject.getString("ThumbnailURL"));
								cocktailList.add(cocktail);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						// ListViewのアダプターを登録
						CocktailListAdapter adapter = new CocktailListAdapter(getBaseContext(),
								R.layout.activity_cocktail_list_item, cocktailList);
						listView = (ListView) findViewById(R.id.listView);
						listView.setAdapter(adapter);

						// アイテムのリスナーを登録(詳細画面にカクテルIDを渡して遷移)
						listView.setOnItemClickListener(
								new AdapterView.OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> parent, View view,
															int position, long id) {
										// タップされたアイテムのカクテルIDを取得
										Cocktail cocktail =
												(Cocktail) parent.getItemAtPosition(position);
										Log.d("A1601", "Select Item is ...");
										Log.d("A1601", "  ID : " + cocktail.getId());
										Log.d("A1601", "  Name : " + cocktail.getName());

										// 詳細画面に遷移
										Intent intent = new Intent(
												A0301_CocktailListActivity.this,
												A0302_CocktailActivity.class);
										intent.putExtra("ID", cocktail.getId());
										startActivity(intent);
									}
								}
						);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("A1601 onErrorResponse", error.toString());
					}
				}
		);

		// カクテルリスト取得のリクエストを送信
		NetworkSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
	}

	public void onBackButtonTapped(View view) {
		finish();
	}
}
