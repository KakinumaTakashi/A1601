package jp.ecweb.homes.a1601.model;

import java.util.List;

/**
 * 楽天商品検索API レスポンス
 *
 * Created by KakinumaTakashi on 2016/09/02.
 */
public class RakutenResponse {

	// 全体情報
	private int count;      // 検索数
	private int page;       // ページ番号
	private int first;      // ページ内商品始追番
	private int last;       // ページ内商品終追番
	private int hits;       // ヒット件数番
	private int carrier;    // キャリア情報
	private int pageCount;  // 総ページ数

	// 商品情報
	private List<RakutenItem> rakutenItems;



	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getCarrier() {
		return carrier;
	}

	public void setCarrier(int carrier) {
		this.carrier = carrier;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<RakutenItem> getRakutenItems() {
		return rakutenItems;
	}

	public void setRakutenItems(List<RakutenItem> rakutenItems) {
		this.rakutenItems = rakutenItems;
	}
}
