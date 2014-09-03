package com.notcharlie.gbpm

import com.beust.jcommander.Parameter
import com.beust.jcommander.converters.FileConverter
import com.notcharlie.gbpm.validator.IsFile
import groovy.transform.CompileStatic

@CompileStatic
class MainParameters {
  @Parameter(names = '--help', help = true)
  boolean help

  @Parameter(names = '', description = 'Location of the properties file with The Echo Nest keys', converter = FileConverter, validateWith = IsFile)
  File tenProperties = new File('ten.properties')
}
