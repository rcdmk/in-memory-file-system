package main.scala.filesystem

import main.scala.filesystem.files.Directory

class State(val root: Directory, val wd: Directory, val output: String) {
  def show: Unit = {
    println(output)
    print(s"${wd.path} ${State.SHELL_TOKEN}")
  }

  def setMessage(message: String): State =
    State(root, wd, message)

  def setWorkingDirectory(wd: Directory): State =
    State(root, wd)

}

object State {
  val SHELL_TOKEN = "$ "

  def apply(root: Directory, wd: Directory, output: String = ""): State = {
    new State(root, wd, output)
  }
}
