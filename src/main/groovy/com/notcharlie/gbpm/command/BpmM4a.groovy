package com.notcharlie.gbpm.command

import static com.google.common.base.Preconditions.checkNotNull

import com.coremedia.iso.IsoFile
import com.coremedia.iso.boxes.MetaBox
import com.coremedia.iso.boxes.MovieBox
import com.coremedia.iso.boxes.UserDataBox
import com.coremedia.iso.boxes.apple.AppleItemListBox
import com.googlecode.mp4parser.boxes.apple.AppleArtistBox
import com.googlecode.mp4parser.boxes.apple.AppleNameBox
import com.googlecode.mp4parser.boxes.apple.AppleTempoBox
import com.notcharlie.gbpm.command.BpmFile.FileHandler

import com.beust.jcommander.Parameters
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
@Parameters(commandNames = 'bpm-m4a')
@ToString
class BpmM4a extends BpmFile {
  static class M4aFileHandler implements FileHandler {
    static class M4aMediaFile implements FileHandler.MediaFile {
      final IsoFile isoFile
      final AppleItemListBox appleItem

      M4aMediaFile(IsoFile isoFile) {
        this.isoFile = checkNotNull(isoFile)
        final moov = isoFile.getBoxes(MovieBox).head()
        final udta = moov.getBoxes(UserDataBox, true).head()
        final meta = udta.getBoxes(MetaBox).head()
        appleItem = meta.getBoxes(AppleItemListBox).head()
      }

      @Override
      String getArtist() {
        return appleItem.getBoxes(AppleArtistBox).head()?.value
      }

      @Override
      String getTitle() {
        return appleItem.getBoxes(AppleNameBox).head()?.value
      }

      @Override
      int getLengthInSeconds() {
        def movieHeaderBox = isoFile.getMovieBox().getMovieHeaderBox()
        return (movieHeaderBox.getDuration() / movieHeaderBox.getTimescale()) as int
      }

      @Override
      void setBpm(int bpm) {
        def tempo = appleItem.getBoxes(AppleTempoBox).head()
        if (!tempo) {
          tempo = new AppleTempoBox()
          appleItem.addBox(tempo)
        }
        tempo.setValue(bpm)
        tempo.setIntLength(2)
      }

      @Override
      int getBpm() {
        final tempo = appleItem.getBoxes(AppleTempoBox).head()
        return tempo?.value as Integer ?: 0
      }

      @Override
      void save(String filename) {
        final fc = new FileOutputStream(new File(filename)).getChannel()
        isoFile.writeContainer(fc)
        fc.close()
      }
    }

    @Override
    FileHandler.MediaFile load(String filename) {
      return new M4aMediaFile(new IsoFile(filename))
    }
  }

  BpmM4a(String file = null, String outputDir = null) {
    super(new M4aFileHandler(), file, outputDir)
  }
}
