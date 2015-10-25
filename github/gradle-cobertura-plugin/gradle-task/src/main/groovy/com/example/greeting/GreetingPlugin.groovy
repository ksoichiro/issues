package com.example.greeting

import org.gradle.api.Plugin
import org.gradle.api.Project

class GreetingPlugin implements Plugin<Project> {
    @Override
    void apply(Project target) {
        target.task("greeting", type: GreetingTask)
    }
}
