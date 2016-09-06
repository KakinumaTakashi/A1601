package jp.ecweb.homes.a1601.callback;

import java.util.List;

import jp.ecweb.homes.a1601.model.Category;
import jp.ecweb.homes.a1601.model.Product;

/**
 * Created by KakinumaTakashi on 2016/09/06.
 */
public interface ProductListCallbacks {
	void CategoryCallback(Category category);
	void ListResponseCallback(List<Product> productList);
}
