package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MoveList;
import it.polimi.ingsw.PSP18.server.model.*;

import java.io.IOException;

public class CliViewUpdate extends ViewUpdate {

    InputParser inputParser= new InputParser(/*passare socket qui*/);
    java.io.BufferedReader console = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

    @Override
    public void moveUpdate(MoveList movelist) throws IOException {

        Boolean moving = true;
        while(moving){
            System.out.println("Pick a move from below:");
            for (Direction dir : movelist.getMoveList()) {
                System.out.println(dir);
            }
            String chosenMove = console.readLine();

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


}
