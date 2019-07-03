package game.controller;


import game.LaunchClient;
import game.controller.commands.ClientGameMessage;
import game.controller.commands.clientcommands.*;
import game.model.*;
import game.model.effects.FullEffect;
import game.model.exceptions.MapOutOfLimitException;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.collections.FXCollections;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sun.javafx.application.PlatformImpl.runLater;


public class ClientGUIView extends Application implements View{
    private static ClientGUIView GUI;
    private ClientController controller;
    private String user;
    private Stage primaryStage;
    private double screenWidth = Screen.getScreens().get(0).getBounds().getWidth();
    private double screenHeight = Screen.getScreens().get(0).getBounds().getHeight();
    private List<GameMap> availableMaps;


    private final String orangeB = "-fx-background-color: linear-gradient(#ffd65b, #e68400), linear-gradient(#ffef84, #f2ba44), linear-gradient(#ffea6a, #efaa22), linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%), linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0)); -fx-background-radius: 24; -fx-background-insets: 0,1,2,3,0; -fx-text-fill: #654b00; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 7 16 7 16;";

    private StackPane map = new StackPane();
    private Scene scene = new Scene(map);
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
    private List<ImageView> deathsBoard = new ArrayList<>();
    private Button move = new Button("Movement");
    private Button grab = new Button("Grab");
    private Button shoot = new Button("Shoot");
    private Button exit = new Button("Exit");
    private Button power = new Button("Power-Up");
    private Button yes = new Button("Yes");
    private Button no = new Button("No");
    private Label text = new Label("");
    private Label textNotify = new Label("");
    private ClientState state = ClientState.WAITING_TURN;
    private Label textwait;
    private Tooltip toolw = new Tooltip("Right Click for more info");
    private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    private Runnable timert = new Runnable() {
        @Override
        public void run() {
            System.out.println("TIMer");
            state = ClientState.WAITING_ACTION;
            yes.setVisible(false);
            no.setVisible(false);
            controller.getClient().sendMessage(new CounterAttackResponse());
            runLater(GUI::chooseTurnActionPhase);
        }
    };

    private Stage sg = new Stage();
    private AudioClip loginBack;

    private List<Square> possiblePositions;
    private List<CardWeapon> weaponToGrab;
    private CardWeapon weaponG;
    private CardWeapon weaponW;
    private FullEffect plusEff;                                                                                         //used for CHOOSEPLUSEFFECT and CHOOSEPBB
    private List<FullEffect> fullEffectList;                                                                            //used for CHOOSEPLUSEFFECT and CHOOSEPLUSORDER
    private List<ClientGameMessage> reloadRequests = new ArrayList<>();
    private List<CardWeapon> weaponsToReload;
    private CardWeapon weaponToReload;
    private CardPower scopeSelected;
    private char t = 'n';
    private Player shooter;
    private List<Label> points = new ArrayList<>();
    private Label myPoint;


    public ClientGUIView() {
        GUI = this;
    }

