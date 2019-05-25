package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.clientcommands.*;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;

public class ClientTextView implements View {
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
    private String user;
    private List<GameMap> availableMaps;


    public ClientTextView(){

    }

    public ClientTextView(ClientController controller) {
        this.controller = controller;
        this.fromKeyBoard = new Scanner(System.in);
    }

    public void writeText (String text){
        System.out.println(">> " + text);
    }

    public String readText(){
        String string = fromKeyBoard.nextLine();
        return string;
    }

    public char readChar(){
        String string = fromKeyBoard.next();
        return string.charAt(0);
    }

    public  int readInt(){
        //TODO handle NumberFormatException thrown in case of a non numeric character inserted
        int save = Integer.parseInt(this.fromKeyBoard.nextLine());
        return save;
    }

    /**
     * Starting phase during the player enter his username for the game
     */
    public void setUserNamePhase (){
        do {
            writeText("Provide username:");
            this.user = readText();
        } while ( this.user == null);

        writeText("Welcome, " + this.user + ", good luck!");

        controller.getClient().sendMessage(new GetAvailableMapsRequest());
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
        List<ClientMessage> reloadRequests = new ArrayList<>();
        do {
            showWeapons(weaponsToReload,0);
            do {
                writeText("Insert the id of a weapon you want to reload or -1 to terminate the reload phase:");
                n = readInt();
            } while (n != -1 && n < 1 && n > weaponsToReload.size());
            if(n != -1)
            {
                List<CardPower> cp = powerUpSelection();
                reloadRequests.add(new ReloadWeaponRequest(weaponsToReload.get(n-1),cp));
            }

        }while(weaponsToReload.size() > 0 && n!=-1);

        controller.sendMessages(reloadRequests);

    }

    @Override
    public void showReloadMessage(CardWeapon cW) {
        writeText("The weapon " + cW.getName() + " has been correctly reloaded");
    }

