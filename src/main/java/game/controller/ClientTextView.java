package game.controller;

import game.LaunchClient;
import game.controller.commands.ClientGameMessage;
import game.controller.commands.clientcommands.*;
import game.model.*;
import game.model.effects.FullEffect;
import game.model.effects.SimpleEffect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ClientTextView implements  View {
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_GREY = "\u001b[37m";
    private ClientController controller;
    private Scanner fromKeyBoard;
    private List<GameMap> availableMaps;
    private Thread waitingThread;



    public ClientTextView(){
       initInput();
    }

    public ClientTextView(ClientController controller) {
        this.controller = controller;
        initInput();
    }

    private void initInput()
    {
        this.fromKeyBoard = new Scanner(System.in);
        /*File file = new File("input.txt");
        try {
            fromKeyBoard = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    public synchronized void writeText (String text){
        System.out.println(">> " + text);
    }

    public synchronized String readText(){
        initInput();
        String string = null;
        try {
            string = fromKeyBoard.nextLine();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public synchronized char readChar(){
        try {
            String string = readText();
            return string.charAt(0);
        } catch (StringIndexOutOfBoundsException e) {
            return '!';
        }
    }

    /**
     * Read an integer from the input stream
     * @return
     */
    public  synchronized int readInt(){

        String number;
        int n = 0;
        boolean retry;
        do {
            retry = false;
            number = readText();
            try {
                n = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, expected a number");
                retry = true;
            }
        }while(retry);

        return n;
        /*boolean retry;
        do{
            try {
                if(fromKeyBoard.hasNextInt())
                {   int save = fromKeyBoard.nextInt();
                    fromKeyBoard.nextLine();
                    return save;
                }
                retry = true;
                Thread.sleep(10);

            }catch (InputMismatchException e){
                System.out.println("Wrong input, expected a numeber");
                fromKeyBoard.nextLine();
                return readInt();
            }catch(IndexOutOfBoundsException  | IllegalStateException | InterruptedException e)
            {
                retry = true;
            }
        }while(true);*/
    }

    /**
     * Starting phase during the player enter his username for the game
     */
    public void setUserNamePhase (){
        Random r  = new Random();
        do {
            writeText("Provide username:");
            ClientContext.get().setUser(readText());
            //ClientContext.get().setUser("USER" + r.nextInt());
        } while (  ClientContext.get().getUser() == null);

        writeText("Wait for login...");
        controller.getClient().sendMessage(new LoginMessage(ClientContext.get().getUser()));
    }

    /**
     * Show available maps and ask server for available rooms
     * @param availableMaps
     */
    public void showMapsPhase(List<GameMap> availableMaps)
    {
        this.availableMaps = availableMaps;
        printAvailableMaps();
        controller.getClient().sendMessage(new GetWaitingRoomsRequest());
    }

    @Override
    public void reloadWeaponPhase(List<CardWeapon> weaponsToReload) {
        writeText("Here you will be able to choose what weapon reload from the unloaded ones!");
        int n;
        List<ClientGameMessage> reloadRequests = new ArrayList<>();
        List<CardPower> cp = null;
        do {
               showWeapons(weaponsToReload,0,true, true, false);
            do {
                writeText("Insert the id of a weapon you want to reload or -1 to terminate the reload phase:");
                n = readInt();
            } while (n != -1 && n < 1 && n > weaponsToReload.size());
            if(n != -1)
            {
                cp = powerUpSelection(weaponsToReload.get(n-1).getPrice());
                reloadRequests.add(new ReloadWeaponRequest(weaponsToReload.get(n-1),cp));
                weaponsToReload.remove(weaponsToReload.get(n-1));
            }
        }while(weaponsToReload.size() > 0 && n!=-1);

        if(n != -1)
            controller.sendMessages(reloadRequests);
        else
            controller.getClient().sendMessage(new EndActionRequest());

    }

    @Override
    public void showReloadMessage(CardWeapon cW) {
        writeText("The weapon " + cW.getName() + " has been correctly reloaded");
    }

    @Override
    public void notifyPlayerSuspended(Player p) {
        writeText("Player " + p.getNickName() +  " has been suspended from the game because of connection lost");
    }

    @Override
    public void timeOutPhase() {
        char choice;
        writeText("You timed out and you have been kicked out!");
        if(this.controller.getState() != ClientState.GAME_END) {
            writeText("Do you want to rejoin the game?[Y/N]");
            do{
                choice = readChar();
            }while(choice != 'Y' && choice != 'y' && choice != 'n' && choice != 'N' && controller.getState() != ClientState.GAME_END);

            if(choice == 'Y' || choice == 'y')
            {
                if(controller.getState() == ClientState.GAME_END)
                    writeText("The game is terminated!");
                else
                    controller.getClient().sendMessage(new RejoinGameResponse(true,ClientContext.get().getUser()));
            }
            else{
                this.controller.stopListening();
            }
        }
    }

    @Override
    public void alreadyLoggedPhase() {
        writeText("There is already a user logged with this name on the server! Choose another name:");
        setUserNamePhase();
    }

    @Override
    public void loginCompletedPhase() {
        writeText("You correctly logged in!");
        controller.getClient().sendMessage(new GetAvailableMapsRequest());
    }

    @Override
    public void rejoinGameConfirm() {
        writeText("You successfully rejoin your previous game! Now wait for your turn..");
    }

    @Override
    public void notifyPlayerRejoin(Player p) {
        writeText("Player " + p.getNickName() + " has rejoined the game!");
    }

    @Override
    public void notifyPlayerJoinedWaitingRoom(Player p) {
        writeText("Player " + p.getNickName() + " has joined the waiting room!");
    }

    @Override
    public void setController(ClientController clientController) {
        this.controller = clientController;
    }

    @Override
    public void waitStart(){

        waitingThread = new Thread( () -> {
            String s;
            do {
                //s = readText();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                   return;
                }
            } while (/*!s.equals("exit") &&*/ !controller.isGameStarted());
            //to flush the scanner, it seems to be the unique way
            initInput();
        });
        waitingThread.setName("WAITING START THREAD");
        waitingThread.start();
    }

    @Override
    public void chooseConnection() {
        String choice, ip;
        writeText("Choose the connection type");
        do{
            writeText("Insert Socket[S] or RMI[R]:");
            choice = fromKeyBoard.nextLine();
            choice = choice.toUpperCase();
            if(choice.equals("S"))
                choice = "SOCKET";
            else if(choice.equals("R"))
                choice = "RMI";
        }while(!choice.equals("RMI") && !choice.equals("SOCKET"));
        writeText("Insert the server IP (or localhost [L]):");
        ip = fromKeyBoard.nextLine();
        if(ip.equalsIgnoreCase("L"))
            ip = "localhost";
        LaunchClient.startConnection(choice,ip);
    }

    @Override
    public void chooseCounterAttack(List<CardPower> counterattack, Player shooter) {
        char c;
        int n = 1;
        writeText("Do you want to counterattack "+checkPlayerColor(shooter.getColor())+shooter.getNickName()+ANSI_RESET+"?");
        writeText("Insert [Y]es or [N]o");
        do {
            c = readChar();
        }while(c != 'Y' && c != 'y' && c != 'N' && c != 'n');
        if(c == 'Y' || c == 'y'){
            if(counterattack.size() > 1){
                writeText("Choose one of your power-up cards to complete the operation:");
                for(CardPower cp : counterattack) {
                    writeText(n + ". " + cp.toString());
                    n++;
                }
                do{
                    n = readInt();
                }while(n <= 0 && n > counterattack.size());
            }
            controller.getClient().sendMessage(new CounterAttackResponse(counterattack.get(n-1),shooter));
        }
        else
            controller.getClient().sendMessage(new CounterAttackResponse());

    }

    @Override
    public void notifyPlayerLeavedWaitingRoom(Player p) {
        writeText("Player " + p.getNickName() + " has leaved the waiting room!");
    }

    @Override
    public void rejoinGamePhase(List<String> otherPlayers) {
        char choice;
        boolean rejoin;
        writeText("Hi " + ClientContext.get().getUser() + ", welcome back! We have found on the server a game which is currently running where you where previously playing. This are the players actually connected in this game:");
        otherPlayers.forEach(this::writeText);
        do{
            writeText("Do you want to rejoin this game? [Y/N]:");
            choice = readChar();
        }while(choice != 'Y' && choice != 'y' && choice != 'n' && choice != 'N');

        rejoin = choice == 'Y' || choice == 'y';
        controller.getClient().sendMessage(new RejoinGameResponse(rejoin,ClientContext.get().getUser()));

    }

    @Override
    public void chooseWeaponToShoot(List<CardWeapon> myWeapons) {
        int n;
        writeText("Choose which of your weapons you want to use:");
        showWeapons(myWeapons,0,true, false, false);
        do{
            n = readInt();
        }while (n<1 || n>myWeapons.size());
        controller.getClient().sendMessage(new ChooseWeaponToShootResponse(myWeapons.get(n-1)));
    }

    @Override
    public void chooseFirstEffect(FullEffect baseEff, FullEffect altEff) {
        int n;
        List<FullEffect> effects = new ArrayList<>();
        List<CardPower> toUse = new ArrayList<>();
        effects.add(baseEff);
        effects.add(altEff);
        writeText("Choose the base effect you want to apply:");
        showEffects(effects,true);
        do{
            n = readInt();
        }while(n != 1 && n != 2);
        if(n == 2 && altEff.getPrice() != null)
            toUse = powerUpSelection(altEff.getPrice());
        controller.getClient().sendMessage(new ChooseFirstEffectResponse(n,toUse));
    }

    @Override
    public void usePlusBeforeBase(FullEffect plusEff) {
        char t;
        List<FullEffect> effects = new ArrayList<>();
        List<CardPower> toUse = new ArrayList<>();
        effects.add(plusEff);
        writeText("Do you want to use this plus effect before than your weapon base effect?");
        showEffects(effects,false);
        writeText("[Y]es or [N]o?");
        do{
            t = readChar();
        }while (t != 'Y' && t != 'y' && t != 'N' && t != 'n');
        if(plusEff.getPrice() != null && (t == 'y' || t == 'Y'))
            toUse = powerUpSelection(plusEff.getPrice());
        controller.getClient().sendMessage(new UsePlusBeforeResponse(plusEff,t,toUse));
    }

    @Override
    public void usePlusInOrder(List<FullEffect> plusEffects) {
        List<CardPower> toUse = new ArrayList<>();
        char t;
        writeText("Do you want to apply the plus effect, allow by your weapon, listed here:");
        showEffects(Collections.singletonList(plusEffects.get(0)),false);
        writeText("[Y]es or [N]o?");
        do{
            t = readChar();
        }while (t != 'Y' && t != 'y' && t != 'N' && t != 'n');
        if(plusEffects.get(0) != null && (t == 'y' || t == 'Y'))
            toUse = powerUpSelection(plusEffects.get(0).getPrice());
        controller.getClient().sendMessage(new UseOrderPlusResponse(plusEffects,toUse,t));
    }

    @Override
    public void choosePlusEffect(List<FullEffect> plusEffects) {
        List<CardPower> toUse = new ArrayList<>();
        int n;
        int i=1;
        FullEffect toApply = null;
        writeText("Choose the plus effect you want to apply or insert -1 to terminate your action:");
        showEffects(plusEffects,true);
        do{
            n = readInt();
        }while(n != -1 && (n<1 || n>plusEffects.size()));
        if(n == -1)
            controller.getClient().sendMessage(new TerminateShootAction());
        else {
            toApply = plusEffects.get(n-1);
            plusEffects.remove(n-1);
            if(toApply.getPrice() != null)
                toUse = powerUpSelection(toApply.getPrice());
            controller.getClient().sendMessage(new UsePlusEffectResponse(plusEffects, toApply, toUse));
        }
    }

    /**
     * Show the available maps
     */
    private void printAvailableMaps()
    {
        writeText("Available game maps:\n");
        for(GameMap m : availableMaps) {
            writeText("MAP-ID : "+m.getId()+"\n");
            showMap(m);
            System.out.println();
        }
    }

    /**
     * Show available rooms and choose if join one of these or create a new one
     * @param waitingRooms
     */
    public void chooseRoomPhase(List<WaitingRoom> waitingRooms)
    {
        if(waitingRooms.isEmpty()){
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
            if((nRoom <= 0 || nRoom > waitingRooms.size()) && nRoom != -1)
                writeText("Invalid waiting room number!");
        }while((nRoom <= 0 || nRoom > waitingRooms.size()) && nRoom != -1);
        if(nRoom != -1)
        {
            controller.getClient().sendMessage(new JoinWaitingRoomRequest(nRoom,ClientContext.get().getUser()));
        }
        else
            createRoomPhase();
    }

    /**
     * Ask to create a new waiting room on the server
     */
    public void createRoomPhase()
    {
        int mapId, nPlayer;

        synchronized (this) {
            do {
                writeText("Enter the id of the map you want to use in your game:");
                mapId = readInt();
            } while (mapId < 1 || mapId > availableMaps.size());

            /*do {
                writeText("Enter the number of player to start the game [3-5]:");
                nPlayer = readInt();
            } while (nPlayer < 3 || nPlayer > 5);*/
        }

        controller.getClient().sendMessage(new CreateWaitingRoomRequest(mapId,ClientContext.get().getUser()));
    }

    /**
     * Choose a single step for the current action the player want to make
     */
    public void chooseStepActionPhase(){
        List<Action> possibleActions = this.controller.getAvailableActions();
        Action chosenAction = null;
        String action;
        do{
            writeText("The action you have selected preview this ordered single step combination, choose the next: (info for player information/exit[e] to stop action)");
            for(Action ac : possibleActions){
                System.out.print(">> "+ac.name());
                switch(ac.name()){
                    case "MOVEMENT":
                        System.out.println(" [M]");
                        break;
                    case "GRAB":
                        System.out.println(" [G]");
                        break;
                    case "SHOOT":
                        System.out.println(" [S]");
                        break;
                }
            }
            action = readText();
            action = action.toUpperCase();
            try {
                if(action.equals("E"))
                    action = "EXIT";

                if(action.equals("EXIT") && controller.getState() == ClientState.HANDLING_MOVEMENT){
                    controller.getClient().sendMessage(new EndActionRequest());
                    return;
                }

                if(action.equals("INFO")){
                    showMap(ClientContext.get().getMap());
                    showMyPlayerInformation();
                    showPlayerPosition();
                    chosenAction = null;
                }
                /*
                if(action.equals("POWER"))
                {
                    List<CardPower> powerUpsAvailable = ClientContext.get().getMyPlayer().getCardPower().stream().filter(c -> !c.isUseWhenAttacking() && !c.isUseWhenDamaged()).collect(Collectors.toList());
                    if(powerUpsAvailable.isEmpty())
                    {
                        writeText("There are no power-up cards available for use in this moment!");             action = "";
                    }
                    else {
                        powerUpsAvailable = powerUpSelection();
                        controller.getClient().sendMessage(new ChoosePowerUpResponse(choosePowerUp(powerUpsAvailable)));
                    }

                }*/

                if(action.equals("M"))
                    action = "MOVEMENT";
                else if(action.equals("G"))
                    action = "GRAB";
                else if(action.equals("S"))
                    action = "SHOOT";

                chosenAction = Action.valueOf(action);
            }catch ( NullPointerException f){
                chosenAction = null;
            }catch (IllegalArgumentException e){
                chosenAction = null;
            }
        }while(/*!action.equals("POWER") && */(chosenAction == null || (!possibleActions.contains(chosenAction))));

        if(chosenAction != null)
            switch (chosenAction){
                case EXIT:
                    return;
                case GRAB:
                    controller.getClient().sendMessage(new GrabActionRequest());
                    break;
                case SHOOT:
                    controller.getClient().sendMessage(new ShootActionRequest());
                    break;
                case MOVEMENT:
                    controller.setState(ClientState.HANDLING_MOVEMENT);
                    controller.getClient().sendMessage(new MovementActionRequest());
                    break;
            }
    }

    /**
     * Choose one of the proposed squares
     * @param possibleSquare
     */
    public  void chooseSquarePhase(List<Square> possibleSquare){
        showMap(ClientContext.get().getMap());
        Square choosenSquare = null;
        String square;
        int posX;
        int posY;
        do{
            writeText("Choose a square to complete your move: ");
            for(Square sq : possibleSquare){
                if(sq != null)
                    writeText("X :" + sq.getX() + " Y : " + sq.getY());
            }
            writeText("Write X position: ");
            posX = readInt();
            writeText("Write Y position: ");
            posY = readInt();
            for(Square sq : possibleSquare){
                if(sq.getX() == posX && sq.getY() == posY){
                    choosenSquare = sq;
                }
            }
        }while(!possibleSquare.contains(choosenSquare));
        controller.getClient().sendMessage(new ChooseSquareResponse(choosenSquare));
    }

    /**
     * Choose a specified number of targets from the proposed
     * @param possibleTarget
     */
    public void chooseTargetPhase(List<Target> possibleTarget){
        int maxE = ClientContext.get().getCurrentEffect().getMaxEnemy();
        int minE = ClientContext.get().getCurrentEffect().getMinEnemy();
        int i = 0;
        int k= 12;
        List<Target> choosenTarget = new ArrayList<>();
        writeText("Choose between " + minE + " and " + maxE + " targets to apply your attack: (write the number)");
        writeText(i + " to exit");
        i++;
        for (Target tg : possibleTarget) {
            writeText(i + ". " + tg.toString());
            i++;
        }
        for (i = 0; i < maxE && k > 0; ) {
            k = readInt();
            if (k <= possibleTarget.size() && k > 0) {
                if (choosenTarget.contains(possibleTarget.get(k - 1))) {
                    writeText("Target already selected");
                } else {
                    choosenTarget.add(possibleTarget.get(k - 1));
                    i++;
                }
            } else if (k == 0) {
                if (choosenTarget.size() >= minE)
                    writeText("Selection Completed!");
                else {
                    k = 12;
                    writeText("You haven't selected enough targets!");
                }
            } else {
                writeText("Not enough target");
            }
        }
        controller.getClient().sendMessage(new ChooseTargetResponse(choosenTarget));
    }

    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    /**
     * Choose the action for the current turn
     */
    public void chooseTurnActionPhase(){
        Action choosenAction;
        String action;
        do{
            //clearConsole();
            //showMap(ClientContext.get().getMap());
            writeText("Choose the action you want to make between {MOVEMENT[M], GRAB[G], SHOOT[S]} (write info to see the details of the game, write power to use power-up cards): ");
            action = readText();
            action = action.toUpperCase();
            try {
                if(action.equals("INFO")){
                    showMap(ClientContext.get().getMap());
                    showMyPlayerInformation();
                    showPlayerPosition();
                    choosenAction = null;
                }
                else if(action.equals("POWER"))
                {
                    List<CardPower> powerList = ClientContext.get().getMyPlayer().getCardPower().stream().filter(c -> !c.isUseWhenAttacking() && !c.isUseWhenDamaged()).collect(Collectors.toList());
                    if(powerList.isEmpty()) {
                        writeText("There are no power-up cards available for use in this moment!");
                        action = "";
                    }
                    else
                        controller.getClient().sendMessage(new ChoosePowerUpResponse(choosePowerUp(powerList)));
                    choosenAction = null;
                }
                else if(action.equals("EXIT"))
                {
                    choosenAction = null;
                }
                else{
                    if(action.equalsIgnoreCase("M"))
                        action = "MOVEMENT";
                    else if(action.equalsIgnoreCase("G"))
                        action = "GRAB";
                    else if(action.equalsIgnoreCase("S"))
                        action = "SHOOT";
                    choosenAction = Action.valueOf(action);
                }
            }
            catch (IllegalArgumentException | NullPointerException e) {
                choosenAction = null;
            }

        }while(choosenAction==null && !action.equals("POWER"));
        if(!action.equals("POWER"))
            controller.getClient().sendMessage(new ChooseTurnActionResponse(choosenAction));
    }

    /**
     * Notify invalid weapon selection
     */
    public void invalidWeaponNotification(){
        writeText("The weapon selected is not valid!");
    }

    /**
     * Notify invalid action selection
     */
    @Override
    public void invalidActionNotification(){
        writeText("The action selected is not valid!");
    }

    /**
     * Notify impossibility to do others actions fort this current turn
     */
    @Override
    public void insufficientNumberOfActionNotification(){
        writeText("You cannot do others actions for this turn!");
    }

    /**
     * Notify impossibility that the player doesn't have enough ammo
     */
    @Override
    public void insufficientAmmoNotification() {
        writeText("Not enough ammo");
        controller.resumeState();

    }

    /**
     * Notify invalid targets selection
     */
    @Override
    public void invalidTargetNotification(){
        writeText("The targets selected are not valid!");
    }

    /**
     * Notify invalid step selection
     * MANAGED
     */
    @Override
    public void invalidStepNotification(){
        writeText("The step selected is not valid, you loose the action!! xd!!1!1!!");
    }

    /**
     * Notify invalid targets election
     */
    @Override
    public void maxNumberOfWeaponNotification(){
        writeText("You can't grab others weapons from the ground, before discard one of your weapon!");
    }

    /**
     * notify a player that someone took damage
     * @param idShooter
     * @param dmg
     * @param idHitted
     */
    @Override
    public void damageNotification(int idShooter, int dmg, int idHitted){
        if(idHitted == ClientContext.get().getMyID())
            writeText("Player "+ClientContext.get().getMap().getPlayerById(idShooter).getNickName()+" dealt "+dmg+" damage to you");
        else if(idShooter != ClientContext.get().getMyID())
            writeText("Player "+ClientContext.get().getMap().getPlayerById(idShooter).getNickName()+" dealt "+dmg+" damage to player "+ClientContext.get().getMap().getPlayerById(idHitted).getNickName());
        else
            writeText("You dealt "+dmg+" damage to player "+ClientContext.get().getMap().getPlayerById(idHitted).getNickName());
    }

    /**
     *
     * @param pID
     * @param name
     * @param x
     * @param y
     */
    @Override
    public void grabWeaponNotification(int pID, String name, int x, int y){
        if(pID == ClientContext.get().getMyID())
            writeText("You grab the weapon "+name+" in position X:"+x+" Y:"+y);
        else
            writeText("Player "+ClientContext.get().getMap().getPlayerById(pID).getNickName()+" grab the weapon "+name+" in position X:"+x+" Y:"+y);

    }

    /**
     * Notify the player from a power up usage
     * @param id
     * @param name
     * @param desc
     */
    @Override
    public void powerUpUsageNotification(int id, String name, String desc){
        int num = 0;
        if(ClientContext.get().getMyID() == id)
            writeText(name +" used correctly");
        else {
            writeText("Player " + id + " used power up: " + name + " press 1 for more info, anything else to exit");
            num = readInt();
        }
        if(num == 1){
            writeText("Power up: "+name+", "+desc);
        }
    }

    /**
     *
     * @param pId
     * @param newX
     * @param newY
     */
    @Override
    public void notifyMovement(int pId, int newX, int newY) {
        if(pId != ClientContext.get().getMyID()) {
            writeText("Player " + ClientContext.get().getMap().getPlayerById(pId).getNickName() + " has moved in square (X : " + newX + ", Y : " + newY + ")");
        }
        else{
            writeText("You have successfully moved in square (X : " + newX + ", Y : " + newY+")");
        }
    }

    /**
     *
     * @param kill
     */
    @Override
    public void notifyDeath(Kill kill) {
        System.out.print("Player "+kill.getVictim().getId()+" was killed by the player "+kill.getKiller().getId());
        if(kill.isRage())
            writeText(" that also has raged him!");
        else
            writeText("!");
    }

    /**
     *
     * @param list
     */
    @Override
    public void choosePowerUpToRespawn(List <CardPower> list){
        writeText("Choose which power-up card you want to discard to respawn (you will respawn in the room with the same color of the discard card):");
        controller.getClient().sendMessage(new RespawnResponse(choosePowerUp(list)));
    }

    /**
     *
     * @param ranking
     */
    @Override
    public void showRanking(SortedMap<Player, Integer> ranking){
        int i=1;
        System.out.println("The game is over! \nLet's see the final results:");
        for(Player p : ranking.keySet()) {
            switch (i) {
                case 1:
                    System.out.print(">>> First place : ");
                    break;
                case 2:
                    System.out.print(">>> Second place : ");
                    break;
                case 3:
                    System.out.print(">>> Third place : ");
                    break;
                case 4:
                    System.out.print(">>> Fourth place : ");
                    break;
                case 5:
                    System.out.print(">>> Fifth place : ");
                    break;
            }
            i++;
            System.out.println("ID: "+p.getId() + ", nickname: "+p.getNickName()+", total points "+ranking.get(p) +".");
        }
        restartGamePhase();
        
    }

    private void restartGamePhase() {
        char choice;
        writeText("Do you want to play a new game?[Y/N]");
        do{
            choice = readChar();
        }while(choice != 'y' && choice != 'Y' && choice != 'N' && choice != 'n');
        if(choice == 'Y' || choice == 'y')
        {
            controller.startNewGame();
        }
        else
        {
            controller.stopListening();
            System.exit(0);
        }
    }

    /**
     *
     * @param message
     */
    @Override
    public void notifyCompletedOperation(String message) {
        writeText(message);
    }

    /**
     *
     */
    @Override
    public void notifyInvalidPowerUP(){
        System.out.println("You have selected an invalid power-up card!");
    }

    /**
     *
     */
    @Override
    public void notifyInvalidGrabPosition(){
        System.out.println("Your grab is failed! There are not card to grab in this square!");
    }

    /**
     *
     * @param list
     */
    @Override
    public void choosePowerUpToUse(List<CardPower> list){
        char c;
        int n = 1, t=1;
        list = list.stream().filter(x -> x.getName().equals("Targeting scope")).collect(Collectors.toList());
        if(list.size() > 0 && !ClientContext.get().getMyPlayer().getAmmo().isEmpty()) {
            writeText("Do you want to use a Targeting scope power-up card to apply an additional damage to one of your previous target?");
            writeText("Insert [Y]es or [N]o");
            do {
                c = readChar();
            } while (c != 'Y' && c != 'y' && c != 'N' && c != 'n');
            if (c == 'Y' || c == 'y') {
                if (list.size() > 1) {
                    System.out.println("Choose which power-up card you want to use after that the damages of your attack have been applied:");
                    for (CardPower cp : list) {
                        writeText(n + ". " + cp.toString());
                        n++;
                    }
                    do {
                        n = readInt();
                    } while (n <= 0 && n > list.size());
                }
                System.out.println("Choose which ammo you want to pay:");
                for (Color co : ClientContext.get().getMyPlayer().getAmmo()) {
                    writeText(t + ". " + co.toString());
                    t++;
                }
                do {
                    t = readInt();
                } while (t <= 0 && t > list.size());
                controller.getClient().sendMessage(new ChoosePowerUpResponse(list.get(n - 1), ClientContext.get().getMyPlayer().getAmmo().get(t - 1)));
            } else
                controller.getClient().sendMessage(new ChoosePowerUpResponse());
        }
        else
            controller.getClient().sendMessage(new ChoosePowerUpResponse());
    }


    @Override
    public void notifyStart() {
        writeText("Game has started!");
    }

    /**
     *
     */
    @Override
    public void notifyInvalidMessage() {
        writeText("ERROR! Invalid choice!");
    }

    /**
     *
     * @param pID
     */
    @Override
    public void notifyTurnChanged(int pID) {
        if(pID != ClientContext.get().getMyID()){
            Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == pID).findFirst().orElse(ClientContext.get().getMap().getPlayerById(pID));
            writeText("The turn is finished! Now it's "+p.getNickName()+"'s turn!");
        }
        else
            writeText("The turn is finished! Now it's your time!");
    }

    /**
     *
     * @param marks
     * @param idHitten
     * @param idShooter
     */
    @Override
    public void notifyMarks(int marks, int idHitten, int idShooter) {
        if(idHitten == ClientContext.get().getMyID())
            writeText("You have received "+marks+" marks from "+ClientContext.get().getMap().getPlayerById(idShooter).getNickName()+"!");
        else if(idShooter != ClientContext.get().getMyID())
            writeText("Player "+ClientContext.get().getMap().getPlayerById(idHitten).getNickName()+" has received "+marks+" marks from "+ClientContext.get().getMap().getPlayerById(idShooter).getNickName()+"!");
        else
            writeText("You give "+marks+" marks to player "+ClientContext.get().getMap().getPlayerById(idHitten).getNickName()+"!");
    }

    /**
     *
     * @param pID
     */
    @Override
    public void notifyGrabCardAmmo(int pID) {
        if(pID == ClientContext.get().getMyID())
            writeText("You grab a card ammo from the field");
        else
            writeText("Player "+ClientContext.get().getMap().getPlayerById(pID).getNickName()+" grab a card ammo from the field!");
    }

    /**
     *
     * @param pID
     */
    @Override
    public void notifyRespawn(int pID) {
        if(pID == ClientContext.get().getMyID())
            writeText("You are spawn!");
        else
            writeText("Player "+ ClientContext.get().getMap().getPlayerById(pID).getNickName()+" is spawned!");
    }

    /**
     *  Allow the choice of a power-up card from the list of cards available for the player
     * @param list
     */
    private CardPower choosePowerUp(List<CardPower> list) {
        int k=0;
        showMap(ClientContext.get().getMap());
        for(int i=1;i<=list.size();i++)
            System.out.println(i+">>>"+list.get(i-1).toString());
        do{
            k=readInt();
        }while(k<1 || k>list.size());
        return list.get(k-1);
    }

    public void showMap(GameMap map) {
        Square [][] grid = map.getGrid();
        String col;
        String wallVert = "│";
        String wallHor ="────";
        String wallHor2 = "────";
        String doorVert[] ={"┴","┬"};
        String doorHor ="┤  ├";
        String doorHor2 ="┤  ├";
        String openV = " ";
        String openH = "    ";
        String insideSquare = "      ";
        String angleLeftUp ="┌─";
        String angleRigUp = "─┐";
        String angleLeftDown = "└─";
        String angleRigDown ="─┘";
        String emptySquare = "        ";
        String spawnPoint = "s"; //ˢ s Ⓢ
        //String player = "\u265F";
        String player = "°";
        int numberP = 0;

        //print rhe map index
        System.out.println("    0       1       2       3   ");
        for (int x = 0; x < 3; x++) { //change with pos in the string
            for (int j = 0; j < 4; j++) { //change with square
                if(grid[x][j]!=null) {
                    col = checkColor(grid[x][j].getColor());
                    System.out.print(col + angleLeftUp + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.UP).name().equals("WALL"))
                        System.out.print(col + wallHor + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.UP).name().equals("DOOR"))
                        System.out.print(col + doorHor + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.UP).name().equals("OPEN"))
                        System.out.print(col + openH + ANSI_RESET);
                    System.out.print(col + angleRigUp + ANSI_RESET);
                }
                else{
                    System.out.print(emptySquare);
                }
            }
            System.out.println("");
            for(int height = 0; height < 2; height++){
                for(int j = 0; j < 4; j++) {
                    if (grid[x][j] != null) {
                        col = checkColor(grid[x][j].getColor());
                        if (grid[x][j].getEdge(Direction.LEFT).name().equals("WALL"))
                            System.out.print(col + wallVert + ANSI_RESET);
                        if (grid[x][j].getEdge(Direction.LEFT).name().equals("DOOR"))
                            System.out.print(col + doorVert[height] + ANSI_RESET);
                        if (grid[x][j].getEdge(Direction.LEFT).name().equals("OPEN"))
                            System.out.print(col + openV + ANSI_RESET);
                        if(height == 0){
                            if(grid[x][j].isRespawn()) { //check respown point
                                System.out.print(col + spawnPoint + insideSquare.substring(4));
                            }else {
                                System.out.print(col + insideSquare.substring(3) + ANSI_RESET); // da togliere quando si testa i player
                            }  // print ammo color
                            if(grid[x][j].getCardAmmo() != null){
                                if(grid[x][j].getCardAmmo().getCardPower() != 0)
                                    System.out.print(ANSI_GREY + "■" + ANSI_RESET);
                                for(Color c :grid[x][j].getCardAmmo().getAmmo()){
                                    System.out.print(checkAmmoColor(c) + "■" + ANSI_RESET);
                                }
                            }
                            else
                                System.out.print(insideSquare.substring(0,3));
                        }

                        if(height == 1){//show player in map
                            if (grid[x][j].getPlayers().isEmpty())// check lpayer in the square
                                System.out.print(insideSquare);
                            else {
                                numberP = grid[x][j].getPlayers().size();
                                System.out.print(col + insideSquare.substring(numberP) + ANSI_RESET);
                                for (int num = 0; num < numberP; num++) {
                                    System.out.print(checkPlayerColor(grid[x][j].getPlayers().get(num).getColor()) + player + ANSI_RESET);
                                }
                            }
                            //System.out.print(insideSquare); // da togliere con il commento sopra (per player)
                        }
                        if (grid[x][j].getEdge(Direction.RIGHT).name().equals("WALL"))
                            System.out.print(col + wallVert + ANSI_RESET);
                        if (grid[x][j].getEdge(Direction.RIGHT).name().equals("DOOR"))
                            System.out.print(col + doorVert[height] + ANSI_RESET);
                        if (grid[x][j].getEdge(Direction.RIGHT).name().equals("OPEN"))
                            System.out.print(col + openV + ANSI_RESET);
                    } else {
                        System.out.print(emptySquare);
                    }
                }
                if(height == 1) {
                    System.out.print(" "+x);
                }
                System.out.println("");
            }
            for (int j = 0; j < 4; j++) {
                if(grid[x][j]!=null) {
                    col = checkColor(grid[x][j].getColor());
                    System.out.print(col + angleLeftDown + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.DOWN).name().equals("WALL"))
                        System.out.print(col + wallHor2 + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.DOWN).name().equals("DOOR"))
                        System.out.print(col + doorHor2 + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.DOWN).name().equals("OPEN"))
                        System.out.print(col + openH + ANSI_RESET);
                    System.out.print(col + angleRigDown + ANSI_RESET);
                }
                else{
                    System.out.print(emptySquare);
                }
            }
            System.out.println("");
        }
        if(ClientContext.get().getMap() != null && this.controller.getState() != ClientState.WAITING_START)
            for(Square sq : ClientContext.get().getMap().getSpawnpoints()){
                if(sq.getWeapons() != null) {
                    writeText("In position X= " + sq.getX() + "  Y= " + sq.getY() + " there are:");
                    for(CardWeapon cw : sq.getWeapons()){
                        printWeapon(cw,0,true, false);
                    }
                }
            }
        //showMyPlayerInformation();
        //showPlayerPosition();
    }


    private void showMyPlayerInformation() {
        String death = "\u2620";
        Player p = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        writeText(checkPlayerColor(p.getColor()) + "You are in position x: " + p.getPosition().getX() + ", y: " + p.getPosition().getY() + ANSI_RESET);
        writeText(checkPlayerColor(p.getColor())+"Your weapon: "+ANSI_RESET);
        showWeapons(p.getWeapons(),0, false, true, true);
        if(!p.getAmmo().isEmpty()) {
            writeText(checkPlayerColor(p.getColor()) + "Your munition: " + ANSI_RESET);
            for (Color c : p.getAmmo()) {
                System.out.print(checkAmmoColor(c) + "■ " + ANSI_RESET);
            }
            System.out.println("");
        }
        else
            writeText(checkPlayerColor(p.getColor()) + "You haven't available munitions! " + ANSI_RESET);
        if(!p.getCardPower().isEmpty()) {
            writeText(checkPlayerColor(p.getColor()) + "Your power-up cards: " + ANSI_RESET);
            for (CardPower c : p.getCardPower()) {
                writeText(c.toString());
            }
        }
        else
            writeText(checkPlayerColor(p.getColor()) + "You haven't available power-up cards! " + ANSI_RESET);
        if(p.getDamage().size()==0)
            writeText(checkPlayerColor(p.getColor())+"No damage"+ANSI_RESET);
        else{
            System.out.print(checkPlayerColor(p.getColor())+">> Your damage: "+ANSI_RESET);
            for(PlayerColor c : p.getDamage()) {
                System.out.print(checkPlayerColor(c) + "¤" + ANSI_RESET);
            }
            System.out.println("");
        }
        if(p.getMark().size()==0)
            writeText(checkPlayerColor(p.getColor())+"No marks"+ANSI_RESET);
        else{
            System.out.print(checkPlayerColor(p.getColor())+">> Your marks: "+ANSI_RESET);
            for(PlayerColor c : p.getMark()) {
                System.out.print(checkPlayerColor(c) + "¤" + ANSI_RESET);
            }
            System.out.println("");
        }
    }

    private void showPlayerPosition() {
        for(Player p : ClientContext.get().getMap().getAllPlayers()) {
            if (p.getId() != ClientContext.get().getMyID()){
                System.out.println("");
                writeText(checkPlayerColor(p.getColor()) + "Player " + p.getNickName() + " is in position x: " + p.getPosition().getX() + ", y: " + p.getPosition().getY() + " with: "+ ANSI_RESET);
                if(p.getDamage().size()==0)
                    writeText(checkPlayerColor(p.getColor())+"No damage"+ANSI_RESET);
                else{
                    System.out.print(checkPlayerColor(p.getColor())+">> Damage: "+ANSI_RESET);
                    for(PlayerColor d : p.getDamage()) {
                        System.out.print(checkPlayerColor(d) + "¤" + ANSI_RESET);
                    }
                    System.out.println("");
                }
                if(p.getMark().size()==0)
                    writeText(checkPlayerColor(p.getColor())+"No marks"+ANSI_RESET);
                else{
                    System.out.print(checkPlayerColor(p.getColor())+">> Marks: "+ANSI_RESET);
                    for(PlayerColor c : p.getMark()) {
                        System.out.print(checkPlayerColor(c) + "¤" + ANSI_RESET);
                    }
                    System.out.println("");
                }
            }
        }
    }

    private String checkAmmoColor(Color color){
        if(color.equals(Color.YELLOW))return ANSI_YELLOW;
        if(color.equals(Color.RED))return ANSI_RED;
        return ANSI_BLUE;
    }
    private String checkPlayerColor(PlayerColor color) {
        if(color.equals(PlayerColor.YELLOW))return ANSI_YELLOW;
        if(color.equals(PlayerColor.GREEN))return ANSI_GREEN;
        if(color.equals(PlayerColor.BLUE))return ANSI_BLUE;
        if(color.equals(PlayerColor.PURPLE))return ANSI_PURPLE;
        return ANSI_GREY;
    }

    private String checkColor(MapColor color){
        if(color.equals(MapColor.YELLOW))return ANSI_YELLOW;
        if(color.equals(MapColor.GREEN))return ANSI_GREEN;
        if(color.equals(MapColor.BLUE))return ANSI_BLUE;
        if(color.equals(MapColor.PURPLE))return ANSI_PURPLE;
        if(color.equals(MapColor.RED))return ANSI_RED;
        return ANSI_GREY;
    }

    public void chooseWeaponToGrab(List<CardWeapon> weapons){
        int i=1;
        int choiceWG = 0;
        int choiceWD = -1;
        CardWeapon wG;
        CardWeapon wD = null;
        char t = 'Y';
        List<CardPower> toUse = null;
        Player myP = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        //Choose which weapon to grab
        writeText("Choose one weapon to grab between the possible: ");
            showWeapons(weapons,1, true, true, false);
        do{
            choiceWG = readInt();
        }while(choiceWG>weapons.size() || choiceWG<=0);
        wG = weapons.get(choiceWG-1);
        //Selection of weapon to discard
        if(myP.getWeapons().size()==3) {
            writeText("You already have 3 weapons, do you want to discard one of them to grab the new one ([Y]es, [N]o)?");
            do {
                t = readChar();
            } while (t != 'Y' && t != 'N' && t != 'y' && t != 'n');
            if (t == 'Y' || t == 'y') {
                i = 1;
                for (CardWeapon cw : myP.getWeapons()) {
                    writeText(i + "- " + cw.getName());
                    i++;
                }
                do{
                    choiceWD = readInt();
                }while (choiceWD <1 || choiceWD>3);
            }
        }
        //Selection of power-up to pay (if there is anything to pay)
        if(wG.getPrice().size() > 1)
            toUse = powerUpSelection(wG.getPrice().subList(1,wG.getPrice().size()));
        if (choiceWD != -1)
            wD = myP.getWeapons().get(choiceWD-1);
        controller.getClient().sendMessage(new PickUpWeaponRequest(wG,toUse, wD));
    }

    private List<CardPower> powerUpSelection(List<Color> price)
    {
        Player myP = ClientContext.get().getMyPlayer();
        List<CardPower> list = new ArrayList<>(myP.getCardPower().stream().filter(n -> price.contains(n.getColor())).collect(Collectors.toList()));
        List<CardPower> toUse = new ArrayList<>();
        boolean mustUse = myP.mustUsePowerUpsToPay(new ArrayList<>(price));
        if(!list.isEmpty()) {
            char t = 'Y';
            int i;
            if(!mustUse){
                writeText("Do you want to use some of your power-up to pay ([Y]es, [N]o)?");
                do {
                    t = readChar();
                } while (t != 'Y' && t != 'N' && t != 'y' && t != 'n');
            }
            else
                writeText("You have to use some of your power-ip cards to complete your payment!");
            if (t == 'Y' || t == 'y') {
                i = 1;
                writeText("Insert the number of the correspondent power-up you want to use separated by comma (ex: 1,3,...):");
                for (CardPower cp : list) {
                    if(price.contains(cp.getColor())) {
                        writeText(i + "- " + cp.toString());
                        i++;
                    }
                }
                String selection = readText();
                String[] parts = selection.split(",");
                for (String s : parts) {
                    int id = Integer.parseInt(s);
                    if (id <= list.size() && id >= 1)
                        toUse.add(list.get(id - 1));
                }
            }
        }
        return toUse;
    }

    /**
     * print a weapon and its cost
     * @param cw, p
     */
    public void printWeapon(CardWeapon cw, int p, boolean showCost, boolean showLoaded)
    {
        List<Color> tmp = cw.getPrice().subList(p, cw.getPrice().size());
        String string = "";
        if (showLoaded)
            string = cw.isLoaded() ? " (loaded)" : " (unloaded)";
        writeText(cw.getName()+string);
        if (showCost) {
            System.out.print("       Cost: ");
            if (tmp.size() == 0)
                System.out.print("Free");
            else
                for (Color c : tmp)
                    System.out.print(checkAmmoColor(c) + "■ " + ANSI_RESET);
            System.out.println("");
        }

    }

    /**
     * print a list of weapont and they cost.
     * p = 0 if we want to print the weapons reload price; p = 1 if we want to print the weapons grab price
     * numeric = true for print the numeric identifiers od the list
     * @param cws
     * @param p
     * @param numeric
     */
    public void showWeapons(List<CardWeapon> cws, int p, boolean numeric, boolean showCost, boolean showLoaded) {
        int i = 1;
        if(numeric){
            for (CardWeapon cw : cws){
                System.out.print(i + " ");
                i++;
                printWeapon(cw,p,showCost,showLoaded);
            }
        }
        else {
            for (CardWeapon cw : cws) {
                printWeapon(cw, p,showCost,showLoaded);
            }
        }


    }

    private void showEffects(List<FullEffect> effects, boolean numeric){
        FullEffect actual = null;
        if(numeric) {
            for (int i = 1; i <= effects.size(); i++) {
                actual = effects.get(i - 1);
                writeText(i + ". Name: " + actual.getName());
                writeText("   Description: " + actual.getDescription());
                System.out.print("   Cost: ");
                if (actual.getPrice() != null && !actual.getPrice().isEmpty()) {
                    if(actual.getPrice().get(0) == Color.ANY)
                        System.out.println("FREE");
                    else {
                        for (Color c : actual.getPrice())
                            System.out.print(checkAmmoColor(c) + "■ " + ANSI_RESET);
                    }
                }
                else
                    System.out.print("FREE");
                System.out.println();
            }
        }
        else {
            for (int i = 1; i <= effects.size(); i++) {
                actual = effects.get(i - 1);
                writeText("Name: " + actual.getName());
                writeText("Description: " + actual.getDescription());
                System.out.print("  Cost: ");
                if (actual.getPrice() != null) {
                    for (Color c : actual.getPrice())
                        System.out.print(checkAmmoColor(c) + "■ " + ANSI_RESET);
                }
                else
                    System.out.print("FREE");
                System.out.println();
            }
        }
    }

    @Override
    public void notifyConnectionError() {
        char choice;
        writeText("There is connection problem with the server! Check your internet connection please");
        writeText("Do you want to retry the connection?[Y/N]");
        do{
            choice = readChar();
        }while(choice != 'Y' && choice != 'y' && choice != 'n' && choice != 'N');

        if(choice == 'Y' || choice == 'y')
        {
            controller.retryConnection();
        }
        else{
            if(waitingThread != null)
                waitingThread.interrupt();
            controller.stopListening();
            System.exit(0);
        }
    }

    /**
     * Show reconnection message
     */
    @Override
    public void notifyReconnected() {
        writeText("Reconnected to the server!");
    }

    @Override
    public void connectionFailed() {
        writeText("ERROR: Unable to connect, check your connection and the server ip!");
    }
}
