/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication3;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Karol
 */
public class StylController implements Initializable {
    static String src=VideoToImages.testSource;                                                              // inicjalizacja statyczną zmienną z innej klasy
    private final static String [] t={"ASCII","Dots","Lines","Circles", "Lines2"};
    private BufferedInputStream bi;
    Image img;
    @FXML
    private ChoiceBox<String> choice;
    private int typ;
    @FXML
    private CheckBox color;
    @FXML
    private Button test;
    @FXML
    private Button ok;
    @FXML
    private ImageView imv;
    @FXML
    private Label pl1;
    @FXML
    private Slider p1;
    @FXML
    private Label pl2;
    @FXML
    private Slider p2;
    @FXML
    private Label pl3;
    @FXML
    private Slider p3;
    @FXML
    private Label pv1;
    @FXML
    private Label pv2;
    @FXML
    private Label pv3;
    @FXML
    public void start(){

    int []t={(int)p1.getValue(),(int)p2.getValue(),(int)p3.getValue()};
    VideoToImages.setPrefx(t, typ, color.isSelected());
    ((Stage)imv.getScene().getWindow()).close();
    }
    @FXML
    public void changeBox(){
      switch(choice.getValue()){
            case "ASCII":
                color.setDisable(false);
                p1.setVisible(true);
                pl1.setVisible(true);
                pv1.setVisible(true);
                pl1.setText("Rozdzielczość");
                p1.setMax(25);
                p1.setMin(4);
                p2.setMin(4);
                p3.setMin(4);
                p2.setVisible(false);
                pl2.setVisible(false);
                pv2.setVisible(false);
                p3.setVisible(false);
                pl3.setVisible(false);
                pv3.setVisible(false);
                typ=1;
                break;
            case "Dots":
                color.setDisable(false);
                p1.setVisible(true);
                pl1.setVisible(true);
                pv1.setVisible(true);
                pl1.setText("Rozdzielczość");
                p1.setMax(50);
                p1.setMin(4);
                p2.setMin(4);
                p3.setMin(4);
                p2.setVisible(false);
                pl2.setVisible(false);
                pv2.setVisible(false);
                p3.setVisible(false);
                pl3.setVisible(false);
                pv3.setVisible(false);
                typ=2;
                break;
            case "Lines":
                color.setDisable(true);
                p1.setVisible(true);
                pl1.setVisible(true);
                pv1.setVisible(true);
                pl1.setText("Rozdzielczość - x");
                pl2.setText("Rozdzielczość - y");
                p1.setMax(25);
                p1.setMin(4);
                p2.setMin(4);
                p3.setMin(4);
                p2.setMax(25);
                p2.setVisible(true);
                pl2.setVisible(true);
                pv2.setVisible(true);
                p3.setVisible(false);
                pl3.setVisible(false);
                pv3.setVisible(false);
                typ=3;
                break;
            case "Circles":
                color.setDisable(true);
                p1.setVisible(true);
                pl1.setVisible(true);
                pv1.setVisible(true);
                pl1.setText("Odstęp");
                pl2.setText("Rozdzielczość - x");
                pl3.setText("Rozdzielczość - y");
                p1.setMax(25);
                p1.setMin(1);
                p2.setMin(2);
                p3.setMin(2);
                p2.setMax(25);
                p3.setMax(25);
                p2.setVisible(true);
                pl2.setVisible(true);
                pv2.setVisible(true);
                p3.setVisible(true);
                pl3.setVisible(true);
                pv3.setVisible(true);
                typ=4;
                break;
            case "Lines2":
                color.setDisable(true);
                p1.setVisible(true);
                pl1.setVisible(true);
                pv1.setVisible(true);
                p1.setMax(25);
                p1.setMin(4);
                p2.setMin(4);
                p3.setMin(4);
                pl1.setText("Rozdzielczość");
                p2.setVisible(false);
                pl2.setVisible(false);
                pv2.setVisible(false);
                p3.setVisible(false);
                pl3.setVisible(false);
                pv3.setVisible(false);
                typ=5;
                break;
      }
    }
    @FXML
    public void test() throws IOException{
        File f = new File(src + "\\0.jpg");
        BufferedImage bf = ImageIO.read(f);
        BufferedImage en = null ;
        switch(choice.getValue()){
            case "ASCII":
                int liczba=(int)p1.getValue();
                 en= ImageToAscii.CharToIMG4(bf, color.isSelected(), liczba - 3, liczba - 3, liczba);
                break;
            case "Dots":
                 en= ImageToAscii.imageToDots(bf, (int)p1.getValue(), color.isSelected());
                break;
            case "Lines":
                 en= ImageToAscii.curvedLines(bf, (int)p1.getValue(), (int)p2.getValue());
                break;
            case "Circles":
                 en= ImageToAscii.curvedRound(bf,(int)p1.getValue(), (int)p2.getValue(), (int)p3.getValue());
                break;
            case "Lines2":
                 en= ImageToAscii.imageToLine(bf, (int)p1.getValue());
                break;
                
        
        }
       
        File fs = new File(src + "\\test.jpg");
        fs.createNewFile();
        ImageIO.write(en, "jpg", fs);
         bi = new BufferedInputStream(new FileInputStream(new File(src + "\\test.jpg")));
         img =new Image(bi);
         imv.setImage(img);
    }
/*
    public StylController(Stage st, String sourceImage) {
        this.src=sourceImage;
    }/*
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
       choice.getItems().addAll(t);

       p1.valueProperty().addListener((e)->pv1.setText(Integer.toString((int)p1.getValue())));
       p2.valueProperty().addListener((e)->pv2.setText(Integer.toString((int)p2.getValue())));
       p3.valueProperty().addListener((e)->pv3.setText(Integer.toString((int)p3.getValue())));
        ok=new Button("xd");
    }    
 
}
