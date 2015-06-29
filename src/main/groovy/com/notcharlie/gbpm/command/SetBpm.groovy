package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.echonest.api.v4.EchoNestAPI
import com.notcharlie.gbpm.filehandler.FileHandler
import com.notcharlie.gbpm.filehandler.M4aFileHandler
import com.notcharlie.gbpm.filehandler.Mp3FileHandler
import com.notcharlie.gbpm.validator.IsFile
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.io.FilenameUtils

import java.nio.file.Files
import java.nio.file.Paths

@Parameters(commandNames = 'set-bpm')
@CompileStatic
@Slf4j
class SetBpm implements Command {
  private static final Mp3FileHandler MP3_FILE_HANDLER = new Mp3FileHandler()
  static private M4aFileHandler M4A_FILE_HANDLER = new M4aFileHandler()
  private static final Map<String, Closure<FileHandler.MediaFile>> MAKE_MEDIA_FILE = [
      mp3: { String filename -> MP3_FILE_HANDLER.load(filename) },
      m4a: { String filename -> M4A_FILE_HANDLER.load(filename) }
  ]

  @Parameter(description = 'The file (mp3 or m4a) to add the bpm into to', required = true, validateWith = IsFile)
  List<String> filenames

  @Parameter(names = ['-b', '--bpm'], description = 'The BPM value to set', required = true)
  int bpm

  @Parameter(names = ['--output', '-o'], description = 'Directory to place mp3 file with updated tags, with paths matching the original mp3 file')
  private String outputDir = 'output'

  @Override
  void run(EchoNestAPI echoNestAPI) {
    if (filenames.size() != 1) {
      throw new IllegalArgumentException()
    }
    final filename = filenames.head()
    final extension = FilenameUtils.getExtension(filename)?.toLowerCase()
    if (MAKE_MEDIA_FILE.containsKey(extension)) {
      final mediaFile = MAKE_MEDIA_FILE[extension](filename) as FileHandler.MediaFile
      mediaFile.bpm = bpm

      // TODO: refactor to share with BpmFile
      final outputPath = Paths.get(outputDir, filename)
      Files.createDirectories(outputPath.parent)
      if (outputPath.toFile().exists()) {
        log.warn("Output file ${outputPath} already exists.  Skipping")
      } else {
        mediaFile.save(outputPath.toString())
      }
    } else {
      log.warn("Unable to set the BPM for file ${filename} with extension ${extension}")
    }
  }
}
