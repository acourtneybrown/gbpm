package com.notcharlie.gbpm.filehandler

interface FileHandler {
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
