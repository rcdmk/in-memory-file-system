package test.scala.filesystem.files

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import main.scala.filesystem.files.Directory
import main.scala.filesystem.files.File

class DirectoryTest extends AnyWordSpec with Matchers {
  "A directory" when {
    val file = File.empty("/dir/subdir", "file")
    val subDir = new Directory("/dir", "subdir", List(file))
    val dir = new Directory("/", "dir", List(subDir))
    val root = new Directory("", "", List(dir))

    "requested to find an entry" which {
      "is a valid subdirectory name" should {
        "return the subdirectory" in {
          dir.findEntry("subdir") should be(subDir)
        }
      }

      "is a valid file name" should {
        "return the file" in {
          subDir.findEntry("file") should be(file)
        }
      }

      "is an invalid entry name" should {
        "return null" in {
          root.findEntry("non-existing") should be(null)
        }
      }
    }
  }
}
