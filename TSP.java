import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TSP {

	@SuppressWarnings("unchecked")
	private static void work(Map<Integer, ArrayList<String>> linesSorted, String[] startCity,
			Map<Integer, ArrayList<String>> previousCities, Map<Integer, Map<String, Map<String, Line>>> rootMap) {

		for (Integer key : linesSorted.keySet()) {
			linesSorted.get(key).forEach(line -> {
				String[] values = line.split(" ");
				String dCity = values[0];
				String aCity = values[1];
				Long[] price = { Long.parseLong(values[3]), Long.parseLong(values[3]) };
				if (previousCities.get(key - 1).contains(dCity)) {
					ArrayList<String> aCities = null;
					if (previousCities.get(key) != null) {
						if (!previousCities.get(key).contains(aCity))
							previousCities.get(key).add(aCity);
					} else {
						aCities = new ArrayList<String>();
						aCities.add(aCity);
						previousCities.put(key, aCities);
					}

					Map<String, Line> map2 = null;
					if (rootMap.get(key) != null && rootMap.get(key).get(dCity) != null) {
						map2 = rootMap.get(key).get(dCity);
					} else
						map2 = new HashMap<String, Line>();

					if (rootMap.get(key - 1) != null) {
						for (String keyCity : rootMap.get(key - 1).keySet()) {
							Long[] newPrice = {price[0], price[1] + (rootMap.get(key - 1).get(keyCity).get(dCity) != null
									? rootMap.get(key - 1).get(keyCity).get(dCity).getPrices()[1] : 0)};
							
									
							ArrayList<String> history = new ArrayList<String>();
							if (rootMap.get(key - 1).get(keyCity).get(dCity) != null)
								{
								history.addAll(rootMap.get(key - 1).get(keyCity).get(dCity).getHistory());
								history.add(line);
								map2.put(aCity, new Line(newPrice, history));
								}
						}
					} else if (key == 0){
						ArrayList<String> history = new ArrayList<String>();
						history.add(line);
						map2.put(aCity, new Line(price, history));
					}

					Map<String, Map<String, Line>> map1 = null;
					if (rootMap.get(key) != null) {
						map1 = rootMap.get(key);
					} else
						map1 = new HashMap<String, Map<String, Line>>();

					map1.put(dCity, map2);
					rootMap.put(key, map1);

				}
			});
		}
	}

	public static void main(String[] args) throws IOException {
		//long startTime = System.nanoTime();
		Map<Integer, ArrayList<String>> linesSorted = new TreeMap<Integer, ArrayList<String>>();
		String[] startCity = new String[1];
		Map<Integer, ArrayList<String>> previousCities = new TreeMap<Integer, ArrayList<String>>();
		Map<Integer, Map<String, Map<String, Line>>> rootMap = new TreeMap<Integer, Map<String, Map<String, Line>>>();

		Files.lines(Paths.get(args[0])).filter(line -> {
			if (startCity[0] == null && line.length() < 4) {
				startCity[0] = line;
				ArrayList<String> aCities = new ArrayList<String>();
				aCities.add(line);
				previousCities.put(-1, aCities);
				return false;
			}

			return true;
		}).forEach(line -> {
			String[] values = line.split(" ");
			Integer day = Integer.parseInt(values[2]);
			if (linesSorted.get(day) == null) {
				ArrayList<String> lines = new ArrayList<String>();
				lines.add(line);
				linesSorted.put(day, lines);
			} else
				linesSorted.get(day).add(line);
		});

		work(linesSorted, startCity, previousCities, rootMap);

		int finalKey = rootMap.size() - 1;
		Long finalPrice = 0L;
		String keyToSelect = null;
		for (String key1 : rootMap.get(finalKey).keySet()) {
			for (String key : rootMap.get(finalKey).get(key1).keySet()) {
				if (key.equals(startCity[0])) {
					Long newPrice = rootMap.get(finalKey).get(key1).get(key).getPrices()[1];
					if (finalPrice == 0 || newPrice < finalPrice) {
						finalPrice = newPrice;
						keyToSelect = key1;
					}
				}
			}
			;
		}

		System.out.println(rootMap.get(finalKey).get(keyToSelect).get(startCity[0]));
		//long endTime = System.nanoTime();
		//System.out.println((endTime - startTime)/1000000);
	}

}
