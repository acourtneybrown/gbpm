package com.notcharlie.gbpm.command

import static com.google.common.base.Preconditions.checkNotNull

import com.beust.jcommander.Parameter
import com.beust.jcommander.converters.FileConverter
import com.echonest.api.v4.EchoNestAPI
import com.notcharlie.gbpm.validator.IsDirectory
import groovy.io.FileType
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
abstract class ListFiles implements Command {
  final String extension

  ListFiles(String extension, List<File> files = []) {
    this.extension = checkNotNull(extension)
    this.files = files
  }

  @Parameter(description = 'files', validateWith = IsDirectory, converter = FileConverter)
  private List<File> files = []

  @Override
  void run(EchoNestAPI en) {
    perform({ File it -> println it })
  }

  public <T> List<T> perform(Closure<T> closure) {
    final result = []
    files.each { File dir ->
      dir.eachFileRecurse(FileType.FILES) { File file ->
        if (file.name.endsWith(extension)) {
          final clone = closure.clone() as Closure
          clone.resolveStrategy = Closure.DELEGATE_ONLY
          clone.delegate = null
          result << clone.call(file)
        }
      }
    }
    return result
  }
}
