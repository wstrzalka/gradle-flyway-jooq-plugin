/**
 Copyright 2015 Wojciech Strzalka

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package pl.codelabs.flywayJooq

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Plugin that registers a {@link FlywayJooqInfoFileTask} and add dependency to it for each jOOQ task.
 * Sets up 'flywayJooq' extension for reconfiguration in gradle script.
 */
class FlywayJooqPlugin implements Plugin<Project> {

    Project project

    void apply(Project project) {
        this.project = project

        project.afterEvaluate {
            if (isJOOQProject() && isFlywayProject()) {
                project.task('flywayJooqInfoFile', type: FlywayJooqInfoFileTask)
                project.tasks.withType(nu.studer.gradle.jooq.JooqTask).each { applyDependencyToJooqTask(it) }
            } else {
                project.logger.warn("###################################################################################################")
                project.logger.warn("flywayJooq: Both Flyway and Jooq plugins needs to be applied in order to get this plugin to work!!!")
                project.logger.warn("###################################################################################################")
            }
        }
    }

    void applyDependencyToJooqTask(Task jooqTask) {
        jooqTask.inputs.file project.flywayJooqInfoFile.infoFile
        jooqTask.dependsOn project.flywayJooqInfoFile
    }

    boolean isJOOQProject() {
        project.plugins.hasPlugin('nu.studer.jooq')
    }

    boolean isFlywayProject() {
        project.plugins.hasPlugin('flyway')
    }
}

