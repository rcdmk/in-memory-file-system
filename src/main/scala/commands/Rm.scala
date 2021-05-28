package main.scala.commands

import main.scala.filesystem.State
import main.scala.filesystem.files.{Directory, DirEntry}
import scala.annotation.tailrec
import scala.collection.mutable.HashMap

class Rm(path: String) extends NavigableCommand {

  override def apply(state: State): State = {
    if (path.isEmpty) state.setMessage("")
    else {
      val root = state.root
      val wd = state.wd

      val absolutePath = getAbsolutePath(wd, path)

      val entry = root.findDescendant(absolutePath)
      if (entry == null) state.setMessage(s"$path: no such file or directory!")
      else if (entry == root) state.setMessage(s"can't remove root path!")
      else if (entry.isDirectory && entry.asDirectory.contents.nonEmpty)
        state.setMessage(s"directory $path is not empty")
      else {
        val tokens = absolutePath.drop(1).split(Directory.SEPARATOR).toList
        val newTokens = entry.collapseRelativeTokens(tokens)
        doRm(state, newTokens)
      }
    }
  }

  def doRm(state: State, tokens: List[String]): State = {
    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {
      if (path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)
        if (nextDirectory == currentDirectory) currentDirectory
        else {
          val newNextDir = rmHelper(nextDirectory.asDirectory, path.tail)
          if (newNextDir == nextDirectory) currentDirectory
          else currentDirectory.replaceEntry(path.head, newNextDir)
        }
      }
    }

    val newRoot = rmHelper(state.root, tokens)
    if (newRoot == state.root)
      state.setMessage(s"$path: no such file or directory!")
    else {
      val newWd = newRoot.findDescendant(state.wd.getFolderNamesInPath).asDirectory
      State(newRoot, newWd)
    }
  }

}
