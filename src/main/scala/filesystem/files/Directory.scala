package main.scala.filesystem.files

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
    extends DirEntry(parentPath, name) {

  def hasEntry(name: String): Boolean =
    findEntry(name) != null

  def findDescendant(path: List[String]): DirEntry = {
    def findDescendantHelper(path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) this
      else if (path.tail.isEmpty) findEntry(path.head)
      else {
        val nextDirectory = findEntry(path.head)
        if (nextDirectory == null || !nextDirectory.isDirectory) null
        else
          nextDirectory.asDirectory.findDescendant(path.tail)
      }
    }

    val collapsedPath = collapseRelativeTokens(path)
    findDescendantHelper(collapsedPath)
  }

  def findDescendant(relativePath: String): DirEntry = {
    if (relativePath.isEmpty) this
    else {
      val path =
        if (relativePath.startsWith(Directory.SEPARATOR)) relativePath.drop(1)
        else relativePath

      val tokens = path.split(Directory.SEPARATOR).toList

      findDescendant(tokens)
    }
  }

  def addEntry(newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents :+ newEntry)

  def removeEntry(entryName: String): Directory =
    if (!hasEntry(entryName)) this
    else
      new Directory(parentPath, name, contents.filter(entry => entry.name != entryName))

  def findEntry(entryName: String): DirEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirEntry]): DirEntry =
      if (contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)

    findEntryHelper(entryName, contents)
  }

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filter(entry => !entry.name.equals(entryName)) :+ newEntry)

  override def asDirectory: Directory = this
  override def asFile: File = null
  override def getType: String = "Directory"
  override def isDirectory: Boolean = true
  override def isFile: Boolean = false

  def isRoot: Boolean = parentPath.isEmpty

}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, List())
}
