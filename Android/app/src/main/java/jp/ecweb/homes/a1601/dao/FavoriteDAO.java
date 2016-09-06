package jp.ecweb.homes.a1601.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.helper.MySQLiteOpenHelper;
import jp.ecweb.homes.a1601.model.Favorite;

/**
 * SQLite お気に入りテーブル操作クラス
 *
 * Created by KakinumaTakashi on 2016/09/04.
 */
public class FavoriteDAO {

	private SQLiteDatabase mDb;

/*--------------------------------------------------------------------------------------------------
	コンストラクタ
--------------------------------------------------------------------------------------------------*/
	public FavoriteDAO(Context context) {
		MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
		this.mDb = mySQLiteOpenHelper.getWritableDatabase();
	}


/*--------------------------------------------------------------------------------------------------
	メソッド
--------------------------------------------------------------------------------------------------*/
	public void close() {
		if (mDb != null) mDb.close();
	}

	// カクテルID存在チェック処理
	public Boolean ExistCocktailId(String cocktailId) {
		// クエリ作成
		String sql = "SELECT CocktailID FROM favorite WHERE CocktailID=?";

		// クエリ実行
		Cursor cursor = mDb.rawQuery(sql, new String[]{cocktailId});
//		cursor.close();

		// チェック結果を返す
		return cursor.moveToFirst();
	}

	// お気に入りリスト取得処理
	public List<Favorite> getFavoriteList() {
		// クエリ作成
		String sql = "SELECT CocktailID FROM favorite";

		// クエリ実行
		Cursor cursor= mDb.rawQuery(sql, null);
//		cursor.close();
		cursor.moveToFirst();

		// カクテルIDをListに格納
		List<Favorite> favoriteList = new ArrayList<>();

		for (int i = 0; i < cursor.getCount(); i++) {
			Favorite favorite = new Favorite();

			favorite.setCocktailId(cursor.getString(cursor.getColumnIndex("CocktailID")));

			favoriteList.add(favorite);
			cursor.moveToNext();
		}

		return favoriteList;
	}

	// お気に入りリスト追加処理
	public Boolean insertFavorite(Favorite favorite) {
		// 追加するレコードを作成
		ContentValues values = new ContentValues();
		values.put("CocktailID", favorite.getCocktailId());

		// お気に入りテーブルにレコードを追加
		return (mDb.insert("favorite", null, values) >= 0);
	}

	// お気に入りリスト削除処理
	public Boolean deleteFavorite(Favorite favorite) {
		// お気に入りテーブルからレコードを削除
		return (mDb.delete("favorite", "CocktailID=?", new String[]{favorite.getCocktailId()}) >= 0);
	}
}
