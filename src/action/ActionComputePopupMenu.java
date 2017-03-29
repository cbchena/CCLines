package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import compute.ComputeJava;
import compute.ComputeType;
import compute.SuffixMapping;
import compute.TypeEntity;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 右键菜单选中当前项目的某个目录或者文件进行统计 2017/3/24 15:14
 */
public class ActionComputePopupMenu extends AnAction {

    private JTextArea jTextArea;
    private List<String> _lstType = new ArrayList<>(); // 记录允许统计的类型 2017/3/21 14:42

    private long _allFilesCount = 0; // 所有文件 2017/3/21 16:29
    private long _allLines = 0; // 总行数
    private long _allWriteLines = 0; // 总实际代码行数
    private long _allCommentLines = 0; // 总注释行数
    private long _allNormalLines = 0; // 总空行数

    @Override
    public void actionPerformed(AnActionEvent e) {

        // 找出统计文件 2017/3/18 19:59
        ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow("Compute Code Lines");
        if (toolWindow != null) {

            // 无论当前状态为关闭/打开，进行强制打开ToolWindow 2017/3/21 16:21
            toolWindow.show(new Runnable() {
                @Override
                public void run() {

                }
            });

            jTextArea = (JTextArea) ((JScrollPane)toolWindow.getContentManager().getContent(0)
                    .getComponent().getComponent(0)).getViewport().getComponent(0);
        }

        // 获取当前选择的文件或文件夹路径 2017/3/24 15:48
        VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        if(file == null) {
            if (jTextArea != null)
                jTextArea.append("找不到统计文件.\n");

            return;
        }

        String path = file.getPath();

        // 初始化统计格式 2017/3/20 10:57
        SuffixMapping.initMapping();

        // 找出允许统计的类型 2017/3/21 14:42
        _lstType.clear();
        for (TypeEntity entity: ComputeType.getTypes()) {
            if (entity.isCheck()) { // 是否选择统计 2017/3/21 14:43
                _lstType.add(entity.getSuffix().toUpperCase());
            }
        }

        if (toolWindow != null) {
            if (jTextArea != null) {
                jTextArea.append("开始统计，项目路径为：\"" + path + "\".\n");
                _getFiles(path);
            }
        }
    }

    /**
     * 获取文件 2017/3/18 19:47
     * @param path 获取路径
     */
    private void _getFiles(String path) {
        try {
            _readFile(path);
            jTextArea.append("结束统计.\n");
            jTextArea.append("\n\n=============  结果  =============\n");
            jTextArea.append("总统计文件数：" + _allFilesCount + ".\n");
            jTextArea.append("总实际代码行数：" + _allWriteLines + ".\n");
            jTextArea.append("总注释行数：" + _allCommentLines + ".\n");
            jTextArea.append("总空行数：" + _allNormalLines + ".\n");
            jTextArea.append("总行数：" + _allLines + ".\n");
            jTextArea.append("====================================\n");

            // 重置 2017/3/21 16:31
            _allFilesCount = 0; // 所有文件 2017/3/21 16:29
            _allLines = 0; // 总行数
            _allWriteLines = 0; // 总实际代码行数
            _allCommentLines = 0; // 总注释行数
            _allNormalLines = 0; // 总空行数
        } catch (Exception e) {
            System.out.println("_getFiles:   " + e.getMessage());
        }
    }

    /**
     * 读取某个文件夹下的所有文件 2017/3/18 19:54
     * @param filepath 文件路径
     */
    private boolean _readFile(String filepath) throws Exception {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                String endsWith = filepath.substring(filepath.lastIndexOf(".") + 1); // 后缀名 2017/3/19 10:18
                _compute(file, endsWith);
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "/" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        String endsWith = filelist[i].substring(filelist[i].lastIndexOf(".") + 1); // 后缀名 2017/3/19 10:18
                        _compute(readfile, endsWith);
                    } else if (readfile.isDirectory()) {
                        _readFile(filepath + "/" + filelist[i]);
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }

        return true;
    }

    /**
     * 计算 2017/3/19 07:47
     * @param file 文件
     * @param suffix 后缀名
     * @throws Exception 异常
     */
    private void _compute(File file, String suffix) throws Exception{
        if (_lstType.contains(suffix.toUpperCase())) { // 有包含才允许统计 2017/3/20 11:00
            this._allFilesCount++; // 总文件数
            jTextArea.append("\n" + file.getPath() + ":\n");
            List<Long> lstData = ComputeJava.compute(file, suffix); // 代码、注释、空行
            long writeLines = lstData.get(0);
            long commentLines = lstData.get(1);
            long normalLines = lstData.get(2);

            this._allLines += (writeLines + commentLines + normalLines); // 总行数 2017/3/21 16:32
            this._allWriteLines += writeLines; // 总实际代码数
            this._allCommentLines += commentLines; // 总注释数
            this._allNormalLines += normalLines; // 总空行数

            jTextArea.append("实际代码行数：" + writeLines + ".\n");
            jTextArea.append("注释行数：" + commentLines + ".\n");
            jTextArea.append("空行数：" + normalLines + ".\n");
            jTextArea.append("该文件总行数：" + (writeLines + commentLines + normalLines) + ".\n");
        }
    }
}
