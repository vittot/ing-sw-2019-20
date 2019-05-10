package game.controller;

import game.controller.commands.clientcommands.ChooseSquareResponse;
import game.controller.commands.clientcommands.ChooseTargetResponse;
import game.controller.commands.clientcommands.ChooseTurnActionResponse;
import game.controller.commands.clientcommands.GrabActionRequest;
import game.model.Action;
import game.model.Square;
import game.model.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            }catch (IllegalArgumentException e){
                choosenAction = null;
            }catch ( NullPointerException f){
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
        for (Target tg : possibleTarget){
            writeText(i +" player: "+tg.returnName());
            i++;
        }
        for(i = 0; i < maxE-minE && k>0 ; ){
            k = readInt();
            if(k < possibleTarget.size() && k >= 0){
                choosenTarget.add(possibleTarget.get(k));
                i++;
            }
            else if(k<0){
                if(choosenTarget.size()>=minE)
                    writeText("Selection Completed!");
                else
                    k = 12;
            }
            else{
                writeText("Invalid Target");
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
    public void invalidActionNotification(){
        writeText("The action selected is not valid!");
    }

    /**
     * Notify impossibility to do others actions fort this current turn
     */
    public void insufficientNumberOfActionNotification(){
        writeText("You cannot do others actions for this turn!");
    }

    /**
     * Notify invalid targets selection
     */
    public void invalidTargetNotification(){
        writeText("The targets selected are not valid!");
    }

    /**
     * Notify invalid step selection
     */
    public void invalidStepNotification(){
        writeText("The step selected is not valid!");
    }

    /**
     * Notify invalid targets election
     */
    public void maxNumberOfWeaponNotification(){
        writeText("You can't grab others weapons from the ground, before discard one of your weapon!");
    }
}
