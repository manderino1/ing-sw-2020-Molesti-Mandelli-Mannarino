package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;
import it.polimi.ingsw.PSP18.server.model.*;

import java.util.ArrayList;

public class CliViewUpdate extends ViewUpdate {
    private ArrayList<PlayerData> playerDataArrayList = new ArrayList<PlayerData>();

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
}
