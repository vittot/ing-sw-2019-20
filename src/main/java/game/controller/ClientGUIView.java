package game.controller;

import game.controller.commands.clientcommands.JoinWaitingRoomRequest;
import game.model.*;
import game.model.effects.FullEffect;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.StandardWatchEventKinds;
import java.util.*;
import java.util.List;


public class ClientGUIView extends Application implements View{
    public static ClientGUIView GUI;
    public ClientController controller;
    private String user;
    Stage primaryStage;
    int displayX = 1382;
    int displayY = 744;

    public ClientGUIView() {
        GUI = this;
    }

    public static ClientGUIView getInstance() throws InterruptedException {
        if(GUI!=null){
            return GUI;
        }
        else{
            (new Thread(() -> launch())).start();
            while(GUI == null)
                Thread.sleep(100);
        }
        return GUI;
    }

    public void setController(ClientController controller){
        this.controller = controller;
    }

    @Override
    public void start(Stage primaryStage) {
        this.GUI = this;
        this.primaryStage = primaryStage;

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        InputStream input;
        InputStream input2;
        InputStream input3;
        InputStream input4;
        int mapId = 1;
        input = classLoader.getResourceAsStream("graphics/map/mappa"+mapId+".png");
        input2 =  classLoader.getResourceAsStream("graphics/cards/AD_weapons_IT_024.png");
        input3 = classLoader.getResourceAsStream("graphics/map/BluDash.png");
        input4 = classLoader.getResourceAsStream("graphics/map/YellowDash.png");

        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(734);
        imageView.setPreserveRatio(true);
        Image image2 = new Image(input2);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(122);
        imageView2.setPreserveRatio(true);
        ImageView im3 = new ImageView(image2);
        ImageView im4 = new ImageView(image2);
        ImageView im5 = new ImageView(image2);
        ImageView im6 = new ImageView(image2);
        ImageView im7 = new ImageView(image2);
        Image imdg = new Image(input3);
        Image myp = new Image(input4);
        ImageView imd1 = new ImageView(imdg);
        ImageView imd2 = new ImageView(imdg);
        ImageView imd3 = new ImageView(imdg);
        ImageView imd4 = new ImageView(imdg);
        ImageView imd5 = new ImageView(myp);
        Label text = new Label("Log text.dwwddwdwd");

        imd5.setOnMouseClicked(e -> {
            System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height + "" + GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width);
        });


        text.setEllipsisString("pawpowdwpo");
        text.setStyle("-fx-font: 20px Tahoma;"+"-fx-background-color: Red");
        text.setPrefWidth(718);
        text.setMaxHeight(25);
        text.setWrapText(true);



        StackPane map = new StackPane();
        map.setPrefSize(displayX,displayY);
        map.getChildren().addAll(imageView,imageView2,im3,im4,im5,im6,im7,imd1,imd2,imd3,imd4,imd5,text);

        List<ImageView> im = new ArrayList<>(Arrays.asList(imageView2,im3,im4,im5,im6,im7));
        List<ImageView> imd = new ArrayList<>(Arrays.asList(imd1,imd2,imd3,imd4,imd5));

