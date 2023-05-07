package org.tictactoe;

import static org.tictactoe.Player.EMPTY;

import java.util.Arrays;

public final class Board {

  public record Position(int x, int y) {}

  private Player[][] scenario;

  public Board(Player[][] scenario) {
    this.scenario = scenario;
  }

  public static Board cleanBoard() {
    return new Board(
        new Player[][] {
          {EMPTY, EMPTY, EMPTY},
          {EMPTY, EMPTY, EMPTY},
          {EMPTY, EMPTY, EMPTY}
        });
  }

  public Board play(Player play, Position position) {
    scenario[position.x()][position.y()] = play;
    return this;
  }

  public boolean checkWinCondition(Player player) {
    return checkForVerticalWin(player) || checkForHorizontalWin(player) || checkForDiagonalWin(player);
  }

  private boolean checkForVerticalWin(Player player) {
    for (int i = 0; i < 3; i++) {
      if (scenario[0][i] == player && scenario[1][i] == player && scenario[2][i] == player) {
        return true;
      }
    }
    return false;
  }

  private boolean checkForHorizontalWin(Player player) {
    for (int i = 0; i < 3; i++) {
      if (scenario[i][0] == player && scenario[i][1] == player && scenario[i][2] == player) {
        return true;
      }
    }
    return false;
  }

  private boolean checkForDiagonalWin(Player player) {
    return scenario[0][0] == player && scenario[1][1] == player && scenario[2][2] == player
        || scenario[0][2] == player && scenario[1][1] == player && scenario[2][0] == player;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Board board = (Board) o;

    return Arrays.deepEquals(scenario, board.scenario);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(scenario);
  }

  @Override
  public String toString() {
    return "Board{" + "scenario=" + Arrays.deepToString(scenario) + '}';
  }
}
