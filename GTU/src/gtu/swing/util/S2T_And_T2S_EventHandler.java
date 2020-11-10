package gtu.swing.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

import gtu.binary.Base64JdkUtil;
import taobe.tec.jcc.JChineseConvertor;

public class S2T_And_T2S_EventHandler {

    JTextComponent input;

    public S2T_And_T2S_EventHandler(JTextComponent input) {
        this.input = input;
    }

    final Transformer trans = new Transformer() {
        public Object transform(final Object _input) {
            String text = input.getText();
            try {
                boolean s2t = (Boolean) _input;
                if (StringUtils.isNotBlank(input.getSelectedText())) {
                    String before = StringUtils.substring(text, 0, input.getSelectionStart());
                    String middle = input.getSelectedText();
                    if (s2t) {
                        middle = JChineseConvertor.getInstance().s2t(middle);
                    } else {
                        middle = JChineseConvertor.getInstance().t2s(middle);
                    }
                    String after = StringUtils.substring(text, input.getSelectionEnd());
                    return before + middle + after;
                } else {
                    if (s2t) {
                        text = JChineseConvertor.getInstance().s2t(text);
                    } else {
                        text = JChineseConvertor.getInstance().t2s(text);
                    }
                    return text;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return text;
        }
    };

    final Transformer trans2 = new Transformer() {
        public Object transform(final Object _input) {
            String text = input.getText();
            try {
                boolean isEncode = (Boolean) _input;
                if (StringUtils.isNotBlank(input.getSelectedText())) {
                    String before = StringUtils.substring(text, 0, input.getSelectionStart());
                    String middle = input.getSelectedText();
                    if (isEncode) {
                        middle = Base64JdkUtil.encode(middle);
                    } else {
                        middle = Base64JdkUtil.decodeToString(middle);
                    }
                    String after = StringUtils.substring(text, input.getSelectionEnd());
                    return before + middle + after;
                } else {
                    if (isEncode) {
                        text = Base64JdkUtil.encode(text);
                    } else {
                        text = Base64JdkUtil.decodeToString(text);
                    }
                    return text;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return text;
        }
    };

    final Transformer toChangeCase = new Transformer() {
        public Object transform(final Object _input) {
            String text = input.getText();
            boolean isUpperCase = (Boolean) _input;
            try {
                if (isUpperCase) {
                    if (StringUtils.isNotBlank(input.getSelectedText())) {
                        String before = StringUtils.substring(text, 0, input.getSelectionStart());
                        String middle = input.getSelectedText();
                        middle = middle.toUpperCase();
                        String after = StringUtils.substring(text, input.getSelectionEnd());
                        return before + middle + after;
                    } else {
                        return text.toUpperCase();
                    }
                } else {
                    if (StringUtils.isNotBlank(input.getSelectedText())) {
                        String before = StringUtils.substring(text, 0, input.getSelectionStart());
                        String middle = input.getSelectedText();
                        middle = middle.toLowerCase();
                        String after = StringUtils.substring(text, input.getSelectionEnd());
                        return before + middle + after;
                    } else {
                        return text.toLowerCase();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return text;
        }
    };

    public JMenuItem getMenuItem3(final boolean isUpperCase) {
        JMenuItem item = new JMenuItem(isUpperCase ? "轉大寫" : "轉小寫");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.setText((String) toChangeCase.transform(isUpperCase));
            }
        });
        return item;
    }

    public JMenuItem getMenuItem(final boolean isS2t) {
        JMenuItem item = new JMenuItem(isS2t ? "簡轉繁" : "繁轉簡");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.setText((String) trans.transform(isS2t));
            }
        });
        return item;
    }

    public JMenuItem getMenuItem2(final boolean isEncode) {
        JMenuItem item = new JMenuItem(isEncode ? "Encode" : "Decode");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.setText((String) trans2.transform(isEncode));
            }
        });
        return item;
    }

    public MouseAdapter getEvent() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    final JTextField input = (JTextField) e.getSource();
                    if (JMouseEventUtil.buttonRightClick(1, e)) {

                        JPopupMenuUtil.newInstance(input)//
                                .addJMenuItem("繁轉簡", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        input.setText((String) trans.transform(false));
                                    }
                                }).addJMenuItem("簡轉繁", new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        input.setText((String) trans.transform(true));
                                    }
                                }).addJMenuItem("轉大寫", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        input.setText((String) toChangeCase.transform(true));
                                    }
                                }).addJMenuItem("轉小寫", new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        input.setText((String) toChangeCase.transform(false));
                                    }
                                }).addJMenuItem("Encode", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        input.setText((String) trans2.transform(true));
                                    }
                                }).addJMenuItem("Decode", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        input.setText((String) trans2.transform(false));
                                    }
                                }).applyEvent(e).show();
                    }
                } catch (Exception ex1) {
                    JCommonUtil.handleException(ex1);
                }
            }
        };
    }
}