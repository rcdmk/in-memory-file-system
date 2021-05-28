package main.scala.commands

import main.scala.filesystem.State

class UnknownCommand(name: String) extends Command {
  override def apply(state: State): State =
    state.setMessage(s"$name: command not found!")
}
