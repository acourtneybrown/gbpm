package com.notcharlie.gbpm.command

import static com.google.common.base.Preconditions.checkNotNull

import com.beust.jcommander.Parameter
import com.echonest.api.v4.EchoNestAPI
import com.echonest.api.v4.Song
import com.echonest.api.v4.SongParams
import com.notcharlie.gbpm.validator.IsFile
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

import java.nio.file.Files
import java.nio.file.Paths

@CompileStatic
@Slf4j
@ToString
abstract class BpmFile implements Command {
  static interface FileHandler {
    interface MediaFile {
      String getArtist()
      String getTitle()
      int getLengthInSeconds()
      void setBpm(int bpm)
      int getBpm()
      void save(String filename)
    }

    MediaFile load(String filename)
  }

  @Parameter(names = ['--output', '-o'], description = 'Directory to place mp3 file with updated tags, with paths matching the original mp3 file')
  private String outputDir = 'output'

  @Parameter(description = 'file', validateWith = IsFile)
  private String file

  private final FileHandler handler

  BpmFile(FileHandler handler, String file = null, String outputDir = null) {
    this.handler = checkNotNull(handler)
    this.file = file
    this.outputDir = outputDir
  }

  @Override
  void run(EchoNestAPI en) {
    log.info("running ${this.class} for file ${file} -> ${outputDir}")
    final mediaFile = handler.load(file)
    if (mediaFile.getBpm() != 0) {
      log.info("$file already has BPM info... skipping")
      return
    }

    final p = new SongParams()
    p.artist = mediaFile.artist
    p.title = mediaFile.title
    p.includeAudioSummary()
    final songs = en.searchSongs(p)
    Song bestMatch = null
    def currDelta = Double.MAX_VALUE
    songs.each { Song song ->
      log.trace "checking ${song}"
      if ((Math.abs(song.duration - mediaFile.lengthInSeconds) / mediaFile.lengthInSeconds) < currDelta) {
        bestMatch = song
        log.trace "new best match"
      }
    }

    if (bestMatch) {
      log.trace "best match ${bestMatch}"
      mediaFile.bpm = bestMatch.tempo as Integer
      final outputPath = Paths.get(outputDir, file)
      Files.createDirectories(outputPath.parent)
      if (outputPath.toFile().exists()) {
        log.warn("Output file ${outputPath} already exists.  Skipping")
      } else {
        mediaFile.save(outputPath.toString())
      }
    } else {
      log.warn "no matching song for ${mediaFile.artist} / ${mediaFile.title} @ ${file}"
    }
  }
}
