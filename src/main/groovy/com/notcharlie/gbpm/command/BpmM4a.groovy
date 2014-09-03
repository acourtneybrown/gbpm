package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.echonest.api.v4.EchoNestAPI
import com.notcharlie.gbpm.validator.IsFile
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
@Parameters(commandNames = 'bpm-m4a')
@ToString
class BpmM4a implements Command {
  @Parameter(names = ['--output', '-o'], description = 'Directory to place mp3 file with updated tags, with paths matching the original mp3 file')
  private String outputDir = 'out'

  @Parameter(description = 'file', validateWith = IsFile)
  private String file

  @Override
  void run(EchoNestAPI echoNestAPI) {
    log.info("running ${this.class} for file ${file} -> ${outputDir}")
  }
}
