package main.scala.commands

import main.scala.filesystem.State
import scala.annotation.tailrec

trait Command extends (State => State) {}

object Command {
  val CAT = "cat"
  val CD = "cd"
  val ECHO = "echo"
  val EXIT = "exit"
  val LS = "ls"
  val MKDIR = "mkdir"
  val PWD = "pwd"
  val RM = "rm"
  val TOUCH = "touch"

  def invalidCommandArgs(command: String, args: Seq[String]): Command = new Command {
    override def apply(state: State): State = {
      val arguments = args.reduceLeft((arg, result) => result + " " + arg)
      state.setMessage(s"Invalid arguments for $command: ($arguments)")
    }
  }

  def imcompleteCommand(command: String): Command = new Command {
    override def apply(state: State): State =
      state.setMessage(s"Command $command missing required arguments")
  }

  def from(input: String): Command = {
    val tokens = input.split(" ")

    if (input.isEmpty || tokens.isEmpty) new Command {
      override def apply(state: State): State = state.setMessage("")
    }
    else {
      tokens match {
        case Array(CAT, args @ _*)  => if (args.isEmpty) imcompleteCommand(CAT) else new Cat(args(0))
        case Array(CD, args @ _*)   => if (args.isEmpty) imcompleteCommand(CD) else new Cd(args(0))
        case Array(ECHO, args @ _*) => if (args.isEmpty) imcompleteCommand(ECHO) else new Echo(args.toArray)
        case Array(EXIT, args @ _*) =>
          if (args.nonEmpty) invalidCommandArgs(EXIT, args) else sys.exit()
        case Array(LS, args @ _*)    => if (args.isEmpty) new Ls() else new Ls(args(0))
        case Array(MKDIR, args @ _*) => if (args.isEmpty) imcompleteCommand(MKDIR) else new Mkdir(args(0))
        case Array(PWD, _*)          => new Pwd()
        case Array(RM, args @ _*)    => if (args.isEmpty) imcompleteCommand(RM) else new Rm(args(0))
        case Array(TOUCH, args @ _*) => if (args.isEmpty) imcompleteCommand(TOUCH) else new Touch(args(0))
        case Array(command, _*)      => new UnknownCommand(command)
      }
    }
  }

}
