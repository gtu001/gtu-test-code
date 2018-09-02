package gtu.image.game;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import javax.swing.ImageIcon;

/** */
/**
 *
 * <p>
 * Title: LoonFramework
 * </p>
 * <p>
 * Description:动画演示
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: LoonFramework
 * </p>
 * <p>
 * License: [url]http://www.apache.org/licenses/LICENSE-2.0[/url]
 * </p>
 *
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class AnimationSimple {
    // 动作时间
    private static final long TIME = 20000;
    final static private int WIDTH = 800;
    final static private int HEIGHT = 600;
    private ScreenManager _screen;
    private Image _bgImage;
    private Animation _animation1;
    private Animation _animation2;
    private Image _cacheImage;
    private Graphics _graphics;

    public static void main(String args[]) {
        // 创建一个显示模式及设定参数,分别为：宽、高、比特位数、刷新率(赫兹)PS:frame中图像会自动放大
        DisplayMode displayMode = new DisplayMode(WIDTH, HEIGHT, 16, 0);
        AnimationSimple test = new AnimationSimple();
        test.run(displayMode);
        // System.out.println((0 << 16) | (0 << 8) | 0);
    }

    public void loadImages() {
        // 为保持图片同步加载，构建一个cache作为二级缓存
        _cacheImage = new BufferedImage(WIDTH, HEIGHT, 2);
        _graphics = _cacheImage.getGraphics();
        // 加载图片
        _bgImage = loadImage("test_images/bg01.png", false);
        Image[] players1 = new Image[9];
        for (int i = 0; i < 9; i++) {
            players1[i] = loadImage("test_images/marisa_0" + i + ".png", true);
        }
        Image[] players2 = new Image[7];
        for (int i = 1; i < 8; i++) {
            players2[i - 1] = loadImage("test_images/reimu2_0" + i + ".png", true);
        }
        // 创建动画
        _animation1 = new Animation();
        _animation1.addFrame(players1);
        _animation2 = new Animation();
        _animation2.addFrame(players2);
    }

    /** */
    /**
     * 加载图象
     *
     * @param fileName
     * @param isfiltration
     * @return
     */
    private Image loadImage(String fileName, boolean isfiltration) {
        // 当ImageIcon使用jar包本身资源时，需要通过jvm获得路径
        ClassLoader classloader = getClass().getClassLoader();
        Image img = new ImageIcon(classloader.getResource(fileName)).getImage();
        // 为了演示例子没有使用偶的loonframework-game包(那天整合下准备发alpha了)，而是直接处理了图像
        if (isfiltration) {
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            // 创建一个PixelGrabber
            PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, true);
            try {
                pg.grabPixels();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 获取其中像素
            int pixels[] = (int[]) pg.getPixels();
            // 遍历过滤像素
            for (int i = 0; i < pixels.length; i++) {
                // -16777216为0,0,0即纯黑
                if (pixels[i] == -16777216) {
                    // 转为透明色
                    // 16777215也就是255,255,255即纯白 处理为 (255 << 16) | (255 << 8) |
                    // 255 后可得此结果
                    pixels[i] = 16777215;
                }
            }
            // 在内存中生成图像后映射到image
            img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pixels, 0, width));
        }
        return img;
    }

    public void run(DisplayMode displayMode) {
        _screen = new ScreenManager();
        try {
            _screen.setFullScreen(displayMode);
            // 初始化图像加载
            loadImages();
            // 动画循环播放
            animationLoop();
        } finally {
            _screen.restoreScreen();
        }
    }

    public void animationLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;
        // 每次比较工作时间，无效时将退出运作
        while (currTime - startTime < TIME) {
            long elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;
            // 改变动画1
            _animation1.update(elapsedTime);
            // 改变动画2
            _animation2.update(elapsedTime);
            // 绘制窗体
            Graphics2D g = _screen.getGraphics();
            draw(g);
            g.dispose();
            // 更新显示内容
            _screen.update();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void draw(Graphics g) {
        // 绘制背景0，0座标
        _graphics.drawImage(_bgImage, 0, 0, null);
        // 绘制动画于0，0座标
        _graphics.drawImage(_animation2.getImage(), 0, 0, null);
        // 绘制动画于400，0座标
        _graphics.drawImage(_animation1.getImage(), 400, 0, null);
        // 将缓存图绘制于窗体0，0座标之上
        g.drawImage(_cacheImage, 0, 0, null);
    }
}