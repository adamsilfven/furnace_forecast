# Building Furnace Forecast

Choose the corresponding loader/version directory, then build it as an independent Gradle project. Do **not** run every loader under a single Gradle invocation.

| Project | Build tool | Gradle | JDK requirements |
|---|---|---|---|
| 1.20.1/fabric | Fabric Loom 1.11.8 | 8.14.3 | Gradle on JDK 21; output targets JDK 17 |
| 1.20.1/forge | ForgeGradle 6.0.44 | 8.12.1 | Gradle on JDK 21 **and** JDK 17 installed for Minecraft toolchain |
| 1.21.1/fabric | Fabric Loom 1.11.8 | 8.14.3 | JDK 21 |
| 1.21.1/forge | ForgeGradle 6.0.44 | 8.12.1 | JDK 21 |
| 1.21.1/neoforge | NeoForge ModDevGradle 2.0.148 | 8.14.3 | JDK 21 |

For Windows, inside the chosen directory run `.\gradlew.bat clean build`; for Fabric ensure `.\gradlew.bat remapJar` has completed. On Linux/macOS run `./gradlew build`. Only install the ordinary project JAR from `build/libs`. Never install a `-sources.jar`.

**Important:** Source control includes the per-project Gradle wrapper and wrapper properties. Keep wrapper versions separate. Initial builds download mod-loader/Minecraft dependencies.

## Test checklist

1. Verify the correct Minecraft version and loader, and Fabric API on Fabric.
2. Open furnace, blast furnace, smoker, and hover the vanilla flame.
3. Check an input of 23 raw iron and one coal in a normal furnace: eight items covered, two more coal needed.
4. Check empty input, sufficient fuel, partly cooked input, live updates, and unknown recipes/fuels.
5. Verify single-player loading and multiplayer client-only installation when available.

Successful compilation and archived JAR structure alone do not establish an in-game pass.
