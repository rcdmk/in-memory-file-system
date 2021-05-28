package test.scala.filesystem.files

import main.scala.filesystem.files.{Directory, DirEntry, File}
import org.scalatest.matchers.should.Matchers

class DirEntryTest extends org.scalatest.wordspec.AnyWordSpec with Matchers {
  "A root directory" should {
    "compute its correct path" in {
      val entry = Directory.empty("", "")

      entry.path should equal("/")
    }
  }

  "A child directory" should {
    "compute its correct path" in {
      val entry = Directory.empty("/", "a")

      entry.path should equal("/a")
    }
  }

  "A nested directory" should {
    "compute its correct path" in {
      val entry = Directory.empty("/a", "b")

      entry.path should equal("/a/b")
    }
  }
}
