def LOGDIR = System.getProperty('gbpm.log') ?: '.'

appender("CONSOLE", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%level %logger - %msg%n"
  }
}

appender("FILE", FileAppender) {
  file = "${LOGDIR}/gbpm.log"
  append = true
  encoder(PatternLayoutEncoder) {
    pattern = "%level %logger - %msg%n"
  }
}

root(INFO, ["CONSOLE", "FILE"])
