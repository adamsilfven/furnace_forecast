# Furnace Forecast

A small, **client-side Minecraft utility** that answers one question: **do you have enough fuel to finish what you're cooking?**

Hover over the **flame** in a furnace, blast furnace, or smoker to see a compact vanilla-style tooltip showing:

- Items remaining in the input stack
- Approximate cooking time for that stack
- How many items the current burning fuel and queued fuel can cook
- How much additional fuel of the currently loaded type is needed

No new blocks, items, HUDs, or changes to vanilla cooking.

## Compatibility

| Minecraft | Loader | Build | In-game test |
|---|---|---|---|
| 1.20.1 | Fabric | Built | **Passed** (user confirmed) |
| 1.20.1 | Forge | Built | **Passed** (user confirmed) |
| 1.21.1 | Fabric | Built | Not yet confirmed |
| 1.21.1 | Forge | Built | Not yet confirmed |
| 1.21.1 | NeoForge | Built | **Passed** (user confirmed) |

All builds are version **0.1.0**. These are separate loader/version-specific JARs, **not** a universal JAR.

## Source tree

Each folder is a self-contained Gradle mod project:

- [Fabric 1.20.1](1.20.1/fabric/)
- [Forge 1.20.1](1.20.1/forge/)
- [Fabric 1.21.1](1.21.1/fabric/)
- [Forge 1.21.1](1.21.1/forge/)
- [NeoForge 1.21.1](1.21.1/neoforge/)

For installation, use **only the JAR built for your exact Minecraft version and loader**. Fabric builds require Fabric API. This is a **client-side display utility**; a dedicated server installation is not intended.

## Build

Use the Gradle wrapper inside the relevant project folder: `./gradlew build` on Linux/macOS or `.\gradlew.bat build` on Windows. Do not install `*-sources.jar` output.

- **Minecraft 1.20.1**: mod bytecode targets Java **17**. The source projects may use Java 21 to run Gradle; ForgeGradle's Minecraft setup also needs a **JDK 17 toolchain** available.
- **Minecraft 1.21.1**: Java **21**.
- Gradle and mod-loader dependencies vary by project; keep the versions specified in each project's build files.
- The projects were built and validated locally by the maintainer; there is not yet a full GitHub-hosted CI verification for every target.

## Scope and limitations

Forecasts are estimates based on the menu's synchronized cooking progress, the current input, and known fuel/recipe values. Input or fuel changes, unusual modded cooking behavior, and external automation can change the outcome. Unknown recipes/fuels are reported without inventing a number.

## Distribution

Compiled JARs are kept separate by loader and Minecraft version. A GitHub repository commit is **not** a release announcement; publish release downloads only after testing the intended target.

## License

[MIT](LICENSE).
