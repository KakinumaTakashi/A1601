package jp.ecweb.homes.a1601.Cocktail;

import io.realm.annotations.PrimaryKey;

/**
 * Created by Takashi Kakinuma on 2016/07/14.
 */
public class Cocktail {

    @PrimaryKey
    private String id;					// カクテルID
    private String name;				// カクテル名
    private String photoUrl;			// 写真
    private String thumbnailURL;		// サムネイルURL
    private String detail;			// 詳細
    private String recipeId;			// レシピID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}
}
