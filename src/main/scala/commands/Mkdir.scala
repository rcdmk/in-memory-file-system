package main.scala.commands

import main.scala.filesystem.files.{Directory, DirEntry}

class Mkdir(path: String) extends CreateEntry(path) {

  override def createSpecificEntry(path: String, name: String): DirEntry =
    Directory.empty(path, name)

}
