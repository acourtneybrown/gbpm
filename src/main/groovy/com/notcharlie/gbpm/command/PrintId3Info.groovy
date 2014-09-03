package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.echonest.api.v4.EchoNestAPI
import com.mpatric.mp3agic.Mp3File
import com.notcharlie.gbpm.validator.IsFile
import groovy.transform.CompileStatic

@CompileStatic
@Parameters(commandNames = 'print-id3')
class PrintId3Info implements Command {
  @Parameter(description = 'files', validateWith = IsFile)
  private List<String> files

  @Override
  void run(EchoNestAPI en) {
    files.each { String file ->
      final mp3file = new Mp3File(file)
      System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds")
      System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"))
      System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz")
      System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"))
      System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"))
      System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"))
      System.out.println("Artist: ${mp3file.id3v2Tag.artist}")
      System.out.println("Title: ${mp3file.id3v2Tag.title}")
      System.out.println("BPM: ${mp3file.id3v2Tag.getBPM()}")
    }
  }
}
