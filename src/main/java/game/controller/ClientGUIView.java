package game.controller;

import game.controller.commands.clientcommands.*;
import game.model.*;
import game.model.effects.FullEffect;
import game.model.effects.SimpleEffect;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.InputStream;
import java.util.*;
import java.util.List;


public class ClientGUIView extends Application implements View{
    public static ClientGUIView GUI;
    public ClientController controller;
    private String user;
    private Stage primaryStage;
    private double displayX = 1366;
    private double displayY = 768;
    private double screenWidth = Screen.getScreens().get(0).getBounds().getWidth();
    private double screenHeight = Screen.getScreens().get(0).getBounds().getHeight();
    private Label text = new Label("");
    private List<GameMap> availableMaps;

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
        this.primaryStage = primaryStage;
        showMap();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void handlePlayerClick(MouseEvent e){
        text.setText("Player "+((Circle)e.getSource()).getId());
    }

    private void handleSquareClick(MouseEvent e){
        int i = Integer.parseInt(((Rectangle)e.getSource()).getId());
        text.setText("You selected square in: " + (i-1)/4 + ", " + (i-1)%4);
    }

    private void handleWeaponClick(MouseEvent e){
        text.setText("You select "+((ImageView)e.getSource()).getId());
    }

    private void callRoomCreate(String s ){

        System.out.println(s);
        int mapId = 0, nPlayer= 5;
        if(s == "map1") mapId = 1;
        if(s == "map2") mapId = 2;
        if(s == "map3") mapId = 3;
        if(s == "map4") mapId = 4;
        System.out.println(mapId);
        controller.getClient().sendMessage(new CreateWaitingRoomRequest(mapId,nPlayer,user));
    }

    @Override
    public void setUserNamePhase() {
        //UserLogin GUI creation
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid, 500, 300);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Text text = new Text("WELCOME TO ADRENALINE");
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

