package settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import compute.ComputeType;
import compute.SuffixMapping;
import compute.TypeEntity;
import dialog.DialogAddSuffix;
import dialog.SelectCallBack;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 设置界面 2017/3/20 14:12
 */
public class Settings implements Configurable {

    private JPanel mMainPanel;
    private JButton buttonDel; // 删除按钮 2017/3/20 14:20
    private JButton buttonAdd; // 添加按钮 2017/3/20 14:21
    private JTable table1;
    private DefaultTableModel mDefaultTableModel;

    private java.util.List<TypeEntity> _lstType; // 类型 2017/3/21 13:49
    private boolean _isModify = false; // 是否已经修改 2017/3/21 13:52

    private int _curRow = -1; // 当前行 2017/3/27 15:17
    private String _curSuffix = null; // 当前选择的后缀名 2017/3/27 15:22
    private java.util.List<String> _lstDels;

    /**
     * 在settings中显示的名称 2017/3/20 14:12
     * @return 名称
     */
    @Nls
    @Override
    public String getDisplayName() {
        return "Compute Code Lines";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    /**
     * 初始化控件 2017/3/20 14:19
     * @return
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        System.out.println("============  createComponent  ");

        this._lstDels = new ArrayList<>();

        // 添加点击del事件 2017/3/20 14:20
        buttonDel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _lstType.remove(_curRow);
                _lstDels.add(_curSuffix);
                _isModify = true; // 标志更改 2017/3/27 15:31

                // 刷新界面 2017/3/27 15:34
                mDefaultTableModel.removeRow(_curRow);
            }
        });

        // 添加点击add事件 2017/3/20 14:21
        buttonAdd.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogAddSuffix dialogAddSuffix = new DialogAddSuffix(new SelectCallBack() {
                    @Override
                    public void select(String content) {

                        // 刷新界面 2017/3/27 14:06
                        _refresh();
                    }
                });

                dialogAddSuffix.setVisible(true);
            }
        });

        return mMainPanel;
    }

    /**
     * 初始化表格数据 2017/3/20 14:38
     */
    private void _initTableData(DefaultTableModel tableModel) {
        this._lstType = ComputeType.getTypes(); // 获取显示统计类型 2017/3/21 13:49
        if (this._lstType.size() == 0)
            return;

        Object[][] object = new Object[this._lstType.size()][2];
        int i = 0;
        for (TypeEntity entity:this._lstType) {
            object[i][0] = entity.isCheck();
            object[i][1] = entity.getType();
            i++;
        }

        tableModel.setDataVector(object, new Object[]{"Box", "Type"});
    }

    /**
     * 是否修改 2017/3/20 14:12
     * @return true 激活apply按钮
     */
    @Override
    public boolean isModified() {
        return this._isModify;
    }

    /**
     * 点击【apply】、【OK】时，调用 2017/3/20 14:12
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        if (this._isModify) {
            this._isModify = false; // 标志修改完成，激活apply 2017/3/21 13:53
            ComputeType.setTypes(this._lstType); // 修改 2017/3/21 14:13
            SuffixMapping.removeMappings(this._lstDels);
        }
    }

    /**
     * 点击【Reset】时，调用 2017/3/20 14:13
     */
    @Override
    public void reset() {
        // 重置数据 2017/3/21 14:15
        if (this._isModify) {
            this._refresh();
        }
    }

    /**
     * 刷新 2017/3/27 15:26
     */
    private void _refresh() {
        this._lstType.clear();
        this._isModify = false;

        // 重新初始化 2017/3/21 14:47
        this._initTableData(mDefaultTableModel);

        // 有多少列，setPreferredWidth都得设置，才能生效 2017/3/20 15:54
        table1.getColumnModel().getColumn(0).setPreferredWidth(35);
        table1.getColumnModel().getColumn(0).setMaxWidth(30); // 设置最大值，防止窗口放大缩小时，动态变化 2017/3/20 15:55
        table1.getColumnModel().getColumn(1).setPreferredWidth(465);
        table1.setRowHeight(25);
    }

    /**
     * 创建组件 2017/3/27 15:25
     */
    private void createUIComponents() {
        mDefaultTableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex != 0)
                    return super.getColumnClass(columnIndex);

                return Boolean.class; // 第一列为复选框 2017/3/20 15:26
            }
        };

        // 初始化数据 2017/3/20 14:45
        _initTableData(mDefaultTableModel);
        table1 = new JTable(mDefaultTableModel) {
            public void tableChanged(TableModelEvent e) {
                super.tableChanged(e);
                repaint();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }


        };

        table1.getTableHeader().setPreferredSize(new Dimension(table1.getTableHeader().getWidth(), 35));

        // 添加点击事件 2017/3/20 16:02
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 1) { // 获取点击的复选框 2017/3/20 16:02
                    int columnIndex = table1.columnAtPoint(e.getPoint()); //获取点击的列
                    int rowIndex = table1.rowAtPoint(e.getPoint()); //获取点击的行
                    _curRow = rowIndex;
                    if (columnIndex == 0) {
                        _isModify = true; // 标志修改，激活apply 2017/3/21 13:53
                        boolean isCheck = (boolean) table1.getValueAt(rowIndex, 0);
                        String type = (String) table1.getValueAt(rowIndex, 1);
                        for (TypeEntity entity:_lstType) {
                            if (type.equals(entity.getType())) {
                                entity.setCheck(isCheck);
                            }
                        }
                    }

                    // 判断是否允许删除 2017/3/27 15:19
                    if (_curRow >= 0) {
                        TypeEntity typeEntity = _lstType.get(_curRow);
                        _curSuffix = typeEntity.getSuffix().toUpperCase().trim();
                        if (typeEntity.isDel())
                            buttonDel.setEnabled(true);
                        else
                            buttonDel.setEnabled(false);
                    } else {
                        buttonDel.setEnabled(false);
                    }
                }
            }
        });


        // 有多少列，setPreferredWidth都得设置，才能生效 2017/3/20 15:54
        table1.getColumnModel().getColumn(0).setPreferredWidth(35);
        table1.getColumnModel().getColumn(0).setMaxWidth(30); // 设置最大值，防止窗口放大缩小时，动态变化 2017/3/20 15:55
        table1.getColumnModel().getColumn(1).setPreferredWidth(465);
        table1.setRowHeight(25);
    }
}
