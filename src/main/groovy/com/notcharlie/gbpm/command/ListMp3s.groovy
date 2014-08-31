package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameters
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
@Parameters(commandNames = 'list-mp3s')
class ListMp3s extends ListFiles {
  ListMp3s() {
    super('.mp3')
  }
}
