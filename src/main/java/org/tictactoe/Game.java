package org.tictactoe;

import java.util.List;
import java.util.Stack;

public final class Game {

  public record Move(Player player, Board.Position position) {}

  private final Board board;
  private final Stack<Move> history;

  private boolean finished = false;
  private boolean draw = false;
  private Player winner;

  private Game(Board board) {
    this.board = board;
    history = new Stack<>();
  }

  public Board newBoard() {
    return board.cleanBoard();
  }

  public static Game startGame() {
    return new Game(Board.cleanBoard());
  }

  public void play(Player player, Board.Position position) {
    if (finished) {
      throw new IllegalStateException("Game is finished");
    }
    if (hasPlayedRightBefore(player)) {
      throw new IllegalArgumentException("Player " + player + " cannot go twice in a row");
    }
    if (isPositionPlayedAlready(position)) {
      throw new IllegalArgumentException("Position " + position + " has already been played");
    }

    board.play(player, position);
    history.add(new Move(player, position));
    checkWinner();

  }

  private void checkWinner() {
    if (board.checkWinCondition(Player.X)) {
      finished = true;
      winner = Player.X;
    } else if (board.checkWinCondition(Player.O)) {
      finished = true;
      winner = Player.O;
    } else if (history.size() == 9 && winner == null) {
      finished = true;
      draw = true;
    }
  }

  public Board getBoard() {
    return board;
  }

  public List<Move> getHistory() {
    return history;
  }

  public boolean isFinished() {
    return finished;
  }

  public boolean isDraw() {
    return draw;
  }

  public Player getWinner() {
    return winner;
  }

  private boolean hasPlayedRightBefore(Player play) {
    return !history.isEmpty() && history.peek().player() == play;
  }

  private boolean isPositionPlayedAlready(Board.Position position) {
    return history.stream().anyMatch(move -> move.position().equals(position));
  }
}
