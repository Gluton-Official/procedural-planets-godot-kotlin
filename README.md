# Procedural Planets

Procedural planet generation using cube mapping and layered noise.
A fragment shader is used to apply color based on the elevation of terrain.

## Setup

### Requirements

- At least JDK 11
- [Godot Kotlin JVM Editor](https://github.com/utopia-rise/godot-kotlin-jvm/releases/tag/0.6.0-3.5.2) version 0.6.0-3.5.2
- Clone this repo
- Platform-specific embedded JVM
  - In the root of the project directory, run:

  ```shell
    jlink --add-modules java.base,java.logging --output jre
  ```
  
  A tiny bit more information can be found in the
  [create embedded JVM step](https://godot-kotl.in/en/stable/contribution/setup/#:~:text=create%20embedded%20JVM%3A)
  in step 4 of the Godot Kotlin/JVM setup guide

### Build

In the root of the project directory, compile the Kotlin code by running:

```shell
./gradlew build
```

## Usage

To open the project in Godot, in the root of the project directory, run:

```shell
<path-to-godot-kotlin-jvm-editor-executable> -d -e
```

Press `F5` to run the project, or you can press the play button in the top right of the window.

You can pan by holding middle mouse or right click and moving your mouse, and zoom by scrolling.

The planet’s properties can be modified and previewed in the PlanetViewer’s inspector’s script variables.

## Developers’ Guide

`PlanetViewer.tscn` runs an auto generated `PlanetViewer.gd` tool file based off of the exported and registered properties
in `Planet.kt`, since Kotlin tools are not supported.
`GeneratePlanetViewer.kt` generates the file, but is not very modular
since it was rushed as it only needed to generate what was needed from `Planet.kt`.
Additionally, not all property types and annotations are implemented.
`PlanetViewer.tscn` is not needed in the scene, as `Planet.kt` itself is sufficient for rendering the planet when running the project.
It simply is used to preview what the planet may look like and provide a sample interface for configuration.

`Generators.tscn` is needed in the scene to run the code gen when the project is loaded.

## Credits

Sebastian Lague's [Procedural Planet Generation Video Playlist](https://youtube.com/playlist?list=PLFt_AvWsXl0cONs3T0By4puYy6GM22ko8)
