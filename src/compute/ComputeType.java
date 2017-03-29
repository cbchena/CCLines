package compute;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 计算的类型 2017/3/21 13:37
 */
public class ComputeType {

    /**
     * 获取类型 2017/3/21 13:42
     * @return 类型列表
     */
    public static List<TypeEntity> getTypes() {
        List<TypeEntity> lstType = new ArrayList<>();
        BufferedReader bf = null;
        try {
            lstType.clear(); // 清空数据 2017/3/20 10:56
            File file = new File(SuffixMapping.class.getResource("/suffix/compute.txt").getFile()); // 读取class内部资源 2017/3/20 11:34
            if (!file.exists()) // 判断是否存在，不存在，则新创建一个文件 2017/3/21 13:44
                file.createNewFile();

            bf = new BufferedReader(new FileReader(file));
            String line;
            String[] types;
            TypeEntity typeEntity;
            while ((line = bf.readLine()) != null) {
                types = line.split("\\*\\*");
                typeEntity = new TypeEntity();
                typeEntity.setIndex(Integer.parseInt(types[0]))
                        .setCheck(Boolean.valueOf(types[1]))
                        .setType(types[2].toUpperCase())
                        .setSuffix(types[3].toUpperCase())
                        .setDel(Boolean.valueOf(types[4]));

                lstType.add(typeEntity);
            }

            // 排序 2017/3/21 14:11
            Collections.sort(lstType, new Comparator<TypeEntity>() {
                public int compare(TypeEntity entity, TypeEntity newEntity) {
                    if (entity.getIndex() < newEntity.getIndex()) {
                        return -1;
                    } else if (entity.getIndex() == newEntity.getIndex()) {
                        return 0;
                    } else {
                        return 1;
                    }

                }
            });
        } catch (Exception e) {
            System.out.println("Types Exception is " + e.getMessage());
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    System.out.println("Types IOException is " + e.getMessage());
                }
            }
        }

        return lstType;
    }

    /**
     * 设置类型 2017/3/21 13:42
     * @param lstTypes 类型
     * @return 是否成功
     */
    public static boolean setTypes(List<TypeEntity> lstTypes) {
        boolean isResult = false;
        BufferedWriter bw = null;
        try {
            File file = new File(SuffixMapping.class.getResource("/suffix/compute.txt").getFile()); // 读取class内部资源 2017/3/20 11:34
            if (file.exists()) { // 判断是否存在，存在，则删除 2017/3/21 13:44
                file.delete();
            }

            // 重新创建 2017/3/21 13:45
            file.createNewFile();

            bw = new BufferedWriter(new FileWriter(file));
            for (TypeEntity entity:lstTypes) {
                bw.write(entity.getIndex() + "**" + entity.isCheck() + "**"
                        + entity.getType().toUpperCase() + "**" + entity.getSuffix().toUpperCase()
                        + "**" + entity.isDel() + "\n");
            }
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

        return isResult;
    }

}
