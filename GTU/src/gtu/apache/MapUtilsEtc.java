package gtu.apache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapUtilsEtc {
	
	private Map putValueToListAppend(Map map, Object key, Object value){
		List val = null;
		if(map.containsKey(key)){
			val = (List)map.get(key);
		}else{
			val = new ArrayList();
		}
		val.add(value);
		map.put(key, val);
		return map;
	}

    /**
     * 比較key存在map1但不存在map2 
     * @param map1
     * @param map2
     * @return
     */
    private Map<String,Object> getOnlyInMap1Attribute(HashMap<String, String> map1, HashMap<String, String> map2){
        Map<String,Object> rtn = new HashMap<String,Object>();
        Set<String> map1set = map1.keySet();
        map1set.removeAll(map2.keySet());
        for(Iterator<String> it = map1set.iterator(); it.hasNext();){
            String key = it.next();
            if(!map1.get(key).equals(map2.get(key))){
                rtn.put(key, map1.get(key));
            }
        }
        return rtn;
    }
    
    /**
     * 比較相同key不同value , 傳回key = value(map1.value , map2.value)
     * @param map1
     * @param map2
     * @return
     */
    private Map<String,Object[]> compareMapDifferentValue(HashMap<String, String> map1, HashMap<String, String> map2){
        Map<String,Object[]> rtn = new HashMap<String,Object[]>();
        Set<String> map1set = map1.keySet();
        map1set.retainAll(map2.keySet());
        for(Iterator<String> it = map1set.iterator(); it.hasNext();){
            String key = it.next();
            if(!map1.get(key).equals(map2.get(key))){
                rtn.put(key, new Object[]{map1.get(key), map2.get(key)});
            }
        }
        return rtn;
    }
}
