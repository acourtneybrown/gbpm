package com.notcharlie.gbpm.filehandler

import static com.google.common.base.Preconditions.checkNotNull

import com.mpatric.mp3agic.ID3v24Tag
import com.mpatric.mp3agic.Mp3File
import com.notcharlie.gbpm.command.BpmFile
import groovy.transform.CompileStatic

@CompileStatic
class Mp3FileHandler implements BpmFile.FileHandler {
  static class Mp3MediaFile implements BpmFile.FileHandler.MediaFile {
    private final Mp3File mp3File

    Mp3MediaFile(Mp3File mp3File) {
      this.mp3File = checkNotNull(mp3File)
    }

    @Override
    String getArtist() {
      return mp3File.id3v2Tag?.artist ?: mp3File.id3v1Tag?.artist
    }

    @Override
    String getTitle() {
      return mp3File.id3v2Tag?.title ?: mp3File.id3v1Tag?.title
    }

    @Override
    int getLengthInSeconds() {
      return mp3File.lengthInSeconds
    }

    @Override
    void setBpm(int bpm) {
      if (!mp3File.id3v2Tag) {
        mp3File.id3v2Tag = new ID3v24Tag()
      }
      mp3File.id3v2Tag.BPM = bpm
    }

    @Override
    int getBpm() {
      return mp3File?.id3v2Tag?.BPM ?: 0
    }

    @Override
    void save(String filename) {
      mp3File.save(filename)
    }
  }

  @Override
  BpmFile.FileHandler.MediaFile load(String filename) {
    return new Mp3MediaFile(new Mp3File(filename))
  }
}