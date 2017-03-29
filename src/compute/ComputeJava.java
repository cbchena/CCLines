package compute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 计算java行数 2017/3/19 07:16
 */
public class ComputeJava {

    /**
     * 计算 2017/3/19 07:16
     * @param file 文件
     * @param suffix 后缀名
     * @return
     */
    public static List<Long> compute(File file, String suffix) throws Exception {
        long normalLines = 0; // 空行
        long commentLines = 0; // 注释行
        long writeLines = 0; // 代码行

        Map<String, String> mapData = SuffixMapping.mapData.get(suffix.toUpperCase()); // 获取统计格式 2017/3/20 10:59
        String[] start = mapData.get(SuffixMapping.START_MORE_COMMENT).split(",");
        String[] end = mapData.get(SuffixMapping.END_MORE_COMMENT).split(",");
        String[] single = mapData.get(SuffixMapping.SINGLE_COMMENT).split(",");

        List<String> lstStart = new ArrayList<>();
        for (int i = 0; i < start.length; i++)
            lstStart.add(start[i]);

        List<String> lstEnd = new ArrayList<>();
        for (int i = 0; i < end.length; i++)
            lstEnd.add(end[i]);

        List<String> lstSingle = new ArrayList<>();
        for (int i = 0; i < single.length; i++)
            lstSingle.add(single[i]);

        boolean comment = false; // 当前是否处于多行注释 2017/3/19 07:22
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String startWithType = "";
        while ((line = br.readLine()) != null) {
            line = line.trim();

            // 多行开头 2017/3/20 11:38
            boolean isStart = false;
            if (!comment) { // 不在注释里面，才需要判断开始 2017/3/21 15:26
                for (String startWith : lstStart) {
                    if (startWith != null
                            && startWith.length() > 0
                            && line.startsWith(startWith)) {
                        isStart = true;
                        startWithType = startWith;
                        break;
                    }
                }
            }

            // 单行注释 2017/3/20 11:38
            boolean isSingle = false;
            if (!isStart && !comment) {
                for (String startWith : lstSingle) {
                    if (startWith != null
                            && startWith.length() > 0
                            && line.startsWith(startWith)) {
                        isSingle = true;
                        break;
                    }
                }
            }

            if (line.matches("^[//s&&[^//n]]*$")) { // 空行 2017/3/19 07:51
                normalLines++;
            } else if (isStart) { // 多行注释 2017/3/19 07:23
                commentLines++;
                if (lstEnd.size() > 0){ // 判断是否当行有结尾 2017/3/20 11:19
                    boolean isEnd = false;
                    for (String endWith:lstEnd) {
                        if (line.length() > startWithType.length() // 防止多行注释开头结尾是一致的，例如python的多行注释 2017/3/21 15:19
                                && line.endsWith(endWith)) {
                            isEnd = true;
                            break;
                        }
                    }

                    comment = !isEnd;
                }
            } else if (comment) {
                commentLines++;
                if (lstEnd.size() > 0){ // 判断是否当行有结尾 2017/3/20 11:19
                    for (String endWith:lstEnd) {
                        if (line.endsWith(endWith)) {
                            comment = false;
                            break;
                        }
                    }
                }

            } else if (isSingle) { // 单行注释 2017/3/20 11:22
                commentLines++;
            } else { // 正常代码行数 2017/3/19 07:22
                writeLines++;
            }
        }

        if (br != null) {
            br.close();
            br = null;
        }

        // 添加展示数据 2017/3/19 07:25
        List<Long> lstData = new ArrayList<>();
        lstData.add(writeLines); // 代码
        lstData.add(commentLines); // 注释
        lstData.add(normalLines); // 空行

        return lstData;
    }

}
