package tools;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Tool windows control.控制文本显示 2017/3/18 19:58
 */
public class ToolFactoryCompute implements ToolWindowFactory {

    private ToolWindow myToolWindow;
    private JPanel mPanel;
    private JTextArea txtContent;
    private JScrollPane mScrollPane;
    private JTextArea textArea1;

    /**
     * 创建控件内容 2017/3/24 09:02
     * @param project 项目
     * @param toolWindow 窗口
     */
    @Override
    public void createToolWindowContent(@NotNull Project project,
                                        @NotNull ToolWindow toolWindow) {
        myToolWindow = toolWindow;

        // 将显示面板添加到显示区 2017/3/18 19:57
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mPanel, "Control", false);
        toolWindow.getContentManager().addContent(content);

        // 禁止编辑 2017/3/18 19:57
        txtContent.setEditable(false);

        // 去除边框 2017/3/19 08:58
        txtContent.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
        mScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
        mPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));

        // 设置透明 2017/3/18 21:41
        mPanel.setOpaque(false);
        mScrollPane.setOpaque(false);
        mScrollPane.getViewport().setOpaque(false);
        txtContent.setOpaque(false);

        txtContent.removeMouseListener(mouseListener);
        txtContent.addMouseListener(mouseListener);

        // 鼠标事件 2017/3/18 19:57
        txtContent.removeMouseListener(mouseAdapter);
        txtContent.addMouseListener(mouseAdapter);

        // 输入变化事件 2017/3/18 19:58
        txtContent.getCaret().removeChangeListener(changeListener);
        txtContent.getCaret().addChangeListener(changeListener);
    }

    @Override
    public void init(ToolWindow window) {

    }

    /**
     * 鼠标进出/入事件 2017/4/4 18:14
     */
    private MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mouseEntered(MouseEvent mouseEvent) {
            txtContent.setCursor(new Cursor(Cursor.TEXT_CURSOR));   //鼠标进入Text区后变为文本输入指针
        }

        public void mouseExited(MouseEvent mouseEvent) {
            txtContent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));   //鼠标离开Text区后恢复默认形态
        }
    };

    /**
     * 鼠标改变事件 2017/4/4 18:13
     */
    private ChangeListener changeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            txtContent.getCaret().setVisible(true);   //使Text区的文本光标显示
        }
    };

    /**
     * 鼠标右键事件 2017/4/4 18:12
     */
    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 3) { // 鼠标右键 2017/3/18 21:12

                // 添加右键菜单的内容 2017/3/18 21:12
                JBList<String> list = new JBList<>();
                String[] title = new String[2];
                title[0] = "    Select All";
                title[1] = "    Clear All";
                list.setListData(title); // 设置数据 2017/3/18 21:13
                list.setFocusable(false);

                // 设置边框 2017/3/18 21:37
                Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
                list.setBorder(lineBorder);

                // 创建菜单 2017/3/18 21:13
                JBPopup popup = new PopupChooserBuilder(list)
                        .setItemChoosenCallback(new Runnable() { // 添加点击项的监听事件 2017/3/18 21:13
                            @Override
                            public void run() {
                                String value = list.getSelectedValue();
                                if ("    Clear All".equals(value)) {
                                    txtContent.setText("");
                                } else if ("    Select All".equals(value)) {
                                    txtContent.selectAll();
                                }
                            }
                        }).createPopup();

                // 设置大小 2017/3/18 21:13
                Dimension dimension = popup.getContent().getPreferredSize();
                popup.setSize(new Dimension(150, dimension.height));

                // 显示 2017/3/18 21:25
                popup.show(new RelativePoint(e)); // 传入e，获取位置进行显示 2017/3/19 09:48
                list.clearSelection();

                // 添加鼠标进入List事件 2017/3/18 21:25
                list.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        list.clearSelection();
                    }
                });
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };

}
