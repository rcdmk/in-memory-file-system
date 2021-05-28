package main.scala.commands

import main.scala.filesystem.State

class Cat(path: String) extends NavigableCommand {

  override def apply(state: State): State = {
    if (path.isEmpty) state.setMessage("")
    else {
      val absolutePath = getAbsolutePath(state.wd, path)

      val entry = state.root.findDescendant(absolutePath)
      if (entry == null || !entry.isFile)
        state.setMessage(s"$path: no such file")
      else
        state.setMessage(entry.asFile.contents)
    }
  }

}