    public static ClientGUIView getInstance(){
        if(GUI!=null){
            return GUI;
        }
        else{
            (new Thread(() -> launch())).start();
            while(GUI == null || !GUI.isPrimaryStageOn())  //ensures that the gui has been launched before proceeding
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

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //showMap();
    }

    private boolean isPrimaryStageOn()
    {
        return primaryStage != null;
    }


    public static void main(String[] args) {
        launch(args);
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

        String url = getClass().getResource("/graphics/sound/login.wav").toExternalForm();
        loginBack = new AudioClip(url);
        loginBack.cycleCountProperty().setValue(Animation.INDEFINITE);
        loginBack.setVolume(0.6);
        loginBack.play();

        GridPane gp = new GridPane();
        Scene sc = new Scene(gp,500,300);
        Label text = new Label("Connection setup");
        text.setMaxWidth(Double.MAX_VALUE);
        text.setAlignment(Pos.CENTER);

        Button b1 = new Button("RMI");
        Button b2 = new Button("Socket");

        TextField ipTextField = new TextField();
        ipTextField.setText("localhost");
        Label serverIPLabel = new Label("Server IP");
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(8);
        gp.setVgap(8);
        gp.setPadding(new Insets(5, 5, 5, 5));
        gp.add(text,0,0,3,1);
        gp.add(serverIPLabel,0,1);
        gp.add(ipTextField,1,1,2,1);
        gp.add(b1,1,2);
        gp.add(b2,2,2);


        primaryStage.setScene(sc);
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.setTitle("Adrenalina Connection Setup");
        primaryStage.show();

        b1.setOnAction(actionEvent -> LaunchClient.startConnection("RMI",ipTextField.getText()));
        b2.setOnAction(actionEvent -> LaunchClient.startConnection("SOCKET",ipTextField.getText()));
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
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    @Override
    public void connectionFailed() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection error");
        alert.setHeaderText(null);
        alert.setContentText("ERROR: Unable to connect, check your connection and the server ip!");
        alert.showAndWait();
    }

    @Override
    public void chooseCounterAttack(List<CardPower> counterattack, Player shooter) {
        state = ClientState.CHOOSECOUNTER;
        this.shooter = shooter;
        text.setText("You want to use counterAttack on "+shooter.getNickName());
        yes.setVisible(true);
        no.setVisible(true);
        yes.setOnMouseClicked(mouseEvent -> {
            timer.shutdownNow();
            text.setText("Choose wich power Up you want to use");
            if(counterattack.size() > 1 ){
                activateCardPower(counterattack);
            }else {
                controller.getClient().sendMessage(new CounterAttackResponse(counterattack.get(0), shooter));
                state = ClientState.WAITING_TURN;
            }
            yes.setVisible(false);
            no.setVisible(false);
        });
        no.setOnMouseClicked(mouseEvent -> {
            timer.shutdownNow();
            controller.getClient().sendMessage(new CounterAttackResponse());
            yes.setVisible(false);
            no.setVisible(false);
            state = ClientState.WAITING_TURN;
        });
    }

    private void callRoomCreate(String s ){

        int mapId = 0;
        if(s.equals("Map 1")) mapId = 1;
        if(s.equals("Map 2")) mapId = 2;
        if(s.equals("Map 3")) mapId = 3;
        if(s.equals("Map 4")) mapId = 4;
        controller.getClient().sendMessage(new CreateWaitingRoomRequest(mapId,user));
    }

    @Override
    public void notifyStart() {
        loginBack.setVolume(0.2);
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



        Image background = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/background01.jpg"));
        BackgroundImage bi = new BackgroundImage(background,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        
        grid.setBackground(new Background(bi));
        
        Text text = new Text("WELCOME TO ADRENALINE");
        grid.add(text, 1,0);

        Label userName = new Label("User Name:");
        userName.setTextFill(Color.WHITE);
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
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        // SetOn Acntion
        btn.setOnAction(e -> {
            if(userTextField.getText() != ""){
                user = userTextField.getText();
                ClientContext.get().setUser(user);
                controller.getClient().sendMessage(new LoginMessage(ClientContext.get().getUser()));
            }
        });

    }

    @Override
    public void insufficientAmmoNotification() {
        textNotify.setText("Not enough ammo \n" + textNotify.getText());
    }

    @Override
    public void chooseStepActionPhase() {
        text.setText("Choose your single step");
        state = ClientState.CHOOSESTEP;
        power.setVisible(true);
        power.setDisable(false);
        exit.setVisible(true);
        exit.setDisable(false);
        if(this.controller.getAvailableActions().contains(Action.MOVEMENT)){
            move.setVisible(true);
            move.setDisable(false);
        }
        if(this.controller.getAvailableActions().contains(Action.GRAB)){
            grab.setVisible(true);
            grab.setDisable(false);
        }
        if(this.controller.getAvailableActions().contains(Action.SHOOT)){
            shoot.setVisible(true);
            shoot.setDisable(false);
        }
    }

    @Override
    public void chooseSquarePhase(List<Square> possiblePositions) {
        this.possiblePositions = possiblePositions;
        text.setText("Choose where you want to move");
        for(Rectangle r : squares){
            if(possiblePositions.stream().filter(p->Integer.parseInt(r.getId()) == ((p.getX() + 1) + (p.getY() * 4))).count() != 0 ){
                r.setStroke(Color.GREEN);
                r.setOnMouseClicked(this::handleSquareClick);
            }
        }
    }

    @Override
    public void chooseTargetPhase(List<Target> possibleTargets) {
        int maxE = ClientContext.get().getCurrentEffect().getMaxEnemy();
        int minE = ClientContext.get().getCurrentEffect().getMinEnemy();
        List<Target> choosenTarget = new ArrayList<>();
        List<CheckBox> check = new ArrayList<>();
        StackPane sp = new StackPane();
        Scene temp = new Scene(sp);
        Label tex = new Label("Choose between " + minE + " and " + maxE + " targets to apply your attack:");
        StackPane.setAlignment(tex,Pos.TOP_CENTER);
        StackPane.setMargin(tex,new Insets(20,0,0,0));
        sp.setPrefSize(screenWidth * 30 / 100,screenHeight * 50/ 100);
        sp.getChildren().add(tex);
        int j = 0;
        for(Target p : possibleTargets){
            CheckBox rb = new CheckBox(p.toString());
            rb.setId(""+j);
            check.add(rb);
            sp.getChildren().add(rb);
            StackPane.setAlignment(rb,Pos.TOP_LEFT);
            StackPane.setMargin(rb,new Insets(100 + j * 40,0,0,30));
            j++;
        }
        Button submit = new Button("Submit");
        sp.getChildren().add(submit);
        StackPane.setAlignment(submit,Pos.BOTTOM_RIGHT);
        StackPane.setMargin(tex,new Insets(0,10,10,0));
        submit.setOnMouseClicked(mouseEvent -> {
            for(CheckBox c : check){
                if(c.isSelected()){
                    System.out.println("Target Selected");
                    Target t = possibleTargets.get(Integer.parseInt(c.getId()));
                    choosenTarget.add(t);
                    System.out.println("Target added ");
                }
            }
            if(choosenTarget.size() <= maxE && choosenTarget.size() >= minE) {
                controller.getClient().sendMessage(new ChooseTargetResponse(choosenTarget));
                sg.close();
            }
            else{
                tex.setText("Invalid number of target (from "+minE+" to "+maxE+")");
                choosenTarget.clear();
            }
        });
        sg.setScene(temp);
        sg.show();
    }

    @Override
    public void chooseTurnActionPhase() {
        System.out.println("Turn action Phase");
        refreshMyPlayerCard();
        refreshMyPlayerAmmo();
        System.out.println(state);
        if(state.equals(ClientState.CHOOSECOUNTER)){
            text.setText("Fast, choose if you want to use counterattack!!!");
            timer.schedule(timert,5, TimeUnit.SECONDS);
        }
        else{
            text.setText("Choose your Action ");
            state = ClientState.CHOOSEACTIOIN;
            move.setVisible(true);
            move.setDisable(false);
            shoot.setVisible(true);
            shoot.setDisable(false);
            grab.setVisible(true);
            grab.setDisable(false);
            power.setVisible(true);
            power.setDisable(false);
        }
    }


    @Override
    public void invalidTargetNotification() {
        textNotify.setText("Invalid terget selected!\n" + textNotify.getText());
    }

    @Override
    public void invalidWeaponNotification() {
        textNotify.setText("Invalid weapon selected!\n" + textNotify.getText());
    }

    @Override
    public void invalidActionNotification() {
        textNotify.setText("Invalid action!\n" + textNotify.getText());
    }

    @Override
    public void insufficientNumberOfActionNotification() {
        textNotify.setText("You cannot do others actions for this turn!\n" + textNotify.getText());
    }

    @Override
    public void invalidStepNotification() {
        textNotify.setText("The step selected is not valid, you loose the action!! xd!!1!1!!\n" + textNotify.getText());
    }

    @Override
    public void maxNumberOfWeaponNotification() {
        textNotify.setText("You can't grab another weapon!\n" + textNotify.getText());
    }

    @Override
    public void damageNotification(int shooterId, int damage, int hit) {
        String url = getClass().getResource("/graphics/sound/shoot.wav").toExternalForm();
        AudioClip audio = new AudioClip(url);
        audio.play();
        String nick = ClientContext.get().getMap().getPlayerById(shooterId).getNickName();
        String hitted = ClientContext.get().getMap().getPlayerById(hit).getNickName();
        if(shooterId == ClientContext.get().getMyID()){
            textNotify.setText("You dealt damage\n" + textNotify.getText());
            refreshPlayerDamage();
        }
        else{
            if(hit == ClientContext.get().getMyID()){
                textNotify.setText(nick + " dealt damage to you\n" + textNotify.getText());
                refreshMyPlayerDamage();
            }else{
                textNotify.setText(nick + " dealt damage to "+ hitted + "\n" + textNotify.getText());
                refreshPlayerDamage();
            }
        }
    }

    @Override
    public void notifyMovement(int pId, int newX, int newY) {
        System.out.println("Notify Movement");
        String url = getClass().getResource("/graphics/sound/move.wav").toExternalForm();
        AudioClip audio = new AudioClip(url);
        audio.play();
        System.out.println("moved");
        String moved = ClientContext.get().getMap().getPlayerById(pId).getNickName();
        if(pId == ClientContext.get().getMyID())
            textNotify.setText("You moved correctly\n" + textNotify.getText());
        else
            textNotify.setText("Player "+ moved + " moved!\n" + textNotify.getText());
        refreshPlayerPosition();
    }

    @Override
    public void notifyDeath(int idKiller, int idVictim, boolean rage) {
        if(idKiller == ClientContext.get().getMyID()){
            String url = getClass().getResource("/graphics/sound/death.wav").toExternalForm();
            AudioClip audio = new AudioClip(url);
            audio.play();
        }
        textNotify.setText("Player "+ClientContext.get().getMap().getPlayerById(idKiller).getNickName() + " killed "+ClientContext.get().getMap().getPlayerById(idVictim).getNickName() + "\n" + textNotify.getText());
        refreshDeaths();
    }

    @Override
    public void notifyRage(Player killer, Player victim) {
        textNotify.setText("Player "+killer.getNickName()+" has raged "+victim.getNickName() + "\n" + textNotify.getText());
    }

    @Override
    public void grabWeaponNotification(int pID, String name, int x, int y) {
        System.out.println("Notify grab weapon");
        refreshWeaponCard();
        String url = getClass().getResource("/graphics/sound/grab.wav").toExternalForm();
        AudioClip audio = new AudioClip(url);
        audio.play();
        if(pID == ClientContext.get().getMyID()){
            refreshMyPlayerCard();
            refreshMyPlayerAmmo();
            textNotify.setText("You grabbed "+name + "\n" + textNotify.getText());
        }else{
            String pName = ClientContext.get().getMap().getPlayerById(pID).getNickName();
            textNotify.setText("Player "+pName+ " grabbed "+ name +"\n" + textNotify.getText());
        }
    }

    @Override
    public void powerUpUsageNotification(int id, String name, String description) {
        if(id == ClientContext.get().getMyID()){
            refreshMyPlayerCard();
            textNotify.setText("You used "+name + "\n" + textNotify.getText());
        }else{
            String pName = ClientContext.get().getMap().getPlayerById(id).getNickName();
            textNotify.setText("Player "+pName+ " used "+ name + "\n" + textNotify.getText());
        }
    }

    @Override
    public void choosePowerUpToRespawn(List<CardPower> cardPower) {
        textNotify.setText("Your turn: respawn\n" + textNotify.getText());
        refreshMyPlayerCard();
        if(cardPower.size() > 3){
            StackPane sp = new StackPane();
            Scene tempS = new Scene(sp);
            sg.setAlwaysOnTop(true);
            primaryStage.setAlwaysOnTop(false);
            sg.show();
        }
        else{
            for(ImageView im : powerUp){
                if(!im.getId().equals("0")){
                    im.setEffect(new DropShadow(35,Color.GREEN));
                    im.setOnMouseClicked(this::handleRespawnSquare);
                }
            }
        }
    }

    @Override
    public void notifyCompletedOperation(String message) {
        System.out.println("notifyOperationcomeplte");
        textNotify.setText(message + "\n" + textNotify.getText());
        if(!this.controller.getState().equals(ClientState.WAITING_START)){
            refreshMyPlayerCard();
            refreshMyPlayerAmmo();
        }
    }

    @Override
    public void notifyInvalidPowerUP() {
        textNotify.setText("Invalid Power Up\n" + textNotify.getText());
    }

    @Override
    public void notifyInvalidGrabPosition() {
        textNotify.setText("Invalid position, no card ammo here\n" + textNotify.getText());
    }


    @Override
    public void choosePowerUpToUse(List<CardPower> cardPower) {
        System.out.println("ChoosePowerUptouse");
        List <CardPower> list = cardPower.stream().filter(x -> x.getName().equals("Targeting scope")).collect(Collectors.toList());
        int dim = list.size();
        if(dim > 0 ){
            state = ClientState.CHOOSESCOPE;
            text.setText("Do you want to use a Targeting scope power-up card to apply an additional damage to one of your previous target?");
            yes.setVisible(true);
            no.setVisible(true);
            yes.setOnMouseClicked(mouseEvent -> {
                if(dim > 1){
                    text.setText("Choose which power up use :");
                    activateCardPower(list);
                }else{
                    text.setText("Choose an ammo ");
                    scopeSelected = list.get(0);
                    activateAmmo();
                }
                no.setVisible(false);
                yes.setVisible(false);
            });
            no.setOnMouseClicked(mouseEvent -> {
                controller.getClient().sendMessage(new ChoosePowerUpResponse());
                no.setVisible(false);
                yes.setVisible(false);
            });
        }

    }
    private synchronized void choosePowerUpToPay(List<CardPower> cardPower) {
        StackPane sp = new StackPane();
        Scene tempScene = new Scene(sp);
        List<CheckBox> powerUp = new ArrayList<>();
        List<CardPower> choosenPW = new ArrayList<>();
        Label tex = new Label("Choose which power up you wanna use to pay");
        Image background = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/background.jpg"));
        BackgroundImage bi = new BackgroundImage(background,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        sp.getChildren().add(tex);
        int j = 0;
        StackPane.setMargin(tex,new Insets(20, 0,0,0));
        tex.setFont(Font.font(25));
        tex.setTextFill(Color.WHITE);
        sp.setBackground(new Background(bi));
        StackPane.setAlignment(tex,Pos.TOP_CENTER);
        sp.setPrefSize(screenWidth * 30 / 100,screenHeight * 50/ 100);
        for(CardPower cp : cardPower){
            CheckBox cb = new CheckBox(cp.getName());
            StackPane.setAlignment(cb,Pos.TOP_LEFT);
            StackPane.setMargin(cb,new Insets(100 + j , 0,0,40));
            sp.getChildren().add(cb);
            cb.setFont(Font.font(25));
            cb.setId(""+cp.getId());
            powerUp.add(cb);
            cb.setTextFill(Color.WHITE);
            j = j + 35;
        }
        Button submit = new Button("Submit");
        sp.getChildren().add(submit);
        StackPane.setAlignment(submit, Pos.BOTTOM_RIGHT);
        submit.setOnMouseClicked(mouseEvent -> {
            for(CheckBox c : powerUp){
                if(c.isSelected()){
                    CardPower cp = ClientContext.get().getMyPlayer().getCardPower().stream().filter(pw -> pw.getId() == Integer.parseInt(c.getId())).findFirst().orElse(null);
                    if(cp != null)
                        choosenPW.add(cp);
                }
            }
            switch (state){
                case CHOOSEWEAPONTOWASTE:
                    controller.getClient().sendMessage(new PickUpWeaponRequest(weaponG,choosenPW, weaponW));
                    break;
                case CHOOSEWEAPONTOGRAB:
                    controller.getClient().sendMessage(new PickUpWeaponRequest(weaponG,choosenPW, null));
                    break;
                case CHOOSEPBB:
                    controller.getClient().sendMessage(new UsePlusBeforeResponse(plusEff,t,choosenPW));
                    break;
                case CHOOSEPLUSORDER:
                    controller.getClient().sendMessage(new UseOrderPlusResponse(fullEffectList, choosenPW, 'y'));
                    break;
                case CHOOSEFIRSTEFFECT:
                    controller.getClient().sendMessage(new ChooseFirstEffectResponse(2,choosenPW));
                    break;
                case CHOOSEPLUSEFFECT:
                    controller.getClient().sendMessage(new UsePlusEffectResponse(fullEffectList, plusEff, choosenPW));
                    break;
                case CHOOSERELOAD:
                    reloadRequests.add(new ReloadWeaponRequest(weaponToReload,choosenPW));
                    reloadWeaponPhase(weaponsToReload);
                    break;
            }
            sg.close();
        });
        sg.setScene(tempScene);
        primaryStage.setAlwaysOnTop(false);
        sg.setAlwaysOnTop(true);
        sg.show();
    }

    @Override
    public void notifyInvalidMessage() {
        textNotify.setText("Invalid choise!\n" + textNotify.getText());
    }

    @Override
    public void notifyTurnChanged(int pID) {
        System.out.println("Turn changed");
        if(!state.equals(ClientState.CHOOSECOUNTER))
            text.setText("");
        refreshWeaponCard();
        refreshAmmoCard();
        if(pID == ClientContext.get().getMyID())
            text.setText("Your turn, Good Luck!");
        else {
            Player p = ClientContext.get().getMap().getPlayerById(pID);
            if( p == null)
                p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == pID).findFirst().orElse(null);
            if(p == null)
                textNotify.setText("Player "+pID+ " turn! Wit your time.\n" + textNotify.getText());
            else
                textNotify.setText("Player "+p.getNickName()+" turn! Wait yout turn.\n" + textNotify.getText());
        }
    }

    @Override
    public void notifyMarks(int marks, int idHitten, int idShooter) {
       /* String url = getClass().getResource("/graphics/sound/shoot.wav").toExternalForm();
        AudioClip audio = new AudioClip(url);
        audio.play();

        */
        String hitted = ClientContext.get().getMap().getPlayerById(idHitten).getNickName();
        String shooter = ClientContext.get().getMap().getPlayerById(idShooter).getNickName();
        if(idHitten == ClientContext.get().getMyID()) {
            refreshMyPlayerDamage();
            textNotify.setText("You got hitted by "+ shooter +"\n" + textNotify.getText());
        }
        else {
            refreshPlayerDamage();
            textNotify.setText(hitted+" got hitted by "+ shooter + "\n" + textNotify.getText());
        }
    }

    @Override
    public void notifyGrabCardAmmo(int pID) {
        System.out.println("notifyGrabAmmo");
        String url = getClass().getResource("/graphics/sound/grab.wav").toExternalForm();
        AudioClip audio = new AudioClip(url);
        audio.play();
        if(pID == ClientContext.get().getMyID()) {
            textNotify.setText("You grabbed correctly!\n" + textNotify.getText());
            refreshMyPlayerAmmo();
        }else
            textNotify.setText("Player "+ClientContext.get().getMap().getPlayerById(pID).getNickName() + "grab!\n" + textNotify.getText() );
        refreshAmmoCard();
    }

    @Override
    public void notifyRespawn(int pID) {
        String url = getClass().getResource("/graphics/sound/respawn.wav").toExternalForm();
        AudioClip audio = new AudioClip(url);
        audio.play();
        System.out.println("notifyrespawn");
        if(pID == ClientContext.get().getMyID())
            refreshMyPlayerCard();
        refreshPlayerPosition();
    }
    private void activateCardPower(List<CardPower> list){
        for(CardPower cp : list){
            ImageView im = powerUp.stream().filter(c -> Integer.parseInt(c.getId())==cp.getId()).findAny().orElse(null);
            if(im!= null){
                im.setEffect(new DropShadow(35,Color.GREEN));
                im.setOnMouseClicked(this::handlePowerUpClick);
            }
        }
    }
    private void activateWeapon(List<CardWeapon> weapons, ImageView iv){
        int id = Integer.parseInt(iv.getId().substring(2));
        if(id != 0) {
            if (weapons.stream().anyMatch(w -> w.getId() == id)) {
                iv.setOnMouseClicked(this::handleWeaponClick);
                iv.setEffect(new DropShadow(35, Color.GREEN));
                Tooltip.install(iv,toolw);
            }
        }
    }
    @Override
    public void chooseWeaponToGrab(List<CardWeapon> weapons) {
        text.setText("Select the weapon to grab!");
        state = ClientState.CHOOSEWEAPONTOGRAB;
        weaponToGrab = weapons;
        for(ImageView iv : mapWT){
            activateWeapon(weapons,iv);
        }
        for(ImageView iv : mapWR){
            activateWeapon(weapons,iv);
        }
        for(ImageView iv : mapWL){
            activateWeapon(weapons,iv);
        }
    }

    @Override
    public void chooseRoomPhase(List<WaitingRoom> waitingRooms) {
        StackPane waits = new StackPane();

        Image background = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/background01.jpg"));
        BackgroundImage bi = new BackgroundImage(background,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        waits.setBackground(new Background(bi));
        textwait = new Label();
        textwait.setText("Wait Other Player");
        textwait.setTextFill(Color.WHITE);
        waits.getChildren().add(textwait);
        StackPane.setAlignment(textwait,Pos.CENTER);
        Scene wait = new Scene(waits);
        StackPane chooseRoom = new StackPane();
        chooseRoom.setBackground(new Background(bi));
        chooseRoom.setPrefSize(500, 300);
        Scene chooseR = new Scene(chooseRoom);
        if (waitingRooms.isEmpty()) {
            Label text = new Label("No waiting room available");
            text.setTextFill(Color.WHITE);
            Button bt1 = new Button("Create new Waiting room");
            chooseRoom.getChildren().addAll(text, bt1);
            bt1.setOnAction(this::handleNewRoom);
        } else {
            VBox infoRoom = new VBox();
            ToggleGroup tg = new ToggleGroup();
            Button bt1 = new Button("Create new Waiting room");
            for (WaitingRoom w : waitingRooms) {
                RadioButton rb = new RadioButton("Waiting Room " + w.getId() + ":");
                rb.setTextFill(Color.WHITE);
                rb.setId(""+w.getId());
                rb.setToggleGroup(tg);
                rb.setSelected(true);
                Label text = new Label(w.toString().substring(18));
                text.setTextFill(Color.WHITE);
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
                primaryStage.setScene(wait);
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
        if(weaponsToReload.size() == 0){
            if (reloadRequests.size() == 0)
                controller.getClient().sendMessage(new EndActionRequest());
            else
                controller.sendMessages(reloadRequests);
        }else {
            System.out.println("Reload");
            state = ClientState.CHOOSERELOAD;
            text.setText("You want to reload?");
            this.weaponsToReload = weaponsToReload;
            no.setVisible(true);
            yes.setVisible(true);
            no.setOnMouseClicked(mouseEvent -> {
                yes.setVisible(false);
                no.setVisible(false);
                if (reloadRequests.size() == 0)
                    controller.getClient().sendMessage(new EndActionRequest());
                else
                    controller.sendMessages(reloadRequests);
            });
            yes.setOnMouseClicked(mouseEvent -> {
                for (ImageView iv : weapons) {
                    if (weaponsToReload.stream().anyMatch(mw -> mw.getId() == Integer.parseInt(iv.getId().substring(2)))) {
                        iv.setEffect(new DropShadow(35, Color.GREEN));
                        iv.setOnMouseClicked(this::handleWeaponClick);
                    }
                }
                yes.setVisible(false);
                no.setVisible(false);
                text.setText("choose the weapon you want to reload!");
            });
        }
    }


    @Override
    public void showReloadMessage(CardWeapon cW) {
        textNotify.setText("You correctly reload " + cW.getName() + "\n" + textNotify.getText());
    }

    @Override
    public void chooseWeaponToShoot(List<CardWeapon> myWeapons) {
        state = ClientState.CHOOSEWEAPONTOSHOOT;
        text.setText("Choose weapon to shoot!");
        for(ImageView iv : weapons){
            if(myWeapons.stream().anyMatch(mw->mw.getId() == Integer.parseInt(iv.getId().substring(2)))){
                iv.setEffect(new DropShadow(35,Color.GREEN));
                iv.setOnMouseClicked(this::handleWeaponClick);
            }

        }
    }

    @Override
    public void chooseFirstEffect(FullEffect baseEff, FullEffect altEff) {
        state = ClientState.CHOOSEFIRSTEFFECT;
        List<CardPower> list;
        list = possibleCardPowerToPay(altEff);
        List<CardPower> price = list;
        text.setText("Want to use Base effect? (No for alternative)");
        yes.setVisible(true);
        yes.setOnMouseClicked(mouseEvent -> {
            controller.getClient().sendMessage(new ChooseFirstEffectResponse(1,null));
            yes.setVisible(false);
            no.setVisible(false);
        });
        no.setVisible(true);
        no.setOnMouseClicked(mouseEvent -> {
            if(price.size() != 0)
                choosePowerUpToPay(price);
            else
                controller.getClient().sendMessage(new ChooseFirstEffectResponse(2,null));
            yes.setVisible(false);
            no.setVisible(false);
        });

    }

    @Override
    public void usePlusBeforeBase(FullEffect plusEff) {
        List<CardPower> price = possibleCardPowerToPay(plusEff);
        state = ClientState.CHOOSEPBB;
        this.plusEff = plusEff;
        text.setText("Do you want to use this plus effect before than your weapon base effect?");
        yes.setVisible(true);
        yes.setOnMouseClicked(mouseEvent -> {
            t = 'y';
            if(price.size() != 0)
                choosePowerUpToPay(price);
            else
                controller.getClient().sendMessage(new UsePlusBeforeResponse(plusEff,t,null));
            yes.setVisible(false);
            no.setVisible(false);
        });
        no.setVisible(true);
        no.setOnMouseClicked(mouseEvent -> {
            controller.getClient().sendMessage(new UsePlusBeforeResponse(plusEff,'n',null));
            yes.setVisible(false);
            no.setVisible(false);
        });
    }

    /**
     * Return a list of my cardpower that i can use for pay the FullEfect
     * @param fe
     * @return
     */
    private List<CardPower> possibleCardPowerToPay (FullEffect fe){
        List<CardPower> list = new ArrayList<>();
        if(fe.getPrice() != null)
            list = ClientContext.get().getMyPlayer().getCardPower().stream().filter(n -> fe.getPrice().contains(n.getColor())).collect(Collectors.toList());
        return list;
    }

    /**
     * Return a list of my cardpower that i can use for pay the Price
     * @param price
     * @return
     */
    private List<CardPower> possibleCardPowerToPay (List<game.model.Color> price){
        List<CardPower> list = new ArrayList<>();
        if(price != null)
            list = ClientContext.get().getMyPlayer().getCardPower().stream().filter(n -> price.contains(n.getColor())).collect(Collectors.toList());
        return list;
    }

    @Override
    public void usePlusInOrder(List<FullEffect> plusEffects) {
        state = ClientState.CHOOSEPLUSORDER;
        fullEffectList = plusEffects;
        List<CardPower> list = possibleCardPowerToPay(plusEffects.get(0));
        text.setText("Do you want to apply the plus effect, allow by your weapon? (in order)");
        yes.setVisible(true);
        no.setVisible(true);
        yes.setOnMouseClicked(mouseEvent -> {
            if(list.size() != 0)
                choosePowerUpToPay(list);
            else
                controller.getClient().sendMessage(new UseOrderPlusResponse(fullEffectList, null, 'y'));
            yes.setVisible(false);
            no.setVisible(false);
        });
        no.setOnMouseClicked(mouseEvent -> {
            yes.setVisible(false);
            no.setVisible(false);
            controller.getClient().sendMessage(new UseOrderPlusResponse(plusEffects,null,'n'));
        });
    }
    @Override
    public synchronized void choosePlusEffect(List<FullEffect> plusEffects) {
        state = ClientState.CHOOSEPLUSEFFECT;
        StackPane sp = new StackPane();
        Scene tempScene = new Scene(sp);
        Label tex = new Label("Choose wich efferct you want to apply:");
        sp.setPrefSize(screenWidth * 30 / 100,screenHeight * 50/ 100);
        sp.getChildren().add(tex);
        StackPane.setAlignment(tex,Pos.TOP_CENTER);
        StackPane.setMargin(tex,new Insets(20, 0,0,0));
        ToggleGroup tg = new ToggleGroup();
        RadioButton none = new RadioButton("None");
        none.setId("none");
        none.setToggleGroup(tg);
        none.setSelected(true);
        sp.getChildren().add(none);
        int j = 1;
        StackPane.setAlignment(none,Pos.TOP_LEFT);
        StackPane.setMargin(none,new Insets(100 + j , 0,0,40));
        j = j + 35;
        for(FullEffect f : plusEffects){
            RadioButton eff = new RadioButton("Effect: "+f.getName());
            eff.setId(f.getName());
            eff.setToggleGroup(tg);
            sp.getChildren().add(eff);
            StackPane.setAlignment(eff,Pos.TOP_LEFT);
            StackPane.setMargin(eff,new Insets(100 + j , 0,0,40));
            j = j + 35;
        }
        Button submit = new Button("Submit");
        sp.getChildren().add(submit);
        StackPane.setAlignment(submit, Pos.BOTTOM_RIGHT);
        submit.setOnMouseClicked(mouseEvent -> {
            if(((RadioButton)tg.getSelectedToggle()).getId().equals("none")) {
                controller.getClient().sendMessage(new TerminateShootAction());
            }
            else{
                int i = 0;
                FullEffect fe = null;
                for(FullEffect e : plusEffects){
                    if(e.getName().equals(((RadioButton)tg.getSelectedToggle()).getId())){
                        fe = e;
                        plusEffects.remove(i);
                        plusEff = e;
                        fullEffectList = plusEffects;
                    }
                    i++;
                }
                if(fe == null)
                    controller.getClient().sendMessage(new TerminateShootAction());
                else
                    if(fe.getPrice() == null) {
                        controller.getClient().sendMessage(new UsePlusEffectResponse(plusEffects, fe, null));
                    }
                    else{
                        List<CardPower> list = possibleCardPowerToPay(fe);
                        if(list != null)
                            choosePowerUpToPay(list);
                        else
                            controller.getClient().sendMessage(new UsePlusEffectResponse(fullEffectList, plusEff, null));
                    }
            }
            sg.close();
        });
        sg.setScene(tempScene);
        sg.show();
    }

    @Override
    public void showRanking(SortedMap<Player, Integer> ranking) {
        System.out.println("RANK");
        StackPane sp = new StackPane();
        Scene tempScene = new Scene(sp);
        Label tex = new Label("And The Winner Is:");
        sp.getChildren().add(text);
        sp.setPrefSize(screenWidth * 30 / 100,screenHeight * 50/ 100);
        StackPane.setMargin(tex,new Insets(20, 0,0,0));
        StackPane.setAlignment(tex,Pos.TOP_CENTER);
        int j = 1;
        for(Player p : ranking.keySet()){
            Label pos = new Label(p.getNickName() + " got " +j+ "° place with "+ ranking.get(p) + " points");
            sp.getChildren().add(pos);
            StackPane.setAlignment(pos,Pos.TOP_CENTER);
            StackPane.setMargin(pos,new Insets(20 + j * 30, 0,0,0));
            j++;
        }
        sg.setScene(tempScene);
        sg.show();
    }

    @Override
    public void rejoinGamePhase(List<String> otherPlayers) {
        disablePowerUp();
        disableMyWeapon();
        disableSquare();
        disableAmmo();
        disableButton();
        disableWeapon();
        text.setText("Hello "+ClientContext.get().getMyPlayer().getNickName() + " do you want to rejoin the game?");
        yes.setVisible(true);
        no.setVisible(true);
        yes.setOnMouseClicked(mouseEvent -> {
            controller.getClient().sendMessage(new RejoinGameResponse(true,ClientContext.get().getUser()));
            no.setVisible(false);
            yes.setVisible(false);
        });
        no.setOnMouseClicked(mouseEvent -> primaryStage.close());
    }

    @Override
    public void notifyPlayerSuspended(Player p) {
        textNotify.setText("Player "+ p.getNickName() + "got suspended!\n" + textNotify.getText());
    }

    @Override
    public void timeOutPhase() {
        textNotify.setText("You timed out and you have been kicked out!\n" + textNotify.getText());
        if(this.controller.getState() != ClientState.GAME_END) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            text.setText("Do you want to rejoin the game?[Y/N]");
            yes.setVisible(true);
            no.setVisible(true);
            yes.setOnMouseClicked(mouseEvent -> {
                if (controller.getState() != ClientState.GAME_END) {
                    controller.getClient().sendMessage(new RejoinGameResponse(true, ClientContext.get().getUser()));
                    no.setVisible(false);
                    yes.setVisible(false);
                } else
                    textNotify.setText("Game Finished\n" + textNotify.getText());
            });
            no.setOnMouseClicked(mouseEvent -> this.controller.stopListening());
        }
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
        btn.setOnAction(ac ->setUserNamePhase());
    }

    @Override
    public void loginCompletedPhase() {
        controller.getClient().sendMessage(new GetAvailableMapsRequest());
    }

    @Override
    public void rejoinGameConfirm() {
        textNotify.setText("You successfully rejoin your previous game! Now wait for your turn..\n" + textNotify.getText());
    }

    @Override
    public void notifyPlayerRejoin(Player p) {
        textNotify.setText("Player " + p.getNickName() + " has rejoined the game!\n" + textNotify.getText());
    }

    @Override
    public void notifyPlayerLeavedWaitingRoom(Player p) {
        textwait.setText(textwait.getText() +"\n Player "+p.getNickName()+" leaved the waiting room.");
    }

    @Override
    public void notifyPlayerJoinedWaitingRoom(Player p) {
        textwait.setText(textwait.getText() +"\n Player "+p.getNickName()+" joined the waiting room.");
    }

    /**
     * Show reconnection message
     */
    @Override
    public void notifyReconnected() {
        textNotify.setText("Reconnected to the server!!");
    }

    @Override
    public void showPoints() {
        for(Label l : points){
            l.setText(""+ClientContext.get().getMap().getPlayerById(Integer.parseInt(l.getId())).getPoints());
        }
        myPoint.setText(""+ClientContext.get().getMyPlayer().getPoints());
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
                if(ClientContext.get().getMap().getSquare(x,y).getCardAmmo() != null){                      //!ClientContext.get().getMap().getSquare(x,y).isRespawn() &&
                    ca = ClientContext.get().getMap().getSquare(x,y).getCardAmmo();
                    if (ca.getCardPower() == 1) {
                        card = card + "P";
                    }
                    for (game.model.Color c : ca.getAmmo()) {
                        if (c != null) {
                            card = card + c.toString().substring(0, 1);
                        }
                    }
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
        if(color.equals(PlayerColor.GREY))return Color.GREY;
        return Color.TRANSPARENT;
    }

    /**
     * Reposition the player Circle in the right position
     */
    private void refreshPlayerPosition(){
        double spaceX = 0;
        double spaceY = 0;
        int x;
        int y;
        for(int i = 1; i< 13 ; i++) {
            if(i ==  1){
                x = 0;
                y = 0;
            }else {
                x = (i - 1) % 4;
                y = (i - 1) / 4;
            }
            if(ClientContext.get().getMap().getGrid()[y][x] != null) {
                int j = 0;
                for (Player pl : ClientContext.get().getMap().getGrid()[y][x].getPlayers()) {
                    Circle p = players.stream().filter(player -> pl.getNickName().equals(player.getId())).findFirst().orElse(null);
                    if (p == null)
                        System.out.println("Player not found");
                    StackPane.setAlignment(p, Pos.TOP_LEFT);
                    double xS = spaceX + j * 20;
                    StackPane.setMargin(p, new Insets(screenHeight * 29 / 100 + spaceY, 0, 0, screenWidth * 11 / 100 + xS));
                    j++;
                }
                }
                if (x % 3 == 0 && x != 0) {
                    spaceY = spaceY + screenHeight * 18 / 100;
                    spaceX = 0;
                } else {
                    spaceX = spaceX + screenWidth * 9.5 / 100;
            }
        }
    }

    /**
     * create a text view in the map
     */
    private void createTextNotification(){
        text.setStyle("-fx-font: 20px Tahoma;");
        text.setWrapText(true);
        text.setTextFill(Color.WHITE);
        text.setPrefWidth(screenWidth*52.5/100);
        text.setMaxHeight(screenHeight* 3 /100);
        textNotify.setStyle("-fx-font: 15px Tahoma;");
        textNotify.setTextFill(Color.WHITE);
        textNotify.setPrefWidth(screenWidth*52.5/100);
        textNotify.setMaxHeight(screenHeight*6/100);
        textNotify.setText("");

        map.getChildren().addAll(text, textNotify , yes , no);
        StackPane.setAlignment(textNotify,Pos.BOTTOM_LEFT);
        StackPane.setMargin(textNotify,new Insets(0,0,(screenHeight * 25 / 100),0));
        StackPane.setAlignment(text,Pos.BOTTOM_LEFT);
        StackPane.setMargin(text,new Insets(0,0,(screenHeight * 17.7 / 100),0));
        StackPane.setAlignment(yes,Pos.BOTTOM_LEFT);
        StackPane.setMargin(yes,new Insets(0,0,(screenHeight * 12 / 100),screenWidth * 42 / 100));
        yes.setVisible(false);
        StackPane.setAlignment(no,Pos.BOTTOM_LEFT);
        StackPane.setMargin(no,new Insets(0,0,(screenHeight * 12 / 100),screenWidth * 46 / 100));
        no.setVisible(false);
    }

    /**
     * Create my player Power-up and Weapon card position in the screen
     * Set the back of the card from default
     */
    private void createMyPlayerCard(){
        Image w = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/W_back.png"));
        Image p = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/PW_back.png"));
        ImageView pu0 = new ImageView(p);
        ImageView pu1 = new ImageView(p);
        ImageView pu2 = new ImageView(p);
        ImageView we1 = new ImageView(w);
        ImageView we2 = new ImageView(w);
        ImageView we0 = new ImageView(w);
        powerUp = new ArrayList<>(Arrays.asList(pu0,pu1,pu2));
        weapons = new ArrayList<>(Arrays.asList(we0,we1,we2));
        int i = 0;
        for(ImageView imw : weapons){
            map.getChildren().add(imw);
            imw.setFitWidth(screenWidth*6.9/100);
            imw.setPreserveRatio(true);
            StackPane.setAlignment(imw,Pos.BOTTOM_RIGHT);
            StackPane.setMargin(imw,new Insets(0,i,0,0));
            i = i + (int)(screenHeight*14/100);
        }

        for(ImageView imw : powerUp){
            map.getChildren().add(imw);
            imw.setFitWidth(screenWidth*6.9/100);
            imw.setPreserveRatio(true);
            StackPane.setAlignment(imw,Pos.BOTTOM_RIGHT);
            StackPane.setMargin(imw,new Insets(0,i,0,0));
            i = i + (int)(screenHeight*14/100);
        }
    }

    /**
     * Create a power up from his name and color (name_color initial)
     * @param name
     * @param color
     * @return
     */
    private Image createPowerUpCard(String name, game.model.Color color){
        return new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/"+name+"_"+color.toString().substring(0,1)+".png"));
    }

    /**
     * Return the weapon picture name from his Id
     * @param i
     * @return
     */
    private Image createWeaponCard(int i){
        if(i == 0)
            return new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/W_back.png"));
        return new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/W_"+i+".png"));
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
        StackPane.setAlignment(mapIV,Pos.TOP_LEFT);
        map.getChildren().add(mapIV);
        Image background = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/background.png"));
        BackgroundImage bi = new BackgroundImage(background,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        map.setBackground(new Background(bi));

        map.getChildren().addAll(move,grab,shoot,exit);
        StackPane.setMargin(move,new Insets(0,screenWidth * 2 / 100,screenHeight * 23 / 100,0));
        StackPane.setAlignment(move,Pos.BOTTOM_RIGHT);
        StackPane.setMargin(grab,new Insets(0,screenWidth * 10 / 100,screenHeight * 23 / 100,0));
        StackPane.setAlignment(grab,Pos.BOTTOM_RIGHT);
        StackPane.setMargin(shoot,new Insets(0,screenWidth * 15/ 100,screenHeight * 23 / 100,0));
        StackPane.setAlignment(shoot,Pos.BOTTOM_RIGHT);
        StackPane.setMargin(exit,new Insets(0,screenWidth * 20/ 100,screenHeight * 23 / 100,0));
        StackPane.setAlignment(exit,Pos.BOTTOM_RIGHT);
        StackPane.setMargin(exit,new Insets(0,screenWidth * 26/ 100,screenHeight * 23 / 100,0));
        StackPane.setAlignment(exit,Pos.BOTTOM_RIGHT);
        move.setStyle(orangeB);
        grab.setStyle(orangeB);
        shoot.setStyle(orangeB);
        exit.setStyle(orangeB);
        power.setStyle(orangeB);
        power.setId("Power");
        move.setId("Movement");
        grab.setId("Grab");
        shoot.setId("Shoot");
        exit.setId("Exit");
        move.setOnMouseClicked(this::handleStepAction);
        grab.setOnMouseClicked(this::handleStepAction);
        shoot.setOnMouseClicked(this::handleStepAction);
        exit.setOnMouseClicked(this::handleStepAction);
        power.setOnMouseClicked(this::handleStepAction);
        exit.setDisable(true);
        move.setDisable(true);
        grab.setDisable(true);
        shoot.setDisable(true);
        power.setDisable(true);
        power.setVisible(false);
        exit.setVisible(false);
        move.setVisible(false);
        grab.setVisible(false);
        shoot.setVisible(false);


        //stampa rettangoli invisibili e ammo
        double spaceX = 0;
        double spaceY = 0;
        for(int i = 1; i < 13; i++){
            Rectangle rec = new Rectangle(screenWidth * 6.77 /100,screenHeight * 12.03 / 100,Color.TRANSPARENT);
            rec.setId(""+i);
            squares.add(rec);
            ImageView ammoIV = createImageViewAmmo(i);
            ammos.add(ammoIV);
            map.getChildren().addAll(rec,ammoIV);
            StackPane.setAlignment(rec,Pos.TOP_LEFT);
            StackPane.setMargin(rec,new Insets(screenHeight*19/100 + spaceY,0,0,screenWidth*11/100 + spaceX));
            StackPane.setAlignment(ammoIV,Pos.TOP_LEFT);
            StackPane.setMargin(ammoIV,new Insets(screenHeight*19/100 + spaceY,0,0,screenWidth*11/100 + spaceX));
            rec.setStroke(Color.TRANSPARENT);
            rec.setStrokeWidth(7);
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
            weapon.setId("1_0");
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
            weapon.setId("2_0");
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
            weapon.setId("3_0");
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
     * Refresh the skull board
     */
    private void refreshDeaths(){
        for(ImageView im : deathsBoard){
            im.setImage(null);
            map.getChildren().remove(im);
        }
        deathsBoard.clear();
        double spaceY = 0;
        for(Kill k : ClientContext.get().getKillboard()){
            Image kill = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/"+k.getKiller().getColor().toString()+"Tear.png"));
            ImageView kills = new ImageView(kill);
            kills.setFitHeight(screenHeight * 3 /100);
            kills.setPreserveRatio(true);
            deathsBoard.add(kills);
            map.getChildren().add(kills);
            StackPane.setAlignment(kills,Pos.TOP_LEFT);
            StackPane.setMargin(kills,new Insets(screenHeight * 5 / 100,0,0,screenWidth * 4.8 / 100 + spaceY));
            spaceY = spaceY + screenWidth * 2.46 / 100;
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
                cardP = createPowerUpCard(myP.getCardPower().get(i).getName(), myP.getCardPower().get(i).getColor());
                powerUp.get(i).setId("" + myP.getCardPower().get(i).getId());
            }
            if (i >= myP.getWeapons().size()){
                cardW = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/cards/W_back.png"));
                weapons.get(i).setId("0_0");
            }
            else {
                cardW = createWeaponCard(myP.getWeapons().get(i).getId());
                weapons.get(i).setId("0_" + myP.getWeapons().get(i).getId());
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
            StackPane.setMargin(c, new Insets(-100,0,0,0));
            c.setOnMouseClicked(this::handlePlayerClick);
            //c.setDisable(true);
        }
    }

    /**
     * refreah the ammo in the map
     */
    private synchronized void refreshAmmoCard(){
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
            Image dash = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/"+p.getColor().toString()+"Dash.png"));
            ImageView imw = new ImageView(dash);
            Label name = new Label(p.getNickName());
            Label point = new Label("0");
            point.setId(""+p.getId());
            switch(p.getColor()){
                case PURPLE:
                case BLUE:
                    name.setTextFill(Color.WHITE);
                    point.setTextFill(Color.WHITE);
                    break;
                case GREEN:
                case YELLOW:
                case GREY:
                    name.setTextFill(Color.BLACK);
                    point.setTextFill(Color.BLACK);
                    break;
            }
            name.setFont(Font.font(16));
            point.setFont(Font.font(25));
            map.getChildren().addAll(imw,name,point);
            imw.setId(""+p.getId());
            imw.setFitWidth(screenWidth*36.6/100);
            imw.setPreserveRatio(true);
            if(p.equals(ClientContext.get().getMyPlayer())){
                myPoint = point;
                StackPane.setAlignment(imw,Pos.BOTTOM_LEFT);
                StackPane.setAlignment(name,Pos.BOTTOM_LEFT);
                StackPane.setMargin(name,new Insets(0,0,screenHeight * 13.5 / 100,screenWidth * 4 / 100));
                StackPane.setAlignment(point,Pos.BOTTOM_LEFT);
                StackPane.setMargin(point,new Insets(0,0,screenHeight * 13.5 / 100,screenWidth * 2/ 100 ));

            }else {
                playerDashBoard.add(imw);
                points.add(point);
                System.out.println("Plater :" +p.getNickName());
                StackPane.setAlignment(point,Pos.TOP_RIGHT);
                StackPane.setMargin(point,new Insets(i + screenHeight * 0.4 / 100,screenWidth * 33 / 100,0,0));
                StackPane.setAlignment(name,Pos.TOP_RIGHT);
                StackPane.setMargin(name,new Insets(i + screenHeight * 0.4 / 100,screenWidth * 29 / 100,0,0));
                StackPane.setAlignment(imw,Pos.TOP_RIGHT);
                StackPane.setMargin(imw,new Insets(i,0,0,0));
                i = i + (int)(screenHeight*18.6/100);
            }
        }
    }

    /**
     * refresh other player damage and marks tear simbol
     */
    private synchronized void refreshPlayerDamage(){
        double spaceY = 0;
        deletePlayerInfo();
        for(ImageView dash : playerDashBoard) {
            int j = 0;
            double spaceX = 0;
            List<ImageView> infoD = new ArrayList<>();
            List<ImageView> infoM = new ArrayList<>();
            Player p = ClientContext.get().getMap().getPlayerById(Integer.parseInt(dash.getId()));
            if(p == null)
                p = ClientContext.get().getPlayersInWaiting().stream().filter(play->play.getId() == Integer.parseInt(dash.getId())).findFirst().orElse(null);
            System.out.println(p.getNickName()+" Danno "+p.getDamage().size() +" color "+p.getMark().size());
            for (PlayerColor  c :p.getDamage()) {
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

            for(PlayerColor c : p.getThisTurnMarks()){
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
                v.setImage(null);
                map.getChildren().remove(v);
            }
        }
        for(List<ImageView> p : playerMarks){
            for(ImageView v : p ){
                v.setImage(null);
                map.getChildren().remove(v);
            }
        }
        playerDamage.clear();
        playerMarks.clear();
    }
    private synchronized ImageView createMyTear(String color){
        Image damage = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/"+color+"Tear.png"));
        ImageView damages = new ImageView(damage);
        myPlayerDamage.add(damages);
        damages.setPreserveRatio(true);
        map.getChildren().add(damages);
        return  damages;
    }
    /**
     * refresh  my player damage and marks
     */
    private synchronized void refreshMyPlayerDamage(){
        double spaceX = 0;
        int j = 0;
        deleteMyPlayerDamage();
        for(PlayerColor p : ClientContext.get().getMyPlayer().getDamage()){
            ImageView damages = createMyTear(p.toString());
            damages.setFitHeight(screenHeight * 3 /100);
            StackPane.setAlignment(damages,Pos.BOTTOM_LEFT);
            StackPane.setMargin(damages,new Insets(0,0,screenHeight * 6.25 / 100,screenWidth * 3.44 / 100 + spaceX));
            spaceX = spaceX + screenWidth * 2.25 / 100;
            if(j > 1 && j != 4)
                spaceX = spaceX - screenWidth * 0.25 / 100;
            j++;
        }
        spaceX = 0;
        for(PlayerColor p : ClientContext.get().getMyPlayer().getMark()){
            ImageView damages = createMyTear(p.toString());
            damages.setFitHeight(screenHeight * 2 /100);
            StackPane.setAlignment(damages,Pos.BOTTOM_LEFT);
            StackPane.setMargin(damages,new Insets(0,0,screenHeight * 13.5 / 100,screenWidth * 17.6 / 100 + spaceX));
            spaceX = spaceX + screenWidth * 1 / 100;
        }
        for(PlayerColor p : ClientContext.get().getMyPlayer().getThisTurnMarks()){
            ImageView damages = createMyTear(p.toString());
            damages.setFitHeight(screenHeight * 2 /100);
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
     * refresh my player ammo
     */
    private synchronized void  refreshMyPlayerAmmo(){
        //ammo
        deleteMyPlayerAmmo();
        double spaceX = 0;
        double spaceY = 0;
        int k = 1;
        List<game.model.Color> color = new ArrayList<>(ClientContext.get().getMyPlayer().getAmmo());
        for(game.model.Color c : color){
            Rectangle ammo = new Rectangle(screenHeight * 2 /100,screenHeight * 2 /100, Color.valueOf(c.toString()));
            ammo.setId(c.toString());
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
    private void deleteMyPlayerAmmo() {
        for (Rectangle r : myAmmo) {
            r.setFill(Color.TRANSPARENT);
            map.getChildren().remove(r);
        }
        myAmmo.clear();
    }
    private synchronized void refreshWeaponCard(){
        for(Square q : ClientContext.get().getMap().getSpawnpoints()){
            if(q.getX() == 0 ){
                for (int x = 0 ; x < 3 ; x++) {
                    if(x < q.getWeapons().size()){
                        mapWL.get(x).setImage(createWeaponCard(q.getWeapons().get(x).getId()));
                        mapWL.get(x).setId("1_"+q.getWeapons().get(x).getId());
                    }
                    else{
                        mapWL.get(x).setImage(createWeaponCard(0));
                        mapWL.get(x).setId("1_0");
                    }
                }
            }else
                if(q.getX() == 3){
                    for (int z = 0 ; z < 3 ; z++) {
                        if(z < q.getWeapons().size()){
                            mapWR.get(z).setImage(createWeaponCard(q.getWeapons().get(z).getId()));
                            mapWR.get(z).setId("3_"+q.getWeapons().get(z).getId());
                        }
                        else{
                            mapWR.get(z).setImage(createWeaponCard(0));
                            mapWR.get(z).setId("3_0");
                        }
                    }
                }
                else {
                    for (int y = 0 ; y < 3 ; y++) {
                        if(y < q.getWeapons().size()){
                            mapWT.get(y).setImage(createWeaponCard(q.getWeapons().get(y).getId()));
                            mapWT.get(y).setId("2_"+q.getWeapons().get(y).getId());
                        }
                        else{
                            mapWT.get(y).setImage(createWeaponCard(0));
                            mapWT.get(y).setId("2_0");
                        }
                    }
                }

        }
    }

    private void showMapGame(){
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        createMapSquareAmmo();
        refreshWeaponCard();
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
        textNotify.setText("Wait Your Turn:");
    }

    /**
     * show the name of the player
     * @param e
     */
    private void handlePlayerClick(MouseEvent e){
        text.setText("Player "+((Circle)e.getSource()).getId());
    }

    /**
     *Identify the square selected 
     * @param e
     */
    private void handleSquareClick(MouseEvent e){
        Square choosenSquare;
        int i = Integer.parseInt(((Rectangle)e.getSource()).getId());
        textNotify.setText("You selected square in: " + (i-1)/4 + ", " + (i-1)%4 + "\n" + textNotify.getText());
        choosenSquare = possiblePositions.stream().filter( p -> p.getX() == (i-1)%4 && p.getY() == (i-1)/4).findFirst().orElse(null);
        controller.getClient().sendMessage(new ChooseSquareResponse(choosenSquare));
        this.possiblePositions = null;
        disableSquare();
    }

    /**
     * Identify the weapon selected and check the state fot the response
     * @param e
     */
    private void handleWeaponClick(MouseEvent e){
        int id = Integer.parseInt(((ImageView) e.getSource()).getId().substring(2));
        CardWeapon selected;
        if(e.getButton() == MouseButton.SECONDARY){
            System.out.println("Right Click");
            StackPane sp = new StackPane();
            selected = ClientContext.get().getMyPlayer().getWeapons().stream().filter(w -> w.getId() == id).findFirst().orElse(null);
            selected = ClientContext.get().getMap().getWeaponOnMap().stream().filter(w->w.getId() == id).findFirst().orElse(selected);
            if(selected != null) {
                sp.setPrefSize(screenWidth * 30 / 100, screenHeight * 50 / 100);
                VBox vbox = new VBox(15);
                Label nameW = new Label("Weapon : " +selected.getName());
                Label name = new Label("Base Effect:");
                Label desc = new Label("Description " +selected.getBaseEffect().getDescription());
                desc.setMaxWidth(screenWidth * 30 / 100);
                desc.setWrapText(true);
                vbox.getChildren().addAll(nameW, name, desc);
                VBox.setMargin(nameW, new Insets(0,0,0,10));
                VBox.setMargin(name, new Insets(0,0,0,25));
                VBox.setMargin(desc, new Insets(0,0,0,40));
                if(selected.getPlusEffects() != null)
                    for (FullEffect fe : selected.getPlusEffects()) {
                        name = new Label("Optional effect: "+fe.getName());
                        desc = new Label(fe.getDescription());
                        desc.setWrapText(true);
                        vbox.getChildren().addAll(name, desc);
                        VBox.setMargin(name, new Insets(0,0,0,25));
                        VBox.setMargin(desc, new Insets(0,0,0,40));
                    }
                if (selected.getAltEffect() != null) {
                    name = new Label("Alternative effect: "+ selected.getAltEffect().getName());
                    desc = new Label(selected.getAltEffect().getDescription());
                    desc.setWrapText(true);
                    vbox.getChildren().addAll(name, desc);
                    VBox.setMargin(name, new Insets(0 ,0,0,25));
                    VBox.setMargin(desc, new Insets(0,0,0,40));
                }

                //sp.getChildren().forEach(z ->StackPane.setAlignment(z,Pos.TOP_LEFT));
                sp.getChildren().add(vbox);
                Scene infoWeapon = new Scene(sp);
                sg.setScene(infoWeapon);
                primaryStage.setAlwaysOnTop(false);
                sg.setAlwaysOnTop(true);
                sg.show();
            }
        }
        else {
            List<game.model.Color> price;
            List<CardPower> list;
            switch (state) {
                case CHOOSEWEAPONTOGRAB: {
                    weaponG = weaponToGrab.stream().filter(w -> w.getId() == id).findFirst().orElse(null);
                    if (ClientContext.get().getMyPlayer().getWeapons().size() == 3) {
                        chooseWeaponToWaste();
                    } else {
                        if (weaponG.getPrice().size() == 1) {
                            controller.getClient().sendMessage(new PickUpWeaponRequest(weaponG, null, null));
                        } else {
                            list = possibleCardPowerToPay(weaponG.getPrice().subList(1,weaponG.getPrice().size()));
                            if (list.size() != 0)
                                choosePowerUpToPay(list);
                            else
                                controller.getClient().sendMessage(new PickUpWeaponRequest(weaponG, null, null));
                        }
                    }
                    disableWeapon();
                    break;
                }
                case CHOOSEWEAPONTOWASTE: {
                    weaponW = ClientContext.get().getMyPlayer().getWeapons().stream().filter(w -> w.getId() == id).findFirst().orElse(null);
                    if (weaponG.getPrice().size() == 1) {
                        controller.getClient().sendMessage(new PickUpWeaponRequest(weaponG, null, weaponW));
                    }else{
                        price = weaponG.getPrice().subList(1,weaponG.getPrice().size());
                        list = new ArrayList<>(ClientContext.get().getMyPlayer().getCardPower().stream().filter(n -> price.contains(n.getColor())).collect(Collectors.toList()));
                        if (list.size() != 0) {
                            choosePowerUpToPay(list);
                        }
                        else {
                            controller.getClient().sendMessage(new PickUpWeaponRequest(weaponG, null, weaponW));
                        }
                    }
                    disableMyWeapon();
                    break;
                }
                case CHOOSEWEAPONTOSHOOT: {
                    selected = ClientContext.get().getMyPlayer().getWeapons().stream().filter(w -> w.getId() == id).findFirst().orElse(null);
                    controller.getClient().sendMessage(new ChooseWeaponToShootResponse(selected));
                    disableMyWeapon();
                    break;
                }
                case CHOOSERELOAD: {
                    selected = ClientContext.get().getMyPlayer().getWeapons().stream().filter(w -> w.getId() == id).findFirst().orElse(null);
                    System.out.println("selected reload " + selected.getName());
                    disableMyWeapon();
                    if (selected != null) {
                        weaponToReload = selected;
                        price = selected.getPrice();
                        list = new ArrayList<>(ClientContext.get().getMyPlayer().getCardPower().stream().filter(n -> price.contains(n.getColor())).collect(Collectors.toList()));
                        if(list.size() == 0)
                            choosePowerUpToPay(list);
                        else
                            reloadRequests.add(new ReloadWeaponRequest(weaponToReload,null));
                        weaponsToReload.remove(weaponToReload);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Activate the weapon to waste
     */
    private void chooseWeaponToWaste(){
        text.setText("Choose weapon to waste: ");
        state = ClientState.CHOOSEWEAPONTOWASTE;
        for(ImageView iv : weapons){
            iv.setEffect(new DropShadow(35, Color.GREEN));
            iv.setOnMouseClicked(this::handleWeaponClick);
        }
    }

    /**
     * Join the selected room
     * @param rb
     */
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
        controller.getClient().sendMessage(new JoinWaitingRoomRequest(id, user));
    }

    /**
     * Create the room with the selected map
     * @param e
     */
    private void handleNewRoom(ActionEvent e){

        //Blank pane
        StackPane waits = new StackPane();
        textwait = new Label();
        textwait.setText("Wait Other Player");
        textwait.setTextFill(Color.WHITE);
        textwait.setWrapText(true);
        waits.getChildren().add(textwait);
        StackPane.setAlignment(textwait,Pos.CENTER);
        Scene wait = new Scene(waits);

        Image background = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/background01.jpg"));
        BackgroundImage bi = new BackgroundImage(background,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        waits.setBackground(new Background(bi));

        StackPane sp = new StackPane();
        sp.setPrefSize(500,300);
        sp.setBackground(new Background(bi));
        Scene room = new Scene(sp);
        Image iMap = new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/mappa1.png"));
        ImageView ivMap = new ImageView(iMap);
        Label text = new Label("Choose map id");
        text.setTextFill(Color.WHITE);
        Label error = new Label("");
        sp.getChildren().add(ivMap);
        ivMap.setFitHeight(150);
        ivMap.setPreserveRatio(true);
        ChoiceBox mapc = new ChoiceBox();
        mapc.setItems(FXCollections.observableArrayList("Map 1", "Map 2", "Map 3 ", "Map 4"));
        mapc.getSelectionModel().selectedIndexProperty().addListener((ov, oldSelected, newSelected) -> {
            int i = newSelected.intValue() + 1;
            ivMap.setImage(new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("graphics/map/mappa"+i+".png")));
        });
        mapc.getSelectionModel().selectFirst();
        Button submit = new Button("Create Room");


        ObservableList list = sp.getChildren();
        list.addAll(text,mapc, submit,error);
        StackPane.setAlignment(ivMap,Pos.TOP_RIGHT);
        StackPane.setMargin(ivMap,new Insets(60,60,0,0));
        StackPane.setAlignment(text,Pos.TOP_CENTER);
        StackPane.setMargin(text,new Insets(25,0,0,0));
        StackPane.setAlignment(mapc,Pos.CENTER_LEFT);
        StackPane.setMargin(mapc,new Insets(0,0,0,50));
        StackPane.setAlignment(submit,Pos.BOTTOM_RIGHT);
        StackPane.setMargin(submit,new Insets(0,35,30,0));
        StackPane.setAlignment(error,Pos.BOTTOM_LEFT);

        primaryStage.setScene(room);
        primaryStage.show();

        submit.setOnAction(actionEvent -> {
            if(mapc.getValue() != null){
                callRoomCreate(mapc.getValue().toString());
                primaryStage.setScene(wait);
            }
            else
                error.setText("Invalid Map choise");
        });
    }

    /**
     * Rotate and zoom the weapon on mouse over
     * @param e
     */
    private void handleMouseOnWeapon(MouseEvent e){
        ((ImageView)e.getSource()).setViewOrder(-1);
        ((ImageView)e.getSource()).setRotate(0);
        ((ImageView)e.getSource()).setFitWidth(screenWidth*12/100);
        ((ImageView)e.getSource()).setPreserveRatio(true);
    }

    /**
     * resize the weapon when the mouse isn't over
     * @param e
     */
    private void handleMouseOutWeapon(MouseEvent e){
        ((ImageView)e.getSource()).setViewOrder(0);
        switch (((ImageView)e.getSource()).getId().substring(0,1)){
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

    /**
     * Send the respawn request from the selected PU
     * @param e
     */
    private void handleRespawnSquare(MouseEvent e){
        CardPower p = ClientContext.get().getMyPlayer().getCardPower().stream().filter(l->l.getId() == Integer.parseInt(((ImageView)e.getSource()).getId())).findFirst().orElse(null);
        controller.getClient().sendMessage(new RespawnResponse((p)));
        disablePowerUp();
    }

    /**
     * Identify the power up selected and check the state fot the response
     * @param e
     */
    private void handlePowerUpClick(MouseEvent e){
        CardPower p = ClientContext.get().getMyPlayer().getCardPower().stream().filter(l->l.getId() == Integer.parseInt(((ImageView)e.getSource()).getId())).findFirst().orElse(null);
        if(state.equals(ClientState.CHOOSECOUNTER)) {
            controller.getClient().sendMessage(new CounterAttackResponse(p, shooter));
            state = ClientState.WAITING_TURN;
        }
        if(state.equals(ClientState.CHOOSECARDPOWER))
            controller.getClient().sendMessage(new ChoosePowerUpResponse(p));
        if(state.equals(ClientState.CHOOSESCOPE)){
            scopeSelected = p;
            activateAmmo();
        }
        disablePowerUp();
    }

    /**
     * make the My player ammo clickable 
     */
    private void activateAmmo(){
        for(Rectangle a : myAmmo){
            a.setOnMouseClicked(this::handleAmmoClick);
        }
    }

    /**
     * disable the ammo click
     */
    private void disableAmmo(){
        for(Rectangle a : myAmmo){
            a.setOnMouseClicked(null);
        }
    }

    /**
     * Identify the ammo selected and check the Client state fot the response
     * @param e
     */
    private void handleAmmoClick(MouseEvent e ){
        System.out.println("ammo");
        if(state.equals(ClientState.CHOOSESCOPE)) {
            game.model.Color c = game.model.Color.valueOf(((Rectangle) e.getSource()).getId());
            controller.getClient().sendMessage(new ChoosePowerUpResponse(scopeSelected, c, null));
        }
        disableAmmo();
    }

    /**
     * Identify the button selected and call the action response
     * @param e
     */
    private void handleStepAction(MouseEvent e){
        Action chosenAction = Action.valueOf(((Button)e.getSource()).getId().toUpperCase());
        switch (chosenAction){
            case MOVEMENT:
                if(state.equals(ClientState.CHOOSESTEP)) {
                    controller.getClient().sendMessage(new MovementActionRequest());
                }
                else {
                    controller.getClient().sendMessage(new ChooseTurnActionResponse(Action.MOVEMENT));
                }
                break;
            case SHOOT:
                if(state.equals(ClientState.CHOOSESTEP))
                    controller.getClient().sendMessage(new ShootActionRequest());
                else
                    controller.getClient().sendMessage(new ChooseTurnActionResponse(Action.SHOOT));
                break;
            case GRAB:
                if(state.equals(ClientState.CHOOSESTEP))
                    controller.getClient().sendMessage(new GrabActionRequest());
                else
                    controller.getClient().sendMessage(new ChooseTurnActionResponse(Action.GRAB));
                break;
            case EXIT:
                controller.getClient().sendMessage(new EndTurnRequest());
                break;
            case POWER:
                state = ClientState.CHOOSECARDPOWER;
                List<CardPower> powerList = ClientContext.get().getMyPlayer().getCardPower().stream().filter(c -> !c.isUseWhenAttacking() && !c.isUseWhenDamaged()).collect(Collectors.toList());
                if(powerList.isEmpty()) {
                    textNotify.setText("There are no power-up cards available for use in this moment!\n" + textNotify.getText());
                    chooseStepActionPhase();
                }
                else
                    activateCardPower(powerList);
                break;
        }
        disableButton();
    }
    private void disableButton(){
        move.setVisible(false);
        move.setDisable(true);
        shoot.setVisible(false);
        shoot.setDisable(true);
        grab.setVisible(false);
        grab.setDisable(true);
        exit.setDisable(true);
        exit.setVisible(false);
        power.setDisable(true);
        power.setVisible(false);
    }
    private void disablePowerUp(){
        for (ImageView iv :powerUp) {
            iv.setOnMouseClicked(null);
            iv.setEffect(null);
        }
    }
    private void disableMyWeapon(){
        for (ImageView iv :weapons) {
            iv.setOnMouseClicked(null);
            iv.setEffect(null);
            Tooltip.uninstall(iv,toolw);
        }
    }
    private void disableWeapon(){
        for (ImageView iv :mapWL) {
            iv.setOnMouseClicked(null);
            iv.setEffect(null);
            Tooltip.uninstall(iv,toolw);
        }
        for (ImageView iv :mapWR) {
            iv.setOnMouseClicked(null);
            iv.setEffect(null);
            Tooltip.uninstall(iv,toolw);
        }

        for (ImageView iv :mapWT) {
            iv.setOnMouseClicked(null);
            iv.setEffect(null);
            Tooltip.uninstall(iv,toolw);
        }

    }
    private void disableSquare(){
        for (Rectangle r :squares) {
            r.setStroke(Color.TRANSPARENT);
            r.setOnMouseClicked(null);
        }
    }
}
