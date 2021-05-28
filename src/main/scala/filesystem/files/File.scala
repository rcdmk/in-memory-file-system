package main.scala.filesystem.files

class File(override val parentPath: String, override val name: String, val contents: String)
    extends DirEntry(parentPath, name) {

  override def getType: String = "File"

  override def asDirectory: Directory = null
  override def asFile: File = this
  override def isDirectory: Boolean = false
  override def isFile: Boolean = true

  def setContent(content: String): File =
    new File(parentPath, name, content)

  def appendToContent(contentToAppend: String): File =
    if (contents.isEmpty) setContent(contentToAppend)
    else
      setContent(contents + "\n" + contentToAppend)
}

object File {
  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}