    /**
     * Show the available maps
     */
    private void printAvailableMaps()
    {
        writeText("Available game maps:\n");
        for(GameMap m : availableMaps) {
            writeText("MAP-ID : "+m.getId()+"\n");
            showMap(m.getGrid());
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
        }while((nRoom <= 0 || nRoom > waitingRooms.size()) && nRoom != -1);
        if(nRoom != -1)
        {
            controller.getClient().sendMessage(new JoinWaitingRoomRequest(nRoom,user));
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
        //printAvailableMaps();
        do{
            writeText("Enter the id of the map you want to use in your game:");
            mapId = readInt();
        }while(mapId < 1 || mapId > availableMaps.size());

        do{
            writeText("Enter the number of player to start the game [3-5]:");
            nPlayer = readInt();
        }while(nPlayer < 3 || nPlayer > 5);

        controller.getClient().sendMessage(new CreateWaitingRoomRequest(mapId,nPlayer,user));
    }

    /**
     * Choose a single step for the current action the player want to make
     */
    public void chooseStepActionPhase(){
        List<Action> possibleActions = this.controller.getAvailableActions();
        Action chosenAction = null;
        String action;
        do{
            writeText("The action you have selected preview this ordered single step combination, choose the next:");
            for(Action ac : possibleActions){
                writeText(ac.name());
            }
            action = readText();
            action = action.toUpperCase();
            try {
                chosenAction = Action.valueOf(action);
            }catch ( NullPointerException f){
                chosenAction = null;
            }catch (IllegalArgumentException e){
                chosenAction = null;
            }
        }while(!possibleActions.contains(chosenAction));

        switch (chosenAction){
            case GRAB:
                controller.getClient().sendMessage(new GrabActionRequest());
                break;
            case SHOOT:
                controller.getClient().sendMessage(new ShootActionRequest());
                break;
            case MOVEMENT:
                controller.getClient().sendMessage(new MovementActionRequest());
                break;
        }
    }

    /**
     * Choose one of the proposed squares
     * @param possibleSquare
     */
    public  void chooseSquarePhase(List<Square> possibleSquare){
        Square choosenSquare = null;
        String square;
        int posX;
        int posY;
        do{
            writeText("Choose a square for complete your move: ");
            for(Square sq : possibleSquare){
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

        List <Target> choosenTarget = new ArrayList<>();
        writeText("Scegli tra "+minE+"e "+maxE+" dei seguenti target: (write the number)");
        writeText(i +" to exit" );
        i++;
        for (Target tg : possibleTarget){
            writeText(i +" player: "+tg.returnName());
            i++;
        }
        for(i = 0; i <= maxE && k>0 ; ){
            k = readInt();
            if(k < possibleTarget.size() && k > 0){
                if(choosenTarget.contains(k)){
                    writeText("Player already selected");
                }else {
                    choosenTarget.add(possibleTarget.get(k));
                    i++;
                }
            }
            else if(k == 0){
                if(choosenTarget.size()>=minE)
                    writeText("Selection Completed!");
                else
                    k = 12;
            }
            else{
                writeText("Not enough target");
            }
        }
        controller.getClient().sendMessage(new ChooseTargetResponse(choosenTarget));
    }

    /**
     * Choose the action for the current turn
     */
    public void chooseTurnActionPhase(){
        Action choosenAction;
        String action;
        do{
            writeText("Choose the action you want to make between {MOVEMENT, GRAB, SHOOT}: ");
            action = readText();
            action = action.toUpperCase();
            try {
                choosenAction = Action.valueOf(action);
            }
            catch (IllegalArgumentException | NullPointerException e) {
                choosenAction = null;
            }

        }while(choosenAction==null);
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
     */
    @Override
    public void invalidStepNotification(){
        writeText("The step selected is not valid!");
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
            writeText("Player "+idShooter+" dealt "+dmg+" damage to you");
        else if(idShooter != ClientContext.get().getMyID())
            writeText("Player "+idShooter+" dealt "+dmg+" to player "+idHitted);
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
            writeText("You the weapon "+name+" in position X:"+x+" Y:"+y);
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
    public void showRanking(Map<Player, Integer> ranking){
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
            System.out.println("ID: "+p.getId() + ", nickname: "+p.getNickName()+", total points "+ranking.get(p)+".");
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
        System.out.println("Choose which power-up card you want to use after that the damages of your attack have been applied:");
        controller.getClient().sendMessage(new ChoosePowerUpResponse(choosePowerUp(list)));
    }

    /*
     *
     *
     *
    @Override
    public int notifyStart(CardPower[] powerups) {
        int choice;
        writeText("Game has started!");
        writeText("Choose one of this two powerups to spawn on the map:");
        writeText("1)\n" + powerups[0].toString());
        writeText("2)\n" + powerups[1].toString());
        do{
            choice = readInt();
        }while(choice != 1 && choice != 2);

        return choice;
    }*/

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
        writeText("The turn is finished! "+ClientContext.get().getMap().getPlayerById(pID).getNickName()+" now it's your time!");
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
        else
            writeText("Player "+ClientContext.get().getMap().getPlayerById(idHitten).getNickName()+" has received "+marks+" marks from "+ClientContext.get().getMap().getPlayerById(idShooter).getNickName()+"!");
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
        for(int i=1;i<=list.size();i++)
            System.out.println(i+">>>"+list.get(i-1).toString());
        do{
            k=readInt();
        }while(k<1 && k>list.size());
        return list.get(k-1);
    }

    public void showMap(Square[][] grid) {
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
        String player = "\u265F";
        int numberP = 0;

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
                                if(grid[x][j].getCardAmmo().getCardPower() > 0)
                                    System.out.print(ANSI_GREY + "■" + ANSI_RESET);
                                for(Color c :grid[x][j].getCardAmmo().getAmmo()){
                                    System.out.print(checkAmmoColor(c) + "■" + ANSI_RESET);
                                }
                            }
                            else
                                System.out.print(insideSquare.substring(0,3));
                        }

                        if(height == 1){//show player in map
                           /* if (grid[x][j].getPlayers().isEmpty())// check lpayer in the square
                                System.out.print(col + insideSquare + ANSI_RESET);
                            else {
                                numberP = grid[x][j].getPlayers().size();
                                System.out.print(col + insideSquare.substring(numberP) + ANSI_RESET);
                                for (int num = 0; num < numberP; num++) {
                                    System.out.print(checkPlayerColor(grid[x][j].getPlayers().get(num).getColor()) + player + ANSI_RESET);
                                }
                            }*/
                            System.out.print(insideSquare); // da togliere con il commento sopra (per player)
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
        //showMyPlayerInformation();
        //showPlayerPosition();
    }

    private void showMyPlayerInformation() {
        String death = "\u2620";
        Player p = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        writeText("Your weapon: ");
        for(CardWeapon cw : p.getWeapons()){
            writeText(cw.getName());
        }
        writeText("Your munition");
        for(Color c : p.getAmmo()){
            System.out.print(checkAmmoColor(c)+"■"+ANSI_RESET);
        }
        System.out.println("");
    }

    private void showPlayerPosition() {
        System.out.println("");
        for(Player p : ClientContext.get().getMap().getAllPlayers()) {
            if (p.getId() != ClientContext.get().getMyID()){
                writeText(checkPlayerColor(p.getColor()) + "Player " + p.getNickName() + " is in position x: " + p.getPosition().getX() + ", y: " + p.getPosition().getY() + " with: "+ ANSI_RESET);
                for(PlayerColor d : p.getDamage()) {
                    System.out.println(checkPlayerColor(d) + "¤" + ANSI_RESET);
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
        if(color.equals(PlayerColor.GREY))return ANSI_RED;
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
        List<CardPower> toUse;
        Player myP = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        //Choose which weapon to grab
        writeText("Choose one weapon to grab between the possible: ");
        showWeapons(weapons,1);
        do{
            choiceWG = readInt();
        }while(choiceWG>weapons.size() || choiceWG<=0);
        wG = weapons.get(choiceWG-1);
        //Selection of weapon to discard
        if(myP.getWeapons().size()==3) {
            writeText("You already have 3 weapons, do you want to discard one of them to grab the new one ([Y]es, [N]o)?");
            do {
                t = readChar();
            } while (t != 'Y' || t != 'N' || t != 'y' || t != 'n');
            if (t == 'Y' || t == 'y') {
                i = 1;
                for (CardWeapon cw : myP.getWeapons()) {
                    writeText(i + "- " + cw.getName());
                }
                do{
                    choiceWD = readInt();
                }while (choiceWD <1 || choiceWD>3);
            }
        }
        //Selection of power-up to pay
        toUse = powerUpSelection();
        if (choiceWD != -1)
            wD = myP.getWeapons().get(choiceWD-1);
        controller.getClient().sendMessage(new PickUpWeaponRequest(wG,toUse, wD));
    }

    private List<CardPower> powerUpSelection()
    {
        List<CardPower> toUse = null;
        char t;
        int i;
        Player myP = ClientContext.get().getMyPlayer();
        writeText("Do you want to use some of your power-up to pay ([Y]es, [N]o)?");
        do{
            t = readChar();
        }while(t != 'Y' && t != 'N' && t != 'y' && t != 'n');
        if(t == 'Y' || t == 'y')
        {
            i=1;
            toUse = new ArrayList<>();
            writeText("Insert the number of the correspondent power-up you want to use separated by comma (ex: 1,3,...):");
            for(CardPower cp : myP.getCardPower()) {
                writeText(i + "- " + cp.getName() + " [" + cp.getColor() + "]");
                i++;
            }
            String selection = readText();
            String [] parts = selection.split(",");
            for(String s : parts)
            {
                int id = Integer.parseInt(s);
                if(id <= myP.getCardPower().size() && id >= 1 )
                    toUse.add(myP.getCardPower().get(id-1));
            }


        }
        return toUse;
    }

    /**
     * Show a list of weapons with numeric identifiers
     * @param weapons
     */
    public void showWeapons(List<CardWeapon> weapons, int p)
    {
        int i = 1;
        for(CardWeapon cw : weapons){
            writeText(i+"- "+cw.getName());
            System.out.println("  >>PRICE");
            if(cw.getPrice() != null) {
                for (Color c : cw.getPrice().subList(p, cw.getPrice().size()))
                    System.out.println("    >>" + c.toString());
            }
            else {
                System.out.println("    >>FREE");
            }
            i++;
        }
    }
}