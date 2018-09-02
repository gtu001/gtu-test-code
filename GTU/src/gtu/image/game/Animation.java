package gtu.image.game;

import java.awt.Image;
import java.util.ArrayList;

public class Animation {
    // 缓存动画面板
    private ArrayList _frames;
    private int _frameIndex;
    private long _time;
    private long _total;

    private class AnimationFrame {
        Image image;
        long endTime;

        public AnimationFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }

    public Animation() {
        _frames = new ArrayList();
        _total = 0;
        start();
    }

    /** */
    /**
     * 增加一个指定的动画图片并设置动画时间
     *
     * @param image
     * @param duration
     */
    public synchronized void addFrame(Image image, long duration) {
        _total += duration;
        _frames.add(new AnimationFrame(image, _total));
    }

    /** */
    /**
     * 以默认播放时间载入图像数组
     *
     * @param image
     */
    public void addFrame(Image[] image) {
        for (int i = 0; i < image.length; i++) {
            addFrame(image[i], 500);
        }
    }

    /** */
    /**
     * 开始执行动画
     *
     */
    public synchronized void start() {
        _time = 0;
        _frameIndex = 0;
    }

    /** */
    /**
     * 更新此动画播放时间
     *
     * @param time
     */
    public synchronized void update(long time) {
        if (_frames.size() > 1) {
            _time += time;
            if (_time >= _total) {
                _time = _time % _total;
                _frameIndex = 0;
            }
            while (_time > getFrame(_frameIndex).endTime) {
                _frameIndex++;
            }
        }
    }

    /** */
    /**
     * 获得当前动画image
     *
     * @return
     */
    public synchronized Image getImage() {
        if (_frames.size() == 0) {
            return null;
        } else {
            return getFrame(_frameIndex).image;
        }
    }

    /** */
    /**
     * 返回指定frame
     *
     * @param i
     * @return
     */
    private AnimationFrame getFrame(int i) {
        return (AnimationFrame) _frames.get(i);
    }
}