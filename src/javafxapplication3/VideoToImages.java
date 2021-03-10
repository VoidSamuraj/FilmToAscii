package javafxapplication3;

import com.sun.javaws.Main;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    
    static String testSource;
    static int [] params=new int[3];
    static int type=-1;
    static boolean color;
    public static void setPrefx(int[] parameters, int typ, boolean c)
    {
        params=parameters;
        type=typ;
        color=c;
    }
    public static ImagesToVideo getVidSaver() {
        return vidSaver;
    }

    public VideoToImages() {
        type=-1;
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

        final Scene scene = new Scene(root, 960, 540);                                              //wymiarvid
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
        mp.setOnEndOfMedia(() -> {                                                                          //zmiana na wybor
            
            primaryStage.hide();
            Stage st = new Stage();
            //AsciiSettings as = null;
            testSource=saveDir+"\\xd";
            /*try {
            as = new AsciiSettings(st, saveDir + "\\xd");
            } catch (IOException ex) {
            Logger.getLogger(VideoToImages.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        
                FXMLLoader load=new FXMLLoader();
                load.setLocation(VideoToImages.class.getClassLoader().getResource("javafxapplication3/styl.fxml"));
                //  System.out.println(VideoToImages.class.getClassLoader().getResource("javafxapplication3/styl.fxml"));
                Parent settingsRoot = null;
                try {
                    settingsRoot = load.load(); //(new URL("\\javafxapplication3\\styl.fxml"));
                    //primaryStage.getClass().getResource("\\javafxapplication3\\styl.fxml")); ///                                       problem
                    // FXMLLoader.lo
                    // primaryStage.getOwner().getc
                } catch (IOException ex) {
                    Logger.getLogger(VideoToImages.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Scene scena2 = new Scene(settingsRoot, 1250, 700);
                
                
                primaryStage.setScene(scena2);
                primaryStage.show();
            

            Task ta = process();
            Thread tp = new Thread(ta);
            pb.progressProperty().bind(ta.progressProperty());
            tp.start();
        });
    }
    public void starnSettings(Scene stage){
    
    }
    public static Task process() {

        return new Task() {
            @Override
            protected Object call(){
                while (type==-1){try {
                    Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VideoToImages.class.getName()).log(Level.SEVERE, null, ex);
                    }
}
                File fd = new File(saveDir + "\\xd");
                vidSaver = new ImagesToVideo(saveDir, licz, klatka);

                int max = fd.listFiles().length;
                int pro = 0;
                for (File fi : fd.listFiles()) {
                    try {
                        pro++;
                        BufferedImage tempImg = ImageIO.read(fi);
                        switch(type){
                            case 1:
                                tempImg=ImageToAscii.CharToIMG4(tempImg, color, params[0] - 3, params[0] - 3, params[0]);
                                break;
                            case 2:
                                tempImg=ImageToAscii.imageToDots(tempImg, params[0], color);
                                break;
                            case 3:
                                tempImg=ImageToAscii.curvedLines(tempImg, params[0], params[1]);
                                break;
                            case 4:
                                tempImg=ImageToAscii.curvedRound(tempImg, params[0], params[1], params[2]);
                                break;
                            case 5:
                                tempImg=ImageToAscii.imageToLine(tempImg, params[0]);
                                break;
                        }
                      //  tempImg = ImageToAscii.CharToIMG4(tempImg, color, fsize - 3, fsize - 3, fsize);
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
