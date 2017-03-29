package compute;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后缀名映射 2017/3/20 10:31
 * PS: /Users/chenchubin/Library/Caches/IntelliJIdea2016.3/plugins-sandbox/plugins/computer_code_lines/classes/suffix/suffix.json
 */
public class SuffixMapping {

    public static final String SUFFIX = "suffix"; // 后缀名
    public static final String START_MORE_COMMENT = "start_more_comment"; // 多行注释开头
    public static final String END_MORE_COMMENT = "end_more_comment"; // 多行注释结尾
    public static final String SINGLE_COMMENT = "single_comment"; // 单行注释

    public static Map<String, Map<String, String>> mapData = new HashMap<>(); // 存放数据 2017/3/20 10:56

    /**
     * 初始化后缀名映射 2017/3/20 10:39
     */
    public static void initMapping() {
        BufferedReader bf = null;
        try {
            mapData.clear(); // 清空数据 2017/3/20 10:56
            StringBuilder stringBuilder = new StringBuilder();
//            File file = new File("suffix/suffix.json");
            File file = new File(SuffixMapping.class.getResource("/suffix/suffix.json").getFile()); // 读取class内部资源 2017/3/20 11:34
            if (!file.exists()) // 判断是否存在，不存在，则新创建一个文件 2017/3/20 10:41
                file.createNewFile();

            bf = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }

            // 解析json数据 2017/3/20 10:57
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            JSONObject jsonObject;
            Map<String, String> mapData;
            for (int i = 0; i < jsonArray.length(); i++) {
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
            System.out.println("Suffix mapping Exception is " + e.getMessage());
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    System.out.println("Suffix mapping IOException is " + e.getMessage());
                }
            }
        }
    }

    /**
     * 添加统计类型 2017/3/27 15:08
     * @param data 数据
     */
    public static void addMapping(Map<String, String> data) {
        BufferedWriter bw = null;
        try {
            // 初始化 2017/3/27 16:00
            if (mapData.size() == 0)
                initMapping();

            mapData.put(data.get(SUFFIX), data);
            File file = new File(SuffixMapping.class.getResource("/suffix/suffix.json").getFile()); // 读取class内部资源 2017/3/20 11:34
            if (file.exists()) { // 判断是否存在，存在，则删除 2017/3/21 13:44
                file.delete();
            }

            // 重新创建 2017/3/21 13:45
            file.createNewFile();

            bw = new BufferedWriter(new FileWriter(file));
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            Map<String, String> mapTemp;
            for (String suffix:mapData.keySet()) {
                mapTemp = mapData.get(suffix);
                jsonObject = new JSONObject();
                jsonObject.put(SUFFIX, suffix);
                jsonObject.put(START_MORE_COMMENT, mapTemp.get(START_MORE_COMMENT));
                jsonObject.put(END_MORE_COMMENT, mapTemp.get(END_MORE_COMMENT));
                jsonObject.put(SINGLE_COMMENT, mapTemp.get(SINGLE_COMMENT));

                jsonArray.put(jsonObject);
            }

            bw.write(jsonArray.toString());
        } catch (Exception e) {
            System.out.println("Types Exception is " + e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Types IOException is " + e.getMessage());
                }
            }
        }
    }

    /**
     * 添加统计类型 2017/3/27 15:08
     * @param suffix key值
     */
    public static void removeMapping(String suffix) {
        BufferedWriter bw = null;
        try {
            // 初始化 2017/3/27 16:00
            if (mapData.size() == 0)
                initMapping();

            mapData.remove(suffix);
            File file = new File(SuffixMapping.class.getResource("/suffix/suffix.json").getFile()); // 读取class内部资源 2017/3/20 11:34
            if (file.exists()) { // 判断是否存在，存在，则删除 2017/3/21 13:44
                file.delete();
            }

            // 重新创建 2017/3/21 13:45
            file.createNewFile();

            bw = new BufferedWriter(new FileWriter(file));
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            Map<String, String> mapTemp;
            for (String key:mapData.keySet()) {
                mapTemp = mapData.get(key);
                jsonObject = new JSONObject();
                jsonObject.put(SUFFIX, key);
                jsonObject.put(START_MORE_COMMENT, mapTemp.get(START_MORE_COMMENT));
                jsonObject.put(END_MORE_COMMENT, mapTemp.get(END_MORE_COMMENT));
                jsonObject.put(SINGLE_COMMENT, mapTemp.get(SINGLE_COMMENT));

                jsonArray.put(jsonObject);
            }

            bw.write(jsonArray.toString());
        } catch (Exception e) {
            System.out.println("Types Exception is " + e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Types IOException is " + e.getMessage());
                }
            }
        }
    }

    /**
     * 添加统计类型 2017/3/27 15:08
     * @param lstSuffix key值
     */
    public static void removeMappings(List<String> lstSuffix) {
        if (lstSuffix == null || lstSuffix.size() == 0)
            return;

        BufferedWriter bw = null;
        try {
            // 初始化 2017/3/27 16:00
            if (mapData.size() == 0)
                initMapping();

            for (String suffix:lstSuffix)
                mapData.remove(suffix);

            File file = new File(SuffixMapping.class.getResource("/suffix/suffix.json").getFile()); // 读取class内部资源 2017/3/20 11:34
            if (file.exists()) { // 判断是否存在，存在，则删除 2017/3/21 13:44
                file.delete();
            }

            // 重新创建 2017/3/21 13:45
            file.createNewFile();

            bw = new BufferedWriter(new FileWriter(file));
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            Map<String, String> mapTemp;
            for (String key:mapData.keySet()) {
                mapTemp = mapData.get(key);
                jsonObject = new JSONObject();
                jsonObject.put(SUFFIX, key);
                jsonObject.put(START_MORE_COMMENT, mapTemp.get(START_MORE_COMMENT));
                jsonObject.put(END_MORE_COMMENT, mapTemp.get(END_MORE_COMMENT));
                jsonObject.put(SINGLE_COMMENT, mapTemp.get(SINGLE_COMMENT));

                jsonArray.put(jsonObject);
            }

            bw.write(jsonArray.toString());
        } catch (Exception e) {
            System.out.println("Types Exception is " + e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Types IOException is " + e.getMessage());
                }
            }
        }
    }

}
