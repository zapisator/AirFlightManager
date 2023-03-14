#!/bin/bash
./gradlew clean
./gradlew jar
java -jar ./build/libs/AirFlightManager-1.0-SNAPSHOT.jar -f=./src/test/resources/tickets.json  -a=Tel_Aviv -d=Vladivostok