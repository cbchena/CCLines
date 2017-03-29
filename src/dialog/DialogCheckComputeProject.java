package dialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * 选择统计项目 2017/3/18 09:49
 */
public class DialogCheckComputeProject extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<Object> _lstProjects; // 展示列表 2017/3/18 09:49

    private String _mChooseText; // 选择文本 2017/3/18 09:37
    private SelectCallBack _selectCallBack; // 回调函数 2017/3/18 09:42

    public DialogCheckComputeProject() {

    }

    /**
     * 构造函数 2017/3/18 08:49
     * @param lstProjects 所有打开的项目
     */
    public DialogCheckComputeProject(List<String> lstProjects, SelectCallBack selectCallBack) {
        _selectCallBack = selectCallBack;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("选择统计项目"); // 设置title 2017/3/18 09:50
        setSize(500, 300); // 设置窗口大小 2017/3/18 09:50

        // 设置窗口位置 2017/3/18 09:50
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int Swing1x = 500;
        int Swing1y = 300;
        setBounds((screensize.width - Swing1x) / 2, (screensize.height - Swing1y) / 2 - 100, Swing1x, Swing1y);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

        // 设置列表数据，并默认选择第一项 2017/3/18 09:50
        _lstProjects.setListData(lstProjects.toArray());
        _lstProjects.setSelectedIndex(0);
        _mChooseText = lstProjects.get(0);

        /**
         * 添加item点击事件 2017/3/18 09:38
         */
        _lstProjects.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                _mChooseText = _lstProjects.getSelectedValue() + "";
            }
        });


        /**
         * 增加键盘处理事件 2017/3/18 09:35
         */
        _lstProjects.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    onCancel();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onOK();
                }
            }
        });

        /**
         * 增加鼠标点击处理事件 2017/3/18 09:36
         */
        _lstProjects.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    onOK();
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        this._selectCallBack.select(this._mChooseText);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DialogCheckComputeProject dialog = new DialogCheckComputeProject();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
