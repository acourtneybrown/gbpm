package com.notcharlie.gbpm

import com.beust.jcommander.Parameter
import groovy.transform.CompileStatic

@CompileStatic
class MainParameters {
  @Parameter(names = "--help", help = true)
  boolean help
}
