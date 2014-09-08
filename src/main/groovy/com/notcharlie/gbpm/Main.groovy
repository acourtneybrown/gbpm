package com.notcharlie.gbpm

import com.beust.jcommander.JCommander
import com.echonest.api.v4.EchoNestAPI
import com.notcharlie.gbpm.command.BpmDirectory
import com.notcharlie.gbpm.command.BpmM4a
import com.notcharlie.gbpm.command.Command
import com.notcharlie.gbpm.command.ListM4as
import com.notcharlie.gbpm.command.ListMp3s
import com.notcharlie.gbpm.command.PrintId3Info
import com.notcharlie.gbpm.command.BpmMp3
import com.notcharlie.gbpm.command.PrintM4aMetadata
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class Main {
  static void main(String[] args) {
    final params = new MainParameters()
    final jc = new JCommander(params)
    final commands = [ new ListMp3s(), new ListM4as(), new PrintId3Info(), new PrintM4aMetadata(), new BpmMp3(), new BpmM4a(), new BpmDirectory() ] as List<Command>
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
      final props = new Properties()
      props.load(params.tenProperties.newReader())
      final en = new EchoNestAPI(props['api.key'] as String)
      command.run(en)
    } else {
      log.error('unable to execute parsed command {}', jc.parsedCommand)
    }
  }
}
