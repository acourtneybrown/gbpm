package com.notcharlie.gbpm.command

import com.echonest.api.v4.EchoNestAPI

interface Command {
  void run(EchoNestAPI echoNestAPI)
}
