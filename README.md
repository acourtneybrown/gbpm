# gBPM - Groovy Beats per Minute

A Groovy command-line tool for querying [The Echo Nest](http://www.echonest.com) to obtain the tempo/beats-per-minute
for a collection of music files (mp3 and m4a).

Currently, this project relies on a few projects that are not available in Maven Central:

* [jEN](https://github.com/johanlindquist/jEN) - the Java API to Echo Nest
* [mp3agic](https://github.com/MrDevJay/mp3agic/tree/additional_frames) - A java library for reading mp3 files and 
reading / manipulating the ID3 tags (ID3v1 and ID3v2.2 through ID3v2.4)

You will need to `mvn install` or `mvn deploy` these artifacts in order to build gBPM.

## Usage

    $ java -jar target/gbpm-0.1.0-0-SNAPSHOT.jar --help
    Usage: <main class> [options] [command] [command options]
      Options:
            --help
    
           Default: false
        --ten, -t
           Location of the properties file with The Echo Nest keys
           Default: ten.properties
      Commands:
        list-mp3s
          Usage: list-mp3s [options] files
    
        list-m4as
          Usage: list-m4as [options] files
    
        print-id3
          Usage: print-id3 [options] files
    
        print-m4a
          Usage: print-m4a [options] files
    
        bpm-mp3
          Usage: bpm-mp3 [options] file
            Options:
              --output, -o
                 Directory to place mp3 file with updated tags, with paths matching
                 the original mp3 file
    
        bpm-m4a
          Usage: bpm-m4a [options] file
            Options:
              --output, -o
                 Directory to place mp3 file with updated tags, with paths matching
                 the original mp3 file
    
        bpm-dir
          Usage: bpm-dir [options] Directories to scan for mp3 & m4a files
            Options:
              --delay, -d
                 Delay (in milliseconds between calls to the Echo Nest
                 Default: 3500
              --output, -o
                 Directory to place mp3 file with updated tags, with paths matching
                 the original mp3 file
                 Default: output
