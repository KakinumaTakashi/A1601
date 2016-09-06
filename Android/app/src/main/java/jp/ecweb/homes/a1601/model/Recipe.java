package jp.ecweb.homes.a1601.model;

/**
 * Created by Takashi Kakinuma on 2016/07/14.
 */
public class Recipe {

	// メンバ変数
	private String id;					// レシピID
	private String cocktailID;		// カクテルID
	private String matelialID;		// 素材ID
	private String category1;			// 大分類
	private String category2;			// 中分類
	private String category3;			// 小分類
	private String matelialName;		// 素材名
	private int quantity;			// 分量
	private String unit;				// 単位

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getCocktailID() { return cocktailID; }
	public void setCocktailID(String cocktailID) { this.cocktailID = cocktailID;}

	public String getMatelialID() { return matelialID; }
	public void setMatelialID(String matelialID) { this.matelialID = matelialID; }

	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }

	public String getUnit() { return unit; }
	public void setUnit(String unit) { this.unit = unit; }

	public String getCategory1() { return category1; }
	public void setCategory1(String category1) { this.category1 = category1; }

	public String getCategory2() { return category2; }
	public void setCategory2(String category2) { this.category2 = category2; }

	public String getCategory3() { return category3; }
	public void setCategory3(String category3) { this.category3 = category3; }

	public String getMatelialName() { return matelialName; }
	public void setMatelialName(String matelialName) { this.matelialName = matelialName; }
}
