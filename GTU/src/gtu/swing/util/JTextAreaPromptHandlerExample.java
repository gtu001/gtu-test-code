package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.text.JTextComponent;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JTextFieldUtil.JTextComponentSelectPositionHandler;

public class JTextAreaPromptHandlerExample {

    public static void main(String args[]) {
        JCommonUtil.defaultToolTipDelay();

        JTextAreaPromptHandlerExample test = new JTextAreaPromptHandlerExample();
        JTextArea sqlTextArea = new JTextArea();

        List<String> columnLst = new ArrayList<String>();
        for (int ii = (int) 'a'; ii <= (int) 'z'; ii++) {
            String val = String.valueOf((char) ii);
            val += val;
            val += val;
            val += val;
            columnLst.add(val);
        }

        JTextAreaPromptHandler textAreaPromptHandler = new JTextAreaPromptHandler(sqlTextArea) {
            List<String> currentLst2 = new ArrayList<String>();

            @Override
            protected boolean isShowPopupMenu(String commaBefore, String commaAfter) {
                currentLst2.clear();
                for (String str : columnLst) {
                    if (str.startsWith(commaAfter)) {
                        currentLst2.add(str);
                    }
                }
                return !currentLst2.isEmpty();
            }

            @Override
            protected void applyJMenuItems(JPopupMenuUtil util) {
                for (String str : currentLst2) {
                    util.addJMenuItem(str, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            replaceCommafter(str);
                            util.dismiss();
                        }
                    });
                }
            }
        };

        sqlTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean isConsume = false;
                if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER && textAreaPromptHandler != null) {
                    isConsume = textAreaPromptHandler.performSelectTopColumn(e);
                    if (!isConsume) {
                        JTextAreaUtil.triggerTabKey(sqlTextArea, e);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && textAreaPromptHandler != null) {
                    isConsume = textAreaPromptHandler.performSelectClose();
                } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN && textAreaPromptHandler != null) {
                    isConsume = textAreaPromptHandler.performSelectUpDown(e);
                } else if (!isConsume && textAreaPromptHandler != null) {
                    textAreaPromptHandler.performUpdateLocation();
                    isConsume = textAreaPromptHandler.checkPopupListFocus(e);
                }
                if (isConsume) {
                    e.consume();
                    System.out.println("-----Consume");
                }
            }
        });

        sqlTextArea.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                textAreaPromptHandler.init(event);
                textAreaPromptHandler.mainProcess();
            }
        }));

        JTextAreaPromptHandlerExample.simpleTestComponent(sqlTextArea);
    }

    public static JFrame simpleTestComponent(JComponent comp) {
        JFrame jframe = new JFrame("測試元件");
        jframe.setSize(500, 300);// 寬 高
        jframe.setLocationRelativeTo(null);
        jframe.setLayout(new BorderLayout());
        jframe.add(JCommonUtil.createScrollComponent(comp), BorderLayout.CENTER);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        return jframe;
    }

    public static abstract class JTextAreaPromptHandler {
        String queryText = "";
        String tableAlias = "";
        String columnPrefix = "";
        LRUMap tabMap = new LRUMap(20);
        LRUMap failMap = new LRUMap(100);
        Pair<Integer, Integer> columnIndex;
        int queryTextPos = -1;
        JPopupMenuUtil util;
        int currentMenuIndex = 0;

        JTextComponentSelectPositionHandler textAreaJTextAreaSelectPositionHandler;
        JTextComponent sqlTextArea;

        public JTextAreaPromptHandler(JTextComponent sqlTextArea) {
            this.sqlTextArea = sqlTextArea;
            textAreaJTextAreaSelectPositionHandler = JTextComponentSelectPositionHandler.newInst(sqlTextArea);
        }

        public boolean performUpdateLocation() {
            Rectangle rect = textAreaJTextAreaSelectPositionHandler.getRect();
            if (rect == null || util == null) {
                return false;
            }
            if (StringUtils.isBlank(queryText)) {
                util.dismiss();
            }
            util.setLocation(sqlTextArea, (int) rect.getX(), (int) rect.getY());
            return false;
        }

        public boolean performSelectClose() {
            if (util == null) {
                return false;
            }
            if (util.getJPopupMenu().isShowing()) {
                util.dismiss();
                return true;
            }
            return false;
        }

        public boolean performSelectUpDown(KeyEvent e) {
            if (util == null) {
                return false;
            }
            if (util.getJPopupMenu().isShowing() && !util.getJPopupMenu().isFocusOwner()) {
                JCommonUtil.focusComponent(util.getJPopupMenu(), false, null);
                util.getJPopupMenu().dispatchEvent(e);
                return true;
            }
            return false;
        }

        public boolean performSelectTopColumn(KeyEvent e2) {
            if (util == null) {
                return false;
            }
            if (util.getJPopupMenu().isShowing() && !util.getJPopupMenu().isFocusOwner() && !util.getMenuList().isEmpty()) {
                JCommonUtil.focusComponent(util.getJPopupMenu(), false, null);
                util.getJPopupMenu().dispatchEvent(e2);// 原生的event才會正確
                return true;
            }
            return false;
        }

        public void init(DocumentEvent event) {
            String tmpSql = StringUtils.substring(sqlTextArea.getText(), 0, event.getOffset() + event.getLength());
            Pattern ptn = Pattern.compile("[\\s\n]", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(tmpSql);
            queryTextPos = -1;
            while (mth.find()) {
                queryTextPos = mth.end();
            }
            queryText = StringUtils.substring(tmpSql, queryTextPos);
            currentMenuIndex = 0;
            System.out.println("prompt - [" + queryText + "]");
        }

        public void mainProcess() {
            if (queryText.contains(".")) {
                delimitDBTable();
            } else {
                return;
            }
            if (this.isShowPopupMenu(tableAlias, columnPrefix)) {
                showPopup();
            }
        }

        protected abstract boolean isShowPopupMenu(String commaBefore, String commaAfter);

        protected abstract void applyJMenuItems(JPopupMenuUtil util);

        private void showPopup() {
            Rectangle rect = textAreaJTextAreaSelectPositionHandler.getRect();
            util = JPopupMenuUtil.newInstance(sqlTextArea, true);
            util.applyEvent(rect);
            // util.getJPopupMenu().setFocusable(false);
            this.applyJMenuItems(util);
            util.getJPopupMenu().addMenuKeyListener(new MenuKeyListener() {
                @Override
                public void menuKeyTyped(MenuKeyEvent arg0) {
                }

                @Override
                public void menuKeyReleased(MenuKeyEvent arg0) {
                }

                @Override
                public void menuKeyPressed(MenuKeyEvent arg0) {
                    if (arg0.getKeyCode() == 38 || arg0.getKeyCode() == 40) {// 上下
                    } else if (arg0.getKeyCode() == KeyEvent.VK_ENTER || arg0.getKeyCode() == KeyEvent.VK_TAB) {
                        JMenuItem item = null;
                        if ((item = JPopupMenuUtil.getCurrentSelectItem()) != null) {
                            JCommonUtil.triggerButtonActionPerformed(item);
                        } else {
                            JPopupMenuUtil.setCurrentSelectItem(util.getJPopupMenu(), 0, null);
                            item = JPopupMenuUtil.getCurrentSelectItem();
                            JCommonUtil.triggerButtonActionPerformed(item);
                        }
                    }
                }
            });
            util.show();
            sqlTextArea.requestFocus();
        }

        public void replaceCommafter(String choiceItemString) {
            if (columnIndex == null) {
                return;
            }
            String textOrign = StringUtils.defaultString(sqlTextArea.getText());
            String text = StringUtils.substring(textOrign, 0, columnIndex.getLeft()) + choiceItemString;
            int afterPos = text.length();
            text = text + StringUtils.substring(textOrign, columnIndex.getRight());

            JTextFieldUtil.setTextIgnoreDocumentListener(sqlTextArea, text);

            sqlTextArea.updateUI();

            sqlTextArea.setSelectionStart(afterPos);
            sqlTextArea.setSelectionEnd(afterPos);
        }

        private void delimitDBTable() {
            Pattern ptn2 = Pattern.compile("(.*)\\.(.*)");
            Matcher mth2 = ptn2.matcher(queryText);
            if (mth2.find()) {
                tableAlias = mth2.group(1);
                columnPrefix = mth2.group(2);
                columnIndex = Pair.of(queryTextPos + mth2.start(2), queryTextPos + mth2.end(2));
            } else {
                tableAlias = queryText.replaceAll("\\.+$", "");
                columnIndex = null;
            }
        }

        public boolean checkPopupListFocus(KeyEvent arg0) {
            if (arg0.getKeyCode() == 38 || arg0.getKeyCode() == 40) {// 上下
                if (util.getJPopupMenu().isShowing()) {
                    util.getJPopupMenu().dispatchEvent(arg0);
                }
                sqlTextArea.requestFocus();
                return true;
            } else if (!sqlTextArea.isFocusOwner()) {
                if (util.getJPopupMenu().isShowing()) {
                    util.getJPopupMenu().dispatchEvent(arg0);
                }
                sqlTextArea.requestFocus();
            }
            return false;
        }
    }
}
