package main.scala.commands

import main.scala.filesystem.State
import main.scala.filesystem.files.{Directory, DirEntry}

abstract class CreateEntry(path: String) extends NavigableCommand {

  override def apply(state: State): State = {
    val absolutePath = getAbsolutePath(state.wd, path)
    val entry = state.root.findDescendant(absolutePath)

    if (entry != null) state.setMessage(s"$path already exists!")
    else if (checkIllegalName(path))
      state.setMessage(s"entry names must not contain invalid characters (${DirEntry.INVALID_CHARS})!")
    else {
      createEntry(state, absolutePath)
    }
  }

  def checkIllegalName(name: String): Boolean =
    name.find(c => DirEntry.INVALID_CHARS.contains(c)).nonEmpty

  def createEntry(state: State, absolutePath: String): State = {
    val entryPath = absolutePath.drop(1).split(Directory.SEPARATOR).toList
    val path =
      if (entryPath.tail.isEmpty) Directory.ROOT_PATH
      else entryPath.init.reduceLeft((segment, result) => result + Directory.SEPARATOR + segment)

    val newEntry = createSpecificEntry(path, entryPath.last)

    val newRoot = updateStructure(state.root, entryPath.init, newEntry)
    if (newRoot == null || newRoot == state.root) state.setMessage(s"$absolutePath: invalid path")
    else {
      val newWd = newRoot.findDescendant(state.wd.path).asDirectory
      State(newRoot, newWd, s"${newEntry.getType} $absolutePath created")
    }
  }

  def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
    if (path.isEmpty) currentDirectory.addEntry(newEntry)
    else {
      val oldEntry = currentDirectory.findEntry(path.head)
      if (oldEntry == null) null
      else
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry.asDirectory, path.tail, newEntry))
    }
  }

  def createSpecificEntry(path: String, name: String): DirEntry
}
