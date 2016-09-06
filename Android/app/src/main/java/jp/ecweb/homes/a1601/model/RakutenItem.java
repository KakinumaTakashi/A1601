package jp.ecweb.homes.a1601.model;

import java.util.List;

/**
 * 楽天商品検索API 商品情報詳細
 *
 * Created by KakinumaTakashi on 2016/09/02.
 */
public class RakutenItem {

	// 商品情報詳細
	private String itemName;                // 商品名
	private String catchcopy;               // キャッチコピー
	private String itemCode;                // 商品コード
	private int itemPrice;                  // 商品価格
	private String itemCaption;             // 商品説明文
	private String itemUrl;                 // 商品URL
	private String affiliateUrl;            // アフィリエイトURL
	private int imageFlag;                  // 商品画像有無フラグ
	private List<String> smallImageUrls;    // 商品画像64x64URL
	private List<String> mediumImageUrls;   // 商品画像128x128URL
	private int availability;               // 販売可能フラグ
	private int taxFlag;                    // 消費税フラグ
	private int postageFlag;                // 送料フラグ
	private int creditCardFlag;             // クレジットカード利用可能フラグ
	private int shopOfTheYearFlag;          // ショップオブザイヤーフラグ
	private int shipOverseasFlag;           // 海外配送フラグ
	private String shipOverseasArea;        // 海外配送対象地域
	private int asurakuFlag;                // あす楽フラグ
	private String asurakuClosingTime;      // あす楽〆時間
	private String asurakuArea;             // あす楽配送対象地域
	private int affiliateRate;              // アフィリエイト利用利率
	private String startTime;               // 販売開始時刻
	private String endTime;                 // 販売終了時刻
	private int reviewCount;                // レビュー件数
	private Double reviewAverage;           // レビュー平均
	private int pointRate;                  // 商品別ポイント倍付け
	private String pointRateStartTime;      // 商品別ポイント倍付け開始日時
	private String pointRateEndTime;        // 商品別ポイント倍付け終了日時
	private int giftFlag;                   // ギフト包装フラグ

	// 店舗情報
	private String shopName;                // 店舗名
	private String shopCode;                // 店舗コード
	private String shopUrl;                 // 店舗URL
	private String shopAffiliateUrl;        // 店舗アフィリエイトURL


	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCatchcopy() {
		return catchcopy;
	}

	public void setCatchcopy(String catchcopy) {
		this.catchcopy = catchcopy;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public int getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemCaption() {
		return itemCaption;
	}

	public void setItemCaption(String itemCaption) {
		this.itemCaption = itemCaption;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public String getAffiliateUrl() {
		return affiliateUrl;
	}

	public void setAffiliateUrl(String affiliateUrl) {
		this.affiliateUrl = affiliateUrl;
	}

	public int getImageFlag() {
		return imageFlag;
	}

	public void setImageFlag(int imageFlag) {
		this.imageFlag = imageFlag;
	}

	public List<String> getSmallImageUrls() {
		return smallImageUrls;
	}

	public void setSmallImageUrls(List<String> smallImageUrls) {
		this.smallImageUrls = smallImageUrls;
	}

	public List<String> getMediumImageUrls() {
		return mediumImageUrls;
	}

	public void setMediumImageUrls(List<String> mediumImageUrls) {
		this.mediumImageUrls = mediumImageUrls;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public int getTaxFlag() {
		return taxFlag;
	}

	public void setTaxFlag(int taxFlag) {
		this.taxFlag = taxFlag;
	}

	public int getPostageFlag() {
		return postageFlag;
	}

	public void setPostageFlag(int postageFlag) {
		this.postageFlag = postageFlag;
	}

	public int getCreditCardFlag() {
		return creditCardFlag;
	}

	public void setCreditCardFlag(int creditCardFlag) {
		this.creditCardFlag = creditCardFlag;
	}

	public int getShopOfTheYearFlag() {
		return shopOfTheYearFlag;
	}

	public void setShopOfTheYearFlag(int shopOfTheYearFlag) {
		this.shopOfTheYearFlag = shopOfTheYearFlag;
	}

	public int getShipOverseasFlag() {
		return shipOverseasFlag;
	}

	public void setShipOverseasFlag(int shipOverseasFlag) {
		this.shipOverseasFlag = shipOverseasFlag;
	}

	public String getShipOverseasArea() {
		return shipOverseasArea;
	}

	public void setShipOverseasArea(String shipOverseasArea) {
		this.shipOverseasArea = shipOverseasArea;
	}

	public int getAsurakuFlag() {
		return asurakuFlag;
	}

	public void setAsurakuFlag(int asurakuFlag) {
		this.asurakuFlag = asurakuFlag;
	}

	public String getAsurakuClosingTime() {
		return asurakuClosingTime;
	}

	public void setAsurakuClosingTime(String asurakuClosingTime) {
		this.asurakuClosingTime = asurakuClosingTime;
	}

	public String getAsurakuArea() {
		return asurakuArea;
	}

	public void setAsurakuArea(String asurakuArea) {
		this.asurakuArea = asurakuArea;
	}

	public int getAffiliateRate() {
		return affiliateRate;
	}

	public void setAffiliateRate(int affiliateRate) {
		this.affiliateRate = affiliateRate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public Double getReviewAverage() {
		return reviewAverage;
	}

	public void setReviewAverage(Double reviewAverage) {
		this.reviewAverage = reviewAverage;
	}

	public int getPointRate() {
		return pointRate;
	}

	public void setPointRate(int pointRate) {
		this.pointRate = pointRate;
	}

	public String getPointRateStartTime() {
		return pointRateStartTime;
	}

	public void setPointRateStartTime(String pointRateStartTime) {
		this.pointRateStartTime = pointRateStartTime;
	}

	public String getPointRateEndTime() {
		return pointRateEndTime;
	}

	public void setPointRateEndTime(String pointRateEndTime) {
		this.pointRateEndTime = pointRateEndTime;
	}

	public int getGiftFlag() {
		return giftFlag;
	}

	public void setGiftFlag(int giftFlag) {
		this.giftFlag = giftFlag;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public String getShopAffiliateUrl() {
		return shopAffiliateUrl;
	}

	public void setShopAffiliateUrl(String shopAffiliateUrl) {
		this.shopAffiliateUrl = shopAffiliateUrl;
	}
}
