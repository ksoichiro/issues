package com.example.greeting

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GreetingTask extends DefaultTask {
    GreetingTask() {
        // This line is executed by calling 'project.evaluate()',
        // but the coverage is zero.
        println "Evaluating GreetingTask"
    }

    @TaskAction
    void exec() {
        println "Hello, world!"
    }
}
