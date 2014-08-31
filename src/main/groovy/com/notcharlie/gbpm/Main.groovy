package com.notcharlie.gbpm

import com.beust.jcommander.JCommander
import com.notcharlie.gbpm.command.Command
import com.notcharlie.gbpm.command.ListM4as
import com.notcharlie.gbpm.command.ListMp3s
import com.notcharlie.gbpm.command.PrintId3Info
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class Main {
  static void main(String[] args) {
    final params = new MainParameters()
    final jc = new JCommander(params)
    final commands = [ new ListMp3s(), new ListM4as(), new PrintId3Info() ] as List<Command>
    commands.each { Command command ->
      jc.addCommand(command)
    }

    jc.parse(args)

    if (params.help) {
      jc.usage()
      System.exit(1)
    }

    final command = jc.commands[jc.parsedCommand].objects[0] as Command
    if (command) {
      command.run()
    } else {
      log.error('unable to execute parsed command {}', jc.parsedCommand)
    }
  }
}