        int i = 0;
        for(ImageView imw : im){
            imw.setFitWidth(108);
            imw.setPreserveRatio(true);
            StackPane.setAlignment(imw,Pos.BOTTOM_RIGHT);
            StackPane.setMargin(imw,new Insets(0,i,0,0));
            i = i + 108;
        }
        i = 0;
        for(ImageView imw : imd){
            imw.setFitWidth(635);
            imw.setPreserveRatio(true);
            StackPane.setAlignment(imw,Pos.TOP_RIGHT);
            StackPane.setMargin(imw,new Insets(i,0,0,0));
            i = i + 143;

        }
        StackPane.setAlignment(text,Pos.BOTTOM_LEFT);
        StackPane.setMargin(text,new Insets(0,0,156,0));
        StackPane.setAlignment(imd5,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(imageView,Pos.TOP_LEFT);
        Scene scene = new Scene(map);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    private void callRoomCreate(String s ){

        System.out.println(s);
        int mapId = 0, nPlayer= 5;
        if(s == "map1") mapId = 1;
        if(s == "map2") mapId = 2;
        if(s == "map3") mapId = 3;
        if(s == "map4") mapId = 4;
        System.out.println(mapId);
        //controller.getClient().sendMessage(new CreateWaitingRoomRequest(mapId,nPlayer,user));
    }

    @Override
    public void setUserNamePhase() {
        //UserLogin GUI creation
        primaryStage.setTitle("Adrenaline Login");
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid, 500, 300);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Text text = new Text("WEALCOME TO ADRENALINE");
        grid.add(text, 1,0);

        Label userName = new Label("User Name:");
        grid.add(userName, 1, 1);


        TextField userTextField = new TextField();
        userTextField.setPromptText("Your username");
        text.setStyle("-fx-font: 20px Tahoma;\n" +
                "    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);\n" +
                "    -fx-stroke-width: 1;");
        grid.add(userTextField, 2, 1);
        Button btn = new Button("Sign in");
        btn.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
        btn.setTooltip(new Tooltip("Sign in"));
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        //User Waiting room GUI
        StackPane rooms = new StackPane();
        rooms.setPrefSize(500,300);

        Button btn2  = new Button("asca");
        StackPane.setMargin(btn2, new Insets(0,50,50,0));
        StackPane.setAlignment(btn2,Pos.BOTTOM_RIGHT);

        ObservableList list = rooms.getChildren();
        list.add(btn2);
        Scene room = new Scene(rooms);
        // SetOn Acntion
        btn.setOnAction(e -> {
            if(userTextField.getText() != null){
                user = userTextField.getText();
                System.out.println("user:\t" + userTextField.getText());
                System.out.println("X :" + primaryStage.getHeight() +" Y "+ primaryStage.getWidth());
                //primaryStage.close();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void insufficientAmmoNotification() {

    }

    @Override
    public void chooseStepActionPhase() {

    }

    @Override
    public void chooseSquarePhase(List<Square> possiblePositions) {

    }

    @Override
    public void chooseTargetPhase(List<Target> possibleTargets) {

    }

    @Override
    public void chooseTurnActionPhase() {

    }

    @Override
    public void invalidTargetNotification() {

    }

    @Override
    public void invalidWeaponNotification() {

    }

    @Override
    public void invalidActionNotification() {

    }

    @Override
    public void insufficientNumberOfActionNotification() {

    }

    @Override
    public void invalidStepNotification() {

    }

    @Override
    public void maxNumberOfWeaponNotification() {

    }

    @Override
    public void damageNotification(int shooterId, int damage, int hit) {

    }

    @Override
    public void notifyMovement(int pId, int newX, int newY) {

    }

    @Override
    public void notifyDeath(Kill kill) {

    }

    @Override
    public void grabWeaponNotification(int pID, String name, int x, int y) {

    }

    @Override
    public void powerUpUsageNotification(int id, String name, String description) {

    }

    @Override
    public void choosePowerUpToRespawn(List<CardPower> cardPower) {

    }

    @Override
    public void notifyCompletedOperation(String message) {

    }

    @Override
    public void notifyInvalidPowerUP() {

    }

    @Override
    public void notifyInvalidGrabPosition() {

    }

    @Override
    public void choosePowerUpToUse(List<CardPower> cardPower) {

    }

    @Override
    public void notifyInvalidMessage() {

    }

    @Override
    public void notifyTurnChanged(int pID) {

    }

    @Override
    public void notifyMarks(int marks, int idHitten, int idShooter) {

    }

    @Override
    public void notifyGrabCardAmmo(int pID) {

    }

    @Override
    public void notifyRespawn(int pID) {

    }

    @Override
    public void chooseWeaponToGrab(List<CardWeapon> weapons) {

    }

    @Override
    public void chooseRoomPhase(List<WaitingRoom> waitingRooms) {
        StackPane chooseRoom = new StackPane();
        chooseRoom.setPrefSize(500, 300);
        Scene chooseR = new Scene(chooseRoom);
        if (waitingRooms.isEmpty()) {
            Label text = new Label("No waiting room available");
            Button bt1 = new Button("Create new Waiting room");
            chooseRoom.getChildren().addAll(text, bt1);
            bt1.setOnAction(this::handleNewRoom);
            return;
        } else {
            VBox infoRoom = new VBox();
            ToggleGroup tg = new ToggleGroup();
            for (WaitingRoom w : waitingRooms) {
                HBox choise = new HBox();
                RadioButton rb = new RadioButton("Waiting Room " + w.getId() + ":");
                rb.setToggleGroup(tg);
                Label text = new Label(w.toString());
                choise.getChildren().add(text);
                infoRoom.getChildren().add(choise);
            }
            chooseRoom.getChildren().add(infoRoom);
            StackPane.setAlignment(infoRoom, Pos.TOP_LEFT);
            StackPane.setMargin(infoRoom, new Insets(25, 0, 0, 15));

            Button submit = new Button("JoinRoom");
            submit.setOnAction(actionEvent -> {
                if(tg.getSelectedToggle().isSelected())
                    handleJoinRoom((RadioButton)tg.getSelectedToggle());
            });
        }
    }
    //TODO mettere javaDoc
    private void handleJoinRoom(RadioButton rb){
        int id=0;
        if(rb.getId() == "Waiting Room 1:")
            id = 1;
        if(rb.getId() == "Waiting Room 2:")
            id = 2;
        if(rb.getId() == "Waiting Room 3:")
            id = 3;
        if(rb.getId() == "Waiting Room 4:")
            id = 4;
        controller.getClient().sendMessage(new JoinWaitingRoomRequest(id, user));
    }


    /*if(waitingRooms.isEmpty()){
        writeText("No waiting room currently available on the server");
        createRoomPhase();
        return;
    }
    int nRoom;
    for(WaitingRoom w : waitingRooms)
    {
        writeText(w.toString());
    }
    writeText("Enter the id for the selected waiting room or -1 if you want to create a new waiting room:");
    do{
        nRoom = readInt();
    }while((nRoom <= 0 || nRoom > waitingRooms.size()) && nRoom != -1);
    if(nRoom != -1)
    {
        controller.getClient().sendMessage(new JoinWaitingRoomRequest(nRoom,user));
    }
    else
        createRoomPhase();
*/
    private void handleNewRoom(ActionEvent e){

        int mapId = 1, nPlayer;
        StackPane sp = new StackPane();
        sp.setPrefSize(500,300);
        Scene room = new Scene(sp);

        Label text = new Label("Choose map id");
        Label error = new Label("");
        ChoiceBox mapc = new ChoiceBox();
        mapc.getItems().add("map1");
        mapc.getItems().add("map2");
        mapc.getItems().add("map3");
        mapc.getItems().add("map4");
        nPlayer = 5;
        Button submit = new Button("Create Room");


        ObservableList list = sp.getChildren();
        list.addAll(text,mapc, submit,error);
        StackPane.setAlignment(text,Pos.TOP_CENTER);
        StackPane.setMargin(text,new Insets(25,0,0,0));
        StackPane.setAlignment(mapc,Pos.CENTER_LEFT);
        StackPane.setMargin(mapc,new Insets(0,0,0,50));
        StackPane.setAlignment(submit,Pos.BOTTOM_RIGHT);
        StackPane.setMargin(submit,new Insets(0,50,25,0));
        StackPane.setAlignment(error,Pos.BOTTOM_LEFT);

        primaryStage.setScene(room);
        primaryStage.show();

        submit.setOnAction(actionEvent -> {
            if(mapc.getValue() != null)
                callRoomCreate(mapc.getValue().toString());
            else
                error.setText("Invalid Map choise");
        });
    }

    @Override
    public void showMapsPhase(List<GameMap> availableMaps) {

    }

    @Override
    public void reloadWeaponPhase(List<CardWeapon> weaponsToReload) {

    }

    @Override
    public void showReloadMessage(CardWeapon cW) {

    }

    @Override
    public void chooseWeaponToShoot(List<CardWeapon> myWeapons) {

    }

    @Override
    public void chooseFirstEffect(FullEffect baseEff, FullEffect altEff) {

    }

    @Override
    public void usePlusBeforeBase(FullEffect plusEff) {

    }

    @Override
    public void usePlusInOrder(List<FullEffect> plusEffects, int i) {

    }

    @Override
    public void choosePlusEffect(List<FullEffect> plusEffects) {

    }

    @Override
    public void showRanking(SortedMap<Player, Integer> ranking) {

    }

    @Override
    public void rejoinGamePhase(List<String> otherPlayers) {

    }

    @Override
    public void notifyPlayerSuspended(Player p) {

    }

    @Override
    public void timeOutPhase() {

    }

    @Override
    public void alreadyLoggedPhase() {

    }

    @Override
    public void loginCompletedPhase() {

    }

    @Override
    public void rejoinGameConfirm() {

    }

    @Override
    public void notifyPlayerRejoin(Player p) {

    }

    @Override
    public void notifyPlayerLeavedWaitingRoom(Player p) {

    }

    @Override
    public void notifyPlayerJoinedWaitingRoom(Player p) {

    }
}
