package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.coremedia.iso.IsoFile
import com.coremedia.iso.boxes.MetaBox
import com.coremedia.iso.boxes.MovieBox
import com.coremedia.iso.boxes.UserDataBox
import com.coremedia.iso.boxes.apple.AppleItemListBox
import com.echonest.api.v4.EchoNestAPI
import com.googlecode.mp4parser.boxes.apple.AppleVariableSignedIntegerBox
import com.googlecode.mp4parser.boxes.apple.Utf8AppleDataBox
import com.notcharlie.gbpm.validator.IsFile
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Parameters(commandNames = 'print-m4a')
@CompileStatic
@Slf4j
class PrintM4aMetadata implements Command {
  @Parameter(description = 'files', validateWith = IsFile)
  private List<String> files

  @Override
  void run(EchoNestAPI en) {
    files.each { String file ->
      final isoFile = new IsoFile(file);
      try {
        final moov = isoFile.getBoxes(MovieBox).head()
        final udta = moov.getBoxes(UserDataBox, true).head()
        final meta = udta.getBoxes(MetaBox).head()
        final appleItem = meta.getBoxes(AppleItemListBox).head()
        appleItem.getBoxes(Utf8AppleDataBox).each { Utf8AppleDataBox box ->
          println "${box.type} -> ${box.value}"
        }
        appleItem.getBoxes(AppleVariableSignedIntegerBox).each { AppleVariableSignedIntegerBox box ->
          println "{$box.type} -> ${box.value}"
        }
      } finally {
        isoFile.close()
      }
    }
  }
}
