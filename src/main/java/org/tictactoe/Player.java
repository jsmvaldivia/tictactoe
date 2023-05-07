package org.tictactoe;

enum Player {
  X("X"),
  O("O"),
  EMPTY("E");

  private final String prettyName;

  Player(String prettyName) {
    this.prettyName = prettyName;
  }

  @Override
  public String toString() {
    return prettyName;
  }
}
