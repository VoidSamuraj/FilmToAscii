package javafxapplication3;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Karol
 */
public class ImagesToVideo implements Runnable {

    private static String outputFilename;
    private static int dur; //cały czas
    private static double klatka;
    public int procent;

    public ImagesToVideo(String outputFilename, int dur, double klatka) {
        this.outputFilename = outputFilename;
        this.dur = dur;
        this.klatka = klatka;
    }

    @Override
    public void run() {
        try {
            toMP4();
        } catch (IOException ex) {
            Logger.getLogger(ImagesToVideo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void toMP4() throws IOException {
        System.out.println(outputFilename);
        File f = new File(outputFilename + "\\ASCII.mp4");
        f.createNewFile();
        IMediaWriter writer = ToolFactory.makeWriter(f.getAbsolutePath());
        Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, screenBounds.width / 2, screenBounds.height / 2);
        
        for (int index = 0; index <= dur - 1; index++) {
            System.err.println(dur);
            System.out.println(index);
            BufferedImage screen = null;
            try {
                File fi = new File(outputFilename + "\\xd\\" + index + ".jpg");
                screen = ImageIO.read(fi);
            } catch (IOException ex) {
                Logger.getLogger(ImagesToVideo.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(f.getAbsolutePath());
            }
            BufferedImage bgrScreen = ImageToAscii.convertToType(screen, BufferedImage.TYPE_3BYTE_BGR);
            writer.encodeVideo(0, bgrScreen, (long) (2 * 600 * klatka * index), TimeUnit.MILLISECONDS);
            System.out.println(klatka);
        }
        writer.close();
        System.out.println("Video Created");
    }
}
