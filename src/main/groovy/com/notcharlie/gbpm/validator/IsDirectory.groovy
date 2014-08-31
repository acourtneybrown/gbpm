package com.notcharlie.gbpm.validator

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException
import groovy.transform.CompileStatic

@CompileStatic
class IsDirectory implements IParameterValidator {
  @Override
  void validate(String name, String value) throws ParameterException {
    if (!(new File(value).isDirectory())) {
      throw new ParameterException("Parameter ${name} with value ${value} is not a directory")
    }
  }
}
