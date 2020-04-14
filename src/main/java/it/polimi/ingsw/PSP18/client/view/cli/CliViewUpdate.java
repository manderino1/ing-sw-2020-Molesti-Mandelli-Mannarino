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

    private InputParser inputParser= new InputParser(/*passare socket qui*/);
    java.io.BufferedReader console = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
    private ArrayList<PlayerData> playerDataArrayList = new ArrayList<PlayerData>();

    @Override
    public void moveUpdate(MoveList movelist) throws IOException {

        Boolean moving = true;
        while (moving) {
            String chosenMove = new String();
            System.out.println("Which Worker do you want to move? 1 or 2");
            String chosenWorker = console.readLine();
            switch (chosenWorker) {

                case "1":
                    System.out.println("Available moves:");

                    for (Direction dir : movelist.getMoveList1()) {
                        System.out.println(dir);
                    }
                    System.out.println("Pick a Move from above");
                    chosenMove = console.readLine();

                    for (Direction dir : movelist.getMoveList1()) {
                        if (dir.toString().equals(chosenMove)) {
                            InputParser.selectMove(chosenWorker, chosenMove);
                            moving = false;

                        }

                    }

                case "2":
                    System.out.println("Available moves:");

                    for (Direction dir : movelist.getMoveList2()) {
                        System.out.println(dir);
                    }
                    System.out.println("Pick a Move from above");
                    chosenMove = console.readLine();

                    for (Direction dir : movelist.getMoveList2()) {
                        if (dir.toString().equals(chosenMove)) {
                            InputParser.selectMove(chosenWorker, chosenMove);
                            moving = false;

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
    public void selectNick() throws IOException {
        String playerID = console.readLine();
        inputParser.selectPlayerData(playerID);
    }

    @Override
    public void selectDivinity(DivinityList divinityList) throws IOException {
        ArrayList<String> divinities = divinityList.getDivinities();
        for (String string : divinities) {
            System.out.println(string);
        }
        String divinityChosen = console.readLine();
        inputParser.selectDivinity(divinityChosen);
    }

    @Override
    public void buildUpdate(BuildList buildList) throws IOException {
        System.out.println("Pick a building move from below:");
        for (Direction dir : buildList.getBuildlist()) {
            System.out.println(dir);
        }
        String chosenMove = console.readLine();
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


}
