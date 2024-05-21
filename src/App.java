import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.RotateTransition;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;

public class App extends Application {
    
    private ArrayList<String> playlist;
    private int nowPlaying = 0;
    private int klik = 0;
    private int enter = 0;
    
    private Media media;
    private MediaPlayer mediaPlayer;
    private Label nowLabel;
    private Slider timeSlider;
   RotateTransition  rotateTransition;

    Image image = new Image("file:resources/kaset.png");
  
    ImageView imageView = new ImageView(image);
    
    
    @Override
    public void start(Stage primaryStage) {

        // List musik
        playlist = new ArrayList<>();
        playlist.add("laguu.mp3");
        playlist.add("lagu2.mp3");
        playlist.add("lagu3.mp3");

        // Label judul lagu
        nowLabel = new Label();
        nowLabel.setText("Lagu yang sedang di putar : " + playlist.get(nowPlaying));
        nowLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), imageView);
        rotateTransition.setByAngle(360); // Mengatur rotasi sebanyak 360 derajat (1 putaran)
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Menjalankan animasi secara terus menerus
        rotateTransition.setAutoReverse(false); // Tidak melakukan rotasi terbalik
        rotateTransition.play();

       

        //menggunakan event klik pada gambar musik untuk memainkan dan memberhentikan lagu
        imageView.setOnMouseClicked(event -> {
              if(klik == 0){
                 pause();
                 if (rotateTransition != null){
                    rotateTransition.stop();
                 klik++;
        };
              }else if (klik == 1) {
              
            resume();
            if (rotateTransition != null){
                 rotateTransition.play();
                  klik--;
                     };

              }
             
        });
       
        Button playButton = new Button("Play");
        playButton.setOnAction(e -> play());

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e ->{
            pause();
        if (rotateTransition != null){
        rotateTransition.stop();
       
        };
        }
        );
        
        Button resumeButton = new Button("Resume");  
        resumeButton.setOnAction(e ->{ 
        resume();
        if (rotateTransition != null){
        rotateTransition.play();}
        });
        
        Button nextButton = new Button("Next >");
        nextButton.setOnAction(e -> {nextSong();
        if (rotateTransition != null){
        rotateTransition.play();}
        });

        Button mundurButton = new Button("mundur <");
        nextButton.setOnAction(e -> {mundursong();
        if (rotateTransition != null){
        rotateTransition.play();}
        });
        
        playButton.setStyle("-fx-text-fill: white; -fx-background-color: black ; -fx-font-size: 14px; -fx-min-width: 70px; -fx-min-height: 50px;");
        pauseButton.setStyle("-fx-text-fill: white; -fx-background-color: black ; -fx-font-size: 14px; -fx-min-width: 70px; -fx-min-height: 50px;");
        resumeButton.setStyle("-fx-text-fill: white; -fx-background-color: black ; -fx-font-size: 14px; -fx-min-width: 70px; -fx-min-height: 50px;");
        nextButton.setStyle("-fx-text-fill: white; -fx-background-color: black ; -fx-font-size: 14px; -fx-min-width: 70px; -fx-min-height: 50px;");
        mundurButton.setStyle("-fx-text-fill: white; -fx-background-color: black ; -fx-font-size: 14px; -fx-min-width: 70px; -fx-min-height: 50px;");
  
         // Tombol kontrol
        timeSlider = new Slider();
        timeSlider.setPrefHeight(300);
        timeSlider.setPrefWidth(300);
         timeSlider.setMin(0);
         timeSlider.setMax(100);

    // Menambahkan listener untuk mengatur posisi waktu lagu saat nilai slider berubah
        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(newValue.doubleValue() / 100.0));
        }

    });
        
        HBox buttons = new HBox(80);
        buttons.getChildren().addAll(playButton, pauseButton, resumeButton, nextButton,mundurButton);
        
        HBox slideBox = new HBox(10);
        slideBox.setPadding(new Insets(60));
        slideBox.getChildren().addAll(imageView,timeSlider);

       
        // Susunan layout
        BorderPane pane = new BorderPane();
        pane.setTop(nowLabel);
        pane.setCenter(slideBox);
        pane.setBottom(buttons);
        pane.setStyle("-fx-background-image: url('file:resources/background.jpeg');" +
        "-fx-background-size: cover; -fx-background-repeat: no-repeat;");
        
        // Tampilkan GUI
        Scene scene = new Scene(pane, 690, 450); 
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pemutar Musik");
        primaryStage.show();

    //menggunakan event keyboard spasi untuk memainkan dan memberhentikan lagu
        scene.setOnKeyPressed(event -> {
            if (event.getCode()  == KeyCode.SPACE ) {
                if(klik == 0){
                 pause();
                 if (rotateTransition != null){
                    rotateTransition.stop();
                 klik++;
        };
              }else if (klik == 1) {
              
            resume();
            if (rotateTransition != null){
                 rotateTransition.play();
                  klik--;
                     };

              }
            }
        });
        
        // Mutar lagu pertama
        play();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    

    private void play() {
     
        // Kode play music
        media = new Media(new File("resources/" + playlist.get(nowPlaying)).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
       
        

     
    mediaPlayer.setOnPlaying(() -> {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Duration totalTime = mediaPlayer.getTotalDuration();
            Duration currentTime = mediaPlayer.getCurrentTime();

            double percentage = (currentTime.toSeconds() / totalTime.toSeconds()) * 100.0;
            timeSlider.setValue(percentage);
        });
    });
        
        // Update label
        nowLabel.setText("Lagu yang sedang di putar : " + playlist.get(nowPlaying));
    }

    
    //untuk memberhentikan lagu yang sedang di mainkan
    private void pause() {
        mediaPlayer.pause(); 
        if (rotateTransition != null){
        rotateTransition.stop();
        imageView.setRotate(0);
        }
    }
    //melanjutkan lagu yang telah di berhentikan dan memainkanya kembali 

    private void resume() {
       if(mediaPlayer != null) {
           mediaPlayer.play();
       }
    }
    //untuk memainkan lagu selanjutnya yang ada di dalam list lagu
    private void nextSong() {
        nowPlaying++;
        if(nowPlaying >= playlist.size()) {
            nowPlaying = 0; 
        }
        play();
    }
// memainkan lagu sebelumnya yang ada di list lagu
    private void mundursong() {
        if (nowPlaying > 0){
            nowPlaying = playlist.size();
        nowPlaying--;
        }
        if(nowPlaying >= playlist.size()) {
            nowPlaying = 0; 
        }
        play();
    }

    
    
}