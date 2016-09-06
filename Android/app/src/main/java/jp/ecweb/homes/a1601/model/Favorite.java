package jp.ecweb.homes.a1601.model;

/**
 * SQLite お気に入りテーブル モデル
 *
 * Created by KakinumaTakashi on 2016/09/04.
 */
public class Favorite {

	private String cocktailId;              // カクテルID


	public String getCocktailId() {
		return cocktailId;
	}

	public void setCocktailId(String cocktailId) {
		this.cocktailId = cocktailId;
	}
}
