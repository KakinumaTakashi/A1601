package jp.ecweb.homes.a1601;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	// ログ出力
	private final String LOG_TAG = "A1601";
	private final String LOG_CLASSNAME = this.getClass().getSimpleName() + " : ";

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// 画面を縦方向に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_a0101__main);
    }

/*--------------------------------------------------------------------------------------------------
	メニューイベント処理
--------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// リソースの登録
        getMenuInflater().inflate(R.menu.menu_a0101__main, menu);

		// タップリスナーの登録
		// 戻る
		menu.findItem(R.id.menu_back).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						finish();
						return true;
					}
				}
		);

		return true;
    }

/*--------------------------------------------------------------------------------------------------
	ボタンタップ処理
--------------------------------------------------------------------------------------------------*/
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
}
