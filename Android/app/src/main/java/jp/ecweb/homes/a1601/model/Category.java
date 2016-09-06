package jp.ecweb.homes.a1601.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KakinumaTakashi on 2016/09/03.
 */
public class Category {

	private String category1;                   // 頭文字
	private String category2;                   // ベース種類

	private List<String> category1List;         // 頭文字一覧
	private List<String> category1ValueList;    // 頭文字別内部値
	private List<String> category1NumList;      // 頭文字別件数
	private List<String> category2List;         // ベース種類一覧
	private List<String> category2NumList;      // ベース種類別件数
	private List<String> category2ValueList;    // ベース種類別内部値

	public Category() {
		resetCategoryAll();
		this.category1List = new ArrayList<>();
		this.category1ValueList = new ArrayList<>();
		this.category1NumList = new ArrayList<>();
		this.category2List = new ArrayList<>();
		this.category2NumList = new ArrayList<>();
		this.category2ValueList = new ArrayList<>();
	}

	public void resetCategoryAll() {
		resetCategory1();
		resetCategory2();
	}

	public void resetCategory1() {
		this.category1 = "All";
	}

	public void resetCategory2() {
		this.category2 = "All";
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

	public List<String> getCategory1List() {
		return category1List;
	}

	public void setCategory1List(List<String> category1List) {
		this.category1List = category1List;
	}

	public List<String> getCategory2List() {
		return category2List;
	}

	public void setCategory2List(List<String> category2List) {
		this.category2List = category2List;
	}

	public List<String> getCategory1NumList() {
		return category1NumList;
	}

	public void setCategory1NumList(List<String> category1NumList) {
		this.category1NumList = category1NumList;
	}

	public List<String> getCategory2NumList() {
		return category2NumList;
	}

	public void setCategory2NumList(List<String> category2NumList) {
		this.category2NumList = category2NumList;
	}

	public List<String> getCategory1ValueList() {
		return category1ValueList;
	}

	public void setCategory1ValueList(List<String> category1ValueList) {
		this.category1ValueList = category1ValueList;
	}

	public List<String> getCategory2ValueList() {
		return category2ValueList;
	}

	public void setCategory2ValueList(List<String> category2ValueList) {
		this.category2ValueList = category2ValueList;
	}
}