        primaryStage.setScene(scene);
        primaryStage.setHeight(300);
        primaryStage.setWidth(500);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        // SetOn Acntion
        btn.setOnAction(e -> {
            if(userTextField.getText() != ""){
                user = userTextField.getText();
                System.out.println("user:\t" + userTextField.getText());
                System.out.println("X :" + primaryStage.getHeight() +" Y "+ primaryStage.getWidth());
                ClientContext.get().setUser(user);
                controller.getClient().sendMessage(new LoginMessage(ClientContext.get().getUser()));
                System.out.println("Login inviato");
            }
        });

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
    public void chooseTargetPhase(List<Target> possibleTargets, SimpleEffect currSimpleEffect) {

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
        System.out.println(message);
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
        } else {
            VBox infoRoom = new VBox();
            ToggleGroup tg = new ToggleGroup();
            Button bt1 = new Button("Create new Waiting room");
            for (WaitingRoom w : waitingRooms) {
                RadioButton rb = new RadioButton("Waiting Room " + w.getId() + ":");
                rb.setId(""+w.getId());
                rb.setToggleGroup(tg);
                Label text = new Label(w.toString());
                HBox choice = new HBox(rb,text);
                infoRoom.getChildren().add(choice);
            }
            chooseRoom.getChildren().addAll(infoRoom,bt1);
            StackPane.setAlignment(infoRoom, Pos.TOP_LEFT);
            StackPane.setMargin(infoRoom, new Insets(25, 0, 0, 15));

            Button submit = new Button("JoinRoom");
            chooseRoom.getChildren().add(submit);
            StackPane.setAlignment(submit,Pos.BOTTOM_RIGHT);
            StackPane.setAlignment(bt1,Pos.BOTTOM_LEFT);
            submit.setOnAction(actionEvent -> {
                if(tg.getSelectedToggle().isSelected())
                    handleJoinRoom((RadioButton)tg.getSelectedToggle());
            });
        }
        primaryStage.setScene(chooseR);
        primaryStage.show();
    }
    //TODO mettere javaDoc
    private void handleJoinRoom(RadioButton rb){
        int id=0;
        if(rb.getId().equals("1"))
            id = 1;
        if(rb.getId().equals("2"))
            id = 2;
        if(rb.getId().equals("3"))
            id = 3;
        if(rb.getId().equals("4"))
            id = 4;
        System.out.println("id"+id+" user:"+user);
        controller.getClient().sendMessage(new JoinWaitingRoomRequest(id, user));
    }

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
        this.availableMaps = availableMaps;
        controller.getClient().sendMessage(new GetWaitingRoomsRequest());
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
    public void usePlusInOrder(List<FullEffect> plusEffects) {

    }

    @Override
    public void choosePlusEffect(List<FullEffect> plusEffects) {

    }

    @Override
    public void showRanking(SortedMap<Player, Integer> ranking) {

    }

    @Override
    public void rejoinGamePhase(List<String> otherPlayers) {
       /* char choice;
        boolean rejoin;
        writeText("Hi " + ClientContext.get().getUser() + ", welcome back! We have found on the server a game which is currently running where you where previously playing. This are the players actually connected in this game:");
        otherPlayers.forEach(this::writeText);
        do{
            writeText("Do you want to rejoin this game? [Y/N]:");
            choice = readChar();
        }while(choice != 'Y' && choice != 'y' && choice != 'n' && choice != 'N');

        rejoin = choice == 'Y' || choice == 'y';
        controller.getClient().sendMessage(new RejoinGameResponse(rejoin,ClientContext.get().getUser()));
        */
    }

    @Override
    public void notifyPlayerSuspended(Player p) {

    }

    @Override
    public void timeOutPhase() {

    }

    @Override
    public void alreadyLoggedPhase() {
        StackPane sp = new StackPane();
        Label text = new Label("User name already used!");
        Button btn = new Button("relog");
        sp.getChildren().addAll(btn,text);
        StackPane.setAlignment(text,Pos.CENTER);
        StackPane.setAlignment(btn,Pos.BOTTOM_RIGHT);
        Scene scene = new Scene(sp);
        primaryStage.setScene(scene);
        btn.setOnAction(ac ->{setUserNamePhase();});
    }

    @Override
    public void loginCompletedPhase() {
        System.out.println("Login Complete");
        controller.getClient().sendMessage(new GetAvailableMapsRequest());
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
    private ImageView createImageViewAmmo(CardAmmo ca ){
        String card = "";
        if(ca.getCardPower()==1)
            card = card + "P";
        for(game.model.Color c : ca.getAmmo()){
            if(c != null) {
                card = card + c.toString().substring(6,7);
            }
        }
        Image ammoI = new Image ("graphics/ammo/"+card);
        ImageView ammoIV = new ImageView(ammoI);
        ammoIV.setId("");
        ammoIV.setFitWidth(screenWidth*2.4/100);
        ammoIV.setPreserveRatio(true);
        return ammoIV;
    }
    private void showMapGame(){
        ImageView mapIV;
        StackPane map = new StackPane();


        Image mapI = new Image("graphics/cards/mappa"+ClientContext.get().getMap().getId()+".png");
        mapIV = new ImageView(mapI);
        mapIV.setFitWidth(screenWidth*55/100);
        mapIV.setPreserveRatio(true);

        //stampa rettangoli invisibili e ammo
        List<Rectangle> squares = new ArrayList<>();
        List<ImageView> ammos = new ArrayList<>();
        double spaceX = 0;
        double spaceY = 0;
        for(int i = 1; i < 13; i++){
            Rectangle rec = new Rectangle(130,130,Color.TRANSPARENT);
            rec.setId(""+i);
            squares.add(rec);
            ImageView ammoIV = createImageViewAmmo(ClientContext.get().getMap().getGrid()[(i-1)/4][(i-1)%4].getCardAmmo());
            ammos.add(ammoIV);
            map.getChildren().add(rec);
            map.getChildren().add(ammoIV);
            StackPane.setAlignment(rec,Pos.TOP_LEFT);
            StackPane.setMargin(rec,new Insets(screenHeight*19/100 + spaceY,0,0,screenWidth*11/100 + spaceX));
            StackPane.setAlignment(ammoIV,Pos.TOP_LEFT);
            StackPane.setMargin(ammoIV,new Insets(screenHeight*19/100 + spaceY,0,0,screenWidth*11/100 + spaceX));
            rec.setOnMouseClicked(this :: handleSquareClick);
            if(i % 4 == 0 && i != 0){
                spaceX = 0;
                spaceY = spaceY + screenHeight*18/100;
            }else{
                spaceX = spaceX + screenWidth*9.5/100;
            }
        }
    }

    private void showMap(){
        int mapId = 1;

        Image image = new Image("graphics/map/mappa"+mapId+".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(screenWidth*55/100);
        imageView.setPreserveRatio(true);
        Image image2 = new Image("graphics/cards/AD_weapons_IT_024.png");
        Image image7 = new Image("graphics/cards/AD_powerups_IT_024.png");
        ImageView im2 = new ImageView(image2);
        im2.setId("Distruttore");
        ImageView im3 = new ImageView(image2);
        im3.setId("THOR");
        ImageView im4 = new ImageView(image2);
        im4.setId("Spada Laser");
        ImageView im5 = new ImageView(image7);
        im5.setId("Machine Gun");
        ImageView im6 = new ImageView(image7);
        im6.setId("Bomba");
        ImageView im7 = new ImageView(image7);
        im7.setId("Launch Granede");
        Image imdg = new Image("graphics/map/BluDash.png");
        Image myp = new Image("graphics/map/YellowDash.png");
        ImageView imd1 = new ImageView(imdg);
        imd1.setId("Player 2");
        ImageView imd2 = new ImageView(imdg);
        imd2.setId("Player 3");
        ImageView imd3 = new ImageView(imdg);
        imd3.setId("Player 4");
        ImageView imd4 = new ImageView(imdg);
        imd4.setId("Player 5");
        ImageView imd5 = new ImageView(myp);
        imd5.setId("MyPlayer");

        imd5.setOnMouseClicked(e -> {
            System.out.println(((ImageView)e.getSource()).getId());
        });


        text.setStyle("-fx-font: 20px Tahoma;");
        text.setTextFill(Color.WHITE);
        text.setPrefWidth(screenWidth*52.5/100);
        text.setMaxHeight(screenHeight*3.25/100);
        text.setWrapText(true);




        StackPane map = new StackPane();
        map.setPrefSize(screenWidth,screenHeight);
        map.getChildren().addAll(imageView,im2,im3,im4,im5,im6,im7,imd1,imd2,imd3,imd4,imd5,text);

        List<ImageView> im = new ArrayList<>(Arrays.asList(im2,im3,im4,im5,im6,im7));
        List<ImageView> imd = new ArrayList<>(Arrays.asList(imd1,imd2,imd3,imd4,imd5));

        List<ImageView> mapWR = new ArrayList<>(Arrays.asList(new ImageView(image2),new ImageView(image2),new ImageView(image2)));
        List<ImageView> mapWT = new ArrayList<>(Arrays.asList(new ImageView(image2),new ImageView(image2),new ImageView(image2)));
        List<ImageView> mapWL = new ArrayList<>(Arrays.asList(new ImageView(image2),new ImageView(image2),new ImageView(image2)));

        int i = 0;
        for(ImageView imw : im){
            imw.setFitWidth(screenWidth*6.9/100);
            imw.setPreserveRatio(true);
            StackPane.setAlignment(imw,Pos.BOTTOM_RIGHT);
            StackPane.setMargin(imw,new Insets(0,i,0,0));
            i = i + (int)(screenHeight*14/100);
            imw.setOnMouseClicked(this::handleWeaponClick);
        }

        i = 0;
        for(ImageView imw : imd){
            imw.setFitWidth(screenWidth*36.6/100);
            imw.setPreserveRatio(true);
            StackPane.setAlignment(imw,Pos.TOP_RIGHT);
            StackPane.setMargin(imw,new Insets(i,0,0,0));
            i = i + (int)(screenHeight*18.6/100);
            imw.setOnMouseClicked(this::handleWeaponClick);
            //javafx.scene.effect.
        }
        double spaceX = 0;
        for(ImageView weapon : mapWL){
            map.getChildren().add(weapon);
            weapon.setFitWidth(screenWidth*5.12/100);
            weapon.setId("1");
            weapon.setPreserveRatio(true);
            StackPane.setAlignment(weapon,Pos.CENTER_LEFT);
            weapon.setRotate(270);
            StackPane.setMargin(weapon,new Insets(screenHeight*12/100 + spaceX,0,0,screenWidth*1.83/100));
            weapon.setOnMouseClicked(this::handleWeaponClick);
            weapon.setOnMouseEntered(this::handleMouseOnWeapon);
            weapon.setOnMouseExited(this::handleMouseOutWeapon);
            spaceX = spaceX - screenHeight*22.4/100;
            //weapon.setEffect(new DropShadow(20,Color.GREEN));
        }
        double spaceT = 0;
        for(ImageView weapon : mapWT){
            map.getChildren().add(weapon);
            weapon.setFitWidth(screenWidth*5.12/100);
            weapon.setId("2");
            weapon.setPreserveRatio(true);
            StackPane.setAlignment(weapon,Pos.TOP_CENTER);
            StackPane.setMargin(weapon,new Insets(0,screenWidth*34/100 + spaceT,0,0));
            weapon.setOnMouseClicked(this::handleWeaponClick);
            weapon.setOnMouseEntered(this::handleMouseOnWeapon);
            weapon.setOnMouseExited(this::handleMouseOutWeapon);
            spaceT = spaceT - screenHeight*20/100;
            //weapon.setEffect(new DropShadow(20,Color.GREEN));
        }

        double spaceR = 0;
        for(ImageView weapon : mapWR){
            map.getChildren().add(weapon);
            weapon.setFitWidth(screenWidth*5.12/100);
            weapon.setId("3");
            weapon.setPreserveRatio(true);
            StackPane.setAlignment(weapon,Pos.CENTER);
            weapon.setRotate(90);
            StackPane.setMargin(weapon,new Insets(screenHeight*43/100 + spaceR,0,0,screenWidth*4/100));
            weapon.setOnMouseClicked(this::handleWeaponClick);
            weapon.setOnMouseEntered(this::handleMouseOnWeapon);
            weapon.setOnMouseExited(this::handleMouseOutWeapon);
            spaceR = spaceR - screenHeight*22.4/100;
            //weapon.setEffect(new DropShadow(20,Color.GREEN));
        }



        //stampa rettangoli invisibili e ammo
        List<Rectangle> squares = new ArrayList<>();
        List<ImageView> ammos = new ArrayList<>();
        spaceX = 0;
        double spaceY = 0;
        image = new Image("graphics/ammo/BRR.png");
        for(i = 1; i < 13; i++){
            Rectangle rec = new Rectangle(130,130,Color.TRANSPARENT);
            rec.setId(""+i);
            squares.add(rec);
            ImageView am = new ImageView(image);
            am.setId(""+i);
            ammos.add(am);
            am.setFitWidth(screenWidth*2.4/100);
            am.setPreserveRatio(true);
            map.getChildren().add(rec);
            map.getChildren().add(am);
            StackPane.setAlignment(rec,Pos.TOP_LEFT);
            StackPane.setMargin(rec,new Insets(screenHeight*19/100 + spaceY,0,0,screenWidth*11/100 + spaceX));
            StackPane.setAlignment(am,Pos.TOP_LEFT);
            StackPane.setMargin(am,new Insets(screenHeight*19/100 + spaceY,0,0,screenWidth*11/100 + spaceX));
            rec.setOnMouseClicked(this :: handleSquareClick);
            if(i % 4 == 0 && i != 0){
                spaceX = 0;
                spaceY = spaceY + screenHeight*18/100;
            }else{
                spaceX = spaceX + screenWidth*9.5/100;
            }
        }/*
        GridPane gp = new GridPane();
        gp.setMaxSize(500,500);
        gp.setViewOrder(9);
        gp.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        Button p = new Button("daiwj");
        p.setOnMouseClicked(mouseEvent -> gp.setViewOrder(9));
        gp.getChildren().add(p);
        map.getChildren().add(gp);
        StackPane.setAlignment(gp,Pos.TOP_LEFT);*/
        //player stamp
        List<Circle> players = new ArrayList<>();
        spaceX = 0;
        spaceY = 0;
        for(i = 1; i< 13 ; i++) {
            for (int j = 0; j < 5; j++) {
                Circle c = new Circle(9, Color.GREENYELLOW);
                c.setStroke(Color.BLACK);
                players.add(c);
                map.getChildren().add(c);
                c.setId("hwllp");
                StackPane.setAlignment(c,Pos.TOP_LEFT);
                StackPane.setMargin(c, new Insets(screenHeight * 29 / 100 + spaceY, 0, 0, screenWidth * 11 / 100 + spaceX + j * 20));
                c.setOnMouseClicked(mouseEvent -> {
                    primaryStage.setAlwaysOnTop(false);
                    Stage sg = new Stage();
                    StackPane sp = new StackPane();
                    sp.setPrefSize(500,500);
                    Button b = new Button("alwdiwd");
                    sp.getChildren().add(b);
                    StackPane.setAlignment(b,Pos.CENTER);
                    Scene sc = new Scene(sp);
                    sg.setAlwaysOnTop(true);
                    sg.setScene(sc);
                    sg.show();
                });
                //c.setOnMouseClicked(this::handlePlayerClick);
            }
            if (i % 4 == 0 && i != 0) {
                spaceY = spaceY + screenHeight * 18 / 100;
                spaceX = 0;
            } else {
                spaceX = spaceX + screenWidth * 9.5 / 100;

            }
        }
        Image background = new Image("graphics/map/background.png");
        BackgroundImage bi = new BackgroundImage(background,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        map.setBackground(new Background(bi));

        StackPane.setAlignment(text,Pos.BOTTOM_LEFT);
        StackPane.setMargin(text,new Insets(0,0,(screenHeight/(displayY/130)),0));
        StackPane.setAlignment(imd5,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(imageView,Pos.TOP_LEFT);
        Scene scene = new Scene(map);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }
    private void handleMouseOnWeapon(MouseEvent e){
        ((ImageView)e.getSource()).setViewOrder(-1);
        ((ImageView)e.getSource()).setRotate(0);
        ((ImageView)e.getSource()).setFitWidth(screenWidth*12/100);
        ((ImageView)e.getSource()).setPreserveRatio(true);
    }
    private void handleMouseOutWeapon(MouseEvent e){
        ((ImageView)e.getSource()).setViewOrder(0);
        switch (((ImageView)e.getSource()).getId()){
            case "1":
                ((ImageView)e.getSource()).setRotate(270);
                break;
            case "2":
                ((ImageView)e.getSource()).setRotate(0);
                break;
            case "3":
                ((ImageView)e.getSource()).setRotate(90);
                break;
        }
        ((ImageView)e.getSource()).setFitWidth(screenWidth*5.12/100);
        ((ImageView)e.getSource()).setPreserveRatio(true);
    }
}
