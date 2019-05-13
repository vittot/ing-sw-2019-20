package game.controller;

import game.controller.commands.clientcommands.*;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;

public class ClientTextView implements View {
    private ClientController controller;
    private Scanner fromKeyBoard;
    private String user;

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
        //TODO:
        //get list of waiting rooms and available maps
        //choose if join a room or create a new room

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
            case MOVEMENT:
                //TODO callMovementResponse in clientControllore
                break;
            case SHOOT:
                //todo call ShootActionResponse in clietController
                break;
            case GRAB:
                //TODO this.controller.handle(GrabActionRequest mse);
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
            catch (IllegalArgumentException e) {
                choosenAction = null;
            }
            catch (NullPointerException e) {
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
        else
            writeText("Player "+idShooter+" dealt "+dmg+" to player "+idHitted);
    }

    /**
     * notify the player that someone grab a weapon in a specific position
     * @param p
     * @param x
     * @param y
     */
    @Override
    public void grabWeaponNotification(int p, int x, int y){
        writeText("Player "+p+" grabbed a weapon in position X:"+x+" Y:"+y);
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
            System.out.println(" that also has raged him!");
        else
            System.out.println("!");
    }

    /**
     *
     * @param list
     */
    @Override
    public void choosePowerUpToRespawn(List <CardPower> list){
        System.out.println("Choose which power-up card you want to discard to respawn (you will respawn in the room with the same color of the discard card):");
        choosePowerUp(list);

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
     * @param name
     */
    @Override
    public void notifyWeaponGrab(String name){
        System.out.println("You grab "+name);
    }

    @Override
    public void notifyCompletedOpeartion(String message) {

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
        choosePowerUp(list);
    }

    @Override
    public void notifyStart(Game game) {

    }

    @Override
    public void notifyInvalidMessage() {

    }

    @Override
    public void notifyTurnChanged() {

    }

    @Override
    public void notifyMarks() {

    }

    @Override
    public void notifyGrabAmmo() {

    }

    @Override
    public void notifyRespawn() {

    }

    /**
     *  Allow the choice of a power-up card from the list of cards available for the player
     * @param list
     */
    private void choosePowerUp(List<CardPower> list) {
        int k=0;
        for(int i=1;i<=list.size();i++)
            System.out.println(i+">>>"+list.get(i-1).toString());
        do{
            k=readInt();
        }while(k<1 && k>list.size());
        //TODO have we to remove the card power from the list or we waiting for the notify of the operation?
        controller.getClient().sendMessage(new RespawnResponse(list.get(k-1)));
    }
}
