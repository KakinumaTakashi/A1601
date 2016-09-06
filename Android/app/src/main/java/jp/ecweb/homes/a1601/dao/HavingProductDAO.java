package jp.ecweb.homes.a1601.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.helper.MySQLiteOpenHelper;
import jp.ecweb.homes.a1601.model.Favorite;
import jp.ecweb.homes.a1601.model.HavingProduct;

/**
 * SQLite 所持製品テーブル操作クラス
 *
 * Created by KakinumaTakashi on 2016/09/06.
 */
public class HavingProductDAO {

	private SQLiteDatabase mDb;

/*--------------------------------------------------------------------------------------------------
	コンストラクタ
--------------------------------------------------------------------------------------------------*/
	public HavingProductDAO(Context context) {
		MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
		this.mDb = mySQLiteOpenHelper.getWritableDatabase();
	}


/*--------------------------------------------------------------------------------------------------
	メソッド
--------------------------------------------------------------------------------------------------*/
	public void close() {
		if (mDb != null) mDb.close();
	}

	// 製品ID存在チェック処理
	public Boolean ExistProductID(String productId) {
		// クエリ作成
		String sql = "SELECT ProductID,MaterialID FROM HavingProduct WHERE ProductID=?";

		// クエリ実行
		Cursor cursor = mDb.rawQuery(sql, new String[]{productId});
//		cursor.close();

		// チェック結果を返す
		return cursor.moveToFirst();
	}

	// 材料ID存在チェック処理
	public Boolean ExistMaterialID(String materialId) {
		// クエリ作成
		String sql = "SELECT ProductID,MaterialID FROM HavingProduct WHERE MaterialID=?";

		// クエリ実行
		Cursor cursor = mDb.rawQuery(sql, new String[]{materialId});
//		cursor.close();

		// チェック結果を返す
		return cursor.moveToFirst();
	}

	// 所持製品テーブル取得処理
	public List<HavingProduct> getProductList() {
		// クエリ作成
		String sql = "SELECT ProductID,MaterialID FROM HavingProduct";

		// クエリ実行
		Cursor cursor= mDb.rawQuery(sql, null);
//		cursor.close();
		cursor.moveToFirst();

		// 所持製品テーブルをListに格納
		List<HavingProduct> productList = new ArrayList<>();

		for (int i = 0; i < cursor.getCount(); i++) {
			HavingProduct product = new HavingProduct();

			product.setProductID(cursor.getString(cursor.getColumnIndex("ProductID")));
			product.setMaterialID(cursor.getString(cursor.getColumnIndex("MaterialID")));

			productList.add(product);
			cursor.moveToNext();
		}

		return productList;
	}

	// 所持製品テーブル追加処理
	public Boolean insertProduct(HavingProduct product) {
		// 追加するレコードを作成
		ContentValues values = new ContentValues();
		values.put("ProductID", product.getProductID());
		values.put("MaterialID", product.getMaterialID());

		// 所持製品テーブルにレコードを追加
		return (mDb.insert("HavingProduct", null, values) >= 0);
	}

	// 所持製品テーブル削除処理
	public Boolean deleteProduct(HavingProduct product) {
		// 所持製品テーブルからレコードを削除
		return (mDb.delete("HavingProduct", "ProductID=?", new String[]{product.getProductID()}) >= 0);
	}

}
