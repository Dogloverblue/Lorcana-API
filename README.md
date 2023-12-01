### Lorcana.api.com
This is the backend code for [Lorcana-api.com](https://lorcana-api.com).
Lorcana-Api is a website for easiley fetching and filtering Lorcana cards.
Several endpoints are avaliable, like /strict/, /fuzzy/, /lists/, and /search/. An in-depth explanation of all of these endpoints is avaliable at [Lorcana-api.com](lorcana-api.com/How-To.html)

## Compilation & Usage

**FatJar**

```bash
./gradlew shadowJar
```

The artifact will be located into backend/build/libs/backend-<version>-all.jar

**NonFat**

```bash
./gradlew distTar distJar
```

The artifacts will create backend/build/distributions/backend-0.0.1.<extension> that then can be extracted
and run as `./backend` and the various jar files alongside it
