package andUtils;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Utils {
	public static boolean isEven(int i) {
		return i%2 == 0;
	}

	public static void cls() throws InterruptedException, IOException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}

	public static ArrayList<Integer> toIntArray(ArrayList<String> in) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		try {
			for(String s : in) {
				out.add(Integer.parseInt(s));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static HashMap<Integer, Integer> sortByValue(Map<Integer, Integer> unsortedMap, final boolean order) {
		//Ascending: input true
		//Descending: input false
		List<Entry<Integer, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
		list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
				? o1.getKey().compareTo(o2.getKey())
						: o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
						? o2.getKey().compareTo(o1.getKey())
								: o2.getValue().compareTo(o1.getValue()));
		return (HashMap<Integer,Integer>) list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
	}

	public static HashMap<Character, Integer> sortCharMapByValue(HashMap<Character, Integer> unsortedMap, final boolean order) {
		//Ascending: input true
		//Descending: input false
		List<Entry<Character, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
		list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
				? o1.getKey().compareTo(o2.getKey())
				: o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
				? o2.getKey().compareTo(o1.getKey())
				: o2.getValue().compareTo(o1.getValue()));
		return (HashMap<Character, Integer>) list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
	}
	
	public static HashMap<Integer,Integer> getIntFrequencies(ArrayList<Integer> list) {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i : list) {
			map.put(i, map.getOrDefault(i, 0)+1);
		}
		return map;
	}
	
	public static ArrayList<Entry<Integer,Integer>> getMostFrequentInt(ArrayList<Integer> list) {
		HashMap<Integer,Integer> map = getIntFrequencies(list);
		HashMap<Integer,Integer> sortedMap = sortByValue(map, false);
		List<Entry<Integer,Integer>> l = new LinkedList<>(sortedMap.entrySet());
		ArrayList<Entry<Integer,Integer>> outList = new ArrayList<Entry<Integer,Integer>>();
		for(Entry<Integer, Integer> entry : l) {
			if(l.get(0).getValue() == entry.getValue()) {
				outList.add(entry);
			}
		}
		return outList;
	}

	public static void rmChar(ArrayList<Character> list, char c) {
		list.removeAll(Collections.singleton(Character.toLowerCase(c)));
		list.removeAll(Collections.singleton(Character.toUpperCase(c)));
	}

}
