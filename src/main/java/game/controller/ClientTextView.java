package game.controller;

import game.controller.commands.clientcommands.*;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;

public class ClientTextView implements View {
    private static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_GREEN = "\u001B[32m";
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

    public String readText(){return this.fromKeyBoard.nextLine();}
    public  int readInt(){return this.fromKeyBoard.nextInt();}

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

    /**
     * Show the available maps
     */
    private void printAvailableMaps()
    {
        writeText("Available game maps:\n");
        for(GameMap m : availableMaps)
            writeText(m.toString());
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
        printAvailableMaps();
        do{
            writeText("Enter the room map id:");
            mapId = readInt();
        }while(mapId < 1 || mapId > availableMaps.size());

        do{
            writeText("Enter the number of player for the room [3-5]:");
            nPlayer = readInt();
        }while(nPlayer < 3 || nPlayer > 5);

        controller.getClient().sendMessage(new CreateWaitingRoomRequest(mapId,nPlayer,user));
    }

    /**
     * Choose a single step for the current action the player want to make
     * @param possibleAction
     */
    public void chooseStepActionPhase(List<Action> possibleAction){
        Action choosenAction = null;
        String action;
        do{
            writeText("Choose one of the possible step for this action, that are:");
            for(Action ac : possibleAction){
                writeText(">>> " + ac.name());
            }
            action = readText();
            try {
                choosenAction = Action.valueOf(action);
            }catch ( NullPointerException f){
                choosenAction = null;
            }catch (IllegalArgumentException e){
                choosenAction = null;
            }
        }while(!possibleAction.contains(choosenAction));

        switch (choosenAction){
            case GRAB:
                //TODO this.controller.handle(GrabActionRequest mse);
                break;
            case SHOOT:
                //todo call ShootActionResponse in clietController
                break;
            case MOVEMENT:
                //TODO callMovementResponse in clientControllore
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
                writeText(">>> X :" + sq.getX() + " Y : " + sq.getY());
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
        writeText("Choose the action you want to make: ");
        do{
            action = readText();
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
            writeText("Player "+pID+" grabbed the weapon "+name+" in position X:"+x+" Y:"+y);
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

    /**
     *
     *
     */
    @Override
    public void notifyStart() {
        writeText("Game is started!");
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
    public void notifyGrabAmmo(int pID) {
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
            writeText("You are respawn!");
        else
            writeText("Player "+ClientContext.get().getMap().getPlayerById(pID).getNickName()+" is respawn!");
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
        return list.get(k);
    }

    public void showMap(Square[][] grid) {
        String col;
        String wallVert = " │";
        String wallHor ="────";
        String wallHor2 = "────";
        String doorVert1 =" ┴";
        String doorVert2 =" ┬";
        String doorHor ="┤  ├";
        String doorHor2 ="┤  ├";
        String openV = "  ";
        String openH = "    ";
        String angleLeftUp =" ┌─";
        String angleRigUp = "─┐";
        String angleLeftDown = " └─";
        String angleRigDown ="─┘";

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
                    System.out.print("         ");
                }
            }
            System.out.println("");
            for(int j = 0; j < 4; j++){ // first mid
                if(grid[x][j]!=null) {
                    col = checkColor(grid[x][j].getColor());
                    if (grid[x][j].getEdge(Direction.LEFT).name().equals("WALL"))
                        System.out.print(col + wallVert + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.LEFT).name().equals("DOOR"))
                        System.out.print(col + doorVert1 + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.LEFT).name().equals("OPEN"))
                        System.out.print(col + openV + ANSI_RESET);
                    System.out.print(col + "     " + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.RIGHT).name().equals("WALL"))
                        System.out.print(col + wallVert + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.RIGHT).name().equals("DOOR"))
                        System.out.print(col + doorVert1 + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.RIGHT).name().equals("OPEN"))
                        System.out.print(col + openV + ANSI_RESET);
                }
                else{
                    System.out.print("         ");
                }
            }
            System.out.println("");
            for(int j = 0; j < 4; j++){ // second mid
                if(grid[x][j]!=null) {
                    col = checkColor(grid[x][j].getColor());
                    if (grid[x][j].getEdge(Direction.LEFT).name().equals("WALL"))
                        System.out.print(col + wallVert + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.LEFT).name().equals("DOOR"))
                        System.out.print(col + doorVert2 + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.LEFT).name().equals("OPEN"))
                        System.out.print(col + openV + ANSI_RESET);
                    System.out.print(col + "     " + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.RIGHT).name().equals("WALL"))
                        System.out.print(col + wallVert + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.RIGHT).name().equals("DOOR"))
                        System.out.print(col + doorVert2 + ANSI_RESET);
                    if (grid[x][j].getEdge(Direction.RIGHT).name().equals("OPEN"))
                        System.out.print(col + openV + ANSI_RESET);
                }
                else{
                    System.out.print("         ");
                }
            }
            System.out.println("");
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
                    System.out.print("         ");
                }
            }
            System.out.println("");
        }
        //showPlayerPosition();
        //showMyPlayerInformation();
    }

    private void showMyPlayerInformation() {
        Player p = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        writeText("Your weapon: ");
        for(CardWeapon cw : p.getWeapons()){
            writeText(cw.getName());
        }
        writeText("Your munition");
        for(Color c : p.getAmmo()){
            System.out.print(checkAmmoColor(c)+"■"+ANSI_RESET);
        }
    }

    private void showPlayerPosition() {
        for(Player p : ClientContext.get().getMap().getAllPlayers()) {
            if (p.getId() != ClientContext.get().getMyID())
                writeText(checkPlayerColor(p.getColor()) + "Player " + p.getNickName() + " is in position x: " + p.getPosition().getX() + ", y: " + p.getPosition().getY() + " with: "+ ANSI_RESET);
            for(PlayerColor d : p.getDamage()) {
                System.out.print(checkPlayerColor(d) + "¤" + ANSI_RESET);
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
        return ANSI_CYAN;
    }

    private String checkColor(MapColor color){
        if(color.equals(MapColor.YELLOW))return ANSI_YELLOW;
        if(color.equals(MapColor.GREEN))return ANSI_GREEN;
        if(color.equals(MapColor.BLUE))return ANSI_BLUE;
        if(color.equals(MapColor.PURPLE))return ANSI_PURPLE;
        if(color.equals(MapColor.RED))return ANSI_RED;
        return ANSI_CYAN;
    }
}
