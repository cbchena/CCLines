package compute;

/**
 * 类型实体 2017/3/21 14:04
 */
public class TypeEntity {

    private int index; // 索引
    private boolean isCheck; // 是否选择
    private String type; // 类型
    private String suffix; // 后缀类型
    private boolean isDel; // 是否可以删除

    public int getIndex() {
        return index;
    }

    public TypeEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public TypeEntity setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    public String getType() {
        return type;
    }

    public TypeEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public TypeEntity setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public boolean isDel() {
        return isDel;
    }

    public TypeEntity setDel(boolean del) {
        isDel = del;
        return this;
    }
}
