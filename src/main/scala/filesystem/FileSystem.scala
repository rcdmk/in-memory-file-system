package main.scala.filesystem

import main.scala.commands.Command
import main.scala.filesystem.files.Directory
import java.util.Scanner

object FileSystem extends App {
  val root = Directory.ROOT
  var initialState = State(root, root)

  initialState.show

  io.Source.stdin
    .getLines()
    .foldLeft(initialState)((currentState, newLine) => {
      val newState = Command.from(newLine).apply(currentState)
      newState.show

      newState
    })
}
