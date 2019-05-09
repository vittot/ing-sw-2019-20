package game.controller;

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

    public void setUserNamePhase (){
        do {
            writeText("Provide username:");
            this.user = readText();
        } while ( this.user == null);

        writeText("Wealcome, " + this.user + ", good luck!");
    }

    public void chooseStepActionPhase(List<Action> possibleAction){
        Action choosenAction = null;
        String action;
        do{
            writeText("Choose a step for this action");
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
        }while(possibleAction.contains(choosenAction));
        switch (choosenAction){
            case MOVEMENT://TODO callMovementResponse in clientControllore
                return;
            case SHOOT:
                //todo call ShootActionResponse in clietController
                return;
            case GRAB:
                //TODO this.controller.handle(GrabActionRequest mse);
                return;
        }
    }

    public  void chooseSquarePhase(List<Square> possibleSquare){
        Square choosenSquare = null;
        String square;
        int posX;
        int posY;
        do{
            writeText("Choose a square for this movement: ");
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
        }while(possibleSquare.contains(choosenSquare));
        //TODO client.Contrller call ChooseSqaureResponse
    }

    public void chooseTargetPhase(List<Target> possibleTarget){
        int maxE = ClientContext.get().getCurrentEffect().getMaxEnemy();
        int minE = ClientContext.get().getCurrentEffect().getMinEnemy();
        int i = 0;
        int k = 12;

        List <Target> choosenTarget = new ArrayList<>();
        writeText("Scegli tra "+minE+"e "+maxE+" dei seguenti target: (write the number)");
        for (Target tg : possibleTarget){
            writeText(i +" player: "+tg.returnName());
            i++;
        }
        for(i = 0; i >= minE && i <= maxE;){
            k = readInt();
            if(k < possibleTarget.size() && k >= 0){
                choosenTarget.add(possibleTarget.get(k));
                i++;
            }else{
                writeText("Invalid Target");
            }
        }
        //TODO call clientController chooseTarget
    }


}
