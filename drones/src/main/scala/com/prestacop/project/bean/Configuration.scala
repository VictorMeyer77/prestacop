package com.prestacop.project.bean

import com.google.gson._

class Configuration(var kafkaHost: String,
                    var kafkaPort: String,
                    var kafkaTopic: String,
                    var nbDrones: Int,
                    var sgPath: String,
                    var lePath: String,
                    var sfPath: String){


}
