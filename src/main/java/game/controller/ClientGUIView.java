package game.controller;

import game.LaunchClient;
import game.controller.commands.clientcommands.*;
import game.model.*;
import game.model.effects.FullEffect;
import game.model.effects.SimpleEffect;
import game.model.exceptions.MapOutOfLimitException;
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

import java.io.IOException;
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
    private List<GameMap> availableMaps;


    private StackPane map = new StackPane();
    private Scene scene;
    private List<Rectangle> squares = new ArrayList<>();
    private List<Rectangle> myAmmo = new ArrayList<>();
    private List<ImageView> ammos = new ArrayList<>();
    private List<Circle> players = new ArrayList<>();
    private List<ImageView> playerDashBoard = new ArrayList<>();
    private List<ImageView> powerUp = new ArrayList<>();
    private List<ImageView> weapons = new ArrayList<>();
    private List<ImageView> mapWL = new ArrayList<>();
    private List<ImageView> mapWR = new ArrayList<>();
    private List<ImageView> mapWT = new ArrayList<>();
    private List<List<ImageView>> playerDamage = new ArrayList<>();
    private List<List<ImageView>> playerMarks = new ArrayList<>();
    private List<ImageView> myPlayerDamage = new ArrayList<>();
    private List<ImageView> myPlayerMarks = new ArrayList<>();
    private Label text = new Label("");

    //TODO esiste?
    private List<ImageView> mapWB;
    public ClientGUIView() {
        GUI = this;
    }

    public static ClientGUIView getInstance(){
        if(GUI!=null){
            return GUI;
        }
        else{
            (new Thread(() -> launch())).start();
            while(GUI == null)  //ensures that the gui has been launched before proceeding
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return GUI;
    }

    public void setController(ClientController controller){
        this.controller = controller;
    }

    @Override
    public void waitStart() {
        //TODO (?)
    }

    @Override
    public void chooseConnection() {

        GridPane grid = new GridPane();
        Scene sc = new Scene(grid);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label text = new Label("Choose the connection type:");
        Button b1 = new Button("RMI");
        Button b2 = new Button("Socket");

        TextField ipTextField = new TextField();
        ipTextField.setPromptText("Server IP");

        //sp.getChildren().addAll(text,b1,b2,ipTextField);
        StackPane.setAlignment(text,Pos.TOP_CENTER);
        StackPane.setAlignment(b1,Pos.CENTER);
        StackPane.setAlignment(b2,Pos.CENTER);
        StackPane.setMargin(b1,new Insets(0,150,0,0));
        StackPane.setMargin(b2,new Insets(0,0,0,150));

        primaryStage.setScene(sc);
        primaryStage.show();
        b1.setOnAction(actionEvent -> LaunchClient.startConnection("RMI"));
        b2.setOnAction(actionEvent -> LaunchClient.startConnection("SOCKET"));
        /*
        String choice;
        Scanner in = new Scanner(System.in);
        System.out.println("Choose the connection type");
        System.out.println("Insert Socket or RMI:");
        do{
            choice = in.nextLine();
            choice = choice.toUpperCase();
        }while(!choice.equals("RMI") && !choice.equals("SOCKET"));
        return choice;
         */
    }

    @Override
    public void notifyConnectionError() {
        StackPane sp = new StackPane();
        Scene scene = new Scene(sp);
        Text text = new Text("Connection Lost:");
        Button btn = new Button("Retry");
        Button bt1 = new Button("Close");
        sp.getChildren().addAll(btn,bt1, text);
        StackPane.setAlignment(btn,Pos.CENTER);
        StackPane.setAlignment(bt1,Pos.CENTER);
        StackPane.setAlignment(text,Pos.TOP_CENTER);
        StackPane.setMargin(text, new Insets(100,0,0,0));
        StackPane.setMargin(bt1, new Insets(0,0,0,100));
        StackPane.setMargin(btn, new Insets(0,100,0,0));
        bt1.setOnAction(actionEvent -> this.setUserNamePhase());
        bt1.setOnAction(actionEvent -> primaryStage.close());
        primaryStage.setScene(scene);
        primaryStage.setHeight(300);
        primaryStage.setWidth(500);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showMap();
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
        controller.getClient().sendMessage(new CreateWaitingRoomRequest(mapId,nPlayer,user));
    }

    @Override
    public void notifyStart() {
        System.out.println("ALFIH");
        showMapGame();
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

    /**
     * Create a Ammo card from a Ammo in map, build a String with initials of Color
     * @param i
     * @return
     */
    private Image createAmmoCard(int i) {
        String card = "";
        int x = 0;
        int y = 0;
        CardAmmo ca;
        Image ammoI = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/ammo/Empty.png"));    // if the square is empty
        if(i ==  1){
            x = 0;
            y = 0;
        }else {
            x = (i - 1) % 4;
            y = (i - 1) / 4;
        }
        try {
            if(ClientContext.get().getMap().getGrid()[y][x] != null) {
                if(!ClientContext.get().getMap().getSquare(x,y).isRespawn()){
                    ca = ClientContext.get().getMap().getSquare(x,y).getCardAmmo();
                    if (ca.getCardPower() == 1) {
                        card = card + "P";
                    }
                    for (game.model.Color c : ca.getAmmo()) {
                        if (c != null) {
                            card = card + c.toString().substring(0, 1);
                        }
                    }
                    System.out.println("adwd"+card);
                    ammoI = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/ammo/"+card+".png"));
                }
            }
        } catch (MapOutOfLimitException e) {
            e.printStackTrace();
        }
        return ammoI;
    }

    /**
     * create ImageView with the relative ammo inside from a Square position in the map
     * @param i
     * @return
     */
    private ImageView createImageViewAmmo(int i ){
        ImageView ammoIV = null;
        ammoIV = new ImageView(createAmmoCard(i));
        ammoIV.setId(""+i);
        ammoIV.setFitWidth(screenWidth*2.4/100);
        ammoIV.setPreserveRatio(true);
        return ammoIV;
    }

    /**
     * Create the relative Paint color from a Player color
     * @param color
     * @return
     */
    private Color createPlayerColor(PlayerColor color){
        if(color.equals(PlayerColor.YELLOW))return Color.YELLOW;
        if(color.equals(PlayerColor.GREEN))return Color.GREEN;
        if(color.equals(PlayerColor.BLUE))return Color.BLUE;
        if(color.equals(PlayerColor.PURPLE))return Color.PURPLE;
        if(color.equals(PlayerColor.GREY))return Color.RED;
        return Color.TRANSPARENT;
    }

    /**
     * Reposition the player Circle in the right position
     */
    private void refreshPlayerPosition(){
        double spaceX = 0;
        double spaceY = 0;
        for(int i = 1; i< 13 ; i++) {
            if(ClientContext.get().getMap().getGrid()[(i-1)/4][(i-1)%4] != null) {
                for (Player pl : ClientContext.get().getMap().getGrid()[(i-1)/4][(i-1)%4].getPlayers()) {
                    int j = 0;
                    Circle p = players.stream().filter(player -> pl.getNickName().equals(player.getId())).findFirst().orElse(null);
                    if(p == null)
                        System.out.println("Player not found");
                    StackPane.setAlignment(p,Pos.TOP_LEFT);
                    StackPane.setMargin(p, new Insets(screenHeight * 29 / 100 + spaceY, 0, 0, screenWidth * 11 / 100 + spaceX + j * 20));
                }
                if (i % 4 == 0 && i != 0) {
                    spaceY = spaceY + screenHeight * 18 / 100;
                    spaceX = 0;
                } else {
                    spaceX = spaceX + screenWidth * 9.5 / 100;
                }
            }
        }
    }

    /**
     * create a text view in the map
     */
    private void createTextNotification(){
        text.setStyle("-fx-font: 20px Tahoma;");
        text.setTextFill(Color.WHITE);
        text.setPrefWidth(screenWidth*52.5/100);
        text.setMaxHeight(screenHeight*3.25/100);
        text.setWrapText(true);
    }

    /**
     * Create my player Power-up and Weapon card position in the screen
     * Set the back of the card from default
     */
    private void createMyPlayerCard(){
        Image w = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/W_back.png"));
        Image p = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/PW_back.png"));
        ImageView pu = new ImageView(p);
        ImageView we = new ImageView(w);
        powerUp = new ArrayList<>(Arrays.asList(pu,pu,pu));
        weapons = new ArrayList<>(Arrays.asList(we,we,we));
        int i = 0;
        for(ImageView imw : weapons){
            imw.setFitWidth(screenWidth*6.9/100);
            imw.setPreserveRatio(true);
            StackPane.setAlignment(imw,Pos.BOTTOM_RIGHT);
            StackPane.setMargin(imw,new Insets(0,i,0,0));
            i = i + (int)(screenHeight*14/100);
            imw.setOnMouseClicked(this::handleWeaponClick);
        }

        for(ImageView imw : powerUp){
            imw.setFitWidth(screenWidth*6.9/100);
            imw.setPreserveRatio(true);
            StackPane.setAlignment(imw,Pos.BOTTOM_RIGHT);
            StackPane.setMargin(imw,new Insets(0,i,0,0));
            i = i + (int)(screenHeight*14/100);
            imw.setOnMouseClicked(this::handleWeaponClick);
        }
    }

    /**
     * Create a power up from his name and color (name_color initial)
     * @param name
     * @param color
     * @return
     */
    private String createPowerUpCard(String name, game.model.Color color){
        System.out.println(""+name+"_"+color.toString().substring(0,1)+".png");
        return ""+name+"_"+color.toString().substring(0,1)+".png";
    }

    /**
     * Return the weapon picture name from his Id
     * @param i
     * @return
     */
    private String createWeaponCard(int i){
        return "W_"+ClientContext.get().getMyPlayer().getCardPower().get(i).getId()+".png";
    }

    /**
     * create the map space with the relative square, ammo space and weapon to grab
     */
    private void createMapSquareAmmo(){
        ImageView mapIV;


        Image mapI = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/mappa"+ClientContext.get().getMap().getId()+".png"));
        mapIV = new ImageView(mapI);
        mapIV.setFitWidth(screenWidth*55/100);
        mapIV.setPreserveRatio(true);

        //stampa rettangoli invisibili e ammo
        double spaceX = 0;
        double spaceY = 0;
        for(int i = 1; i < 13; i++){
            Rectangle rec = new Rectangle(130,130,Color.TRANSPARENT);
            rec.setId(""+i);
            squares.add(rec);
            ImageView ammoIV = createImageViewAmmo(i);
            ammos.add(ammoIV);
            map.getChildren().add(rec);
            map.getChildren().add(ammoIV);
            StackPane.setAlignment(rec,Pos.TOP_LEFT);
            StackPane.setMargin(rec,new Insets(screenHeight*19/100 + spaceY,0,0,screenWidth*11/100 + spaceX));
            StackPane.setAlignment(ammoIV,Pos.TOP_LEFT);
            StackPane.setMargin(ammoIV,new Insets(screenHeight*19/100 + spaceY,0,0,screenWidth*11/100 + spaceX));
            rec.setOnMouseClicked(this :: handleSquareClick);
            rec.setDisable(true);
            if(i % 4 == 0 && i != 0){
                spaceX = 0;
                spaceY = spaceY + screenHeight*18/100;
            }else{
                spaceX = spaceX + screenWidth*9.5/100;
            }
        }
        //print all the back of the weapon card in the map
        Image back = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/W_back.png"));
        spaceX = 0;
        for(int k = 0 ; k < 3 ; k ++){
            ImageView weapon = new ImageView(back);
            mapWL.add(weapon);
            map.getChildren().add(weapon);
            weapon.setFitWidth(screenWidth*5.12/100);
            weapon.setId("0");
            weapon.setPreserveRatio(true);
            StackPane.setAlignment(weapon,Pos.CENTER_LEFT);
            weapon.setRotate(270);
            StackPane.setMargin(weapon,new Insets(screenHeight*12/100 + spaceX,0,0,screenWidth*1.83/100));
            weapon.setOnMouseEntered(this::handleMouseOnWeapon);
            weapon.setOnMouseExited(this::handleMouseOutWeapon);
            spaceX = spaceX - screenHeight*22.4/100;
        }
        double spaceT = 0;
        for(int k = 0; k < 3; k++){
            ImageView weapon = new ImageView(back);
            mapWT.add(weapon);
            map.getChildren().add(weapon);
            weapon.setFitWidth(screenWidth*5.12/100);
            weapon.setId("0");
            weapon.setPreserveRatio(true);
            StackPane.setAlignment(weapon,Pos.TOP_CENTER);
            StackPane.setMargin(weapon,new Insets(0,screenWidth*34/100 + spaceT,0,0));
            weapon.setOnMouseEntered(this::handleMouseOnWeapon);
            weapon.setOnMouseExited(this::handleMouseOutWeapon);
            spaceT = spaceT - screenHeight*20/100;
        }

        double spaceR = 0;
        for(int k = 0; k < 3 ; k++){
            ImageView weapon = new ImageView(back);
            mapWR.add(weapon);
            map.getChildren().add(weapon);
            weapon.setFitWidth(screenWidth*5.12/100);
            weapon.setId("0");
            weapon.setPreserveRatio(true);
            StackPane.setAlignment(weapon,Pos.CENTER);
            weapon.setRotate(90);
            StackPane.setMargin(weapon,new Insets(screenHeight*43/100 + spaceR,0,0,screenWidth*4/100));
            weapon.setOnMouseEntered(this::handleMouseOnWeapon);
            weapon.setOnMouseExited(this::handleMouseOutWeapon);
            spaceR = spaceR - screenHeight*22.4/100;
            //weapon.setEffect(new DropShadow(20,Color.GREEN));
        }
    }

    /**
     * Refresh my player power up and weapon
     */
    private void refreshMyPlayerCard(){
        Image cardP;
        Image cardW;
        Player myP = ClientContext.get().getMyPlayer();
        for(int i = 0; i < 3 ; i++) {
            if (i >= myP.getCardPower().size()) {
                cardP = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/PW_back.png"));
                powerUp.get(i).setId("0");
            } else {
                cardP = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/"+createPowerUpCard(myP.getCardPower().get(i).getName(), myP.getCardPower().get(i).getColor())));
                powerUp.get(i).setId("" + myP.getCardPower().get(i).getId());
            }
            if (i >= myP.getWeapons().size()){
                cardW = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/W_back.png"));
                weapons.get(i).setId("0");
            }
            else {
                cardW = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/"+createWeaponCard(myP.getCardPower().get(i).getId())));
                weapons.get(i).setId("" + myP.getWeapons().get(i).getId());
            }
            powerUp.get(i).setImage(cardP);
            weapons.get(i).setImage(cardW);
        }
    }

    /**
     * create player circle out of the map, so the can be moved in when they spawned
     */
    private void createPlayerPosition(){
        //stampa player fuori dalla mappa, per poi spostarli dove serve
        for(Player pl : ClientContext.get().getPlayersInWaiting()){
            Circle c = new Circle(9, createPlayerColor(pl.getColor()));
            c.setStroke(Color.BLACK);
            players.add(c);
            map.getChildren().add(c);
            c.setId(pl.getNickName());
            StackPane.setAlignment(c,Pos.TOP_LEFT);
            StackPane.setMargin(c, new Insets(0,0,1000,0));
            c.setOnMouseClicked(this::handlePlayerClick);
            c.setDisable(true);
        }
    }

    /**
     * refreah the ammo in the map
     */
    private void refreshAmmoCard(){
        for(ImageView iv : ammos){
            int i = Integer.parseInt(iv.getId());
            iv.setImage(createAmmoCard(i));
        }
    }

    /**
     * Add other plaeyer dash board in the screen
     */
    private void createPlayerDashBoard(){
        int i = 0;
        for(Player p : ClientContext.get().getPlayersInWaiting()){
            System.out.println(p.getColor().name());
            Image dash = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/"+p.getColor().toString()+"Dash.png"));
            ImageView imw = new ImageView(dash);
            playerDashBoard.add(imw);
            imw.setId(""+p.getId());
            imw.setFitWidth(screenWidth*36.6/100);
            imw.setPreserveRatio(true);
            if(p.equals(ClientContext.get().getMyPlayer())){
                StackPane.setAlignment(imw,Pos.BOTTOM_LEFT);
            }else {
                StackPane.setAlignment(imw,Pos.TOP_RIGHT);
                StackPane.setMargin(imw,new Insets(i,0,0,0));
                i = i + (int)(screenHeight*18.6/100);
            }
        }
    }

    /**
     * refresh other player damage and marks tear simbol
     */
    private void refreshPlayerDamage(){
        double spaceY = 0;
        deletePlayerInfo();
        for(ImageView dash : playerDashBoard) {
            int j = 0;
            double spaceX = 0;
            List<ImageView> infoD = new ArrayList<>();
            List<ImageView> infoM = new ArrayList<>();
            Player p = ClientContext.get().getMap().getPlayerById(Integer.parseInt(dash.getId()));
            for (PlayerColor  c :p.getDamage()) {
                System.out.println(c.name());
                Image damage = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/"+c.toString()+"Tear.png"));
                ImageView damages = new ImageView(damage);
                infoD.add(damages);
                damages.setFitHeight(screenHeight * 3 / 100);
                damages.setPreserveRatio(true);
                map.getChildren().add(damages);
                StackPane.setAlignment(damages, Pos.TOP_RIGHT);
                StackPane.setMargin(damages, new Insets(screenHeight * 6.5 / 100 + spaceY, screenWidth * 32.1 / 100 - spaceX, 0, 0));
                spaceX = spaceX + screenWidth * 2.25 / 100;
                if (j > 1 && j != 4)
                    spaceX = spaceX - screenWidth * 0.25 / 100;
                j++;
            }
            spaceX = 0;
            for (PlayerColor  c :p.getMark()) {
                Image mark = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/"+c.toString()+"Tear.png"));
                ImageView marks = new ImageView(mark);
                infoM.add(marks);
                marks.setFitHeight(screenHeight * 2 / 100);
                marks.setPreserveRatio(true);
                map.getChildren().add(marks);
                StackPane.setAlignment(marks, Pos.TOP_RIGHT);
                StackPane.setMargin(marks, new Insets(screenHeight * 0.4 / 100 + spaceY, screenWidth * 18 / 100 - spaceX, 0, 0));
                spaceX = spaceX + screenWidth * 1 / 100;
            }
            spaceY = spaceY + screenHeight * 18.5 / 100;
            playerDamage.add(infoD);
            playerMarks.add(infoM);
        }
    }

    /**
     * delete all tears from other player and clea the Imageview list
     */
    private void deletePlayerInfo(){
        for(List<ImageView> p : playerDamage){
            for(ImageView v : p ){
                map.getChildren().remove(v);
                v.setImage(null);
            }
        }
        for(List<ImageView> p : playerMarks){
            for(ImageView v : p ){
                map.getChildren().remove(v);
                v.setImage(null);
            }
        }
        playerDamage.clear();
        playerMarks.clear();
    }

    /**
     * refresh  my player damage and marks
     */
    private void refreshMyPlayerDamage(){
        double spaceX = 0;
        int j = 0;
        deleteMyPlayerDamage();
        for(PlayerColor p : ClientContext.get().getMyPlayer().getDamage()){
            System.out.println(p.name());
            Image damage = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/"+p.toString()+"Tear.png"));
            ImageView damages = new ImageView(damage);
            myPlayerDamage.add(damages);
            damages.setFitHeight(screenHeight * 3 /100);
            damages.setPreserveRatio(true);
            map.getChildren().add(damages);
            StackPane.setAlignment(damages,Pos.BOTTOM_LEFT);
            StackPane.setMargin(damages,new Insets(0,0,screenHeight * 6.25 / 100,screenWidth * 3.44 / 100 + spaceX));
            spaceX = spaceX + screenWidth * 2.25 / 100;
            if(j > 1 && j != 4)
                spaceX = spaceX - screenWidth * 0.25 / 100;
            j++;
        }
        spaceX = 0;
        for(PlayerColor p : ClientContext.get().getMyPlayer().getMark()){
            Image damage = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/"+p.toString()+"Tear.png"));
            ImageView damages = new ImageView(damage);
            myPlayerMarks.add(damages);
            damages.setFitHeight(screenHeight * 2 /100);
            damages.setPreserveRatio(true);
            map.getChildren().add(damages);
            StackPane.setAlignment(damages,Pos.BOTTOM_LEFT);
            StackPane.setMargin(damages,new Insets(0,0,screenHeight * 13.5 / 100,screenWidth * 17.6 / 100 + spaceX));
            spaceX = spaceX + screenWidth * 1 / 100;
        }
    }

    /**
     * deòlete my player damage and marks
     */
    private void deleteMyPlayerDamage(){
        for(ImageView i : myPlayerDamage){
            map.getChildren().remove(i);
            i.setImage(null);
        }
        for(ImageView i : myPlayerMarks){
            map.getChildren().remove(i);
            i.setImage(null);
        }
        myPlayerDamage.clear();
        myPlayerMarks.clear();
    }

    /**
     * refresh ny player ammo
     */
    private void refreshMyPlayerAmmo(){
        //ammo
        deleteMyPlayerAmmo();
        double spaceX = 0;
        double spaceY = 0;
        int k = 1;
        for(game.model.Color c :ClientContext.get().getMyPlayer().getAmmo()){
            System.out.println(c.name());
            Rectangle ammo = new Rectangle(screenHeight * 2 /100,screenHeight * 2 /100, Color.valueOf(c.toString()));
            map.getChildren().add(ammo);
            myAmmo.add(ammo);
            StackPane.setAlignment(ammo, Pos.BOTTOM_LEFT);
            StackPane.setMargin(ammo, new Insets(0, 0, screenHeight * 12 / 100 - spaceY, screenWidth * 29 / 100 + spaceX));
            spaceX = spaceX + screenWidth * 2.5 / 100;
            if(k % 3 == 0) {
                spaceY = spaceY + screenHeight * 3.5 / 100;
                spaceX = 0;
            }
            k++;
        }
    }

    /**
     * delete my player ammo
     */
    private void deleteMyPlayerAmmo(){
        for(Rectangle r : myAmmo){
            map.getChildren().remove(r);
        }
        myAmmo.clear();
    }

    private void showMapGame(){
        createMapSquareAmmo();
        refreshAmmoCard();
        createPlayerPosition();
        refreshPlayerPosition();
        createTextNotification();
        createMyPlayerCard();
        refreshMyPlayerCard();
        createPlayerDashBoard();
        refreshPlayerDamage();
        refreshMyPlayerDamage();
        refreshMyPlayerAmmo();
        scene = new Scene(map);
        primaryStage.setScene(scene);
    }


    private void showMap(){
        int mapId = 1;
        Image image = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/mappa1.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(screenWidth*55/100);
        imageView.setPreserveRatio(true);
        Image image2 = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/W_18.png"));
        Image image7 = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/Teleporter_R.png"));
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
        Image imdg = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/BluDash.png"));
        Image myp = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/YellowDash.png"));
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
        }
        StackPane.setAlignment(imd5,Pos.BOTTOM_LEFT);
        double spaceX = 0;
        for(ImageView weapon : mapWL){
            map.getChildren().add(weapon);
            weapon.setFitWidth(screenWidth*5.12/100);
            weapon.setId("1");
            weapon.setPreserveRatio(true);
            StackPane.setAlignment(weapon,Pos.CENTER_LEFT);
            weapon.setRotate(270);
            StackPane.setMargin(weapon,new Insets(screenHeight*12/100 + spaceX,0,0,screenWidth*1.83/100));
            weapon.setOnMouseClicked(null);
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
        image = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/ammo/BRR.png"));
        for(i = 1; i < 13; i++){
            Rectangle rec = new Rectangle(130,130);
            rec.setFill(Color.TRANSPARENT);
            rec.setId(""+i);
            rec.setStroke(Color.GREEN);
            rec.setEffect(new DropShadow(40,Color.GREEN));
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
        }

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

        //Stampa gocce Kill
        spaceY = 0;
        for(int j = 0; j < 8; j++){
            Image kill = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/YellowTear.png"));
            ImageView kills = new ImageView(kill);
            kills.setId(""+i);
            kills.setFitHeight(screenHeight * 3 /100);
            kills.setPreserveRatio(true);
            map.getChildren().add(kills);
            StackPane.setAlignment(kills,Pos.TOP_LEFT);
            StackPane.setMargin(kills,new Insets(screenHeight * 5 / 100,0,0,screenWidth * 4.8 / 100 + spaceY));
            spaceY = spaceY + screenWidth * 2.46 / 100;
        }


        //Stampa gocce My Player danno e marchio
        spaceX = 0;
        for(int j = 0; j < 12; j++){
            Image damage = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/BlueTear.png"));
            ImageView damages = new ImageView(damage);
            damages.setId(""+i);
            damages.setFitHeight(screenHeight * 3 /100);
            damages.setPreserveRatio(true);
            map.getChildren().add(damages);
            StackPane.setAlignment(damages,Pos.BOTTOM_LEFT);
            StackPane.setMargin(damages,new Insets(0,0,screenHeight * 6.25 / 100,screenWidth * 3.44 / 100 + spaceX));
            spaceX = spaceX + screenWidth * 2.25 / 100;
            if(j > 1 && j != 4)
                spaceX = spaceX - screenWidth * 0.25 / 100;
        }
        spaceX = 0;
        for(int j = 0; j < 9; j++){
            Image damage = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/PurpleTear.png"));
            ImageView damages = new ImageView(damage);
            damages.setId(""+i);
            damages.setFitHeight(screenHeight * 2 /100);
            damages.setPreserveRatio(true);
            map.getChildren().add(damages);
            StackPane.setAlignment(damages,Pos.BOTTOM_LEFT);
            StackPane.setMargin(damages,new Insets(0,0,screenHeight * 13.5 / 100,screenWidth * 17.6 / 100 + spaceX));
            spaceX = spaceX + screenWidth * 1 / 100;
        }


        //Stampa gocce other Player danno e marchio
        spaceY = 0;
        for(int k = 0; k < 4 ; k++) {
            spaceX = 0;
            for (int j = 0; j < 12; j++) {
                Image damage = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/YellowTear.png"));
                ImageView damages = new ImageView(damage);
                damages.setId("" + i);
                damages.setFitHeight(screenHeight * 3 / 100);
                damages.setPreserveRatio(true);
                map.getChildren().add(damages);
                StackPane.setAlignment(damages, Pos.TOP_RIGHT);
                StackPane.setMargin(damages, new Insets(screenHeight * 6.5 / 100 + spaceY, screenWidth * 32.1 / 100 - spaceX, 0, 0));
                spaceX = spaceX + screenWidth * 2.25 / 100;
                if (j > 1 && j != 4)
                    spaceX = spaceX - screenWidth * 0.25 / 100;
            }
            spaceX = 0;
            for (int j = 0; j < 9; j++) {
                Image damage = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/BlueTear.png"));
                ImageView damages = new ImageView(damage);
                damages.setId("" + i);
                damages.setFitHeight(screenHeight * 2 / 100);
                damages.setPreserveRatio(true);
                map.getChildren().add(damages);
                StackPane.setAlignment(damages, Pos.TOP_RIGHT);
                StackPane.setMargin(damages, new Insets(screenHeight * 0.4 / 100 + spaceY, screenWidth * 18 / 100 - spaceX, 0, 0));
                spaceX = spaceX + screenWidth * 1 / 100;
            }
            spaceY = spaceY + screenHeight * 18.5 / 100;
        }

        //ammo
        spaceX = 0;
        spaceY = 0;
        for(int j = 0; j < 3 ; j++){
            for (int k = 0;k < 3 ; k ++){
                Rectangle ammo = new Rectangle(screenHeight * 2 /100,screenHeight * 2 /100, Color.valueOf("Yellow"));
                map.getChildren().add(ammo);
                StackPane.setAlignment(ammo, Pos.BOTTOM_LEFT);
                StackPane.setMargin(ammo, new Insets(0, 0, screenHeight * 12 / 100 - spaceY, screenWidth * 29 / 100 + spaceX));
                spaceX = spaceX + screenWidth * 2.5 / 100;
            }
            spaceY = spaceY + screenHeight * 3.5 /100;
            spaceX = 0;
        }

        Image background = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/background.png"));
        BackgroundImage bi = new BackgroundImage(background,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        map.setBackground(new Background(bi));

        StackPane.setAlignment(text,Pos.BOTTOM_LEFT);
        StackPane.setMargin(text,new Insets(0,0,(screenHeight/(displayY/130)),0));
        StackPane.setAlignment(imageView,Pos.TOP_LEFT);
        Scene scene = new Scene(map);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    //TODO mettere javaDoc
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
