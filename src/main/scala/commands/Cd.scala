package main.scala.commands

import main.scala.filesystem.State
import main.scala.filesystem.files.Directory

class Cd(path: String) extends NavigableCommand {

  override def apply(state: State): State = {
    if (path.isEmpty) state.setMessage("")
    else {
      val absolutePath = getAbsolutePath(state.wd, path)
      if (absolutePath == Directory.ROOT_PATH) state.setWorkingDirectory(state.root)
      else {
        val destination = state.root.findDescendant(absolutePath)

        if (destination == null || !destination.isDirectory)
          state.setMessage(s"$path: no such directory")
        else
          state.setWorkingDirectory(destination.asDirectory)
      }
    }
  }
}
