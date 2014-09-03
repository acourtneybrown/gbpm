package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameters
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
@Parameters(commandNames = 'list-m4as')
class ListM4as extends ListFiles {
  ListM4as(List<File> files = []) {
    super('.m4a', files)
  }
}
