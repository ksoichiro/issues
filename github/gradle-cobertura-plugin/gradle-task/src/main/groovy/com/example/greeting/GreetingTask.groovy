package com.example.greeting

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GreetingTask extends DefaultTask {
    @TaskAction
    void exec() {
        println "Hello, world!"
    }
}
