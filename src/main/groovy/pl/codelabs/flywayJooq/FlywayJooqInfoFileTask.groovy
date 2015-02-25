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
 package pl.codelabs.flywayJooq;

import org.flywaydb.core.Flyway
import org.flywaydb.gradle.task.AbstractFlywayTask
import org.flywaydb.core.internal.info.MigrationInfoDumper

/**
 * Gradle Task that generates info file with applied scripts.
 * Task is based on org.flywaydb.gradle.task.AbstractFlywayTask and Flyway utilities that do the hard work. 
 */
public class FlywayJooqInfoFileTask extends AbstractFlywayTask {

    def infoFile = project.file(project.flywayJooq.infoFile)

    FlywayJooqInfoFileTask() {
        group = ''
        description = 'Prints the details and status information about applied migrations to a file.'

        // FIX IT: this shal be removed when/if the following is resolved
        // https://github.com/flyway/flyway/issues/962
        project.afterEvaluate {
            if (isJavaProject()) {
                this.dependsOn.remove(project.tasks.testClasses)
            }
        }
        // END OF FIX IT
    }

    def run(Flyway flyway) {
       // we don't need to scan locations as we need only applied scripts
       flyway.setLocations()
       infoFile.withWriter { out -> out.writeLine(MigrationInfoDumper.dumpToAsciiTable(flyway.info().applied())) }    
    }

    protected boolean isJavaProject() {
        return false
    }
}
