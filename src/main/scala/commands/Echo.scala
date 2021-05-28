package main.scala.commands

import main.scala.filesystem.State
import main.scala.filesystem.files.File
import main.scala.filesystem.files.Directory
import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command {

  override def apply(state: State): State = {
    if (args.isEmpty) state.setMessage("")
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val operator = args(args.length - 2)
      val fileName = args.last
      val content = createContent(args.length - 2)

      if (operator.equals(Echo.OUTPUT_OPERATOR))
        doEcho(state, fileName, content, append = false)
      else if (operator.equals(Echo.APPEND_OPERATOR))
        doEcho(state, fileName, content, append = true)
      else
        state.setMessage(createContent(args.length))
    }
  }

  def createContent(topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {
      if (currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")
  }

  def updateDirectoryStructure(
      currentDirectory: Directory,
      path: List[String],
      contents: String,
      append: Boolean
  ): Directory = {
    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val entry = currentDirectory.findEntry(path.head)
      if (entry == null) currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if (entry.isDirectory) currentDirectory
      else if (append) currentDirectory.replaceEntry(path.head, entry.asFile.appendToContent(contents))
      else currentDirectory.replaceEntry(path.head, entry.asFile.setContent(contents))
    } else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = updateDirectoryStructure(nextDirectory, path.tail, contents, append)
      if (newNextDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }

  def doEcho(state: State, fileName: String, contents: String, append: Boolean): State = {
    if (fileName.contains(Directory.SEPARATOR))
      state.setMessage(s"file names must not contain directory separators (${Directory.SEPARATOR})")
    else {
      val newRoot = updateDirectoryStructure(state.root, state.wd.getFolderNamesInPath :+ fileName, contents, append)

      if (newRoot == state.root)
        state.setMessage(s"$fileName: no such file!")
      else
        State(newRoot, newRoot.findDescendant(state.wd.getFolderNamesInPath).asDirectory)
    }
  }
}

object Echo {
  val OUTPUT_OPERATOR = ">"
  val APPEND_OPERATOR = ">>"
}
