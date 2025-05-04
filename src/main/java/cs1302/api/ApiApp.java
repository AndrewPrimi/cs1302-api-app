package cs1302.api;

import com.google.gson.annotations.SerializedName;
import java.nio.charset.StandardCharsets;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URL;
import java.io.IOException;
import java.net.URLEncoder;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.layout.Priority;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



/**
 * The User searches for a specific aircraft by callsign, then the api automatically feteches
 * airport information for the aircrafts destination and deparure aiport.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    Stage stage;
    Scene scene;
    VBox root;
    HBox topRow;
    HBox picRow;
    HBox textRow;
    HBox bottomRow;
    HBox infoRow;
    TextField searchBar;
    Button getInfo;
    ImageView picture;
    TextArea originInfo;
    TextArea destinInfo;
    ProgressBar progressBar;
    Label label;
    Label gettingInfo;
    String originCode;
    String destinationCode;
    AirportApiResponse airportResponse;
    AirportApiResponse destinText;
    AirportApiResponse originText;
    Boolean hasOriginInfo;
    Boolean hasDestinInfo;
    Button clear;


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        topRow = new HBox(8);
        picRow = new HBox();
        textRow = new HBox(8);
        bottomRow = new HBox(8);
        infoRow = new HBox(8);
        searchBar = new TextField("Input Plane CallSign (Ex: ENT59UC, AMU882)");
        getInfo = new Button("Get Info");
        clear = new Button("Clear");
        picture = new ImageView();
        gettingInfo = new Label(" Input an aircraft's callsign " +
        " to obtain info about its airport of origin and destination");
        originInfo = new TextArea("Information About Origin Airport\n");
        destinInfo = new TextArea("Information About Destination Airport\n");
        originInfo.setEditable(false);
        destinInfo.setEditable(false);
        label = new Label(" In cooperation with adsbdb api and airports api");
        hasDestinInfo = true;
        hasOriginInfo = true;


    } //ApiApp

    //https://wallpaperaccess.com/full/254383.jpg
    @Override
    public void init() {
        Image planeImage = new Image("file:resources/flightimage.jpg");
        originInfo.setMaxWidth(280);
        destinInfo.setMaxWidth(280);
        destinInfo.setMaxHeight(280);
        originInfo.setMaxHeight(280);
        picture.setImage(planeImage);
        picture.setPreserveRatio(true);
        picture.setFitWidth(580);
        picture.setFitHeight(500);
        topRow.getChildren().addAll(clear,searchBar, getInfo);
        topRow.setHgrow(searchBar,Priority.ALWAYS);
        searchBar.setMaxWidth(Double.MAX_VALUE);
        infoRow.getChildren().add(gettingInfo);
        picRow.getChildren().addAll(picture);
        picRow.setHgrow(picture, Priority.ALWAYS);

        textRow.getChildren().addAll(originInfo, destinInfo);
        //textRow.setHgrow(originInfo,Priority.ALWAYS);
        //originInfo.setMaxWidth(Double.MAX_VALUE);
        // textRow.setHgrow(destinInfo, Priority.ALWAYS);
        //destinInfo.setMaxWidth(Double.MAX_VALUE);

        bottomRow.getChildren().add(label);

        root.getChildren().addAll(topRow,infoRow, picRow,textRow, bottomRow);

        EventHandler<ActionEvent> loadInfoButton = (ActionEvent e) ->  {
            getInfoButton();
        };
        EventHandler<ActionEvent> clearButton = (ActionEvent e) -> {
            originInfo.clear();
            destinInfo.clear();
            originInfo.setText("Information About Origin Airport\n");
            destinInfo.setText("Information About Destination Airport\n");
        };

        getInfo.setOnAction(loadInfoButton);
        clear.setOnAction(clearButton);
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        scene = new Scene(root);
        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        //stage.setWidth(1280);
        //stage.setHeight(720);
        System.out.println("width" + scene.getWidth());
        System.out.println("height" + scene.getHeight());
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
        System.out.println(scene.getWidth());
        System.out.println(scene.getHeight());

    } // start


    private final String adsbdApi = "https://api.adsbdb.com/v0/callsign/";

    /**
     * This method builds the request to the Adsbdb api to obatin the aiport icao code.
     */
    private void apiRequest() throws IOException, InterruptedException {
        String callsign = URLEncoder.encode(searchBar.getText(), StandardCharsets.UTF_8);
        String uri = adsbdApi + callsign;
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        System.out.println("\n\n*****" + uri);
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        }
        String jsonString = response.body();
        //System.out.println(jsonString.trim());
        OuterResponse flightInfo = GSON.fromJson(jsonString, OuterResponse.class);
        System.out.println("\n\n*****" + flightInfo.response);
        originCode = flightInfo.response.flightroute.origin.icaoCode;
        destinationCode = flightInfo.response.flightroute.destination.icaoCode;
        System.out.println(originCode);
        System.out.println(destinationCode);
    }

    private final String aviationApi = "https://airport-web.appspot.com/_ah/api/" +
        "airportsapi/v1/airports/";


    /**
    * This method makes a request to the aviation api
    * in order to get information about the orgin aiport
    * and the destination aiport.
    * If no information appears then an error message appears
    *
    */
    private void apiRequest2() throws IOException, InterruptedException, NullPointerException {
        String originIcao = URLEncoder.encode(originCode, StandardCharsets.UTF_8);
        String destinIcao = URLEncoder.encode(destinationCode, StandardCharsets.UTF_8);
        String uri = aviationApi + originIcao;
        String uri2 = aviationApi + destinIcao;
        System.out.println(uri);
        System.out.println(uri2);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        HttpRequest request2 = HttpRequest.newBuilder()
            .uri(URI.create(uri2))
            .build();
        HttpResponse<String> response2 = HTTP_CLIENT.send(request2, BodyHandlers.ofString());
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        if (response.statusCode() != 200 && response2.statusCode() != 200 ) {
            throw new IOException(response.toString() + response2.toString());
        }
        //if (response2.statusCode() != 200) {
        //throw new IOException(response2.toString());
        // }

        String jsonString = response.body();
        String jsonString2 = response2.body();
        System.out.println(jsonString);
        System.out.println(jsonString2);
        NotFound originError  = GSON.fromJson(jsonString, NotFound.class);
        NotFound destinError = GSON.fromJson(jsonString2, NotFound.class);
        hasOriginInfo = true;
        hasDestinInfo = true;
        if (originError.errorName != null && originError.errorName.code == 404 ) {
            hasOriginInfo = false;
            //throw new NullPointerException("No information about oirgin airport");
        }
        if (destinError.errorName != null && destinError.errorName.code == 404) {
            hasDestinInfo = false;
            //throw new NullPointerException("No information about destination airport");
        }
        System.out.println(hasDestinInfo);
        System.out.println(hasOriginInfo);

        originText = GSON.fromJson(jsonString, AirportApiResponse.class);
        destinText = GSON.fromJson(jsonString2, AirportApiResponse.class);

    }



    /**
     * This method is used to output information about the destination and origin aiports.
     */
    public void getInfoButton() {
        try {
            apiRequest();
            apiRequest2();
            Runnable text = () -> {
                Platform.runLater( () ->  gettingInfo.setText("Getting Information. . ."));
                if (hasOriginInfo && hasDestinInfo) {
                    paragraph(destinInfo, destinText);
                    paragraph(originInfo, originText);
                    //gettingInfo.setText("Maybe try a callsign for popular airlines in the U.S.");
                    // throw new NullPointerException("No information about either airport");
                } else if (!hasOriginInfo) {
                    paragraph(destinInfo, destinText);
                    originInfo.setText("No info about the origin airport");
                } else if (!hasDestinInfo) {
                    paragraph(originInfo, originText);
                    destinInfo.setText("No info about the destination airport");
                } else {
                    throw new NullPointerException("No information about either airport");
                    // paragraph(destinInfo, destinText);
                    // paragraph(originInfo, originText);
                }
                Platform.runLater(() -> gettingInfo.setText("Done . . ."));
            };

            runNow(text);
        } catch (IOException | InterruptedException e) {
            alertError(e);
        } catch (NullPointerException NPE) {
            alertError(NPE);
        }
        gettingInfo.setText("Try Again. . .");
    }


    /**
     * This method is used to construct the aiport information paragraph.
     * @param name used to determine which text box the information is going to
     * @param airportInfo used to designate if the airport information should be
     * from the destination or origin
     */
    private void paragraph(TextArea name, AirportApiResponse airportInfo) {
        name.setText(
            "ICAO: " + airportInfo.icao + "\n" +
            "Name: " + airportInfo.name + "\n" +
            "URL: " + airportInfo.url + "\n" +
            "Last Update: " + airportInfo.lastUpdate
        );
    }



    /**
     * creates and starts new daemon thread.
     * @param target the object whose method is invoked when thread starts
     */
    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Creates error alert that is shown on panel in the app.
     * @param cause the exception that caused error
     */
    public static void alertError(Throwable cause) {
        TextArea text = new TextArea(cause.toString());
        text.setEditable(false);
        Alert alert  = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    }


} // ApiApp
