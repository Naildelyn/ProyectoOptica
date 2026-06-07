#!/bin/bash
FX=~/javafx-sdk-21/lib
echo "→ Compilando..."
mkdir -p out
javac --module-path "$FX" --add-modules javafx.controls,javafx.fxml \
    -d out src/module-info.java $(find src -name "*.java")
cp -r src/visionmaster/styles out/visionmaster/styles
echo "→ Ejecutando..."
java --module-path "$FX" --add-modules javafx.controls,javafx.fxml \
    -cp out visionmaster.App
