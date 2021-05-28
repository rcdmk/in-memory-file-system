package main.scala.filesystem.files

import scala.annotation.tailrec

abstract class DirEntry(val parentPath: String, val name: String) {

  def path: String = {
    val separator =
      if (parentPath.equals(Directory.ROOT_PATH)) ""
      else Directory.SEPARATOR

    parentPath + separator + name
  }

  def getFolderNamesInPath: List[String] =
    if (path.isEmpty) List()
    else path.drop(1).split(Directory.SEPARATOR).toList.filter(name => name.nonEmpty)

  def collapseRelativeTokens(path: List[String]): List[String] = {
    @tailrec
    def collapseRelativeTokensHelper(path: List[String], result: List[String] = List()): List[String] = {
      if (path.isEmpty) result
      else if (path.head.equals(".")) collapseRelativeTokensHelper(path.tail, result)
      else if (path.head.equals(".."))
        if (result.isEmpty) collapseRelativeTokensHelper(path.tail, result)
        else collapseRelativeTokensHelper(path.tail, result.init)
      else collapseRelativeTokensHelper(path.tail, result :+ path.head)
    }

    collapseRelativeTokensHelper(path)
  }

  def asDirectory: Directory
  def asFile: File
  def isDirectory: Boolean
  def isFile: Boolean
  def getType: String
}

object DirEntry {
  val INVALID_CHARS = "`;{[()]}\\,`'\"?*^´"
}
