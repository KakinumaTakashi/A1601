package jp.ecweb.homes.a1601.model;

/**
 * Created by Takashi Kakinuma on 2016/07/14.
 */
public class Product {

    private String id;                  // 商品ID
    private String materialID;        // 素材ID
    private String category1;			// 大分類
    private String category2;			// 中分類
    private String category3;			// 小分類
    private String materialName;		// 素材名
    private String name;              // 商品名
    private String maker;             // メーカー名
    private String brand;             // ブランド名
    private float alcoholDegree;    // アルコール度数(%)
    private String thumbnailURL;     // サムネイルURL
    private String itemCode;            // 楽天商品コード

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getAlcoholDegree() {
        return alcoholDegree;
    }

    public void setAlcoholDegree(float alcoholDegree) {
        this.alcoholDegree = alcoholDegree;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
}
