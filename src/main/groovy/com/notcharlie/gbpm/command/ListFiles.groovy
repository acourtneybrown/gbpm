package com.notcharlie.gbpm.command

import static com.google.common.base.Preconditions.checkNotNull

import com.beust.jcommander.Parameter
import com.beust.jcommander.converters.FileConverter
import com.notcharlie.gbpm.validator.IsDirectory
import groovy.io.FileType
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
abstract class ListFiles implements Command {
  final String extension

  ListFiles(String extension) {
    this.extension = checkNotNull(extension)
  }

  @Parameter(description = 'files', validateWith = IsDirectory, converter = FileConverter)
  private List<File> files = []

  @Override
  void run() {
    files.each { File dir ->
      dir.eachFileRecurse(FileType.FILES) { File file ->
        if (file.name.endsWith(extension)) {
          println file
        }
      }
    }
  }
}
