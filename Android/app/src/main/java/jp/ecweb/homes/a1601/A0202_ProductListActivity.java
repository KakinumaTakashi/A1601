package jp.ecweb.homes.a1601;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import jp.ecweb.homes.a1601.Cocktail.Product;

public class A0202_ProductListActivity extends AppCompatActivity {

    private ListView listView;
    private List<Product> productList = new ArrayList<Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 画面を縦方向に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_a0202__product_list);

        // サーバーの商品リスト取得URL
        String url = getString(R.string.server_URL) + "getProductList.php";

        // 商品リストの受信処理(JSONをListViewに表示)
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
                                Product product = new Product();
                                product.setId(jsonObject.getString("ID"));
                                product.setMaterialID(jsonObject.getString("MatelialID"));
                                product.setCategory1(jsonObject.getString("Category1"));
                                product.setCategory2(jsonObject.getString("Category2"));
                                product.setCategory3(jsonObject.getString("Category3"));
                                product.setMaterialName(jsonObject.getString("MaterialName"));
                                product.setName(jsonObject.getString("Name"));
                                product.setMaker(jsonObject.getString("Maker"));
                                product.setBrand(jsonObject.getString("Brand"));
                                product.setCapacity(jsonObject.getLong("Capacity"));
                                product.setUnit(jsonObject.getString("Unit"));
                                product.setAlcoholDegree(
                                        (float) jsonObject.getDouble("AlcoholDegree"));

                                String ThumbnailURL = jsonObject.getString("ThumbnailURL");
                                if (ThumbnailURL == "null") {
                                    ThumbnailURL = getString(R.string.server_URL) +
                                            getString(R.string.photo_URL) +
                                            getString(R.string.NoThumbnail_URL);
                                } else {
                                    ThumbnailURL = getString(R.string.server_URL) +
                                            getString(R.string.photo_URL) +
                                            ThumbnailURL;
                                }
                                product.setThumbnailURL(ThumbnailURL);

                                String PhotoURL = jsonObject.getString("PhotoURL");
                                if (PhotoURL == "null") {
                                    PhotoURL = getString(R.string.server_URL) +
                                            getString(R.string.photo_URL) +
                                            getString(R.string.NoImage_URL);
                                } else {
                                    PhotoURL = getString(R.string.server_URL) +
                                            getString(R.string.photo_URL) +
                                            PhotoURL;
                                }
                                product.setPhotoURL(PhotoURL);

                                productList.add(product);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // ListViewのアダプターを登録
                        ProductListAdapter adapter = new ProductListAdapter(getBaseContext(),
                                R.layout.activity_product_list_item, productList);
                        listView = (ListView) findViewById(R.id.listView);
                        listView.setAdapter(adapter);
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
