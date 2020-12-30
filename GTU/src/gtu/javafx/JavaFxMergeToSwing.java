package gtu.javafx;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gtu.javafx.mp4player.MediaControl;
import gtu.swing.util.JCommonUtil;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class JavaFxMergeToSwing {

    public static void main(String[] args) {
        // 範例一
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JavaFxMergeToSwing.initAndShowGUI();
            }
        });
    }

    private void ______test_moviePlayer() {
        final AtomicReference<Scene> scene1 = new AtomicReference<Scene>();
        JFrame jframe = new JFrame("測試元件");
        jframe.setSize(500, 300);// 寬 高
        jframe.setLocationRelativeTo(null);
        jframe.setLayout(new BorderLayout());
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        final JPanel panel = new JPanel();
        final JPanel panel2 = new JPanel();
        jframe.setLayout(new BorderLayout());
        jframe.add(panel, BorderLayout.CENTER);
        jframe.add(panel2, BorderLayout.SOUTH);

        JButton openMovieBtn = new JButton("開啟檔案");
        openMovieBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                File movie = JCommonUtil._jFileChooser_selectFileOnly();
                Media pick = new Media(movie.toURI().toString());
                MediaPlayer player = new MediaPlayer(pick);
                MediaControl mMediaControl = new MediaControl(player);
                scene1.get().setRoot(mMediaControl);
            }
        });
        panel2.add(openMovieBtn);

        JFXPanelToSwing mJFXPanelToSwing = new JFXPanelToSwing() {
            @Override
            public Scene createScene(JFXPanel fxPanel) {
                Scene scene = new Scene(new Pane(), javafx.scene.paint.Color.ALICEBLUE);
                scene1.set(scene);
                return scene;
            }

            @Override
            public void appendToJFrame(Container frame, JFXPanel fxPanel) {
                jframe.add(fxPanel, BorderLayout.CENTER);
            }
        };
        mJFXPanelToSwing.execute(jframe);
    }

    public static abstract class JFXPanelToSwing {
        Container container;
        JFXPanel fxPanel;
        Scene scene;

        protected abstract Scene createScene(JFXPanel fxPanel);

        protected abstract void appendToJFrame(Container container, JFXPanel fxPanel);

        public void execute(Container jframe) {
            try {
                this.container = jframe;
                if (this.fxPanel == null) {
                    this.fxPanel = new JFXPanel();
                }
                this.appendToJFrame(jframe, fxPanel);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        scene = JFXPanelToSwing.this.createScene(fxPanel);
                        fxPanel.setScene(scene);
                    }
                });
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        public javafx.scene.Parent getRoot() {
            return scene.getRoot();
        }

        public void setRoot(javafx.scene.Parent root) {
            scene.setRoot(root);
        }

        public Scene getScene() {
            return scene;
        }

        public Container getContainer() {
            return container;
        }

        public JFXPanel getFxPanel() {
            return fxPanel;
        }
    }

    private static void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("Swing and JavaFX");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(300, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private static Scene createScene() {
        Group root = new Group();
        Scene scene = new Scene(root, Color.ALICEBLUE);
        Text text = new Text();

        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("Welcome JavaFX!");

        root.getChildren().add(text);

        return (scene);
    }
}
