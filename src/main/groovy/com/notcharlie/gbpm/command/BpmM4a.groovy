package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameters
import com.notcharlie.gbpm.filehandler.M4aFileHandler
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
@Parameters(commandNames = 'bpm-m4a')
@ToString
class BpmM4a extends BpmFile {
  BpmM4a(String file = null, String outputDir = null) {
    super(new M4aFileHandler(), file, outputDir)
  }
}
