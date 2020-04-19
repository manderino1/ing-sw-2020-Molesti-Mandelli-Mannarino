package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CliViewUpdate extends ViewUpdate {

    private Cell[][] lastMap;
    private InputParser inputParser;
    private java.io.BufferedReader console;
    private ArrayList<PlayerData> playerDataArrayList = new ArrayList<>();

    /***
     * Used for testing, can redirect the console input
     * @param console a bufferedreader with inputstream to send
     */
    public CliViewUpdate(BufferedReader console) {
        if(console == null) {
            this.console = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        } else {
            this.console = console;
        }
    }

    /***
     * Standard constructor, just init the console variable
     */
    public CliViewUpdate() {
        this.console = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
    }

    /***
     * sets the input parser
     * @param inputParser the class that sends the message to the server
     */
    public void setInputParser(InputParser inputParser) {
        this.inputParser = inputParser;
    }

    /***
     * shows at screen that all players are ready
     * @param startMatch the message from the server that all players are ready
     */
    @Override
    public void startMatch(StartMatch startMatch){
        System.out.println("All players are ready. Game on!");
    }

    /***
     * ask to the player where his workers should be placed
     * @param placeReady the message from the server that says the player needs to place his workers
     */
    @Override
    public void setWorker(PlaceReady placeReady) {

        int x1;
        int y1;
        boolean waiting = true;

        do {
            waiting = true;
            System.out.println("Chose your first worker coordinates:");

            String W1 = "A1";
            while(waiting) {
                try {
                    W1 = console.readLine();
                    if(W1.toUpperCase().charAt(0) >= 'A' && W1.toUpperCase().charAt(0) <= 'E') {
                        if(W1.toUpperCase().charAt(1) >= '0' && W1.toUpperCase().charAt(1) <= '4') {
                            waiting = false;
                        }
                    }
                    if(waiting) {
                        System.out.println("Input incorrect, retry");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            x1 = Character.getNumericValue(W1.toUpperCase().charAt(0)) - 10; // TODO: Controllare se é sempre vero
            y1 = Character.getNumericValue(W1.toUpperCase().charAt(1));
        } while (lastMap[x1][y1].getWorker()!=null);

        int x2;
        int y2;
        do {
            System.out.println("Chose your second worker coordinates:");

            String W2 = "A1";
            waiting = true;
            while(waiting) {
                try {
                    W2 = console.readLine();
                    if(W2.toUpperCase().charAt(0) >= 'A' && W2.toUpperCase().charAt(0) <= 'E') {
                        if(W2.toUpperCase().charAt(1) >= '0' && W2.toUpperCase().charAt(1) <= '4') {
                            waiting = false;
                        }
                    }
                    if(waiting) {
                        System.out.println("Input incorrect, retry");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            x2 = Character.getNumericValue(W2.toUpperCase().charAt(0)) - 10;
            y2 = Character.getNumericValue(W2.toUpperCase().charAt(1));
        } while (lastMap[x2][y2].getWorker()!=null || ((x2 == x1) && (y2 == y1)));
        inputParser.selectWorkers(x1, y1, x2, y2);
    }

    /***
     * ask to the player which worker needs to be move and where
     * @param movelist the message that says that the player needs to move
     */
    @Override
    public void moveUpdate(MoveList movelist) {

        boolean moving = true;


        while (moving) {
            String chosenMove = "";
            System.out.println("Which Worker do you want to move? 1 or 2");
            String chosenWorker = null;
            boolean waiting = true;

            while(waiting) {
                try {
                    chosenWorker = console.readLine();
                    if(chosenWorker.equals("1") || chosenWorker.equals("2")) {
                        waiting = false;
                        break;
                    }
                    if(waiting) {
                        System.out.println("Entry incorrect, enter 1 or 2");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(chosenWorker.equals("1")) {
                System.out.println("Available moves:");

                for (Direction dir : movelist.getMoveList1()) {
                    System.out.println(dir.toString());
                }
                System.out.println("Pick a Move from above");
                try {
                    chosenMove = console.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Direction dir : movelist.getMoveList1()) {
                    if (dir.toString().equals(chosenMove.toUpperCase())) {
                        inputParser.selectMove(chosenWorker, chosenMove);
                        return;
                    }
                }
            }

            if(chosenWorker.equals("2")) {
                System.out.println("Available moves:");

                for (Direction dir : movelist.getMoveList2()) {
                    System.out.println(dir);
                }
                System.out.println("Pick a Move from above");
                try {
                    chosenMove = console.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Direction dir : movelist.getMoveList2()) {
                    if (dir.toString().equals(chosenMove.toUpperCase())) {
                        inputParser.selectMove(chosenWorker, chosenMove);
                        return;
                    }
                }
            }
            System.out.println("Entry incorrect, retry");
        }
    }

    /***
     * shows at screen the game map
     * @param gameMapUpdate the message that contains the game map
     */
    @Override
    public void updateMap(GameMapUpdate gameMapUpdate) {

        lastMap= gameMapUpdate.getGameMap();

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                if (i > 0) {
                    if (gameMapUpdate.getGameMap()[i][j].getWorker() != null) {
                        if (gameMapUpdate.getGameMap()[i][j].getWorker().getPlayerColor() == Color.RED) {
                            if (gameMapUpdate.getGameMap()[i][j].getWorker().getID() == 0) {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_RED + "w1");
                                System.out.print(CliColor.RESET + "-");
                            } else {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_RED + "w2");
                                System.out.print(CliColor.RESET + "-");
                            }
                        }
                        if (gameMapUpdate.getGameMap()[i][j].getWorker().getPlayerColor() == Color.GREEN) {
                            if (gameMapUpdate.getGameMap()[i][j].getWorker().getID() == 0) {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_GREEN + "w1");
                                System.out.print(CliColor.RESET + "-");
                            } else {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_GREEN + "w2");
                                System.out.print(CliColor.RESET + "-");
                            }
                        }
                        if (gameMapUpdate.getGameMap()[i][j].getWorker().getPlayerColor() == Color.BLUE) {
                            if (gameMapUpdate.getGameMap()[i][j].getWorker().getID() == 0) {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_BLUE + "w1");
                                System.out.print(CliColor.RESET + "-");
                            } else {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_BLUE + "w2");
                                System.out.print(CliColor.RESET + "-");
                            }
                        }

                    } else {
                        System.out.print("|  -");
                    }

                    if (i == 4) {
                        if (gameMapUpdate.getGameMap()[i][j].getDome()) {
                            System.out.println("D| " + j);
                        } else {
                            Integer b = gameMapUpdate.getGameMap()[i][j].getBuilding();
                            System.out.println(b + "| " + j);
                        }
                    } else {
                        if (gameMapUpdate.getGameMap()[i][j].getDome()) {
                            System.out.print("D");
                        } else {
                            Integer b = gameMapUpdate.getGameMap()[i][j].getBuilding();
                            System.out.print(b);
                        }
                    }

                } else {
                    if (gameMapUpdate.getGameMap()[i][j].getWorker() != null) {
                        if (gameMapUpdate.getGameMap()[i][j].getWorker().getPlayerColor() == Color.RED) {
                            if (gameMapUpdate.getGameMap()[i][j].getWorker().getID() == 0) {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_RED + "w1");
                                System.out.print(CliColor.RESET + "-");
                            } else {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_RED + "w2");
                                System.out.print(CliColor.RESET + "-");
                            }
                        }
                        if (gameMapUpdate.getGameMap()[i][j].getWorker().getPlayerColor() == Color.GREEN) {
                            if (gameMapUpdate.getGameMap()[i][j].getWorker().getID() == 0) {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_GREEN + "w1");
                                System.out.print(CliColor.RESET + "-");
                            } else {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_GREEN + "w2");
                                System.out.print(CliColor.RESET + "-");
                            }
                        }
                        if (gameMapUpdate.getGameMap()[i][j].getWorker().getPlayerColor() == Color.BLUE) {
                            if (gameMapUpdate.getGameMap()[i][j].getWorker().getID() == 0) {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_BLUE + "w1");
                                System.out.print(CliColor.RESET + "-");
                            } else {
                                System.out.print("|");
                                System.out.print(CliColor.ANSI_BLUE + "w2");
                                System.out.print(CliColor.RESET + "-");
                            }
                        }
                    } else {
                        System.out.print("|  -");
                    }

                    if (gameMapUpdate.getGameMap()[i][j].getDome()) {
                        System.out.print("D");
                    } else {
                        Integer b = gameMapUpdate.getGameMap()[i][j].getBuilding();
                        System.out.print(b);
                    }
                }

            }
        }
        System.out.println(" a    b    c    d    e  ");
    }

    /***
     * the message that ask the player to insert his data
     * @param playerDataUpdate the message that asks for the player's data
     */
    @Override
    public void updatePlayerData(PlayerDataUpdate playerDataUpdate) {
        boolean present = false;
        if(playerDataUpdate != null) {
            for(PlayerData player : playerDataArrayList) {
                if (player.getPlayerID().equals(playerDataUpdate.getPlayerID())) {
                    present = true;
                    break;
                }
            }
            if(!present) {
                PlayerData playerData = new PlayerData(playerDataUpdate.getPlayerID(), playerDataUpdate.getPlayerColor(), playerDataUpdate.getPlayOrder());
                playerData.setDivinity(playerDataUpdate.getDivinity());
                playerDataArrayList.add(playerData);
            }
        }

        for(PlayerData player : playerDataArrayList) {
            if(player.getPlayerColor() == Color.RED) {
                System.out.println(CliColor.ANSI_RED + "Nickname: " + player.getPlayerID());
                System.out.println("Play order: " + player.getPlayOrder());
                if (player.getDivinity() == null) {
                    System.out.println("Divinity: null");
                } else {
                    System.out.println("Divinity: " + player.getDivinity());
                }
                if(player.getLost()) {
                    System.out.println("THIS PLAYER HAS LOST");
                }
                System.out.println(CliColor.RESET);
            }

            if(player.getPlayerColor() == Color.GREEN) {
                System.out.println(CliColor.ANSI_GREEN + "Nickname: " + player.getPlayerID());
                System.out.println("Play order: " + player.getPlayOrder());
                if (player.getDivinity() == null) {
                    System.out.println("Divinity: null");
                } else {
                    System.out.println("Divinity: " + player.getDivinity());
                }
                if(player.getLost()) {
                    System.out.println("THIS PLAYER HAS LOST");
                }
                System.out.println(CliColor.RESET);
            }

            if(player.getPlayerColor() == Color.BLUE) {
                System.out.println(CliColor.ANSI_BLUE + "Nickname: " + player.getPlayerID());
                System.out.println("Play order: " + player.getPlayOrder());
                if (player.getDivinity() == null) {
                    System.out.println("Divinity: null");
                } else {
                    System.out.println("Divinity: " + player.getDivinity());
                }
                if(player.getLost()) {
                    System.out.println("THIS PLAYER HAS LOST");
                }
                System.out.println(CliColor.RESET);
            }
        }
    }

    /***
     * asks the player for a nickname
     */
    @Override
    public void selectNick() {
        System.out.println("Select a nickname:");
        String playerID = null;
        try {
            playerID = console.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputParser.selectPlayerData(playerID);
    }

    /***
     * asks the player to chose his divinity
     * @param divinityList the message that asks for the player divinity
     */
    @Override
    public void selectDivinity(DivinityList divinityList) {
        System.out.println("Select a divinity from the following list");
        ArrayList<String> divinities = divinityList.getDivinities();
        for (String string : divinities) {
            System.out.println(string);
        }
        String divinityChosen = null;
        boolean waiting = true;
        while(waiting) {
            try {
                divinityChosen = console.readLine();
                for(String divinity : divinities) {
                    if (divinityChosen.toUpperCase().equals(divinity.toUpperCase())) {
                        waiting = false;
                        break;
                    }
                }
                if(waiting) {
                    System.out.println("Name incorrect, retry:");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inputParser.selectDivinity(divinityChosen.substring(0, 1).toUpperCase() + divinityChosen.toLowerCase().substring(1));
    }

    /***
     * aks the player where he wants to move
     * @param buildList the message that asks for the player move
     */
    @Override
    public void buildUpdate(BuildList buildList) {
        System.out.println("Pick a building move from below:");
        for (Direction dir : buildList.getBuildlist()) {
            System.out.println(dir.toString());
        }
        String chosenMove = null;
        boolean waiting = true;
        while(waiting) {
            try {
                chosenMove = console.readLine();
                for(Direction dir : buildList.getBuildlist()) {
                    if (dir.toString().toUpperCase().equals(chosenMove.toUpperCase())) {
                        waiting = false;
                        break;
                    }
                }
                if(waiting) {
                    System.out.println("Direction incorrect, retry:");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inputParser.selectBuild(chosenMove);
    }

    /***
     * notify the player that he has lost
     * @param matchLost the message that notify the player that he has lost
     */
    @Override
    public void matchLostUpdate(MatchLost matchLost) {

        for (PlayerData playerData : playerDataArrayList) {
            if (matchLost.getMatchLost().equals(playerData.getPlayerID())) {
                if (playerData.getPlayerColor() == Color.RED) {
                    System.out.print(CliColor.ANSI_RED + "" + playerData.getPlayerID());
                }
                if (playerData.getPlayerColor() == Color.BLUE) {
                    System.out.print(CliColor.ANSI_BLUE + "" + playerData.getPlayerID());
                }
                if (playerData.getPlayerColor() == Color.GREEN) {
                    System.out.print(CliColor.ANSI_GREEN + "" + playerData.getPlayerID());
                }
                System.out.println(" has lost!");
                playerData.setLost();
                updatePlayerData(null);
            }
        }
    }

    /***
     * Notify the player that he has won
     * @param matchWon the message that notify the player that he has won
     */
    @Override
    public void matchWonUpdate(MatchWon matchWon) {
        for (PlayerData playerData : playerDataArrayList) {
            if (matchWon.getMatchWon().equals(playerData.getPlayerID())) {
                System.out.println("Congratulations ");
                if (playerData.getPlayerColor() == Color.RED) {
                    System.out.print(CliColor.ANSI_RED + "" + playerData.getPlayerID());
                }
                if (playerData.getPlayerColor() == Color.BLUE) {
                    System.out.print(CliColor.ANSI_BLUE + "" + playerData.getPlayerID());
                }
                if (playerData.getPlayerColor() == Color.GREEN) {
                    System.out.print(CliColor.ANSI_GREEN + "" + playerData.getPlayerID());
                }
                System.out.print(", you have won!");
            }
        }
    }

    /***
     * Asks the player when they are ready
     * @param matchReady the message that asks if players are ready
     */
    @Override
    public void matchReadyUpdate(MatchReady matchReady) {
        boolean waiting = true;
        System.out.println("Write READY when you are ready");
        while (waiting) {
            try {
                String ready = console.readLine();
                if(ready.toUpperCase().equals("READY")) {
                    waiting = false;
                }
                if(waiting){
                    System.out.println("Incorrect answer, write ready");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inputParser.selectReady();
    }

    /***
     * In case prometheus wants to build before the movement
     * @param prometheusBuildList contains two sets of possible moves one for each worker
     */
    @Override
    public void prometheusBuildListUpdate(PrometheusBuildList prometheusBuildList) {
        String chosenBuild;
        System.out.println("Available moves for worker 1:");

        for (Direction dir : prometheusBuildList.getBuildlist1()) {
            System.out.println(dir.toString());
        }

        System.out.println("Available moves for worker 2:");

        for (Direction dir : prometheusBuildList.getBuildlist2()) {
            System.out.println(dir.toString());
        }

        System.out.println("Would you like to build? If so worker 1 or 2? Chose between 1,2 or NO");
        try {
            chosenBuild = console.readLine();
            if (chosenBuild.equals("1")) {
                inputParser.selectPrometheus(0);
            } else if (chosenBuild.equals("2")) {
                inputParser.selectPrometheus(1);
            } else if(chosenBuild.toUpperCase().equals("NO")){
                inputParser.selectPrometheus(null);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void atlasBuildUpdate(AtlasBuildList atlasBuildList) {
        System.out.println("Pick a building move from below:");
        for (Direction dir : atlasBuildList.getBuildlist()) {
            System.out.println(dir.toString());
        }
        String chosenMove = null;
        boolean waiting = true;
        while(waiting) {
            try {
                chosenMove = console.readLine();
                for(Direction dir : atlasBuildList.getBuildlist()) {
                    if (dir.toString().toUpperCase().equals(chosenMove.toUpperCase())) {
                        waiting = false;
                        break;
                    }
                }
                if(waiting) {
                    System.out.println("Direction incorrect, retry:");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Do you want to build a dome here? Yes or no?");
        String domeSelection;
        waiting = true;
        while(waiting) {
            try {
                domeSelection = console.readLine();
                if(domeSelection.toUpperCase().equals("YES")) {
                    inputParser.atlasBuild(chosenMove, true);
                    waiting = false;
                } else if (domeSelection.toUpperCase().equals("NO")) {
                    inputParser.atlasBuild(chosenMove, false);
                    waiting = false;
                }
                if(waiting) {
                    System.out.println("Write yes or no, retry:");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * In case the player already moved and his hero can move again
     * @param singleMoveList a set of possible moves
     */
    @Override
    public void singleMoveUpdate(SingleMoveList singleMoveList) {
        System.out.println("Would you like to move again?");
        String chosenMove = "";

        String workerID = "";
        if(singleMoveList.getWorkerID() == 0) {
            workerID = "1";
        } else if(singleMoveList.getWorkerID() == 1) {
            workerID = "2";
        }

        System.out.println("Available moves:");
        for (Direction dir : singleMoveList.getMoveList()) {
            System.out.println(dir.toString());
        }

        System.out.println("Write YES or NO:");
        boolean waiting = true;
        while(waiting) {
            try {
                String reply = console.readLine();
                if (reply.toUpperCase().equals("NO")) {
                    inputParser.selectMove(workerID, null);
                    return;
                } else if(reply.toUpperCase().equals("YES")) {
                    waiting = false;
                }
                if(waiting) {
                    System.out.println("Incorrect reply, write YES or NO:");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.out.println("Pick a Move from above");
        while(true) {
            try {
                chosenMove = console.readLine();
                for (Direction dir : singleMoveList.getMoveList()) {
                    if (dir.toString().equals(chosenMove.toUpperCase())) {
                        inputParser.selectMove(workerID, chosenMove);
                        return;
                    }
                }
                System.out.println("Incorrect reply, choose a direction from above:");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void buildListFlagUpdate(BuildListFlag buildListFlag) {

        System.out.println("Would you like to build again? If so where?");
        System.out.println("Say 'no' or pick a building move from below:");
        for (Direction dir : buildListFlag.getBuildlist()) {
            System.out.println(dir.toString());
        }
        String chosenMove = "";
        boolean waiting = true;
        while(waiting) {
            try {
                chosenMove = console.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (chosenMove.toUpperCase().equals("NO")) {
                waiting = false;
                inputParser.selectBuild("NO");
            }
            for(Direction dir : buildListFlag.getBuildlist()) {
                if (dir.toString().toUpperCase().equals(chosenMove.toUpperCase())) {
                    waiting = false;
                    inputParser.selectBuild(chosenMove);
                    break;
                }
            }
            if(waiting) {
                System.out.println("Direction incorrect, retry:");
            }
        }
    }

    @Override
    public void endTurn(EndTurnAvaiable endTurnAvaiable) {
        String endStr;
        boolean waiting = true;

        System.out.println("Write END to end the turn");
        while(waiting) {
            try {
                endStr = console.readLine();
                if(endStr.toUpperCase().equals("END")) {
                    waiting = false;
                    break;
                }
                if(!waiting) {
                    System.out.println("Input incorrect, retry");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inputParser.endTurnSignal();
    }
}
