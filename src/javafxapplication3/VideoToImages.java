package javafxapplication3;

import com.sun.javaws.Main;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

/**
 *
 * @author Karol
 */
public class VideoToImages {

    static double dur;
    static double klatka = 0.05;
    static int licz = 0;
    public int procent;
    static double rate = 1.0;
    static int liczba = 0;
    public static String saveDir;
    static ImagesToVideo vidSaver;

    public static ImagesToVideo getVidSaver() {
        return vidSaver;
    }

    public static void start(Stage primaryStage, String source, String output, ProgressBar pb) {
        liczba = 0;
        saveDir = output;
        final File f = new File(source);
        final Media m = new Media(f.toURI().toString());
        final MediaPlayer mp = new MediaPlayer(m);
        final MediaView mv = new MediaView(mp);

        mv.setPreserveRatio(true);
        StackPane root = new StackPane();
        root.getChildren().add(mv);

        final Scene scene = new Scene(root, 960, 540);
        scene.setFill(Color.BLACK);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Video");
        primaryStage.show();

        mp.setMute(true);
        mp.setRate(rate);

        mp.play();
        File fd = new File(saveDir + "\\xd");
        fd.mkdirs();

        mp.setOnReady(() -> {
            licz = 0;
            liczba = 0;
            ObservableMap<String, Duration> os = m.getMarkers();
            dur = mp.getTotalDuration().toSeconds();
            for (double i = 0; i < dur; i += klatka) {
                os.put("" + i, Duration.seconds(i));
            }
        });
        mp.setOnMarker(e -> {

            String name = e.getMarker().getKey();
            WritableImage wi = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
            WritableImage snapshot = primaryStage.getScene().snapshot(wi);
            BufferedImage tempImg = null;
            tempImg = SwingFXUtils.fromFXImage(snapshot, tempImg);

            File fi = new File(fd.getAbsolutePath() + "\\" + liczba++ + ".jpg");

            try {
                ImageIO.write(tempImg, "jpg", fi);
                licz++;
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        mp.play();
        mp.setOnEndOfMedia(() -> {

            primaryStage.hide();
            Stage st = new Stage();
            AsciiSettings as = null;
            try {
                as = new AsciiSettings(st, saveDir + "\\xd");
            } catch (IOException ex) {
                Logger.getLogger(VideoToImages.class.getName()).log(Level.SEVERE, null, ex);
            }

            Task ta = process((int) as.getSb(), as.getCkolor());
            Thread tp = new Thread(ta);

            pb.progressProperty().bind(ta.progressProperty());
            tp.start();
        });
    }

    public static Task process(int fsize, boolean color) {

        return new Task() {
            @Override
            protected Object call() throws Exception {

                File fd = new File(saveDir + "\\xd");
                vidSaver = new ImagesToVideo(saveDir, licz, klatka);

                int max = fd.listFiles().length;
                int pro = 0;
                for (File fi : fd.listFiles()) {
                    try {
                        pro++;
                        BufferedImage tempImg = ImageIO.read(fi);
                        tempImg = ImageToAscii.CharToIMG4(tempImg, color, fsize - 3, fsize - 3, fsize);
                        //System.out.println(".call()");
                        //tempImg = ImageToAscii.curvedRound(tempImg,12,1.2,4);
                       // tempImg = ImageToAscii.curvedLines(tempImg, 4, 16);
                         // System.out.println(".call()");
                        ImageIO.write(tempImg, "jpg", new File(fi.getAbsolutePath()));

                        System.out.println(pro);
                        updateProgress(pro, max);

                    } catch (IOException ex) {
                        Logger.getLogger(VideoToImages.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    System.out.println("save");
                    vidSaver.toMP4();

                } catch (IOException ex) {
                    Logger.getLogger(VideoToImages.class.getName()).log(Level.SEVERE, null, ex);
                }

                for (File fi : fd.listFiles()) {
                    fi.delete();
                }
                fd.delete();
                updateProgress(0, max);
                return null;
            }
        };

    }
}
