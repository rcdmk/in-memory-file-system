package test.scala.commands

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import main.scala.commands.Cd
import main.scala.filesystem.State
import main.scala.filesystem.files.Directory
import main.scala.filesystem.files.DirEntry

class CdTest extends AnyWordSpec with Matchers {
  // setup directory tree as /existing/dir
  var subDir = Directory.empty("/existing", "dir")
  var dir = new Directory("/", "existing", List(subDir))
  val root = new Directory("", "", List(dir))

  val initialState = State(root, root, "some message")

  "A CD command" when {
    "provided with an empty path" should {
      val newState = new Cd("").apply(initialState)

      "clear existing message" in {
        newState.output should be(empty)
      }

      "not change the root and working directory" in {
        newState.root should equal(initialState.root)
        newState.wd should equal(initialState.wd)
      }
    }

    "provided with an absolute path" which {
      "is the root path" should {
        val newState = new Cd(Directory.ROOT_PATH).apply(initialState)

        "clear existing message" in {
          newState.output should be(empty)
        }

        "not change the root and working directory" in {
          newState.root should equal(initialState.root)
          newState.wd should equal(initialState.wd)
        }
      }

      "is an invalid path" should {
        val newState = new Cd("/non/existing").apply(initialState)

        "not change the root and working directory" in {
          newState.root should equal(initialState.root)
          newState.wd should equal(initialState.wd)
        }

        "display an error message" in {
          newState.output should equal("/non/existing: no such directory")
        }
      }

      "is a valid path" should {
        val newState = new Cd("/existing/dir").apply(initialState)

        "clear existing message" in {
          newState.output should be(empty)
        }

        "not change the root directory" in {
          newState.root should equal(initialState.root)
        }

        "change the working directory to the provided one" in {
          newState.wd should be(subDir)
        }
      }
    }

    "provided with a relative path" which {
      "is a single dot" should {
        val newState = new Cd(".").apply(initialState)

        "clear existing message" in {
          newState.output should be(empty)
        }

        "not change the root and working directory" in {
          newState.root should equal(initialState.root)
          newState.wd should equal(initialState.wd)
        }
      }

      "is a double dot" which {
        "is in the root path" should {
          val newState = new Cd("..").apply(initialState)

          "clear existing message" in {
            newState.output should be(empty)
          }

          "not change the root and working directory" in {
            newState.root should equal(initialState.root)
            newState.wd should equal(initialState.wd)
          }
        }

        "is in a directory in the root" should {
          var state = State(root, dir, "some message")
          val newState = new Cd("..").apply(state)

          "clear existing message" in {
            newState.output should be(empty)
          }

          "not change the root directory" in {
            newState.root should equal(state.root)
          }

          "change the working directory to the root" in {
            newState.wd should be(state.root)
          }
        }

        "is in a sub directory" should {
          var state = State(root, subDir, "some message")
          val cd = new Cd("..")
          val newState = cd(state)

          "clear existing message" in {
            newState.output should be(empty)
          }

          "not change the root directory" in {
            newState.root should equal(state.root)
          }

          "change the working directory to the parent directory" in {
            newState.wd should be(dir)
          }
        }
      }
    }
  }
}
