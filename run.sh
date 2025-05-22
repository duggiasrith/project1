#!/bin/bash

# Compile
echo "Compiling Java files..."
javac -d bin src/*.java test/*.java

echo "Running LibraryManagementSystem..."
java -cp bin LibraryManagementSystem
