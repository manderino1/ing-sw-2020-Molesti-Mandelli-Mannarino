package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.DivinityReceiver;
import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.server.model.*;

import java.io.IOException;

import it.polimi.ingsw.PSP18.server.model.*;

import java.util.ArrayList;

public class CliViewUpdate extends ViewUpdate {

    private InputParser inputParser;
    java.io.BufferedReader console = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
    private ArrayList<PlayerData> playerDataArrayList = new ArrayList<PlayerData>();

    public void setInputParser(InputParser inputParser) {
        this.inputParser = inputParser;
    }

    @Override
    public void startMatch(StartMatch startMatch){
        System.out.println("All players are ready. Game on!");
    }

    @Override
    public void moveUpdate(MoveList movelist) {

        boolean moving = true;


        while (moving) {
            String chosenMove = "";
            System.out.println("Which Worker do you want to move? 1 or 2");
            String chosenWorker = null;
            boolean waiting = true;

            while(waiting) { // TODO: CHECK WARNING
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

            switch (chosenWorker) {
                case "1":
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
                            moving = false;
                            break;
                        }
                    }

                case "2":
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
                            moving = false;
                            break;
                        }

                    }

            }
        }
    }

    @Override
    public void updateMap(GameMapUpdate gameMapUpdate) {

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j > 0) {
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

                    if (j == 4) {
                        if (gameMapUpdate.getGameMap()[i][j].getDome()) {
                            System.out.println("D|");
                        } else {
                            Integer b = gameMapUpdate.getGameMap()[i][j].getBuilding();
                            System.out.println(b + "|");
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
    }

    @Override
    public void updatePlayerData(PlayerDataUpdate playerDataUpdate) {
        if (playerDataArrayList == null) {
            playerDataArrayList.add(playerDataUpdate.getPlayerData());
        }

        if (!playerDataArrayList.contains(playerDataUpdate.getPlayerData()) ){
            playerDataArrayList.add(playerDataUpdate.getPlayerData());
        }

        if(playerDataUpdate.getPlayerData().getPlayerColor() == Color.RED) {
            System.out.println(CliColor.ANSI_RED + "Nickname: " + playerDataUpdate.getPlayerData().getPlayerID());
            System.out.println("Play order: " + playerDataUpdate.getPlayerData().getPlayOrder());
            if (playerDataUpdate.getPlayerData().getDivinity() == null) {
                System.out.println("Divinity: null");
            } else {
                System.out.println("Divinity: " + playerDataUpdate.getPlayerData().getDivinity());
            }
            System.out.println(CliColor.RESET);
        }

        if(playerDataUpdate.getPlayerData().getPlayerColor() == Color.GREEN) {
            System.out.println(CliColor.ANSI_GREEN + "Nickname: " + playerDataUpdate.getPlayerData().getPlayerID());
            System.out.println("Play order: " + playerDataUpdate.getPlayerData().getPlayOrder());
            if (playerDataUpdate.getPlayerData().getDivinity() == null) {
                System.out.println("Divinity: null");
            } else {
                System.out.println("Divinity: " + playerDataUpdate.getPlayerData().getDivinity());
            }
            System.out.println(CliColor.RESET);
        }

        if(playerDataUpdate.getPlayerData().getPlayerColor() == Color.BLUE) {
            System.out.println(CliColor.ANSI_BLUE + "Nickname: " + playerDataUpdate.getPlayerData().getPlayerID());
            System.out.println("Play order: " + playerDataUpdate.getPlayerData().getPlayOrder());
            if (playerDataUpdate.getPlayerData().getDivinity() == null) {
                System.out.println("Divinity: null");
            } else {
                System.out.println("Divinity: " + playerDataUpdate.getPlayerData().getDivinity());
            }
            System.out.println(CliColor.RESET);
        }
    }

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
        inputParser.selectDivinity(divinityChosen);
    }

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

    @Override
    public void matchLostUpdate(MatchLost matchLost) {

        for (PlayerData playerData : playerDataArrayList) {
            if (matchLost.getMatchLost().equals(playerData.getPlayerID())) {
                System.out.println("Sorry ");
                if (playerData.getPlayerColor() == Color.RED) {
                    System.out.print(CliColor.ANSI_RED + "" + playerData.getPlayerID());
                }
                if (playerData.getPlayerColor() == Color.BLUE) {
                    System.out.print(CliColor.ANSI_BLUE + "" + playerData.getPlayerID());
                }
                if (playerData.getPlayerColor() == Color.GREEN) {
                    System.out.print(CliColor.ANSI_GREEN + "" + playerData.getPlayerID());
                }
                System.out.print(", you have lost!");
                playerDataArrayList.remove(playerData);
            }
        }
    }

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inputParser.selectReady();
    }
}
