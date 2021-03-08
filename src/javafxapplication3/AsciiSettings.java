package javafxapplication3;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Karol
 */
public class AsciiSettings {

    StackPane root = new StackPane();
    Scene scene;
    String src;
    CheckBox ckolor;
    ComboBox<String> typy;
    ScrollBar sb;
    Label licz;
    Button pruba, ok;
    Image img;
    ImageView iv;
    BufferedInputStream bi;
    VBox vb;
    HBox hb;
    public double getSb() {
        return sb.getValue();
    }

    public boolean getCkolor() {
        return ckolor.isSelected();
    }

    public AsciiSettings(Stage st, String sr) throws IOException {
        src = sr;
        hb = new HBox();
        vb= new VBox();
        ok = new Button("OK");
        pruba = new Button("Próba");
        scene = new Scene(root, 960, 540);
        scene.setFill(Color.DARKGREY);
        licz = new Label();
        ckolor = new CheckBox("Kolor");
        typy=new ComboBox<>();                      //
        sb = new ScrollBar();
        DoubleProperty dp;
        
        vb.setSpacing(10);
        hb.setSpacing(10);
        hb.setPrefWidth(960);
        sb.setOrientation(Orientation.HORIZONTAL);
        sb.setMin(8);
        sb.setMax(30);
        sb.setBlockIncrement(1.0);
        sb.setPrefWidth(100);
        dp = sb.valueProperty();
        
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                st.hide();
            }
        });

        
        pruba.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                proba();
            }
        });
        
        test();
        bi = new BufferedInputStream(new FileInputStream(new File(src + "test.jpg")));
        img = new Image(bi);
        iv = new ImageView(img);
        iv.setFitWidth(700);
        iv.setFitHeight(400);
        dp.addListener((observable, oldvalue, newValue) -> {
            String v = newValue.toString();
            licz.setText(v.substring(0, v.indexOf('.')));

        });
        vb.setAlignment(Pos.TOP_CENTER);
        vb.getChildren().add(ckolor);
        vb.getChildren().add(sb);
        vb.getChildren().add(licz);
        vb.getChildren().add(pruba);
        vb.getChildren().add(ok);
        hb.getChildren().add(vb);
        hb.getChildren().add(iv);
        root.getChildren().add(hb);
        st.setScene(scene);
        st.setTitle("Video");
        st.showAndWait();

    }
    public int getSB()
    {
        return sb.valueProperty().intValue();
    }

    public void test() throws IOException {
        File f = new File(src + "\\0.jpg");
        BufferedImage bf = ImageIO.read(f);
        int liczba = 5;
        if (licz.getText() != "") {
            liczba = (Integer.parseInt(licz.getText())) - 3;
        }
        BufferedImage en = ImageToAscii.CharToIMG4(bf, ckolor.isSelected(), liczba - 3, liczba - 3, liczba);
        File fs = new File(src + "test.jpg");
        fs.createNewFile();
        ImageIO.write(en, "jpg", fs);

    }

    public void proba() {
        try {
            test();
        } catch (IOException ex) {
            Logger.getLogger(AsciiSettings.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            bi = new BufferedInputStream(new FileInputStream(new File(src + "test.jpg")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsciiSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        img = new Image(bi);
        iv.setImage(img);
    }

}
