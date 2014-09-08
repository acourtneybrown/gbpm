package com.notcharlie.gbpm.command

import static com.google.common.base.Preconditions.checkNotNull

import com.notcharlie.gbpm.command.BpmFile.FileHandler

import com.beust.jcommander.Parameters
import com.mpatric.mp3agic.Mp3File
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

@CompileStatic
@Parameters(commandNames = 'bpm-mp3')
@Slf4j
@ToString
class BpmMp3 extends BpmFile {
  private static class Mp3FileHandler implements FileHandler {
    static class Mp3MediaFile implements FileHandler.MediaFile {
      private final Mp3File mp3File

      Mp3MediaFile(Mp3File mp3File) {
        this.mp3File = checkNotNull(mp3File)
      }

      @Override
      String getArtist() {
        return mp3File.id3v2Tag.artist
      }

      @Override
      String getTitle() {
        return mp3File.id3v2Tag.title
      }

      @Override
      int getLengthInSeconds() {
        return mp3File.lengthInSeconds
      }

      @Override
      void setBpm(int bpm) {
        mp3File.id3v2Tag.BPM = bpm
      }

      @Override
      void save(String filename) {
        mp3File.save(filename)
      }
    }

    @Override
    FileHandler.MediaFile load(String filename) {
      return new Mp3MediaFile(new Mp3File(filename))
    }
  }

  BpmMp3(String file = null, String outputDir = null) {
    super(new Mp3FileHandler(), file, outputDir)
  }
}
