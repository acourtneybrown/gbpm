package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameters
import com.notcharlie.gbpm.filehandler.Mp3FileHandler
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

@CompileStatic
@Parameters(commandNames = 'bpm-mp3')
@Slf4j
@ToString
class BpmMp3 extends BpmFile {
  BpmMp3(String file = null, String outputDir = null) {
    super(new Mp3FileHandler(), file, outputDir)
  }
}
