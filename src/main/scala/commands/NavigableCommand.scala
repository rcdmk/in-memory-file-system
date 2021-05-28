package main.scala.commands

import main.scala.filesystem.files.Directory

abstract class NavigableCommand extends Command {

  def getAbsolutePath(wd: Directory, path: String): String =
    if (path.startsWith(Directory.SEPARATOR)) path
    else if (wd.isRoot) wd.path + path
    else wd.path + Directory.SEPARATOR + path

}
