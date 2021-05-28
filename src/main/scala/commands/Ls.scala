package main.scala.commands

import main.scala.filesystem.State
import main.scala.filesystem.files.DirEntry

class Ls(path: String = "") extends NavigableCommand {

  override def apply(state: State): State = {
    val absolutePath = getAbsolutePath(state.wd, path)
    val wd = state.root.findDescendant(absolutePath)

    if (wd == null) state.setMessage(s"$path: no such file or directory ($absolutePath)")
    else {
      val contents =
        if (wd.isFile) List(wd.asFile)
        else wd.asDirectory.contents

      val output = formatOutput(contents.sortBy(entry => entry.name))

      state.setMessage(output)
    }
  }

  def formatOutput(contents: List[DirEntry]): String = {
    if (contents.isEmpty) return ""
    else {
      val entry = contents.head
      entry.name + " [" + entry.getType + "]\n" + formatOutput(contents.tail)
    }
  }
}
