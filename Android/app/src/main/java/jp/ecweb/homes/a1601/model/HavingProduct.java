package jp.ecweb.homes.a1601.model;

/**
 *  SQLite 所持製品テーブル モデル
 *
 * Created by KakinumaTakashi on 2016/09/06.
 */
public class HavingProduct {
	private String ProductID;                   // 製品ID
	private String MaterialID;                  // 材料ID

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getMaterialID() {
		return MaterialID;
	}

	public void setMaterialID(String materialID) {
		MaterialID = materialID;
	}
}
