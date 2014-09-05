package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.echonest.api.v4.EchoNestAPI
import com.echonest.api.v4.Song
import com.echonest.api.v4.SongParams
import com.mpatric.mp3agic.Mp3File
import com.notcharlie.gbpm.validator.IsFile
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

import java.nio.file.Files
import java.nio.file.Paths

@CompileStatic
@Parameters(commandNames = 'bpm-mp3')
@Slf4j
@ToString
class BpmMp3 implements Command {
  @Parameter(names = ['--output', '-o'], description = 'Directory to place mp3 file with updated tags, with paths matching the original mp3 file')
  private String outputDir = 'out'

  @Parameter(description = 'file', validateWith = IsFile)
  private String file

  @Override
  void run(EchoNestAPI en) {
    log.info("running ${this.class} for file ${file} -> ${outputDir}")
    final mp3file = new Mp3File(file)
    final p = new SongParams()
    p.artist = mp3file.id3v2Tag.artist
    p.title = mp3file.id3v2Tag.title
    p.includeAudioSummary()
    final songs = en.searchSongs(p)
    Song bestMatch = null
    def currDelta = Double.MAX_VALUE
    songs.each { Song song ->
      log.trace "checking ${song}"
      if ((Math.abs(song.duration - mp3file.lengthInSeconds) / mp3file.lengthInSeconds) < currDelta) {
        bestMatch = song
        log.trace "new best match"
      }
    }

    if (bestMatch) {
      log.trace "best match ${bestMatch}"
      mp3file.id3v2Tag.BPM = bestMatch.tempo as Integer
      final outputPath = Paths.get(outputDir, file)
      Files.createDirectories(outputPath.parent)
      if (outputPath.toFile().exists()) {
        log.warn("Output file ${outputPath} already exists.  Skipping")
      } else {
        mp3file.save(outputPath.toString())
      }
    } else {
      log.warn "no matching song for ${mp3file.id3v2Tag.artist} / ${mp3file.id3v2Tag.title} @ ${file}"
    }
  }
}
