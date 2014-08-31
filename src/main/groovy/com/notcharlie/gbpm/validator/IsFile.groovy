package com.notcharlie.gbpm.validator

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException
import groovy.transform.CompileStatic

@CompileStatic
class IsFile implements IParameterValidator {
  @Override
  void validate(String name, String value) throws ParameterException {
    final file = new File(value)
    if (!file.exists() || file.isDirectory()) {
      throw new ParameterException("Parameter ${name} with value ${value} is not a directory")
    }
  }
}
