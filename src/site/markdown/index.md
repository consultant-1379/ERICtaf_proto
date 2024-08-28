# Introduction

The plugin was created to unite different code checking plugins and make them run based on unified rules.

## Usage
Plugin currently executes the following Maven plugins under the hood:

- CheckStyle
- PMD (uses Ericsson rule sets)
- FindBugs (uses TAF extension)
- HuntBugs

Plugins get executed one after one, in the sequence defined above.

```xml

  <plugin>
      <groupId>com.ericsson.cifwk</groupId>
      <artifactId>testware-quality-check-plugin</artifactId>
      <version>${pluginVersion}</version>
      <configuration>
          ...
      </configuration>
      <executions>
          <execution>
              <goals>
                  <goal>check</goal>
              </goals>
          </execution>
      </executions>
  </plugin>

```

# Configuration

Available configuration properties which can be set in ```<configuration>``` section as ```<propertyName>value</propertyName>```

- ```runPmd``` (default = true) - whether to run PMD
- ```minCopyPasteThreshold``` (default = 44) - Copy-paste finding threshold after which PMD will fail

- ```runHuntBugs``` (default = true) - whether to run HuntBugs
- ```runCheckstyle``` (default = true) - whether to run Checkstyle
- ```runFindBugs``` (default = true) - whether to run FindBugs
- ```includeTests``` (default = true) - Include test sources or not
- ```excludedPaths``` - Ant-style selectors that MAY be considered by the underlying code check plugins to ignore the mentioned paths
- ```skip``` (default = false) -  Skip the execution (can be useful in particular child modules of the project)

# Accepted System properties

It is possible to override properties for chosen analysis plugins by specifying system properties via ```-D``` in maven.

## Supported properties

- ```taf.qa-plugin.disableCheckstyle=true``` to disable Checkstyle checks
- ```taf.qa-plugin.disablePmd=true``` to disable PMD checks
- ```taf.qa-plugin.disableHuntBugs=true``` to disable HuntBugs checks
- ```taf.qa-plugin.disableFindBugs=true``` to disable FindBugs checks
