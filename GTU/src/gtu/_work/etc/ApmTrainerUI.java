package gtu._work.etc;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import gtu._work.ui.SwingTemplateUI;
import gtu.number.RandomUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ApmTrainerUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel jPanel1;
    Graphics2D g2d;
    Graphics2D g2dGreen;
    RoundRectangle2D rectagle = new RoundRectangle2D.Double(30, 50, 50, 50, 8, 8);
    MouseEvent mousePressEvent;
    boolean initDraw;
    boolean clickMode;

    ApmCounter backgroundApmCounter;

    int circleSize = 10;

    static int APM_SECOND = 60;

    static class ApmCounter {
        int correctTime;
        int incorrectTime;
        Thread thread;
        boolean executeThread = true;
    }

    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(SwingTemplateUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ApmTrainerUI inst = new ApmTrainerUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, inst);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void randomRectagle() {
        g2d.setColor(Color.WHITE);
        g2d.draw(rectagle);

        System.out.println("panel = " + jPanel1.getHeight() + "/" + jPanel1.getWidth());
        int w = jPanel1.getWidth() / circleSize;
        int h = jPanel1.getHeight() / circleSize;

        int x = RandomUtil.rangeInteger(5, jPanel1.getWidth() - w);
        int y = RandomUtil.rangeInteger(25, jPanel1.getHeight() - h);

        rectagle.setRoundRect(x, y, w, h, 8, 8);

        g2d.setColor(Color.BLACK);
        g2d.draw(rectagle);
    }

    void recoverRectagle() {
        if (rectagle == null) {
            return;
        }
        new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                g2d.setColor(Color.BLACK);
                g2d.draw(rectagle);
            }
        }).start();
    }

    boolean checkMouseEvent(MouseEvent m1, MouseEvent m2) {
        int minX = Math.min(m1.getX() + 5, m2.getX() + 5);
        int minY = Math.min(m1.getY() + 25, m2.getY() + 25);

        int maxX = Math.max(m1.getX() + 5, m2.getX() + 5);
        int maxY = Math.max(m1.getY() + 25, m2.getY() + 25);

        int rectagleX = (int) rectagle.getX();
        int rectagleY = (int) rectagle.getY();
        int rectagleW = (int) rectagle.getWidth();
        int rectagleH = (int) rectagle.getHeight();

        if (!initDraw) {
            initDraw = true;
            return true;
        }

        if (minX < rectagleX && minY < rectagleY//
                && (rectagleX + rectagleW) < maxX && (rectagleY + rectagleH) < maxY) {
            return true;
        }
        return false;
    }

    boolean checkMouseEvent_forClickMode(MouseEvent m1) {
        int x = m1.getX();
        int y = m1.getY();

        int rectagleX = (int) rectagle.getX();
        int rectagleY = (int) rectagle.getY();
        int rectagleW = (int) rectagle.getWidth();
        int rectagleH = (int) rectagle.getHeight();

        int left = rectagleX - 5;
        int right = rectagleX + rectagleW + 5;
        int top = rectagleY - 25 - 5;
        int buttom = rectagleY + rectagleH - 25 + 5;

        System.out.println("左" + left + "右" + right + "上" + top + "下" + buttom);
        System.out.println("點" + x + "/" + y);

        if (!initDraw) {
            initDraw = true;
            return true;
        }

        if (left < x && right > x && top < y && buttom > y) {
            return true;
        }
        return false;
    }

    void drawReleaseRectagle(MouseEvent m1, MouseEvent m2) {
        final int x = Math.min(m1.getX() + 5, m2.getX() + 5);
        final int y = Math.min(m1.getY() + 25, m2.getY() + 25);
        final int w = Math.max(m1.getX() + 5, m2.getX() + 5) - x;
        final int h = Math.max(m1.getY() + 25, m2.getY() + 25) - y;
        final RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, w, h, 0, 0);
        g2dGreen.draw(rect);
        new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x, y, w, h);
                g2d.draw(rect);
            }
        }).start();
    }

    public ApmTrainerUI() {
        super();
        initGUI();
    }

    private class AlphaContainer extends JComponent {
        private JComponent component;

        public AlphaContainer(JComponent component) {
            this.component = component;
            this.component.setBackground(new Color(0, 0, 0, 0));// 0,0,0,0 全透明

            setLayout(new BorderLayout());
            setOpaque(false);
            component.setOpaque(false);
            add(component);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(component.getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jPanel1 = new JPanel();
                getContentPane().add(new AlphaContainer(jPanel1), BorderLayout.CENTER);
                jPanel1.setBackground(new java.awt.Color(255, 255, 255));

                jPanel1.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (JMouseEventUtil.buttonRightClick(1, e)) {
                            JPopupMenuUtil.newInstance(jPanel1).applyEvent(e)//
                                    .addJMenuItem("重新計算APM", new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            backgroundApmCounterInit();
                                            initDraw = false;
                                        }
                                    })//
                                    .addJMenuItem("調整黑框Size", new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String value = JCommonUtil._jOptionPane_showInputDialog("請輸入黑框的佔螢幕的比例,數字越小黑框越大 : (若1/10則輸入10)", "10");
                                            try {
                                                circleSize = Integer.parseInt(value);
                                            } catch (Exception ex) {
                                                JCommonUtil._jOptionPane_showMessageDialog_error("請輸入2~40之間的建議值");
                                            }
                                        }
                                    })//
                                    .addJMenuItem("點擊模式", new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            clickMode = !clickMode;
                                            JCommonUtil._jOptionPane_showMessageDialog_info("點擊模式 : " + (clickMode ? "on" : "off"));
                                        }
                                    })//
                                    .show();
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        System.out.println(e.getX() + "/" + e.getY());
                        mousePressEvent = e;

                        if (clickMode) {
                            if (checkMouseEvent_forClickMode(e)) {
                                randomRectagle();
                                backgroundApmCounter.correctTime++;
                            } else {
                                backgroundApmCounter.incorrectTime++;
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        System.out.println(e.getX() + "/" + e.getY());

                        drawReleaseRectagle(e, mousePressEvent);

                        if (clickMode) {
                            return;
                        }

                        if (checkMouseEvent(e, mousePressEvent)) {
                            randomRectagle();
                            backgroundApmCounter.correctTime++;
                        } else {
                            recoverRectagle();
                            backgroundApmCounter.incorrectTime++;
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });

                jPanel1.addMouseMotionListener(new MouseMotionListener() {
                    List<MouseEvent> allMoveList = new ArrayList<MouseEvent>();

                    void drawReleaseRectagle_forDrag(MouseEvent m1, MouseEvent m2) {
                        int x = Math.min(m1.getX() + 5, m2.getX() + 5);
                        int y = Math.min(m1.getY() + 25, m2.getY() + 25);
                        int w = Math.max(m1.getX() + 5, m2.getX() + 5) - x;
                        int h = Math.max(m1.getY() + 25, m2.getY() + 25) - y;
                        final RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, w, h, 0, 0);
                        g2d.setColor(Color.WHITE);
                        g2d.draw(rect);
                    }

                    void drawReleaseRectagle_forNew(MouseEvent m1, MouseEvent m2) {
                        int x = Math.min(m1.getX() + 5, m2.getX() + 5);
                        int y = Math.min(m1.getY() + 25, m2.getY() + 25);
                        int w = Math.max(m1.getX() + 5, m2.getX() + 5) - x;
                        int h = Math.max(m1.getY() + 25, m2.getY() + 25) - y;
                        final RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, w, h, 0, 0);
                        g2dGreen.draw(rect);
                        g2dGreen.fillRect(x, y, w, h);
                    }

                    void repaintBlackRectagle() {
                        g2d.setColor(Color.BLACK);
                        g2d.draw(rectagle);
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        // 是否要執行拖曳模式
                        if (false) {
                            return;
                        }

                        if (g2dGreen == null || g2d == null) {
                            return;
                        }

                        for (int ii = 0; ii < allMoveList.size(); ii++) {
                            MouseEvent e2 = allMoveList.get(ii);
                            drawReleaseRectagle_forDrag(e2, mousePressEvent);
                            allMoveList.remove(ii);
                            ii--;
                        }

                        allMoveList.add(e);
                        drawReleaseRectagle_forNew(e, mousePressEvent);

                        repaintBlackRectagle();
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        if (g2d == null) {
                            g2d = (Graphics2D) getGraphics();
                            g2d.setStroke(new BasicStroke(4.0f));
                        }
                        if (g2dGreen == null) {
                            g2dGreen = (Graphics2D) getGraphics();
                            g2dGreen.setStroke(new BasicStroke(1.0f));
                            g2dGreen.setColor(Color.GREEN);
                        }
                    }
                });
            }

            this.addComponentListener(new ComponentListener() {

                @Override
                public void componentResized(ComponentEvent e) {
                    initDraw = false;
                }

                @Override
                public void componentMoved(ComponentEvent e) {
                }

                @Override
                public void componentShown(ComponentEvent e) {
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                }
            });

            pack();
            setSize(400, 300);

            backgroundApmCounterInit();

            setLocationRelativeTo(null);

            JCommonUtil.setJFrameIcon(this, "resource/images/ico/starcraft.ico");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void backgroundApmCounterInit() {
        if (backgroundApmCounter != null) {
            // backgroundApmCounter.thread.interrupt();
            backgroundApmCounter.executeThread = false;
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
            }
        }
        backgroundApmCounter = new ApmCounter();
        backgroundApmCounter.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                while (backgroundApmCounter.executeThread) {
                    try {
                        Thread.sleep(500);
                        float betweenSec = (float) (System.currentTimeMillis() - currentTime) / 1000;

                        float total = ((float) (backgroundApmCounter.correctTime + backgroundApmCounter.incorrectTime)) / betweenSec * 60;
                        float correct = ((float) (backgroundApmCounter.correctTime)) / betweenSec * 60;
                        float incorrect = ((float) (backgroundApmCounter.incorrectTime)) / betweenSec * 60;

                        setTitle("每分鐘圈數:" + (int) total + "有效圈數:" + (int) correct + "無效圈數:" + (int) incorrect + " id:" + backgroundApmCounter.thread.getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        backgroundApmCounter.thread.start();
    }
}
