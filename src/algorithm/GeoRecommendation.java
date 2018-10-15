package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.experimental.categories.Categories;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

public class GeoRecommendation {
	public List<Item>recommendItems(String userId, double lat, double lon){
		List<Item> recommendItems = new ArrayList<>();
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			//get all favorit itms 
			Set<String> favItemsIds = conn.getFavoriteItemIds(userId);
			System.out.println("favSize" + favItemsIds.size());
			//get all categories 
			Map<String, Integer> allCategories = new HashMap<>();
			for (String faveItemId: favItemsIds) {
				Set<String> categories = conn.getCategories(faveItemId);
				for (String category: categories) {
					int count = allCategories.getOrDefault(category, 0);
					allCategories.put(category, count+1);
				}
			}
			//sort according to distance 
			List<Entry<String, Integer>> categoryList = new ArrayList<Entry<String, Integer>>(allCategories.entrySet());
			for (Entry entry: categoryList) {
				printEntry(entry);
			}
			Collections.sort(categoryList, (Entry<String,Integer> e1, Entry<String, Integer> e2) -> {
				return Integer.compare(e2.getValue(), e1.getValue());
			});
			Set<Item> visitedItems = new HashSet<>();
			for (Entry<String, Integer>category : categoryList ) {
				List<Item> items = conn.searchItems(lat, lon, category.getKey());
				for (Item item:items) {
					if (!favItemsIds.contains(item.getItemId()) && !visitedItems.contains(item)) {
						recommendItems.add(item);
						visitedItems.add(item);
					}
				}
			}
			Collections.sort(recommendItems, (Item o1, Item o2) -> {
				return Double.compare(o1.getDistance(), o2.getDistance());
			});
			System.out.println("size"+recommendItems.size());
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			conn.close();
		}
		return recommendItems;
	}
	//debugger
	void printEntry(Entry<String, Integer> entry) {
		System.out.println("Name: "+entry.getKey() + " Value: "+entry.getValue());
	}
}
