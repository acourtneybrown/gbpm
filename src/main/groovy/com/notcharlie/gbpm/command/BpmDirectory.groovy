package com.notcharlie.gbpm.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.echonest.api.v4.EchoNestAPI
import com.google.common.collect.Queues
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.notcharlie.gbpm.validator.IsDirectory
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@CompileStatic
@Parameters(commandNames = 'bpm-dir')
@Slf4j
class BpmDirectory implements Command {
  @Parameter(names = ['--output', '-o'], description = 'Directory to place mp3 file with updated tags, with paths matching the original mp3 file')
  private String outputDir = 'output'

  @Parameter(names = ['--delay', '-d'], description = 'Delay (in milliseconds between calls to the Echo Nest')
  private long delayInMillis = 3500

  @Parameter(description = 'Directories to scan for mp3 & m4a files', validateWith = IsDirectory)
  private List<String> dirs

  private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2, new ThreadFactoryBuilder().setNameFormat('bpm-dir-%s').build())

  @Override
  void run(EchoNestAPI echoNestAPI) {
    final mp3s = listMp3s()
    final m4as = listM4as()
    log.debug('list of files to tag with bpm {}, {}', mp3s, m4as)
    final commandQueue = Queues.newConcurrentLinkedQueue()
    commandQueue.addAll(mp3s.collect { String file -> new BpmMp3(file, outputDir) })
    commandQueue.addAll(m4as.collect { String file -> new BpmM4a(file, outputDir) })

    final future = executorService.scheduleAtFixedRate({ ->
      def command = null
      try {
        command = commandQueue.poll() as Command
        if (command) {
          command.run(echoNestAPI)
        }
      } catch (Exception e) {
        log.warn("Received exception during command ${command}", e)
      }
    } as Runnable, 0, delayInMillis, TimeUnit.MILLISECONDS)

    executorService.scheduleAtFixedRate({ ->
      log.debug 'checking if queue has been processed'
      if (commandQueue.isEmpty()) {
        log.debug 'queue empty, shutting down'
        future.cancel(false)
        executorService.shutdown()
      }
    } as Runnable, 5000, delayInMillis, TimeUnit.MILLISECONDS)
  }

  private List<String> listMp3s() {
    final cmd = new ListMp3s(dirs.collect { String dir -> new File(dir) })
    return cmd.perform({ File file -> file.path })
  }

  private List<String> listM4as() {
    final cmd = new ListM4as(dirs.collect { String dir -> new File(dir) })
    return cmd.perform({ File file -> file.path })
  }
}
