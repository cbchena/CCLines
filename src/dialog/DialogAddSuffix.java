package dialog;

import compute.ComputeType;
import compute.SuffixMapping;
import compute.TypeEntity;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 添加统计类型 2017/3/27 13:58
 */
public class DialogAddSuffix extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtName; // 名称 2017/3/27 14:11
    private JTextField txtSuffix; // 后缀名
    private JTextField txtSingleComment; // 单行注释
    private JTextField txtStartWith; // 多行注释开头
    private JTextField txtEndWith; // 多行注释结尾

    private SelectCallBack _selectCallBack; // 回调函数 2017/3/18 09:42

    public DialogAddSuffix() {

    }

    /**
     * 构造函数 2017/3/27 14:06
     * @param selectCallBack 回调
     */
    public DialogAddSuffix(SelectCallBack selectCallBack) {
        _selectCallBack = selectCallBack;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("添加统计类型"); // 设置title 2017/3/27 14:03
        setSize(600, 400); // 设置窗口大小 2017/3/27 14:03

        // 设置窗口位置 2017/3/27 14:03
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int Swing1x = 600;
        int Swing1y = 300;
        setBounds((screensize.width - Swing1x) / 2, (screensize.height - Swing1y) / 2 - 100, Swing1x, Swing1y);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (buttonOK.isEnabled())
                    onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // 添加键盘输入事件 2017/3/27 14:51
        txtName.getDocument().addDocumentListener(new MyDocumentListener());
        txtSuffix.getDocument().addDocumentListener(new MyDocumentListener());
        txtSingleComment.getDocument().addDocumentListener(new MyDocumentListener());
        txtStartWith.getDocument().addDocumentListener(new MyDocumentListener());
        txtEndWith.getDocument().addDocumentListener(new MyDocumentListener());
    }

    /**
     * 是否能提交 2017/3/27 14:42
     * @return Boolean
     */
    private boolean _isCommit() {
        boolean isCommit = false;

        String name = this.txtName.getText();
        String suffix = this.txtSuffix.getText();
        String single = this.txtSingleComment.getText();
        String start = this.txtStartWith.getText();
        String end = this.txtEndWith.getText();
        if (name != null && name.trim().length() > 0
                && suffix != null && suffix.trim().length() > 0
                && single != null && single.trim().length() > 0
                && start != null && start.trim().length() > 0
                && end != null && end.trim().length() > 0)
            isCommit = true;

        return isCommit;
    }

    /**
     * 提交 2017/3/27 14:59
     */
    private void _commit() {

        // 初始化 2017/3/27 16:03
        SuffixMapping.initMapping();

        String name = this.txtName.getText();
        String suffix = this.txtSuffix.getText();
        String single = this.txtSingleComment.getText();
        String start = this.txtStartWith.getText();
        String end = this.txtEndWith.getText();
        if (name != null && name.trim().length() > 0
                && suffix != null && suffix.trim().length() > 0
                && single != null && single.trim().length() > 0
                && start != null && start.trim().length() > 0
                && end != null && end.trim().length() > 0
                && !SuffixMapping.mapData.containsKey(suffix.trim().toUpperCase())) {
            java.util.List<TypeEntity> lstTypes = ComputeType.getTypes(); // 获取统计类型 2017/3/27 15:00
            TypeEntity typeEntity = new TypeEntity();
            typeEntity.setIndex(lstTypes.size() + 1)
                    .setCheck(true)
                    .setType(name.toUpperCase().trim())
                    .setSuffix(suffix.toUpperCase().trim())
                    .setDel(true);
            lstTypes.add(typeEntity);

            // 设置 2017/3/27 15:04
            ComputeType.setTypes(lstTypes);

            Map<String, String> mapData = new HashMap<>();
            mapData.put(SuffixMapping.SUFFIX, suffix.toUpperCase().trim());
            mapData.put(SuffixMapping.START_MORE_COMMENT, start.toUpperCase().trim());
            mapData.put(SuffixMapping.END_MORE_COMMENT, end.toUpperCase().trim());
            mapData.put(SuffixMapping.SINGLE_COMMENT, single.toUpperCase().trim());

            // 添加类型 2017/3/27 15:13
            SuffixMapping.addMapping(mapData);
        }
    }

    private void onOK() {
        this._commit(); // 提交 2017/3/27 15:13
        this._selectCallBack.select("");

        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DialogAddSuffix dialog = new DialogAddSuffix();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    class MyDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            if (_isCommit())
                buttonOK.setEnabled(true);
            else
                buttonOK.setEnabled(false);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (_isCommit())
                buttonOK.setEnabled(true);
            else
                buttonOK.setEnabled(false);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }
}
