package main.scala.commands

import main.scala.filesystem.files.DirEntry
import main.scala.filesystem.files.File

class Touch(path: String) extends CreateEntry(path) {

  override def createSpecificEntry(path: String, name: String): DirEntry =
    File.empty(path, name)

}
