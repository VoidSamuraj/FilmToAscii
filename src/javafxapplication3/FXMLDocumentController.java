
package javafxapplication3;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 *
 * @author Karol
 */
public class FXMLDocumentController implements Initializable {

    Service thread;
    public IntegerProperty proc;
    public IntegerProperty prom;
    @FXML
    private Label output;
    @FXML
    private Label source;
    @FXML
    private ProgressBar statusbar;

    @FXML
    public void selectSourcePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz wideo");
        source.setText(fileChooser.showOpenDialog(source.getScene().getWindow()).getAbsolutePath());
    }

    @FXML
    public void selectSavePath() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Wybierz katalog zapisu filmu");
        chooser.setInitialDirectory(new JFileChooser().getFileSystemView().getDefaultDirectory());
        File dir = chooser.showDialog(output.getScene().getWindow());

        output.setText(dir.getAbsolutePath());
    }

    @FXML
    public void check() throws InterruptedException {

        if (source.getText() == "") {
            source.setStyle("-fx-background-color: red;");
            return;
        } else {
            source.setStyle("-fx-background-color: green;");
        }
        
        if (output.getText() == "") {
            output.setStyle("-fx-background-color: red;");
            return;
        } else {
            output.setStyle("-fx-background-color: green;");
        }
        Stage st = new Stage();
        VideoToImages vti = new VideoToImages();
        vti.start(st, source.getText(), output.getText(), statusbar);
    }

    public void setBar() {
        this.statusbar.setProgress(proc.doubleValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        proc = new SimpleIntegerProperty();
        prom = new SimpleIntegerProperty();
    }
}
