package compute;

import com.intellij.ide.util.PropertiesComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 计算的类型 2017/3/21 13:37
 */
public class ComputeType {

    private static final String TYPE_DATA_KEY = "type_data_key";

    /**
     * 获取类型 2017/3/21 13:42
     * @return 类型列表
     */
    public static List<TypeEntity> getTypes() {
        List<TypeEntity> lstType = new ArrayList<>();
        try {
            lstType.clear(); // 清空数据 2017/3/20 10:56
            String[] lines = _getData();
            String[] types;
            String line = null;
            TypeEntity typeEntity;
            for (int i = 0; i < lines.length; i++) {
                line = lines[i];
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
        }

        return lstType;
    }

    /**
     * 获取数据列表 2017/4/1 20:09
     * @return
     */
    private static String[] _getData() {
        if (PropertiesComponent.getInstance().isValueSet(TYPE_DATA_KEY)) {
            return PropertiesComponent.getInstance().getValues(TYPE_DATA_KEY);
        } else {
            return new String[] {"1**true**Java**java**false",
                    "2**true**Xml**xml**false",
                    "3**true**Python**py**false"};
        }
    }

    /**
     * 保存数据 2017/4/1 20:20
     * @param array
     */
    private static void _saveData(String[] array) {
        PropertiesComponent.getInstance().setValues(TYPE_DATA_KEY, array);
    }

    /**
     * 设置类型 2017/3/21 13:42
     * @param lstTypes 类型
     * @return 是否成功
     */
    public static boolean setTypes(List<TypeEntity> lstTypes) {
        boolean isResult = false;
        try {
            String line;
            String[] lines = new String[lstTypes.size()];
            int idx = 0;
            for (TypeEntity entity:lstTypes) {
                line = entity.getIndex() + "**" + entity.isCheck() + "**"
                        + entity.getType().toUpperCase() + "**" + entity.getSuffix().toUpperCase()
                        + "**" + entity.isDel();

                lines[idx++] = line;
            }

            _saveData(lines);
        } catch (Exception e) {
            System.out.println("Types Exception is " + e.getMessage());
        }

        return isResult;
    }

}
