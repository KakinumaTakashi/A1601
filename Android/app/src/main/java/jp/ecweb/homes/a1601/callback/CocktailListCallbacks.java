package jp.ecweb.homes.a1601.callback;

import java.util.List;

import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.model.Category;

/**
 * Created by KakinumaTakashi on 2016/09/03.
 */
public interface CocktailListCallbacks {
	void CategoryCallback(Category category);
	void ListResponseCallback(List<Cocktail> cocktailList);
}
