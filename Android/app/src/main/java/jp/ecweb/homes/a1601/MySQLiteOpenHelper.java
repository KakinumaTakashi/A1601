package jp.ecweb.homes.a1601;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Takashi Kakinuma on 2016/07/21.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	// メンバ変数
	private static final String DATABASE_NAME = "A1601.db";				// ローカルDB名
	private static final int DATABASE_VERSION = 1;						// DBバージョン

	private static final String PRODUCT_TABLE_NAME = "HavingProduct";	// 所持製品テーブル名

	private static final String SQL_CREATE_HAVINGMATERIAL_TABLE =		// DB作成SQL
			"CREATE TABLE " +
					PRODUCT_TABLE_NAME + " (" +
					"ProductID text primary key, " +
					"MaterialID text"
					+ ");";
	private static final String SQL_DROP_HAVINGMATERIAL_TABLE =			// DB破棄SQL
			"DROP TABLE " + PRODUCT_TABLE_NAME + ";";

	// コンストラクタ
	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// DB作成処理
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(SQL_CREATE_HAVINGMATERIAL_TABLE);
	}

	// DB更新処理
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL(SQL_DROP_HAVINGMATERIAL_TABLE);
		onCreate(sqLiteDatabase);
	}
}
