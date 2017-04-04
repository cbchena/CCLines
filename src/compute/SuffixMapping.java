package compute;

import com.intellij.ide.util.PropertiesComponent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后缀名映射 2017/3/20 10:31
 */
public class SuffixMapping {

    private static final String SUFFIX_DATA_KEY = "suffix_data_key";

    public static final String SUFFIX = "suffix"; // 后缀名
    public static final String START_MORE_COMMENT = "start_more_comment"; // 多行注释开头
    public static final String END_MORE_COMMENT = "end_more_comment"; // 多行注释结尾
    public static final String SINGLE_COMMENT = "single_comment"; // 单行注释

    public static Map<String, Map<String, String>> mapData = new HashMap<>(); // 存放数据 2017/3/20 10:56

    /**
     * 初始化后缀名映射 2017/3/20 10:39
     */
    public static void initMapping() {
        try {
            mapData.clear(); // 清空数据 2017/3/20 10:56

            // 获取数据 2017/4/1 19:56
            String[] data = _getData();
            String json = "[";
            for (int i = 0; i < data.length; i++) {
                json += data[i] + ",";
            }

            json += "]";

            // 解析json数据 2017/3/20 10:57
            JSONArray jsonArray = JSONArray.fromObject(json);
            JSONObject jsonObject;
            Map<String, String> mapData;
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    mapData = new HashMap<>();
                    mapData.put(START_MORE_COMMENT, jsonObject.optString(START_MORE_COMMENT).toUpperCase().trim());
                    mapData.put(END_MORE_COMMENT, jsonObject.optString(END_MORE_COMMENT).toUpperCase().trim());
                    mapData.put(SINGLE_COMMENT, jsonObject.optString(SINGLE_COMMENT).toUpperCase().trim());

                    SuffixMapping.mapData.put(jsonObject.optString(SUFFIX).toUpperCase().trim(), mapData);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Suffix mapping Exception is " + e.getMessage());
        }
    }

    /**
     * 获取数据列表 2017/4/1 20:09
     * @return
     */
    private static String[] _getData() {
        if (PropertiesComponent.getInstance().isValueSet(SUFFIX_DATA_KEY)) {
            return PropertiesComponent.getInstance().getValues(SUFFIX_DATA_KEY);
        } else {
            return new String[] {"{\"suffix\": \"JAVA\", \"start_more_comment\": \"/*\", \"end_more_comment\": \"*/\", \"single_comment\": \"//\"}",
            "{\"suffix\": \"XML\", \"start_more_comment\": \"<!--\", \"end_more_comment\": \"-->\", \"single_comment\": \"\"}",
            "{\"suffix\": \"PY\", \"start_more_comment\": \"''',\\\"\\\"\\\"\", \"end_more_comment\": \"''',\\\"\\\"\\\"\", \"single_comment\": \"#\"}"};
        }
    }

    /**
     * 保存数据 2017/4/1 20:20
     * @param array
     */
    private static void _saveData(String[] array) {
        PropertiesComponent.getInstance().setValues(SUFFIX_DATA_KEY, array);
    }

    /**
     * 添加统计类型 2017/3/27 15:08
     * @param data 数据
     */
    public static void addMapping(Map<String, String> data) {
        try {
            // 初始化 2017/3/27 16:00
            if (mapData.size() == 0)
                initMapping();

            if (data != null)
                mapData.put(data.get(SUFFIX), data);

            JSONObject jsonObject;
            Map<String, String> mapTemp;
            String[] datas = new String[mapData.size()];
            int idx = 0;
            for (String suffix:mapData.keySet()) {
                mapTemp = mapData.get(suffix);
                jsonObject = new JSONObject();
                jsonObject.put(SUFFIX, suffix);
                jsonObject.put(START_MORE_COMMENT, mapTemp.get(START_MORE_COMMENT));
                jsonObject.put(END_MORE_COMMENT, mapTemp.get(END_MORE_COMMENT));
                jsonObject.put(SINGLE_COMMENT, mapTemp.get(SINGLE_COMMENT));

                datas[idx++] = jsonObject.toString();
            }

            _saveData(datas);
        } catch (Exception e) {
            System.out.println("Types Exception is " + e.getMessage());
        }
    }

    /**
     * 添加统计类型 2017/3/27 15:08
     * @param suffix key值
     */
    public static void removeMapping(String suffix) {
        // 初始化 2017/3/27 16:00
        if (mapData.size() == 0)
            initMapping();

        if (mapData.containsKey(suffix))
            mapData.remove(suffix);

        addMapping(null);
    }

    /**
     * 添加统计类型 2017/3/27 15:08
     * @param lstSuffix key值
     */
    public static void removeMappings(List<String> lstSuffix) {
        if (lstSuffix == null || lstSuffix.size() == 0)
            return;

        // 初始化 2017/3/27 16:00
        if (mapData.size() == 0)
            initMapping();

        for (String suffix:lstSuffix)
            if (mapData.containsKey(suffix))
                mapData.remove(suffix);

        addMapping(null);
    }

}
