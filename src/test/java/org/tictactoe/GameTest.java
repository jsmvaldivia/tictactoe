package org.tictactoe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.tictactoe.Board.cleanBoard;
import static org.tictactoe.Player.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.tictactoe.Board.Position;

@DisplayName("Game")
class GameTest {

  private Game game;

  @BeforeEach
  void setUp() {
    game = Game.startGame();
  }

  @Test
  @DisplayName("New board is clean")
  void testNewBoard_BoardIsClear() {
    final Board board = game.newBoard();
    assertThat(board, is(equalTo(cleanBoard())));
  }

  @ParameterizedTest(name = "Play {0} in {1}")
  @MethodSource("providePlays")
  @DisplayName("When play makes a move it changes the board")
  void testPlayer_BoardChanges(Board expectedBoard, Position position) {
    game.play(X, position);
    assertThat(game.getBoard(), is(equalTo(expectedBoard)));
  }

  private static Stream<Arguments> providePlays() {
    return Stream.of(
        Arguments.of(
            new Board(
                new Player[][] {
                  {X, EMPTY, EMPTY},
                  {EMPTY, EMPTY, EMPTY},
                  {EMPTY, EMPTY, EMPTY}
                }),
            new Position(0, 0)),
        Arguments.of(
            new Board(
                new Player[][] {
                  {EMPTY, EMPTY, EMPTY},
                  {X, EMPTY, EMPTY},
                  {EMPTY, EMPTY, EMPTY}
                }),
            new Position(1, 0)),
        Arguments.of(
            new Board(
                new Player[][] {
                  {EMPTY, EMPTY, EMPTY},
                  {EMPTY, EMPTY, EMPTY},
                  {EMPTY, EMPTY, X}
                }),
            new Position(2, 2)));
  }

  @Test
  @DisplayName("Player creates entry in history")
  void testPlayer_createsEntryInHistory() {
    assertThat(game.getHistory().size(), is(equalTo(0)));
    game.play(X, new Position(0, 0));
    assertThat(game.getHistory().size(), is(equalTo(1)));
  }

  @Test
  @DisplayName("Same player twice throws error")
  void testPlayer_throwsErrorWhenSamePlayer() {
    game.play(X, new Position(0, 0));
    assertThrows(IllegalArgumentException.class, () -> game.play(X, new Position(0, 0)));
  }

  @Test
  @DisplayName("Different player does not throw error")
  void testPlayer_differentPlayOk() {
    game.play(X, new Position(0, 0));
    game.play(O, new Position(0, 1));
    assertThat(game.getHistory().size(), is(equalTo(2)));
  }

  @Test
  @DisplayName("Different player same location throws error")
  void testPlayer_differentPlayerSameLocationError() {
    game.play(X, new Position(0, 0));
    assertThrows(IllegalArgumentException.class, () -> game.play(O, new Position(0, 0)));
  }

  @Test
  @DisplayName("Game is not finished when started")
  void testGame_notFinishedWhenStarted() {
    assertThat(game.isFinished(), is(false));
  }

  @Test
  @DisplayName("Cannot play when game is finished")
  void testGame_cannotPlayWhenFinished() {
    game.play(X, new Position(0, 0));
    game.play(O, new Position(0, 1));
    game.play(X, new Position(1, 0));
    game.play(O, new Position(1, 1));
    game.play(X, new Position(2, 0));
    assertThrows(IllegalStateException.class, () -> game.play(O, new Position(2, 1)));
  }

  @Test
  @DisplayName("Game is finished when there is a winner vertically")
  void testGame_finishedWhenWinnerVertically() {
    game.play(X, new Position(0, 0));
    game.play(O, new Position(0, 1));
    game.play(X, new Position(1, 0));
    game.play(O, new Position(1, 1));
    game.play(X, new Position(2, 0));
    assertThat(game.isFinished(), is(true));
    assertThat(game.getWinner(), is(equalTo(X)));
    assertThat(game.isDraw(), is(false));
  }

  @Test
  @DisplayName("Game is finished when there is a winner horizontally")
  void testGame_finishedWhenWinnerHorizontally() {
    game.play(X, new Position(0, 0));
    game.play(O, new Position(1, 0));
    game.play(X, new Position(0, 1));
    game.play(O, new Position(1, 1));
    game.play(X, new Position(0, 2));
    assertThat(game.isFinished(), is(true));
    assertThat(game.getWinner(), is(equalTo(X)));
    assertThat(game.isDraw(), is(false));
  }

  @Test
  @DisplayName("Game is finished when there is a winner in diagonal")
  void testGame_finishedWhenWinnerDiagonal() {
    game.play(X, new Position(0, 0));
    game.play(O, new Position(0, 1));
    game.play(X, new Position(1, 1));
    game.play(O, new Position(1, 2));
    game.play(X, new Position(2, 2));
    assertThat(game.isFinished(), is(true));
    assertThat(game.getWinner(), is(equalTo(X)));
    assertThat(game.isDraw(), is(false));
  }




  @Test
  @DisplayName("Game is finished when there is a draw")
  void testGame_finishedWhenDraw() {
    game.play(X, new Position(0, 1));
    game.play(O, new Position(1, 1));
    game.play(X, new Position(0, 0));
    game.play(O, new Position(0, 2));
    game.play(X, new Position(2, 0));
    game.play(O, new Position(1, 0));
    game.play(X, new Position(1, 2));
    game.play(O, new Position(2, 1));
    game.play(X, new Position(2, 2));
    System.out.println(game.getBoard());
    assertThat(game.isFinished(), is(true));
    assertThat(game.getWinner(), is(nullValue()));
    assertThat(game.isDraw(), is(true));
  }
}

