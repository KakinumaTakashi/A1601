package jp.ecweb.homes.a1601;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// 画面を縦方向に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_a0101__main);
    }

// ボタン押下イベント
    // 材料からカクテルを探す
    public void onMaterialToCocktailButtonTapped(View view) {
        Intent intent = new Intent(this, A0201_ProductToCocktailActivity.class);
        startActivity(intent);
    }

    // カクテル一覧
    public void onCocktailListButton(View view) {
        Intent intent = new Intent(this, A0301_CocktailListActivity.class);
        startActivity(intent);
    }

    // 戻る
    public void onBackButtonTapped(View view) {
        finish();
    }

}
