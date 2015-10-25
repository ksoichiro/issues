package com.example.greeting

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class GreetingTests {
    @Test
    void greet() {
        Project project = ProjectBuilder.builder().build()
        project.plugins.apply "com.example.greeting"
        project.evaluate()
    }
}
